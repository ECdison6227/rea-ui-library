import fs from 'node:fs';
import path from 'node:path';
import http from 'node:http';
import crypto from 'node:crypto';
import {spawn} from 'node:child_process';
import {fileURLToPath} from 'node:url';
import {setTimeout as delay} from 'node:timers/promises';

// Offline local-video analysis only: no ADB, default browser profile or downloads.
const self=fileURLToPath(import.meta.url),root=path.resolve(path.dirname(self),'..');
const maxSamples=1800,maxMediaSeconds=20;
const sha=b=>crypto.createHash('sha256').update(b).digest('hex');
function boxes(buffer,start,end){
 const result=[];
 for(let at=start;at<end;){
  if(at+8>end)throw Error('Truncated MP4 box header');
  let size=buffer.readUInt32BE(at),header=8;const type=buffer.toString('ascii',at+4,at+8);
  if(size===1){if(at+16>end)throw Error('Truncated large MP4 box');size=Number(buffer.readBigUInt64BE(at+8));header=16;}
  if(size===0)size=end-at;
  if(!Number.isSafeInteger(size)||size<header||at+size>end)throw Error('Invalid MP4 box size');
  result.push({type,start:at,payload:at+header,end:at+size});at+=size;
 }
 return result;
}
function one(list,type,optional=false){const found=list.filter(b=>b.type===type);if(found.length!==1){if(optional&&found.length===0)return null;throw Error('Expected one '+type+' box');}return found[0];}
function u32table(buffer,box,stride){const count=buffer.readUInt32BE(box.payload+4);if(count>maxSamples||box.payload+8+count*stride>box.end)throw Error('Invalid/bounded table '+box.type);return count;}

/** Strict nonfragmented, unencrypted AVC MP4 demux. Unsupported carriers fail. */
export function parseAvcMp4(buffer){
 if(buffer.length>32*1024*1024)throw Error('Input exceeds bounded 32 MiB motion fixture');
 const top=boxes(buffer,0,buffer.length);if(top.some(b=>b.type==='moof'))throw Error('Fragmented MP4 is unsupported; no guessed PTS');
 one(top,'ftyp');const moov=one(top,'moov'),mdats=top.filter(b=>b.type==='mdat');if(!mdats.length)throw Error('No media payload');
 const movie=boxes(buffer,moov.payload,moov.end),videoTracks=[];
 for(const trak of movie.filter(b=>b.type==='trak')){
  const track=boxes(buffer,trak.payload,trak.end),mdia=one(track,'mdia'),media=boxes(buffer,mdia.payload,mdia.end),hdlr=one(media,'hdlr');
  if(buffer.toString('ascii',hdlr.payload+8,hdlr.payload+12)==='vide')videoTracks.push({track,media});
 }
 if(videoTracks.length!==1)throw Error('Require exactly one video track');
 const {track,media}=videoTracks[0],mdhd=one(media,'mdhd'),version=buffer[mdhd.payload];
 if(![0,1].includes(version))throw Error('Unknown mdhd version');
 const timescale=buffer.readUInt32BE(mdhd.payload+(version===1?20:12));if(!timescale)throw Error('Invalid media time base');
 const minf=one(media,'minf'),stbl=one(boxes(buffer,minf.payload,minf.end),'stbl'),tables=boxes(buffer,stbl.payload,stbl.end),stsd=one(tables,'stsd');
 if(buffer.readUInt32BE(stsd.payload+4)!==1)throw Error('Multiple sample descriptions unsupported');
 const entry=one(boxes(buffer,stsd.payload+8,stsd.end),'avc1');
 if(entry.payload+78>entry.end)throw Error('Truncated AVC visual entry');
 const width=buffer.readUInt16BE(entry.payload+24),height=buffer.readUInt16BE(entry.payload+26);
 if(!width||!height||width*height>4096*4096)throw Error('Invalid/bounded video dimensions');
 const avcC=one(boxes(buffer,entry.payload+78,entry.end),'avcC'),description=buffer.subarray(avcC.payload,avcC.end);
 if(description.length<7||description[0]!==1)throw Error('Invalid AVC configuration');
 const codec='avc1.'+[...description.subarray(1,4)].map(n=>n.toString(16).padStart(2,'0')).join('');
 const stsz=one(tables,'stsz'),fixedSize=buffer.readUInt32BE(stsz.payload+4),count=buffer.readUInt32BE(stsz.payload+8);
 if(!count||count>maxSamples||(!fixedSize&&stsz.payload+12+count*4>stsz.end))throw Error('Invalid/bounded sample count');
 const sizes=Array.from({length:count},(_,i)=>fixedSize||buffer.readUInt32BE(stsz.payload+12+i*4));
 const stts=one(tables,'stts'),timingCount=u32table(buffer,stts,8),durations=[];
 for(let i=0;i<timingCount;i++){const p=stts.payload+8+i*8,n=buffer.readUInt32BE(p),delta=buffer.readUInt32BE(p+4);if(!delta||durations.length+n>count)throw Error('Invalid decode-time table');for(let j=0;j<n;j++)durations.push(delta);}
 if(durations.length!==count)throw Error('Sample/timing count differs');
 const ctts=one(tables,'ctts',true),composition=Array(count).fill(0);
 if(ctts){const v=buffer[ctts.payload];if(![0,1].includes(v))throw Error('Unknown composition-time version');const n=u32table(buffer,ctts,8);let k=0;for(let i=0;i<n;i++){const p=ctts.payload+8+i*8,c=buffer.readUInt32BE(p),offset=v===1?buffer.readInt32BE(p+4):buffer.readUInt32BE(p+4);if(k+c>count)throw Error('Composition count overflow');for(let j=0;j<c;j++)composition[k++]=offset;}if(k!==count)throw Error('Composition count differs');}
 const stss=one(tables,'stss',true),keys=new Set();
 if(stss){const n=u32table(buffer,stss,4);for(let i=0;i<n;i++){const number=buffer.readUInt32BE(stss.payload+8+i*4);if(!number||number>count)throw Error('Invalid sync sample');keys.add(number);}}else for(let i=1;i<=count;i++)keys.add(i);
 if(!keys.has(1))throw Error('First AVC sample must be an independent sync sample');
 const stsc=one(tables,'stsc'),mapCount=u32table(buffer,stsc,12),mapping=[];
 for(let i=0;i<mapCount;i++){const p=stsc.payload+8+i*12,first=buffer.readUInt32BE(p),per=buffer.readUInt32BE(p+4),descriptionIndex=buffer.readUInt32BE(p+8);if(!per||per>count||descriptionIndex!==1||(i===0?first!==1:first<=mapping[i-1].first))throw Error('Invalid sample/chunk map');mapping.push({first,per});}
 if(!mapping.length)throw Error('Empty sample/chunk map');
 const stco=one(tables,'stco',true),co64=one(tables,'co64',true);if(!!stco===!!co64)throw Error('Require one chunk-offset representation');
 const offsets=stco||co64,stride=stco?4:8,chunkCount=u32table(buffer,offsets,stride),sampleOffsets=[];
 let m=0;
 for(let c=1;c<=chunkCount;c++){
  while(m+1<mapping.length&&mapping[m+1].first<=c)m++;
  let at=stco?buffer.readUInt32BE(offsets.payload+8+(c-1)*4):Number(buffer.readBigUInt64BE(offsets.payload+8+(c-1)*8));
  for(let j=0;j<mapping[m].per;j++){
   const size=sizes[sampleOffsets.length];if(!size||!Number.isSafeInteger(at)||!mdats.some(b=>at>=b.payload&&at+size<=b.end))throw Error('Sample outside mdat payload');sampleOffsets.push(at);at+=size;
  }
 }
 if(sampleOffsets.length!==count)throw Error('Chunk/sample count differs');
 let dts=0;
 const samples=sampleOffsets.map((offset,i)=>{const pts=dts+composition[i],sample={number:i+1,offset,size:sizes[i],dtsTicks:dts,ptsTicks:pts,durationTicks:durations[i],timeBase:{numerator:1,denominator:timescale},timestampMicroseconds:Math.round(pts*1000000/timescale),durationMicroseconds:Math.round(durations[i]*1000000/timescale),key:keys.has(i+1)};dts+=durations[i];return sample;});
 if(dts/timescale>maxMediaSeconds)throw Error('Video exceeds bounded twenty-second decoder input');
 const edts=one(track,'edts',true),edits=[];
 if(edts){const elst=one(boxes(buffer,edts.payload,edts.end),'elst',true);if(elst){const v=buffer[elst.payload],n=buffer.readUInt32BE(elst.payload+4),stride=v===1?20:12;if(![0,1].includes(v)||n>16||elst.payload+8+n*stride>elst.end)throw Error('Invalid edit list');for(let i=0;i<n;i++){const p=elst.payload+8+i*stride;edits.push({segmentDurationMovieTicks:v===1?Number(buffer.readBigUInt64BE(p)):buffer.readUInt32BE(p),mediaTimeTicks:v===1?Number(buffer.readBigInt64BE(p+8)):buffer.readInt32BE(p+4)});}}}
 return {codec,width,height,timescale,mediaDurationSeconds:dts/timescale,sampleCount:count,descriptionBase64:description.toString('base64'),samples,editList:edits,timestampPolicy:'PTS are video-media-track ticks including ctts, not adjusted by edit list or synchronized to host actions. WebCodecs receives rounded microseconds; original integer ticks are retained.',displayTransformPolicy:'Raw codec-frame dimensions; MP4 track-matrix and pixel-aspect transformations are not applied. Confirm orientation and dimensions before source screenshot comparison.'};
}

async function main(){
 const args=process.argv.slice(2),opts={};for(let i=0;i<args.length;i++){if(['--check','--help'].includes(args[i]))opts[args[i]]=true;else if(['--input','--out','--browser'].includes(args[i])&&i+1<args.length)opts[args[i]]=args[++i];else throw Error('Invalid option; use --help');}
 if(opts['--help']){console.log('Offline browser AVC decoder; no ADB or installation.\n--check: isolated Chrome/Edge H.264 capability and synthetic encode/decode check.\n--input evidence/runtime/run03-20261016/motion/ID/screenrecord.mp4 [--out NEW_SUBDIRECTORY] [--browser EXISTING_EXE]\nSupports one nonfragmented unencrypted avc1 MP4 video track, <=32 MiB, <=1800 samples, <=20 s.\nPreserves original MP4 DTS/PTS integer ticks and emits lossless decoded PNGs.\nAnimation duration/easing/interruption remain unverified; frame extraction is not animation analysis.');return;}
 const motionRoot=path.join(root,'evidence/runtime/run03-20261016/motion');let input,output,bytes,index;
 if(!opts['--check']){
  if(!opts['--input'])throw Error('Specify --check or --input');input=path.resolve(opts['--input']);
  if(!input.startsWith(motionRoot+path.sep)||!input.endsWith('.mp4'))throw Error('Input must be a retained Run03 motion MP4');
  bytes=fs.readFileSync(input);index=parseAvcMp4(bytes);output=path.resolve(opts['--out']||path.join(path.dirname(input),'browser-decoded'));
  if(!output.startsWith(path.dirname(input)+path.sep)||fs.existsSync(output))throw Error('Use a fresh decoder output subdirectory beside this motion input');
 }
 const candidates=[opts['--browser'],'C:/Program Files/Google/Chrome/Application/chrome.exe','C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe'].filter(Boolean),browser=candidates.find(p=>fs.existsSync(p));if(!browser)throw Error('No existing Chrome/Edge executable in the declared paths');
 const workspace=path.resolve(root,'../..'),privateRoot=path.join(workspace,'.runtime/motion-browser'),profile=path.join(privateRoot,crypto.randomUUID());fs.mkdirSync(profile,{recursive:true});
 const frames=[],pending=new Map();let child,socket,server,counter=0,sessionId,fatalFrameError=null;
 const report={schemaVersion:1,mode:opts['--check']?'capability-and-synthetic-check':'retained-video-decode',adbCalled:false,installationPerformed:false,userBrowserProfileAccessed:false,browser,animationDuration:null,easing:null,interruptionVerified:false};
 try{
  server=http.createServer((request,response)=>{if(request.url==='/video'&&bytes){response.setHeader('Content-Type','video/mp4');response.end(bytes);}else if(request.url==='/index'&&index){response.setHeader('Content-Type','application/json');response.end(JSON.stringify(index));}else{response.setHeader('Content-Type','text/html');response.end('<!doctype html><meta charset="utf-8"><title>Independent offline motion decoder</title><canvas id="pixels"></canvas>');}});
  await new Promise(resolve=>server.listen(0,'127.0.0.1',resolve));const port=server.address().port;
  child=spawn(browser,['--headless=new','--disable-gpu','--disable-background-networking','--disable-component-update','--disable-sync','--disable-extensions','--no-first-run','--no-default-browser-check','--mute-audio','--remote-debugging-address=127.0.0.1','--remote-debugging-port=0','--user-data-dir='+profile,'about:blank'],{windowsHide:true,stdio:'ignore'});
  let launchError=null;child.once('error',e=>launchError=e);
  const active=path.join(profile,'DevToolsActivePort');for(let n=0;n<100&&!fs.existsSync(active);n++){if(launchError)throw launchError;await delay(100);}if(!fs.existsSync(active))throw Error('Isolated browser did not expose its own DevTools endpoint');
  const [debugPort,endpoint]=fs.readFileSync(active,'utf8').trim().split(/[\r\n]+/);socket=new WebSocket('ws://127.0.0.1:'+debugPort+endpoint);
  await new Promise((resolve,reject)=>{socket.addEventListener('open',resolve,{once:true});socket.addEventListener('error',reject,{once:true});});
  function call(method,params={},session){return new Promise((resolve,reject)=>{const id=++counter,timeout=setTimeout(()=>{pending.delete(id);reject(Error('CDP timeout: '+method));},45000);pending.set(id,{resolve,reject,timeout});socket.send(JSON.stringify({id,method,params,...(session?{sessionId:session}:{})}));});}
  socket.addEventListener('message',message=>{
   const data=JSON.parse(message.data);
   if(data.id){const entry=pending.get(data.id);if(!entry)return;clearTimeout(entry.timeout);pending.delete(data.id);if(data.error)entry.reject(Error(data.error.message));else entry.resolve(data.result);return;}
   if(data.method==='Runtime.bindingCalled'&&data.params.name==='saveDecodedFrame')try{
    const frame=JSON.parse(data.params.payload),png=Buffer.from(frame.pngBase64,'base64');
    if(!output||frame.number!==frames.length+1||frames.length>=maxSamples||png.subarray(0,8).toString('hex')!=='89504e470d0a1a0a')throw Error('Invalid decoded-frame stream');
    const name='frame-'+String(frame.number).padStart(4,'0')+'.png';fs.writeFileSync(path.join(output,name),png,{flag:'wx'});delete frame.pngBase64;frames.push({...frame,file:name,bytes:png.length,sha256:sha(png)});
   }catch(error){fatalFrameError=String(error.message);}
  });
  report.browserVersion=await call('Browser.getVersion');const target=await call('Target.createTarget',{url:'http://127.0.0.1:'+port+'/'});sessionId=(await call('Target.attachToTarget',{targetId:target.targetId,flatten:true})).sessionId;
  await call('Runtime.enable',{},sessionId);await call('Runtime.addBinding',{name:'saveDecodedFrame'},sessionId);
  async function evaluate(expression){const result=await call('Runtime.evaluate',{expression,awaitPromise:true,returnByValue:true},sessionId);if(result.exceptionDetails)throw Error(result.exceptionDetails.exception?.description||result.exceptionDetails.text);return result.result.value;}
  if(opts['--check']){
   report.capabilities=await evaluate(`(async()=>{
    const result={secureContext:isSecureContext,videoDecoder:typeof VideoDecoder,videoEncoder:typeof VideoEncoder,htmlH264:document.createElement('video').canPlayType('video/mp4; codecs="avc1.42E01E"')};
    if(typeof VideoDecoder!=='function')return result;
    result.h264DecoderConfig=(await VideoDecoder.isConfigSupported({codec:'avc1.42001e',codedWidth:64,codedHeight:64,hardwareAcceleration:'prefer-software'})).supported;
    if(typeof VideoEncoder!=='function'){result.syntheticRoundTrip='encoder-unavailable';return result;}
    const config={codec:'avc1.42001e',width:64,height:64,bitrate:100000,framerate:10,avc:{format:'avc'},hardwareAcceleration:'prefer-software'};
    if(!(await VideoEncoder.isConfigSupported(config)).supported){result.syntheticRoundTrip='encoder-config-unavailable';return result;}
    const chunks=[];let decoderConfig,codecError;
    const encoder=new VideoEncoder({output:(chunk,metadata)=>{chunks.push(chunk);if(metadata.decoderConfig)decoderConfig=metadata.decoderConfig;},error:e=>codecError=String(e)});encoder.configure(config);
    const canvas=document.getElementById('pixels');canvas.width=64;canvas.height=64;const ctx=canvas.getContext('2d');ctx.fillStyle='rgb(0,255,0)';ctx.fillRect(0,0,64,64);
    for(let i=0;i<3;i++){const frame=new VideoFrame(canvas,{timestamp:i*100000,duration:100000});encoder.encode(frame,{keyFrame:i===0});frame.close();}await encoder.flush();encoder.close();if(codecError)throw Error(codecError);
    const timestamps=[];let green=true;const decoder=new VideoDecoder({output:frame=>{timestamps.push(frame.timestamp);ctx.drawImage(frame,0,0);const p=ctx.getImageData(1,1,1,1).data;green=green&&p[1]>180&&p[0]<80&&p[2]<80;frame.close();},error:e=>codecError=String(e)});decoder.configure(decoderConfig);for(const chunk of chunks)decoder.decode(chunk);await decoder.flush();decoder.close();if(codecError)throw Error(codecError);
    result.syntheticRoundTrip=timestamps.join(',')==='0,100000,200000'&&green?'passed':'failed';result.syntheticDecodedTimestampMicroseconds=timestamps;result.syntheticPixelCheck=green;return result;
   })()`);
   report.originalVideoDecoded=false;report.limitation='Synthetic codec verification only. Original MP4 demux/decode must succeed on the actual retained file before claiming reliable extracted frames.';
  }else{
   const capturePath=path.join(path.dirname(input),'capture.json'),capture=fs.existsSync(capturePath)?JSON.parse(fs.readFileSync(capturePath,'utf8').replace(/^\uFEFF/,'')):null;
   const capturedVideoSha=capture?.video?.sha256||(typeof capture?.video==='string'?capture.sha256:null);
   if(capturedVideoSha&&capturedVideoSha!==sha(bytes))throw Error('Video bytes differ from the adjacent source capture metadata');
   fs.mkdirSync(output);report.source={file:path.relative(root,input).replaceAll(path.sep,'/'),bytes:bytes.length,sha256:sha(bytes),captureMetadataPresent:!!capture,originalRuntimeMetadataMatches:!!(capture?.classification==='A'&&capture.package==='com.moonshot.kimichat'&&capture.runId==='run03-20261016'&&capture.serial==='emulator-5596'&&capture.avd==='rea_kimi_ui_20261015'&&capturedVideoSha===sha(bytes)),provenanceLimit:'Decoder does not repeat runtime capture or pixel privacy review; source provenance remains the separately audited recording metadata'};
   report.demux={...index,descriptionBase64:undefined};
   report.decoderResult=await evaluate(`(async()=>{
    const index=await(await fetch('/index')).json(),bytes=new Uint8Array(await(await fetch('/video')).arrayBuffer()),description=Uint8Array.from(atob(index.descriptionBase64),c=>c.charCodeAt(0));
    const config={codec:index.codec,description,codedWidth:index.width,codedHeight:index.height,hardwareAcceleration:'prefer-software'};
    if(!(await VideoDecoder.isConfigSupported(config)).supported)throw Error('Actual AVC config unsupported');
    const canvas=document.getElementById('pixels'),ctx=canvas.getContext('2d');let number=0,codecError;
    const decoder=new VideoDecoder({output:frame=>{canvas.width=frame.displayWidth;canvas.height=frame.displayHeight;ctx.drawImage(frame,0,0);saveDecodedFrame(JSON.stringify({number:++number,timestampMicroseconds:frame.timestamp,durationMicroseconds:frame.duration,displayWidth:frame.displayWidth,displayHeight:frame.displayHeight,pngBase64:canvas.toDataURL('image/png').split(',')[1]}));frame.close();},error:e=>codecError=String(e)});decoder.configure(config);
    for(const sample of index.samples){decoder.decode(new EncodedVideoChunk({type:sample.key?'key':'delta',timestamp:sample.timestampMicroseconds,duration:sample.durationMicroseconds,data:bytes.subarray(sample.offset,sample.offset+sample.size)}));if(decoder.decodeQueueSize>12)await new Promise(resolve=>setTimeout(resolve,0));}
    await decoder.flush();decoder.close();if(codecError)throw Error(codecError);return {decodedFrames:number,mediaPtsSource:'MP4 sample tables, ctts applied',hostTimingUsedForPTS:false};
   })()`);
   if(fatalFrameError)throw Error(fatalFrameError);
   if(frames.length!==index.sampleCount||report.decoderResult.decodedFrames!==index.sampleCount)throw Error('Decoded frame/sample count mismatch; incomplete output is not reliable');
   const consumed=new Set();for(const frame of frames){const matches=index.samples.filter(s=>s.timestampMicroseconds===frame.timestampMicroseconds&&!consumed.has(s.number));if(matches.length!==1)throw Error('Decoded PTS cannot be uniquely bound to one original sample');consumed.add(matches[0].number);frame.sourceSampleNumber=matches[0].number;frame.ptsTicks=matches[0].ptsTicks;frame.dtsTicks=matches[0].dtsTicks;frame.durationTicks=matches[0].durationTicks;frame.timeBase={numerator:1,denominator:index.timescale};}
   report.status='decoded-complete-pending-pixel-review';report.frames=frames;report.classification=report.source.originalRuntimeMetadataMatches?'Derived frame extraction bound to original runtime video metadata; not a new runtime capture':'Derived frame extraction from supplied retained video; original runtime provenance unverified';
   report.timestampLimit='Original integer media-track PTS preserved. Rounded microseconds are only the decoder interface; edit-list mapping and host action alignment are not inferred.';
   fs.writeFileSync(path.join(output,'frame-index.json'),JSON.stringify(report,null,2)+'\n');
  }
  console.log(JSON.stringify(opts['--check']?report:{status:report.status,source:report.source,frames:frames.length,frameIndex:path.relative(root,path.join(output,'frame-index.json')).replaceAll(path.sep,'/'),animationDuration:null,easing:null,interruptionVerified:false,adbCalled:false},null,2));
 }catch(error){if(output&&fs.existsSync(output))fs.writeFileSync(path.join(output,'decode-failure.json'),JSON.stringify({...report,status:'failed-or-partial',failure:String(error.message),extractedFrames:frames.length,completeSequenceVerified:false},null,2)+'\n');throw error;}
 finally{
  if(socket?.readyState===WebSocket.OPEN){try{const id=++counter;socket.send(JSON.stringify({id,method:'Browser.close'}));}catch{}await delay(500);socket.close();}
  if(child&&child.exitCode===null){child.kill();await delay(300);}
  if(server)await new Promise(resolve=>server.close(resolve));
  // Delete only this newly generated isolated profile, never a user browser profile.
  const target=path.resolve(profile);if(!target.startsWith(path.resolve(privateRoot)+path.sep)||path.dirname(target)!==path.resolve(privateRoot))throw Error('Unsafe temporary-profile cleanup target');
  try{fs.rmSync(target,{recursive:true,force:true,maxRetries:2,retryDelay:100});}catch{}
 }
}
if(process.argv[1]&&path.resolve(process.argv[1])===self)main().catch(error=>{console.error('Offline decoder: '+error.message);process.exitCode=1;});

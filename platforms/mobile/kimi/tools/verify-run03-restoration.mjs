import fs from 'node:fs';
import path from 'node:path';
const serial='emulator-5596',avd='rea_kimi_ui_20261015',pkg='com.moonshot.kimichat';
const date=value=>typeof value==='string'&&Number.isFinite(Date.parse(value));
const parsePermission=line=>{
 const m=line.match(/(android\.permission\.[A-Z_]+): granted=(true|false), flags=\[([^\]]*)\]/);
 return m?{permission:m[1],granted:m[2]==='true',flags:m[3].split('|').map(s=>s.trim()).filter(Boolean).sort()}:null;
};
/** No ADB, mutation or pixel review. Values are cross-checked against retained independent records. */
export function verifyRun03Restoration({root,run,session,expected,required,check}){
 const issues=[];
 const test=(ok,message)=>{if(!ok)issues.push(message);if(required)check(ok,message);};
 const exists=p=>fs.existsSync(path.join(root,p));
 const read=p=>JSON.parse(fs.readFileSync(path.join(root,p),'utf8').replace(/^\uFEFF/,''));
 const descriptor={status:'not-yet-verified',required,initialSystemLocaleStorageStateKnown:false,exactInitialSystemLocaleStorageRestorationProved:false,issues};
 const restorePath=run+'/restore.json';
 if(!exists(restorePath)){test(false,'Run03 restoration record missing');return descriptor;}
 try{
  const r=read(restorePath);
  test(r.serial===serial&&r.avd===avd,'Restoration record target differs from dedicated AVD');
  const contained=(value,fallback)=>{const p=value||fallback;test(typeof p==='string'&&p.startsWith(run+'/')&&!p.includes('..')&&!path.isAbsolute(p),'Restoration evidence path escapes its run');return typeof p==='string'&&p.startsWith(run+'/')&&!p.includes('..')&&!path.isAbsolute(p)?p:null;};
  const statePath=contained(r.finalState,run+'/final-device-state.json');
  const shutdownPath=contained(r.shutdownVerification,run+'/shutdown-verification.json');
  const proofPath=run+'/artifact-identity-final.json';
  for(const p of [statePath,shutdownPath,proofPath])test(p&&exists(p),'Restoration evidence missing: '+p);
  if(!statePath||!shutdownPath||![statePath,shutdownPath,proofPath].every(exists)){descriptor.status='record-incomplete';return descriptor;}
  const state=read(statePath),shutdown=read(shutdownPath),proof=read(proofPath);
  test(state.schemaVersion===2&&state.kind==='run03-final-read-only-state'&&state.runId==='run03-20261016'&&state.serial===serial&&state.avd===avd&&state.package===pkg,'Final device state schema/identity differs');
  test(date(state.startedAt)&&date(state.completedAt)&&Date.parse(state.completedAt)>=Date.parse(state.startedAt),'Final state observation timestamps missing or unordered');
  const latest=session.scenes.at(-1)?.capturedAt;
  if(date(latest)&&date(state.startedAt))test(Date.parse(state.startedAt)>=Date.parse(latest),'Final state precedes the last source capture');
  const baseline=read(run+'/environment-baseline.json'),conditions=read(run+'/conditions-baseline.json'),wm=read(run+'/wm-rotation-baseline.json');
  for(const [key,value]of Object.entries(conditions.settings))test(state.settings?.[key]===value,'Final display setting differs from baseline: '+key);
  for(const key of ['size','density'])test(state[key]===baseline[key]&&state[key]===conditions[key],'Final '+key+' differs from baseline');
  test(state.night===baseline.night,'Final system night mode differs from baseline');
  test(state.wmUserRotation===wm.userRotation&&state.wmFixedToUserRotation===wm.fixed,'Final WindowManager rotation differs from baseline');
  test(state.ime===conditions.ime&&state.appLocales===conditions.appLocales,'Final IME or source app locale differs from baseline');
  const network=state.network;
  test(network?.wifi===baseline.wifi&&network?.data===baseline.mobileData&&r.network?.settingsMatch===true,'Final network switches differ from baseline');
  test(state.settings?.wifi_on===network?.wifi&&state.settings?.mobile_data===network?.data,'Final network summary differs from its shell settings observation');
  test(network?.activeDefaultNetworkValidated===true&&/^Active default network: \d+$/.test(network?.defaultNetworkLine||'')&&r.network?.activeNetworkVerified===true,'Final validated active network is unverified');
  const baselinePermissions=read(run+'/permission-baseline.json').permissions.map(parsePermission);
  test(baselinePermissions.length===3&&baselinePermissions.every(Boolean),'Permission baseline cannot be parsed');
  for(const baselinePermission of baselinePermissions.filter(Boolean)){
   const matches=state.permissions?.filter(p=>p.permission===baselinePermission.permission)||[];
   test(matches.length===1&&matches[0].granted===baselinePermission.granted&&JSON.stringify(matches[0].flags)===JSON.stringify(baselinePermission.flags),'Final permission grant/flags differ: '+baselinePermission.permission);
  }
  const initialLocale=session.scenes.find(s=>s.conditions?.systemLocales==='null');
  test(!!initialLocale,'Initial null system_locales observation is missing');
  test(state.locale?.systemLocalesGet==='null'&&state.locale?.systemLocalesKeyPresent===false,'Final system_locales must be observed null with the key absent');
  test(state.locale?.buildLocale==='en-US','Final build locale differs from retained en-US baseline');
  const locale=r.locale;
  test(locale?.initialGet==='null'&&locale?.initialKeyPresenceKnown===false&&locale?.exactStorageRestorationClaim===false&&typeof locale?.limitation==='string'&&locale.limitation.length>=20,'System locale restoration must disclose unknown initial key presence; null get alone does not prove initial absence');
  descriptor.localeBoundary={initialGet:'null',initialObservationScene:initialLocale?.id||null,initialKeyPresenceKnown:false,finalGet:state.locale?.systemLocalesGet,finalKeyPresent:state.locale?.systemLocalesKeyPresent,exactInitialStorageRestorationProved:false};
  const pref=r.appPreferences;
  for(const [key,value]of Object.entries({theme:'Follow System',fontSize:'Follow System',language:'English',model:'Instant',thinking:'High',webSearch:'Auto'}))test(pref?.[key]===value,'Final app preference differs or is missing: '+key);
  test(pref?.reviewed===true&&Array.isArray(pref?.evidenceSceneIds)&&new Set(pref.evidenceSceneIds).size>=2,'Final app preference pixel/UIA review evidence is missing');
  for(const id of pref?.evidenceSceneIds||[])test(session.scenes.some(s=>s.id===id&&s.classification==='A'&&s.hierarchy),'Final preference evidence is not a complete source scene: '+id);
  test(proof.serial===serial&&proof.avd===avd&&proof.package===pkg&&proof.matches===true&&proof.originalSha256===expected.sha256&&proof.installedSha256===expected.sha256&&proof.versionName==='3.1.3'&&proof.versionCode===376&&proof.bytes===41038830&&date(proof.verifiedAt),'Final source APK identity proof differs');
  if(date(latest)&&date(proof.verifiedAt))test(Date.parse(proof.verifiedAt)>=Date.parse(latest),'Final APK identity proof precedes the last source capture');
  test(JSON.stringify(session.identityVerificationFinal)===JSON.stringify(proof),'Final source APK proof differs from preserved session final record');
  test(r.account?.loggedOut===false&&r.account?.credentialsRecorded===false,'Account preservation/no-credential boundary is missing');
  test(shutdown.serial===serial&&shutdown.dedicatedAvd===avd&&shutdown.serialStillListed===false&&Array.isArray(shutdown.remainingOwnedProcesses)&&shutdown.remainingOwnedProcesses.length===0&&date(shutdown.checkedAt),'Owned AVD shutdown not verified');
  if(date(shutdown.checkedAt)&&date(state.completedAt))test(Date.parse(shutdown.checkedAt)>=Date.parse(state.completedAt),'Shutdown check predates the final state observation');
  if(date(shutdown.checkedAt)&&date(proof.verifiedAt))test(Date.parse(shutdown.checkedAt)>=Date.parse(proof.verifiedAt),'Shutdown check predates the final APK proof');
  descriptor.status=issues.length?'record-incomplete':'recorded-restoration-verified-with-explicit-locale-boundary';
  descriptor.evidence={restore:restorePath,finalState:statePath,shutdown:shutdownPath,finalSourceApkIdentity:proofPath};
  descriptor.boundary='Verifies retained record consistency and known baseline values, including exact permission flags. Preference values remain the controller’s explicit pixel/UIA review. Initial system_locales storage presence was never measured; the final absent key recreates the observed null read, without proving byte-exact initial storage restoration.';
 }catch(error){test(false,'Restoration record validation failed: '+error.message);descriptor.status='record-incomplete';}
 return descriptor;
}

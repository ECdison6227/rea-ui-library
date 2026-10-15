// Offline preview ONLY. This file is authored for analysis and is not in the APK.
// ?platform=android enables fake bridge answers to reveal Android-only UI branches.
(() => {
  const params = new URLSearchParams(location.search);
  const android = params.get('platform') === 'android';
  const view = params.get('view') || 'login';
  const theme = params.get('theme') || 'dark';
  const store = localStorage;
  const calls = window.__previewCalls = [];
  // Start screenshots with a deterministic, separate preview profile.
  store.clear();
  const hosts = view === 'home' ? JSON.stringify({hosts:[{
    id:'offline-analysis',name:'我的工作空间',host:'https://192.0.2.1',
    addedAt:1,lastUsedAt:1
  }],activeId:'offline-analysis'}) : null;
  if(hosts) store.setItem('mirasim.hosts.v1',hosts);
  window.fetch = async (url) => {calls.push({kind:'blocked-fetch',url:String(url)});throw new TypeError('Offline analysis preview');};
  window.WebSocket = class extends EventTarget {
    static CONNECTING=0; static OPEN=1; static CLOSING=2; static CLOSED=3;
    readyState=0; bufferedAmount=0;
    constructor(url){super();calls.push({kind:'blocked-websocket',url:String(url)});}
    send(){} close(){this.readyState=3;}
  };
  if(android){
    window.androidBridge = {};
    const methods = {
      App:['getInfo','getState','getLaunchUrl','addListener','removeListener','exitApp'],
      Preferences:['get','set','remove','keys'],
      SecureStorage:['internalGetItem','internalSetItem','internalRemoveItem','getPrefixedKeys'],
      LocalNotifications:['checkPermissions','requestPermissions','addListener','removeListener'],
      ForegroundService:['batteryStatus','vendorAutostartAvailable','start','stop','jsAlive','attentionTicket','addListener','removeListener','requestBatteryExemption','openVendorAutostart','openNotificationSettings','syncChannelCredential','clearChannelCredential'],
      AttentionNotifier:['deviceProfile','openChannelSettings','post','cancel','addListener','removeListener'],
      ApkUpdater:['canInstall','requestInstallPermission','download','install','addListener','removeListener'],
      Keyboard:['addListener','removeListener','setResizeMode'],
      StatusBar:['setStyle','setBackgroundColor','setOverlaysWebView'],
      WindowFloor:['setColor'], EdgeGesture:['setLeftEdgeClaimed','addListener','removeListener'],
      LandscapeLock:['setLandscape'], Browser:['open','close','addListener','removeListener'],
    };
    window.Capacitor = {
      PluginHeaders:Object.entries(methods).map(([name,items])=>({name,methods:items.map(name=>({name,rtype:name==='addListener'?'callback':'promise'}))})),
      nativeCallback:()=> 'preview-listener',
      async nativePromise(plugin,method,args={}){
        calls.push({kind:'simulated-native',plugin,method,args});
        if(plugin==='App'&&method==='getInfo')return {name:'Mirasim',id:'ai.mirofish.mirasim',version:'0.0.497',build:'497'};
        if(plugin==='App'&&method==='getState')return {isActive:true};
        if(plugin==='Preferences'){
          if(method==='get')return {value:store.getItem('pref:'+args.key)};
          if(method==='set')store.setItem('pref:'+args.key,args.value);
          if(method==='remove')store.removeItem('pref:'+args.key);
        }
        if(plugin==='SecureStorage'){
          const key=args.prefixedKey;
          if(method==='internalGetItem')return {data:key?.endsWith('mirasim.hosts.v1')&&hosts?JSON.stringify(hosts):store.getItem('secure:'+key)};
          if(method==='internalSetItem')store.setItem('secure:'+key,args.data);
          if(method==='internalRemoveItem'){store.removeItem('secure:'+key);return {success:true};}
          if(method==='getPrefixedKeys')return {keys:[]};
        }
        if(plugin==='LocalNotifications')return {display:'prompt'};
        if(method==='batteryStatus')return {exempt:false};
        if(method==='vendorAutostartAvailable')return {available:true};
        if(method==='deviceProfile')return {manufacturer:'Xiaomi',brand:'Redmi',model:'analysis-profile'};
        if(plugin==='ApkUpdater'&&method==='canInstall')return {allowed:false};
        if(plugin==='ApkUpdater'&&(method==='download'||method==='install'))throw new Error('Native download/install is not available in browser preview');
        return {};
      }
    };
  }
  addEventListener('DOMContentLoaded',async()=>{
    const {P:uiStore} = await import('/app/assets/index-BCeL5nAW.js');
    let attempts=0;
    const ready=setInterval(()=>{
      if(!document.querySelector('button')||++attempts>100)return attempts>100&&clearInterval(ready);
      clearInterval(ready);
      uiStore.getState().setThemeMode(theme);
      uiStore.getState().setLocale('zh-CN');
      const clickLabel=label=>document.querySelector(`[aria-label="${label}"]`)?.click();
      if(view==='settings'||view==='notifications'||view==='update')clickLabel('设置');
      if(view==='pair')Array.from(document.querySelectorAll('button')).find(x=>x.textContent.includes('改用扫码'))?.click();
      if(view==='notifications'||view==='update')setTimeout(()=>{
        const target = view==='update'?'检查更新':'任务提醒';
        Array.from(document.querySelectorAll('button,div,span')).find(x=>x.textContent===target)?.scrollIntoView({block:'center'});
      },300);
    },50);
  });
})();

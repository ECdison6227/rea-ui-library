'use strict';
(() => {
  const data = window.__UI_STUDY_DATA__;
  if (!data) { document.querySelector('main').textContent = '离线数据 data.js 未载入。请确认文件完整，并通过 README 中的本地服务启动。'; return; }
  const $ = s => document.querySelector(s);
  const escape = x => String(x ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  const state = { account:'member', content:'ready', hasData:true, tab:0, tabBusy:false, menu:null, theme:'system', events:[], refreshing:false, requestId:0, dialog:null, reduced:false };
  let toastTimer, revealTimer, revealFinishTimer, lastFocused;
  const media = matchMedia('(prefers-color-scheme: dark)');
  const reducedMedia = matchMedia('(prefers-reduced-motion: reduce)');
  function icon(name) {
    const paths = {
      book:'<path d="M4 4h6c1.2 0 2 .5 2 1.5C12 4.5 13 4 14 4h6v15h-6c-1.2 0-2 .5-2 1.5 0-1-1-1.5-2-1.5H4z"/><path d="M12 5.5v15"/>',
      grid:'<rect x="4" y="4" width="6" height="6" rx="1.5"/><rect x="14" y="4" width="6" height="6" rx="1.5"/><rect x="4" y="14" width="6" height="6" rx="1.5"/><rect x="14" y="14" width="6" height="6" rx="1.5"/>',
      chart:'<path d="M4 19V5M4 19h16M8 15v-4m5 4V7m5 8V9"/>',
      user:'<circle cx="12" cy="8" r="3.5"/><path d="M5 20v-2a7 7 0 0 1 14 0v2"/>',
      clock:'<circle cx="12" cy="12" r="8"/><path d="M12 7v5l3 2"/>',
      check:'<circle cx="12" cy="12" r="8"/><path d="m8 12 3 3 5-6"/>',
      plus:'<path d="M12 6v12M6 12h12"/>',
      arrow:'<path d="m10 7 5 5-5 5"/>',
      folder:'<path d="M3 7a2 2 0 0 1 2-2h5l2 3h7a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>',
      team:'<circle cx="9" cy="8" r="3"/><path d="M3 20v-2a6 6 0 0 1 12 0v2M16 5a3 3 0 0 1 0 6m2 3a5 5 0 0 1 3 5"/>',
      bell:'<path d="M5 17h14l-2-3V9a5 5 0 0 0-10 0v5zM10 20h4"/>',
      gear:'<path d="m10 3-1 3-3 1-3 3 2 2-1 4 3 2 3-1 2 4 3-2 2-3 4-1-1-4-3-1-1-4z"/><circle cx="12" cy="12" r="3"/>',
      empty:'<path d="m5 8 7-4 7 4v10l-7 3-7-3zM5 8l7 4 7-4M12 12v9"/>',
      wifi:'<path d="M3 9a14 14 0 0 1 18 0M6 12a9 9 0 0 1 12 0M9 15a4 4 0 0 1 6 0M12 19h.01M3 3l18 18"/>',
      lock:'<rect x="5" y="10" width="14" height="11" rx="2"/><path d="M8 10V7a4 4 0 0 1 8 0v3M12 14v3"/>',
      globe:'<circle cx="12" cy="12" r="9"/><path d="M3 12h18M12 3a18 18 0 0 1 0 18 18 18 0 0 1 0-18"/>',
      medal:'<circle cx="12" cy="14" r="6"/><path d="m8 9-3-6h5l2 5 2-5h5l-3 6"/>',
      scan:'<path d="M4 9V4h5m6 0h5v5m0 6v5h-5M9 20H4v-5M7 12h10"/>'
    };
    return `<svg viewBox="0 0 24 24" aria-hidden="true">${paths[name] || paths.grid}</svg>`;
  }
  function sourceLink(evidence) {
    if (!evidence) return '';
    const e = Array.isArray(evidence) ? evidence[0] : evidence;
    const file = e.extractedModule?.replace(/^analysis\/ui\//,'');
    return file ? `<a href="../${escape(file)}">${escape(e.moduleId)} · bundle 行 ${escape(e.bundleLine)}</a>` : '';
  }
  function event(text) { state.events.unshift({time:new Date().toLocaleTimeString('zh-CN',{hour12:false}),text});state.events=state.events.slice(0,18);renderState(); }
  function toast(text) { clearTimeout(toastTimer);$('#toast').textContent=text;$('#toast').hidden=false;toastTimer=setTimeout(()=>$('#toast').hidden=true,2000); }
  function themeName() { return state.theme==='system'?(media.matches?'night':'day'):state.theme; }
  function applyTheme() {
    const mode=themeName();document.documentElement.dataset.miTheme=mode;
    const rn=data.rn.tokens.appTheme[mode==='night'?'dark':'light'];
    const mapping={'--rn-bg':'navBarBgColor','--rn-card':'bgColor','--rn-text':'mainTextColor','--rn-sub':'subTextColor','--rn-tab':'tabLabelColor','--rn-primary':'primaryColor','--rn-line':'LinebgColor','--rn-panel':'panelColor'};
    for(const [css,key] of Object.entries(mapping))if(rn[key])document.documentElement.style.setProperty(css,rn[key]);
    const semantic=data.rn.tokens.semanticColors[mode==='night'?'dark':'light'];
    document.documentElement.style.setProperty('--rn-pressed',semantic['color.brand.primaryPressed']);
    renderTokens();renderState();
  }
  $('#theme').addEventListener('change',e=>{state.theme=e.target.value;applyTheme();});
  media.addEventListener('change',()=>{if(state.theme==='system')applyTheme();});
  state.reduced=reducedMedia.matches;$('#reduce-motion').checked=state.reduced;document.body.classList.toggle('reduced',state.reduced);
  $('#reduce-motion').addEventListener('change',e=>{state.reduced=e.target.checked;document.body.classList.toggle('reduced',state.reduced);});

  function statusBar() { return '<div class="sim-status"><span>09:41 · 演示安全区</span><span class="dots">▂▃▄ ▰</span></div>'; }
  function navigation(active) { return `<nav class="sim-navigation" aria-label="业务示例底部导航">${[['book','概览'],['grid','资源'],['chart','统计'],['user','我的']].map(([i,t],n)=>`<button data-act="nav" data-label="${t}" ${n===active?'aria-current="page"':''}>${icon(i)}<span>${t}</span></button>`).join('')}</nav>`; }
  function row(label,desc,ic='folder',act='detail',value='') { return `<button class="demo-list-row" data-act="${act}" data-label="${escape(label)}">${icon(ic)}<span class="row-label"><b>${escape(label)}</b>${desc?`<small>${escape(desc)}</small>`:''}</span>${value?`<span class="value">${escape(value)}</span>`:''}<span class="arrow" aria-hidden="true">›</span></button>`; }
  function section(title,inner,more=false) { return `<div class="rn-section"><div class="card-title"><h4>${title}</h4>${more?`<button data-act="detail" data-label="${title}">全部${icon('arrow')}</button>`:''}</div>${inner}</div>`; }
  function placeholder() {
    if(state.content==='loading')return '<div aria-busy="true" aria-label="首次加载占位">'+Array.from({length:3},()=>'<div class="skeleton"><span></span><span></span><span></span></div>').join('')+'</div>';
    if(state.hasData)return '';
    const [ic,title,desc,button,act]=state.content==='empty'?['empty','这里还没有内容','从一个小目标开始，添加第一项记录。','添加示例内容','create']:state.content==='offline'?['wifi','暂时无法连接','离线状态属于本地模拟，不会访问网络。','重试','retry']:['empty','内容暂时无法载入','保留当前页面结构，提供明确的恢复入口。','重试','retry'];
    return `<div class="rn-section empty-state">${icon(ic)}<strong>${title}</strong><p>${desc}</p><button data-act="${act}" data-label="添加内容">${button}</button></div>`;
  }
  function strip() {
    if(state.refreshing)return '<div class="status-strip" role="status">○ 正在刷新 · 保留已有内容 <span>本地 700ms</span></div>';
    if(state.content==='error'&&state.hasData)return '<div class="status-strip error">更新失败，已保留上次数据<button data-act="retry">重试</button></div>';
    return '<div class="status-strip"><span>示例数据</span><span>·</span><span>与真实账号无关</span></div>';
  }
  function menu(phone) { return state.menu===phone?`<button class="menu-scrim" data-act="close-menu" aria-label="关闭菜单"></button><div class="phone-menu" role="menu"><button role="menuitem" data-act="menu-create" data-label="创建新项目">${phone==='learning'?'添加学习计划':'创建新项目'}</button><button role="menuitem" data-act="menu-scan" data-label="扫码加入">扫码加入</button></div>`:''; }
  function learning() {
    let content=placeholder();
    if(state.hasData){const isPlan=state.tab===1;content=`${state.account==='guest'?'<div class="rn-section guest-card"><h4>把进度留在自己的空间</h4><p>公开资源可浏览，个人学习计划需要登录。</p><button class="rn-primary" data-act="login">登录后同步</button></div>':''}${section(isPlan?'本周计划':'本周学习','<div class="big-stat"><strong>128</strong><span>分钟 / 目标 180</span></div><div class="demo-progress"><span></span></div><p>再学 52 分钟，完成本周目标</p>',true)}<div class="metrics"><div class="metric">${icon('check')}<span>完成单元</span><strong>12 <small>个</small></strong><small>较上周 +3</small></div><div class="metric">${icon('clock')}<span>连续学习</span><strong>7 <small>天</small></strong><small>保持自己的节奏</small></div></div>${section(isPlan?'待完成':'继续学习',row('视觉设计基础','模块 03 · 信息层级','book','private','42%')+row('把复杂问题讲清楚','最近学习 · 16 分钟','book','private'),true)}${section('我的资源',row('收藏与笔记','把有用的内容留在一起','folder','private'))}`;}
    return `${statusBar()}<header class="phone-header"><h3>学习</h3><button class="icon-button" data-act="menu" data-phone="learning" aria-label="学习页添加菜单" aria-expanded="${state.menu==='learning'}">${icon('plus')}</button></header><div class="phone-body"><div class="phone-tabs" role="tablist" aria-label="学习分类">${['概览','计划','资源'].map((t,i)=>`<button role="tab" aria-selected="${state.tab===i}" data-act="tab" data-tab="${i}">${t}</button>`).join('')}</div>${strip()}<div class="scene-enter" id="learning-content">${content}</div></div>${navigation(0)}${menu('learning')}`;
  }
  function team() {
    const profile=`<div class="profile"><div class="avatar">${state.account==='member'?'林':icon('user')}</div><button data-act="${state.account==='member'?'private':'login'}" data-label="个人资料"><div class="name">${state.account==='member'?'林川的工作空间':'登录你的空间'}</div><p>${state.account==='member'?'产品设计 | 协作成员':'登录后查看项目与团队'}</p></button></div><div class="shortcuts">${[['folder','项目'],['team','团队'],['book','文件'],['medal','收藏']].map(([ic,t])=>`<button class="shortcut" data-act="private" data-label="${t}">${icon(ic)}<span>${t}</span></button>`).join('')}</div>`;
    let content=placeholder();
    if(state.hasData)content=`<div class="team-tools"><button class="tool-large" data-act="private" data-label="待办事项"><b>待办事项</b><strong>06</strong><small>把今天最重要的事做完</small></button><button class="tool-small" data-act="private" data-label="团队周报"><b>团队周报</b><small>本周 3 项新进展</small></button><button class="tool-small" data-act="private" data-label="最近文件"><b>最近文件</b><small>2 份内容已更新</small></button></div>${section('工作空间',row('项目与成员','管理协作范围','team','private')+row('文件管理','','folder','private'),false)}`;
    return `${statusBar()}<header class="phone-header"><h3>我的空间</h3><button class="icon-button" data-act="menu" data-phone="team" aria-label="团队页添加菜单" aria-expanded="${state.menu==='team'}">${icon('plus')}</button></header><div class="phone-body">${profile}${strip()}${content}${section('通用',row('通知与偏好','','bell','public','已开启')+row('关于工作空间','','globe','public'),false)}</div>${navigation(3)}${menu('team')}`;
  }
  function renderPhones(){ $('#learning-phone').innerHTML=learning();$('#team-phone').innerHTML=team();renderState(); }
  function renderState(){
    $('#state-values').innerHTML=[['账号',state.account],['内容',state.content],['已有数据',state.hasData?'yes':'no'],['刷新中',state.refreshing?'yes':'no'],['Tab',state.tab],['分页忙碌',state.tabBusy?'yes':'no'],['主题',themeName()],['本地模式','offline demo']].map(([a,b])=>`<dt>${a}</dt><dd><code>${escape(b)}</code></dd>`).join('');
    $('#event-log').innerHTML=state.events.length?state.events.map(e=>`<li><time>${e.time}</time> ${escape(e.text)}</li>`).join(''):'<li>点击模板中的按钮查看状态迁移。</li>';
  }
  function setContent(value){state.requestId++;state.refreshing=false;state.content=value;state.hasData=value==='ready';state.menu=null;renderPhones();event(`内容 → ${value}；示例情景切换`);}
  $('#account-state').addEventListener('change',e=>{state.account=e.target.value;state.menu=null;renderPhones();event(`账号 → ${state.account}`);});
  $('#content-state').addEventListener('change',e=>setContent(e.target.value));
  function refresh(){if(state.refreshing)return;const id=++state.requestId;state.refreshing=true;if(!state.hasData)state.content='loading';renderPhones();event('发起本地刷新；已有内容保持显示');setTimeout(()=>{if(id!==state.requestId)return;state.refreshing=false;if($('#load-result').value==='failure'){state.content='error';event('本地刷新失败 → error；没有网络调用');}else{state.content='ready';state.hasData=true;event('本地刷新完成 → ready');}$('#content-state').value=state.content;renderPhones();},700);}
  $('#refresh-templates').addEventListener('click',refresh);
  $('#reset-templates').addEventListener('click',()=>{state.requestId++;Object.assign(state,{account:'member',content:'ready',hasData:true,tab:0,tabBusy:false,menu:null,refreshing:false,events:[]});$('#account-state').value='member';$('#content-state').value='ready';$('#load-result').value='success';renderPhones();event('演示状态已重置');});
  $('#clear-log').addEventListener('click',()=>{state.events=[];renderState();});
  function openDialog(html,mode='detail'){lastFocused=document.activeElement;const dialog=$('#study-dialog');if(dialog.open)dialog.close();dialog.classList.toggle('bottom-sheet',mode==='login');$('#dialog-body').innerHTML=html;state.dialog=mode;dialog.showModal();}
  function closeDialog(){const d=$('#study-dialog');if(d.open)d.close();state.dialog=null;if(lastFocused?.isConnected)lastFocused.focus();}
  $('#study-dialog').addEventListener('click',e=>{if(e.target===$('#study-dialog'))closeDialog();});
  $('#study-dialog').addEventListener('cancel',()=>{state.dialog=null;event('关闭演示面板');});
  function detail(label){event(`进入演示详情：${label}`);openDialog(`<div class="dialog-detail"><span class="pill adaptation">业务适配示例</span><h3 id="dialog-title">${escape(label)}</h3><p>这是本地模板的详情占位，用于检查标题、内容层级、返回和登录门槛。它不是原 APK 的页面。</p><div class="detail-value">128 <small>示例值</small></div><div class="dialog-actions"><button class="cancel" data-dialog="close">返回模板</button><button class="confirm" data-dialog="save">保存示例</button></div><div class="dialog-provenance">本弹层布局是研究台选择；RN / 原生的原始页面结构请查看相应 JSON。</div></div>`);}
  let pendingAction=null;
  function executeAction(label,act='detail'){if(act==='create'){state.content='ready';state.hasData=true;$('#content-state').value='ready';renderPhones();toast('已添加一组本地示例内容');event('创建示例内容 → ready');}else detail(label);}
  function guard(label,act='detail'){if(state.account==='guest'){pendingAction={label,act};event(`登录门槛：${label}`);openDialog(`<h3 id="dialog-title">登录后继续</h3><p>登录后可访问个人内容和协作空间。这里仅演示登录门槛，不会连接账号服务，也不会收集任何凭据。</p><div class="dialog-actions"><button class="cancel" data-dialog="close">暂不登录</button><button class="confirm" data-dialog="login-launch">继续登录</button></div><div class="dialog-provenance">面板参考 mi-484：padding 24、r28，按钮 r16 / 17sp，说明 16sp / 行高26。浏览器边角和中文字体为近似。</div>`,'login');}else executeAction(label,act);}
  $('#study-dialog').addEventListener('click',e=>{const b=e.target.closest('[data-dialog]');if(!b)return;const action=b.dataset.dialog;if(action==='close'){closeDialog();event('关闭面板，保留当前页面');}if(action==='save'){closeDialog();toast('示例已保存到本次演示状态');event('保存示例；无持久化、无服务调用');}if(action==='login-launch'){closeDialog();event('关闭登录门槛；等待 1000ms 再模拟打开登录页');toast('正在模拟打开登录页…');setTimeout(()=>openDialog(`<h3 id="dialog-title">模拟登录页</h3><p>1000ms 等待参考原 RN 的登录页启动延迟。这里没有真实登录；请用下方按钮显式模拟账号成功状态。</p><div class="dialog-actions"><button class="cancel" data-dialog="close">返回</button><button class="confirm" data-dialog="login-success">模拟成功</button></div><div class="dialog-provenance">账号成功和后续路由为研究台交互选择。</div>`),1000);}if(action==='login-success'){closeDialog();state.account='member';$('#account-state').value='member';renderPhones();event('显式模拟登录成功 → member');const action=pendingAction;pendingAction=null;if(action)executeAction(action.label,action.act);}});
  function phoneClick(e){const b=e.target.closest('[data-act]');if(!b)return;const act=b.dataset.act,label=b.dataset.label||'个人内容';if(act==='menu'){state.menu=state.menu===b.dataset.phone?null:b.dataset.phone;renderPhones();event(state.menu?'打开锚定菜单':'关闭菜单');}else if(act==='close-menu'){state.menu=null;renderPhones();}else if(act==='menu-create'||act==='menu-scan'){state.menu=null;renderPhones();event('先关闭菜单，再校验登录状态');guard(label,act==='menu-create'?'create':'detail');}else if(act==='login'){guard('同步与个人资料');}else if(act==='private'){guard(label);}else if(act==='public'||act==='detail'||act==='nav'){detail(label);}else if(act==='create'){guard(label,'create');}else if(act==='retry'){refresh();}else if(act==='tab'){if(state.tabBusy){event('Tab 切换中：忽略重复点击');return;}state.tab=Number(b.dataset.tab);state.tabBusy=true;renderPhones();event(`Tab → ${state.tab}；220ms 为网页过渡选择`);setTimeout(()=>{state.tabBusy=false;renderState();},state.reduced?0:220);}}
  $('#learning-phone').addEventListener('click',phoneClick);$('#team-phone').addEventListener('click',phoneClick);

  const rnColorLabels={navBarBgColor:'页面与顶栏',bgColor:'内容卡片',mainTextColor:'正文主色',subTextColor:'正文次色',tabLabelColor:'非选中 Tab',primaryColor:'主动作',panelColor:'浮层',toastBgColor:'Toast 背景',toastTextColor:'Toast 文字',toastBorderColor:'Toast 边线',skeletonStartColor:'骨架起始',skeletonEndColor:'骨架终止',LinebgColor:'分割线',cardSubtitle:'卡片次标题',iBgColor:'内层底色'};
  function tokenRows(){const night=themeName()==='night';if($('#token-system').value==='native')return Object.entries(data.native.semantic).map(([name,t])=>({name,label:t.ref,value:t[night?'night':'day'].color?.css||t[night?'night':'day'].value,raw:t[night?'night':'day'].value,source:t.usage,chain:t[night?'night':'day'].chain.map(c=>`${c.resource} → ${c.raw} (${c.qualifier})`).join(' → ')}));const colors=data.rn.tokens.appTheme[night?'dark':'light'];const rows=Object.entries(rnColorLabels).map(([name,label])=>({name,label,value:colors[name],raw:colors[name],source:'RN sp-293 useThemeValue；sp-132 theme',href:'../rn-evidence/sport-sp-293.js',chain:'包内 RN 颜色；8位 CSS/RN 顺序为 RRGGBBAA'}));const sem=data.rn.tokens.semanticColors[night?'dark':'light'];return rows.concat(['color.brand.primary','color.brand.primaryPressed','color.brand.primaryHalo','color.link','color.error','color.disabled'].map(name=>({name,label:'RN 语义色',value:sem[name],raw:sem[name],source:'RN sp-311；第一方语义主题',href:'../rn-evidence/sport-sp-311.js',chain:'请保留普通、按下、光晕三个状态层'})));}
  function renderTokens(){const q=$('#token-filter').value.toLowerCase();const rows=tokenRows().filter(t=>[t.name,t.label,t.source,t.value].join(' ').toLowerCase().includes(q));$('#token-grid').innerHTML=rows.map(t=>`<article class="token-card"><div class="swatch"><span style="background:${escape(t.value)}"></span></div><div class="token-name">${escape(t.name)}</div><code>${escape(t.label)}</code><code>${escape(t.value)}</code><small>${escape(t.source)}</small><details><summary>引用与原值</summary><small>原值：${escape(t.raw)}</small><small>${escape(t.chain)}</small>${t.href?`<a href="${t.href}">查看模块</a>`:''}</details><button data-copy="${escape(t.value)}">复制值</button></article>`).join('')||'<p>没有匹配的 Token。</p>';}
  $('#token-system').addEventListener('change',renderTokens);$('#token-filter').addEventListener('input',renderTokens);
  async function copy(text){try{await navigator.clipboard.writeText(text);toast('已复制');}catch{openDialog(`<h3 id="dialog-title">手动复制</h3><p>当前浏览器未开放剪贴板权限，请选择下方文本。</p><textarea readonly style="width:100%;min-height:140px">${escape(text)}</textarea><button data-dialog="close">关闭</button>`);}}
  $('#token-grid').addEventListener('click',e=>{const b=e.target.closest('[data-copy]');if(b)copy(b.dataset.copy);});
  $('#copy-token-values').addEventListener('click',()=>copy(JSON.stringify(Object.fromEntries(tokenRows().map(x=>[x.name,x.value])),null,2)));
  const types=[['一级页面标题','32 / light（中文当前系统回退）','每天，向前一点。',32,300],['资料用户名','20 / 500','林川的工作空间',20,500],['分区卡标题','17 / bold','继续学习',17,700],['设置行标题','16 / bold；bold 优先','项目与成员',16,700],['次级说明','12 / 400','产品设计 | 协作成员',12,400],['数字与 Latin','18 / 600；包内 MiSansLatin','128  /  180  ·  Design',18,600]];
  $('#type-samples').innerHTML=types.map(([role,spec,text,size,weight])=>`<div class="type-sample"><div class="spec">${role} · ${spec}</div><div class="sample" style="--sample-size:${size}px;--sample-weight:${weight}">${text}</div></div>`).join('');
  $('#font-inventory').innerHTML=data.native.fonts.map(f=>`<div class="font-row"><code>${escape(f.file.split('/').pop())}</code><small>weightClass ${f.weightClass} · ${f.glyphCount} glyph · ${Math.round(f.bytes/1024)} KB</small><small>探针：0 ${f.unicodeGlyphProbes['U+0030']?'✓':'×'} / A ${f.unicodeGlyphProbes['U+0041']?'✓':'×'} / 健 ${f.unicodeGlyphProbes['U+5065']?'✓':'×'}</small></div>`).join('');
  $('#latin-font').addEventListener('change',e=>{document.documentElement.style.setProperty('--study-font',`${e.target.value},system-ui,"Microsoft YaHei","PingFang SC",sans-serif`);});
  $('#text-scale').addEventListener('input',e=>{document.documentElement.style.setProperty('--study-scale',e.target.value/100);$('#text-scale-value').textContent=e.target.value+'%';});
  $('#sample-action').addEventListener('click',()=>toast('主动作：普通 → 按下 → 提示'));
  $('#sample-secondary').addEventListener('click',()=>toast('次要操作示例'));
  function updateRow(){$('#sample-row').setAttribute('aria-disabled',$('#row-disabled').checked?'true':'false');}
  $('#row-disabled').addEventListener('change',updateRow);
  $('#sample-row').addEventListener('click',()=>{if($('#row-disabled').checked&&!$('#row-allow-click').checked){toast('禁用且不允许点击：操作未执行');return;}toast($('#row-disabled').checked?'禁用但允许解释：请联系管理员开放权限':'共享空间：进入详情');});
  $('#component-contracts').innerHTML=data.components.componentContracts.map(c=>`<article class="contract"><h4>${escape(c.id)}</h4><code>${escape(typeof c.source==='object'?c.source.file:c.source)}</code><pre>${escape(JSON.stringify({metrics:c.metrics,declarations:c.declarations,states:c.states,unknown:c.unknown,inference:c.inference},null,2))}</pre></article>`).join('');

  $('#toast-demo').addEventListener('click',()=>toast('这是一条本地提示，2000ms 后关闭'));
  $('#reveal-demo').addEventListener('click',()=>{clearTimeout(revealTimer);clearTimeout(revealFinishTimer);const slot=$('#reveal-slot');slot.style.height='0px';$('#reveal-status').textContent=state.reduced?'减少动画：直接展示新增内容':'等待 2000ms…';revealTimer=setTimeout(()=>{slot.style.height=slot.scrollHeight+'px';$('#reveal-status').textContent=state.reduced?'已展示':'正在展开，持续 500ms';revealFinishTimer=setTimeout(()=>$('#reveal-status').textContent='展开完成；此示例仅适用于新增内容',state.reduced?0:500);},state.reduced?0:2000);});
  const medal=$('#medal-demo');let releaseTimer;
  function pressObject(){clearTimeout(releaseTimer);medal.style.transition='transform 100ms ease';medal.style.transform='scale(.9)';}
  function releaseObject(){clearTimeout(releaseTimer);releaseTimer=setTimeout(()=>{medal.style.transition='transform 300ms ease-out';medal.style.transform='scale(1)';},state.reduced?0:100);}
  medal.addEventListener('pointerdown',pressObject);medal.addEventListener('pointerup',releaseObject);medal.addEventListener('pointercancel',releaseObject);medal.addEventListener('pointerleave',releaseObject);medal.addEventListener('keydown',e=>{if([' ','Enter'].includes(e.key))pressObject();});medal.addEventListener('keyup',releaseObject);
  const motionNames={rn_navigation:'RN 页面导航弹簧',top_tab_scrub:'顶部 Tab 与连续插值',medal_press:'勋章按压',new_indicator_reveal:'新增指标展开',promotion_popup:'表盘运营弹层',toast:'底部 Toast',login_bottom_modal:'登录底部面板',anchored_add_menu:'锚定添加菜单',accessibility:'无障碍行为'};
  $('#motion-catalog').innerHTML=data.rn.interactionPatterns.map(p=>`<article class="motion-record"><h4>${escape(motionNames[p.id]||p.id)}</h4><small>${escape(p.scope||'以第一方调用位置为范围')}</small><pre>${escape(JSON.stringify(p.observed,null,2))}</pre>${sourceLink(p.evidence)}</article>`).join('');

  async function loadScreens(){const status=$('#screen-status'),gallery=$('#screens-gallery');status.textContent='正在读取本地取证清单…';gallery.innerHTML='';try{const r=await fetch('../runtime/screens.json?preview='+Date.now(),{cache:'no-store'});if(!r.ok)throw Error('HTTP '+r.status);const payload=await r.json();const screens=Array.isArray(payload)?payload:(payload.screens||[]);if(!Array.isArray(screens)||screens.length===0){status.textContent='当前清单中没有原 APK 运行截图；这里不会用重建模板代替。';return;}let shown=0;for(const s of screens){const image=String(s.image||'').replaceAll('\\','/');if(!image||image.startsWith('/')||image.split('/').includes('..')||/^[a-z]+:/i.test(image))continue;const card=document.createElement('article');card.className='screen-record';card.innerHTML=`<a href="../${escape(image)}" target="_blank" rel="noopener"><img loading="lazy" src="../${escape(image)}" alt="${escape(s.title||s.id||'原 APK 截图')}" /></a><span class="pill runtime">原 APK 运行取证</span><h3>${escape(s.title||s.id)}</h3><p>状态：${escape(typeof s.state==='object'?JSON.stringify(s.state):s.state||'未注明')}<br>Evidence：${escape(s.evidenceId||'未注明')}<br>来源：${escape(typeof s.source==='object'?JSON.stringify(s.source):s.source||'未注明')}</p><details><summary>完整取证元数据</summary><pre>${escape(JSON.stringify(s,null,2))}</pre></details>`;card.querySelector('img').addEventListener('error',()=>{const note=document.createElement('p');note.textContent='图片文件未能载入，请检查清单路径；不替换成示例图片。';card.querySelector('img').replaceWith(note);});gallery.append(card);shown++;}status.textContent=`已载入 ${shown} 条原 APK 运行取证记录。图片内容以对应环境和状态为限。`;}catch(e){status.innerHTML=`<strong>原 APK 截图尚未取证或当前无法读取。</strong><p>未找到可读的 runtime/screens.json（${escape(e.message)}）。如果直接以 file:// 打开，浏览器可能禁止 fetch；请用本地服务访问 /preview/。其余规范与重建示例使用内置快照，可继续离线审阅。</p>`;}}
  $('#reload-screens').addEventListener('click',loadScreens);
  $('#stats').innerHTML=[[Object.keys(data.native.semantic).length,'原生语义颜色'],[data.components.componentContracts.length,'原生组件契约'],[data.rn.pages.length,'RN 页面规格'],[data.rn.interactionPatterns.length,'RN 交互模式']].map(([n,label])=>`<div class="stat"><strong>${n}</strong><span>${label}</span></div>`).join('');
  $('#snapshot-info').textContent=`离线快照生成：${data.snapshot.generatedAt}。数据来自本地已生成规范；可用 build-preview.cjs 重建。页面不依赖 CDN、外部字体或外部 API。`;
  const observer=new IntersectionObserver(entries=>{for(const entry of entries)if(entry.isIntersecting){document.querySelectorAll('.sidebar nav a').forEach(a=>a.classList.toggle('active',a.hash==='#'+entry.target.id));}},{rootMargin:'-10% 0px -75% 0px'});document.querySelectorAll('main>section').forEach(s=>observer.observe(s));
  applyTheme();renderPhones();loadScreens();
})();

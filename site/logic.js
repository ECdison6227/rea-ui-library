export function filterSources(sources,{platform='all',query=''}={}){
 const words=query.trim().toLocaleLowerCase().split(/\s+/).filter(Boolean);
 return sources.filter(s=>(platform==='all'||s.platform===platform)&&words.every(w=>[s.name,s.subtitle,s.summary,...s.tags,...s.stack].join(' ').toLocaleLowerCase().includes(w)));
}
export function migrationPrompt(s){return `使用 rea-style-library 的迁移模式。\n来源：${s.name}（${s.sourceVersion}）\n研究目录：${s.root}\n入口：${s.artifacts.find(a=>a.role==='readme')?.path||'README.md'}\n我的项目：[填写你的项目路径]\n目标：迁移视觉语言与核心交互，保留我的业务、数据和品牌。\n先读来源规格、事件契约、原版对照和未知，再审查目标源码、建立规则映射并实施。\n来源未测的行为按我的产品需求作独立D决策；不要重新全量解析，也不要默认穷举原版全部业务功能。\n来源覆盖：${s.coverage}\n已知边界：${s.limits.join('；')}`;}
export function comparisonRows(sources){return [
 ['原版平台',s=>({web:'网页',desktop:'电脑端',mobile:'手机端'})[s.platform]],
 ['来源版本',s=>s.sourceVersion],['技术栈',s=>s.stack.join(' / ')],['可用范围',s=>s.readiness],
 ['覆盖证据',s=>s.coverage],['适合参考',s=>s.features.join('；')],['保留边界',s=>s.limits.join('；')]
].map(([label,get])=>({label,values:sources.map(get)}));}

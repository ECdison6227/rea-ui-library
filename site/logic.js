export function filterSources(sources,{platform='all',query=''}={}){
 const words=query.trim().toLocaleLowerCase().split(/\s+/).filter(Boolean);
 return sources.filter(s=>(platform==='all'||s.platform===platform)&&words.every(w=>[s.name,s.subtitle,s.summary,...s.tags,...s.stack].join(' ').toLocaleLowerCase().includes(w)));
}
export function migrationPrompt(s){return `使用 $rea-design-transfer 应用规范模式。\n规范仓库：${s.sourceRepositoryUrl||'[填写规范仓库]'}\n规范ref：${s.sourceRef||'[填写ref/commit]'}（读取时记录实际取得的commit）\n来源：${s.name}（${s.sourceVersion}）\n来源目录：${s.root}\n设计入口：${s.designPackage||s.artifacts.find(a=>a.role==='readme')?.path||'README.md'}\n我的项目：[填写已完成功能和普通UI的项目路径]\n目标：对齐所选产品的视觉设计、组件/布局、关键动效及通用交互，保留我的业务、数据、品牌与合理的平台交互。\n新格式按design-package.json → DESIGN_SPEC.md → 所需模块/资产 → readiness与对照 → ADAPTATION_GUIDE.md读取；旧格式按现有入口显式映射。\n先审查目标源码和技术栈，建立规则/资产映射，再持续实施并运行对照。原版未知部分作明确D选择，不重新全量分析或默认穷举原业务功能。\n来源覆盖：${s.coverage}\n已知边界：${s.limits.join('；')}`;}
export function comparisonRows(sources){return [
 ['原版平台',s=>({web:'网页',desktop:'电脑端',mobile:'手机端'})[s.platform]],
 ['来源版本',s=>s.sourceVersion],['技术栈',s=>s.stack.join(' / ')],['可用范围',s=>s.readiness],
 ['覆盖证据',s=>s.coverage],['适合参考',s=>s.features.join('；')],['保留边界',s=>s.limits.join('；')]
].map(([label,get])=>({label,values:sources.map(get)}));}

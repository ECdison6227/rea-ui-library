export function filterSources(sources,{platform='all',query=''}={}){
 const words=query.trim().toLocaleLowerCase().split(/\s+/).filter(Boolean);
 return sources.filter(s=>(platform==='all'||s.platform===platform)&&words.every(w=>[s.name,s.subtitle,s.summary,...s.tags,...s.stack].join(' ').toLocaleLowerCase().includes(w)));
}
export function migrationPrompt(s){return `我的软件功能和基础UI已经完成，请直接把选定风格应用到项目。
使用 $rea-design-transfer 应用规范模式；未安装时先读仓库skills/rea-design-transfer/SKILL.md。
规范仓库：${s.sourceRepositoryUrl||'[填写规范仓库]'}
规范ref：${s.sourceRef||'main'}（拉取后记录实际取得的commit）
选定App：${s.name}（${s.sourceVersion}）
来源目录：${s.root}
设计入口：${s.designPackage||s.artifacts.find(a=>a.role==='readme')?.path||'README.md'}
我的项目：[填写已完成功能的项目路径]
请使用现有Git凭据拉取；可稀疏检出来源目录和skills/rea-design-transfer。先读design-package.json → DESIGN_SPEC.md → 所需模块/资产 → ADAPTATION_GUIDE.md与对照，再审查我的源码。
保留业务、数据、品牌和技术栈。实现字体、配色、间距、形状/表面、图标层级、组件状态、布局和动画，以及返回、输入、草稿、保存取消、加载、错误、重试等用户逻辑；不要只改颜色或静态首页。
未知原版参数采用包内明确D方案并在目标中实测；不要为原业务登录、支付或后端全测停住实施。持续完成代码、实际运行和前后/来源对照，交付修改与验证结果。
来源覆盖：${s.coverage}
已知边界：${s.limits.join('；')}`;}
export function comparisonRows(sources){return [
 ['原版平台',s=>({web:'网页',desktop:'电脑端',mobile:'手机端'})[s.platform]],
 ['来源版本',s=>s.sourceVersion],['技术栈',s=>s.stack.join(' / ')],['可用范围',s=>s.readiness],
 ['覆盖证据',s=>s.coverage],['适合参考',s=>s.features.join('；')],['保留边界',s=>s.limits.join('；')]
].map(([label,get])=>({label,values:sources.map(get)}));}

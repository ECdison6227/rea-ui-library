# 后续开发参考

这里是依据 APK 提炼的独立可读参考，不是原始项目源码；平台接口还需要由你自己的 Capacitor/Android 工程实现。

- `design-tokens.css`：从离线原前端在 393×852 Chromium 视口下读取的 154 个浅色/深色计算变量。引入后在根节点设置 `data-theme="light"` 或 `data-theme="dark"`。它只提供变量，不会自动生成组件。
- `ports.ts`：业务和平台之间的能力边界；部分方法为更清晰的封装接口命名，并非原生插件逐字签名。
- `update-policy.ts`：发布源解析、简化版本比较和更新 reducer。保留原实现的行为/局限，方便理解；没有下载/安装副作用，不应当作已经完成的生产更新服务。
- `permission-policy.ts`：授权和可靠性引导的纯决策逻辑。
- `reference.test.ts`：验证频道、无效摘要、活动下载保护、校验失败不可安装、安装器返回状态及权限拒绝等边界。

已用支持 TypeScript 类型擦除的 Node 26 运行：

```sh
node --test analysis/reference/reference.test.ts
```

测试通过不表示 Android 插件已经实现或通过真机认证。原始代码定位和新项目目录建议见 `../reports/05-architecture.md`。

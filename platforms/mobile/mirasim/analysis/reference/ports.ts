/** Independent architecture reference. Interfaces model observed boundaries, not original TS sources. */
export type Platform = 'android' | 'ios' | 'web';
export type NotificationPermission = 'granted' | 'denied' | 'prompt';
export type ListenerHandle = { remove(): Promise<void> | void };

export interface PublicPreferences {
  get(key: string): Promise<string | null>;
  set(key: string, value: string): Promise<void>;
  remove(key: string): Promise<void>;
}
export interface SecureVault extends PublicPreferences {}

export interface LifecyclePort {
  version(): Promise<string>;
  onForeground(callback: () => void): Promise<ListenerHandle>;
  onDeepLink(callback: (url: string) => void): Promise<ListenerHandle>;
  launchUrl(): Promise<string | null>;
}

export interface ApkUpdaterPort {
  canInstall(): Promise<{ allowed: boolean }>;
  /** Opens settings only; callers must query again after foreground resumes. */
  requestInstallPermission(): Promise<void>;
  download(input: { url: string; sha256: string; version: string }): Promise<{ path: string }>;
  install(input: { path: string }): Promise<void>;
  onProgress(callback: (progress: number) => void): Promise<ListenerHandle>;
}

export interface NotificationsPort {
  checkPermission(): Promise<NotificationPermission>;
  requestPermission(): Promise<NotificationPermission>;
  openAppSettings(): Promise<void>;
  openAttentionChannelSettings(): Promise<void>;
  postTask(input: { id: number; title: string; body: string; sessionKey?: string }): Promise<void>;
  cancel(id: number): Promise<void>;
  onTaskTap(callback: (sessionKey: string) => void): Promise<ListenerHandle>;
  onChatTap(callback: (link: string) => void): Promise<ListenerHandle>;
}

export interface BackgroundConnectionPort {
  start(text: string): Promise<void>;
  stop(): Promise<void>;
  batteryExempt(): Promise<boolean>;
  requestBatteryExemption(): Promise<void>;
  vendorAutostartAvailable(): Promise<boolean>;
  openVendorAutostart(): Promise<void>;
}

export interface HostClient {
  readonly connected: boolean;
  post(frame: { type: string; [key: string]: unknown }): void;
  onFrame(callback: (frame: { type: string; [key: string]: unknown }) => void): () => void;
  reconnectNow(): void;
  dispose(): void;
}

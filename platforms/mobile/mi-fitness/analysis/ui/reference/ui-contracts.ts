/**
 * Cross-business implementation contracts, synthesized from observed UI rules.
 * These interfaces are a new reference design, not the author's source code.
 * Evidence and source-specific exceptions are in interaction-contracts.json.
 */
export type ThemeMode = 'system' | 'day' | 'night';
export type EvidenceLevel = 'runtime-observed' | 'code-observed' | 'inferred' | 'adaptation';
export type ContentState<T> =
  | { kind: 'initial' }
  | { kind: 'loading'; previous?: T }
  | { kind: 'empty'; message: string; action?: UserAction }
  | { kind: 'ready'; data: T; refreshing: boolean; stale: boolean }
  | { kind: 'error'; message: string; previous?: T; retry?: UserAction }
  | { kind: 'restricted'; reason: 'permission' | 'account' | 'prerequisite'; action?: UserAction };
export interface UserAction { id: string; label: string; enabled: boolean; intent: string }
export interface NavigationItem { id: string; label: string; icon: string; destination: string; badge?: boolean }
export interface DetailContext { itemId: string; date?: string; sourcePage: string; returnAnchor?: string }
export interface SettingRow {
  id: string;
  title: string;
  description?: string;
  leadingIcon?: string;
  trailing: 'arrow' | 'switch' | 'value' | 'none';
  enabled: boolean;
  /** Some source rows remain clickable to explain an unmet prerequisite. */
  allowClickWhenDisabled: boolean;
  disabledExplanation?: string;
}
export interface PermissionGate {
  id: string;
  trigger: 'explicit-user-action' | 'automatic-reminder';
  granted: boolean;
  lastExplainedAt?: number;
  automaticReminderCooldownMs?: number;
}
export interface UiPlatformPorts {
  navigate(destination: string, context?: DetailContext): void;
  back(): void;
  announce(message: string): void;
  requestPermission(id: string): Promise<'granted' | 'denied' | 'blocked'>;
  openSystemSettings(id: string): Promise<void>;
  prepareAsset(id: string, onProgress: (progress: number) => void): Promise<string>;
  now(): number;
}
export interface StyleProfile {
  nativeAndRnAreSeparateSources: true;
  logicalUnitScale: number;
  fontScale: number;
  theme: ThemeMode;
  fontFallback: readonly string[];
  motion: { reduced: boolean; sourceProfile: 'native' | 'rn-navigator' | 'adapted' };
}

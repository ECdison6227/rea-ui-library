import type { NotificationPermission } from './ports.ts';

/** Mirrors the APK's decision to avoid repeatedly asking after denial. */
export function notificationAction(permission: NotificationPermission) {
  return permission === 'granted' ? 'continue' : permission === 'denied' ? 'open-settings' : 'request';
}

export type ReliabilityStep = 'battery' | 'autostart' | 'channel';
export interface ReliabilityContext {
  keepAliveSupported: boolean;
  batteryExempt: boolean | null;
  vendorAutostartAvailable: boolean;
  channelSilencingAdvised: boolean;
  channelAlreadyNudged: boolean;
}

/** Planning only: opened settings are NOT equivalent to granted permissions. */
export function reliabilitySteps(context: ReliabilityContext): ReliabilityStep[] {
  if (!context.keepAliveSupported) return [];
  const steps: ReliabilityStep[] = [];
  if (context.batteryExempt === false) steps.push('battery');
  if (context.vendorAutostartAvailable) steps.push('autostart');
  if (!context.channelAlreadyNudged && (context.channelSilencingAdvised || steps.length > 0)) steps.push('channel');
  return steps;
}

export const DEFAULT_NOTIFICATION_PREFERENCES = Object.freeze({ enabled: true, keepalive: true });
export const NOTIFICATION_KEYS = Object.freeze({
  enabled: 'mobile.notify.enabled',
  keepalive: 'mobile.notify.keepalive',
  setupNudged: 'mobile.notify.setupNudged.v2',
  channelNudged: 'mobile.notify.channelNudged',
});

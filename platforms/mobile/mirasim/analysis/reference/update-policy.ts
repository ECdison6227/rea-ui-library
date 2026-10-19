/** Independent readable reference for observed APK update decisions. No network or native side effects. */
export type Channel = 'stable' | 'beta';
export interface Release { version: string; url: string; sha256: string; size: number }
const record = (value: unknown): value is Record<string, unknown> => value !== null && typeof value === 'object';

/** Intentionally follows the APK's simplified comparison; this is not full SemVer. */
export function compareVersions(left: string, right: string): -1 | 0 | 1 {
  const parts = (value: string) => value.trim().replace(/^v/i, '').split(/[-+]/)[0].split('.').map(part => parseInt(part, 10) || 0);
  const a = parts(left), b = parts(right);
  for (let index = 0; index < Math.max(a.length, b.length); index++) {
    const delta = (a[index] ?? 0) - (b[index] ?? 0);
    if (delta) return delta > 0 ? 1 : -1;
  }
  return 0;
}

export function parseRelease(feed: unknown, channel: Channel = 'stable'): Release | null {
  if (!record(feed)) return null;
  if (typeof feed.schemaVersion === 'number' && feed.schemaVersion > 1) return null;
  const branch = channel === 'stable' ? feed.stable : feed;
  if (!record(branch) || !record(branch.android)) return null;
  const release = branch.android;
  if (typeof release.version !== 'string' || !release.version || typeof release.url !== 'string') return null;
  try { if (new URL(release.url).protocol !== 'https:') return null; } catch { return null; }
  if (typeof release.sha256 !== 'string' || !/^[0-9a-f]{64}$/i.test(release.sha256)) return null;
  if (typeof release.size !== 'number' || !Number.isFinite(release.size) || release.size <= 0) return null;
  return {version:release.version,url:release.url,sha256:release.sha256,size:release.size};
}

export function selectUpdate(feed: unknown, currentVersion: string | null, channel: Channel = 'stable'): Release | null {
  if (!currentVersion) return null;
  const release = parseRelease(feed, channel);
  return release && compareVersions(release.version, currentVersion) > 0 ? release : null;
}

export const UPDATE_TIMING = Object.freeze({ startupMs:8000, autoIntervalMs:1800000, pollMs:60000, timeoutMs:15000, manualMinMs:600 });

export type UpdatePhase = 'idle' | 'up-to-date' | 'available' | 'downloading' | 'ready' | 'installing' | 'error';
export type UpdateError = 'network' | 'storage' | 'checksum' | 'permission' | 'install';
export interface UpdateState {
  phase: UpdatePhase; checking: boolean; checkedAt: number | null; checkFailed: boolean;
  release: Release | null; progress: number; file: string | null; error: UpdateError | null;
}
export type UpdateEvent =
  | {type:'check-start'} | {type:'check-found'; release:Release; at:number}
  | {type:'check-none'; at:number} | {type:'check-failed'; manual:boolean}
  | {type:'download-start'} | {type:'download-progress'; progress:number}
  | {type:'download-done'; file:string} | {type:'download-failed'; reason:'network'|'storage'|'checksum'}
  | {type:'install-start'} | {type:'install-denied'} | {type:'install-dismissed'}
  | {type:'install-failed'} | {type:'retry'};

export function initialUpdateState(): UpdateState {
  return {phase:'idle',checking:false,checkedAt:null,checkFailed:false,release:null,progress:0,file:null,error:null};
}
export function reduceUpdate(state: UpdateState, event: UpdateEvent): UpdateState {
  const active = state.phase === 'downloading' || state.phase === 'installing';
  switch(event.type) {
    case 'check-start': return {...state,checking:true,checkFailed:false};
    case 'check-found': {
      const checked = {...state,checking:false,checkedAt:event.at,checkFailed:false};
      if(active) return checked;
      if(state.release?.version === event.release.version) return {...checked,release:event.release,phase:state.phase==='ready'||state.phase==='error'?state.phase:'available'};
      return {...checked,phase:'available',release:event.release,progress:0,file:null,error:null};
    }
    case 'check-none': {
      const checked = {...state,checking:false,checkedAt:event.at,checkFailed:false};
      return active ? checked : {...checked,phase:'up-to-date',release:null,progress:0,file:null,error:null};
    }
    case 'check-failed': return {...state,checking:false,checkFailed:event.manual||state.checkFailed};
    case 'download-start': return state.release?{...state,phase:'downloading',progress:0,file:null,error:null}:state;
    case 'download-progress': return state.phase==='downloading'?{...state,progress:Math.max(0,Math.min(1,event.progress))}:state;
    case 'download-done': return state.phase==='downloading'?{...state,phase:'ready',progress:1,file:event.file,error:null}:state;
    case 'download-failed': return state.phase==='downloading'?{...state,phase:'error',progress:0,file:null,error:event.reason}:state;
    case 'install-start': return state.file?{...state,phase:'installing',error:null}:state;
    case 'install-denied': return {...state,phase:state.file?'ready':'available',error:'permission'};
    case 'install-dismissed': return state.phase==='installing'?{...state,phase:'ready',error:null}:state;
    case 'install-failed': return {...state,phase:'error',error:'install'};
    case 'retry': return state.release?{...state,phase:'available',progress:0,file:null,error:null}:{...initialUpdateState(),checkedAt:state.checkedAt};
  }
}

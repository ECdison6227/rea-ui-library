package ai.mirofish.mirasim;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.internal.ImagesContract;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import okhttp3.OkHttpClient;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class MachineAttentionNotifier implements MachineAttentionSocket.Listener {
    private static final long POST_DELAY_MS = 2000;
    private static final String TAG = "mirasim.attention-link";
    private final Context context;
    private final Map<String, Pending> pending;
    private final MachineAttentionSocket socket;
    private final ScheduledExecutorService timer;
    private String userId;

    /* JADX INFO: Access modifiers changed from: private */
    static final class Pending {
        final TreeMap<Long, JSONObject> frames = new TreeMap<>();
        final String machine;
        final String room;
        ScheduledFuture<?> task;

        Pending(String str, String str2) {
            this.machine = str;
            this.room = str2;
        }
    }

    MachineAttentionNotifier(Context context) {
        ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.timer = scheduledExecutorServiceNewSingleThreadScheduledExecutor;
        this.userId = "";
        this.pending = new HashMap();
        this.context = context.getApplicationContext();
        this.socket = new MachineAttentionSocket(new OkHttpClient(), scheduledExecutorServiceNewSingleThreadScheduledExecutor, new LongSupplier() { // from class: ai.mirofish.mirasim.MachineAttentionNotifier$$ExternalSyntheticLambda1
            @Override // java.util.function.LongSupplier
            public final long getAsLong() {
                return SystemClock.elapsedRealtime();
            }
        }, new MachineAttentionSocket.Timing(), this);
    }

    synchronized void ensureStarted(JSONObject jSONObject) {
        String str = ChatText.str(jSONObject, "ticket");
        String str2 = ChatText.str(jSONObject, ImagesContract.URL);
        long jOptLong = jSONObject == null ? 0L : jSONObject.optLong("expiresAt", 0L);
        if (!str.isEmpty() && str2.startsWith("wss://") && (jOptLong <= 0 || jOptLong > System.currentTimeMillis())) {
            this.userId = ChatText.str(jSONObject, "userId");
            this.socket.start(str2, str);
            return;
        }
        this.socket.stop();
        dropPending();
    }

    void nudge() {
        this.socket.nudge();
    }

    synchronized void close() {
        this.socket.close();
        dropPending();
        this.timer.shutdown();
    }

    @Override // ai.mirofish.mirasim.MachineAttentionSocket.Listener
    public void onReady(String str, long j) {
        Log.i(TAG, "attention link ready");
    }

    @Override // ai.mirofish.mirasim.MachineAttentionSocket.Listener
    public void onAttention(JSONObject jSONObject) {
        consider(jSONObject);
    }

    @Override // ai.mirofish.mirasim.MachineAttentionSocket.Listener
    public void onTicketRefused(String str) {
        JSONObject jSONObjectLoadAttentionTicket = ChannelCredentialStore.loadAttentionTicket(this.context);
        if (jSONObjectLoadAttentionTicket != null && str.equals(ChatText.str(jSONObjectLoadAttentionTicket, "ticket"))) {
            ChannelCredentialStore.clearAttentionTicket(this.context);
        }
        ForegroundServicePlugin.dispatchAttentionTicketRefused();
    }

    @Override // ai.mirofish.mirasim.MachineAttentionSocket.Listener
    public void onDiagnostic(String str) {
        Log.i(TAG, str);
    }

    private synchronized void consider(JSONObject jSONObject) {
        String str = ChatText.str(jSONObject, "machine");
        String str2 = ChatText.str(jSONObject, "room");
        long jOptLong = jSONObject.optLong("seq", 0L);
        String str3 = ChatText.str(jSONObject, "kind");
        if (!str.isEmpty() && !str2.isEmpty() && jOptLong > 0) {
            if (!"reaction".equals(str3) && (this.userId.isEmpty() || !this.userId.equals(ChatText.str(jSONObject, "by")))) {
                if (ChatRowLedger.handled(str, str2, jOptLong)) {
                    return;
                }
                final String str4 = str + '\n' + str2;
                final Pending pending = this.pending.get(str4);
                if (pending == null) {
                    Map<String, Pending> map = this.pending;
                    Pending pending2 = new Pending(str, str2);
                    map.put(str4, pending2);
                    pending = pending2;
                }
                if (pending.frames.containsKey(Long.valueOf(jOptLong))) {
                    return;
                }
                pending.frames.put(Long.valueOf(jOptLong), jSONObject);
                if (pending.task != null) {
                    pending.task.cancel(false);
                }
                pending.task = later(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionNotifier$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.lambda$consider$0(str4, pending);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:28:0x0099
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1478)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /* JADX INFO: renamed from: decide, reason: merged with bridge method [inline-methods] */
    public void lambda$consider$0(java.lang.String r11, ai.mirofish.mirasim.MachineAttentionNotifier.Pending r12) throws java.lang.Throwable {
        /*
            r10 = this;
            monitor-enter(r10)
            java.util.Map<java.lang.String, ai.mirofish.mirasim.MachineAttentionNotifier$Pending> r0 = r10.pending     // Catch: java.lang.Throwable -> L94
            java.lang.Object r0 = r0.get(r11)     // Catch: java.lang.Throwable -> L94
            if (r0 == r12) goto L10
            monitor-exit(r10)     // Catch: java.lang.Throwable -> Lb
            return
        Lb:
            r0 = move-exception
            r11 = r0
            r4 = r10
            goto L97
        L10:
            java.util.Map<java.lang.String, ai.mirofish.mirasim.MachineAttentionNotifier$Pending> r0 = r10.pending     // Catch: java.lang.Throwable -> L94
            r0.remove(r11)     // Catch: java.lang.Throwable -> L94
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L94
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.util.TreeMap<java.lang.Long, org.json.JSONObject> r1 = r12.frames
            java.util.Set r1 = r1.entrySet()
            java.util.Iterator r1 = r1.iterator()
        L2a:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L72
            java.lang.Object r2 = r1.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.String r3 = r12.machine
            java.lang.String r4 = r12.room
            java.lang.Object r5 = r2.getKey()
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            boolean r3 = ai.mirofish.mirasim.ChatRowLedger.wasSeenByApp(r3, r4, r5)
            if (r3 == 0) goto L4b
            goto L2a
        L4b:
            java.lang.String r5 = r12.machine
            java.lang.String r6 = r12.room
            java.lang.Object r3 = r2.getKey()
            java.lang.Long r3 = (java.lang.Long) r3
            long r7 = r3.longValue()
            java.lang.Object r3 = r2.getValue()
            r9 = r3
            org.json.JSONObject r9 = (org.json.JSONObject) r9
            r4 = r10
            ai.mirofish.mirasim.ChatRowLedger$Post r3 = r4.post(r5, r6, r7, r9)
            r11.add(r3)
            java.lang.Object r2 = r2.getValue()
            org.json.JSONObject r2 = (org.json.JSONObject) r2
            r0.put(r3, r2)
            goto L2a
        L72:
            r4 = r10
            android.content.Context r12 = r4.context
            java.util.List r11 = ai.mirofish.mirasim.ChatRows.post(r12, r11)
            java.util.Iterator r11 = r11.iterator()
        L7d:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L93
            java.lang.Object r12 = r11.next()
            ai.mirofish.mirasim.ChatRowLedger$Post r12 = (ai.mirofish.mirasim.ChatRowLedger.Post) r12
            java.lang.Object r12 = r0.get(r12)
            org.json.JSONObject r12 = (org.json.JSONObject) r12
            ai.mirofish.mirasim.ForegroundServicePlugin.dispatchMachineAttention(r12)
            goto L7d
        L93:
            return
        L94:
            r0 = move-exception
            r4 = r10
        L96:
            r11 = r0
        L97:
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L99
            throw r11
        L99:
            r0 = move-exception
            goto L96
        */
        throw new UnsupportedOperationException("Method not decompiled: ai.mirofish.mirasim.MachineAttentionNotifier.lambda$consider$0(java.lang.String, ai.mirofish.mirasim.MachineAttentionNotifier$Pending):void");
    }

    private ChatRowLedger.Post post(String str, String str2, long j, JSONObject jSONObject) {
        String str3 = ChatText.str(jSONObject, "byName");
        if (str3.isEmpty()) {
            str3 = this.context.getString(R.string.chat_someone);
        }
        String strWithKind = ChatText.withKind(this.context, ChatText.str(jSONObject, "kind"), ChatText.previewText(this.context, jSONObject.optJSONObject("preview")));
        boolean zStartsWith = str2.startsWith("dm-");
        return new ChatRowLedger.Post(AttentionPoster.machineChatNotificationId(str, str2, j), ChatLinks.deepLink(str2, j, str), str3, strWithKind).title(this.context.getString(R.string.chat_title_other_machine, zStartsWith ? str3 : ChatText.roomLabel(this.context, str2)), false).group(Boolean.valueOf(!zStartsWith));
    }

    private ScheduledFuture<?> later(Runnable runnable) {
        try {
            return this.timer.schedule(runnable, POST_DELAY_MS, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException unused) {
            return null;
        }
    }

    private void dropPending() {
        for (Pending pending : this.pending.values()) {
            if (pending.task != null) {
                pending.task.cancel(false);
            }
        }
        this.pending.clear();
    }
}
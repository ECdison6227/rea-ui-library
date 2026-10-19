package ai.mirofish.mirasim;

import androidx.camera.video.AudioStats;
import androidx.core.view.PointerIconCompat;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class MachineAttentionSocket {
    static final int CLOSE_IDLE = 4408;
    static final int CLOSE_SUPERSEDED = 4409;
    static final int CLOSE_UNAUTHORIZED = 4401;
    private ScheduledFuture<?> authTask;
    private long backoffMs;
    private final LongSupplier clock;
    private final OkHttpClient http;
    private long lastInboundAt;
    private final Listener listener;
    private long pingId;
    private int protocolStrikes;
    private boolean protocolWait;
    private ScheduledFuture<?> redialTask;
    private WebSocket socket;
    private ScheduledFuture<?> tickTask;
    private String ticket;
    private final ScheduledExecutorService timer;
    private final Timing timing;
    private String url;
    private final Random random = new Random();
    private State state = State.IDLE;

    interface Listener {
        void onAttention(JSONObject jSONObject);

        void onDiagnostic(String str);

        void onReady(String str, long j);

        void onTicketRefused(String str);
    }

    private enum State {
        IDLE,
        CONNECTING,
        AUTHING,
        READY,
        WAITING,
        REFUSED,
        CLOSED
    }

    private static boolean refusesUpgrade(int i) {
        return (i >= 400 && i < 500 && i != 408 && i != 429) || i == 501;
    }

    static final class Timing {
        long pingMs = 30000;
        long deadAfterMs = 120000;
        long authWaitMs = 15000;
        long backoffMinMs = 3000;
        long backoffMaxMs = 120000;
        long protocolBackoffMinMs = 300000;
        long protocolBackoffMaxMs = 3600000;
        double jitter = 0.2d;

        Timing() {
        }
    }

    MachineAttentionSocket(OkHttpClient okHttpClient, ScheduledExecutorService scheduledExecutorService, LongSupplier longSupplier, Timing timing, Listener listener) {
        this.http = okHttpClient;
        this.timer = scheduledExecutorService;
        this.clock = longSupplier;
        this.timing = timing;
        this.listener = listener;
        this.backoffMs = timing.backoffMinMs;
    }

    synchronized void start(String str, String str2) {
        if (this.state == State.CLOSED) {
            return;
        }
        if (str.equals(this.url) && str2.equals(this.ticket) && this.state != State.IDLE) {
            return;
        }
        this.url = str;
        this.ticket = str2;
        this.backoffMs = this.timing.backoffMinMs;
        this.protocolStrikes = 0;
        connectNow("start");
    }

    synchronized void stop() {
        if (this.state == State.CLOSED) {
            return;
        }
        teardown(true);
        cancelTimers();
        this.url = null;
        this.ticket = null;
        this.state = State.IDLE;
    }

    synchronized void close() {
        stop();
        this.state = State.CLOSED;
    }

    synchronized void nudge() {
        int iOrdinal = this.state.ordinal();
        if (iOrdinal != 3) {
            if (iOrdinal != 4) {
                return;
            }
            if (this.protocolWait) {
                return;
            }
            this.backoffMs = this.timing.backoffMinMs;
            connectNow("wake");
            return;
        }
        if (this.clock.getAsLong() - this.lastInboundAt < this.timing.deadAfterMs) {
            sendPing();
            return;
        }
        diagnose("silent past the dead line at a wake — redialing");
        this.backoffMs = this.timing.backoffMinMs;
        connectNow("wake");
    }

    private void connectNow(String str) {
        teardown(false);
        cancel(this.redialTask);
        this.redialTask = null;
        this.protocolWait = false;
        if (this.state == State.CLOSED || this.url == null || this.ticket == null) {
            return;
        }
        this.state = State.CONNECTING;
        this.lastInboundAt = this.clock.getAsLong();
        try {
            this.socket = this.http.newWebSocket(new Request.Builder().url(this.url).build(), new WebSocketListener() { // from class: ai.mirofish.mirasim.MachineAttentionSocket.1
                @Override // okhttp3.WebSocketListener
                public void onOpen(WebSocket webSocket, Response response) {
                    MachineAttentionSocket.this.opened(webSocket);
                }

                @Override // okhttp3.WebSocketListener
                public void onMessage(WebSocket webSocket, String str2) {
                    MachineAttentionSocket.this.received(webSocket, str2);
                }

                @Override // okhttp3.WebSocketListener
                public void onClosing(WebSocket webSocket, int i, String str2) {
                    webSocket.close(1000, null);
                    MachineAttentionSocket.this.closed(webSocket, i, str2);
                }

                @Override // okhttp3.WebSocketListener
                public void onClosed(WebSocket webSocket, int i, String str2) {
                    MachineAttentionSocket.this.closed(webSocket, i, str2);
                }

                @Override // okhttp3.WebSocketListener
                public void onFailure(WebSocket webSocket, Throwable th, Response response) {
                    MachineAttentionSocket.this.failed(webSocket, th, response);
                }
            });
            if (this.tickTask == null) {
                this.tickTask = repeat(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.tick();
                    }
                }, this.timing.pingMs);
            }
            diagnose("dial (" + str + ")");
        } catch (IllegalArgumentException unused) {
            diagnose("not a dialable url — parked");
            this.state = State.IDLE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void opened(final WebSocket webSocket) {
        if (webSocket != this.socket) {
            return;
        }
        this.state = State.AUTHING;
        this.lastInboundAt = this.clock.getAsLong();
        try {
            webSocket.send(new JSONObject().put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "attentionAuth").put("ticket", this.ticket).toString());
            cancel(this.authTask);
            this.authTask = later(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$opened$0(webSocket);
                }
            }, this.timing.authWaitMs);
        } catch (JSONException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: authOverdue, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$opened$0(WebSocket webSocket) {
        if (webSocket == this.socket && this.state == State.AUTHING) {
            diagnose("no attentionReady in time");
            teardown(false);
            scheduleRedial(nextBackoff(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void received(WebSocket webSocket, String str) {
        final JSONObject jSONObject;
        Runnable runnable = null;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException unused) {
            jSONObject = null;
        }
        synchronized (this) {
            if (webSocket != this.socket) {
                return;
            }
            this.lastInboundAt = this.clock.getAsLong();
            if (jSONObject == null) {
                return;
            }
            String strOptString = jSONObject.optString(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "");
            if ("attentionReady".equals(strOptString) && this.state == State.AUTHING) {
                this.state = State.READY;
                cancel(this.authTask);
                this.authTask = null;
                final String strOptString2 = jSONObject.isNull("userId") ? "" : jSONObject.optString("userId", "");
                final long jOptLong = jSONObject.optLong("expiresAt", 0L);
                diagnose("ready");
                runnable = new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$received$1(strOptString2, jOptLong);
                    }
                };
            } else if ("pong".equals(strOptString)) {
                this.backoffMs = this.timing.backoffMinMs;
                this.protocolStrikes = 0;
            } else if ("chatMachineAttention".equals(strOptString) && this.state == State.READY) {
                runnable = new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$received$2(jSONObject);
                    }
                };
            }
            if (runnable != null) {
                dispatch(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$received$1(String str, long j) {
        this.listener.onReady(str, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$received$2(JSONObject jSONObject) {
        this.listener.onAttention(jSONObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void closed(WebSocket webSocket, int i, String str) {
        if (webSocket != this.socket) {
            return;
        }
        this.socket = null;
        cancel(this.authTask);
        this.authTask = null;
        diagnose("closed " + i + ((str == null || str.isEmpty()) ? "" : " (" + str + ")"));
        if (i == CLOSE_UNAUTHORIZED) {
            this.state = State.REFUSED;
            cancelTimers();
            final String str2 = this.ticket;
            dispatch(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$closed$3(str2);
                }
            });
            return;
        }
        if (i == CLOSE_IDLE) {
            this.backoffMs = this.timing.backoffMinMs;
            connectNow("idle");
        } else {
            switch (i) {
                case PointerIconCompat.TYPE_CROSSHAIR /* 1007 */:
                case PointerIconCompat.TYPE_TEXT /* 1008 */:
                case PointerIconCompat.TYPE_VERTICAL_TEXT /* 1009 */:
                    scheduleRedial(protocolBackoff(), true);
                    return;
                default:
                    scheduleRedial(nextBackoff(), false);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closed$3(String str) {
        this.listener.onTicketRefused(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void failed(WebSocket webSocket, Throwable th, Response response) {
        if (webSocket != this.socket) {
            return;
        }
        this.socket = null;
        cancel(this.authTask);
        this.authTask = null;
        int iCode = response == null ? 0 : response.code();
        diagnose("failure " + (iCode > 0 ? "HTTP " + iCode : String.valueOf(th)));
        if (refusesUpgrade(iCode)) {
            scheduleRedial(protocolBackoff(), true);
        } else {
            scheduleRedial(nextBackoff(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void tick() {
        if (this.state == State.READY && this.socket != null) {
            if (this.clock.getAsLong() - this.lastInboundAt > this.timing.deadAfterMs) {
                diagnose("silent for " + this.timing.deadAfterMs + " ms — the link is dead");
                teardown(false);
                scheduleRedial(nextBackoff(), false);
                return;
            }
            sendPing();
        }
    }

    private void sendPing() {
        WebSocket webSocket = this.socket;
        if (webSocket != null) {
            StringBuilder sb = new StringBuilder("{\"type\":\"ping\",\"id\":");
            long j = this.pingId + 1;
            this.pingId = j;
            webSocket.send(sb.append(j).append("}").toString());
        }
    }

    private void scheduleRedial(long j, boolean z) {
        cancel(this.redialTask);
        this.state = State.WAITING;
        this.protocolWait = z;
        long jJittered = jittered(j);
        diagnose("redial in " + jJittered + " ms");
        this.redialTask = later(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRedial$4();
            }
        }, jJittered);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRedial$4() {
        synchronized (this) {
            this.redialTask = null;
            if (this.state == State.WAITING) {
                connectNow("backoff");
            }
        }
    }

    private long nextBackoff() {
        long j = this.backoffMs;
        this.backoffMs = Math.min(this.timing.backoffMaxMs, this.backoffMs * 2);
        return j;
    }

    private long protocolBackoff() {
        this.protocolStrikes = Math.min(this.protocolStrikes + 1, 16);
        long j = this.timing.protocolBackoffMinMs;
        for (int i = 1; i < this.protocolStrikes && j < this.timing.protocolBackoffMaxMs; i++) {
            j *= 2;
        }
        return Math.min(j, this.timing.protocolBackoffMaxMs);
    }

    private long jittered(long j) {
        return this.timing.jitter <= AudioStats.AUDIO_AMPLITUDE_NONE ? j : Math.max(0L, Math.round(j * ((((this.random.nextDouble() * 2.0d) - 1.0d) * this.timing.jitter) + 1.0d)));
    }

    private void teardown(boolean z) {
        WebSocket webSocket = this.socket;
        this.socket = null;
        cancel(this.authTask);
        this.authTask = null;
        if (webSocket == null) {
            return;
        }
        if (z) {
            webSocket.close(1000, null);
        } else {
            webSocket.cancel();
        }
    }

    private void cancelTimers() {
        cancel(this.tickTask);
        this.tickTask = null;
        cancel(this.redialTask);
        this.redialTask = null;
        cancel(this.authTask);
        this.authTask = null;
    }

    private void diagnose(final String str) {
        dispatch(new Runnable() { // from class: ai.mirofish.mirasim.MachineAttentionSocket$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$diagnose$5(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$diagnose$5(String str) {
        this.listener.onDiagnostic(str);
    }

    private void dispatch(Runnable runnable) {
        try {
            this.timer.execute(runnable);
        } catch (RejectedExecutionException unused) {
        }
    }

    private ScheduledFuture<?> later(Runnable runnable, long j) {
        try {
            return this.timer.schedule(runnable, j, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException unused) {
            return null;
        }
    }

    private ScheduledFuture<?> repeat(Runnable runnable, long j) {
        try {
            return this.timer.scheduleWithFixedDelay(runnable, j, j, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException unused) {
            return null;
        }
    }

    private static void cancel(ScheduledFuture<?> scheduledFuture) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }
}
package ai.mirofish.mirasim;

import android.content.Context;
import android.util.Log;
import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlinx.coroutines.DebugKt;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class NotifyChannel {
    private static final long BACKOFF_MAX_MS = 120000;
    private static final long BACKOFF_MIN_MS = 3000;
    private static final long DEAD_AFTER_MS = 120000;
    private static final long JS_ALIVE_MS = 45000;
    private static final long PING_MS = 30000;
    private static final String TAG = "mirasim.notify-channel";
    private boolean authRejected;
    private boolean authed;
    private long backoffMs;
    private final ChatNotifier chat;
    private boolean closed;
    private final Context context;
    private JSONObject credential;
    private MirachannelE2e e2e;
    private final OkHttpClient http;
    private long lastInboundAt;
    private ScheduledFuture<?> pingTask;
    private ScheduledFuture<?> redialTask;
    private String sessionId;
    private WebSocket socket;
    private String startedWith;
    private boolean stopped;
    private final ScheduledExecutorService timer;

    NotifyChannel(Context context) {
        ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.timer = scheduledExecutorServiceNewSingleThreadScheduledExecutor;
        this.http = new OkHttpClient();
        this.backoffMs = BACKOFF_MIN_MS;
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.chat = new ChatNotifier(applicationContext, scheduledExecutorServiceNewSingleThreadScheduledExecutor, new ChatNotifier.Wire() { // from class: ai.mirofish.mirasim.NotifyChannel$$ExternalSyntheticLambda0
            @Override // ai.mirofish.mirasim.ChatNotifier.Wire
            public final void send(JSONObject jSONObject) {
                this.f$0.sendHostEnvelopeQuietly(jSONObject);
            }
        });
    }

    synchronized void ensureStarted(JSONObject jSONObject) {
        String string = jSONObject.toString();
        if (!string.equals(this.startedWith) || this.stopped || this.authRejected) {
            this.startedWith = string;
            start(jSONObject);
        }
    }

    synchronized void start(JSONObject jSONObject) {
        if (this.closed) {
            return;
        }
        this.credential = jSONObject;
        this.authRejected = false;
        this.stopped = false;
        this.backoffMs = BACKOFF_MIN_MS;
        reconnectNow();
    }

    synchronized void stop() {
        this.stopped = true;
        teardown();
        ScheduledFuture<?> scheduledFuture = this.pingTask;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            this.pingTask = null;
        }
    }

    synchronized void close() {
        stop();
        this.closed = true;
        this.timer.shutdown();
    }

    synchronized void nudge(String str) {
        if (!this.stopped && !this.authRejected && this.credential != null) {
            if (this.socket != null && System.currentTimeMillis() - this.lastInboundAt < 120000) {
                sendMirachannel("{\"type\":\"ping\"}");
            } else {
                this.backoffMs = BACKOFF_MIN_MS;
                reconnectNow();
            }
        }
    }

    private synchronized void reconnectNow() {
        JSONObject jSONObject;
        teardown();
        if (!this.stopped && !this.authRejected && (jSONObject = this.credential) != null) {
            String strOptString = jSONObject.optString("rendezvous", "");
            final String strOptString2 = this.credential.optString("nodeId", "");
            if (!strOptString.isEmpty() && !strOptString2.isEmpty()) {
                Request requestBuild = new Request.Builder().url(strOptString).build();
                this.lastInboundAt = System.currentTimeMillis();
                this.socket = this.http.newWebSocket(requestBuild, new WebSocketListener() { // from class: ai.mirofish.mirasim.NotifyChannel.1
                    @Override // okhttp3.WebSocketListener
                    public void onOpen(WebSocket webSocket, Response response) {
                        try {
                            JSONObject jSONObject2 = new JSONObject();
                            jSONObject2.put("t", "dial");
                            jSONObject2.put("nodeId", strOptString2);
                            jSONObject2.put(NotificationCompat.CATEGORY_TRANSPORT, "relay");
                            webSocket.send(jSONObject2.toString());
                        } catch (Exception e) {
                            Log.w(NotifyChannel.TAG, "dial failed", e);
                        }
                    }

                    @Override // okhttp3.WebSocketListener
                    public void onMessage(WebSocket webSocket, String str) {
                        NotifyChannel.this.onWire(str);
                    }

                    @Override // okhttp3.WebSocketListener
                    public void onClosed(WebSocket webSocket, int i, String str) {
                        NotifyChannel.this.scheduleRedial();
                    }

                    @Override // okhttp3.WebSocketListener
                    public void onFailure(WebSocket webSocket, Throwable th, Response response) {
                        NotifyChannel.this.scheduleRedial();
                    }
                });
                if (this.pingTask == null) {
                    this.pingTask = this.timer.scheduleWithFixedDelay(new Runnable() { // from class: ai.mirofish.mirasim.NotifyChannel$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.pingTick();
                        }
                    }, PING_MS, PING_MS, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    private synchronized void teardown() {
        this.chat.onLinkLost();
        WebSocket webSocket = this.socket;
        if (webSocket != null) {
            try {
                webSocket.close(1000, null);
            } catch (Exception unused) {
            }
            this.socket = null;
        }
        this.sessionId = null;
        this.e2e = null;
        this.authed = false;
        ScheduledFuture<?> scheduledFuture = this.redialTask;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            this.redialTask = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void scheduleRedial() {
        this.chat.onLinkLost();
        this.socket = null;
        this.sessionId = null;
        this.e2e = null;
        this.authed = false;
        if (!this.stopped && !this.authRejected && this.redialTask == null) {
            long j = this.backoffMs;
            this.backoffMs = Math.min(120000L, 2 * j);
            this.redialTask = this.timer.schedule(new Runnable() { // from class: ai.mirofish.mirasim.NotifyChannel$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleRedial$0();
                }
            }, j, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRedial$0() {
        synchronized (this) {
            this.redialTask = null;
            reconnectNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void pingTick() {
        if (this.socket != null && this.authed) {
            if (System.currentTimeMillis() - this.lastInboundAt > 120000) {
                scheduleRedial();
            } else {
                sendMirachannel("{\"type\":\"ping\"}");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onWire(String str) {
        this.lastInboundAt = System.currentTimeMillis();
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("t", "");
            if ("dialed".equals(strOptString)) {
                this.sessionId = jSONObject.getString("sessionId");
                if (!this.credential.optString("e2e", "").isEmpty()) {
                    MirachannelE2e mirachannelE2e = new MirachannelE2e();
                    this.e2e = mirachannelE2e;
                    sendRelay(mirachannelE2e.sec1());
                } else {
                    sendRelay(helloFrame());
                }
                return;
            }
            if ("relay".equals(strOptString)) {
                String str2 = this.sessionId;
                if (str2 != null && str2.equals(jSONObject.optString("sessionId"))) {
                    onInner(jSONObject.getString("d"));
                    return;
                }
                return;
            }
            if ("end".equals(strOptString)) {
                scheduleRedial();
            }
        } catch (Exception e) {
            Log.w(TAG, "wire frame dropped", e);
        }
    }

    private void onInner(String str) throws Exception {
        MirachannelE2e mirachannelE2e;
        MirachannelE2e mirachannelE2e2;
        JSONObject jSONObject = new JSONObject(str);
        String strOptString = jSONObject.optString(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "");
        if ("sec2".equals(strOptString) && (mirachannelE2e2 = this.e2e) != null) {
            mirachannelE2e2.onSec2(jSONObject, this.credential.optString("e2e", ""));
            sendRelay(this.e2e.seal(helloFrame()));
        } else if ("sec".equals(strOptString) && (mirachannelE2e = this.e2e) != null && mirachannelE2e.ready()) {
            onMirachannel(new JSONObject(this.e2e.open(jSONObject)));
        } else {
            onMirachannel(jSONObject);
        }
    }

    private void onMirachannel(JSONObject jSONObject) throws Exception {
        String strOptString = jSONObject.optString(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "");
        if ("welcome".equals(strOptString)) {
            this.authed = true;
            this.backoffMs = BACKOFF_MIN_MS;
            sendHostEnvelope(new JSONObject().put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "clientVisibility").put("visible", false));
            sendHostEnvelope(new JSONObject().put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "watchAttention").put(DebugKt.DEBUG_PROPERTY_VALUE_ON, true).put("sinceEdgeSeq", ChannelCredentialStore.lastEdgeSeq(this.context)).put("mine", true));
            this.chat.onWelcome(this.credential.optString("nodeId", ""));
            return;
        }
        if ("error".equals(strOptString)) {
            Log.w(TAG, "channel error: " + jSONObject);
            this.authRejected = true;
            teardown();
        } else if ("host".equals(strOptString)) {
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("payload");
            String strOptString2 = jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.optString(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "") : "";
            if ("attention".equals(strOptString2)) {
                onAttention(jSONObjectOptJSONObject);
            } else if (strOptString2.startsWith("chat")) {
                this.chat.onHostFrame(jSONObjectOptJSONObject);
            }
        }
    }

    private void onAttention(JSONObject jSONObject) {
        long jLastEdgeSeq = ChannelCredentialStore.lastEdgeSeq(this.context);
        long jJsAliveAgoMs = ForegroundServicePlugin.jsAliveAgoMs();
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("edges");
        if (jSONArrayOptJSONArray != null) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    long jOptLong = jSONObjectOptJSONObject.optLong("edgeSeq", 0L);
                    if (jOptLong > jLastEdgeSeq) {
                        jLastEdgeSeq = Math.max(jLastEdgeSeq, jOptLong);
                        if (jJsAliveAgoMs >= JS_ALIVE_MS) {
                            postEdge(jSONObjectOptJSONObject);
                        }
                    }
                }
            }
        }
        ChannelCredentialStore.saveLastEdgeSeq(this.context, Math.max(jLastEdgeSeq, jSONObject.optLong("latest", jLastEdgeSeq)));
    }

    /* JADX WARN: Code duplicated, block: B:28:0x00c7 A[PHI: r3
      0x00c7: PHI (r3v11 java.lang.String) = (r3v10 java.lang.String), (r3v13 java.lang.String) binds: [B:21:0x00a1, B:26:0x00c4] A[DONT_GENERATE, DONT_INLINE]] */
    private void postEdge(JSONObject jSONObject) {
        String string;
        String strOptString = jSONObject.optString("sessionKey", "");
        String strOptString2 = jSONObject.optString("kind", "settled");
        String strOptString3 = jSONObject.optString(NotificationCompat.CATEGORY_STATUS, "done");
        String strOptString4 = jSONObject.optString("prompt", "");
        String strOptString5 = jSONObject.optString("title", "");
        if (strOptString4.isEmpty()) {
            strOptString4 = strOptString5;
        }
        String strOptString6 = jSONObject.optString("reply", "");
        String strOptString7 = jSONObject.optString("askTitle", "");
        if ("waiting".equals(strOptString2)) {
            string = this.context.getString(R.string.notify_waiting);
        } else if ("failed".equals(strOptString3)) {
            string = this.context.getString(R.string.notify_failed);
        } else if ("stalled".equals(strOptString3)) {
            string = this.context.getString(R.string.notify_stalled);
        } else {
            string = this.context.getString(R.string.notify_done);
        }
        if ("waiting".equals(strOptString2) && !strOptString7.isEmpty()) {
            strOptString4 = strOptString4 + " | " + strOptString7;
        }
        if ("waiting".equals(strOptString2)) {
            strOptString6 = strOptString4;
        } else {
            if (!strOptString5.isEmpty()) {
                string = string + " · " + strOptString5;
            }
            if (strOptString6.isEmpty()) {
                strOptString6 = strOptString4;
            }
        }
        Context context = this.context;
        AttentionPoster.ensureChannel(context, context.getString(R.string.notify_channel_name), this.context.getString(R.string.notify_channel_desc));
        AttentionPoster.post(this.context, AttentionPoster.notificationId(strOptString), string, strOptString6, strOptString);
    }

    private String helloFrame() throws Exception {
        return new JSONObject().put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "hello").put("v", 1).put("client", new JSONObject().put(HintConstants.AUTOFILL_HINT_NAME, "mirasim-notify").put("platform", "android-native")).put("token", this.credential.optString("token", "")).toString();
    }

    private void sendHostEnvelope(JSONObject jSONObject) throws Exception {
        sendMirachannel(new JSONObject().put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "host").put("payload", jSONObject).toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHostEnvelopeQuietly(JSONObject jSONObject) {
        try {
            sendHostEnvelope(jSONObject);
        } catch (Exception e) {
            Log.w(TAG, "chat frame not sent", e);
        }
    }

    private void sendMirachannel(String str) {
        try {
            MirachannelE2e mirachannelE2e = this.e2e;
            if (mirachannelE2e != null && mirachannelE2e.ready()) {
                str = this.e2e.seal(str);
            }
            sendRelay(str);
        } catch (Exception e) {
            Log.w(TAG, "send failed", e);
        }
    }

    private void sendRelay(String str) throws Exception {
        WebSocket webSocket = this.socket;
        String str2 = this.sessionId;
        if (webSocket == null || str2 == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("t", "relay");
        jSONObject.put("sessionId", str2);
        jSONObject.put("d", str);
        webSocket.send(jSONObject.toString());
    }
}
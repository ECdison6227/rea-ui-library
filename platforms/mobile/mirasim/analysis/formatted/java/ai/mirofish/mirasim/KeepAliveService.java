package ai.mirofish.mirasim;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class KeepAliveService extends Service {
    static final String ACTION_RESYNC = "ai.mirofish.mirasim.KEEPALIVE_RESYNC";
    static final String ACTION_STOP = "ai.mirofish.mirasim.KEEPALIVE_STOP";
    static final String CHANNEL_ID = "mirasim.keepalive";
    static final String EXTRA_TEXT = "text";
    static final int NOTIFICATION_ID = 1001;
    private MachineAttentionNotifier attention;
    private NotifyChannel channel;
    private WakeSources wakeSources;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stopChannel();
            stopForeground(1);
            stopSelf();
            return 2;
        }
        if (intent != null && ACTION_RESYNC.equals(intent.getAction())) {
            if (this.wakeSources != null) {
                ensureArmed();
                return 1;
            }
            stopSelf();
            return 2;
        }
        String stringExtra = intent != null ? intent.getStringExtra(EXTRA_TEXT) : null;
        if (stringExtra == null) {
            stringExtra = getString(R.string.keepalive_interrupted);
        }
        ensureChannel();
        Notification notificationBuildNotification = buildNotification(stringExtra);
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(1001, notificationBuildNotification, 1073741824);
        } else {
            startForeground(1001, notificationBuildNotification);
        }
        ensureArmed();
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        stopChannel();
        super.onDestroy();
    }

    private void ensureArmed() {
        if (this.wakeSources == null) {
            WakeSources wakeSources = new WakeSources(this, new WakeSources.Listener() { // from class: ai.mirofish.mirasim.KeepAliveService$$ExternalSyntheticLambda0
                @Override // ai.mirofish.mirasim.WakeSources.Listener
                public final void onWake(String str) {
                    this.f$0.lambda$ensureArmed$0(str);
                }
            });
            this.wakeSources = wakeSources;
            wakeSources.arm();
        }
        if (this.attention == null) {
            this.attention = new MachineAttentionNotifier(this);
        }
        this.attention.ensureStarted(ChannelCredentialStore.loadAttentionTicket(this));
        if (this.channel == null) {
            this.channel = new NotifyChannel(this);
        }
        JSONObject jSONObjectLoad = ChannelCredentialStore.load(this);
        if (jSONObjectLoad != null && !jSONObjectLoad.optString("token", "").isEmpty()) {
            this.channel.ensureStarted(jSONObjectLoad);
        } else {
            this.channel.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$ensureArmed$0(String str) {
        NotifyChannel notifyChannel = this.channel;
        if (notifyChannel != null) {
            notifyChannel.nudge(str);
        }
        MachineAttentionNotifier machineAttentionNotifier = this.attention;
        if (machineAttentionNotifier != null) {
            machineAttentionNotifier.nudge();
        }
        ForegroundServicePlugin.dispatchSystemWake(str);
    }

    private void stopChannel() {
        NotifyChannel notifyChannel = this.channel;
        if (notifyChannel != null) {
            notifyChannel.close();
            this.channel = null;
        }
        MachineAttentionNotifier machineAttentionNotifier = this.attention;
        if (machineAttentionNotifier != null) {
            machineAttentionNotifier.close();
            this.attention = null;
        }
        WakeSources wakeSources = this.wakeSources;
        if (wakeSources != null) {
            wakeSources.disarm();
            this.wakeSources = null;
        }
    }

    private void ensureChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.keepalive_channel_name), 2);
        notificationChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private Notification buildNotification(String str) {
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        intent.setFlags(536870912);
        return new Notification.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_stat_mirasim).setContentTitle(getString(R.string.app_name)).setContentText(str).setContentIntent(PendingIntent.getActivity(this, 0, intent, 201326592)).setOngoing(true).build();
    }
}
package ai.mirofish.mirasim;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/* JADX INFO: loaded from: classes.dex */
final class WakeSources {
    static final String ACTION_WAKE_TICK = "ai.mirofish.mirasim.WAKE_TICK";
    static final long WAKE_TICK_INTERVAL_MS = 600000;
    private final Context context;
    private final Listener listener;
    private ConnectivityManager.NetworkCallback networkCallback;
    private BroadcastReceiver receiver;

    interface Listener {
        void onWake(String str);
    }

    WakeSources(Context context, Listener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    void arm() {
        if (this.receiver != null) {
            return;
        }
        this.receiver = new BroadcastReceiver() { // from class: ai.mirofish.mirasim.WakeSources.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (WakeSources.ACTION_WAKE_TICK.equals(action)) {
                    WakeSources.scheduleWakeTick(context);
                    WakeSources.this.listener.onWake(NotificationCompat.CATEGORY_ALARM);
                } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                    WakeSources.this.listener.onWake("screenOn");
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction(ACTION_WAKE_TICK);
        ContextCompat.registerReceiver(this.context, this.receiver, intentFilter, 4);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
        if (connectivityManager != null) {
            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: ai.mirofish.mirasim.WakeSources.2
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    WakeSources.this.listener.onWake("network");
                }
            };
            this.networkCallback = networkCallback;
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
        scheduleWakeTick(this.context);
    }

    void disarm() {
        BroadcastReceiver broadcastReceiver = this.receiver;
        if (broadcastReceiver != null) {
            try {
                this.context.unregisterReceiver(broadcastReceiver);
            } catch (IllegalArgumentException unused) {
            }
            this.receiver = null;
        }
        if (this.networkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(this.networkCallback);
            }
            this.networkCallback = null;
        }
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager != null) {
            alarmManager.cancel(wakeTickIntent(this.context));
        }
    }

    private static PendingIntent wakeTickIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(ACTION_WAKE_TICK).setPackage(context.getPackageName()), 201326592);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scheduleWakeTick(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager == null) {
            return;
        }
        alarmManager.setAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + WAKE_TICK_INTERVAL_MS, wakeTickIntent(context));
    }
}
package com.capacitorjs.plugins.localnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.getcapacitor.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class TimedNotificationPublisher extends BroadcastReceiver {
    public static String CRON_KEY = "NotificationPublisher.cron";
    public static String NOTIFICATION_KEY = "NotificationPublisher.notification";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Notification parcelableExtraLegacy;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 33) {
            parcelableExtraLegacy = (Notification) intent.getParcelableExtra(NOTIFICATION_KEY, Notification.class);
        } else {
            parcelableExtraLegacy = getParcelableExtraLegacy(intent, NOTIFICATION_KEY);
        }
        parcelableExtraLegacy.when = System.currentTimeMillis();
        int intExtra = intent.getIntExtra(LocalNotificationManager.NOTIFICATION_INTENT_KEY, Integer.MIN_VALUE);
        if (intExtra == Integer.MIN_VALUE) {
            Logger.error(Logger.tags("LN"), "No valid id supplied", null);
        }
        NotificationStorage notificationStorage = new NotificationStorage(context);
        LocalNotificationsPlugin.fireReceived(notificationStorage.getSavedNotificationAsJSObject(Integer.toString(intExtra)));
        notificationManager.notify(intExtra, parcelableExtraLegacy);
        if (rescheduleNotificationIfNeeded(context, intent, intExtra)) {
            return;
        }
        notificationStorage.deleteNotification(Integer.toString(intExtra));
    }

    private Notification getParcelableExtraLegacy(Intent intent, String str) {
        return (Notification) intent.getParcelableExtra(NOTIFICATION_KEY);
    }

    private boolean rescheduleNotificationIfNeeded(Context context, Intent intent, int i) {
        String stringExtra = intent.getStringExtra(CRON_KEY);
        if (stringExtra == null) {
            return false;
        }
        DateMatch dateMatchFromMatchString = DateMatch.fromMatchString(stringExtra);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        long jNextTrigger = dateMatchFromMatchString.nextTrigger(new Date());
        PendingIntent broadcast = PendingIntent.getBroadcast(context, i, (Intent) intent.clone(), Build.VERSION.SDK_INT >= 31 ? 301989888 : 268435456);
        if (Build.VERSION.SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
            Logger.warn("Capacitor/LocalNotification", "Exact alarms not allowed in user settings.  Notification scheduled with non-exact alarm.");
            alarmManager.set(1, jNextTrigger, broadcast);
        } else {
            alarmManager.setExact(1, jNextTrigger, broadcast);
        }
        Logger.debug(Logger.tags("LN"), "notification " + i + " will next fire at " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(jNextTrigger)));
        return true;
    }
}
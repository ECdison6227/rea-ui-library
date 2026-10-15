package ai.mirofish.mirasim;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.PointerIconCompat;

/* JADX INFO: loaded from: classes.dex */
final class AttentionPoster {
    static final String CHANNEL_ID = "mirasim.attention.v2";
    static final String CHAT_CHANNEL_ID = "mirasim.chat.v1";
    static final String EXTRA_CHAT_LINK = "mirasim.notify.chatLink";
    static final String EXTRA_SESSION_KEY = "mirasim.notify.sessionKey";
    private static final int KEEPALIVE_NOTIFICATION_ID = 1001;
    private static final String[] OBSOLETE_CHANNEL_IDS = {"mirasim.attention"};
    private static volatile boolean channelEnsured = false;
    private static volatile boolean chatChannelEnsured = false;

    private AttentionPoster() {
    }

    static int notificationId(String str) {
        int iCharAt = -2128831035;
        for (int i = 0; i < str.length(); i++) {
            iCharAt = (iCharAt ^ str.charAt(i)) * 16777619;
        }
        int i2 = ((iCharAt >>> 1) % 2147483644) + 3;
        return i2 == 1001 ? PointerIconCompat.TYPE_HAND : i2;
    }

    static int chatNotificationId(String str, long j) {
        return notificationId("chat:" + str + "#" + j);
    }

    static int machineChatNotificationId(String str, String str2, long j) {
        return notificationId("chat:" + str + ":" + str2 + "#" + j);
    }

    static void ensureChannel(Context context, String str, String str2) {
        NotificationManager notificationManager;
        if (channelEnsured || (notificationManager = (NotificationManager) context.getSystemService("notification")) == null) {
            return;
        }
        channelEnsured = true;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, str, 4);
        notificationChannel.setDescription(str2);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(0);
        notificationManager.createNotificationChannel(notificationChannel);
        for (String str3 : OBSOLETE_CHANNEL_IDS) {
            notificationManager.deleteNotificationChannel(str3);
        }
    }

    static void ensureChatChannel(Context context) {
        NotificationManager notificationManager;
        if (chatChannelEnsured || (notificationManager = (NotificationManager) context.getSystemService("notification")) == null) {
            return;
        }
        chatChannelEnsured = true;
        NotificationChannel notificationChannel = new NotificationChannel(CHAT_CHANNEL_ID, context.getString(R.string.chat_channel_name), 4);
        notificationChannel.setDescription(context.getString(R.string.chat_channel_desc));
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(0);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    static void post(Context context, int i, String str, String str2, String str3) {
        NotificationCompat.Builder smallIcon = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_stat_mirasim);
        if (str == null) {
            str = "";
        }
        NotificationCompat.Builder contentTitle = smallIcon.setContentTitle(str);
        if (str2 == null) {
            str2 = "";
        }
        try {
            NotificationManagerCompat.from(context).notify(i, contentTitle.setContentText(str2).setContentIntent(tapIntent(context, i, EXTRA_SESSION_KEY, str3)).setAutoCancel(true).setOnlyAlertOnce(false).setPriority(1).setCategory(NotificationCompat.CATEGORY_MESSAGE).setVisibility(0).build());
        } catch (SecurityException unused) {
        }
    }

    static PendingIntent tapIntent(Context context, int i, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) MainActivity.class);
        intent.setFlags(603979776);
        if (str2 != null) {
            intent.putExtra(str, str2);
        }
        return PendingIntent.getActivity(context, i, intent, 201326592);
    }
}
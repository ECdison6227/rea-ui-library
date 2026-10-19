package ai.mirofish.mirasim;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
final class ChatRows {
    private static final long ENQUEUE_GRACE_MS = 2000;
    private static final String GROUP_PREFIX = "mirasim.chat:";
    static final int LINES_SHOWN = 6;
    private static final long REDRAW_GAP_MS = 1000;
    private static final String TAG = "mirasim.chat-rows";
    private static ScheduledExecutorService drawer;
    private static final Map<Integer, Long> postedAt = new HashMap();
    private static final Set<Integer> rowsDue = new HashSet();
    private static final Map<String, Long> summaryPostedAt = new HashMap();
    private static final Set<String> summariesDue = new HashSet();

    private ChatRows() {
    }

    static List<ChatRowLedger.Post> post(Context context, List<ChatRowLedger.Post> list) {
        if (list.isEmpty()) {
            return list;
        }
        ChatRowLedger.Admission admissionAdmit = ChatRowLedger.admit(list, SystemClock.elapsedRealtime());
        draw(context, admissionAdmit.rows);
        return admissionAdmit.admitted;
    }

    static boolean post(Context context, ChatRowLedger.Post post) {
        return !post(context, (List<ChatRowLedger.Post>) Collections.singletonList(post)).isEmpty();
    }

    static void read(Context context, String str, String str2, long j, long j2) {
        draw(context, ChatRowLedger.read(str, str2, j, j2));
    }

    static void retract(Context context, String str, String str2, long j) {
        draw(context, ChatRowLedger.retract(str, str2, j));
    }

    static void roomGone(Context context, String str, String str2) {
        draw(context, ChatRowLedger.roomGone(str, str2));
    }

    static void withdraw(Context context, int i) {
        int[] iArrWithdraw = ChatRowLedger.withdraw(i);
        if (iArrWithdraw.length > 0) {
            draw(context, iArrWithdraw);
        } else {
            NotificationManagerCompat.from(context).cancel(i);
        }
    }

    private static synchronized void draw(Context context, int[] iArr) {
        if (iArr.length == 0) {
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        for (final int i : iArr) {
            if (rowsDue.add(Integer.valueOf(i))) {
                schedule(new Runnable() { // from class: ai.mirofish.mirasim.ChatRows$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatRows.drawRow(applicationContext, i);
                    }
                }, waitAfter(postedAt.get(Integer.valueOf(i)), jElapsedRealtime));
            }
        }
    }

    private static synchronized void requestSummary(final Context context, final String str) {
        if (summariesDue.add(str)) {
            schedule(new Runnable() { // from class: ai.mirofish.mirasim.ChatRows$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatRows.drawSummary(context, str);
                }
            }, waitAfter(summaryPostedAt.get(str), SystemClock.elapsedRealtime()));
        }
    }

    private static long waitAfter(Long l, long j) {
        if (l == null) {
            return 0L;
        }
        return Math.max(0L, (l.longValue() + 1000) - j);
    }

    private static void schedule(final Runnable runnable, long j) {
        if (drawer == null) {
            drawer = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: ai.mirofish.mirasim.ChatRows$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.ThreadFactory
                public final Thread newThread(Runnable runnable2) {
                    return ChatRows.lambda$schedule$2(runnable2);
                }
            });
        }
        drawer.schedule(new Runnable() { // from class: ai.mirofish.mirasim.ChatRows$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ChatRows.lambda$schedule$3(runnable);
            }
        }, j, TimeUnit.MILLISECONDS);
    }

    static /* synthetic */ Thread lambda$schedule$2(Runnable runnable) {
        Thread thread = new Thread(runnable, "mirasim-chat-rows");
        thread.setDaemon(true);
        return thread;
    }

    static /* synthetic */ void lambda$schedule$3(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            Log.w(TAG, "chat row draw failed", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void drawRow(Context context, int i) {
        Map<Integer, Long> map;
        Long l;
        Context context2;
        int i2;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        synchronized (ChatRows.class) {
            rowsDue.remove(Integer.valueOf(i));
            map = postedAt;
            l = map.get(Integer.valueOf(i));
        }
        ChatRowLedger.RowView rowViewView = ChatRowLedger.view(i);
        NotificationManagerCompat notificationManagerCompatFrom = NotificationManagerCompat.from(context);
        if (rowViewView == null || rowViewView.lines.isEmpty()) {
            context2 = context;
            notificationManagerCompatFrom.cancel(i);
            forget(i);
        } else {
            if (rowViewView.fresh) {
                context2 = context;
                i2 = i;
            } else {
                if (l != null) {
                    long jLongValue = l.longValue();
                    context2 = context;
                    i2 = i;
                    if (onShade(context2, i2, jLongValue, jElapsedRealtime)) {
                    }
                } else {
                    context2 = context;
                    i2 = i;
                }
                forget(i2);
            }
            AttentionPoster.ensureChatChannel(context2);
            synchronized (ChatRows.class) {
                map.put(Integer.valueOf(i2), Long.valueOf(jElapsedRealtime));
            }
            try {
                notificationManagerCompatFrom.notify(i2, buildRow(context2, rowViewView));
            } catch (SecurityException unused) {
            }
        }
        if (rowViewView != null) {
            requestSummary(context2, rowViewView.machine);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void drawSummary(Context context, String str) {
        HashMap map;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        synchronized (ChatRows.class) {
            summariesDue.remove(str);
            map = new HashMap(postedAt);
        }
        ArrayList arrayList = new ArrayList();
        Set<Integer> setActiveIds = null;
        boolean z = false;
        for (ChatRowLedger.RowView rowView : ChatRowLedger.showing(str)) {
            Long l = (Long) map.get(Integer.valueOf(rowView.id));
            if (l != null) {
                if (jElapsedRealtime - l.longValue() >= ENQUEUE_GRACE_MS) {
                    if (!z) {
                        setActiveIds = activeIds(context);
                        z = true;
                    }
                    if (setActiveIds == null || setActiveIds.contains(Integer.valueOf(rowView.id))) {
                    }
                }
                arrayList.add(rowView);
            }
        }
        int iSummaryId = summaryId(str);
        NotificationManagerCompat notificationManagerCompatFrom = NotificationManagerCompat.from(context);
        if (arrayList.isEmpty()) {
            notificationManagerCompatFrom.cancel(iSummaryId);
            synchronized (ChatRows.class) {
                summaryPostedAt.remove(str);
            }
        } else {
            synchronized (ChatRows.class) {
                summaryPostedAt.put(str, Long.valueOf(jElapsedRealtime));
            }
            try {
                notificationManagerCompatFrom.notify(iSummaryId, buildSummary(context, iSummaryId, arrayList));
            } catch (SecurityException unused) {
            }
        }
    }

    private static synchronized void forget(int i) {
        postedAt.remove(Integer.valueOf(i));
    }

    private static boolean onShade(Context context, int i, long j, long j2) {
        Set<Integer> setActiveIds;
        return j2 - j < ENQUEUE_GRACE_MS || (setActiveIds = activeIds(context)) == null || setActiveIds.contains(Integer.valueOf(i));
    }

    private static Set<Integer> activeIds(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager == null) {
            return null;
        }
        try {
            HashSet hashSet = new HashSet();
            for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
                hashSet.add(Integer.valueOf(statusBarNotification.getId()));
            }
            return hashSet;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    private static Notification buildRow(Context context, ChatRowLedger.RowView rowView) {
        ChatRowLedger.Line lineLatest = rowView.latest();
        String strTitle = title(context, rowView, true);
        NotificationCompat.MessagingStyle groupConversation = new NotificationCompat.MessagingStyle(new Person.Builder().setName(context.getString(R.string.chat_self)).build()).setConversationTitle(strTitle).setGroupConversation(rowView.group);
        for (ChatRowLedger.Line line : rowView.lines.subList(Math.max(0, rowView.lines.size() - 6), rowView.lines.size())) {
            groupConversation.addMessage(line.text, line.at, new Person.Builder().setName(sender(context, line)).build());
        }
        return builder(context, rowView.id, lineLatest.link, rowView.machine).setContentTitle(strTitle).setContentText(rowView.group ? context.getString(R.string.chat_line, sender(context, lineLatest), lineLatest.text) : lineLatest.text).setStyle(groupConversation).setNumber(rowView.lines.size()).setWhen(lineLatest.at).setShowWhen(true).setOnlyAlertOnce(!rowView.alert).setSilent(!rowView.alert).build();
    }

    private static Notification buildSummary(Context context, int i, List<ChatRowLedger.RowView> list) {
        ChatRowLedger.RowView rowView = list.get(0);
        String string = context.getString(R.string.chat_channel_name);
        for (ChatRowLedger.RowView rowView2 : list) {
            if (!rowView2.machineName.isEmpty()) {
                string = rowView2.machineName;
                break;
            }
        }
        NotificationCompat.InboxStyle bigContentTitle = new NotificationCompat.InboxStyle().setBigContentTitle(string);
        int size = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            ChatRowLedger.RowView rowView3 = list.get(i2);
            size += rowView3.lines.size();
            if (i2 < 6) {
                bigContentTitle.addLine(context.getString(R.string.chat_line, title(context, rowView3, false), rowView3.latest().text));
            }
        }
        return builder(context, i, rowView.latest().link, rowView.machine).setContentTitle(string).setContentText(context.getString(R.string.chat_line, title(context, rowView, false), rowView.latest().text)).setStyle(bigContentTitle).setNumber(size).setWhen(rowView.latest().at).setShowWhen(true).setGroupSummary(true).setOnlyAlertOnce(true).setSilent(true).build();
    }

    private static NotificationCompat.Builder builder(Context context, int i, String str, String str2) {
        return new NotificationCompat.Builder(context, "mirasim.chat.v1").setSmallIcon(R.drawable.ic_stat_mirasim).setContentIntent(AttentionPoster.tapIntent(context, i, "mirasim.notify.chatLink", str)).setAutoCancel(true).setPriority(1).setCategory(NotificationCompat.CATEGORY_MESSAGE).setVisibility(0).setGroup(GROUP_PREFIX + str2);
    }

    private static String title(Context context, ChatRowLedger.RowView rowView, boolean z) {
        String strSender = rowView.title.isEmpty() ? sender(context, rowView.latest()) : rowView.title;
        if (rowView.root > 0) {
            strSender = context.getString(R.string.chat_row_thread, strSender);
        }
        return (!z || rowView.machineName.isEmpty()) ? strSender : context.getString(R.string.chat_row_on_machine, rowView.machineName, strSender);
    }

    private static String sender(Context context, ChatRowLedger.Line line) {
        return line.sender.isEmpty() ? context.getString(R.string.chat_someone) : line.sender;
    }

    private static int summaryId(String str) {
        return AttentionPoster.notificationId("chat-group:" + str);
    }
}
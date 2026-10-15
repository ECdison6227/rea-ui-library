package ai.mirofish.mirasim;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class ChatNotifier {
    private static final int CATCH_UP_INBOX = 30;
    private static final Pattern CLOCK = Pattern.compile("^(\\d{1,2}):(\\d{2})$");
    private static final long CURSOR_FLUSH_MS = 5000;
    private static final String INBOX_REQ = "native-chat-inbox";
    private static final long JS_ALIVE_MS = 45000;
    private static final String OPEN_REQ = "native-chat-open";
    private static final int POSTED_MEMORY = 256;
    private static final long POST_DELAY_MS = 2000;
    private static final String PREFS = "mirasim.chat-notify";
    private final Context context;
    private ScheduledFuture<?> cursorFlush;
    private JSONObject dndSchedule;
    private JSONObject roomCursors;
    private final ScheduledExecutorService timer;
    private final Wire wire;
    private String nodeId = "";
    private boolean open = false;
    private String me = "";
    private String machine = "";
    private String dndUntil = "";
    private final Map<String, Room> rooms = new HashMap();
    private final Map<String, String> names = new HashMap();
    private final LinkedHashMap<String, Boolean> decided = new LinkedHashMap<String, Boolean>(64, 0.75f, false) { // from class: ai.mirofish.mirasim.ChatNotifier.1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<String, Boolean> entry) {
            return size() > 256;
        }
    };

    interface Wire {
        void send(JSONObject jSONObject);
    }

    private static final class Room {
        String kind;
        String notify;
        String title;

        private Room() {
            this.kind = "";
            this.title = "";
            this.notify = "";
        }
    }

    ChatNotifier(Context context, ScheduledExecutorService scheduledExecutorService, Wire wire) {
        this.context = context.getApplicationContext();
        this.timer = scheduledExecutorService;
        this.wire = wire;
    }

    synchronized void onWelcome(String str) {
        if (str == null) {
            str = "";
        }
        try {
            this.nodeId = str;
            this.open = false;
            this.me = "";
            this.machine = "";
            this.dndUntil = "";
            this.dndSchedule = null;
            this.rooms.clear();
            this.names.clear();
            forgetOtherMachines();
            this.roomCursors = loadRoomCursors();
            this.wire.send(frame("chatOpen", "reqId", OPEN_REQ));
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void onLinkLost() {
        this.open = false;
        flushCursors();
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    synchronized void onHostFrame(JSONObject jSONObject) {
        String str = ChatText.str(jSONObject, ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY);
        if ("chatOpened".equals(str)) {
            if (OPEN_REQ.equals(ChatText.str(jSONObject, "reqId"))) {
                onOpened(jSONObject);
            }
            return;
        }
        if (this.open) {
            switch (str.hashCode()) {
                case -1759522809:
                    if (str.equals("chatPeople")) {
                        adoptPeople(jSONObject.optJSONArray("people"));
                        return;
                    }
                    break;
                case -1394978235:
                    if (str.equals("chatReadMoved")) {
                        onReadMoved(jSONObject);
                        return;
                    }
                    break;
                case -940010148:
                    if (str.equals("chatPrefsChanged")) {
                        adoptPrefs(jSONObject.optJSONObject("prefs"));
                        return;
                    }
                    break;
                case 787591058:
                    if (str.equals("chatRoomGone")) {
                        String str2 = ChatText.str(jSONObject, "room");
                        this.rooms.remove(str2);
                        ChatRows.roomGone(this.context, this.machine, str2);
                        return;
                    }
                    break;
                case 1025759410:
                    if (str.equals("chatInboxNew")) {
                        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("item");
                        if (jSONObjectOptJSONObject == null) {
                            return;
                        }
                        considerInbox(jSONObjectOptJSONObject);
                        saveInboxCursor(jSONObjectOptJSONObject.optLong("n", 0L));
                        return;
                    }
                    break;
                case 1362646882:
                    if (str.equals("chatRoomUpsert")) {
                        adoptRoom(jSONObject.optJSONObject("room"));
                        return;
                    }
                    break;
                case 1596147234:
                    if (str.equals("chatEvent")) {
                        onEvent(ChatText.str(jSONObject, "room"), jSONObject.optJSONObject(NotificationCompat.CATEGORY_EVENT));
                        return;
                    }
                    break;
                case 1733825981:
                    if (str.equals("chatInboxPage")) {
                        if (INBOX_REQ.equals(ChatText.str(jSONObject, "reqId"))) {
                            onInboxPage(jSONObject.optJSONArray("items"));
                        }
                        return;
                    }
                    break;
            }
        }
    }

    private void onOpened(JSONObject jSONObject) {
        String str;
        long j;
        if (jSONObject.optBoolean("ok", false)) {
            String str2 = ChatText.str(jSONObject.optJSONObject("me"), "uid");
            this.me = str2;
            if (str2.isEmpty()) {
                return;
            }
            this.machine = ChatText.str(jSONObject, "machine");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("prefs");
            this.dndUntil = ChatText.str(jSONObjectOptJSONObject, "dndUntil");
            this.dndSchedule = jSONObjectOptJSONObject == null ? null : jSONObjectOptJSONObject.optJSONObject("dndSchedule");
            adoptPeople(jSONObject.optJSONArray("people"));
            this.open = true;
            boolean z = this.roomCursors == null;
            if (z) {
                this.roomCursors = new JSONObject();
            }
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("rooms");
            for (int i = 0; jSONArrayOptJSONArray != null && i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(i);
                String strAdoptRoom = adoptRoom(jSONObjectOptJSONObject2);
                JSONObject jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2 == null ? null : jSONObjectOptJSONObject2.optJSONObject("last");
                if (strAdoptRoom != null && jSONObjectOptJSONObject3 != null) {
                    long jOptLong = jSONObjectOptJSONObject3.optLong("seq", 0L);
                    if (z || jOptLong <= this.roomCursors.optLong(strAdoptRoom, 0L) || jSONObjectOptJSONObject2.optInt("unread", 0) <= 0) {
                        str = strAdoptRoom;
                        j = jOptLong;
                    } else {
                        str = strAdoptRoom;
                        j = jOptLong;
                        considerMessage(str, j, ChatText.str(jSONObjectOptJSONObject3, "by"), ChatText.str(jSONObjectOptJSONObject3, HintConstants.AUTOFILL_HINT_NAME), ChatText.previewText(this.context, jSONObjectOptJSONObject3.optJSONObject("preview")), ChatText.str(jSONObjectOptJSONObject3, "ts"));
                    }
                    bumpRoomCursor(str, j);
                }
            }
            flushCursors();
            this.wire.send(frame("chatInbox", "reqId", INBOX_REQ, "limit", 30));
        }
    }

    private void onInboxPage(JSONArray jSONArray) {
        long j = prefs().getLong(inboxKey(), -1L);
        long jMax = Math.max(j, 0L);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; jSONArray != null && i < jSONArray.length(); i++) {
            JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
            if (jSONObjectOptJSONObject != null) {
                long jOptLong = jSONObjectOptJSONObject.optLong("n", 0L);
                jMax = Math.max(jMax, jOptLong);
                if (j >= 0 && jOptLong > j) {
                    String str = ChatText.str(jSONObjectOptJSONObject, "room");
                    JSONObject jSONObject = (JSONObject) linkedHashMap.get(str);
                    if (jSONObject == null || jSONObject.optLong("n", 0L) < jOptLong) {
                        linkedHashMap.put(str, jSONObjectOptJSONObject);
                    }
                }
            }
        }
        Iterator it = linkedHashMap.values().iterator();
        while (it.hasNext()) {
            considerInbox((JSONObject) it.next());
        }
        saveInboxCursor(jMax);
    }

    private void onEvent(String str, JSONObject jSONObject) {
        if (jSONObject != null && !str.isEmpty()) {
            long jOptLong = jSONObject.optLong("seq", 0L);
            bumpRoomCursor(str, jOptLong);
            String str2 = ChatText.str(jSONObject, "kind");
            if ("retract".equals(str2) || "purge".equals(str2)) {
                ChatRows.retract(this.context, this.machine, str, jSONObject.optLong("target", -1L));
                return;
            }
            if (("message".equals(str2) || "system".equals(str2)) && ((jSONObject.optLong("thread", 0L) <= 0 || jSONObject.optBoolean("alsoInRoom", false)) && !namesMe(jSONObject))) {
                considerMessage(str, jOptLong, ChatText.str(jSONObject, "by"), ChatText.str(jSONObject, HintConstants.AUTOFILL_HINT_NAME), "system".equals(str2) ? ChatText.oneLine(ChatText.str(jSONObject, "text")) : ChatText.previewOfLine(this.context, jSONObject), ChatText.str(jSONObject, "ts"));
            }
        }
    }

    private boolean namesMe(JSONObject jSONObject) {
        if (jSONObject.optBoolean("mentionAll", false) || jSONObject.optBoolean("mentionHere", false)) {
            return true;
        }
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("mentions");
        for (int i = 0; jSONArrayOptJSONArray != null && i < jSONArrayOptJSONArray.length(); i++) {
            if (this.me.equals(jSONArrayOptJSONArray.optString(i))) {
                return true;
            }
        }
        return false;
    }

    private void onReadMoved(JSONObject jSONObject) {
        if (this.me.equals(ChatText.str(jSONObject, "uid"))) {
            ChatRows.read(this.context, this.machine, ChatText.str(jSONObject, "room"), jSONObject.optLong("thread", 0L), jSONObject.optLong("read", 0L));
        }
    }

    private void considerInbox(JSONObject jSONObject) {
        String str = ChatText.str(jSONObject, "kind");
        String str2 = ChatText.str(jSONObject, "room");
        String str3 = ChatText.str(jSONObject, "by");
        if (str2.isEmpty() || "reaction".equals(str) || str3.equals(this.me) || !jSONObject.optBoolean("unread", true) || "mute".equals(levelOf(str2))) {
            return;
        }
        Context context = this.context;
        decide(str2, jSONObject.optLong("seq", 0L), jSONObject.optLong("thread", 0L), nameOf(str3, ChatText.str(jSONObject, "byName")), ChatText.withKind(context, str, ChatText.previewText(context, jSONObject.optJSONObject("preview"))), ChatText.str(jSONObject, "roomTitle"), ChatText.str(jSONObject, "ts"));
    }

    private void considerMessage(String str, long j, String str2, String str3, String str4, String str5) {
        if (str2.isEmpty() || str2.equals(this.me) || !"all".equals(levelOf(str))) {
            return;
        }
        decide(str, j, 0L, nameOf(str2, str3), str4, "", str5);
    }

    private void decide(String str, long j, long j2, String str2, String str3, String str4, String str5) {
        String str6 = str + "#" + j;
        if (this.decided.containsKey(str6)) {
            return;
        }
        this.decided.put(str6, Boolean.TRUE);
        if (inDoNotDisturb()) {
            return;
        }
        long jEpochMs = epochMs(str5);
        boolean zIsDm = isDm(str);
        ChatRowLedger.Post postThread = new ChatRowLedger.Post(AttentionPoster.chatNotificationId(str, j), ChatLinks.deepLink(str, j, this.machine), str2, str3).thread(j2);
        if (jEpochMs == Long.MIN_VALUE) {
            jEpochMs = System.currentTimeMillis();
        }
        ChatRowLedger.Post postAt = postThread.at(jEpochMs);
        if (!zIsDm) {
            str2 = roomLabel(str, str4);
        }
        final ChatRowLedger.Post postGroup = postAt.title(str2, true).group(Boolean.valueOf(true ^ zIsDm));
        later(new Runnable() { // from class: ai.mirofish.mirasim.ChatNotifier$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$decide$0(postGroup);
            }
        }, POST_DELAY_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: post, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$decide$0(ChatRowLedger.Post post) {
        if (ForegroundServicePlugin.jsAliveAgoMs() < JS_ALIVE_MS) {
            return;
        }
        ChatRows.post(this.context, post);
    }

    private boolean inDoNotDisturb() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.dndUntil.isEmpty() || epochMs(this.dndUntil) <= jCurrentTimeMillis) {
            return quietHoursOn(this.dndSchedule, jCurrentTimeMillis);
        }
        return true;
    }

    private static boolean quietHoursOn(JSONObject jSONObject, long j) {
        ZoneId zoneIdZoneOf;
        if (jSONObject == null) {
            return false;
        }
        try {
            int iClockMinutes = clockMinutes(ChatText.str(jSONObject, "from"));
            int iClockMinutes2 = clockMinutes(ChatText.str(jSONObject, "to"));
            if (iClockMinutes < 0 || iClockMinutes2 < 0 || iClockMinutes == iClockMinutes2 || (zoneIdZoneOf = zoneOf(ChatText.str(jSONObject, "tz"))) == null) {
                return false;
            }
            String str = ChatText.str(jSONObject, "skipUntil");
            if (!str.isEmpty() && epochMs(str) > j) {
                return false;
            }
            ZonedDateTime zonedDateTimeAtZone = Instant.ofEpochMilli(j).atZone(zoneIdZoneOf);
            int hour = (zonedDateTimeAtZone.getHour() * 60) + zonedDateTimeAtZone.getMinute();
            if (iClockMinutes < iClockMinutes2) {
                return hour >= iClockMinutes && hour < iClockMinutes2;
            }
            return hour >= iClockMinutes || hour < iClockMinutes2;
        } catch (RuntimeException unused) {
        }
        return false;
    }

    private static int clockMinutes(String str) {
        Matcher matcher = CLOCK.matcher(jsTrim(str));
        if (!matcher.matches()) {
            return -1;
        }
        int i = Integer.parseInt(matcher.group(1));
        int i2 = Integer.parseInt(matcher.group(2));
        if (i > 23 || i2 > 59) {
            return -1;
        }
        return (i * 60) + i2;
    }

    private static ZoneId zoneOf(String str) {
        if (!str.isEmpty() && str.length() <= 64) {
            try {
                return ZoneId.of(str);
            } catch (RuntimeException unused) {
            }
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.time.ZonedDateTime] */
    private static long epochMs(String str) {
        try {
            try {
                try {
                    return OffsetDateTime.parse(str).toInstant().toEpochMilli();
                } catch (RuntimeException unused) {
                    return Long.MIN_VALUE;
                }
            } catch (DateTimeParseException unused2) {
                return LocalDate.parse(str).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            } catch (RuntimeException unused3) {
                return Long.MIN_VALUE;
            }
        } catch (DateTimeParseException unused4) {
            return LocalDateTime.parse(str).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (RuntimeException unused5) {
            return Long.MIN_VALUE;
        }
    }

    private static String jsTrim(String str) {
        int length = str.length();
        int i = 0;
        while (i < length && jsSpace(str.charAt(i))) {
            i++;
        }
        while (length > i && jsSpace(str.charAt(length - 1))) {
            length--;
        }
        return str.substring(i, length);
    }

    private static boolean jsSpace(char c) {
        return (c >= '\t' && c <= '\r') || c == 65279 || Character.isSpaceChar(c);
    }

    private String levelOf(String str) {
        Room room = this.rooms.get(str);
        if (room == null || room.notify.isEmpty()) {
            return isDm(str) ? "all" : "mentions";
        }
        return room.notify;
    }

    private boolean isDm(String str) {
        Room room = this.rooms.get(str);
        return (room == null || room.kind.isEmpty()) ? str.startsWith("dm-") : "dm".equals(room.kind);
    }

    private String nameOf(String str, String str2) {
        if (!str2.isEmpty()) {
            return str2;
        }
        String str3 = this.names.get(str);
        return str3 != null ? str3 : this.context.getString(R.string.chat_someone);
    }

    private String roomLabel(String str, String str2) {
        if (!str2.isEmpty()) {
            return str2;
        }
        Room room = this.rooms.get(str);
        return (room == null || room.title.isEmpty()) ? ChatText.roomLabel(this.context, str) : room.title;
    }

    private String adoptRoom(JSONObject jSONObject) {
        String str = ChatText.str(jSONObject, "id");
        if (str.isEmpty()) {
            return null;
        }
        Room room = this.rooms.get(str);
        if (room == null) {
            Map<String, Room> map = this.rooms;
            Room room2 = new Room();
            map.put(str, room2);
            room = room2;
        }
        if (!jSONObject.isNull("kind")) {
            room.kind = ChatText.str(jSONObject, "kind");
        }
        if (!jSONObject.isNull("title")) {
            room.title = ChatText.str(jSONObject, "title");
        }
        if (!jSONObject.isNull("notify")) {
            room.notify = ChatText.str(jSONObject, "notify");
        }
        return str;
    }

    private void adoptPrefs(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        this.dndUntil = ChatText.str(jSONObject, "dndUntil");
        this.dndSchedule = jSONObject.optJSONObject("dndSchedule");
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("notify");
        for (Map.Entry<String, Room> entry : this.rooms.entrySet()) {
            entry.getValue().notify = ChatText.str(jSONObjectOptJSONObject, entry.getKey());
        }
        Iterator<String> itKeys = jSONObjectOptJSONObject == null ? null : jSONObjectOptJSONObject.keys();
        while (itKeys != null && itKeys.hasNext()) {
            String next = itKeys.next();
            if (!this.rooms.containsKey(next)) {
                Room room = new Room();
                room.notify = ChatText.str(jSONObjectOptJSONObject, next);
                this.rooms.put(next, room);
            }
        }
    }

    private void adoptPeople(JSONArray jSONArray) {
        for (int i = 0; jSONArray != null && i < jSONArray.length(); i++) {
            JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
            String str = ChatText.str(jSONObjectOptJSONObject, "uid");
            String str2 = ChatText.str(jSONObjectOptJSONObject, HintConstants.AUTOFILL_HINT_NAME);
            if (str2.isEmpty()) {
                str2 = ChatText.str(jSONObjectOptJSONObject, "email");
            }
            if (!str.isEmpty() && !str2.isEmpty()) {
                this.names.put(str, str2);
            }
        }
    }

    private SharedPreferences prefs() {
        return this.context.getSharedPreferences(PREFS, 0);
    }

    private String inboxKey() {
        return "inbox." + this.nodeId;
    }

    private String roomsKey() {
        return "rooms." + this.nodeId;
    }

    private void saveInboxCursor(long j) {
        if (j <= prefs().getLong(inboxKey(), -1L)) {
            return;
        }
        prefs().edit().putLong(inboxKey(), j).apply();
    }

    private JSONObject loadRoomCursors() {
        String string = prefs().getString(roomsKey(), null);
        if (string == null) {
            return null;
        }
        try {
            return new JSONObject(string);
        } catch (JSONException unused) {
            return null;
        }
    }

    private void bumpRoomCursor(String str, long j) {
        JSONObject jSONObject = this.roomCursors;
        if (jSONObject == null || j <= jSONObject.optLong(str, 0L)) {
            return;
        }
        try {
            this.roomCursors.put(str, j);
            if (this.cursorFlush == null) {
                this.cursorFlush = later(new Runnable() { // from class: ai.mirofish.mirasim.ChatNotifier$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.flushCursors();
                    }
                }, 5000L);
            }
        } catch (JSONException unused) {
        }
    }

    private ScheduledFuture<?> later(Runnable runnable, long j) {
        try {
            return this.timer.schedule(runnable, j, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void flushCursors() {
        ScheduledFuture<?> scheduledFuture = this.cursorFlush;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            this.cursorFlush = null;
        }
        if (this.roomCursors != null && !this.nodeId.isEmpty()) {
            prefs().edit().putString(roomsKey(), this.roomCursors.toString()).apply();
        }
    }

    private void forgetOtherMachines() {
        SharedPreferences sharedPreferencesPrefs = prefs();
        SharedPreferences.Editor editorEdit = sharedPreferencesPrefs.edit();
        boolean z = false;
        for (String str : sharedPreferencesPrefs.getAll().keySet()) {
            if (!str.endsWith("." + this.nodeId)) {
                editorEdit.remove(str);
                z = true;
            }
        }
        if (z) {
            editorEdit.apply();
        }
    }

    private static JSONObject frame(String str, Object... objArr) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, str);
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (i2 >= objArr.length) {
                    break;
                }
                jSONObject.put((String) objArr[i], objArr[i2]);
                i += 2;
            }
        } catch (JSONException unused) {
        }
        return jSONObject;
    }
}
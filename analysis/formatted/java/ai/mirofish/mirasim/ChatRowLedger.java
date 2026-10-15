package ai.mirofish.mirasim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
final class ChatRowLedger {
    private static final int ALERTED = 2;
    private static final int LINES_KEPT = 100;
    private static final int MESSAGES_KEPT = 1024;
    private static final long NEVER = Long.MIN_VALUE;
    static final long QUIET_MS = 60000;
    private static final int ROWS_KEPT = 256;
    private static final int SEEN = 1;
    private static final int SENDER_KEPT = 80;
    private static final int TEXT_KEPT = 400;
    static final long THREAD_UNKNOWN = -1;
    private static final LinkedHashMap<String, Integer> messages = new LinkedHashMap<String, Integer>(64, 0.75f, false) { // from class: ai.mirofish.mirasim.ChatRowLedger.1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<String, Integer> entry) {
            return size() > 1024;
        }
    };
    private static final LinkedHashMap<String, Row> rows = new LinkedHashMap<>(32, 0.75f, false);
    private static final Map<Integer, Row> byId = new HashMap();
    private static long unnamed = 0;

    private ChatRowLedger() {
    }

    static final class Post {
        final String link;
        final int messageId;
        final String sender;
        final String text;
        long thread = -1;
        long at = System.currentTimeMillis();
        String title = "";
        boolean titleGiven = false;
        Boolean group = null;
        String machineName = "";

        Post(int i, String str, String str2, String str3) {
            this.messageId = i;
            this.link = str == null ? "" : str;
            this.sender = ChatRowLedger.clip(str2, ChatRowLedger.SENDER_KEPT);
            this.text = ChatRowLedger.clip(str3, ChatRowLedger.TEXT_KEPT);
        }

        Post thread(long j) {
            this.thread = j;
            return this;
        }

        Post at(long j) {
            this.at = j;
            return this;
        }

        Post title(String str, boolean z) {
            this.title = ChatRowLedger.clip(str, ChatRowLedger.SENDER_KEPT);
            this.titleGiven = z;
            return this;
        }

        Post group(Boolean bool) {
            this.group = bool;
            return this;
        }

        Post machineName(String str) {
            this.machineName = str == null ? "" : ChatRowLedger.clip(str, ChatRowLedger.SENDER_KEPT);
            return this;
        }
    }

    static final class Line {
        final long at;
        final String link;
        final int messageId;
        final String sender;
        final long seq;
        final String text;

        Line(long j, Post post) {
            this.seq = j;
            this.messageId = post.messageId;
            this.sender = post.sender;
            this.text = post.text;
            this.at = post.at;
            this.link = post.link;
        }
    }

    static final class RowView {
        final boolean alert;
        final boolean fresh;
        final boolean group;
        final int id;
        final List<Line> lines;
        final String machine;
        final String machineName;
        final long root;
        final String title;

        RowView(Row row, boolean z, boolean z2) {
            this.id = row.id;
            this.machine = row.machine;
            this.root = row.root;
            this.title = row.title;
            this.machineName = row.machineName;
            this.group = row.group;
            this.lines = Collections.unmodifiableList(new ArrayList(row.lines.values()));
            this.fresh = z;
            this.alert = z2;
        }

        Line latest() {
            List<Line> list = this.lines;
            return list.get(list.size() - 1);
        }
    }

    static final class Admission {
        final List<Post> admitted;
        final int[] rows;

        Admission(List<Post> list, int[] iArr) {
            this.admitted = list;
            this.rows = iArr;
        }
    }

    private static final class Row {
        final int id;
        final String key;
        final String machine;
        final String room;
        final long root;
        String title = "";
        boolean titleGiven = false;
        boolean group = true;
        String machineName = "";
        final TreeMap<Long, Line> lines = new TreeMap<>();
        long lastMessageAt = Long.MIN_VALUE;
        boolean fresh = false;
        boolean alert = false;

        Row(String str, String str2, String str3, long j) {
            this.key = str;
            this.id = AttentionPoster.notificationId(str);
            this.machine = str2;
            this.room = str3;
            this.root = j;
        }

        boolean in(String str, String str2) {
            return this.machine.equals(str) && this.room.equals(str2);
        }
    }

    static String conversationKey(String str, String str2, long j) {
        return "chat:" + str + ':' + str2 + (j > 0 ? "#t" + j : "");
    }

    private static String key(String str, String str2, long j) {
        return str + '\n' + str2 + '#' + j;
    }

    private static int flags(String str, String str2, long j) {
        Integer num = messages.get(key(str, str2, j));
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private static void mark(String str, String str2, long j, int i) {
        messages.put(key(str, str2, j), Integer.valueOf(flags(str, str2, j) | i));
    }

    static synchronized Admission admit(List<Post> list, long j) {
        ArrayList arrayList;
        ArrayList arrayList2;
        long j2;
        Row row;
        long j3;
        arrayList = new ArrayList();
        arrayList2 = new ArrayList();
        for (Post post : list) {
            ChatLinks.Link link = ChatLinks.parse(post.link);
            if (link == null) {
                long j4 = unnamed + 1;
                unnamed = j4;
                j2 = -j4;
                row = row("chat::#m" + post.messageId, "", "", 0L, arrayList2);
            } else {
                if (link.seq > 0) {
                    if ((flags(link.machine, link.room, link.seq) & 2) == 0) {
                        mark(link.machine, link.room, link.seq, 2);
                    }
                }
                if (link.seq > 0) {
                    j3 = link.seq;
                } else {
                    long j5 = unnamed + 1;
                    unnamed = j5;
                    j3 = -j5;
                }
                long j6 = j3;
                long j7 = post.thread > 0 ? post.thread : 0L;
                row = row(conversationKey(link.machine, link.room, j7), link.machine, link.room, j7, arrayList2);
                j2 = j6;
            }
            if (!post.title.isEmpty() && (post.titleGiven || !row.titleGiven)) {
                row.title = post.title;
                row.titleGiven = post.titleGiven;
            }
            if (!post.machineName.isEmpty()) {
                row.machineName = post.machineName;
            }
            row.group = post.group != null ? post.group.booleanValue() : (row.room.startsWith("dm-") || row.room.isEmpty()) ? false : true;
            row.lines.put(Long.valueOf(j2), new Line(j2, post));
            while (row.lines.size() > 100) {
                row.lines.pollFirstEntry();
            }
            if (row.lastMessageAt == Long.MIN_VALUE || j - row.lastMessageAt >= QUIET_MS) {
                row.alert = true;
            }
            row.lastMessageAt = j;
            row.fresh = true;
            arrayList.add(post);
            if (!arrayList2.contains(Integer.valueOf(row.id))) {
                arrayList2.add(Integer.valueOf(row.id));
            }
        }
        return new Admission(arrayList, ints(arrayList2));
    }

    private static Row row(String str, String str2, String str3, long j, List<Integer> list) {
        String str4;
        LinkedHashMap<String, Row> linkedHashMap = rows;
        Row rowRemove = linkedHashMap.remove(str);
        if (rowRemove == null) {
            str4 = str;
            Row row = new Row(str4, str2, str3, j);
            evict(list);
            rowRemove = row;
        } else {
            str4 = str;
        }
        linkedHashMap.put(str4, rowRemove);
        byId.put(Integer.valueOf(rowRemove.id), rowRemove);
        return rowRemove;
    }

    private static void evict(List<Integer> list) {
        Row next;
        LinkedHashMap<String, Row> linkedHashMap = rows;
        if (linkedHashMap.size() < 256) {
            return;
        }
        Iterator<Row> it = linkedHashMap.values().iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!next.lines.isEmpty());
        if (next == null) {
            next = rows.values().iterator().next();
        }
        rows.remove(next.key);
        byId.remove(Integer.valueOf(next.id));
        if (next.lines.isEmpty() || list.contains(Integer.valueOf(next.id))) {
            return;
        }
        list.add(Integer.valueOf(next.id));
    }

    static synchronized void seenByApp(String str, String str2, long j) {
        mark(str, str2, j, 1);
    }

    static synchronized boolean wasSeenByApp(String str, String str2, long j) {
        return (flags(str, str2, j) & 1) != 0;
    }

    static synchronized boolean handled(String str, String str2, long j) {
        return flags(str, str2, j) != 0;
    }

    static synchronized int[] read(String str, String str2, long j, long j2) {
        LinkedHashMap<String, Row> linkedHashMap = rows;
        if (j <= 0) {
            j = 0;
        }
        Row row = linkedHashMap.get(conversationKey(str, str2, j));
        if (row == null) {
            return new int[0];
        }
        NavigableMap<Long, Line> navigableMapHeadMap = row.lines.headMap(Long.valueOf(j2), true);
        if (navigableMapHeadMap.isEmpty()) {
            return new int[0];
        }
        navigableMapHeadMap.clear();
        return new int[]{row.id};
    }

    static synchronized int[] retract(String str, String str2, long j) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Row row : rows.values()) {
            if (row.in(str, str2) && row.lines.remove(Long.valueOf(j)) != null) {
                arrayList.add(Integer.valueOf(row.id));
            }
        }
        return ints(arrayList);
    }

    static synchronized int[] roomGone(String str, String str2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Row row : rows.values()) {
            if (row.in(str, str2) && !row.lines.isEmpty()) {
                row.lines.clear();
                arrayList.add(Integer.valueOf(row.id));
            }
        }
        return ints(arrayList);
    }

    static synchronized int[] withdraw(int i) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Row row : rows.values()) {
            if (!row.lines.isEmpty()) {
                if (row.id == i) {
                    row.lines.clear();
                    arrayList.add(Integer.valueOf(row.id));
                } else {
                    Iterator<Line> it = row.lines.values().iterator();
                    boolean z = false;
                    while (it.hasNext()) {
                        if (it.next().messageId == i) {
                            it.remove();
                            z = true;
                        }
                    }
                    if (z) {
                        arrayList.add(Integer.valueOf(row.id));
                    }
                }
            }
        }
        return ints(arrayList);
    }

    static synchronized RowView view(int i) {
        Row row = byId.get(Integer.valueOf(i));
        if (row == null) {
            return null;
        }
        RowView rowView = new RowView(row, row.fresh, row.alert);
        row.fresh = false;
        row.alert = false;
        return rowView;
    }

    static synchronized List<RowView> showing(String str) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Row row : rows.values()) {
            if (row.machine.equals(str) && !row.lines.isEmpty()) {
                arrayList.add(new RowView(row, false, false));
            }
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String clip(String str, int i) {
        if (str == null) {
            return "";
        }
        return str.length() > i ? str.substring(0, i) : str;
    }

    private static int[] ints(List<Integer> list) {
        int size = list.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = list.get(i).intValue();
        }
        return iArr;
    }
}
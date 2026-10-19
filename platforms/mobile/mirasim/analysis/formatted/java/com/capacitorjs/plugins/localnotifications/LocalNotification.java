package com.capacitorjs.plugins.localnotifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.collection.SieveCacheKt;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.PluginCall;
import com.getcapacitor.plugin.util.AssetUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlinx.coroutines.DebugKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class LocalNotification {
    private String actionTypeId;
    private List<LocalNotificationAttachment> attachments;
    private boolean autoCancel;
    private String body;
    private String channelId;
    private JSObject extra;
    private String group;
    private boolean groupSummary;
    private String iconColor;
    private Integer id;
    private List<String> inboxList;
    private String largeBody;
    private String largeIcon;
    private boolean ongoing;
    private LocalNotificationSchedule schedule;
    private String smallIcon;
    private String sound;
    private String source;
    private String summaryText;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public void setLargeBody(String str) {
        this.largeBody = str;
    }

    public String getLargeBody() {
        return this.largeBody;
    }

    public void setSummaryText(String str) {
        this.summaryText = str;
    }

    public String getSummaryText() {
        return this.summaryText;
    }

    public LocalNotificationSchedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(LocalNotificationSchedule localNotificationSchedule) {
        this.schedule = localNotificationSchedule;
    }

    public String getSound(Context context, int i) {
        String resourceBaseName = AssetUtil.getResourceBaseName(this.sound);
        int resourceID = resourceBaseName != null ? AssetUtil.getResourceID(context, resourceBaseName, "raw") : 0;
        if (resourceID != 0) {
            i = resourceID;
        }
        if (i != 0) {
            return "android.resource://" + context.getPackageName() + "/" + i;
        }
        return null;
    }

    public void setSound(String str) {
        this.sound = str;
    }

    public void setSmallIcon(String str) {
        this.smallIcon = AssetUtil.getResourceBaseName(str);
    }

    public void setLargeIcon(String str) {
        this.largeIcon = AssetUtil.getResourceBaseName(str);
    }

    public void setInboxList(List<String> list) {
        this.inboxList = list;
    }

    public List<String> getInboxList() {
        return this.inboxList;
    }

    public String getIconColor(String str) {
        String str2 = this.iconColor;
        return str2 != null ? str2 : str;
    }

    public void setIconColor(String str) {
        this.iconColor = str;
    }

    public List<LocalNotificationAttachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(List<LocalNotificationAttachment> list) {
        this.attachments = list;
    }

    public String getActionTypeId() {
        return this.actionTypeId;
    }

    public void setActionTypeId(String str) {
        this.actionTypeId = str;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public JSObject getExtra() {
        return this.extra;
    }

    public void setExtra(JSObject jSObject) {
        this.extra = jSObject;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public boolean isGroupSummary() {
        return this.groupSummary;
    }

    public void setGroupSummary(boolean z) {
        this.groupSummary = z;
    }

    public boolean isOngoing() {
        return this.ongoing;
    }

    public void setOngoing(boolean z) {
        this.ongoing = z;
    }

    public boolean isAutoCancel() {
        return this.autoCancel;
    }

    public void setAutoCancel(boolean z) {
        this.autoCancel = z;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String str) {
        this.channelId = str;
    }

    public static List<LocalNotification> buildNotificationList(PluginCall pluginCall) {
        JSArray array = pluginCall.getArray("notifications");
        if (array == null) {
            pluginCall.reject("Must provide notifications array as notifications option");
            return null;
        }
        ArrayList arrayList = new ArrayList(array.length());
        try {
            for (JSONObject jSONObject : array.toList()) {
                try {
                    long j = jSONObject.getLong("id");
                    if (j <= SieveCacheKt.NodeLinkMask && j >= SieveCacheKt.NodeMetaAndPreviousMask) {
                        try {
                            arrayList.add(buildNotificationFromJSObject(JSObject.fromJSONObject(jSONObject)));
                        } catch (ParseException e) {
                            pluginCall.reject("Invalid date format sent to Notification plugin", e);
                            return null;
                        }
                    }
                    pluginCall.reject("The identifier should be a Java int");
                    return null;
                } catch (JSONException e2) {
                    pluginCall.reject("Invalid JSON object sent to NotificationPlugin", e2);
                    return null;
                }
            }
            return arrayList;
        } catch (JSONException unused) {
            pluginCall.reject("Provided notification format is invalid");
            return null;
        }
    }

    public static LocalNotification buildNotificationFromJSObject(JSObject jSObject) throws ParseException {
        LocalNotification localNotification = new LocalNotification();
        localNotification.setSource(jSObject.toString());
        localNotification.setId(jSObject.getInteger("id"));
        localNotification.setBody(jSObject.getString("body"));
        localNotification.setLargeBody(jSObject.getString("largeBody"));
        localNotification.setSummaryText(jSObject.getString("summaryText"));
        localNotification.setActionTypeId(jSObject.getString("actionTypeId"));
        localNotification.setGroup(jSObject.getString("group"));
        localNotification.setSound(jSObject.getString("sound"));
        localNotification.setTitle(jSObject.getString("title"));
        localNotification.setSmallIcon(jSObject.getString("smallIcon"));
        localNotification.setLargeIcon(jSObject.getString("largeIcon"));
        localNotification.setIconColor(jSObject.getString("iconColor"));
        localNotification.setAttachments(LocalNotificationAttachment.getAttachments(jSObject));
        localNotification.setGroupSummary(jSObject.getBoolean("groupSummary", false).booleanValue());
        localNotification.setChannelId(jSObject.getString("channelId"));
        JSObject jSObject2 = jSObject.getJSObject("schedule");
        if (jSObject2 != null) {
            localNotification.setSchedule(new LocalNotificationSchedule(jSObject2));
        }
        localNotification.setExtra(jSObject.getJSObject("extra"));
        localNotification.setOngoing(jSObject.getBoolean("ongoing", false).booleanValue());
        localNotification.setAutoCancel(jSObject.getBoolean("autoCancel", true).booleanValue());
        try {
            JSONArray jSONArray = jSObject.getJSONArray("inboxList");
            if (jSONArray != null) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(jSONArray.getString(i));
                }
                localNotification.setInboxList(arrayList);
            }
        } catch (Exception unused) {
        }
        return localNotification;
    }

    public static List<Integer> getLocalNotificationPendingList(PluginCall pluginCall) {
        List list;
        try {
            list = pluginCall.getArray("notifications").toList();
        } catch (JSONException unused) {
            list = null;
        }
        if (list == null || list.size() == 0) {
            pluginCall.reject("Must provide notifications array as notifications option");
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            try {
                arrayList.add(Integer.valueOf(((JSONObject) it.next()).getInt("id")));
            } catch (JSONException unused2) {
            }
        }
        return arrayList;
    }

    public static JSObject buildLocalNotificationPendingList(List<LocalNotification> list) {
        JSObject jSObject = new JSObject();
        JSArray jSArray = new JSArray();
        for (LocalNotification localNotification : list) {
            JSObject jSObject2 = new JSObject();
            jSObject2.put("id", (Object) localNotification.getId());
            jSObject2.put("title", localNotification.getTitle());
            jSObject2.put("body", localNotification.getBody());
            LocalNotificationSchedule schedule = localNotification.getSchedule();
            if (schedule != null) {
                JSObject jSObject3 = new JSObject();
                jSObject3.put("at", (Object) schedule.getAt());
                jSObject3.put("every", schedule.getEvery());
                jSObject3.put("count", schedule.getCount());
                jSObject3.put(DebugKt.DEBUG_PROPERTY_VALUE_ON, (Object) schedule.getOnObj());
                jSObject3.put("repeats", schedule.isRepeating());
                jSObject2.put("schedule", (Object) jSObject3);
            }
            jSObject2.put("extra", (Object) localNotification.getExtra());
            jSArray.put(jSObject2);
        }
        jSObject.put("notifications", (Object) jSArray);
        return jSObject;
    }

    public int getSmallIcon(Context context, int i) {
        String str = this.smallIcon;
        int resourceID = str != null ? AssetUtil.getResourceID(context, str, "drawable") : 0;
        return resourceID == 0 ? i : resourceID;
    }

    public Bitmap getLargeIcon(Context context) {
        String str = this.largeIcon;
        if (str == null) {
            return null;
        }
        return BitmapFactory.decodeResource(context.getResources(), AssetUtil.getResourceID(context, str, "drawable"));
    }

    public boolean isScheduled() {
        LocalNotificationSchedule localNotificationSchedule = this.schedule;
        if (localNotificationSchedule != null) {
            return (localNotificationSchedule.getOn() == null && this.schedule.getAt() == null && this.schedule.getEvery() == null) ? false : true;
        }
        return false;
    }

    public String toString() {
        return "LocalNotification{title='" + this.title + "', body='" + this.body + "', id=" + this.id + ", sound='" + this.sound + "', smallIcon='" + this.smallIcon + "', iconColor='" + this.iconColor + "', actionTypeId='" + this.actionTypeId + "', group='" + this.group + "', extra=" + this.extra + ", attachments=" + this.attachments + ", schedule=" + this.schedule + ", groupSummary=" + this.groupSummary + ", ongoing=" + this.ongoing + ", autoCancel=" + this.autoCancel + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            LocalNotification localNotification = (LocalNotification) obj;
            String str = this.title;
            if (str == null ? localNotification.title != null : !str.equals(localNotification.title)) {
                return false;
            }
            String str2 = this.body;
            if (str2 == null ? localNotification.body != null : !str2.equals(localNotification.body)) {
                return false;
            }
            String str3 = this.largeBody;
            if (str3 == null ? localNotification.largeBody != null : !str3.equals(localNotification.largeBody)) {
                return false;
            }
            Integer num = this.id;
            if (num == null ? localNotification.id != null : !num.equals(localNotification.id)) {
                return false;
            }
            String str4 = this.sound;
            if (str4 == null ? localNotification.sound != null : !str4.equals(localNotification.sound)) {
                return false;
            }
            String str5 = this.smallIcon;
            if (str5 == null ? localNotification.smallIcon != null : !str5.equals(localNotification.smallIcon)) {
                return false;
            }
            String str6 = this.largeIcon;
            if (str6 == null ? localNotification.largeIcon != null : !str6.equals(localNotification.largeIcon)) {
                return false;
            }
            String str7 = this.iconColor;
            if (str7 == null ? localNotification.iconColor != null : !str7.equals(localNotification.iconColor)) {
                return false;
            }
            String str8 = this.actionTypeId;
            if (str8 == null ? localNotification.actionTypeId != null : !str8.equals(localNotification.actionTypeId)) {
                return false;
            }
            String str9 = this.group;
            if (str9 == null ? localNotification.group != null : !str9.equals(localNotification.group)) {
                return false;
            }
            JSObject jSObject = this.extra;
            if (jSObject == null ? localNotification.extra != null : !jSObject.equals(localNotification.extra)) {
                return false;
            }
            List<LocalNotificationAttachment> list = this.attachments;
            if (list == null ? localNotification.attachments != null : !list.equals(localNotification.attachments)) {
                return false;
            }
            List<String> list2 = this.inboxList;
            if (list2 == null ? localNotification.inboxList != null : !list2.equals(localNotification.inboxList)) {
                return false;
            }
            if (this.groupSummary != localNotification.groupSummary || this.ongoing != localNotification.ongoing || this.autoCancel != localNotification.autoCancel) {
                return false;
            }
            LocalNotificationSchedule localNotificationSchedule = this.schedule;
            LocalNotificationSchedule localNotificationSchedule2 = localNotification.schedule;
            if (localNotificationSchedule != null) {
                return localNotificationSchedule.equals(localNotificationSchedule2);
            }
            if (localNotificationSchedule2 == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        String str = this.title;
        int iHashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.body;
        int iHashCode2 = (iHashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        Integer num = this.id;
        int iHashCode3 = (iHashCode2 + (num != null ? num.hashCode() : 0)) * 31;
        String str3 = this.sound;
        int iHashCode4 = (iHashCode3 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.smallIcon;
        int iHashCode5 = (iHashCode4 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.iconColor;
        int iHashCode6 = (iHashCode5 + (str5 != null ? str5.hashCode() : 0)) * 31;
        String str6 = this.actionTypeId;
        int iHashCode7 = (iHashCode6 + (str6 != null ? str6.hashCode() : 0)) * 31;
        String str7 = this.group;
        int iHashCode8 = (((((((iHashCode7 + (str7 != null ? str7.hashCode() : 0)) * 31) + Boolean.hashCode(this.groupSummary)) * 31) + Boolean.hashCode(this.ongoing)) * 31) + Boolean.hashCode(this.autoCancel)) * 31;
        JSObject jSObject = this.extra;
        int iHashCode9 = (iHashCode8 + (jSObject != null ? jSObject.hashCode() : 0)) * 31;
        List<LocalNotificationAttachment> list = this.attachments;
        int iHashCode10 = (iHashCode9 + (list != null ? list.hashCode() : 0)) * 31;
        LocalNotificationSchedule localNotificationSchedule = this.schedule;
        return iHashCode10 + (localNotificationSchedule != null ? localNotificationSchedule.hashCode() : 0);
    }

    public void setExtraFromString(String str) {
        try {
            this.extra = JSObject.fromJSONObject(new JSONObject(str));
        } catch (JSONException e) {
            Logger.error(Logger.tags("LN"), "Cannot rebuild extra data", e);
        }
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }
}
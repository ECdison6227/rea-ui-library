package com.capacitorjs.plugins.localnotifications;

import android.content.Context;
import android.content.SharedPreferences;
import com.getcapacitor.JSObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationStorage {
    private static final String ACTION_TYPES_ID = "ACTION_TYPE_STORE";
    private static final String ID_KEY = "notificationIds";
    private static final String NOTIFICATION_STORE_ID = "NOTIFICATION_STORE";
    private Context context;

    public NotificationStorage(Context context) {
        this.context = context;
    }

    public void appendNotifications(List<LocalNotification> list) {
        SharedPreferences.Editor editorEdit = getStorage(NOTIFICATION_STORE_ID).edit();
        for (LocalNotification localNotification : list) {
            if (localNotification.isScheduled()) {
                editorEdit.putString(localNotification.getId().toString(), localNotification.getSource());
            }
        }
        editorEdit.apply();
    }

    public List<String> getSavedNotificationIds() {
        Map<String, ?> all = getStorage(NOTIFICATION_STORE_ID).getAll();
        if (all != null) {
            return new ArrayList(all.keySet());
        }
        return new ArrayList();
    }

    public List<LocalNotification> getSavedNotifications() {
        Map<String, ?> all = getStorage(NOTIFICATION_STORE_ID).getAll();
        if (all != null) {
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = all.keySet().iterator();
            while (it.hasNext()) {
                JSObject notificationFromJSONString = getNotificationFromJSONString((String) all.get(it.next()));
                if (notificationFromJSONString != null) {
                    try {
                        arrayList.add(LocalNotification.buildNotificationFromJSObject(notificationFromJSONString));
                    } catch (ParseException unused) {
                    }
                }
            }
            return arrayList;
        }
        return new ArrayList();
    }

    public JSObject getNotificationFromJSONString(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new JSObject(str);
        } catch (JSONException unused) {
            return null;
        }
    }

    public JSObject getSavedNotificationAsJSObject(String str) {
        try {
            String string = getStorage(NOTIFICATION_STORE_ID).getString(str, null);
            if (string == null) {
                return null;
            }
            return new JSObject(string);
        } catch (ClassCastException | JSONException unused) {
            return null;
        }
    }

    public LocalNotification getSavedNotification(String str) {
        JSObject savedNotificationAsJSObject = getSavedNotificationAsJSObject(str);
        if (savedNotificationAsJSObject == null) {
            return null;
        }
        try {
            return LocalNotification.buildNotificationFromJSObject(savedNotificationAsJSObject);
        } catch (ParseException unused) {
            return null;
        }
    }

    public void deleteNotification(String str) {
        SharedPreferences.Editor editorEdit = getStorage(NOTIFICATION_STORE_ID).edit();
        editorEdit.remove(str);
        editorEdit.apply();
    }

    private SharedPreferences getStorage(String str) {
        return this.context.getSharedPreferences(str, 0);
    }

    public void writeActionGroup(Map<String, NotificationAction[]> map) {
        for (String str : map.keySet()) {
            SharedPreferences.Editor editorEdit = getStorage(ACTION_TYPES_ID + str).edit();
            editorEdit.clear();
            NotificationAction[] notificationActionArr = map.get(str);
            editorEdit.putInt("count", notificationActionArr.length);
            for (int i = 0; i < notificationActionArr.length; i++) {
                editorEdit.putString("id" + i, notificationActionArr[i].getId());
                editorEdit.putString("title" + i, notificationActionArr[i].getTitle());
                editorEdit.putBoolean("input" + i, notificationActionArr[i].isInput());
            }
            editorEdit.apply();
        }
    }

    public NotificationAction[] getActionGroup(String str) {
        SharedPreferences storage = getStorage(ACTION_TYPES_ID + str);
        int i = storage.getInt("count", 0);
        NotificationAction[] notificationActionArr = new NotificationAction[i];
        for (int i2 = 0; i2 < i; i2++) {
            notificationActionArr[i2] = new NotificationAction(storage.getString("id" + i2, ""), storage.getString("title" + i2, ""), Boolean.valueOf(storage.getBoolean("input" + i2, false)));
        }
        return notificationActionArr;
    }
}
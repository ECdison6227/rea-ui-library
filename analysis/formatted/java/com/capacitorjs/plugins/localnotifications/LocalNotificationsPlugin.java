package com.capacitorjs.plugins.localnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.activity.result.ActivityResult;
import androidx.core.app.NotificationCompat;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginHandle;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@CapacitorPlugin(name = "LocalNotifications", permissions = {@Permission(alias = LocalNotificationsPlugin.LOCAL_NOTIFICATIONS, strings = {"android.permission.POST_NOTIFICATIONS"})})
public class LocalNotificationsPlugin extends Plugin {
    static final String LOCAL_NOTIFICATIONS = "display";
    private static Bridge staticBridge;
    private LocalNotificationManager manager;
    private NotificationChannelManager notificationChannelManager;
    public NotificationManager notificationManager;
    private NotificationStorage notificationStorage;

    @Override // com.getcapacitor.Plugin
    public void load() {
        super.load();
        this.notificationStorage = new NotificationStorage(getContext());
        LocalNotificationManager localNotificationManager = new LocalNotificationManager(this.notificationStorage, getActivity(), getContext(), this.bridge.getConfig());
        this.manager = localNotificationManager;
        localNotificationManager.createNotificationChannel();
        this.notificationChannelManager = new NotificationChannelManager(getActivity());
        this.notificationManager = (NotificationManager) getActivity().getSystemService("notification");
        staticBridge = this.bridge;
    }

    @Override // com.getcapacitor.Plugin
    protected void handleOnNewIntent(Intent intent) {
        JSObject jSObjectHandleNotificationActionPerformed;
        super.handleOnNewIntent(intent);
        if ("android.intent.action.MAIN".equals(intent.getAction()) && (jSObjectHandleNotificationActionPerformed = this.manager.handleNotificationActionPerformed(intent, this.notificationStorage)) != null) {
            notifyListeners("localNotificationActionPerformed", jSObjectHandleNotificationActionPerformed, true);
        }
    }

    @PluginMethod
    public void schedule(PluginCall pluginCall) {
        JSONArray jSONArraySchedule;
        List<LocalNotification> listBuildNotificationList = LocalNotification.buildNotificationList(pluginCall);
        if (listBuildNotificationList == null || (jSONArraySchedule = this.manager.schedule(pluginCall, listBuildNotificationList)) == null) {
            return;
        }
        this.notificationStorage.appendNotifications(listBuildNotificationList);
        JSObject jSObject = new JSObject();
        JSArray jSArray = new JSArray();
        for (int i = 0; i < jSONArraySchedule.length(); i++) {
            try {
                jSArray.put(new JSObject().put("id", jSONArraySchedule.getInt(i)));
            } catch (Exception unused) {
            }
        }
        jSObject.put("notifications", (Object) jSArray);
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void cancel(PluginCall pluginCall) {
        this.manager.cancel(pluginCall);
    }

    @PluginMethod
    public void getPending(PluginCall pluginCall) {
        pluginCall.resolve(LocalNotification.buildLocalNotificationPendingList(this.notificationStorage.getSavedNotifications()));
    }

    @PluginMethod
    public void registerActionTypes(PluginCall pluginCall) {
        this.notificationStorage.writeActionGroup(NotificationAction.buildTypes(pluginCall.getArray("types")));
        pluginCall.resolve();
    }

    @PluginMethod
    public void areEnabled(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put("value", this.manager.areNotificationsEnabled());
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void getDeliveredNotifications(PluginCall pluginCall) {
        JSArray jSArray = new JSArray();
        for (StatusBarNotification statusBarNotification : this.notificationManager.getActiveNotifications()) {
            JSObject jSObject = new JSObject();
            jSObject.put("id", statusBarNotification.getId());
            jSObject.put("tag", statusBarNotification.getTag());
            Notification notification = statusBarNotification.getNotification();
            if (notification != null) {
                jSObject.put("title", (Object) notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE));
                jSObject.put("body", (Object) notification.extras.getCharSequence(NotificationCompat.EXTRA_TEXT));
                jSObject.put("group", notification.getGroup());
                jSObject.put("groupSummary", (notification.flags & 512) != 0);
                JSObject jSObject2 = new JSObject();
                for (String str : notification.extras.keySet()) {
                    jSObject2.put(str, notification.extras.get(str));
                }
                jSObject.put("data", (Object) jSObject2);
            }
            jSArray.put(jSObject);
        }
        JSObject jSObject3 = new JSObject();
        jSObject3.put("notifications", (Object) jSArray);
        pluginCall.resolve(jSObject3);
    }

    @PluginMethod
    public void removeDeliveredNotifications(PluginCall pluginCall) {
        try {
            for (Object obj : pluginCall.getArray("notifications").toList()) {
                if (obj instanceof JSONObject) {
                    JSObject jSObjectFromJSONObject = JSObject.fromJSONObject((JSONObject) obj);
                    String string = jSObjectFromJSONObject.getString("tag");
                    Integer integer = jSObjectFromJSONObject.getInteger("id");
                    if (string == null) {
                        this.notificationManager.cancel(integer.intValue());
                    } else {
                        this.notificationManager.cancel(string, integer.intValue());
                    }
                } else {
                    pluginCall.reject("Expected notifications to be a list of notification objects");
                }
            }
        } catch (JSONException e) {
            pluginCall.reject(e.getMessage());
        }
        pluginCall.resolve();
    }

    @PluginMethod
    public void removeAllDeliveredNotifications(PluginCall pluginCall) {
        this.notificationManager.cancelAll();
        pluginCall.resolve();
    }

    @PluginMethod
    public void createChannel(PluginCall pluginCall) {
        this.notificationChannelManager.createChannel(pluginCall);
    }

    @PluginMethod
    public void deleteChannel(PluginCall pluginCall) {
        this.notificationChannelManager.deleteChannel(pluginCall);
    }

    @PluginMethod
    public void listChannels(PluginCall pluginCall) {
        this.notificationChannelManager.listChannels(pluginCall);
    }

    @Override // com.getcapacitor.Plugin
    @PluginMethod
    public void checkPermissions(PluginCall pluginCall) {
        if (Build.VERSION.SDK_INT < 33) {
            JSObject jSObject = new JSObject();
            jSObject.put(LOCAL_NOTIFICATIONS, getNotificationPermissionText());
            pluginCall.resolve(jSObject);
            return;
        }
        super.checkPermissions(pluginCall);
    }

    @Override // com.getcapacitor.Plugin
    @PluginMethod
    public void requestPermissions(PluginCall pluginCall) {
        if (Build.VERSION.SDK_INT < 33 || getPermissionState(LOCAL_NOTIFICATIONS) == PermissionState.GRANTED) {
            JSObject jSObject = new JSObject();
            jSObject.put(LOCAL_NOTIFICATIONS, getNotificationPermissionText());
            pluginCall.resolve(jSObject);
            return;
        }
        requestPermissionForAlias(LOCAL_NOTIFICATIONS, pluginCall, "permissionsCallback");
    }

    @PluginMethod
    public void changeExactNotificationSetting(PluginCall pluginCall) {
        if (Build.VERSION.SDK_INT >= 31) {
            startActivityForResult(pluginCall, new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM", Uri.parse("package:" + getActivity().getPackageName())), "alarmPermissionsCallback");
        } else {
            checkExactNotificationSetting(pluginCall);
        }
    }

    @PluginMethod
    public void checkExactNotificationSetting(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put("exact_alarm", getExactAlarmPermissionText());
        pluginCall.resolve(jSObject);
    }

    @PermissionCallback
    private void permissionsCallback(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put(LOCAL_NOTIFICATIONS, getNotificationPermissionText());
        pluginCall.resolve(jSObject);
    }

    @ActivityCallback
    private void alarmPermissionsCallback(PluginCall pluginCall, ActivityResult activityResult) {
        checkExactNotificationSetting(pluginCall);
    }

    private String getNotificationPermissionText() {
        if (this.manager.areNotificationsEnabled()) {
            return "granted";
        }
        return "denied";
    }

    private String getExactAlarmPermissionText() {
        return (Build.VERSION.SDK_INT < 31 || ((AlarmManager) getActivity().getSystemService(NotificationCompat.CATEGORY_ALARM)).canScheduleExactAlarms()) ? "granted" : "denied";
    }

    public static void fireReceived(JSObject jSObject) {
        LocalNotificationsPlugin localNotificationsInstance = getLocalNotificationsInstance();
        if (localNotificationsInstance != null) {
            localNotificationsInstance.notifyListeners("localNotificationReceived", jSObject, true);
        }
    }

    public static LocalNotificationsPlugin getLocalNotificationsInstance() {
        PluginHandle plugin;
        Bridge bridge = staticBridge;
        if (bridge == null || bridge.getWebView() == null || (plugin = staticBridge.getPlugin("LocalNotifications")) == null) {
            return null;
        }
        return (LocalNotificationsPlugin) plugin.getInstance();
    }
}
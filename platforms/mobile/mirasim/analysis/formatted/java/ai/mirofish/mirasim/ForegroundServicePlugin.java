package ai.mirofish.mirasim;

import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.core.content.ContextCompat;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.common.internal.ImagesContract;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "ForegroundService")
public class ForegroundServicePlugin extends Plugin {
    private static volatile ForegroundServicePlugin instance;
    private static volatile long jsAliveAt;

    @Override // com.getcapacitor.Plugin
    public void load() {
        instance = this;
    }

    @Override // com.getcapacitor.Plugin
    protected void handleOnDestroy() {
        if (instance == this) {
            instance = null;
        }
    }

    static long jsAliveAgoMs() {
        long j = jsAliveAt;
        if (j == 0) {
            return Long.MAX_VALUE;
        }
        return SystemClock.elapsedRealtime() - j;
    }

    static void dispatchSystemWake(String str) {
        ForegroundServicePlugin foregroundServicePlugin = instance;
        if (foregroundServicePlugin == null) {
            return;
        }
        JSObject jSObject = new JSObject();
        jSObject.put("reason", str);
        foregroundServicePlugin.notifyListeners("systemWake", jSObject);
    }

    static void dispatchAttentionTicketRefused() {
        ForegroundServicePlugin foregroundServicePlugin = instance;
        if (foregroundServicePlugin == null) {
            return;
        }
        foregroundServicePlugin.notifyListeners("attentionTicketRefused", new JSObject());
    }

    static void dispatchMachineAttention(JSONObject jSONObject) {
        ForegroundServicePlugin foregroundServicePlugin = instance;
        if (foregroundServicePlugin == null) {
            return;
        }
        try {
            foregroundServicePlugin.notifyListeners("machineAttention", JSObject.fromJSONObject(jSONObject));
        } catch (Exception unused) {
        }
    }

    @PluginMethod
    public void start(PluginCall pluginCall) {
        Intent intent = new Intent(getContext(), (Class<?>) KeepAliveService.class);
        intent.putExtra("text", pluginCall.getString("text", ""));
        ContextCompat.startForegroundService(getContext(), intent);
        pluginCall.resolve();
    }

    @PluginMethod
    public void stop(PluginCall pluginCall) {
        Intent intent = new Intent(getContext(), (Class<?>) KeepAliveService.class);
        intent.setAction("ai.mirofish.mirasim.KEEPALIVE_STOP");
        getContext().startService(intent);
        pluginCall.resolve();
    }

    @PluginMethod
    public void jsAlive(PluginCall pluginCall) {
        jsAliveAt = SystemClock.elapsedRealtime();
        pluginCall.resolve();
    }

    @PluginMethod
    public void syncChannelCredential(PluginCall pluginCall) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("rendezvous", pluginCall.getString("rendezvous", ""));
            jSONObject.put("nodeId", pluginCall.getString("nodeId", ""));
            jSONObject.put("token", pluginCall.getString("token", ""));
            jSONObject.put("e2e", pluginCall.getString("e2e", ""));
            ChannelCredentialStore.save(getContext(), jSONObject);
            nudgeServiceResync();
            pluginCall.resolve();
        } catch (Exception e) {
            pluginCall.reject("credential sync failed", e);
        }
    }

    @PluginMethod
    public void clearChannelCredential(PluginCall pluginCall) {
        ChannelCredentialStore.clear(getContext());
        nudgeServiceResync();
        pluginCall.resolve();
    }

    @PluginMethod
    public void syncAttentionTicket(PluginCall pluginCall) {
        JSObject data = pluginCall.getData();
        String strOptString = data.optString("ticket", "");
        String strOptString2 = data.optString(ImagesContract.URL, "");
        long jOptLong = data.optLong("expiresAt", 0L);
        if (!strOptString.isEmpty() && strOptString2.startsWith("wss://") && jOptLong > System.currentTimeMillis()) {
            try {
                ChannelCredentialStore.saveAttentionTicket(getContext(), new JSONObject().put("ticket", strOptString).put(ImagesContract.URL, strOptString2).put("expiresAt", jOptLong).put("userId", data.optString("userId", "")).put("account", data.optString("account", "")));
                nudgeServiceResync();
                pluginCall.resolve();
                return;
            } catch (Exception e) {
                pluginCall.reject("attention ticket sync failed", e);
                return;
            }
        }
        pluginCall.reject("not a usable attention ticket");
    }

    @PluginMethod
    public void clearAttentionTicket(PluginCall pluginCall) {
        ChannelCredentialStore.clearAttentionTicket(getContext());
        nudgeServiceResync();
        pluginCall.resolve();
    }

    @PluginMethod
    public void attentionTicket(PluginCall pluginCall) {
        JSONObject jSONObjectLoadAttentionTicket = ChannelCredentialStore.loadAttentionTicket(getContext());
        JSObject jSObject = new JSObject();
        if (jSONObjectLoadAttentionTicket != null && !jSONObjectLoadAttentionTicket.optString("ticket", "").isEmpty()) {
            jSObject.put("expiresAt", jSONObjectLoadAttentionTicket.optLong("expiresAt", 0L));
            jSObject.put("userId", jSONObjectLoadAttentionTicket.optString("userId", ""));
            jSObject.put("account", jSONObjectLoadAttentionTicket.optString("account", ""));
        }
        pluginCall.resolve(jSObject);
    }

    private void nudgeServiceResync() {
        try {
            Intent intent = new Intent(getContext(), (Class<?>) KeepAliveService.class);
            intent.setAction("ai.mirofish.mirasim.KEEPALIVE_RESYNC");
            getContext().startService(intent);
        } catch (Exception unused) {
        }
    }

    @PluginMethod
    public void batteryStatus(PluginCall pluginCall) {
        PowerManager powerManager = (PowerManager) getContext().getSystemService("power");
        JSObject jSObject = new JSObject();
        jSObject.put("exempt", powerManager != null && powerManager.isIgnoringBatteryOptimizations(getContext().getPackageName()));
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void requestBatteryExemption(PluginCall pluginCall) {
        Intent intent = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        getActivity().startActivity(intent);
        pluginCall.resolve();
    }

    @PluginMethod
    public void vendorAutostartAvailable(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put("available", VendorAutostart.resolve(getContext()) != null);
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void openVendorAutostart(PluginCall pluginCall) {
        Intent intentResolve = VendorAutostart.resolve(getContext());
        if (intentResolve == null) {
            pluginCall.resolve();
        } else {
            try {
                getActivity().startActivity(intentResolve);
            } catch (Exception unused) {
            }
            pluginCall.resolve();
        }
    }

    @PluginMethod
    public void openNotificationSettings(PluginCall pluginCall) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());
        getActivity().startActivity(intent);
        pluginCall.resolve();
    }
}
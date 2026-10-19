package ai.mirofish.mirasim;

import android.content.Intent;
import android.os.Build;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "AttentionNotifier")
public class AttentionNotifierPlugin extends Plugin {
    static final String CHANNEL_ID = "mirasim.attention.v2";
    static final String EXTRA_CHAT_LINK = "mirasim.notify.chatLink";
    static final String EXTRA_SESSION_KEY = "mirasim.notify.sessionKey";

    @PluginMethod
    public void post(PluginCall pluginCall) {
        Integer num = pluginCall.getInt("id");
        if (num == null) {
            pluginCall.reject("id required");
            return;
        }
        AttentionPoster.ensureChannel(getContext(), pluginCall.getString("channelName", "Attention"), pluginCall.getString("channelDesc", ""));
        AttentionPoster.post(getContext(), num.intValue(), pluginCall.getString("title", ""), pluginCall.getString("body", ""), pluginCall.getString("sessionKey"));
        pluginCall.resolve();
    }

    @PluginMethod
    public void postChat(PluginCall pluginCall) {
        long jMax;
        Integer num = pluginCall.getInt("id");
        String string = pluginCall.getString("link");
        if (num == null || string == null || string.isEmpty()) {
            pluginCall.reject("id and link required");
            return;
        }
        String string2 = pluginCall.getString("title", "");
        String string3 = pluginCall.getString("sender", "");
        String string4 = pluginCall.getString("conversationTitle", "");
        int iLastIndexOf = string2.lastIndexOf(" · ");
        if (string3.isEmpty()) {
            string3 = iLastIndexOf >= 0 ? string2.substring(iLastIndexOf + 3) : string2;
        }
        boolean zIsEmpty = string4.isEmpty();
        boolean z = !zIsEmpty;
        if (zIsEmpty) {
            if (iLastIndexOf >= 0) {
                string2 = string2.substring(0, iLastIndexOf);
            }
            string4 = string2;
        }
        JSObject data = pluginCall.getData();
        if (data.has("thread")) {
            jMax = data.isNull("thread") ? 0L : Math.max(0L, data.optLong("thread", 0L));
        } else {
            jMax = -1;
        }
        ChatRows.post(getContext(), new ChatRowLedger.Post(num.intValue(), string, string3, pluginCall.getString("body", "")).thread(jMax).title(string4, z).group((!data.has("isGroup") || data.isNull("isGroup")) ? null : Boolean.valueOf(data.optBoolean("isGroup", true))).machineName(pluginCall.getString("machineName", "")));
        pluginCall.resolve();
    }

    @PluginMethod
    public void cancel(PluginCall pluginCall) {
        Integer num = pluginCall.getInt("id");
        if (num == null) {
            pluginCall.reject("id required");
        } else {
            ChatRows.withdraw(getContext(), num.intValue());
            pluginCall.resolve();
        }
    }

    @PluginMethod
    public void chatAttentionSeen(PluginCall pluginCall) {
        String string = pluginCall.getString("machine", "");
        String string2 = pluginCall.getString("room", "");
        long jOptLong = pluginCall.getData().optLong("seq", 0L);
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty() || jOptLong <= 0) {
            pluginCall.reject("machine, room and seq required");
        } else {
            ChatRowLedger.seenByApp(string, string2, jOptLong);
            pluginCall.resolve();
        }
    }

    @PluginMethod
    public void withdrawChat(PluginCall pluginCall) {
        String string = pluginCall.getString("machine", "");
        String string2 = pluginCall.getString("room", "");
        if (string == null || string2 == null || string2.isEmpty()) {
            pluginCall.reject("room required");
            return;
        }
        JSObject data = pluginCall.getData();
        if (data.has("seq")) {
            ChatRows.retract(getContext(), string, string2, data.optLong("seq", 0L));
        } else if (data.has("upTo")) {
            ChatRows.read(getContext(), string, string2, data.optLong("thread", 0L), data.optLong("upTo", 0L));
        } else {
            ChatRows.roomGone(getContext(), string, string2);
        }
        pluginCall.resolve();
    }

    @PluginMethod
    public void openChannelSettings(PluginCall pluginCall) {
        AttentionPoster.ensureChannel(getContext(), pluginCall.getString("channelName", "Attention"), pluginCall.getString("channelDesc", ""));
        Intent intent = new Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", getContext().getPackageName());
        intent.putExtra("android.provider.extra.CHANNEL_ID", CHANNEL_ID);
        getActivity().startActivity(intent);
        pluginCall.resolve();
    }

    @PluginMethod
    public void deviceProfile(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put("manufacturer", Build.MANUFACTURER == null ? "" : Build.MANUFACTURER);
        pluginCall.resolve(jSObject);
    }

    @Override // com.getcapacitor.Plugin
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        deliverTap(intent);
    }

    @Override // com.getcapacitor.Plugin
    public void load() {
        if (getActivity() != null) {
            deliverTap(getActivity().getIntent());
        }
    }

    private void deliverTap(Intent intent) {
        if (intent == null) {
            return;
        }
        String stringExtra = intent.getStringExtra(EXTRA_SESSION_KEY);
        if (stringExtra != null) {
            intent.removeExtra(EXTRA_SESSION_KEY);
            JSObject jSObject = new JSObject();
            jSObject.put("sessionKey", stringExtra);
            notifyListeners("attentionTap", jSObject, true);
        }
        String stringExtra2 = intent.getStringExtra(EXTRA_CHAT_LINK);
        if (stringExtra2 != null) {
            intent.removeExtra(EXTRA_CHAT_LINK);
            JSObject jSObject2 = new JSObject();
            jSObject2.put("link", stringExtra2);
            notifyListeners("chatTap", jSObject2, true);
        }
    }
}
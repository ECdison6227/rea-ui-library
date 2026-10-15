package ai.mirofish.mirasim;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.Bridge;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "WindowFloor")
public class WindowFloorPlugin extends Plugin {
    @PluginMethod
    public void setColor(final PluginCall pluginCall) {
        Integer num = parse(pluginCall.getString("color"));
        final AppCompatActivity activity = getActivity();
        if (num == null) {
            pluginCall.reject("WindowFloor.setColor: expected #rrggbb");
        } else if (activity == null) {
            pluginCall.resolve();
        } else {
            final int iIntValue = num.intValue();
            activity.runOnUiThread(new Runnable() { // from class: ai.mirofish.mirasim.WindowFloorPlugin$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setColor$0(activity, iIntValue, pluginCall);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setColor$0(Activity activity, int i, PluginCall pluginCall) {
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(i));
        Bridge bridge = getBridge();
        WebView webView = bridge != null ? bridge.getWebView() : null;
        if (webView != null) {
            webView.setBackgroundColor(i);
            Object parent = webView.getParent();
            if (parent instanceof View) {
                ((View) parent).setBackgroundColor(i);
            }
        }
        pluginCall.resolve();
    }

    private static Integer parse(String str) {
        if (str != null && str.matches("#[0-9a-fA-F]{6}")) {
            try {
                return Integer.valueOf(Color.parseColor(str));
            } catch (IllegalArgumentException unused) {
            }
        }
        return null;
    }
}
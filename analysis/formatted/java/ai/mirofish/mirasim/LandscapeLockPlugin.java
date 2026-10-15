package ai.mirofish.mirasim;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import kotlinx.coroutines.DebugKt;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "LandscapeLock")
public class LandscapeLockPlugin extends Plugin {
    @PluginMethod
    public void setLandscape(final PluginCall pluginCall) {
        final boolean zEquals = Boolean.TRUE.equals(pluginCall.getBoolean(DebugKt.DEBUG_PROPERTY_VALUE_ON, false));
        final AppCompatActivity activity = getActivity();
        if (activity == null) {
            pluginCall.resolve();
        } else {
            activity.runOnUiThread(new Runnable() { // from class: ai.mirofish.mirasim.LandscapeLockPlugin$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LandscapeLockPlugin.lambda$setLandscape$0(activity, zEquals, pluginCall);
                }
            });
        }
    }

    static /* synthetic */ void lambda$setLandscape$0(Activity activity, boolean z, PluginCall pluginCall) {
        activity.setRequestedOrientation(z ? 6 : -1);
        pluginCall.resolve();
    }
}
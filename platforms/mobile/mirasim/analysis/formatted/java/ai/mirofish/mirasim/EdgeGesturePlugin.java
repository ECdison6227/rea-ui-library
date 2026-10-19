package ai.mirofish.mirasim;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "EdgeGesture")
public class EdgeGesturePlugin extends Plugin {
    private static final int EDGE_DP = 24;
    private static final int MAX_EXCLUSION_DP = 200;
    private boolean claimed = false;
    private final View.OnLayoutChangeListener onLayout = new View.OnLayoutChangeListener() { // from class: ai.mirofish.mirasim.EdgeGesturePlugin$$ExternalSyntheticLambda0
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            this.f$0.lambda$new$0(view, i, i2, i3, i4, i5, i6, i7, i8);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.claimed) {
            apply(true);
        }
    }

    @Override // com.getcapacitor.Plugin
    public void load() {
        View viewWebViewOrNull = webViewOrNull();
        if (viewWebViewOrNull != null) {
            viewWebViewOrNull.addOnLayoutChangeListener(this.onLayout);
        }
    }

    @Override // com.getcapacitor.Plugin
    protected void handleOnDestroy() {
        View viewWebViewOrNull = webViewOrNull();
        if (viewWebViewOrNull != null) {
            viewWebViewOrNull.removeOnLayoutChangeListener(this.onLayout);
        }
    }

    @PluginMethod
    public void setLeftEdgeClaimed(PluginCall pluginCall) {
        boolean zEquals = Boolean.TRUE.equals(pluginCall.getBoolean("claimed", false));
        this.claimed = zEquals;
        apply(zEquals);
        pluginCall.resolve();
    }

    private void apply(boolean z) {
        View viewWebViewOrNull;
        if (Build.VERSION.SDK_INT >= 29 && (viewWebViewOrNull = webViewOrNull()) != null) {
            if (!z) {
                viewWebViewOrNull.setSystemGestureExclusionRects(Collections.emptyList());
                return;
            }
            float f = viewWebViewOrNull.getResources().getDisplayMetrics().density;
            int iRound = Math.round(24.0f * f);
            int iMin = Math.min(Math.round(f * 200.0f), viewWebViewOrNull.getHeight());
            if (iRound <= 0 || iMin <= 0) {
                return;
            }
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(new Rect(0, viewWebViewOrNull.getHeight() - iMin, iRound, viewWebViewOrNull.getHeight()));
            viewWebViewOrNull.setSystemGestureExclusionRects(arrayList);
        }
    }

    private View webViewOrNull() {
        if (getBridge() != null) {
            return getBridge().getWebView();
        }
        return null;
    }
}
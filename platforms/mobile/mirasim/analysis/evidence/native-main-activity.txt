package ai.mirofish.mirasim;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.FrameMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Logger;
import com.getcapacitor.WebViewListener;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends BridgeActivity {
    private static final int APP_SURFACE_0 = -14935271;
    private static final int FIRST_FRAME_MAX_ATTEMPTS = 5;
    private static final long FIRST_FRAME_RETRY_MS = 2000;
    private static final long PRESENTATION_GRACE_MS = 100;
    private static final long RECOVERY_COOLDOWN_MS = 15000;
    private static final long RESUME_FRAME_DEADLINE_MS = 700;
    private static long lastRecoveryAt;
    private long resumeFrameToken = 0;
    private boolean firstFramePresented = false;
    private Window.OnFrameMetricsAvailableListener frameReceipt = null;
    private int firstFrameAttempts = 0;
    private boolean resumed = false;

    @Override // com.getcapacitor.BridgeActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        registerPlugin(ForegroundServicePlugin.class);
        registerPlugin(AttentionNotifierPlugin.class);
        registerPlugin(ApkUpdaterPlugin.class);
        registerPlugin(SaveFilePlugin.class);
        registerPlugin(EdgeGesturePlugin.class);
        registerPlugin(LandscapeLockPlugin.class);
        registerPlugin(WindowFloorPlugin.class);
        super.onCreate(bundle);
        getWindow().setBackgroundDrawable(new ColorDrawable(APP_SURFACE_0));
        armFirstFrameReceipt();
        getBridge().addWebViewListener(new WebViewListener() { // from class: ai.mirofish.mirasim.MainActivity.1
            @Override // com.getcapacitor.WebViewListener
            public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                return MainActivity.this.recoverFromDeadRenderer(webView, renderProcessGoneDetail);
            }
        });
    }

    @Override // com.getcapacitor.BridgeActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.resumed = true;
        WebView webView = getBridge() != null ? getBridge().getWebView() : null;
        if (webView == null) {
            return;
        }
        checkPresentation(webView, new Handler(Looper.getMainLooper()));
    }

    @Override // com.getcapacitor.BridgeActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.resumed = false;
        super.onPause();
    }

    private void checkPresentation(final WebView webView, final Handler handler) {
        final long j = this.resumeFrameToken + 1;
        this.resumeFrameToken = j;
        final boolean[] zArr = {false};
        final ViewTreeObserver.OnDrawListener[] onDrawListenerArr = {null};
        handler.post(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$checkPresentation$0(webView, j, handler, zArr, onDrawListenerArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPresentation$0(final WebView webView, final long j, final Handler handler, final boolean[] zArr, final ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
        webView.requestLayout();
        webView.invalidate();
        webView.postVisualStateCallback(j, new AnonymousClass2(j, handler, webView, zArr, onDrawListenerArr));
        handler.postDelayed(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity.3
            private boolean graced = false;

            @Override // java.lang.Runnable
            public void run() {
                if (zArr[0] || j != MainActivity.this.resumeFrameToken) {
                    MainActivity.this.removeDrawProbe(webView, onDrawListenerArr);
                    return;
                }
                ViewTreeObserver.OnDrawListener[] onDrawListenerArr2 = onDrawListenerArr;
                if (onDrawListenerArr2[0] != null && !this.graced) {
                    this.graced = true;
                    handler.postDelayed(this, MainActivity.PRESENTATION_GRACE_MS);
                } else {
                    MainActivity.this.removeDrawProbe(webView, onDrawListenerArr2);
                    Logger.warn("No frame 700ms after resume — re-acquiring the surface");
                    MainActivity.this.reacquireSurface(webView, handler);
                    MainActivity.this.scheduleFirstFrameRetry(webView, handler);
                }
            }
        }, 700L);
    }

    /* JADX INFO: renamed from: ai.mirofish.mirasim.MainActivity$2, reason: invalid class name */
    class AnonymousClass2 extends WebView.VisualStateCallback {
        final /* synthetic */ Handler val$main;
        final /* synthetic */ boolean[] val$presented;
        final /* synthetic */ ViewTreeObserver.OnDrawListener[] val$probe;
        final /* synthetic */ long val$token;
        final /* synthetic */ WebView val$view;

        AnonymousClass2(long j, Handler handler, WebView webView, boolean[] zArr, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
            this.val$token = j;
            this.val$main = handler;
            this.val$view = webView;
            this.val$presented = zArr;
            this.val$probe = onDrawListenerArr;
        }

        @Override // android.webkit.WebView.VisualStateCallback
        public void onComplete(long j) {
            final long j2 = this.val$token;
            if (j != j2) {
                return;
            }
            final Handler handler = this.val$main;
            final WebView webView = this.val$view;
            final boolean[] zArr = this.val$presented;
            final ViewTreeObserver.OnDrawListener[] onDrawListenerArr = this.val$probe;
            handler.post(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onComplete$0(j2, webView, handler, zArr, onDrawListenerArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onComplete$0(long j, WebView webView, Handler handler, boolean[] zArr, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
            if (j != MainActivity.this.resumeFrameToken) {
                return;
            }
            MainActivity.this.armPresentationProbe(webView, handler, zArr, onDrawListenerArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void armPresentationProbe(WebView webView, Handler handler, boolean[] zArr, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
        ViewTreeObserver viewTreeObserver = webView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(zArr, handler, webView, onDrawListenerArr);
            onDrawListenerArr[0] = anonymousClass4;
            viewTreeObserver.addOnDrawListener(anonymousClass4);
            webView.invalidate();
        }
    }

    /* JADX INFO: renamed from: ai.mirofish.mirasim.MainActivity$4, reason: invalid class name */
    class AnonymousClass4 implements ViewTreeObserver.OnDrawListener {
        final /* synthetic */ Handler val$main;
        final /* synthetic */ boolean[] val$presented;
        final /* synthetic */ ViewTreeObserver.OnDrawListener[] val$probe;
        final /* synthetic */ WebView val$view;

        AnonymousClass4(boolean[] zArr, Handler handler, WebView webView, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
            this.val$presented = zArr;
            this.val$main = handler;
            this.val$view = webView;
            this.val$probe = onDrawListenerArr;
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            this.val$presented[0] = true;
            Handler handler = this.val$main;
            final WebView webView = this.val$view;
            final ViewTreeObserver.OnDrawListener[] onDrawListenerArr = this.val$probe;
            handler.post(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDraw$0(webView, onDrawListenerArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDraw$0(WebView webView, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
            MainActivity.this.removeDrawProbe(webView, onDrawListenerArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDrawProbe(WebView webView, ViewTreeObserver.OnDrawListener[] onDrawListenerArr) {
        if (onDrawListenerArr[0] == null) {
            return;
        }
        ViewTreeObserver viewTreeObserver = webView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnDrawListener(onDrawListenerArr[0]);
        }
        onDrawListenerArr[0] = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reacquireSurface(final WebView webView, final Handler handler) {
        final long j = this.resumeFrameToken + 1;
        this.resumeFrameToken = j;
        final boolean[] zArr = {false};
        final Runnable runnable = new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.lambda$reacquireSurface$1(zArr, webView);
            }
        };
        webView.setVisibility(4);
        webView.postVisualStateCallback(j, new WebView.VisualStateCallback(this) { // from class: ai.mirofish.mirasim.MainActivity.5
            @Override // android.webkit.WebView.VisualStateCallback
            public void onComplete(long j2) {
                if (j2 == j) {
                    handler.post(runnable);
                }
            }
        });
        handler.postDelayed(runnable, 700L);
    }

    static /* synthetic */ void lambda$reacquireSurface$1(boolean[] zArr, WebView webView) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        webView.setVisibility(0);
        webView.invalidate();
    }

    private void armFirstFrameReceipt() {
        final Handler handler = new Handler(Looper.getMainLooper());
        this.frameReceipt = new Window.OnFrameMetricsAvailableListener() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.Window.OnFrameMetricsAvailableListener
            public final void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int i) {
                this.f$0.lambda$armFirstFrameReceipt$2(handler, window, frameMetrics, i);
            }
        };
        getWindow().addOnFrameMetricsAvailableListener(this.frameReceipt, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$armFirstFrameReceipt$2(Handler handler, Window window, FrameMetrics frameMetrics, int i) {
        if (this.firstFramePresented) {
            return;
        }
        this.firstFramePresented = true;
        handler.post(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.disarmFirstFrameReceipt();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disarmFirstFrameReceipt() {
        if (this.frameReceipt == null) {
            return;
        }
        try {
            getWindow().removeOnFrameMetricsAvailableListener(this.frameReceipt);
        } catch (Throwable unused) {
        }
        this.frameReceipt = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleFirstFrameRetry(final WebView webView, final Handler handler) {
        if (this.firstFramePresented || !this.resumed) {
            return;
        }
        int i = this.firstFrameAttempts;
        if (i >= 5) {
            Logger.warn("No first frame after 5 attempts — leaving it to the user");
            return;
        }
        final int i2 = i + 1;
        this.firstFrameAttempts = i2;
        handler.postDelayed(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleFirstFrameRetry$3(i2, webView, handler);
            }
        }, FIRST_FRAME_RETRY_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleFirstFrameRetry$3(int i, WebView webView, Handler handler) {
        if (this.firstFramePresented || !this.resumed || isFinishing() || isDestroyed()) {
            return;
        }
        Logger.warn("Still no first frame — cold-start retry " + i + "/5");
        View decorView = getWindow() != null ? getWindow().getDecorView() : null;
        if (decorView != null) {
            decorView.invalidate();
        }
        checkPresentation(webView, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean recoverFromDeadRenderer(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = lastRecoveryAt;
        if (j != 0 && jElapsedRealtime - j < 15000) {
            Logger.warn("Renderer gone again within the cooldown — leaving it to the platform");
            return false;
        }
        lastRecoveryAt = jElapsedRealtime;
        Logger.warn("WebView renderer gone (didCrash=" + renderProcessGoneDetail.didCrash() + ") — recreating the activity");
        try {
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(webView);
            }
            webView.destroy();
        } catch (Throwable unused) {
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: ai.mirofish.mirasim.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.recreate();
            }
        });
        return true;
    }
}
package com.xiaomi.fitness.login.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.fitness.banner.export.compare.ICompare;
import com.fitness.banner.export.compare.ICompareKt;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.account.region.RegionPreference;
import com.xiaomi.fitness.baseui.utils.ViewUiUtils;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.CheckerUserSettingManagerExtKt;
import com.xiaomi.fitness.login.export.PrivacyEasyModePreference;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.login.mode.GuidePersonalizeActivity;
import com.xiaomi.fitness.login.preference.ModePreference;
import com.xiaomi.fitness.login.privacy.PrivacyActivity;
import com.xiaomi.fitness.login.privacy.PrivacyViewModel;
import com.xiaomi.fitness.login.region.RegionSelectActivity;
import com.xiaomi.fitness.login.utils.RegionUtil;
import com.xiaomi.fitness.main.export.IntentRelay;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.fitness.main.export.MainTab;
import defpackage.fdk;
import defpackage.qgk;
import defpackage.v1g;
import defpackage.vwt;
import defpackage.y02;
import java.lang.ref.WeakReference;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001\u0012B\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u0019\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0014¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\bH\u0014¢\u0006\u0004\b\u000b\u0010\u0005R\u0014\u0010\u000f\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u0011\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000e¨\u0006\u0013"}, d2 = {"Lcom/xiaomi/fitness/login/welcome/WelcomeActivity;", "Lcom/xiaomi/fitness/baseui/view/BaseBindingActivity;", "Lcom/xiaomi/fitness/login/privacy/PrivacyViewModel;", "Lv1g;", "<init>", "()V", "Landroid/os/Bundle;", "savedInstanceState", "", "onCreate", "(Landroid/os/Bundle;)V", LifecycleConstantKt.ON_DESTROY, "", "getViewModelId", "()I", "viewModelId", "getLayoutId", "layoutId", "SafeRunnable", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WelcomeActivity extends Hilt_WelcomeActivity<PrivacyViewModel, v1g> {

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\t\u001a\u00020\nH\u0016R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Lcom/xiaomi/fitness/login/welcome/WelcomeActivity$SafeRunnable;", "Ljava/lang/Runnable;", "activityWeakRef", "Ljava/lang/ref/WeakReference;", "Lcom/xiaomi/fitness/login/welcome/WelcomeActivity;", "<init>", "(Ljava/lang/ref/WeakReference;)V", "getActivityWeakRef", "()Ljava/lang/ref/WeakReference;", "run", "", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class SafeRunnable implements Runnable {

        @NotNull
        private final WeakReference<WelcomeActivity> activityWeakRef;

        public SafeRunnable(@NotNull WeakReference<WelcomeActivity> activityWeakRef) {
            Intrinsics.checkNotNullParameter(activityWeakRef, "activityWeakRef");
            this.activityWeakRef = activityWeakRef;
        }

        @NotNull
        public final WeakReference<WelcomeActivity> getActivityWeakRef() {
            return this.activityWeakRef;
        }

        @Override // java.lang.Runnable
        public void run() {
            WelcomeActivity welcomeActivity = this.activityWeakRef.get();
            if (welcomeActivity == null || welcomeActivity.isDestroyed()) {
                return;
            }
            TextView textView = (TextView) welcomeActivity.findViewById(fdk.z0);
            Button button = (Button) welcomeActivity.findViewById(fdk.x0);
            int iMeasureText = ((int) textView.getPaint().measureText(textView.getText().toString())) + textView.getPaddingStart() + textView.getPaddingEnd();
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = iMeasureText;
            button.setLayoutParams(layoutParams2);
        }
    }

    /* JADX WARN: Code duplicated, block: B:29:0x00b9  */
    private static final void onCreate$lambda$3(WelcomeActivity welcomeActivity, View view) {
        ModePreference.INSTANCE.setWelcomeFinish(true);
        boolean privacy_agree = RegionPreference.INSTANCE.getPRIVACY_AGREE();
        boolean zIsEasyMode = PrivacyEasyModePreference.INSTANCE.isEasyMode();
        boolean z = RegionExtKt.getInstance(RegionManager.INSTANCE).getCurrentRegion().length() == 0;
        Bundle bundleForward = IntentRelay.INSTANCE.forward(welcomeActivity.getIntent());
        if (!privacy_agree && !zIsEasyMode) {
            Intent intent = new Intent(welcomeActivity, (Class<?>) PrivacyActivity.class);
            intent.putExtras(bundleForward);
            welcomeActivity.startActivity(intent);
        } else if (zIsEasyMode) {
            Bundle bundle = new Bundle();
            bundle.putInt("key_tab", MainTab.INSTANCE.getINDEX_SPORT());
            bundle.putAll(bundleForward);
            MainExtKt.showMainActivity(welcomeActivity, null, bundle);
        } else if (z) {
            Intent intent2 = new Intent(welcomeActivity, (Class<?>) RegionSelectActivity.class);
            intent2.putExtras(bundleForward);
            welcomeActivity.startActivity(intent2);
        } else {
            AppUtil appUtil = AppUtil.INSTANCE;
            if (appUtil.isPlayChannel() && RegionUtil.isInnerCN() && AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                RegionExtKt.mainLandNotesSetup(welcomeActivity);
            } else if (appUtil.isPlayChannel()) {
                MainExtKt.showMainActivity(welcomeActivity, null, bundleForward);
            } else {
                CheckerUserSettingManager.Companion companion = CheckerUserSettingManager.INSTANCE;
                if (CheckerUserSettingManagerExtKt.getInstance(companion).getSelectMode() != companion.getUNKOWN_MODE() || ICompareKt.getINSTANCE(ICompare.INSTANCE).showAppV4Enable()) {
                    MainExtKt.showMainActivity(welcomeActivity, null, bundleForward);
                } else {
                    Intent intent3 = new Intent(welcomeActivity, (Class<?>) GuidePersonalizeActivity.class);
                    intent3.putExtras(bundleForward);
                    welcomeActivity.startActivity(intent3);
                }
            }
        }
        welcomeActivity.finish();
    }

    public static /* synthetic */ void t(WelcomeActivity welcomeActivity, View view) {
        onCreate$lambda$3(welcomeActivity, view);
    }

    @Override // com.xiaomi.fitness.baseui.ViewCreator
    /* JADX INFO: renamed from: getLayoutId */
    public int getLayoutRes() {
        return qgk.f;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public int getViewModelId() {
        return y02.a;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.login.welcome.Hilt_WelcomeActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUiUtils.fitFullScreen$default((Activity) this, (List) null, (List) null, (List) null, false, true, 15, (Object) null);
        setActionBarDisplayable(false);
        ((TextView) findViewById(fdk.z0)).post(new SafeRunnable(new WeakReference(this)));
        ((v1g) getMBinding()).N.setOnClickListener(new vwt(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.login.welcome.Hilt_WelcomeActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        ((v1g) getMBinding()).getRoot().removeCallbacks(null);
        super.onDestroy();
    }
}
package com.xiaomi.fitness.login.privacy;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.statistics.export.IOnetrack;
import com.example.statistics.export.OneTrackExtKt;
import com.fitness.banner.export.compare.ICompare;
import com.fitness.banner.export.compare.ICompareKt;
import com.xiaomi.accounts.Manifest;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.account.region.RegionPreference;
import com.xiaomi.fitness.baseui.dialog.CommonDialog;
import com.xiaomi.fitness.baseui.dialog.DialogParams;
import com.xiaomi.fitness.common.app.AppManager;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.LinkSpanHelper;
import com.xiaomi.fitness.common.extensions.PermissionBuilder;
import com.xiaomi.fitness.common.extensions.PermissionExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.LocaleUtil;
import com.xiaomi.fitness.common.utils.RomUtils;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.PrivacyEasyModePreference;
import com.xiaomi.fitness.login.export.PrivacyExtKt;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.login.mode.GuidePersonalizeActivity;
import com.xiaomi.fitness.login.preference.PrivacyPreference;
import com.xiaomi.fitness.login.region.RegionSelectActivity;
import com.xiaomi.fitness.login.util.LoginUtil;
import com.xiaomi.fitness.login.util.PrivacyUrlsKt;
import com.xiaomi.fitness.login.util.hyperlink.a;
import com.xiaomi.fitness.login.utils.RegionUtil;
import com.xiaomi.fitness.main.export.IntentRelay;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.fitness.main.export.MainTab;
import com.xiaomi.fitness.res.preload.export.PreloadManagerExtKt;
import com.xiaomi.fitness.res.preload.export.ResPreloadManager;
import com.xiaomi.fitness.webview.WebViewUtilKt;
import com.xiaomi.smarthome.frame.server_compact.ServerConstants;
import defpackage.cuj;
import defpackage.duj;
import defpackage.euj;
import defpackage.fuj;
import defpackage.guj;
import defpackage.huj;
import defpackage.iuj;
import defpackage.juj;
import defpackage.kuj;
import defpackage.luj;
import defpackage.p1g;
import defpackage.qgk;
import defpackage.sj7;
import defpackage.tmk;
import defpackage.tn4;
import defpackage.y02;
import defpackage.z4k;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Route(path = PrivacyExtKt.PRIVACY_PATH)
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004B\u0007¢\u0006\u0004\b\u0005\u0010\u0006J\u000f\u0010\b\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\u000b\u0010\u0006J\u000f\u0010\f\u001a\u00020\u0007H\u0002¢\u0006\u0004\b\f\u0010\tJ!\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\r2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u000f\u0010\u0013\u001a\u00020\nH\u0002¢\u0006\u0004\b\u0013\u0010\u0006J\u000f\u0010\u0015\u001a\u00020\u0014H\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\nH\u0002¢\u0006\u0004\b\u0017\u0010\u0006J\u000f\u0010\u0018\u001a\u00020\nH\u0002¢\u0006\u0004\b\u0018\u0010\u0006J\u000f\u0010\u0019\u001a\u00020\nH\u0002¢\u0006\u0004\b\u0019\u0010\u0006J\u0019\u0010\u001c\u001a\u00020\n2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0014¢\u0006\u0004\b\u001c\u0010\u001dJ!\u0010!\u001a\u00020\n2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001e2\u0006\u0010 \u001a\u00020\u0007H\u0016¢\u0006\u0004\b!\u0010\"J\u000f\u0010#\u001a\u00020\nH\u0014¢\u0006\u0004\b#\u0010\u0006R\"\u0010%\u001a\u00020$8\u0006@\u0006X\u0086.¢\u0006\u0012\n\u0004\b%\u0010&\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010+\u001a\u00020\u00148\u0006X\u0086D¢\u0006\f\n\u0004\b+\u0010,\u001a\u0004\b-\u0010\u0016R\u001a\u00100\u001a\b\u0012\u0004\u0012\u00020/0.8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b0\u00101R\u0016\u00102\u001a\u00020\u00078\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b2\u00103R\"\u00106\u001a\u0010\u0012\f\u0012\n 5*\u0004\u0018\u00010\u000f0\u000f048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b6\u00107R\u0014\u0010:\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b8\u00109R\u0014\u0010<\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b;\u00109¨\u0006="}, d2 = {"Lcom/xiaomi/fitness/login/privacy/PrivacyActivity;", "Lcom/xiaomi/fitness/baseui/view/BaseBindingActivity;", "Lcom/xiaomi/fitness/login/privacy/PrivacyViewModel;", "Lp1g;", "Landroid/widget/CompoundButton$OnCheckedChangeListener;", "<init>", "()V", "", "showKrPrivacyEnable", "()Z", "", "dealEnterEasyModeAction", "showCnPrivacyEnable", "", "resultCode", "Landroid/content/Intent;", "data", "dealActivityResult", "(ILandroid/content/Intent;)V", "agreeClick", "", "getPrivacyDialogText", "()Ljava/lang/String;", "showPrivacyDialog", "handlePrivacyAgree", "clearSpan", "Landroid/os/Bundle;", "savedInstanceState", "onCreate", "(Landroid/os/Bundle;)V", "Landroid/widget/CompoundButton;", "buttonView", "isChecked", "onCheckedChanged", "(Landroid/widget/CompoundButton;Z)V", LifecycleConstantKt.ON_DESTROY, "Landroid/content/Context;", "mContext", "Landroid/content/Context;", "getMContext", "()Landroid/content/Context;", "setMContext", "(Landroid/content/Context;)V", "TAG", "Ljava/lang/String;", "getTAG", "", "Landroid/widget/TextView;", "clickableViewList", "Ljava/util/List;", "privacyAgreeHasChecked", "Z", "Landroidx/activity/result/ActivityResultLauncher;", "kotlin.jvm.PlatformType", "activityForResult", "Landroidx/activity/result/ActivityResultLauncher;", "getViewModelId", "()I", "viewModelId", "getLayoutId", "layoutId", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nPrivacyActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PrivacyActivity.kt\ncom/xiaomi/fitness/login/privacy/PrivacyActivity\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 SpannableString.kt\nandroidx/core/text/SpannableStringKt\n+ 4 SpannedString.kt\nandroidx/core/text/SpannedStringKt\n+ 5 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,436:1\n1869#2:437\n1870#2:445\n32#3:438\n31#4,4:439\n13579#5,2:443\n*S KotlinDebug\n*F\n+ 1 PrivacyActivity.kt\ncom/xiaomi/fitness/login/privacy/PrivacyActivity\n*L\n425#1:437\n425#1:445\n428#1:438\n428#1:439,4\n428#1:443,2\n*E\n"})
public final class PrivacyActivity extends Hilt_PrivacyActivity<PrivacyViewModel, p1g> implements CompoundButton.OnCheckedChangeListener {

    @NotNull
    private final ActivityResultLauncher<Intent> activityForResult;
    public Context mContext;
    private boolean privacyAgreeHasChecked;

    @NotNull
    private final String TAG = "PrivacyActivity";

    @NotNull
    private final List<TextView> clickableViewList = new ArrayList();

    /* JADX INFO: renamed from: com.xiaomi.fitness.login.privacy.PrivacyActivity$showPrivacyDialog$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/login/privacy/PrivacyActivity$showPrivacyDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass1 extends sj7 {
        public AnonymousClass1() {
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            if (which == -2) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            } else {
                if (which != -1) {
                    return;
                }
                PrivacyActivity.access$handlePrivacyAgree(PrivacyActivity.this);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    }

    public PrivacyActivity() {
        ActivityResultLauncher<Intent> activityResultLauncherRegisterForActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new luj(this));
        Intrinsics.checkNotNullExpressionValue(activityResultLauncherRegisterForActivityResult, "registerForActivityResult(...)");
        this.activityForResult = activityResultLauncherRegisterForActivityResult;
    }

    public static /* synthetic */ void A(View view) {
        onCreate$lambda$8(view);
    }

    public static /* synthetic */ Unit B(PrivacyActivity privacyActivity, String str) {
        return onCreate$lambda$6$lambda$5$lambda$4(privacyActivity, str);
    }

    public static /* synthetic */ void C(View view) {
        onCreate$lambda$1(view);
    }

    public static /* synthetic */ void D(View view) {
        onCreate$lambda$7(view);
    }

    public static final /* synthetic */ void access$handlePrivacyAgree(PrivacyActivity privacyActivity) {
        privacyActivity.handlePrivacyAgree();
    }

    private static final void activityForResult$lambda$0(PrivacyActivity privacyActivity, ActivityResult activityResult) {
        privacyActivity.dealActivityResult(activityResult.getResultCode(), activityResult.getData());
    }

    private final void agreeClick() {
        if (this.privacyAgreeHasChecked) {
            handlePrivacyAgree();
        } else {
            showPrivacyDialog();
        }
    }

    private final void clearSpan() {
        for (TextView textView : this.clickableViewList) {
            CharSequence text = textView.getText();
            if (text instanceof Spannable) {
                Spannable spannable = (Spannable) text;
                for (Object obj : spannable.getSpans(0, spannable.length(), Object.class)) {
                    spannable.removeSpan(obj);
                }
                textView.setMovementMethod(null);
                textView.setText(" ");
            }
        }
        this.clickableViewList.clear();
    }

    private final void dealActivityResult(int resultCode, Intent data) {
        Logger.i(this.TAG, "dealActivityResult resultCode " + resultCode + ";data " + data);
        if (resultCode == 100) {
            dealEnterEasyModeAction();
        } else {
            if (resultCode != 101) {
                return;
            }
            AppManager.appExit$default(AppManager.INSTANCE.getInstance(), false, 1, null);
        }
    }

    private final void dealEnterEasyModeAction() {
        PrivacyEasyModePreference.INSTANCE.setEasyMode(true);
        RegionManager.Companion companion = RegionManager.INSTANCE;
        RegionExtKt.getInstance(companion).setCurrentRegion("cn");
        RegionExtKt.getInstance(companion).setLocaleCountry(ServerConstants.NEW_CN_COUNTRY_CODE);
        RegionExtKt.getInstance(companion).setCurrentCountry("中国大陆");
        Bundle bundle = new Bundle();
        bundle.putInt("key_tab", MainTab.INSTANCE.getINDEX_SPORT());
        bundle.putAll(IntentRelay.INSTANCE.forward(getIntent()));
        MainExtKt.showMainActivity(this, null, bundle);
        finish();
    }

    private final String getPrivacyDialogText() {
        if (showCnPrivacyEnable()) {
            Resources resources = getResources();
            int i = tmk.u;
            LocaleUtil localeUtil = LocaleUtil.INSTANCE;
            String string = resources.getString(i, PrivacyUrlsKt.getWatchUserLicenceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"), PrivacyUrlsKt.getWatchAgreementHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"), PrivacyUrlsKt.getAppPrivacyBriefUrl(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"));
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            return string;
        }
        Resources resources2 = getResources();
        int i2 = tmk.t;
        LocaleUtil localeUtil2 = LocaleUtil.INSTANCE;
        String string2 = resources2.getString(i2, PrivacyUrlsKt.getWatchUserLicenceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil2, false, false, 3, null), "app"), PrivacyUrlsKt.getWatchAgreementHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil2, false, false, 3, null), "app"));
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        return string2;
    }

    private final void handlePrivacyAgree() {
        String currentRegion;
        PrivacyEasyModePreference privacyEasyModePreference = PrivacyEasyModePreference.INSTANCE;
        boolean zIsEasyMode = privacyEasyModePreference.isEasyMode();
        PrivacyPreference privacyPreference = PrivacyPreference.INSTANCE;
        privacyPreference.setPrivacyAgreeTime(System.currentTimeMillis());
        privacyPreference.setPrivacyAgreeTag(true);
        privacyEasyModePreference.setEasyMode(false);
        privacyPreference.setNeedReportAgreeTag(true);
        Bundle bundleForward = IntentRelay.INSTANCE.forward(getIntent());
        if (zIsEasyMode || (currentRegion = RegionExtKt.getInstance(RegionManager.INSTANCE).getCurrentRegion()) == null || currentRegion.length() == 0) {
            Intent intent = new Intent(this, (Class<?>) RegionSelectActivity.class);
            intent.putExtras(bundleForward);
            startActivity(intent);
        } else {
            AppUtil appUtil = AppUtil.INSTANCE;
            if (appUtil.isPlayChannel() && RegionUtil.isInnerCN() && AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                RegionExtKt.mainLandNotesSetup(this);
            } else if (appUtil.isPlayChannel() || LoginUtil.INSTANCE.checkDeviceSettingForSelectMode() != CheckerUserSettingManager.INSTANCE.getUNKOWN_MODE() || ICompareKt.getINSTANCE(ICompare.INSTANCE).showAppV4Enable()) {
                Application application = getApplication();
                Intrinsics.checkNotNullExpressionValue(application, "getApplication(...)");
                MainExtKt.showMainActivity(application, null, bundleForward);
            } else {
                Intent intent2 = new Intent(this, (Class<?>) GuidePersonalizeActivity.class);
                intent2.putExtras(bundleForward);
                startActivity(intent2);
            }
        }
        PreloadManagerExtKt.getInstance(ResPreloadManager.INSTANCE).preloadIfNeed();
        finish();
    }

    private static final void onCreate$lambda$1(View view) {
        AppManager.appExit$default(AppManager.INSTANCE.getInstance(), false, 1, null);
    }

    private static final void onCreate$lambda$2(boolean z, PrivacyActivity privacyActivity, View view) {
        if (!z) {
            privacyActivity.finish();
        } else {
            privacyActivity.activityForResult.launch(new Intent(privacyActivity, (Class<?>) EasyModeDetailActivity.class));
        }
    }

    private static final void onCreate$lambda$6(PrivacyActivity privacyActivity, View view) {
        Logger.d(privacyActivity.TAG, "privace agree");
        boolean zIsPermissionGranted = PermissionExtKt.isPermissionGranted(privacyActivity, Manifest.permission.GET_ACCOUNTS);
        Logger.d(privacyActivity.TAG, "isPermissionGranted(Manifest.permission.GET_ACCOUNTS) " + zIsPermissionGranted);
        if (!AppUtil.INSTANCE.isPlayChannel() || Build.VERSION.SDK_INT > 26 || !RomUtils.INSTANCE.isMIUIRom(privacyActivity) || zIsPermissionGranted) {
            privacyActivity.agreeClick();
        } else {
            PermissionExtKt.permission$default(privacyActivity, Manifest.permission.GET_ACCOUNTS, (String) null, new cuj(privacyActivity), 2, (Object) null);
        }
    }

    private static final Unit onCreate$lambda$6$lambda$5(PrivacyActivity privacyActivity, PermissionBuilder permission) {
        Intrinsics.checkNotNullParameter(permission, "$this$permission");
        permission.denied(new juj());
        permission.granted(new kuj(privacyActivity));
        return Unit.INSTANCE;
    }

    private static final Unit onCreate$lambda$6$lambda$5$lambda$3(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AppManager.INSTANCE.getInstance().appExit(false);
        return Unit.INSTANCE;
    }

    private static final Unit onCreate$lambda$6$lambda$5$lambda$4(PrivacyActivity privacyActivity, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        privacyActivity.agreeClick();
        return Unit.INSTANCE;
    }

    private static final void onCreate$lambda$7(View view) {
        RegionPreference regionPreference = RegionPreference.INSTANCE;
        regionPreference.setPREF_PRIVACY_EXPERIENCE_TAG(view.isClickable());
        OneTrackExtKt.getInstance(IOnetrack.INSTANCE).setPrivacyExperienceAccepted(regionPreference.getPREF_PRIVACY_EXPERIENCE_TAG());
    }

    private static final void onCreate$lambda$8(View view) {
        RegionPreference.INSTANCE.setPREF_PRIVACY_EXPERIENCE_TAG(view.isClickable());
    }

    private final boolean showCnPrivacyEnable() {
        String currentRegion = RegionExtKt.getInstance(RegionManager.INSTANCE).getCurrentRegion();
        Logger.i(this.TAG, "app currentRegion:" + currentRegion);
        if (AppUtil.INSTANCE.isPlayChannel()) {
            return false;
        }
        LocaleUtil localeUtil = LocaleUtil.INSTANCE;
        if (localeUtil.isSameCountry(ServerConstants.NEW_CN_COUNTRY_CODE) || localeUtil.isSameCountry(ServerConstants.NEW_TW_COUNTRY_CODE)) {
            return currentRegion.length() == 0 || StringsKt.equals(currentRegion, "cn", true);
        }
        return false;
    }

    private final boolean showKrPrivacyEnable() {
        LocaleUtil localeUtil = LocaleUtil.INSTANCE;
        if (localeUtil.isSameCountry("kr")) {
            return true;
        }
        String language = Locale.KOREA.getLanguage();
        Intrinsics.checkNotNullExpressionValue(language, "getLanguage(...)");
        return localeUtil.isSameLanguage(language);
    }

    private final void showPrivacyDialog() {
        new CommonDialog.c("privacy_sure_dialog").setDialogTitle(tmk.v).setDialogDescriptionString(new DialogParams.DialogDescriptionString(LinkSpanHelper.a(this, getPrivacyDialogText(), Integer.valueOf(z4k.c), new duj()))).setPositiveText(tmk.d).setNegativeText(tmk.a).setCanceledOnBackPressed(false).create().addDialogCallback(new AnonymousClass1()).showIfNeed(getSupportFragmentManager());
    }

    private static final void showPrivacyDialog$lambda$9(View view, String str) {
        Intrinsics.checkNotNull(str);
        WebViewUtilKt.startWebView$default(str, null, false, false, null, false, null, false, 254, null);
    }

    public static /* synthetic */ Unit t(String str) {
        return onCreate$lambda$6$lambda$5$lambda$3(str);
    }

    public static /* synthetic */ void u(boolean z, PrivacyActivity privacyActivity, View view) {
        onCreate$lambda$2(z, privacyActivity, view);
    }

    public static /* synthetic */ void v(View view, String str) {
        showPrivacyDialog$lambda$9(view, str);
    }

    public static /* synthetic */ void w(PrivacyActivity privacyActivity, ActivityResult activityResult) {
        activityForResult$lambda$0(privacyActivity, activityResult);
    }

    public static /* synthetic */ Unit x(PrivacyActivity privacyActivity, PermissionBuilder permissionBuilder) {
        return onCreate$lambda$6$lambda$5(privacyActivity, permissionBuilder);
    }

    public static /* synthetic */ void y(PrivacyActivity privacyActivity, View view) {
        onCreate$lambda$6(privacyActivity, view);
    }

    @Override // com.xiaomi.fitness.baseui.ViewCreator
    /* JADX INFO: renamed from: getLayoutId */
    public int getLayoutRes() {
        return qgk.c;
    }

    @NotNull
    public final Context getMContext() {
        Context context = this.mContext;
        if (context != null) {
            return context;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mContext");
        return null;
    }

    @NotNull
    public final String getTAG() {
        return this.TAG;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public int getViewModelId() {
        return y02.a;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(@Nullable CompoundButton buttonView, boolean isChecked) {
        if (!showKrPrivacyEnable()) {
            this.privacyAgreeHasChecked = ((p1g) getMBinding()).S.isChecked();
            return;
        }
        if (buttonView != null && buttonView.getId() == ((p1g) getMBinding()).A.getId()) {
            tn4.h(((p1g) getMBinding()).D, isChecked, this);
            tn4.h(((p1g) getMBinding()).F, isChecked, this);
            tn4.h(((p1g) getMBinding()).B, isChecked, this);
            tn4.h(((p1g) getMBinding()).M, isChecked, this);
            this.privacyAgreeHasChecked = isChecked;
            return;
        }
        if (!isChecked) {
            if (!((p1g) getMBinding()).D.isChecked() || !((p1g) getMBinding()).B.isChecked()) {
                this.privacyAgreeHasChecked = false;
            }
            tn4.h(((p1g) getMBinding()).A, false, this);
            return;
        }
        if (!((p1g) getMBinding()).D.isChecked() || !((p1g) getMBinding()).B.isChecked()) {
            this.privacyAgreeHasChecked = false;
            return;
        }
        if (((p1g) getMBinding()).F.isChecked() && ((p1g) getMBinding()).M.isChecked()) {
            tn4.h(((p1g) getMBinding()).A, true, this);
        }
        this.privacyAgreeHasChecked = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.login.privacy.Hilt_PrivacyActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarDisplayable(false);
        setMContext(this);
        boolean zShowCnPrivacyEnable = showCnPrivacyEnable();
        String str = this.TAG;
        Locale locale = Locale.getDefault();
        LocaleUtil localeUtil = LocaleUtil.INSTANCE;
        Logger.i(str, "phone defaultRegion:" + locale + ";current " + LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null) + ";showCnPrivacyEnable:" + zShowCnPrivacyEnable);
        if (showKrPrivacyEnable()) {
            ((p1g) getMBinding()).Z.setText(tmk.x);
            ((p1g) getMBinding()).Y.setText(tmk.J);
            String string = getResources().getString(tmk.G, PrivacyUrlsKt.getWatchUserLicenceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"));
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            CharSequence charSequenceB = a.b(string);
            ((p1g) getMBinding()).E.setText(charSequenceB);
            ((p1g) getMBinding()).E.setContentDescription(charSequenceB.toString());
            ((p1g) getMBinding()).E.setMovementMethod(LinkMovementMethod.getInstance());
            List<TextView> list = this.clickableViewList;
            TextView agreeMiwearPolicyTv = ((p1g) getMBinding()).E;
            Intrinsics.checkNotNullExpressionValue(agreeMiwearPolicyTv, "agreeMiwearPolicyTv");
            list.add(agreeMiwearPolicyTv);
            String string2 = getResources().getString(tmk.F, PrivacyUrlsKt.getPrivacyUserInfoHtmlUrl(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app_v3"));
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            CharSequence charSequenceB2 = a.b(string2);
            ((p1g) getMBinding()).C.setText(charSequenceB2);
            ((p1g) getMBinding()).C.setContentDescription(charSequenceB2.toString());
            ((p1g) getMBinding()).C.setMovementMethod(LinkMovementMethod.getInstance());
            List<TextView> list2 = this.clickableViewList;
            TextView agreeCollectUserInfoTv = ((p1g) getMBinding()).C;
            Intrinsics.checkNotNullExpressionValue(agreeCollectUserInfoTv, "agreeCollectUserInfoTv");
            list2.add(agreeCollectUserInfoTv);
            String string3 = getResources().getString(tmk.E, PrivacyUrlsKt.getPrivacyLocationHtmlUrl(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app_v3"));
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            CharSequence charSequenceB3 = a.b(string3);
            ((p1g) getMBinding()).G.setText(charSequenceB3);
            ((p1g) getMBinding()).G.setContentDescription(charSequenceB3.toString());
            ((p1g) getMBinding()).G.setMovementMethod(LinkMovementMethod.getInstance());
            List<TextView> list3 = this.clickableViewList;
            TextView agreeUseLocationInfoTv = ((p1g) getMBinding()).G;
            Intrinsics.checkNotNullExpressionValue(agreeUseLocationInfoTv, "agreeUseLocationInfoTv");
            list3.add(agreeUseLocationInfoTv);
            String string4 = getResources().getString(tmk.f14497r, PrivacyUrlsKt.getWatchExperienceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"));
            Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
            CharSequence charSequenceB4 = a.b(string4);
            ((p1g) getMBinding()).X.setText(charSequenceB4);
            ((p1g) getMBinding()).X.setContentDescription(charSequenceB4.toString());
            ((p1g) getMBinding()).X.setMovementMethod(LinkMovementMethod.getInstance());
            List<TextView> list4 = this.clickableViewList;
            TextView privacyExperienceTvKr = ((p1g) getMBinding()).X;
            Intrinsics.checkNotNullExpressionValue(privacyExperienceTvKr, "privacyExperienceTvKr");
            list4.add(privacyExperienceTvKr);
            ((p1g) getMBinding()).F.setOnCheckedChangeListener(this);
            ((p1g) getMBinding()).D.setOnCheckedChangeListener(this);
            ((p1g) getMBinding()).B.setOnCheckedChangeListener(this);
            ((p1g) getMBinding()).A.setOnCheckedChangeListener(this);
            ((p1g) getMBinding()).J.setVisibility(8);
            ((p1g) getMBinding()).T.setVisibility(8);
            ((p1g) getMBinding()).U.setVisibility(8);
            ((p1g) getMBinding()).b0.setVisibility(8);
            ((p1g) getMBinding()).H.setVisibility(8);
            ((p1g) getMBinding()).N.setVisibility(8);
            ((p1g) getMBinding()).I.setVisibility(8);
        } else {
            String string5 = !zShowCnPrivacyEnable ? getResources().getString(tmk.s, PrivacyUrlsKt.getWatchUserLicenceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"), PrivacyUrlsKt.getWatchAgreementHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app")) : getResources().getString(tmk.w, PrivacyUrlsKt.getWatchUserLicenceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"), PrivacyUrlsKt.getWatchAgreementHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"), PrivacyUrlsKt.getAppPrivacyBriefUrl(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"));
            Intrinsics.checkNotNull(string5);
            CharSequence charSequenceB5 = a.b(string5);
            ((p1g) getMBinding()).P.setText(charSequenceB5);
            ((p1g) getMBinding()).P.setContentDescription(charSequenceB5.toString());
            ((p1g) getMBinding()).P.setMovementMethod(LinkMovementMethod.getInstance());
            List<TextView> list5 = this.clickableViewList;
            TextView privacyAgreeActionTv = ((p1g) getMBinding()).P;
            Intrinsics.checkNotNullExpressionValue(privacyAgreeActionTv, "privacyAgreeActionTv");
            list5.add(privacyAgreeActionTv);
            ((p1g) getMBinding()).Z.setText(tmk.K);
            ((p1g) getMBinding()).Y.setText(tmk.I);
            ((p1g) getMBinding()).R.setText(tmk.d);
            ((p1g) getMBinding()).S.setOnCheckedChangeListener(this);
            ((p1g) getMBinding()).C.setVisibility(8);
            ((p1g) getMBinding()).E.setVisibility(8);
            ((p1g) getMBinding()).G.setVisibility(8);
            ((p1g) getMBinding()).F.setVisibility(8);
            ((p1g) getMBinding()).D.setVisibility(8);
            ((p1g) getMBinding()).B.setVisibility(8);
            ((p1g) getMBinding()).A.setVisibility(8);
            ((p1g) getMBinding()).Q.setVisibility(8);
            ((p1g) getMBinding()).K.setVisibility(8);
            if (AppUtil.INSTANCE.isPlayChannel()) {
                ((p1g) getMBinding()).H.setVisibility(8);
            }
            if (zShowCnPrivacyEnable) {
                ((p1g) getMBinding()).H.setVisibility(8);
                ((p1g) getMBinding()).N.setVisibility(8);
                ((p1g) getMBinding()).I.setVisibility(8);
                ((p1g) getMBinding()).J.setVisibility(8);
                ((p1g) getMBinding()).O.setVisibility(0);
                ((p1g) getMBinding()).R.setText(getString(tmk.z));
                ((p1g) getMBinding()).V.setText(getString(tmk.y));
                ((p1g) getMBinding()).Y.setText(getString(tmk.H));
                ((p1g) getMBinding()).U.setVisibility(8);
                ((p1g) getMBinding()).a0.setVisibility(0);
                ((p1g) getMBinding()).a0.setText(getString(tmk.D));
                ((p1g) getMBinding()).a0.setOnClickListener(new euj());
            }
        }
        ((p1g) getMBinding()).R.setEnabled(true);
        this.privacyAgreeHasChecked = false;
        ((p1g) getMBinding()).R.setTextColor(getResources().getColor(z4k.f));
        String string6 = getResources().getString(tmk.q, PrivacyUrlsKt.getWatchExperienceHtmlUrlOnFirst(LocaleUtil.getCurrentLocale$default(localeUtil, false, false, 3, null), "app"));
        Intrinsics.checkNotNullExpressionValue(string6, "getString(...)");
        CharSequence charSequenceB6 = a.b(string6);
        ((p1g) getMBinding()).W.setText(charSequenceB6);
        ((p1g) getMBinding()).W.setContentDescription(charSequenceB6.toString());
        ((p1g) getMBinding()).W.setMovementMethod(LinkMovementMethod.getInstance());
        List<TextView> list6 = this.clickableViewList;
        TextView privacyExperienceTv = ((p1g) getMBinding()).W;
        Intrinsics.checkNotNullExpressionValue(privacyExperienceTv, "privacyExperienceTv");
        list6.add(privacyExperienceTv);
        ((p1g) getMBinding()).V.setOnClickListener(new fuj(zShowCnPrivacyEnable, this));
        ((p1g) getMBinding()).R.setOnClickListener(new guj(this));
        ((p1g) getMBinding()).L.setOnClickListener(new huj());
        ((p1g) getMBinding()).M.setOnClickListener(new iuj());
    }

    @Override // com.xiaomi.fitness.login.privacy.Hilt_PrivacyActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        clearSpan();
        super.onDestroy();
    }

    public final void setMContext(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        this.mContext = context;
    }
}
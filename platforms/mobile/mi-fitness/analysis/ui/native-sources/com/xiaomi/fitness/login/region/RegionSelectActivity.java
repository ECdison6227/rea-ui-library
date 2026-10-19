package com.xiaomi.fitness.login.region;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fitness.banner.export.compare.ICompare;
import com.fitness.banner.export.compare.ICompareKt;
import com.xiaomi.fitness.account.api.bean.CountryInfo;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.common.extensions.ToastExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.LocaleUtil;
import com.xiaomi.fitness.common.utils.ToastUtil;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.CheckerUserSettingManagerExtKt;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.login.mode.GuidePersonalizeActivity;
import com.xiaomi.fitness.login.util.LoginUtil;
import com.xiaomi.fitness.login.util.PrivacyUrlsKt;
import com.xiaomi.fitness.login.utils.RegionUtil;
import com.xiaomi.fitness.main.export.IntentRelay;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.smarthome.frame.server_compact.ServerConstants;
import defpackage.ezl;
import defpackage.fzl;
import defpackage.g2g;
import defpackage.gzl;
import defpackage.hzl;
import defpackage.izl;
import defpackage.qgk;
import defpackage.tmk;
import defpackage.y02;
import defpackage.z4k;
import java.lang.ref.WeakReference;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Route(path = RegionExtKt.REGION_SETUP_ACTIVITY_PATH)
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u0000 22\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u00012B\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u0019\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0014¢\u0006\u0004\b\t\u0010\nJ\u0017\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\f\u0010\nJ\u0017\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\r\u0010\nJ\r\u0010\u000e\u001a\u00020\b¢\u0006\u0004\b\u000e\u0010\u0005J\u0015\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0017\u0010\u0015\u001a\u00020\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013¢\u0006\u0004\b\u0015\u0010\u0016J\u0017\u0010\u0018\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0013¢\u0006\u0004\b\u0018\u0010\u0016J\r\u0010\u0019\u001a\u00020\b¢\u0006\u0004\b\u0019\u0010\u0005R\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u001a8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0018\u0010\u001d\u001a\u0004\u0018\u00010\u001a8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010\u001cR\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u001a8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001e\u0010\u001cR\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u00138\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001f\u0010 R\u0014\u0010!\u001a\u00020\u001a8\u0002X\u0082D¢\u0006\u0006\n\u0004\b!\u0010\u001cRF\u0010)\u001a4\u0012\u0013\u0012\u00110#¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(&\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b((\u0012\u0004\u0012\u00020\b0\"8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b)\u0010*R\u0014\u0010-\u001a\u00020#8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b+\u0010,R\u0014\u0010/\u001a\u00020#8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010,R\u0014\u00100\u001a\u00020\u000f8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b0\u00101¨\u00063"}, d2 = {"Lcom/xiaomi/fitness/login/region/RegionSelectActivity;", "Lcom/xiaomi/fitness/baseui/view/BaseBindingActivity;", "Lcom/xiaomi/fitness/login/region/RegionViewModel;", "Lg2g;", "<init>", "()V", "Landroid/os/Bundle;", "savedInstanceState", "", "onCreate", "(Landroid/os/Bundle;)V", "outState", "saveInstanceState", "restoreInstanceState", "loadCountryInfoByIP", "", "enable", "updateNextButton", "(Z)V", "Lcom/xiaomi/fitness/account/api/bean/CountryInfo;", "it", "updateCountryInfo", "(Lcom/xiaomi/fitness/account/api/bean/CountryInfo;)V", "countryItem", "showCountryName", "setRegionFromCurrentCountry", "", "region", "Ljava/lang/String;", "country", "currentCountry", "currentCountryItem", "Lcom/xiaomi/fitness/account/api/bean/CountryInfo;", "TAG", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "resultCode", "Landroid/content/Intent;", "data", "resultCallback", "Lkotlin/jvm/functions/Function2;", "getViewModelId", "()I", "viewModelId", "getLayoutId", "layoutId", "isAdaptActionBar", "()Z", "Companion", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nRegionSelectActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RegionSelectActivity.kt\ncom/xiaomi/fitness/login/region/RegionSelectActivity\n+ 2 Uri.kt\nandroidx/core/net/UriKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,243:1\n29#2:244\n1#3:245\n*S KotlinDebug\n*F\n+ 1 RegionSelectActivity.kt\ncom/xiaomi/fitness/login/region/RegionSelectActivity\n*L\n92#1:244\n*E\n"})
public final class RegionSelectActivity extends Hilt_RegionSelectActivity<RegionViewModel, g2g> {

    @NotNull
    public static final String KEY_REQUEST_RESULT = "request_result";

    @Nullable
    private String country;

    @Nullable
    private String currentCountry;

    @Nullable
    private CountryInfo currentCountryItem;

    @Nullable
    private String region;

    @NotNull
    private final String TAG = "RegionSelectActivity";

    @NotNull
    private final Function2<Integer, Intent, Unit> resultCallback = new ezl(this);

    private static final void onCreate$lambda$1(RegionSelectActivity regionSelectActivity, View view) {
        regionSelectActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(PrivacyUrlsKt.getMainLandIntroUrl(LocaleUtil.getCurrentLocale$default(LocaleUtil.INSTANCE, false, false, 3, null)))));
    }

    private static final Unit onCreate$lambda$2(RegionSelectActivity regionSelectActivity, CountryInfo countryInfo) {
        regionSelectActivity.updateCountryInfo(countryInfo);
        return Unit.INSTANCE;
    }

    private static final void onCreate$lambda$3(RegionSelectActivity regionSelectActivity, View view) {
        Intent intent = new Intent();
        intent.putExtra(RegionExtKt.GUID_SHOW, true);
        intent.putExtra(RegionSelectListActivity.CURRENT_COUNTRY_ITEM, regionSelectActivity.currentCountryItem);
        intent.setClass(regionSelectActivity, RegionSelectListActivity.class);
        regionSelectActivity.startForActivityResult(intent, regionSelectActivity.resultCallback);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final void onCreate$lambda$9(RegionSelectActivity regionSelectActivity, boolean z, View view) {
        if (regionSelectActivity.currentCountryItem == null) {
            ToastUtil.INSTANCE.showToast(tmk.g);
            return;
        }
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            regionSelectActivity.setRegionFromCurrentCountry();
        } else {
            CountryInfo countryInfo = regionSelectActivity.currentCountryItem;
            Intrinsics.checkNotNull(countryInfo);
            if (StringsKt.equals$default(countryInfo.getName(), ServerConstants.NEW_CN_COUNTRY_CODE, false, 2, null)) {
                String str = regionSelectActivity.region;
                if (str != null) {
                    RegionExtKt.getInstance(RegionManager.INSTANCE).switchRegion(str);
                }
                String str2 = regionSelectActivity.region;
                if (str2 != null) {
                    CountryInfo countryInfo2 = regionSelectActivity.currentCountryItem;
                    Intrinsics.checkNotNull(countryInfo2);
                    String name = countryInfo2.getName();
                    if (name != null) {
                        LoginUtil.INSTANCE.saveCurrentRegion(str2, name);
                        CountryInfo countryInfo3 = regionSelectActivity.currentCountryItem;
                        Intrinsics.checkNotNull(countryInfo3);
                        String country = countryInfo3.getCountry();
                        if (country != null) {
                            RegionExtKt.getInstance(RegionManager.INSTANCE).setCurrentCountry(country);
                        }
                    }
                }
            } else {
                regionSelectActivity.setRegionFromCurrentCountry();
            }
        }
        Bundle bundleForward = IntentRelay.INSTANCE.forward(regionSelectActivity.getIntent());
        if (AppUtil.INSTANCE.isPlayChannel()) {
            CheckerUserSettingManager.Companion companion = CheckerUserSettingManager.INSTANCE;
            CheckerUserSettingManagerExtKt.getInstance(companion).setSelectMode(companion.getCONCISE_MODE());
            if (z) {
                RegionViewModel regionViewModel = (RegionViewModel) regionSelectActivity.getMViewModel();
                CountryInfo countryInfo4 = regionSelectActivity.currentCountryItem;
                Intrinsics.checkNotNull(countryInfo4);
                regionViewModel.mainLandSwitchRegion(countryInfo4, new WeakReference<>(regionSelectActivity));
                return;
            }
            MainExtKt.showMainActivity(regionSelectActivity, null, bundleForward);
            regionSelectActivity.finish();
        } else {
            String str3 = regionSelectActivity.TAG;
            CheckerUserSettingManager.Companion companion2 = CheckerUserSettingManager.INSTANCE;
            Logger.d(str3, "LoginUtil.checkDeviceSettingForSelectMode() is " + CheckerUserSettingManagerExtKt.getInstance(companion2).getSelectMode());
            boolean zShowAppV4Enable = ICompareKt.getINSTANCE(ICompare.INSTANCE).showAppV4Enable();
            Logger.d(regionSelectActivity.TAG, "showV4Ui enable=" + zShowAppV4Enable);
            if (zShowAppV4Enable || CheckerUserSettingManagerExtKt.getInstance(companion2).getSelectMode() != companion2.getUNKOWN_MODE()) {
                MainExtKt.showMainActivity(regionSelectActivity, null, bundleForward);
                regionSelectActivity.finish();
            } else {
                Intent intent = new Intent(regionSelectActivity, (Class<?>) GuidePersonalizeActivity.class);
                intent.putExtras(bundleForward);
                regionSelectActivity.startActivity(intent);
                regionSelectActivity.finish();
            }
        }
        ((RegionViewModel) regionSelectActivity.getMViewModel()).setUserRegion(regionSelectActivity.currentCountryItem);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit resultCallback$lambda$0(RegionSelectActivity regionSelectActivity, int i, Intent intent) {
        if (i == 100) {
            ((RegionViewModel) regionSelectActivity.getMViewModel()).getCountryInfoObserver().postValue(intent != null ? (CountryInfo) intent.getParcelableExtra(RegionSelectListActivity.COUNTRY_INFO) : null);
        }
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Unit t(RegionSelectActivity regionSelectActivity, CountryInfo countryInfo) {
        return onCreate$lambda$2(regionSelectActivity, countryInfo);
    }

    public static /* synthetic */ void u(RegionSelectActivity regionSelectActivity, boolean z, View view) {
        onCreate$lambda$9(regionSelectActivity, z, view);
    }

    public static /* synthetic */ Unit v(RegionSelectActivity regionSelectActivity, int i, Intent intent) {
        return resultCallback$lambda$0(regionSelectActivity, i, intent);
    }

    public static /* synthetic */ void w(RegionSelectActivity regionSelectActivity, View view) {
        onCreate$lambda$1(regionSelectActivity, view);
    }

    public static /* synthetic */ void x(RegionSelectActivity regionSelectActivity, View view) {
        onCreate$lambda$3(regionSelectActivity, view);
    }

    @Override // com.xiaomi.fitness.baseui.ViewCreator
    /* JADX INFO: renamed from: getLayoutId */
    public int getLayoutRes() {
        return qgk.j;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public int getViewModelId() {
        return y02.c;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity
    /* JADX INFO: renamed from: isAdaptActionBar */
    public boolean getIsAdaptActionBar() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void loadCountryInfoByIP() {
        CountryInfo value = ((RegionViewModel) getMViewModel()).getCountryInfoObserver().getValue();
        if (value != null) {
            updateCountryInfo(value);
        } else {
            ((RegionViewModel) getMViewModel()).loadData(new WeakReference<>(this));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.login.region.Hilt_RegionSelectActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(tmk.O);
        hideBack();
        setTitleBackground(z4k.g);
        setSubtitle(tmk.U);
        boolean booleanExtra = getIntent().getBooleanExtra(RegionExtKt.MAIN_LAND_USER, false);
        if (AppUtil.INSTANCE.isPlayChannel()) {
            ((RegionViewModel) getMViewModel()).getIPCountry();
            ((RegionViewModel) getMViewModel()).setContryInfo(null, this);
            if (booleanExtra) {
                ((g2g) getMBinding()).C.setVisibility(0);
                ((g2g) getMBinding()).C.setOnClickListener(new fzl(this));
            }
        } else {
            loadCountryInfoByIP();
        }
        ((RegionViewModel) getMViewModel()).getCountryInfoObserver().observe(this, new RegionSelectActivity$sam$androidx_lifecycle_Observer$0(new gzl(this)));
        ((g2g) getMBinding()).B.setOnClickListener(new hzl(this));
        ((g2g) getMBinding()).A.setOnClickListener(new izl(this, booleanExtra));
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity
    public void restoreInstanceState(@NotNull Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.restoreInstanceState(outState);
        if (outState.getBoolean(KEY_REQUEST_RESULT)) {
            getResultCallBacks().push(this.resultCallback);
        }
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity
    public void saveInstanceState(@NotNull Bundle outState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        super.saveInstanceState(outState);
        outState.putBoolean(KEY_REQUEST_RESULT, !getResultCallBacks().isEmpty());
    }

    public final void setRegionFromCurrentCountry() {
        CountryInfo countryInfo = this.currentCountryItem;
        if (countryInfo == null) {
            return;
        }
        Intrinsics.checkNotNull(countryInfo);
        String name = countryInfo.getName();
        String strFindRegionByCountry = name != null ? RegionUtil.INSTANCE.findRegionByCountry(this, name) : null;
        if (strFindRegionByCountry == null) {
            LoginUtil loginUtil = LoginUtil.INSTANCE;
            CountryInfo countryInfo2 = this.currentCountryItem;
            Intrinsics.checkNotNull(countryInfo2);
            loginUtil.onSwitchToErrorRegion(countryInfo2.getName());
            Application application = getApplication();
            Intrinsics.checkNotNullExpressionValue(application, "getApplication(...)");
            ToastExtKt.toastShort(application, tmk.f);
            return;
        }
        RegionManager.Companion companion = RegionManager.INSTANCE;
        RegionExtKt.getInstance(companion).switchRegion(strFindRegionByCountry);
        CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).getUserInfoFromWeb();
        CountryInfo countryInfo3 = this.currentCountryItem;
        Intrinsics.checkNotNull(countryInfo3);
        String name2 = countryInfo3.getName();
        if (name2 != null) {
            LoginUtil.INSTANCE.saveCurrentRegion(strFindRegionByCountry, name2);
        }
        CountryInfo countryInfo4 = this.currentCountryItem;
        Intrinsics.checkNotNull(countryInfo4);
        String country = countryInfo4.getCountry();
        if (country != null) {
            RegionExtKt.getInstance(companion).setCurrentCountry(country);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void showCountryName(@Nullable CountryInfo countryItem) {
        this.currentCountryItem = countryItem;
        updateNextButton(countryItem != null);
        Logger.d(this.TAG, "currentCountryItem is " + this.currentCountryItem);
        if (this.currentCountryItem != null) {
            TextView textView = ((g2g) getMBinding()).B;
            CountryInfo countryInfo = this.currentCountryItem;
            textView.setText(countryInfo != null ? countryInfo.getCountry() : null);
            ((g2g) getMBinding()).A.setVisibility(0);
            return;
        }
        Logger.d(this.TAG, "currentCountryItem is " + getString(tmk.O));
        ((g2g) getMBinding()).B.setText(getString(tmk.O));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void updateCountryInfo(@Nullable CountryInfo it) {
        String lowerCase;
        String name;
        this.currentCountryItem = it;
        updateNextButton(it != null);
        this.country = it != null ? it.getName() : null;
        if (it == null || (name = it.getName()) == null) {
            lowerCase = null;
        } else {
            Locale US = Locale.US;
            Intrinsics.checkNotNullExpressionValue(US, "US");
            lowerCase = name.toLowerCase(US);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        }
        this.region = lowerCase;
        String country = it != null ? it.getCountry() : null;
        this.currentCountry = country;
        Logger.d(this.TAG, "updateCountryInfo country is " + this.country + " region is " + this.region + " currentCountry is " + country);
        ((RegionViewModel) getMViewModel()).setContryInfo(this.currentCountryItem, this);
        ((g2g) getMBinding()).H();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void updateNextButton(boolean enable) {
        ((g2g) getMBinding()).A.setEnabled(enable);
    }
}
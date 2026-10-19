package com.xiaomi.fitness.mine;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.i;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import coil.transform.RoundedCornersTransformation;
import com.example.appupgrade.export.AppUpgradeHelper;
import com.example.habit.export.HabitManagerKt;
import com.example.statistics.export.IOnetrack;
import com.example.statistics.export.OneTrackExtKt;
import com.fitness.banner.export.BannerExtKt;
import com.fitness.banner.export.BannerItem;
import com.fitness.banner.export.IBanner;
import com.mi.health.course.export.CourseApi;
import com.mi.health.course.export.CourseApiKt;
import com.mi.health.course.export.data.AidongVipUserInfo;
import com.xiaomi.accountsdk.request.NetworkUtils;
import com.xiaomi.fitness.about.export.AboutManager;
import com.xiaomi.fitness.about.utils.MineHtmlUrlsKt;
import com.xiaomi.fitness.account.api.bean.UserProfile;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.extensions.OnUserInfoChange;
import com.xiaomi.fitness.account.manager.AccountCoreInfo;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.account.manager.IMiAccountManager;
import com.xiaomi.fitness.account.region.RegionPreference;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.active.pk.export.ActivePkPageExtKt;
import com.xiaomi.fitness.active.pk.export.IPkExport;
import com.xiaomi.fitness.baseui.extension.BaseFragmentExtKt;
import com.xiaomi.fitness.baseui.view.BaseActivity;
import com.xiaomi.fitness.baseui.widget.ScrollTitleBar;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.AnyExtKt;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.ToastExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import com.xiaomi.fitness.common.utils.ExtUtilsKt;
import com.xiaomi.fitness.common.utils.NetUtil;
import com.xiaomi.fitness.common.utils.ToastUtil;
import com.xiaomi.fitness.component.StandardComponentEvent;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.extensions.ViewExtKt;
import com.xiaomi.fitness.feedback.export.UploadFileManager;
import com.xiaomi.fitness.feedback.export.UploadFileManagerExtKt;
import com.xiaomi.fitness.health.export.HealthPageExtKt;
import com.xiaomi.fitness.health.export.IHealthPage;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.CheckerUserSettingManagerExtKt;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.main.export.BaseMainFragment;
import com.xiaomi.fitness.main.export.MainHelper;
import com.xiaomi.fitness.medal.export.AboutManagerExtKt;
import com.xiaomi.fitness.medal.export.MedalManager;
import com.xiaomi.fitness.medal.export.MedalManagerExtKt;
import com.xiaomi.fitness.medal.export.data.Medal;
import com.xiaomi.fitness.medal.export.data.MedalEntrance;
import com.xiaomi.fitness.medal.export.data.PopMedalData;
import com.xiaomi.fitness.mine.company.CompanyUserEntranceResult;
import com.xiaomi.fitness.mine.company.CompanyUserWebViewActivity;
import com.xiaomi.fitness.mine.export.IWebSyncImpl;
import com.xiaomi.fitness.mine.export.IWebSyncKt;
import com.xiaomi.fitness.pay.export.PayManagerExtKt;
import com.xiaomi.fitness.pay.export.PaymentManager;
import com.xiaomi.fitness.privacy.export.PrivacyHelper;
import com.xiaomi.fitness.privacy.export.PrivacyHelperPointKt;
import com.xiaomi.fitness.relative.export.IRelativeCareAccessApi;
import com.xiaomi.fitness.relative.export.RelativeCareAccessExtKt;
import com.xiaomi.fitness.rn.export.DeviceShareManager;
import com.xiaomi.fitness.rn.export.DeviceShareManagerExtKt;
import com.xiaomi.fitness.route.export.track.TrackNet;
import com.xiaomi.fitness.route.export.track.TrackNetManagerKt;
import com.xiaomi.fitness.sleep.trace.analyze.SleepAnalyzerAlpha;
import com.xiaomi.fitness.user.info.export.SetPageManageExtKt;
import com.xiaomi.fitness.user.info.export.SetPageManager;
import com.xiaomi.fitness.util.MineOneTrack;
import com.xiaomi.fitness.util.MineUtil;
import com.xiaomi.fitness.webview.WebViewActivity;
import com.xiaomi.fitness.webview.healthwebview.HealthWebViewActivity;
import com.xiaomi.fitness.widget.RightArrowBindingTwoLineTextView;
import com.xiaomi.fitness.widget.view.RoundImageView;
import com.xms.wearable.export.XmsManager;
import com.xms.wearable.export.XmsManagerExtKt;
import defpackage.agh;
import defpackage.ahh;
import defpackage.ank;
import defpackage.bgh;
import defpackage.bhh;
import defpackage.cgh;
import defpackage.chh;
import defpackage.d7k;
import defpackage.dgh;
import defpackage.dhh;
import defpackage.e5k;
import defpackage.egh;
import defpackage.ehh;
import defpackage.fgh;
import defpackage.fhh;
import defpackage.ggh;
import defpackage.ghh;
import defpackage.hgh;
import defpackage.hhh;
import defpackage.igh;
import defpackage.ihh;
import defpackage.jdk;
import defpackage.jgh;
import defpackage.jhh;
import defpackage.kgh;
import defpackage.khh;
import defpackage.lgh;
import defpackage.lhh;
import defpackage.m54;
import defpackage.mgh;
import defpackage.mhh;
import defpackage.ngh;
import defpackage.ogh;
import defpackage.pgh;
import defpackage.qgh;
import defpackage.rgh;
import defpackage.sgh;
import defpackage.sj7;
import defpackage.tgh;
import defpackage.ugh;
import defpackage.vgh;
import defpackage.wgh;
import defpackage.wgk;
import defpackage.xgh;
import defpackage.ygh;
import defpackage.zgh;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 u2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001uB\u0011\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000b\u0010\nJ\u000f\u0010\f\u001a\u00020\bH\u0002¢\u0006\u0004\b\f\u0010\nJ\u000f\u0010\u000e\u001a\u00020\rH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0010\u0010\nJ\u000f\u0010\u0011\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0011\u0010\nJ\u000f\u0010\u0012\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0012\u0010\nJ\u000f\u0010\u0013\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0013\u0010\nJ\u000f\u0010\u0014\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0014\u0010\nJ\u000f\u0010\u0015\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0015\u0010\nJ\u0017\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0016H\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u000f\u0010\u001a\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001a\u0010\nJ\u000f\u0010\u001b\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001b\u0010\nJ\u000f\u0010\u001c\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001c\u0010\nJ\u0017\u0010\u001d\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0016H\u0002¢\u0006\u0004\b\u001d\u0010\u001eJ\u000f\u0010\u001f\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001f\u0010\nJ\u000f\u0010 \u001a\u00020\bH\u0002¢\u0006\u0004\b \u0010\nJ\u000f\u0010!\u001a\u00020\bH\u0002¢\u0006\u0004\b!\u0010\nJ\u000f\u0010\"\u001a\u00020\bH\u0002¢\u0006\u0004\b\"\u0010\nJ\u0019\u0010%\u001a\u00020\b2\b\u0010$\u001a\u0004\u0018\u00010#H\u0016¢\u0006\u0004\b%\u0010&J!\u0010)\u001a\u00020\b2\u0006\u0010(\u001a\u00020'2\b\u0010$\u001a\u0004\u0018\u00010#H\u0016¢\u0006\u0004\b)\u0010*J\u0013\u0010+\u001a\u00020\b*\u00020\u0003H\u0016¢\u0006\u0004\b+\u0010,J\u001d\u00101\u001a\u00020/2\u0006\u0010.\u001a\u00020-2\u0006\u00100\u001a\u00020/¢\u0006\u0004\b1\u00102J\u001d\u00105\u001a\u00020/2\u0006\u00103\u001a\u00020/2\u0006\u00104\u001a\u00020-¢\u0006\u0004\b5\u00106J\r\u00107\u001a\u00020\b¢\u0006\u0004\b7\u0010\nJ\u000f\u00108\u001a\u00020\bH\u0014¢\u0006\u0004\b8\u0010\nJ\u000f\u00109\u001a\u00020\bH\u0014¢\u0006\u0004\b9\u0010\nJ\u000f\u0010:\u001a\u00020\bH\u0016¢\u0006\u0004\b:\u0010\nJ\u000f\u0010;\u001a\u00020\bH\u0016¢\u0006\u0004\b;\u0010\nJ\u0017\u0010>\u001a\u00020\b2\u0006\u0010=\u001a\u00020<H\u0016¢\u0006\u0004\b>\u0010?J\u0011\u0010@\u001a\u0004\u0018\u00010'H\u0016¢\u0006\u0004\b@\u0010AR\u0014\u0010\u0005\u001a\u00020\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010BR\"\u0010D\u001a\u00020C8\u0006@\u0006X\u0087.¢\u0006\u0012\n\u0004\bD\u0010E\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\"\u0010K\u001a\u00020J8\u0006@\u0006X\u0087.¢\u0006\u0012\n\u0004\bK\u0010L\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR\"\u0010R\u001a\u00020Q8\u0006@\u0006X\u0087.¢\u0006\u0012\n\u0004\bR\u0010S\u001a\u0004\bT\u0010U\"\u0004\bV\u0010WR$\u0010Y\u001a\u0004\u0018\u00010X8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bY\u0010Z\u001a\u0004\b[\u0010\\\"\u0004\b]\u0010^R\"\u0010_\u001a\u00020/8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b_\u0010`\u001a\u0004\ba\u0010b\"\u0004\bc\u0010dR\"\u0010e\u001a\u00020\r8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\be\u0010f\u001a\u0004\bg\u0010\u000f\"\u0004\bh\u0010iR\u0014\u0010k\u001a\u00020j8\u0002X\u0082D¢\u0006\u0006\n\u0004\bk\u0010lR\u0016\u0010n\u001a\u00020m8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bn\u0010oR\u0014\u0010p\u001a\u00020j8\u0002X\u0082D¢\u0006\u0006\n\u0004\bp\u0010lR$\u0010r\u001a\u0010\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\b\u0018\u00010q8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\br\u0010sR\u0018\u0010t\u001a\u0004\u0018\u00010j8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bt\u0010l¨\u0006v"}, d2 = {"Lcom/xiaomi/fitness/mine/MineFragment;", "Lcom/xiaomi/fitness/main/export/BaseMainFragment;", "Lcom/xiaomi/fitness/mine/MineViewModel;", "Lmhh;", "Lcom/xiaomi/fitness/main/export/MainHelper;", "mMainHelper", "<init>", "(Lcom/xiaomi/fitness/main/export/MainHelper;)V", "", "startObservingPopMedalData", "()V", "initBanner", "addPKEntranceOnClickListener", "", "whetherToShowAidongPaymentEntrance", "()Z", "showOrHideAidongCoursePaymentEntrance", "loadMyHabitList", "setTitleView", "setUserInformation", "setTitleBarColor", "initView", "Lcom/xiaomi/fitness/mine/export/IWebSyncImpl;", "iWebSyncImpl", "showWebSyncDialogIfNeed", "(Lcom/xiaomi/fitness/mine/export/IWebSyncImpl;)Z", "showAidongCompanyUserEntrance", "showPopMedalDialogIfNeed", "handlePopMedalData", "showSyncWebDialog", "(Lcom/xiaomi/fitness/mine/export/IWebSyncImpl;)V", "refreshPadding", "oneTrackRelativeCareEntranceClick", "oneTrackRelativeCareEntranceExpose", "initAidongPaymentEntrance", "Landroid/os/Bundle;", "savedInstanceState", "onCreate", "(Landroid/os/Bundle;)V", "Landroid/view/View;", "view", "onViewCreated", "(Landroid/view/View;Landroid/os/Bundle;)V", "bindView", "(Lmhh;)V", "", SleepAnalyzerAlpha.NAME_ALPHA, "", "baseColor", "getColorWithAlpha", "(FI)I", "normalColor", "darkRatio", "darkColor", "(IF)I", "initUserInfo", "onVisible", "onInvisible", LifecycleConstantKt.ON_RESUME, LifecycleConstantKt.ON_DESTROY, "Landroid/content/res/Configuration;", StandardComponentEvent.KEY_NEW_CONFIG, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", "getContentView", "()Landroid/view/View;", "Lcom/xiaomi/fitness/main/export/MainHelper;", "Lcom/xiaomi/fitness/account/manager/IMiAccountManager;", "mMiAccountManager", "Lcom/xiaomi/fitness/account/manager/IMiAccountManager;", "getMMiAccountManager", "()Lcom/xiaomi/fitness/account/manager/IMiAccountManager;", "setMMiAccountManager", "(Lcom/xiaomi/fitness/account/manager/IMiAccountManager;)V", "Lcom/xiaomi/fitness/account/manager/AccountManager;", "mAccoutManager", "Lcom/xiaomi/fitness/account/manager/AccountManager;", "getMAccoutManager", "()Lcom/xiaomi/fitness/account/manager/AccountManager;", "setMAccoutManager", "(Lcom/xiaomi/fitness/account/manager/AccountManager;)V", "Lcom/example/appupgrade/export/AppUpgradeHelper;", "appUpgradeHelper", "Lcom/example/appupgrade/export/AppUpgradeHelper;", "getAppUpgradeHelper", "()Lcom/example/appupgrade/export/AppUpgradeHelper;", "setAppUpgradeHelper", "(Lcom/example/appupgrade/export/AppUpgradeHelper;)V", "Lcom/xiaomi/fitness/account/manager/AccountCoreInfo;", "xiaomiCoreInfo", "Lcom/xiaomi/fitness/account/manager/AccountCoreInfo;", "getXiaomiCoreInfo", "()Lcom/xiaomi/fitness/account/manager/AccountCoreInfo;", "setXiaomiCoreInfo", "(Lcom/xiaomi/fitness/account/manager/AccountCoreInfo;)V", "habitNum", "I", "getHabitNum", "()I", "setHabitNum", "(I)V", "mStartSportImport", "Z", "getMStartSportImport", "setMStartSportImport", "(Z)V", "", "chinaRegion", "Ljava/lang/String;", "", "mMinePageStartShowTime", "J", "MINE_TAB_PAGE_NAME", "Lkotlin/Function1;", "mSelectModeListener", "Lkotlin/jvm/functions/Function1;", "curtDeviceModelStr", "Companion", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nMineFragment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MineFragment.kt\ncom/xiaomi/fitness/mine/MineFragment\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Extensions.kt\ncoil/-SingletonExtensions\n+ 4 ImageRequest.kt\ncoil/request/ImageRequest$Builder\n*L\n1#1,1103:1\n1#2:1104\n54#3,3:1105\n24#3:1108\n59#3,6:1109\n54#3,3:1115\n24#3:1118\n59#3,6:1119\n54#3,3:1125\n24#3:1128\n59#3,4:1129\n63#3,2:1142\n844#4,9:1133\n*S KotlinDebug\n*F\n+ 1 MineFragment.kt\ncom/xiaomi/fitness/mine/MineFragment\n*L\n242#1:1105,3\n242#1:1108\n242#1:1109,6\n516#1:1115,3\n516#1:1118\n516#1:1119,6\n609#1:1125,3\n609#1:1128\n609#1:1129,4\n609#1:1142,2\n611#1:1133,9\n*E\n"})
public final class MineFragment extends Hilt_MineFragment<MineViewModel, mhh> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    public static final String KEY_PARAMS_MEDAL_CLASS = "key_params_medal_class";

    @NotNull
    public static final String KEY_PARAMS_MEDAL_ID = "key_params_medal_id";

    @NotNull
    public static final String KEY_PARAMS_MEDAL_TYPE = "key_params_medal_type";

    @NotNull
    public static final String TAG = "MineFragment";

    @NotNull
    private final String MINE_TAB_PAGE_NAME;
    public AppUpgradeHelper appUpgradeHelper;

    @NotNull
    private final String chinaRegion;

    @Nullable
    private String curtDeviceModelStr;
    private int habitNum;
    public AccountManager mAccoutManager;

    @NotNull
    private final MainHelper mMainHelper;
    public IMiAccountManager mMiAccountManager;
    private long mMinePageStartShowTime;

    @Nullable
    private Function1<? super Integer, Unit> mSelectModeListener;
    private boolean mStartSportImport;

    @Nullable
    private AccountCoreInfo xiaomiCoreInfo;

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/xiaomi/fitness/mine/MineFragment$Companion;", "", "<init>", "()V", "KEY_PARAMS_MEDAL_ID", "", "KEY_PARAMS_MEDAL_TYPE", "KEY_PARAMS_MEDAL_CLASS", "TAG", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.mine.MineFragment$initUserInfo$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"com/xiaomi/fitness/mine/MineFragment$initUserInfo$1", "Lm54;", "Landroid/app/ActivityOptions;", "activityOptions", "", "onCardZoomTriggered", "(Landroid/app/ActivityOptions;)V", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass1 extends m54 {
        public AnonymousClass1(FragmentActivity fragmentActivity, ConstraintLayout constraintLayout) {
            super(fragmentActivity, constraintLayout, null);
        }

        @Override // defpackage.m54
        public void onCardZoomTriggered(ActivityOptions activityOptions) {
            MineOneTrack.INSTANCE.trackMedal();
            if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                FragmentActivity mActivity = MineFragment.this.getMActivity();
                if (mActivity != null) {
                    MineFragment.access$showWebSyncDialogIfNeed(MineFragment.this, new MineFragment$initUserInfo$1$onCardZoomTriggered$2$1(mActivity));
                    return;
                }
                return;
            }
            FragmentActivity mActivity2 = MineFragment.this.getMActivity();
            if (mActivity2 != null) {
                CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity2);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.mine.MineFragment$onViewCreated$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"com/xiaomi/fitness/mine/MineFragment$onViewCreated$1", "Lcom/xiaomi/fitness/account/extensions/OnUserInfoChange;", "userInfoChange", "", "userProfile", "Lcom/xiaomi/fitness/account/api/bean/UserProfile;", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29471 implements OnUserInfoChange {
        public C29471() {
        }

        @Override // com.xiaomi.fitness.account.extensions.OnUserInfoChange
        public void userInfoChange(UserProfile userProfile) {
            Intrinsics.checkNotNullParameter(userProfile, "userProfile");
            if (MineFragment.this.isInvalid()) {
                return;
            }
            MineFragment.access$setUserInformation(MineFragment.this);
            if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                AnyExtKt.main$default(null, new MineFragment$onViewCreated$1$userInfoChange$1(MineFragment.this, null), 1, null);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.mine.MineFragment$showSyncWebDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J'\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\nJ'\u0010\r\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"com/xiaomi/fitness/mine/MineFragment$showSyncWebDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "", "isChecked", "onDialogMultiClick", "(Ljava/lang/String;IZ)V", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29481 extends sj7 {
        final /* synthetic */ IWebSyncImpl $iWebSyncImpl;

        public C29481(IWebSyncImpl iWebSyncImpl) {
            this.$iWebSyncImpl = iWebSyncImpl;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            Intrinsics.checkNotNullParameter(dialogName, "dialogName");
            Intrinsics.checkNotNullParameter(dialog, "dialog");
            super.onDialogClick(dialogName, dialog, which);
            if (which == -2) {
                dialog.dismiss();
                RegionPreference regionPreference = RegionPreference.INSTANCE;
                regionPreference.setSyncWeb(which == -1);
                regionPreference.setSettingSyncWeb(true);
                return;
            }
            if (which != -1) {
                return;
            }
            dialog.dismiss();
            RegionPreference regionPreference2 = RegionPreference.INSTANCE;
            regionPreference2.setSyncWeb(which == -1);
            regionPreference2.setSettingSyncWeb(true);
            MineSettingHelper.INSTANCE.triggerSyncWithServer();
            this.$iWebSyncImpl.onWebSyncAccepted();
        }

        @Override // defpackage.sj7
        public void onDialogMultiClick(String dialogName, int which, boolean isChecked) {
            Intrinsics.checkNotNullParameter(dialogName, "dialogName");
            super.onDialogMultiClick(dialogName, which, isChecked);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MineFragment(@NotNull MainHelper mMainHelper) {
        super(wgk.f15846k, 0, true);
        Intrinsics.checkNotNullParameter(mMainHelper, "mMainHelper");
        this.mMainHelper = mMainHelper;
        this.chinaRegion = "cn";
        this.MINE_TAB_PAGE_NAME = "profile_tab_page";
    }

    public static /* synthetic */ void B(MineFragment mineFragment, View view) {
        initView$lambda$44(mineFragment, view);
    }

    public static /* synthetic */ void C(MineFragment mineFragment, View view) {
        initView$lambda$34(mineFragment, view);
    }

    public static /* synthetic */ void D(MineFragment mineFragment, View view) {
        initUserInfo$lambda$12(mineFragment, view);
    }

    public static /* synthetic */ void E(MineFragment mineFragment, View view) {
        bindView$lambda$10(mineFragment, view);
    }

    public static /* synthetic */ void F(Function1 function1, View view) {
        setTitleView$lambda$19(function1, view);
    }

    public static /* synthetic */ Unit G(MineFragment mineFragment, CompanyUserEntranceResult companyUserEntranceResult) {
        return showAidongCompanyUserEntrance$lambda$54(mineFragment, companyUserEntranceResult);
    }

    public static /* synthetic */ Unit H(MineFragment mineFragment, AccountCoreInfo accountCoreInfo) {
        return initView$lambda$23$lambda$22(mineFragment, accountCoreInfo);
    }

    public static /* synthetic */ void I(MineFragment mineFragment, View view) {
        initView$lambda$25(mineFragment, view);
    }

    public static /* synthetic */ Unit J(MineFragment mineFragment, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return addPKEntranceOnClickListener$lambda$7(mineFragment, rightArrowBindingTwoLineTextView);
    }

    public static /* synthetic */ void K(MineFragment mineFragment, View view) {
        initView$lambda$46(mineFragment, view);
    }

    public static /* synthetic */ Unit L() {
        return loadMyHabitList$lambda$14();
    }

    public static /* synthetic */ Unit M(MineFragment mineFragment, int i) {
        return onViewCreated$lambda$2(mineFragment, i);
    }

    public static /* synthetic */ Unit N(MineFragment mineFragment, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return initView$lambda$27(mineFragment, rightArrowBindingTwoLineTextView);
    }

    public static /* synthetic */ void O(MineFragment mineFragment, View view) {
        initView$lambda$37(mineFragment, view);
    }

    public static /* synthetic */ Unit P(MineFragment mineFragment, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return initView$lambda$26(mineFragment, rightArrowBindingTwoLineTextView);
    }

    public static /* synthetic */ Unit Q(RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return initAidongPaymentEntrance$lambda$58(rightArrowBindingTwoLineTextView);
    }

    public static /* synthetic */ void R(MineFragment mineFragment, View view) {
        initView$lambda$47(mineFragment, view);
    }

    public static /* synthetic */ Unit S(MineFragment mineFragment, List list) {
        return showPopMedalDialogIfNeed$lambda$55(mineFragment, list);
    }

    public static /* synthetic */ Unit T(MineFragment mineFragment, PopMedalData popMedalData) {
        return startObservingPopMedalData$lambda$1(mineFragment, popMedalData);
    }

    public static /* synthetic */ void U(MineFragment mineFragment, View view) {
        initView$lambda$50(mineFragment, view);
    }

    public static /* synthetic */ void V(MineFragment mineFragment, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Ref.ObjectRef objectRef3, Ref.ObjectRef objectRef4, Ref.ObjectRef objectRef5) {
        setUserInformation$lambda$20(mineFragment, objectRef, objectRef2, objectRef3, objectRef4, objectRef5);
    }

    public static /* synthetic */ Unit W(MineFragment mineFragment, ArrayList arrayList) {
        return loadMyHabitList$lambda$13(mineFragment, arrayList);
    }

    public static /* synthetic */ Unit X(MineFragment mineFragment, BannerItem bannerItem) {
        return initBanner$lambda$5(mineFragment, bannerItem);
    }

    public static /* synthetic */ void Y(MineFragment mineFragment, View view) {
        initView$lambda$38(mineFragment, view);
    }

    public static /* synthetic */ void Z(MineFragment mineFragment, View view) {
        initView$lambda$40(mineFragment, view);
    }

    public static /* synthetic */ Unit a0(MineFragment mineFragment, View view) {
        return setTitleView$lambda$17(mineFragment, view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final /* synthetic */ mhh access$getMBinding(MineFragment mineFragment) {
        return (mhh) mineFragment.getMBinding();
    }

    public static final /* synthetic */ void access$initView(MineFragment mineFragment) {
        mineFragment.initView();
    }

    public static final /* synthetic */ void access$setUserInformation(MineFragment mineFragment) {
        mineFragment.setUserInformation();
    }

    public static final /* synthetic */ boolean access$showWebSyncDialogIfNeed(MineFragment mineFragment, IWebSyncImpl iWebSyncImpl) {
        return mineFragment.showWebSyncDialogIfNeed(iWebSyncImpl);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void addPKEntranceOnClickListener() {
        ViewExtKt.clickWithTrigger$default(((mhh) getMBinding()).B, 0L, new ahh(this), 1, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit addPKEntranceOnClickListener$lambda$7(MineFragment mineFragment, RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        MineOneTrack.INSTANCE.trackCompetition();
        if (mineFragment.getMAccoutManager().isLogin()) {
            ((MineViewModel) mineFragment.getMViewModel()).queryProcessPkCountLiveData().observe(mineFragment.getViewLifecycleOwner(), new MineFragment$sam$androidx_lifecycle_Observer$0(new dhh(mineFragment)));
        } else {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).accountLogin();
        }
        return Unit.INSTANCE;
    }

    private static final Unit addPKEntranceOnClickListener$lambda$7$lambda$6(MineFragment mineFragment, Integer num) {
        if (num.intValue() <= 0) {
            IPkExport instance = ActivePkPageExtKt.getINSTANCE(IPkExport.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
            instance.goToSendInvitationActivity(fragmentActivityRequireActivity, null);
        } else {
            IPkExport instance2 = ActivePkPageExtKt.getINSTANCE(IPkExport.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity2 = mineFragment.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity2, "requireActivity(...)");
            instance2.goToPkListActivity(fragmentActivityRequireActivity2, null);
        }
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Unit b0(MineFragment mineFragment, Integer num) {
        return addPKEntranceOnClickListener$lambda$7$lambda$6(mineFragment, num);
    }

    private static final void bindView$lambda$10(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackLittleHabits();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            FragmentActivity mActivity = mineFragment.getMActivity();
            if (mActivity != null) {
                HabitManagerKt.getHabitManager().gotoHabitPage(mActivity, mineFragment.habitNum);
                return;
            }
            return;
        }
        FragmentActivity mActivity2 = mineFragment.getMActivity();
        if (mActivity2 != null) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity2);
        }
    }

    public static /* synthetic */ void c0(MineFragment mineFragment, View view) {
        initView$lambda$24(mineFragment, view);
    }

    public static /* synthetic */ Unit d0(MineFragment mineFragment) {
        return startObservingPopMedalData$lambda$1$lambda$0(mineFragment);
    }

    public static /* synthetic */ void e0(MineFragment mineFragment, View view) {
        initView$lambda$51(mineFragment, view);
    }

    public static /* synthetic */ void f0(MineFragment mineFragment, View view) {
        initView$lambda$42(mineFragment, view);
    }

    public static /* synthetic */ Unit g0(MineFragment mineFragment) {
        return showAidongCompanyUserEntrance$lambda$54$lambda$53$lambda$52(mineFragment);
    }

    public static /* synthetic */ Unit h0(MineFragment mineFragment, MedalEntrance medalEntrance) {
        return initView$lambda$32(mineFragment, medalEntrance);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void handlePopMedalData() {
        boolean comeFromMedalDetailPageFromSP = ((MineViewModel) getMViewModel()).getComeFromMedalDetailPageFromSP();
        Logger.d("MineFragment", "showPopMedalDialogIfNeed removePoppedMedalData=" + comeFromMedalDetailPageFromSP);
        if (((MineViewModel) getMViewModel()).popMedalListFromSpIsEmpty()) {
            return;
        }
        if (comeFromMedalDetailPageFromSP) {
            ((MineViewModel) getMViewModel()).removeMedalAlreadyPoppedFromSp();
        }
        if (((MineViewModel) getMViewModel()).popMedalListFromSpIsEmpty()) {
            ((MineViewModel) getMViewModel()).setShouldPullPopMedalDataFromNetwork();
        } else {
            ((MineViewModel) getMViewModel()).getMedalToPopLiveData().setValue(((MineViewModel) getMViewModel()).getFirstMedalFromPopMedalDataSP());
        }
    }

    public static /* synthetic */ Unit i0(MineFragment mineFragment, AidongVipUserInfo aidongVipUserInfo) {
        return initAidongPaymentEntrance$lambda$57(mineFragment, aidongVipUserInfo);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void initAidongPaymentEntrance() {
        ((MineViewModel) getMViewModel()).getMiTvAidongVipUserInfoLiveData().observe(getViewLifecycleOwner(), new MineFragment$sam$androidx_lifecycle_Observer$0(new bgh(this)));
        ViewExtKt.clickWithTrigger$default(((mhh) getMBinding()).X, 0L, new cgh(), 1, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit initAidongPaymentEntrance$lambda$57(MineFragment mineFragment, AidongVipUserInfo aidongVipUserInfo) {
        if (aidongVipUserInfo == null) {
            ((mhh) mineFragment.getMBinding()).X.setRemindText((String) null);
            return Unit.INSTANCE;
        }
        ((mhh) mineFragment.getMBinding()).X.setRemindText(MineUtil.INSTANCE.getAidongMemberPlanStatus(aidongVipUserInfo));
        return Unit.INSTANCE;
    }

    private static final Unit initAidongPaymentEntrance$lambda$58(RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        MineUtil.INSTANCE.goToAidongMemberPaymentPage();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void initBanner() {
        ((MineViewModel) getMViewModel()).queryBannerDetailLiveData().observe(getViewLifecycleOwner(), new MineFragment$sam$androidx_lifecycle_Observer$0(new zgh(this)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit initBanner$lambda$5(MineFragment mineFragment, BannerItem bannerItem) {
        if (bannerItem == null) {
            AppCompatImageView ivBanner = ((mhh) mineFragment.getMBinding()).M;
            Intrinsics.checkNotNullExpressionValue(ivBanner, "ivBanner");
            ViewExtKt.setVisible(ivBanner, false);
        } else {
            AppCompatImageView ivBanner2 = ((mhh) mineFragment.getMBinding()).M;
            Intrinsics.checkNotNullExpressionValue(ivBanner2, "ivBanner");
            ViewExtKt.setVisible(ivBanner2, true);
            AppCompatImageView ivBanner3 = ((mhh) mineFragment.getMBinding()).M;
            Intrinsics.checkNotNullExpressionValue(ivBanner3, "ivBanner");
            String iconHead = bannerItem.getIconHead();
            ImageLoader imageLoader = Coil.imageLoader(ivBanner3.getContext());
            ImageRequest.Builder builderTarget = new ImageRequest.Builder(ivBanner3.getContext()).data(iconHead).target(ivBanner3);
            builderTarget.transformations(new RoundedCornersTransformation(ExtUtilsKt.getDp(16)));
            imageLoader.enqueue(builderTarget.build());
            ((mhh) mineFragment.getMBinding()).M.setOnClickListener(new egh(bannerItem));
            BannerExtKt.getINSTANCE(IBanner.INSTANCE).onTrackBannerV1ClickOrExpose(bannerItem, false);
        }
        return Unit.INSTANCE;
    }

    private static final void initBanner$lambda$5$lambda$4(BannerItem bannerItem, View view) {
        BannerExtKt.getINSTANCE(IBanner.INSTANCE).onBannerV1Click(bannerItem);
    }

    private static final void initUserInfo$lambda$12(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsAboutClick();
        FragmentActivity activity = mineFragment.getActivity();
        if (activity != null) {
            AboutManagerExtKt.getInstance(AboutManager.INSTANCE).gotoAboutActivity(activity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void initView() {
        String str = getString(ank.E) + " " + getMAccoutManager().getUserId();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            ((mhh) getMBinding()).U.setDes(str);
        }
        ((MineViewModel) getMViewModel()).getAccountCoreInfo();
        FragmentActivity mActivity = getMActivity();
        if (mActivity != null) {
            ((MineViewModel) getMViewModel()).getXiaomiCoreInfo().observe(mActivity, new MineFragment$sam$androidx_lifecycle_Observer$0(new ggh(this)));
        }
        ((MineViewModel) getMViewModel()).getMedalEntrance();
        initUserInfo();
        if (!getMAccoutManager().isLogin()) {
            ((mhh) getMBinding()).h0.setText(getString(ank.Q));
            ((mhh) getMBinding()).g0.setText(getString(ank.p0));
        }
        ((mhh) getMBinding()).P.setOnClickListener(new pgh(this));
        ((mhh) getMBinding()).j0.showMore(false);
        AppUtil appUtil = AppUtil.INSTANCE;
        if (!appUtil.isPlayChannel() && RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            ((mhh) getMBinding()).j0.setOnClickListener(new qgh(this));
        }
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            RightArrowBindingTwoLineTextView relativeCare = ((mhh) getMBinding()).b0;
            Intrinsics.checkNotNullExpressionValue(relativeCare, "relativeCare");
            ViewExtKt.gone(relativeCare);
        } else {
            RightArrowBindingTwoLineTextView relativeCare2 = ((mhh) getMBinding()).b0;
            Intrinsics.checkNotNullExpressionValue(relativeCare2, "relativeCare");
            ViewExtKt.visible(relativeCare2);
            ViewExtKt.clickWithTrigger$default(((mhh) getMBinding()).b0, 0L, new rgh(this), 1, null);
        }
        showAidongCompanyUserEntrance();
        ViewExtKt.clickWithTrigger$default(((mhh) getMBinding()).a0, 0L, new sgh(this), 1, null);
        MutableLiveData<MedalEntrance> medalMedalEntrance = ((MineViewModel) getMViewModel()).getMedalMedalEntrance();
        if (medalMedalEntrance != null) {
            medalMedalEntrance.observe(getViewLifecycleOwner(), new MineFragment$sam$androidx_lifecycle_Observer$0(new tgh(this)));
        }
        ((mhh) getMBinding()).D.setOnClickListener(new ugh(this));
        ((mhh) getMBinding()).I.setOnClickListener(new vgh(this));
        if (appUtil.isPlayChannel()) {
            ((mhh) getMBinding()).I.setVisibility(8);
        } else {
            ((mhh) getMBinding()).I.setVisibility(0);
        }
        ((mhh) getMBinding()).d0.setOnClickListener(new xgh(this));
        ((mhh) getMBinding()).H.setOnClickListener(new ygh(this));
        ((mhh) getMBinding()).e0.setVisibility(0);
        ((mhh) getMBinding()).e0.setOnClickListener(new hgh(this));
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            ((mhh) getMBinding()).k0.setVisibility(8);
        } else {
            ((mhh) getMBinding()).k0.setVisibility(0);
            ((mhh) getMBinding()).k0.setOnClickListener(new igh(this));
        }
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            RightArrowBindingTwoLineTextView healthVisitDoctor = ((mhh) getMBinding()).L;
            Intrinsics.checkNotNullExpressionValue(healthVisitDoctor, "healthVisitDoctor");
            ViewExtKt.gone(healthVisitDoctor);
        } else {
            RightArrowBindingTwoLineTextView healthVisitDoctor2 = ((mhh) getMBinding()).L;
            Intrinsics.checkNotNullExpressionValue(healthVisitDoctor2, "healthVisitDoctor");
            ViewExtKt.visible(healthVisitDoctor2);
            ((mhh) getMBinding()).L.setRemindText(getString(ank.W));
            ViewExtKt.clickWithTrigger$default(((mhh) getMBinding()).L, 0L, new jgh(this), 1, null);
        }
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            RightArrowBindingTwoLineTextView courseMine = ((mhh) getMBinding()).G;
            Intrinsics.checkNotNullExpressionValue(courseMine, "courseMine");
            ViewExtKt.gone(courseMine);
        } else {
            RightArrowBindingTwoLineTextView courseMine2 = ((mhh) getMBinding()).G;
            Intrinsics.checkNotNullExpressionValue(courseMine2, "courseMine");
            ViewExtKt.visible(courseMine2);
            ((mhh) getMBinding()).G.setOnClickListener(new kgh(this));
        }
        ((mhh) getMBinding()).Y.setOnClickListener(new mgh(this));
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            ((mhh) getMBinding()).C.setVisibility(8);
        } else {
            ((mhh) getMBinding()).C.setOnClickListener(new ngh(this));
        }
        if (appUtil.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            return;
        }
        RightArrowBindingTwoLineTextView orderPageList = ((mhh) getMBinding()).Z;
        Intrinsics.checkNotNullExpressionValue(orderPageList, "orderPageList");
        ViewExtKt.visible(orderPageList);
        ((mhh) getMBinding()).Z.setOnClickListener(new ogh(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit initView$lambda$23$lambda$22(MineFragment mineFragment, AccountCoreInfo accountCoreInfo) {
        String userId;
        mineFragment.xiaomiCoreInfo = accountCoreInfo;
        if (accountCoreInfo != null) {
            String userName = accountCoreInfo.getUserName();
            if (userName == null || userName.length() == 0) {
                accountCoreInfo.getUserId();
            } else {
                accountCoreInfo.getUserName();
            }
        }
        AccountCoreInfo accountCoreInfo2 = mineFragment.xiaomiCoreInfo;
        String avatarAddress = accountCoreInfo2 != null ? accountCoreInfo2.getAvatarAddress() : null;
        if (avatarAddress != null && avatarAddress.length() != 0) {
            RoundImageView userPhoto = ((mhh) mineFragment.getMBinding()).i0;
            Intrinsics.checkNotNullExpressionValue(userPhoto, "userPhoto");
            AccountCoreInfo accountCoreInfo3 = mineFragment.xiaomiCoreInfo;
            String avatarAddress2 = accountCoreInfo3 != null ? accountCoreInfo3.getAvatarAddress() : null;
            ImageLoader imageLoader = Coil.imageLoader(userPhoto.getContext());
            ImageRequest.Builder builderTarget = new ImageRequest.Builder(userPhoto.getContext()).data(avatarAddress2).target(userPhoto);
            builderTarget.bitmapConfig(Bitmap.Config.ARGB_8888);
            imageLoader.enqueue(builderTarget.build());
        }
        if (mineFragment.xiaomiCoreInfo != null) {
            TextView textView = ((mhh) mineFragment.getMBinding()).h0;
            AccountCoreInfo accountCoreInfo4 = mineFragment.xiaomiCoreInfo;
            Intrinsics.checkNotNull(accountCoreInfo4);
            String userName2 = accountCoreInfo4.getUserName();
            if (userName2 == null || userName2.length() == 0) {
                AccountCoreInfo accountCoreInfo5 = mineFragment.xiaomiCoreInfo;
                Intrinsics.checkNotNull(accountCoreInfo5);
                userId = accountCoreInfo5.getUserId();
            } else {
                AccountCoreInfo accountCoreInfo6 = mineFragment.xiaomiCoreInfo;
                Intrinsics.checkNotNull(accountCoreInfo6);
                userId = accountCoreInfo6.getUserName();
            }
            textView.setText(userId);
            AccountCoreInfo accountCoreInfo7 = mineFragment.xiaomiCoreInfo;
            Intrinsics.checkNotNull(accountCoreInfo7);
            Logger.d("xiaomiCoreInfo!!.nickName=  " + accountCoreInfo7.getNickName(), new Object[0]);
        }
        return Unit.INSTANCE;
    }

    private static final void initView$lambda$24(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackPersonalCenter();
        if (!mineFragment.getMAccoutManager().isLogin()) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).accountLogin();
            return;
        }
        SetPageManager setPageManageExtKt = SetPageManageExtKt.getInstance(SetPageManager.INSTANCE);
        FragmentActivity mActivity = mineFragment.getMActivity();
        Intrinsics.checkNotNull(mActivity);
        setPageManageExtKt.startUserIndicatorsActivity(mActivity);
    }

    private static final void initView$lambda$25(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsUpdateClick();
        AppUpgradeHelper appUpgradeHelper = mineFragment.getAppUpgradeHelper();
        FragmentManager childFragmentManager = mineFragment.getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
        appUpgradeHelper.doCheckAppUpgradeInMine(childFragmentManager);
    }

    private static final Unit initView$lambda$26(MineFragment mineFragment, RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        MineOneTrack.INSTANCE.trackMyFamily();
        if (!AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
            checkerUserSettingManagerExtKt.showLoginDialog(fragmentActivityRequireActivity);
            return Unit.INSTANCE;
        }
        mineFragment.oneTrackRelativeCareEntranceClick();
        IRelativeCareAccessApi instance = RelativeCareAccessExtKt.getINSTANCE(IRelativeCareAccessApi.INSTANCE);
        FragmentActivity fragmentActivityRequireActivity2 = mineFragment.requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity2, "requireActivity(...)");
        instance.goToRelativeCareHomePage(fragmentActivityRequireActivity2);
        return Unit.INSTANCE;
    }

    private static final Unit initView$lambda$27(MineFragment mineFragment, RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        MineOneTrack.INSTANCE.trackSettingsDataManagerClick();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            PrivacyHelper privacyHelperPointKt = PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
            privacyHelperPointKt.showPrivacySensitiveDialogIfNeed(fragmentActivityRequireActivity, new MineFragment$initView$5$1(mineFragment));
            return Unit.INSTANCE;
        }
        CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
        FragmentActivity fragmentActivityRequireActivity2 = mineFragment.requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity2, "requireActivity(...)");
        checkerUserSettingManagerExtKt.showLoginDialog(fragmentActivityRequireActivity2);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit initView$lambda$32(MineFragment mineFragment, MedalEntrance medalEntrance) {
        Intrinsics.checkNotNull(medalEntrance);
        if (medalEntrance.getMedal() == null) {
            return Unit.INSTANCE;
        }
        Medal medal = medalEntrance.getMedal();
        Intrinsics.checkNotNull(medal);
        int medalClass = medal.getMedalClass();
        ((mhh) mineFragment.getMBinding()).Q.getLayoutParams().width = (int) (DisplayUtil.dip2px(96.0f) / MineUtil.INSTANCE.getMedalImgRatio(medalClass));
        String icon = medal.getIcon();
        ImageView medalImg = ((mhh) mineFragment.getMBinding()).Q;
        Intrinsics.checkNotNullExpressionValue(medalImg, "medalImg");
        ImageLoader imageLoader = Coil.imageLoader(medalImg.getContext());
        ImageRequest.Builder builderTarget = new ImageRequest.Builder(medalImg.getContext()).data(icon).target(medalImg);
        builderTarget.bitmapConfig(Bitmap.Config.ARGB_8888);
        builderTarget.target(new MineFragment$initView$lambda$32$lambda$31$$inlined$target$default$1(mineFragment, medal, medalClass));
        imageLoader.enqueue(builderTarget.build());
        ((mhh) mineFragment.getMBinding()).S.setText(mineFragment.getString(ank.D, medal.getName()));
        return Unit.INSTANCE;
    }

    private static final void initView$lambda$34(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsFeedbackClick();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            FragmentManager parentFragmentManager = mineFragment.getParentFragmentManager();
            Intrinsics.checkNotNullExpressionValue(parentFragmentManager, "getParentFragmentManager(...)");
            UploadFileManagerExtKt.getInstance(UploadFileManager.INSTANCE).gotoFragmentManager(parentFragmentManager);
        } else {
            FragmentActivity mActivity = mineFragment.getMActivity();
            if (mActivity != null) {
                CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity);
            }
        }
    }

    private static final void initView$lambda$37(MineFragment mineFragment, View view) {
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            FragmentActivity mActivity = mineFragment.getMActivity();
            if (mActivity != null) {
                DeviceShareManager.gotoDeviceShareListActivity$default(DeviceShareManagerExtKt.getInstance(DeviceShareManager.INSTANCE), mActivity, null, 2, null);
                return;
            }
            return;
        }
        FragmentActivity mActivity2 = mineFragment.getMActivity();
        if (mActivity2 != null) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity2);
        }
    }

    private static final void initView$lambda$38(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsAppSetClick();
        AboutManager aboutManagerExtKt = AboutManagerExtKt.getInstance(AboutManager.INSTANCE);
        FragmentActivity mActivity = mineFragment.getMActivity();
        Intrinsics.checkNotNull(mActivity);
        aboutManagerExtKt.gotoSettingActivity(mActivity, false);
    }

    private static final void initView$lambda$40(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsAuthorizationManagementClick();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            XmsManagerExtKt.getInstance(XmsManager.INSTANCE).gotoDeviceAuthorizationPage(mineFragment.getMActivity());
            return;
        }
        FragmentActivity mActivity = mineFragment.getMActivity();
        if (mActivity != null) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity);
        }
    }

    private static final void initView$lambda$42(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSettingsSystemPermissionsClick();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            PrivacyHelper privacyHelperPointKt = PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE);
            Context contextRequireContext = mineFragment.requireContext();
            Intrinsics.checkNotNullExpressionValue(contextRequireContext, "requireContext(...)");
            privacyHelperPointKt.startSystemPermissionPage(contextRequireContext);
            return;
        }
        FragmentActivity mActivity = mineFragment.getMActivity();
        if (mActivity != null) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity);
        }
    }

    private static final void initView$lambda$44(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackSportsWeekly();
        FragmentActivity mActivity = mineFragment.getMActivity();
        if (mActivity != null) {
            if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                mineFragment.showWebSyncDialogIfNeed(new MineFragment$initView$12$1$1(mineFragment));
            } else {
                CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity);
            }
        }
    }

    private static final Unit initView$lambda$45(MineFragment mineFragment, RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        MineOneTrack.INSTANCE.trackOnlineConsultation();
        IHealthPage healthPageExtKt = HealthPageExtKt.getInstance(IHealthPage.INSTANCE);
        Context contextRequireContext = mineFragment.requireContext();
        Intrinsics.checkNotNullExpressionValue(contextRequireContext, "requireContext(...)");
        IHealthPage.goToHealthVisitDoctorPage$default(healthPageExtKt, contextRequireContext, 5, null, 4, null);
        return Unit.INSTANCE;
    }

    private static final void initView$lambda$46(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackMyCourse();
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            CourseApi.startCourseMe$default(CourseApiKt.getCourseApi(), null, 1, null);
            return;
        }
        CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
        FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        checkerUserSettingManagerExtKt.showLoginDialog(fragmentActivityRequireActivity);
    }

    private static final void initView$lambda$47(MineFragment mineFragment, View view) {
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            if (NetUtil.INSTANCE.getNetWorkAvailable(AppUtil.getApp())) {
                TrackNetManagerKt.getInstance(TrackNet.INSTANCE).goTrackLibraryPageDefault();
                return;
            } else {
                ToastUtil.INSTANCE.showToast(ank.P);
                return;
            }
        }
        CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
        FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        checkerUserSettingManagerExtKt.showLoginDialog(fragmentActivityRequireActivity);
    }

    private static final void initView$lambda$50(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackActivities();
        FragmentActivity mActivity = mineFragment.getMActivity();
        if (mActivity != null) {
            if (!AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
                CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(mActivity);
                return;
            }
            String activityHtmlUrlStaging = mineFragment.getMAccoutManager().isStaging() ? MineHtmlUrlsKt.getActivityHtmlUrlStaging() : MineHtmlUrlsKt.getActivityHtmlUrl();
            Logger.d("MineFragment", "pushUrl is " + activityHtmlUrlStaging);
            Intent intent = new Intent();
            Context context = mineFragment.getContext();
            if (context != null) {
                intent.setClass(context, HealthWebViewActivity.class);
            }
            intent.putExtra("data4", mineFragment.getString(ank.d));
            intent.putExtra(WebViewActivity.SHOW_TITLE_BAR, true);
            intent.putExtra(WebViewActivity.SHOW_SINGLE_TITLE_BAR, true);
            intent.putExtra("URL", activityHtmlUrlStaging);
            intent.putExtra(WebViewActivity.SHOW_TITLE_COLOR, e5k.d);
            mineFragment.startActivity(intent);
        }
    }

    private static final void initView$lambda$51(MineFragment mineFragment, View view) {
        MineOneTrack.INSTANCE.trackMyOrder();
        PaymentManager payManagerExtKt = PayManagerExtKt.getInstance(PaymentManager.INSTANCE);
        FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        PaymentManager.gotoOrderPageList$default(payManagerExtKt, fragmentActivityRequireActivity, null, null, 6, null);
    }

    public static /* synthetic */ void j0(Function1 function1, View view) {
        setTitleView$lambda$18(function1, view);
    }

    public static /* synthetic */ Unit k0(CompanyUserEntranceResult companyUserEntranceResult, MineFragment mineFragment, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return showAidongCompanyUserEntrance$lambda$54$lambda$53(companyUserEntranceResult, mineFragment, rightArrowBindingTwoLineTextView);
    }

    private final void loadMyHabitList() {
        HabitManagerKt.getHabitManager().loadMyHabitList(new khh(this), new lhh());
    }

    private static final Unit loadMyHabitList$lambda$13(MineFragment mineFragment, ArrayList it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Logger.i("MineFragment", "loadMyHabitList habit size:" + it.size());
        mineFragment.habitNum = it.size();
        return Unit.INSTANCE;
    }

    private static final Unit loadMyHabitList$lambda$14() {
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$2(MineFragment mineFragment, int i) {
        mineFragment.setTitleView();
        mineFragment.setTitleBarColor();
        return Unit.INSTANCE;
    }

    private final void oneTrackRelativeCareEntranceClick() {
        Map<String, ? extends Object> mapMapOf = MapsKt.mapOf(TuplesKt.to("name", "relatives_entry"), TuplesKt.to("device_model", this.curtDeviceModelStr), TuplesKt.to("source", "profile_tab_page"), TuplesKt.to("tip", "56.1.6.1.25014"));
        OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData("click_v3", mapMapOf);
        Logger.d("MineFragment", "oneTrackRelativeCareEntranceClick oneTrackMap=" + mapMapOf);
    }

    private final void oneTrackRelativeCareEntranceExpose() {
        Map<String, ? extends Object> mapMapOf = MapsKt.mapOf(TuplesKt.to("name", "relatives_entry"), TuplesKt.to("device_model", this.curtDeviceModelStr), TuplesKt.to("source", "profile_tab_page"), TuplesKt.to("tip", "56.1.6.1.25013"));
        OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData("expose_v3", mapMapOf);
        Logger.d("MineFragment", "oneTrackRelativeCareEntranceExpose oneTrackMap=" + mapMapOf);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void refreshPadding() {
        int dimen = (int) BaseFragmentExtKt.getDimen(this, d7k.f6783b);
        ViewGroup.LayoutParams layoutParams = ((mhh) getMBinding()).N.getLayoutParams();
        Intrinsics.checkNotNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.setMargins(dimen, marginLayoutParams.topMargin, dimen, marginLayoutParams.bottomMargin);
    }

    private final void setTitleBarColor() {
        BaseFragmentExtKt.setStatusBarColor(this, e5k.j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void setTitleView() {
        setActionBarDisplayable(false);
        ScrollTitleBar scrollTitleBar = ((mhh) getMBinding()).f0;
        NestedScrollView scrollView = ((mhh) getMBinding()).c0;
        Intrinsics.checkNotNullExpressionValue(scrollView, "scrollView");
        scrollTitleBar.bindScrollView(scrollView);
        ScrollTitleBar scrollTitleBar2 = ((mhh) getMBinding()).f0;
        String string = getString(ank.h0);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        scrollTitleBar2.setTitle(string);
        ((ImageView) ((mhh) getMBinding()).f0.findViewById(jdk.j1)).setContentDescription(getString(ank.f1629b));
        ((mhh) getMBinding()).j0.setSubtitle(AppUtil.getVersionName());
        Context context = getContext();
        if (context == null || !MineUtil.INSTANCE.isSystemNightMode(context)) {
            CheckerUserSettingManager.Companion companion = CheckerUserSettingManager.INSTANCE;
            if (CheckerUserSettingManagerExtKt.getInstance(companion).getSelectMode() == companion.getCONCISE_MODE()) {
                ((mhh) getMBinding()).W.setBackground(BaseFragmentExtKt.getDrawable(this, e5k.f));
                ((mhh) getMBinding()).V.setBackground(BaseFragmentExtKt.getDrawable(this, e5k.f));
                ((mhh) getMBinding()).f0.setCollapseBackground(e5k.f);
            } else {
                ((mhh) getMBinding()).V.setBackground(BaseFragmentExtKt.getDrawable(this, e5k.g));
                ((mhh) getMBinding()).f0.setCollapseBackground(e5k.g);
                ((mhh) getMBinding()).W.setBackground(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{BaseFragmentExtKt.getColor(this, e5k.h), BaseFragmentExtKt.getColor(this, e5k.g), BaseFragmentExtKt.getColor(this, e5k.g)}));
            }
        } else {
            ((mhh) getMBinding()).f0.setCollapseBackground(BaseFragmentExtKt.getDrawable(this, e5k.i));
            ((mhh) getMBinding()).W.setBackground(BaseFragmentExtKt.getDrawable(this, e5k.i));
            ((mhh) getMBinding()).V.setBackground(BaseFragmentExtKt.getDrawable(this, e5k.i));
        }
        lgh lghVar = new lgh(this);
        ((mhh) getMBinding()).U.setMenuClickListener(new wgh(lghVar));
        ((mhh) getMBinding()).f0.setOnFuncMoreClickListener(new fhh(lghVar));
    }

    private static final Unit setTitleView$lambda$17(MineFragment mineFragment, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (mineFragment.getActivity() != null) {
            BaseMainFragment.showPopupMenu$default(mineFragment, it, null, 2, null);
        }
        return Unit.INSTANCE;
    }

    private static final void setTitleView$lambda$18(Function1 function1, View view) {
        function1.invoke(view);
    }

    private static final void setTitleView$lambda$19(Function1 function1, View view) {
        function1.invoke(view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v20, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v24, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v30, types: [T, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v32, types: [T, java.lang.Object, java.lang.String] */
    private final void setUserInformation() {
        ((MineViewModel) getMViewModel()).updateUserProfile();
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = "";
        UserProfile profile = ((MineViewModel) getMViewModel()).getProfile();
        Intrinsics.checkNotNull(profile);
        if (StringsKt.equals$default(profile.getSex(), "female", false, 2, null)) {
            ?? string = getString(ank.o0);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            objectRef.element = string;
        } else {
            UserProfile profile2 = ((MineViewModel) getMViewModel()).getProfile();
            Intrinsics.checkNotNull(profile2);
            if (StringsKt.equals$default(profile2.getSex(), "male", false, 2, null)) {
                ?? string2 = getString(ank.v0);
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                objectRef.element = string2;
            }
        }
        Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = "  ";
        Ref.ObjectRef objectRef3 = new Ref.ObjectRef();
        objectRef3.element = "";
        Ref.ObjectRef objectRef4 = new Ref.ObjectRef();
        objectRef4.element = "";
        UserProfile profile3 = ((MineViewModel) getMViewModel()).getProfile();
        Intrinsics.checkNotNull(profile3);
        if (((int) profile3.getHeight()) != 0) {
            ?? string3 = getString(ank.k0);
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            objectRef3.element = string3;
            UserProfile profile4 = ((MineViewModel) getMViewModel()).getProfile();
            Intrinsics.checkNotNull(profile4);
            objectRef4.element = String.valueOf((int) profile4.getHeight());
        }
        Ref.ObjectRef objectRef5 = new Ref.ObjectRef();
        objectRef5.element = "";
        UserProfile profile5 = ((MineViewModel) getMViewModel()).getProfile();
        Intrinsics.checkNotNull(profile5);
        String birth = profile5.getBirth();
        if (birth != null && birth.length() != 0 && ((MineViewModel) getMViewModel()).getUserAge() != 0) {
            String strValueOf = String.valueOf(((MineViewModel) getMViewModel()).getUserAge());
            ?? string4 = getString(ank.j0, strValueOf);
            Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
            objectRef5.element = string4;
            Logger.d("MineFragment", "ageString is " + ((Object) string4) + " , age is " + strValueOf);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new ehh(this, objectRef, objectRef2, objectRef4, objectRef3, objectRef5));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final void setUserInformation$lambda$20(MineFragment mineFragment, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Ref.ObjectRef objectRef3, Ref.ObjectRef objectRef4, Ref.ObjectRef objectRef5) {
        TextView textView = ((mhh) mineFragment.getMBinding()).g0;
        Object obj = objectRef.element;
        Object obj2 = objectRef2.element;
        Object obj3 = objectRef3.element;
        Object obj4 = objectRef4.element;
        Object obj5 = objectRef5.element;
        StringBuilder sb = new StringBuilder();
        sb.append(obj);
        sb.append(obj2);
        sb.append(obj3);
        sb.append(obj4);
        sb.append(obj2);
        sb.append(obj5);
        textView.setText(sb.toString());
        ((mhh) mineFragment.getMBinding()).h0.setText(((MineViewModel) mineFragment.getMViewModel()).m510getUserName() != null ? ((MineViewModel) mineFragment.getMViewModel()).m510getUserName() : mineFragment.getString(ank.w0));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void showAidongCompanyUserEntrance() {
        if (AppUtil.INSTANCE.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            return;
        }
        ((MineViewModel) getMViewModel()).getCompanyUserEntranceLiveData().observe(getViewLifecycleOwner(), new MineFragment$sam$androidx_lifecycle_Observer$0(new dgh(this)));
        ((MineViewModel) getMViewModel()).requestCompanyEntrance();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit showAidongCompanyUserEntrance$lambda$54(MineFragment mineFragment, CompanyUserEntranceResult companyUserEntranceResult) {
        if (companyUserEntranceResult == null || !companyUserEntranceResult.getShow()) {
            RightArrowBindingTwoLineTextView companyExclusiveEntrance = ((mhh) mineFragment.getMBinding()).F;
            Intrinsics.checkNotNullExpressionValue(companyExclusiveEntrance, "companyExclusiveEntrance");
            ViewExtKt.gone(companyExclusiveEntrance);
            return Unit.INSTANCE;
        }
        RightArrowBindingTwoLineTextView companyExclusiveEntrance2 = ((mhh) mineFragment.getMBinding()).F;
        Intrinsics.checkNotNullExpressionValue(companyExclusiveEntrance2, "companyExclusiveEntrance");
        ViewExtKt.visible(companyExclusiveEntrance2);
        ViewExtKt.clickWithTrigger$default(((mhh) mineFragment.getMBinding()).F, 0L, new hhh(companyUserEntranceResult, mineFragment), 1, null);
        return Unit.INSTANCE;
    }

    private static final Unit showAidongCompanyUserEntrance$lambda$54$lambda$53(CompanyUserEntranceResult companyUserEntranceResult, MineFragment mineFragment, RightArrowBindingTwoLineTextView it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Logger.i("MineFragment", "jumpUrl=" + companyUserEntranceResult.getJumpUrl());
        if (!NetworkUtils.isNetworkConnected(ApplicationExtKt.getApplication())) {
            ToastExtKt.toastShort(ApplicationExtKt.getApplication(), ank.common_hint_network_unavailable);
            return Unit.INSTANCE;
        }
        CompanyUserWebViewActivity.Companion companion = CompanyUserWebViewActivity.INSTANCE;
        String jumpUrl = companyUserEntranceResult.getJumpUrl();
        FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
        Intrinsics.checkNotNull(fragmentActivityRequireActivity, "null cannot be cast to non-null type com.xiaomi.fitness.baseui.view.BaseActivity");
        companion.startAndSetFinishCallback(jumpUrl, (BaseActivity) fragmentActivityRequireActivity, new fgh(mineFragment));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit showAidongCompanyUserEntrance$lambda$54$lambda$53$lambda$52(MineFragment mineFragment) {
        ((MineViewModel) mineFragment.getMViewModel()).requestCompanyEntrance();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void showOrHideAidongCoursePaymentEntrance() {
        if (!whetherToShowAidongPaymentEntrance()) {
            RightArrowBindingTwoLineTextView motionSensingCourseMember = ((mhh) getMBinding()).X;
            Intrinsics.checkNotNullExpressionValue(motionSensingCourseMember, "motionSensingCourseMember");
            ViewExtKt.gone(motionSensingCourseMember);
        } else {
            RightArrowBindingTwoLineTextView motionSensingCourseMember2 = ((mhh) getMBinding()).X;
            Intrinsics.checkNotNullExpressionValue(motionSensingCourseMember2, "motionSensingCourseMember");
            ViewExtKt.visible(motionSensingCourseMember2);
            MineViewModel.sendGetAidongVipUseInfoRequestOnce$default((MineViewModel) getMViewModel(), null, null, 3, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void showPopMedalDialogIfNeed() {
        Logger.d("MineFragment", "showPopMedalDialogIfNeed shouldPullData = " + ((MineViewModel) getMViewModel()).shouldPullPopMedalDataFromNetwork());
        if (RegionPreference.INSTANCE.getSyncWeb()) {
            if (((MineViewModel) getMViewModel()).shouldPullPopMedalDataFromNetwork()) {
                MedalManagerExtKt.getInstance(MedalManager.INSTANCE).requestAllTypeMedalPopupList(new ihh(this));
            } else {
                handlePopMedalData();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit showPopMedalDialogIfNeed$lambda$55(MineFragment mineFragment, List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Logger.d("MineFragment", "showPopMedalDialogIfNeed popMedalDataList=" + it);
        if (it.isEmpty()) {
            ((MineViewModel) mineFragment.getMViewModel()).setShouldPullPopMedalDataFromNetwork();
        } else {
            ((MineViewModel) mineFragment.getMViewModel()).savePopMedalListToSp(it);
            ((MineViewModel) mineFragment.getMViewModel()).setPopMedalDataAlreadyPulled();
        }
        mineFragment.handlePopMedalData();
        return Unit.INSTANCE;
    }

    private final void showSyncWebDialog(IWebSyncImpl iWebSyncImpl) {
        this.mMainHelper.showSyncWebDialog(getMActivity(), new C29481(iWebSyncImpl));
        Logger.d("MineFragment", "SYNC WEB showIfNeed");
    }

    private final boolean showWebSyncDialogIfNeed(IWebSyncImpl iWebSyncImpl) {
        if (RegionPreference.INSTANCE.getSyncWeb()) {
            iWebSyncImpl.onWebSyncAccepted();
            return false;
        }
        showSyncWebDialog(iWebSyncImpl);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void startObservingPopMedalData() {
        ((MineViewModel) getMViewModel()).getMedalToPopLiveData().observe(this, new MineFragment$sam$androidx_lifecycle_Observer$0(new jhh(this)));
    }

    private static final Unit startObservingPopMedalData$lambda$1(MineFragment mineFragment, PopMedalData popMedalData) {
        Logger.d("MineFragment", "medalToPopLiveData value=" + popMedalData);
        if (popMedalData != null) {
            MedalManager medalManagerExtKt = MedalManagerExtKt.getInstance(MedalManager.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity = mineFragment.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
            medalManagerExtKt.showLatestMedalPopDialog(fragmentActivityRequireActivity, popMedalData, new bhh(mineFragment));
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit startObservingPopMedalData$lambda$1$lambda$0(MineFragment mineFragment) {
        ((MineViewModel) mineFragment.getMViewModel()).removeMedalAlreadyPoppedFromSp();
        if (((MineViewModel) mineFragment.getMViewModel()).popMedalListFromSpIsEmpty()) {
            ((MineViewModel) mineFragment.getMViewModel()).setShouldPullPopMedalDataFromNetwork();
        } else {
            ((MineViewModel) mineFragment.getMViewModel()).getMedalToPopLiveData().setValue(((MineViewModel) mineFragment.getMViewModel()).getFirstMedalFromPopMedalDataSP());
        }
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Unit w(MineFragment mineFragment, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView) {
        return initView$lambda$45(mineFragment, rightArrowBindingTwoLineTextView);
    }

    private final boolean whetherToShowAidongPaymentEntrance() {
        return !AppUtil.INSTANCE.isPlayChannel() && RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE)) && AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin() && IWebSyncKt.getSHOW_AIDONG_PAYMENT_ENTRANCE();
    }

    public static /* synthetic */ void x(BannerItem bannerItem, View view) {
        initBanner$lambda$5$lambda$4(bannerItem, view);
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.ViewDataBinder
    public /* bridge */ /* synthetic */ void bindView(i iVar) {
        bindView((mhh) iVar);
    }

    public final int darkColor(int normalColor, float darkRatio) {
        float[] fArr = new float[3];
        Color.colorToHSV(normalColor, fArr);
        fArr[2] = fArr[2] * darkRatio;
        return Color.HSVToColor(fArr);
    }

    @NotNull
    public final AppUpgradeHelper getAppUpgradeHelper() {
        AppUpgradeHelper appUpgradeHelper = this.appUpgradeHelper;
        if (appUpgradeHelper != null) {
            return appUpgradeHelper;
        }
        Intrinsics.throwUninitializedPropertyAccessException("appUpgradeHelper");
        return null;
    }

    public final int getColorWithAlpha(float alpha, int baseColor) {
        return (Math.min(255, Math.max(0, (int) (alpha * 255))) << 24) + (baseColor & 16777215);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.main.export.BaseMainFragment
    @Nullable
    public View getContentView() {
        return ((mhh) getMBinding()).N;
    }

    public final int getHabitNum() {
        return this.habitNum;
    }

    @NotNull
    public final AccountManager getMAccoutManager() {
        AccountManager accountManager = this.mAccoutManager;
        if (accountManager != null) {
            return accountManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mAccoutManager");
        return null;
    }

    @NotNull
    public final IMiAccountManager getMMiAccountManager() {
        IMiAccountManager iMiAccountManager = this.mMiAccountManager;
        if (iMiAccountManager != null) {
            return iMiAccountManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mMiAccountManager");
        return null;
    }

    public final boolean getMStartSportImport() {
        return this.mStartSportImport;
    }

    @Nullable
    public final AccountCoreInfo getXiaomiCoreInfo() {
        return this.xiaomiCoreInfo;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void initUserInfo() {
        ((MineViewModel) getMViewModel()).initUserProfile();
        UserProfile profile = ((MineViewModel) getMViewModel()).getProfile();
        Intrinsics.checkNotNull(profile);
        profile.getBirth();
        setUserInformation();
        if (AppUtil.INSTANCE.isPlayChannel() || !this.chinaRegion.equals(RegionExtKt.getInstance(RegionManager.INSTANCE).getCurrentRegion())) {
            ((mhh) getMBinding()).O.setVisibility(8);
        } else {
            ((mhh) getMBinding()).O.setVisibility(0);
            FragmentActivity mActivity = getMActivity();
            Intrinsics.checkNotNull(mActivity);
            new AnonymousClass1(mActivity, ((mhh) getMBinding()).O).setRadius(ExtUtilsKt.getDp(Float.valueOf(15.0f)));
        }
        ((mhh) getMBinding()).A.setOnClickListener(new chh(this));
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startObservingPopMedalData();
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (this.mSelectModeListener != null) {
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
            Function1<? super Integer, Unit> function1 = this.mSelectModeListener;
            Intrinsics.checkNotNull(function1);
            checkerUserSettingManagerExtKt.removeSelectModeListener(function1);
        }
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseFragment
    public void onInvisible() {
        super.onInvisible();
        long jCurrentTimeMillis = (System.currentTimeMillis() - this.mMinePageStartShowTime) / ((long) 1000);
        MainHelper mainHelper = this.mMainHelper;
        String str = this.MINE_TAB_PAGE_NAME;
        String string = getString(ank.h0);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        mainHelper.onetrackRecordTabShowTime(jCurrentTimeMillis, str, string);
        Logger.i("MineFragment", "mine page show durationSec is " + jCurrentTimeMillis);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        ((MineViewModel) getMViewModel()).getAccountCoreInfo();
        if (this.mStartSportImport) {
            ((MineViewModel) getMViewModel()).getMiSportLastMigrateStatus();
            this.mStartSportImport = false;
        }
        ((MineViewModel) getMViewModel()).getMedalEntrance();
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        setActionBarDisplayable(false);
        setTitleView();
        AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).addUserInfoChangeListener(new C29471());
        addPKEntranceOnClickListener();
        initView();
        initBanner();
        if (this.mSelectModeListener == null) {
            this.mSelectModeListener = new agh(this);
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
            Function1<? super Integer, Unit> function1 = this.mSelectModeListener;
            Intrinsics.checkNotNull(function1);
            checkerUserSettingManagerExtKt.addSelectModeListener(function1);
        }
        Logger.d("MineFragment", "MineFragment onViewCreated");
        initAidongPaymentEntrance();
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseFragment
    public void onVisible() {
        super.onVisible();
        setTitleBarColor();
        loadMyHabitList();
        this.mMinePageStartShowTime = System.currentTimeMillis();
        showPopMedalDialogIfNeed();
        if (!AppUtil.INSTANCE.isPlayChannel() && RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
            this.curtDeviceModelStr = wearableDeviceModel != null ? DeviceModelExtKt.getModel(wearableDeviceModel) : null;
            oneTrackRelativeCareEntranceExpose();
        }
        showOrHideAidongCoursePaymentEntrance();
    }

    public final void setAppUpgradeHelper(@NotNull AppUpgradeHelper appUpgradeHelper) {
        Intrinsics.checkNotNullParameter(appUpgradeHelper, "<set-?>");
        this.appUpgradeHelper = appUpgradeHelper;
    }

    public final void setHabitNum(int i) {
        this.habitNum = i;
    }

    public final void setMAccoutManager(@NotNull AccountManager accountManager) {
        Intrinsics.checkNotNullParameter(accountManager, "<set-?>");
        this.mAccoutManager = accountManager;
    }

    public final void setMMiAccountManager(@NotNull IMiAccountManager iMiAccountManager) {
        Intrinsics.checkNotNullParameter(iMiAccountManager, "<set-?>");
        this.mMiAccountManager = iMiAccountManager;
    }

    public final void setMStartSportImport(boolean z) {
        this.mStartSportImport = z;
    }

    public final void setXiaomiCoreInfo(@Nullable AccountCoreInfo accountCoreInfo) {
        this.xiaomiCoreInfo = accountCoreInfo;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void bindView(@NotNull mhh mhhVar) {
        Intrinsics.checkNotNullParameter(mhhVar, "<this>");
        ((mhh) getMBinding()).K.setVisibility(RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE)) ? 0 : 8);
        ((mhh) getMBinding()).K.setOnClickListener(new ghh(this));
    }
}
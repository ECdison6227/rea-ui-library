package com.xiaomi.fitness.mine.v4;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.MutableLiveData;
import com.fitness.task.export.ITask;
import com.fitness.task.export.TaskHelperExtKt;
import com.xiaomi.fitness.account.api.bean.UserProfile;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountCoreInfo;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.exception.export.ExceptionHelper;
import com.xiaomi.fitness.exception.export.ExceptionHelperExtKt;
import com.xiaomi.fitness.medal.export.IPopMedalListRepo;
import com.xiaomi.fitness.medal.export.PopMedalExtKt;
import com.xiaomi.fitness.medal.export.data.PopMedalData;
import com.xiaomi.fitness.mine.export.MineSettingManager;
import com.xiaomi.fitness.mine.export.MineSettingManagerExtKt;
import com.xiaomi.fitness.util.SportRecordBundleKey;
import com.xiaomi.smarthome.framework.plugin.rn.report.RnReportConst;
import com.xiaomi.wearable.yrn.export.YRNHelper;
import com.xiaomi.wearable.yrn.export.YRNHelperExtKt;
import defpackage.ohh;
import defpackage.phh;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0017\b\u0007\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\t\b\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\u001a\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\b\u0010\u0014\u001a\u00020\u000eH\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0016J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\u0010\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u000eH\u0016R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0018¨\u0006\u001f"}, d2 = {"Lcom/xiaomi/fitness/mine/v4/MineFragmentV4;", "Lcom/xiaomi/yrn/controller/ui/YRNCFragment;", "<init>", "()V", "minViewModel", "Lcom/xiaomi/fitness/mine/v4/MineV4ViewModel;", "getMinViewModel", "()Lcom/xiaomi/fitness/mine/v4/MineV4ViewModel;", "minViewModel$delegate", "Lkotlin/Lazy;", "medalToPopLiveData", "Landroidx/lifecycle/MutableLiveData;", "Lcom/xiaomi/fitness/medal/export/data/PopMedalData;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Landroid/view/View;", LifecycleConstantKt.ON_RESUME, LifecycleConstantKt.ON_STOP, "mUserInfoChangeListener", "com/xiaomi/fitness/mine/v4/MineFragmentV4$mUserInfoChangeListener$1", "Lcom/xiaomi/fitness/mine/v4/MineFragmentV4$mUserInfoChangeListener$1;", "onUserInfoChange", "onHiddenChanged", "hidden", "", LifecycleConstantKt.ON_DESTROY, "Companion", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nMineFragmentV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MineFragmentV4.kt\ncom/xiaomi/fitness/mine/v4/MineFragmentV4\n+ 2 FragmentViewModelLazy.kt\nandroidx/fragment/app/FragmentViewModelLazyKt\n*L\n1#1,161:1\n106#2,15:162\n*S KotlinDebug\n*F\n+ 1 MineFragmentV4.kt\ncom/xiaomi/fitness/mine/v4/MineFragmentV4\n*L\n44#1:162,15\n*E\n"})
public final class MineFragmentV4 extends Hilt_MineFragmentV4 {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    public static final String MINE_HOME_PATH = "mine/MineTab";

    @NotNull
    private static final String MODULE_NAME = "mine";

    @NotNull
    public static final String TAG = "MineFragmentV4";

    @NotNull
    private final MineFragmentV4$mUserInfoChangeListener$1 mUserInfoChangeListener;

    @NotNull
    private MutableLiveData<PopMedalData> medalToPopLiveData;

    /* JADX INFO: renamed from: minViewModel$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy minViewModel;

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\b\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/xiaomi/fitness/mine/v4/MineFragmentV4$Companion;", "", "<init>", "()V", "TAG", "", "MODULE_NAME", "MINE_HOME_PATH", "getFixedUrl", "mine_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ String access$getFixedUrl(Companion companion) {
            return companion.getFixedUrl();
        }

        private final String getFixedUrl() {
            YRNHelper.Companion companion = YRNHelper.INSTANCE;
            return YRNHelperExtKt.getInstance(companion).pageUrl(YRNHelperExtKt.getInstance(companion).appName(), MineFragmentV4.MINE_HOME_PATH);
        }

        private Companion() {
        }
    }

    public MineFragmentV4() {
        Lazy lazy = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new MineFragmentV4$special$$inlined$viewModels$default$2(new MineFragmentV4$special$$inlined$viewModels$default$1(this)));
        this.minViewModel = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(MineV4ViewModel.class), new MineFragmentV4$special$$inlined$viewModels$default$3(lazy), new MineFragmentV4$special$$inlined$viewModels$default$4(null, lazy), new MineFragmentV4$special$$inlined$viewModels$default$5(this, lazy));
        this.medalToPopLiveData = new MutableLiveData<>();
        this.mUserInfoChangeListener = new MineFragmentV4$mUserInfoChangeListener$1(this);
    }

    public static final /* synthetic */ MineV4ViewModel access$getMinViewModel(MineFragmentV4 mineFragmentV4) {
        return mineFragmentV4.getMinViewModel();
    }

    private final MineV4ViewModel getMinViewModel() {
        return (MineV4ViewModel) this.minViewModel.getValue();
    }

    private final void onUserInfoChange() {
        AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).addUserInfoChangeListener(this.mUserInfoChangeListener);
        getMinViewModel().getAccountCoreInfo().observe(getViewLifecycleOwner(), new MineFragmentV4$sam$androidx_lifecycle_Observer$0(new ohh()));
        getMinViewModel().getUserProfile().observe(getViewLifecycleOwner(), new MineFragmentV4$sam$androidx_lifecycle_Observer$0(new phh()));
    }

    private static final Unit onUserInfoChange$lambda$3(AccountCoreInfo accountCoreInfo) {
        String userId;
        Logger.d(TAG, "accountCoreInfo observe:" + (accountCoreInfo != null ? accountCoreInfo.getAvatarAddress() : null) + "  ---  " + (accountCoreInfo != null ? accountCoreInfo.getUserName() : null) + "   userId:" + (accountCoreInfo != null ? accountCoreInfo.getUserId() : null));
        MineSettingManager mineSettingManagerExtKt = MineSettingManagerExtKt.getInstance(MineSettingManager.INSTANCE);
        UserProfile userProfile$default = UserInfoManager.getUserProfile$default(AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE), false, 1, null);
        String userName = accountCoreInfo != null ? accountCoreInfo.getUserName() : null;
        String str = "";
        if ((userName == null || userName.length() == 0) && accountCoreInfo != null && (userId = accountCoreInfo.getUserId()) != null) {
            str = userId;
        }
        userProfile$default.setName(str);
        userProfile$default.setIcon(accountCoreInfo != null ? accountCoreInfo.getAvatarAddress() : null);
        mineSettingManagerExtKt.notifyUserInfoChanged(userProfile$default);
        return Unit.INSTANCE;
    }

    private static final Unit onUserInfoChange$lambda$4(UserProfile userProfile) {
        Logger.d(TAG, "userProfile observe:" + userProfile);
        MineSettingManagerExtKt.getInstance(MineSettingManager.INSTANCE).notifyUserInfoChanged(userProfile);
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Unit t(AccountCoreInfo accountCoreInfo) {
        return onUserInfoChange$lambda$3(accountCoreInfo);
    }

    public static /* synthetic */ Unit u(UserProfile userProfile) {
        return onUserInfoChange$lambda$4(userProfile);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putString("appName", YRNHelperExtKt.getInstance(YRNHelper.INSTANCE).appName());
            bundle.putString(RnReportConst.ApiReport.PARAM_MODULE, "mine");
            Companion companion = INSTANCE;
            bundle.putString(SportRecordBundleKey.BUNDLE_PARAM_URI, Companion.access$getFixedUrl(companion));
            bundle.putString("url", Companion.access$getFixedUrl(companion));
            setArguments(bundle);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                arguments.putString("appName", YRNHelperExtKt.getInstance(YRNHelper.INSTANCE).appName());
                arguments.putString(RnReportConst.ApiReport.PARAM_MODULE, "mine");
                Companion companion2 = INSTANCE;
                arguments.putString(SportRecordBundleKey.BUNDLE_PARAM_URI, Companion.access$getFixedUrl(companion2));
                arguments.putString("url", Companion.access$getFixedUrl(companion2));
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).removeUserInfoChangeListener(this.mUserInfoChangeListener);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            PopMedalExtKt.getInstance(IPopMedalListRepo.INSTANCE).showPopupDialogIfNeed(this.medalToPopLiveData);
            getMinViewModel().getDoctorDataResult();
        }
        String name = MineFragmentV4.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        TaskHelperExtKt.getInstance(ITask.INSTANCE).onPageShowChanged(!hidden, name);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        HashMap map = new HashMap();
        map.put("message", "RN Page Visible:" + this.mUrl);
        ExceptionHelper.addBreadcrumb$default(ExceptionHelperExtKt.getInstance(ExceptionHelper.INSTANCE), null, "navigation", map, 1, null);
        ITask taskHelperExtKt = TaskHelperExtKt.getInstance(ITask.INSTANCE);
        String name = MineFragmentV4.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        taskHelperExtKt.onPageShowChanged(true, name);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ITask taskHelperExtKt = TaskHelperExtKt.getInstance(ITask.INSTANCE);
        String name = MineFragmentV4.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        taskHelperExtKt.onPageShowChanged(false, name);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        onUserInfoChange();
        IPopMedalListRepo popMedalExtKt = PopMedalExtKt.getInstance(IPopMedalListRepo.INSTANCE);
        MutableLiveData<PopMedalData> mutableLiveData = this.medalToPopLiveData;
        FragmentActivity fragmentActivityRequireActivity = requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        popMedalExtKt.startObservingPopupMedalData(mutableLiveData, fragmentActivityRequireActivity);
    }
}
package com.xiaomi.fitness.sport.sporttab;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwnerKt;
import com.fitness.task.export.ITask;
import com.fitness.task.export.TaskHelperExtKt;
import com.xiaomi.fit.data.common.constant.FitnessDataConstants;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.exception.export.ExceptionHelper;
import com.xiaomi.fitness.exception.export.ExceptionHelperExtKt;
import com.xiaomi.fitness.main.export.ConfigPreference;
import com.xiaomi.fitness.net.i18n.I18nResourceLoader;
import com.xiaomi.fitness.res.preload.export.PreloadManagerExtKt;
import com.xiaomi.fitness.res.preload.export.ResPreloadManager;
import com.xiaomi.fitness.sport_export.ISportAccessApi;
import com.xiaomi.fitness.sport_export.SportAccessExportExtKt;
import com.xiaomi.fitness.util.SportRecordBundleKey;
import com.xiaomi.smarthome.framework.plugin.rn.report.RnReportConst;
import com.xiaomi.wearable.yrn.export.YRNHelper;
import com.xiaomi.wearable.yrn.export.YRNHelperExtKt;
import defpackage.mz4;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0007\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\t\b\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u001a\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\r\u001a\u00020\u0007H\u0002J\b\u0010\u000e\u001a\u00020\u0007H\u0002J\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0007H\u0016J\b\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0014\u001a\u00020\u0007H\u0002J\b\u0010\u0015\u001a\u00020\u0007H\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/xiaomi/fitness/sport/sporttab/SportTabV4Fragment;", "Lcom/xiaomi/yrn/controller/ui/YRNCFragment;", "<init>", "()V", "dataChangedReceiver", "Lcom/xiaomi/fitness/sport/sporttab/SportTabRNPageDataChangeReceiver;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "Landroid/view/View;", "registerDataChangedBroadcastReceiver", "unregisterChangeListeners", "onHiddenChanged", "hidden", "", LifecycleConstantKt.ON_RESUME, LifecycleConstantKt.ON_STOP, "ensureAntiCheatI18nResource", LifecycleConstantKt.ON_DESTROY, "Companion", "sport_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SportTabV4Fragment extends Hilt_SportTabV4Fragment {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final String MODULE_NAME = "sport";

    @NotNull
    public static final String SPORT_HOME_PATH = "sport/SportHome";

    @NotNull
    public static final String TAG = "SportTabFragment";

    @Nullable
    private SportTabRNPageDataChangeReceiver dataChangedReceiver;

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\b\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/xiaomi/fitness/sport/sporttab/SportTabV4Fragment$Companion;", "", "<init>", "()V", "TAG", "", "MODULE_NAME", "SPORT_HOME_PATH", "getFixedUrl", "sport_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final /* synthetic */ String access$getFixedUrl(Companion companion) {
            return companion.getFixedUrl();
        }

        private final String getFixedUrl() {
            YRNHelper.Companion companion = YRNHelper.INSTANCE;
            return YRNHelperExtKt.getInstance(companion).pageUrl(YRNHelperExtKt.getInstance(companion).appName(), SportTabV4Fragment.SPORT_HOME_PATH);
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.sport.sporttab.SportTabV4Fragment$ensureAntiCheatI18nResource$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.sport.sporttab.SportTabV4Fragment$ensureAntiCheatI18nResource$1", f = "SportTabV4Fragment.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $resourceId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(String str, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$resourceId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$resourceId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return invoke2(coroutineScope, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            I18nResourceLoader.INSTANCE.preload(this.$resourceId);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    private final void ensureAntiCheatI18nResource() {
        ConfigPreference configPreference = ConfigPreference.INSTANCE;
        String anti_cheat_text_resource_id = configPreference.getANTI_CHEAT_TEXT_RESOURCE_ID();
        if (anti_cheat_text_resource_id.length() == 0) {
            return;
        }
        I18nResourceLoader i18nResourceLoader = I18nResourceLoader.INSTANCE;
        if (i18nResourceLoader.isResourceExist(anti_cheat_text_resource_id)) {
            BuildersKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), Dispatchers.getIO(), null, new AnonymousClass1(anti_cheat_text_resource_id, null), 2, null);
            return;
        }
        String anti_cheat_text_url = configPreference.getANTI_CHEAT_TEXT_URL();
        if (anti_cheat_text_url.length() == 0) {
            return;
        }
        Logger.d("AntiCheat", "ensureAntiCheatI18nResource: resource not exist, downloading");
        I18nResourceLoader.download$default(i18nResourceLoader, anti_cheat_text_url, anti_cheat_text_resource_id, null, null, 12, null);
    }

    private final void registerDataChangedBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("recent_data_changed_broadcast");
        intentFilter.addAction(FitnessDataConstants.ACTION_DATA_LIST_CHANGED_BROADCAST);
        FragmentActivity fragmentActivityRequireActivity = requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        this.dataChangedReceiver = new SportTabRNPageDataChangeReceiver(fragmentActivityRequireActivity);
        if (Build.VERSION.SDK_INT >= 33) {
            requireActivity().registerReceiver(this.dataChangedReceiver, intentFilter, 4);
        } else {
            mz4.registerReceiver(requireActivity(), this.dataChangedReceiver, intentFilter, 4);
        }
    }

    private final void unregisterChangeListeners() {
        SportTabRNPageDataChangeReceiver sportTabRNPageDataChangeReceiver = this.dataChangedReceiver;
        if (sportTabRNPageDataChangeReceiver != null) {
            requireActivity().unregisterReceiver(sportTabRNPageDataChangeReceiver);
        }
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putString("appName", YRNHelperExtKt.getInstance(YRNHelper.INSTANCE).appName());
            bundle.putString(RnReportConst.ApiReport.PARAM_MODULE, "sport");
            Companion companion = INSTANCE;
            bundle.putString(SportRecordBundleKey.BUNDLE_PARAM_URI, Companion.access$getFixedUrl(companion));
            bundle.putString("url", Companion.access$getFixedUrl(companion));
            setArguments(bundle);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                arguments.putString("appName", YRNHelperExtKt.getInstance(YRNHelper.INSTANCE).appName());
                arguments.putString(RnReportConst.ApiReport.PARAM_MODULE, "sport");
                Companion companion2 = INSTANCE;
                arguments.putString(SportRecordBundleKey.BUNDLE_PARAM_URI, Companion.access$getFixedUrl(companion2));
                arguments.putString("url", Companion.access$getFixedUrl(companion2));
            }
        }
        super.onCreate(savedInstanceState);
        ISportAccessApi.checkAndDownloadAudioFeedbackResourcesIfNeed$default(SportAccessExportExtKt.getINSTANCE(ISportAccessApi.INSTANCE), false, null, null, null, 15, null);
        ensureAntiCheatI18nResource();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        unregisterChangeListeners();
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ISportAccessApi.checkAndDownloadAudioFeedbackResourcesIfNeed$default(SportAccessExportExtKt.getINSTANCE(ISportAccessApi.INSTANCE), false, null, null, null, 15, null);
            ensureAntiCheatI18nResource();
            PreloadManagerExtKt.getInstance(ResPreloadManager.INSTANCE).preloadIfNeed();
        }
        String name = SportTabV4Fragment.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        TaskHelperExtKt.getInstance(ITask.INSTANCE).onPageShowChanged(!hidden, name);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        HashMap map = new HashMap();
        map.put("message", "RN Page Visible:" + this.mUrl);
        ExceptionHelper.addBreadcrumb$default(ExceptionHelperExtKt.getInstance(ExceptionHelper.INSTANCE), null, "navigation", map, 1, null);
        boolean z = getUserVisibleHint() && !isHidden();
        ITask taskHelperExtKt = TaskHelperExtKt.getInstance(ITask.INSTANCE);
        String name = SportTabV4Fragment.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        taskHelperExtKt.onPageShowChanged(z, name);
    }

    @Override // com.xiaomi.yrn.controller.ui.YRNCFragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ITask taskHelperExtKt = TaskHelperExtKt.getInstance(ITask.INSTANCE);
        String name = SportTabV4Fragment.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        taskHelperExtKt.onPageShowChanged(false, name);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        registerDataChangedBroadcastReceiver();
    }
}
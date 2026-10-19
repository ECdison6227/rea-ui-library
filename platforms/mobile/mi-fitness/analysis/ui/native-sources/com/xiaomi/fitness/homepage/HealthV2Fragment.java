package com.xiaomi.fitness.homepage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.w;
import com.example.statistics.export.ScrollViewEffectiveExposeBiz;
import com.fitness.banner.export.v2.BannerItemV2;
import com.fitness.banner.export.view.AnnouncementBannerView;
import com.xiaomi.fit.data.common.constant.FitnessDataConstants;
import com.xiaomi.fit.data.common.data.sport.SportBasicReport;
import com.xiaomi.fit.data.common.log.FitnessLogUtils;
import com.xiaomi.fit.data.common.util.FitnessDateUtils;
import com.xiaomi.fit.fitness.export.api.repository.IRepositoryProvider;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyBasicReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyGoalReport;
import com.xiaomi.fit.fitness.export.data.annotation.HomeDataType;
import com.xiaomi.fit.fitness.export.di.FitnessDataExtKt;
import com.xiaomi.fit.fitness.parser.schema.DailyRecordKey;
import com.xiaomi.fit.fitness.sync.export.api.FitnessSyncer;
import com.xiaomi.fit.fitness.sync.export.di.FitnessSyncExtKt;
import com.xiaomi.fitness.account.api.bean.UserProfile;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.extensions.OnUserInfoChange;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.baseui.extension.BaseFragmentExtKt;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.date.LocalDate;
import com.xiaomi.fitness.common.extensions.ItemDecorationExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.recycer.GridItemDecoration;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.ExtUtilsKt;
import com.xiaomi.fitness.common.utils.SettingsPropertiesUtil;
import com.xiaomi.fitness.common.utils.TimeDateUtil;
import com.xiaomi.fitness.common.utils.UIUtils;
import com.xiaomi.fitness.component.StandardComponentEvent;
import com.xiaomi.fitness.data.HealthPrefer;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.extensions.ViewExtKt;
import com.xiaomi.fitness.health.curse.CurseManager;
import com.xiaomi.fitness.health.curse.DailyFemaleHealthReport;
import com.xiaomi.fitness.health.todayhealth.model.HealthCheckViewModel;
import com.xiaomi.fitness.homepage.banner.OperationalBannerAdapter;
import com.xiaomi.fitness.homepage.view.DataHeaderV2View;
import com.xiaomi.fitness.homepage.view.refresh.LoadAction;
import com.xiaomi.fitness.login.NeedLoginAspect;
import com.xiaomi.fitness.login.annotation.NeedLogin;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.CheckerUserSettingManagerExtKt;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.main.export.BaseMainFragment;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.fitness.main.export.MainHelper;
import com.xiaomi.fitness.medal.export.IPopMedalListRepo;
import com.xiaomi.fitness.medal.export.PopMedalExtKt;
import com.xiaomi.fitness.medal.export.data.PopMedalData;
import com.xiaomi.fitness.membership.export.MembershipAlipayTouch;
import com.xiaomi.fitness.membership.export.data.ActivitiesPromotionResult;
import com.xiaomi.fitness.membership.export.data.ActivitiesPromotionResultMine;
import com.xiaomi.fitness.net.extensions.ScopeExtKt;
import com.xiaomi.fitness.res.preload.export.PreloadManagerExtKt;
import com.xiaomi.fitness.res.preload.export.ResPreloadManager;
import com.xiaomi.fitness.settingitem.DeviceSettingManager;
import com.xiaomi.fitness.util.HealthUtil;
import com.xiaomi.fitness.v2.HealthAdapterV2;
import com.xiaomi.fitness.v2.HealthDataTypeUtilsV2;
import com.xiaomi.fitness.view.HealthAlipayTouchView;
import com.xiaomi.fitness.widget.view.HomeHeaderTabTitleView;
import defpackage.a0f;
import defpackage.a4d;
import defpackage.b4d;
import defpackage.bv9;
import defpackage.bxl;
import defpackage.c4d;
import defpackage.csc;
import defpackage.d4d;
import defpackage.hvj;
import defpackage.hza;
import defpackage.i66;
import defpackage.k96;
import defpackage.mz4;
import defpackage.n3d;
import defpackage.o3d;
import defpackage.ogk;
import defpackage.p3d;
import defpackage.q3d;
import defpackage.qmk;
import defpackage.r05;
import defpackage.r3d;
import defpackage.rm2;
import defpackage.s3d;
import defpackage.t3d;
import defpackage.u3d;
import defpackage.v3d;
import defpackage.w3d;
import defpackage.w4k;
import defpackage.x3d;
import defpackage.y3d;
import defpackage.y6k;
import defpackage.z3d;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import miuix.springback.view.SpringBackLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0086\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t*\u0004\u0096\u0001\u0099\u0001\b\u0007\u0018\u0000 ¨\u00012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u00042\u00020\u0005:\u0002¨\u0001B\t\b\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\t\u0010\u0007J\u0017\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\f\u0010\rJ\u000f\u0010\u000e\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000e\u0010\u0007J\u000f\u0010\u000f\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000f\u0010\u0007J\u000f\u0010\u0010\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0010\u0010\u0007J\u000f\u0010\u0011\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0011\u0010\u0007J\u000f\u0010\u0012\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0012\u0010\u0007J\u000f\u0010\u0013\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0013\u0010\u0007J\u000f\u0010\u0014\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0014\u0010\u0007J\u000f\u0010\u0015\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0015\u0010\u0007J\u000f\u0010\u0016\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0016\u0010\u0007J\u000f\u0010\u0017\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0017\u0010\u0007J\u000f\u0010\u0018\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0018\u0010\u0007J!\u0010\u001c\u001a\u00020\b2\u0010\b\u0002\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u0019H\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\u000f\u0010\u001e\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001e\u0010\u0007J\u000f\u0010\u001f\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001f\u0010\u0007J\u0017\u0010\"\u001a\u00020\b2\u0006\u0010!\u001a\u00020 H\u0002¢\u0006\u0004\b\"\u0010#J\u000f\u0010$\u001a\u00020\bH\u0002¢\u0006\u0004\b$\u0010\u0007J%\u0010)\u001a\u00020\b2\u0014\u0010(\u001a\u0010\u0012\u0004\u0012\u00020&\u0012\u0004\u0012\u00020'\u0018\u00010%H\u0002¢\u0006\u0004\b)\u0010*J\u000f\u0010+\u001a\u00020\bH\u0002¢\u0006\u0004\b+\u0010\u0007J\u0015\u0010,\u001a\b\u0012\u0004\u0012\u00020&0\u0019H\u0002¢\u0006\u0004\b,\u0010-J!\u00100\u001a\u00020\u001a2\u0006\u0010.\u001a\u00020&2\b\u0010/\u001a\u0004\u0018\u00010'H\u0002¢\u0006\u0004\b0\u00101J!\u00105\u001a\u00020\b2\u0006\u00102\u001a\u00020\u001a2\b\u00104\u001a\u0004\u0018\u000103H\u0002¢\u0006\u0004\b5\u00106J\u000f\u00107\u001a\u00020\bH\u0002¢\u0006\u0004\b7\u0010\u0007J\u000f\u00108\u001a\u00020\bH\u0002¢\u0006\u0004\b8\u0010\u0007J\u0017\u0010:\u001a\u00020\b2\u0006\u00109\u001a\u00020\u001aH\u0003¢\u0006\u0004\b:\u0010;J\u001d\u0010<\u001a\u00020\b2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0002¢\u0006\u0004\b<\u0010\u001dJ'\u0010@\u001a\u0002032\u0006\u0010>\u001a\u00020=2\u0006\u00109\u001a\u00020\u001a2\u0006\u0010?\u001a\u00020\u001aH\u0002¢\u0006\u0004\b@\u0010AJ\u000f\u0010B\u001a\u00020\bH\u0002¢\u0006\u0004\bB\u0010\u0007J\u0017\u0010E\u001a\u00020D2\u0006\u0010C\u001a\u00020\u001aH\u0002¢\u0006\u0004\bE\u0010FJ\u0017\u0010G\u001a\u00020\n2\u0006\u00102\u001a\u00020\u001aH\u0002¢\u0006\u0004\bG\u0010HJ\u000f\u0010I\u001a\u00020\bH\u0014¢\u0006\u0004\bI\u0010\u0007J\u000f\u0010J\u001a\u00020\bH\u0014¢\u0006\u0004\bJ\u0010\u0007J!\u0010N\u001a\u00020\b2\u0006\u0010L\u001a\u00020K2\b\u0010M\u001a\u0004\u0018\u00010DH\u0016¢\u0006\u0004\bN\u0010OJ\u000f\u0010P\u001a\u00020\bH\u0016¢\u0006\u0004\bP\u0010\u0007J\u000f\u0010Q\u001a\u00020\bH\u0016¢\u0006\u0004\bQ\u0010\u0007J\u000f\u0010R\u001a\u00020\bH\u0016¢\u0006\u0004\bR\u0010\u0007J)\u0010W\u001a\u00020\b2\u0006\u0010T\u001a\u00020S2\u0006\u0010C\u001a\u00020\u001a2\b\u0010V\u001a\u0004\u0018\u00010UH\u0016¢\u0006\u0004\bW\u0010XJ\u0017\u0010[\u001a\u00020\b2\u0006\u0010Z\u001a\u00020YH\u0016¢\u0006\u0004\b[\u0010\\J\u0011\u0010]\u001a\u0004\u0018\u00010KH\u0016¢\u0006\u0004\b]\u0010^R\u0018\u0010`\u001a\u0004\u0018\u00010_8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b`\u0010aR\u0018\u0010c\u001a\u0004\u0018\u00010b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bc\u0010dR\u0018\u0010f\u001a\u0004\u0018\u00010e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bf\u0010gR\u0018\u0010i\u001a\u0004\u0018\u00010h8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bi\u0010jR\u001a\u0010l\u001a\b\u0012\u0004\u0012\u0002030k8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bl\u0010mR \u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u0002030n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bo\u0010pR\u0016\u0010q\u001a\u00020=8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bq\u0010rR\u0018\u0010t\u001a\u0004\u0018\u00010s8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bt\u0010uR\u0018\u0010w\u001a\u0004\u0018\u00010v8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bw\u0010xR\u001e\u0010z\u001a\n\u0012\u0004\u0012\u00020 \u0018\u00010y8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bz\u0010{R\u0018\u0010|\u001a\u0004\u0018\u00010_8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b|\u0010aR\u001e\u0010\u0082\u0001\u001a\u00020}8BX\u0082\u0084\u0002¢\u0006\u000e\n\u0004\b~\u0010\u007f\u001a\u0006\b\u0080\u0001\u0010\u0081\u0001R\u001c\u0010\u0084\u0001\u001a\u0005\u0018\u00010\u0083\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0084\u0001\u0010\u0085\u0001R\u0019\u0010\u0086\u0001\u001a\u00020\n8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0086\u0001\u0010\u0087\u0001R(\u0010\u0088\u0001\u001a\u00020\n8\u0006@\u0006X\u0086\u000e¢\u0006\u0017\n\u0006\b\u0088\u0001\u0010\u0087\u0001\u001a\u0006\b\u0088\u0001\u0010\u0089\u0001\"\u0005\b\u008a\u0001\u0010\rR(\u0010\u008b\u0001\u001a\u00020\u001a8\u0006@\u0006X\u0086\u000e¢\u0006\u0017\n\u0006\b\u008b\u0001\u0010\u008c\u0001\u001a\u0006\b\u008d\u0001\u0010\u008e\u0001\"\u0005\b\u008f\u0001\u0010;R\u0019\u0010\u0090\u0001\u001a\u00020\n8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0090\u0001\u0010\u0087\u0001R \u0010\u0095\u0001\u001a\u00030\u0091\u00018BX\u0082\u0084\u0002¢\u0006\u000f\n\u0005\b\u0092\u0001\u0010\u007f\u001a\u0006\b\u0093\u0001\u0010\u0094\u0001R\u0018\u0010\u0097\u0001\u001a\u00030\u0096\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\b\u0097\u0001\u0010\u0098\u0001R\u0018\u0010\u009a\u0001\u001a\u00030\u0099\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\b\u009a\u0001\u0010\u009b\u0001R'\u0010\u009e\u0001\u001a\u0012\u0012\u0007\u0012\u0005\u0018\u00010\u009d\u0001\u0012\u0004\u0012\u00020\b0\u009c\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\b\u009e\u0001\u0010\u009f\u0001R\u001c\u0010¡\u0001\u001a\u0005\u0018\u00010 \u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b¡\u0001\u0010¢\u0001R*\u0010£\u0001\u001a\u0004\u0018\u00010 8\u0006@\u0006X\u0086\u000e¢\u0006\u0017\n\u0006\b£\u0001\u0010¤\u0001\u001a\u0006\b¥\u0001\u0010¦\u0001\"\u0005\b§\u0001\u0010#¨\u0006©\u0001"}, d2 = {"Lcom/xiaomi/fitness/homepage/HealthV2Fragment;", "Lcom/xiaomi/fitness/main/export/BaseMainFragment;", "Lcom/xiaomi/fitness/homepage/HealthViewModel;", "Lhza;", "Lcom/xiaomi/fitness/v2/HealthAdapterV2$OnGoPageClickListener;", "Lcom/xiaomi/fitness/health/curse/CurseManager$OnConfigChange;", "<init>", "()V", "", "refreshPage", "", "manualRefresh", "triggerDataSync", "(Z)V", "registerDataChangedBroadcast", "unregisterDataChangedBroadcast", "forbiddenRecyclerItemAnimation", "requestBannersV4", "requestAlipayTouchActivities", "requestOtherInfo", "requestOperationalBanners", "initRefreshLayout", "requestHealthTabBanner", "initUserGenderChange", "initTitleView", "", "", "displayList", "initDataItemCard", "(Ljava/util/List;)V", "checkLoginInfoAndNotifyChange", "bindFemaleHealthReport", "Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;", "report", "bindGoalReport", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;)V", "getAndDrawHomeData", "", "Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;", "", com.xiaomi.onetrack.i.a.C0896a.i, "startDrawWithFitnessData", "(Ljava/util/Map;)V", "upDateTodayHealth", "getSupportTodayHealthList", "()Ljava/util/List;", "homeType", "obj", "bindReportInner", "(Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;Ljava/lang/Object;)I", DailyRecordKey.sportType, "Li66;", "dataModelInsert", "notifyInsert", "(ILi66;)V", "setLayoutManager", "initResource", "type", "goCheckLoginUserInfo", "(I)V", "initDataSortMap", "", "time", "sortId", "refreshDataModelSortId", "(JII)Li66;", "changedDataModelSortId", "position", "Landroid/os/Bundle;", "createBundle", "(I)Landroid/os/Bundle;", "needCheckUserInfo", "(I)Z", "onVisible", "onInvisible", "Landroid/view/View;", "view", "savedInstanceState", "onViewCreated", "(Landroid/view/View;Landroid/os/Bundle;)V", LifecycleConstantKt.ON_DESTROY, "onDataChange", "onDestroyView", "Landroidx/recyclerview/widget/RecyclerView$d0;", "viewHolder", "Landroid/app/ActivityOptions;", "activityOptions", "onGoPage", "(Landroidx/recyclerview/widget/RecyclerView$d0;ILandroid/app/ActivityOptions;)V", "Landroid/content/res/Configuration;", StandardComponentEvent.KEY_NEW_CONFIG, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", "getContentView", "()Landroid/view/View;", "Landroidx/recyclerview/widget/RecyclerView;", "recycler", "Landroidx/recyclerview/widget/RecyclerView;", "Lcom/xiaomi/fitness/homepage/view/DataHeaderV2View;", "dataHeaderView", "Lcom/xiaomi/fitness/homepage/view/DataHeaderV2View;", "Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;", "header", "Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;", "Lcom/xiaomi/fitness/v2/HealthAdapterV2;", "mAdapter", "Lcom/xiaomi/fitness/v2/HealthAdapterV2;", "", "mDataList", "Ljava/util/List;", "", "mDataModelMap", "Ljava/util/Map;", "oneTrackStartTime", "J", "Lmiuix/springback/view/SpringBackLayout;", "swipeContainer", "Lmiuix/springback/view/SpringBackLayout;", "Lcom/fitness/banner/export/view/AnnouncementBannerView;", "announcementBannerView", "Lcom/fitness/banner/export/view/AnnouncementBannerView;", "Landroidx/lifecycle/LiveData;", "mGoalReportLivaData", "Landroidx/lifecycle/LiveData;", "heathBannerRecyclerView", "Lcom/xiaomi/fit/fitness/sync/export/api/FitnessSyncer;", "fitnessSyncer$delegate", "Lkotlin/Lazy;", "getFitnessSyncer", "()Lcom/xiaomi/fit/fitness/sync/export/api/FitnessSyncer;", "fitnessSyncer", "Lcom/xiaomi/fitness/account/extensions/OnUserInfoChange;", "mUserInfoChangeListener", "Lcom/xiaomi/fitness/account/extensions/OnUserInfoChange;", "shouldRefreshBanners", "Z", "isFemale", "()Z", "setFemale", "accountStatus", "I", "getAccountStatus", "()I", "setAccountStatus", "mDataChangedReceiverRegistered", "Lcom/xiaomi/fitness/health/todayhealth/model/HealthCheckViewModel;", "healthCheckViewModel$delegate", "getHealthCheckViewModel", "()Lcom/xiaomi/fitness/health/todayhealth/model/HealthCheckViewModel;", "healthCheckViewModel", "com/xiaomi/fitness/homepage/HealthV2Fragment$mDataChangedReceiver$1", "mDataChangedReceiver", "Lcom/xiaomi/fitness/homepage/HealthV2Fragment$mDataChangedReceiver$1;", "com/xiaomi/fitness/homepage/HealthV2Fragment$settingInfoListener$1", "settingInfoListener", "Lcom/xiaomi/fitness/homepage/HealthV2Fragment$settingInfoListener$1;", "Lkotlin/Function1;", "Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;", "deviceChangedListener", "Lkotlin/jvm/functions/Function1;", "Lcsc;", "mTrigger", "Lcsc;", "mDailyGoalReport", "Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;", "getMDailyGoalReport", "()Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;", "setMDailyGoalReport", "Companion", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nHealthV2Fragment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HealthV2Fragment.kt\ncom/xiaomi/fitness/homepage/HealthV2Fragment\n+ 2 FragmentViewModelLazy.kt\nandroidx/fragment/app/FragmentViewModelLazyKt\n+ 3 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n*L\n1#1,787:1\n106#2,15:788\n216#3,2:803\n1491#4:805\n1516#4,3:806\n1519#4,3:816\n382#5,7:809\n*S KotlinDebug\n*F\n+ 1 HealthV2Fragment.kt\ncom/xiaomi/fitness/homepage/HealthV2Fragment\n*L\n132#1:788,15\n673#1:803,2\n403#1:805\n403#1:806,3\n403#1:816,3\n403#1:809,7\n*E\n"})
public final class HealthV2Fragment extends Hilt_HealthV2Fragment<HealthViewModel, hza> implements HealthAdapterV2.OnGoPageClickListener, CurseManager.OnConfigChange {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE;

    @NotNull
    public static final String TAG = "HealthFragmentV2";
    private static /* synthetic */ Annotation ajc$anno$0;
    private static /* synthetic */ a0f.a ajc$tjp_0;
    private int accountStatus;

    @Nullable
    private AnnouncementBannerView announcementBannerView;

    @Nullable
    private DataHeaderV2View dataHeaderView;

    @NotNull
    private final Function1<WearableDeviceModel, Unit> deviceChangedListener;

    /* JADX INFO: renamed from: fitnessSyncer$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy fitnessSyncer;

    @Nullable
    private HomeHeaderTabTitleView header;

    /* JADX INFO: renamed from: healthCheckViewModel$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy healthCheckViewModel;

    @Nullable
    private RecyclerView heathBannerRecyclerView;
    private boolean isFemale;

    @Nullable
    private HealthAdapterV2 mAdapter;

    @Nullable
    private DailyGoalReport mDailyGoalReport;

    @NotNull
    private final HealthV2Fragment$mDataChangedReceiver$1 mDataChangedReceiver;
    private boolean mDataChangedReceiverRegistered;

    @NotNull
    private final List<i66> mDataList;

    @NotNull
    private final Map<Integer, i66> mDataModelMap;

    @Nullable
    private LiveData<DailyGoalReport> mGoalReportLivaData;

    @Nullable
    private csc mTrigger;

    @Nullable
    private OnUserInfoChange mUserInfoChangeListener;
    private long oneTrackStartTime;

    @Nullable
    private RecyclerView recycler;

    @NotNull
    private final HealthV2Fragment$settingInfoListener$1 settingInfoListener;
    private boolean shouldRefreshBanners;

    @Nullable
    private SpringBackLayout swipeContainer;

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/xiaomi/fitness/homepage/HealthV2Fragment$Companion;", "", "<init>", "()V", "TAG", "", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.HealthV2Fragment$initDataItemCard$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.homepage.HealthV2Fragment$initDataItemCard$1", f = "HealthV2Fragment.kt", i = {}, l = {459}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ List<Integer> $displayList;
        int label;
        final /* synthetic */ HealthV2Fragment this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(List<Integer> list, HealthV2Fragment healthV2Fragment, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$displayList = list;
            this.this$0 = healthV2Fragment;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$displayList, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return invoke2(coroutineScope, continuation);
        }

        /* JADX WARN: Code duplicated, block: B:20:0x006a  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            List<Integer> list;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                List<Integer> list2 = this.$displayList;
                Logger.d(HealthV2Fragment.TAG, "initDataItemCard invoke size(" + (list2 != null ? Boxing.boxInt(list2.size()) : null) + ")");
                list = this.$displayList;
                if (list == null) {
                    HealthDataTypeUtilsV2 healthDataTypeUtilsV2 = HealthDataTypeUtilsV2.INSTANCE;
                    this.label = 1;
                    obj = healthDataTypeUtilsV2.getHomeDisplayTypeList(this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                HealthV2Fragment.access$initDataSortMap(this.this$0, list);
                HealthV2Fragment.access$changedDataModelSortId(this.this$0);
                if (CurseManager.INSTANCE.getInstance().getSetting() != null) {
                    HealthV2Fragment.access$bindFemaleHealthReport(this.this$0);
                }
                return Unit.INSTANCE;
            }
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            list = (List) obj;
            HealthV2Fragment.access$initDataSortMap(this.this$0, list);
            HealthV2Fragment.access$changedDataModelSortId(this.this$0);
            if (CurseManager.INSTANCE.getInstance().getSetting() != null) {
                HealthV2Fragment.access$bindFemaleHealthReport(this.this$0);
            }
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.HealthV2Fragment$initUserGenderChange$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"com/xiaomi/fitness/homepage/HealthV2Fragment$initUserGenderChange$1", "Lcom/xiaomi/fitness/account/extensions/OnUserInfoChange;", "userInfoChange", "", "userProfile", "Lcom/xiaomi/fitness/account/api/bean/UserProfile;", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C28911 implements OnUserInfoChange {
        public C28911() {
        }

        @Override // com.xiaomi.fitness.account.extensions.OnUserInfoChange
        public void userInfoChange(UserProfile userProfile) {
            Intrinsics.checkNotNullParameter(userProfile, "userProfile");
            if (HealthV2Fragment.this.isInvalid()) {
                return;
            }
            Logger.d(HealthV2Fragment.TAG, "userInfoChange" + userProfile);
            boolean zAreEqual = Intrinsics.areEqual(userProfile.getSex(), "female");
            boolean z = HealthV2Fragment.this.getIsFemale() != zAreEqual;
            HealthV2Fragment.this.setFemale(zAreEqual);
            if (!z) {
                HealthV2Fragment.access$checkLoginInfoAndNotifyChange(HealthV2Fragment.this);
            } else {
                Logger.i(HealthV2Fragment.TAG, "isFemale status changed, and initDataItemCard");
                HealthV2Fragment.initDataItemCard$default(HealthV2Fragment.this, null, 1, null);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.HealthV2Fragment$setLayoutManager$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0017\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"com/xiaomi/fitness/homepage/HealthV2Fragment$setLayoutManager$1", "Landroidx/recyclerview/widget/GridLayoutManager$b;", "", "position", "getSpanSize", "(I)I", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C28921 extends GridLayoutManager.b {
        final /* synthetic */ int $spanCount;

        public C28921(int i) {
            this.$spanCount = i;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int getSpanSize(int position) {
            HealthAdapterV2 healthAdapterV2Access$getMAdapter$p = HealthV2Fragment.access$getMAdapter$p(HealthV2Fragment.this);
            Intrinsics.checkNotNull(healthAdapterV2Access$getMAdapter$p);
            if (!healthAdapterV2Access$getMAdapter$p.isHeaderView(position)) {
                HealthAdapterV2 healthAdapterV2Access$getMAdapter$p2 = HealthV2Fragment.access$getMAdapter$p(HealthV2Fragment.this);
                Intrinsics.checkNotNull(healthAdapterV2Access$getMAdapter$p2);
                if (!healthAdapterV2Access$getMAdapter$p2.isBottomView(position)) {
                    HealthAdapterV2 healthAdapterV2Access$getMAdapter$p3 = HealthV2Fragment.access$getMAdapter$p(HealthV2Fragment.this);
                    Intrinsics.checkNotNull(healthAdapterV2Access$getMAdapter$p3);
                    i66 itemAsPosition = healthAdapterV2Access$getMAdapter$p3.getItemAsPosition(position);
                    int i = itemAsPosition != null ? itemAsPosition.g : 1;
                    if (itemAsPosition != null && itemAsPosition.f8992b == 19) {
                        Context context = HealthV2Fragment.this.getContext();
                        i = (context == null || !UIUtils.INSTANCE.isTabletAndLandscape(context)) ? 2 : 4;
                    }
                    if (itemAsPosition != null && itemAsPosition.f8992b == 1) {
                        i = this.$spanCount;
                    }
                    int i2 = this.$spanCount;
                    return i > i2 ? i2 : i;
                }
            }
            return this.$spanCount;
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.HealthV2Fragment$triggerDataSync$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.homepage.HealthV2Fragment$triggerDataSync$1", f = "HealthV2Fragment.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    public static final class C28931 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ boolean $manualRefresh;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C28931(boolean z, Continuation<? super C28931> continuation) {
            super(2, continuation);
            this.$manualRefresh = z;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HealthV2Fragment.this.new C28931(this.$manualRefresh, continuation);
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
            HealthV2Fragment.access$getFitnessSyncer(HealthV2Fragment.this).triggerDataSync(this.$manualRefresh);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C28931) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    static {
        ajc$preClinit();
        INSTANCE = new Companion(null);
    }

    public HealthV2Fragment() {
        super(ogk.U, 0, true);
        this.mDataList = new ArrayList();
        this.mDataModelMap = new LinkedHashMap();
        this.fitnessSyncer = LazyKt.lazy(new b4d());
        this.isFemale = AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).isFemale();
        this.accountStatus = HealthUtil.INSTANCE.checkLoginAccountStatusInfo();
        Lazy lazy = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new HealthV2Fragment$special$$inlined$viewModels$default$2(new HealthV2Fragment$special$$inlined$viewModels$default$1(this)));
        this.healthCheckViewModel = FragmentViewModelLazyKt.createViewModelLazy(this, Reflection.getOrCreateKotlinClass(HealthCheckViewModel.class), new HealthV2Fragment$special$$inlined$viewModels$default$3(lazy), new HealthV2Fragment$special$$inlined$viewModels$default$4(null, lazy), new HealthV2Fragment$special$$inlined$viewModels$default$5(this, lazy));
        this.mDataChangedReceiver = new HealthV2Fragment$mDataChangedReceiver$1(this);
        this.settingInfoListener = new HealthV2Fragment$settingInfoListener$1(this);
        this.deviceChangedListener = new c4d(this);
    }

    public static /* synthetic */ void B(HealthV2Fragment healthV2Fragment) {
        onViewCreated$lambda$3(healthV2Fragment);
    }

    public static /* synthetic */ void C(HealthV2Fragment healthV2Fragment) {
        onVisible$lambda$1(healthV2Fragment);
    }

    public static /* synthetic */ Unit D(HealthV2Fragment healthV2Fragment) {
        return initRefreshLayout$lambda$11(healthV2Fragment);
    }

    public static /* synthetic */ Unit E(HealthV2Fragment healthV2Fragment, List list) {
        return requestHealthTabBanner$lambda$13(healthV2Fragment, list);
    }

    public static /* synthetic */ void F(HealthV2Fragment healthV2Fragment, View view) {
        initTitleView$lambda$16(healthV2Fragment, view);
    }

    public static /* synthetic */ void G(HealthV2Fragment healthV2Fragment) {
        onViewCreated$lambda$6(healthV2Fragment);
    }

    public static /* synthetic */ Unit H(HealthV2Fragment healthV2Fragment, WearableDeviceModel wearableDeviceModel) {
        return deviceChangedListener$lambda$2(healthV2Fragment, wearableDeviceModel);
    }

    public static /* synthetic */ Unit I(HealthV2Fragment healthV2Fragment, DailyGoalReport dailyGoalReport) {
        return getAndDrawHomeData$lambda$18(healthV2Fragment, dailyGoalReport);
    }

    public static /* synthetic */ FitnessSyncer J() {
        return fitnessSyncer_delegate$lambda$0();
    }

    public static /* synthetic */ Unit K(HealthV2Fragment healthV2Fragment, Map map) {
        return onViewCreated$lambda$7(healthV2Fragment, map);
    }

    public static /* synthetic */ Unit L(HealthV2Fragment healthV2Fragment, View view) {
        return onViewCreated$lambda$4(healthV2Fragment, view);
    }

    public static /* synthetic */ Unit M(HealthV2Fragment healthV2Fragment, List list) {
        return requestOperationalBanners$lambda$10(healthV2Fragment, list);
    }

    public static /* synthetic */ Unit N(HealthV2Fragment healthV2Fragment, ActivitiesPromotionResult activitiesPromotionResult) {
        return requestAlipayTouchActivities$lambda$9(healthV2Fragment, activitiesPromotionResult);
    }

    public static /* synthetic */ Unit O(HealthV2Fragment healthV2Fragment) {
        return onViewCreated$lambda$8(healthV2Fragment);
    }

    public static final /* synthetic */ void access$bindFemaleHealthReport(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.bindFemaleHealthReport();
    }

    public static final /* synthetic */ void access$changedDataModelSortId(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.changedDataModelSortId();
    }

    public static final /* synthetic */ void access$checkLoginInfoAndNotifyChange(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.checkLoginInfoAndNotifyChange();
    }

    public static final /* synthetic */ FitnessSyncer access$getFitnessSyncer(HealthV2Fragment healthV2Fragment) {
        return healthV2Fragment.getFitnessSyncer();
    }

    public static final /* synthetic */ HealthAdapterV2 access$getMAdapter$p(HealthV2Fragment healthV2Fragment) {
        return healthV2Fragment.mAdapter;
    }

    public static final /* synthetic */ csc access$getMTrigger$p(HealthV2Fragment healthV2Fragment) {
        return healthV2Fragment.mTrigger;
    }

    public static final /* synthetic */ void access$initDataItemCard(HealthV2Fragment healthV2Fragment, List list) {
        healthV2Fragment.initDataItemCard(list);
    }

    public static final /* synthetic */ void access$initDataSortMap(HealthV2Fragment healthV2Fragment, List list) {
        healthV2Fragment.initDataSortMap(list);
    }

    public static final /* synthetic */ void access$refreshPage(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.refreshPage();
    }

    private static /* synthetic */ void ajc$preClinit() {
        bv9 bv9Var = new bv9("HealthV2Fragment.kt", HealthV2Fragment.class);
        ajc$tjp_0 = bv9Var.h("method-execution", bv9Var.g("12", "goCheckLoginUserInfo", "com.xiaomi.fitness.homepage.HealthV2Fragment", "int", "type", "", "void"), 665);
    }

    private final void bindFemaleHealthReport() {
        Logger.d(TAG, "Female Health CurseManager bindFemaleHealthReport!");
        DailyFemaleHealthReport dailyFemaleHealthReportCreateDailyFemaleHealthReport = DailyFemaleHealthReport.INSTANCE.createDailyFemaleHealthReport(System.currentTimeMillis() / ((long) 1000), "home", "");
        Logger.i(TAG, "bindFemaleHealthReport " + dailyFemaleHealthReportCreateDailyFemaleHealthReport);
        dailyFemaleHealthReportCreateDailyFemaleHealthReport.setFemale(AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).isFemale());
        bindReportInner(HomeDataType.WOMEN_HEALTH, dailyFemaleHealthReportCreateDailyFemaleHealthReport);
    }

    private final void bindGoalReport(DailyGoalReport report) {
        DataHeaderV2View dataHeaderV2View = this.dataHeaderView;
        if (dataHeaderV2View != null) {
            dataHeaderV2View.bindGoalReport(report);
        }
    }

    private final int bindReportInner(HomeDataType homeType, Object obj) {
        if (obj != null) {
            Integer numConvertToHealthTypeInt = HealthDataTypeUtilsV2.INSTANCE.convertToHealthTypeInt(homeType);
            if (numConvertToHealthTypeInt == null) {
                Logger.i(TAG, "(" + homeType + ") convertToHealthTypeInt is null");
                return -1;
            }
            Logger.d(TAG, "bindReportInner sportType=" + numConvertToHealthTypeInt);
            i66 i66Var = this.mDataModelMap.get(numConvertToHealthTypeInt);
            if (i66Var != null) {
                if (obj instanceof DailyBasicReport) {
                    DailyBasicReport dailyBasicReport = (DailyBasicReport) obj;
                    i66Var.a = dailyBasicReport.getTime();
                    i66Var.d = dailyBasicReport.getSid();
                    i66Var.h = dailyBasicReport;
                    notifyInsert(numConvertToHealthTypeInt.intValue(), i66Var);
                } else if (obj instanceof SportBasicReport) {
                    SportBasicReport sportBasicReport = (SportBasicReport) obj;
                    i66Var.c = k96.a(sportBasicReport);
                    i66Var.d = sportBasicReport.getSid();
                    i66Var.a = sportBasicReport.getTimeStamp();
                    i66Var.i = sportBasicReport;
                    notifyInsert(numConvertToHealthTypeInt.intValue(), i66Var);
                }
            }
        }
        return 0;
    }

    private final void changedDataModelSortId() {
        if (!this.mDataModelMap.isEmpty()) {
            this.mDataList.clear();
            Iterator<Map.Entry<Integer, i66>> it = this.mDataModelMap.entrySet().iterator();
            while (it.hasNext()) {
                i66 value = it.next().getValue();
                if (value.e >= 0) {
                    this.mDataList.add(value);
                }
            }
            CollectionsKt.sort(this.mDataList);
        }
        HealthAdapterV2 healthAdapterV2 = this.mAdapter;
        if (healthAdapterV2 != null) {
            healthAdapterV2.setBottomCount(1);
        }
        HealthAdapterV2 healthAdapterV3 = this.mAdapter;
        Intrinsics.checkNotNull(healthAdapterV3);
        healthAdapterV3.notifyDataSetChanged();
    }

    private final void checkLoginInfoAndNotifyChange() {
        int iCheckLoginAccountStatusInfo;
        if (this.mDataModelMap.get(1) == null || (iCheckLoginAccountStatusInfo = HealthUtil.INSTANCE.checkLoginAccountStatusInfo()) == this.accountStatus) {
            return;
        }
        this.accountStatus = iCheckLoginAccountStatusInfo;
        initDataItemCard$default(this, null, 1, null);
    }

    private final Bundle createBundle(int position) {
        Bundle bundle = new Bundle();
        HealthAdapterV2 healthAdapterV2 = this.mAdapter;
        Intrinsics.checkNotNull(healthAdapterV2);
        i66 itemAsPosition = healthAdapterV2.getItemAsPosition(position);
        if (itemAsPosition != null) {
            if (!TextUtils.isEmpty(itemAsPosition.d)) {
                bundle.putString("did", itemAsPosition.d);
            }
            bundle.putSerializable("local_date", TimeDateUtil.timestampToLocalDate(itemAsPosition.a));
        }
        HealthAdapterV2 healthAdapterV3 = this.mAdapter;
        Intrinsics.checkNotNull(healthAdapterV3);
        bundle.putInt("sport_type", healthAdapterV3.getItemType(position));
        return bundle;
    }

    private static final Unit deviceChangedListener$lambda$2(HealthV2Fragment healthV2Fragment, WearableDeviceModel wearableDeviceModel) {
        FitnessLogUtils.i(TAG, "deviceChangedListener(" + (wearableDeviceModel != null ? wearableDeviceModel.getDid() : null) + ")");
        CurseManager.INSTANCE.getInstance().refreshDeviceSupportAlgoPredict(wearableDeviceModel != null ? wearableDeviceModel.getDid() : null);
        if (wearableDeviceModel != null && DeviceModelExtKt.isSupportEcgMeasureRemind(wearableDeviceModel)) {
            HealthPrefer.INSTANCE.setPREV_DEVICE_MODEL(wearableDeviceModel.getDid());
        }
        initDataItemCard$default(healthV2Fragment, null, 1, null);
        healthV2Fragment.shouldRefreshBanners = true;
        return Unit.INSTANCE;
    }

    private static final FitnessSyncer fitnessSyncer_delegate$lambda$0() {
        return FitnessSyncExtKt.getInstance(FitnessSyncer.INSTANCE);
    }

    private final void forbiddenRecyclerItemAnimation() {
        RecyclerView recyclerView = this.recycler;
        RecyclerView.l itemAnimator = recyclerView != null ? recyclerView.getItemAnimator() : null;
        if (itemAnimator == null || !(itemAnimator instanceof w)) {
            return;
        }
        ((w) itemAnimator).V(false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void getAndDrawHomeData() {
        if (isInvalid()) {
            return;
        }
        List<HomeDataType> requestHomeTypeList = HealthDataTypeUtilsV2.INSTANCE.getRequestHomeTypeList();
        FitnessLogUtils.i(TAG, "getAndDrawHomeData，" + requestHomeTypeList);
        LocalDate localDateNow = LocalDate.now();
        Intrinsics.checkNotNullExpressionValue(localDateNow, "now(...)");
        long jChangZeroOfTheDay = TimeDateUtil.changZeroOfTheDay(localDateNow);
        LiveData<DailyGoalReport> liveData = this.mGoalReportLivaData;
        if (liveData != null) {
            liveData.removeObservers(getViewLifecycleOwner());
        }
        LiveData<DailyGoalReport> liveDataTriggerFetchRainbowReport = ((HealthViewModel) getMViewModel()).triggerFetchRainbowReport(jChangZeroOfTheDay, false);
        this.mGoalReportLivaData = liveDataTriggerFetchRainbowReport;
        if (liveDataTriggerFetchRainbowReport != null) {
            liveDataTriggerFetchRainbowReport.observe(getViewLifecycleOwner(), new HealthV2Fragment$sam$androidx_lifecycle_Observer$0(new q3d(this)));
        }
        HealthViewModel healthViewModel = (HealthViewModel) getMViewModel();
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        Intrinsics.checkNotNullExpressionValue(viewLifecycleOwner, "getViewLifecycleOwner(...)");
        healthViewModel.loadOtherType(viewLifecycleOwner, new r3d(this));
        FitnessDataExtKt.getInstance(IRepositoryProvider.INSTANCE).getHomeDataRepository().triggerGetHomeData(requestHomeTypeList);
    }

    private static final Unit getAndDrawHomeData$lambda$18(HealthV2Fragment healthV2Fragment, DailyGoalReport dailyGoalReport) {
        Logger.i(TAG, "RainbowReport, mDailyGoalReport = " + dailyGoalReport);
        if (dailyGoalReport != null && TimeDateUtil.isToday(dailyGoalReport.getTime())) {
            DailyGoalReport dailyGoalReport2 = healthV2Fragment.mDailyGoalReport;
            if (dailyGoalReport2 == null) {
                healthV2Fragment.mDailyGoalReport = dailyGoalReport;
                healthV2Fragment.bindGoalReport(dailyGoalReport);
                DataHeaderV2View dataHeaderV2View = healthV2Fragment.dataHeaderView;
                if (dataHeaderV2View != null) {
                    dataHeaderV2View.refreshAnimation();
                }
            } else {
                Intrinsics.checkNotNull(dailyGoalReport2);
                if (!Intrinsics.areEqual(dailyGoalReport2, dailyGoalReport)) {
                    healthV2Fragment.mDailyGoalReport = dailyGoalReport;
                    healthV2Fragment.bindGoalReport(dailyGoalReport);
                    DataHeaderV2View dataHeaderV2View2 = healthV2Fragment.dataHeaderView;
                    if (dataHeaderV2View2 != null) {
                        dataHeaderV2View2.refreshAnimation();
                    }
                }
            }
        }
        return Unit.INSTANCE;
    }

    private static final Unit getAndDrawHomeData$lambda$19(HealthV2Fragment healthV2Fragment, DailyBasicReport it) {
        Intrinsics.checkNotNullParameter(it, "it");
        DataHeaderV2View dataHeaderV2View = healthV2Fragment.dataHeaderView;
        if (dataHeaderV2View != null) {
            dataHeaderV2View.bindOtherGoalReport(it);
        }
        return Unit.INSTANCE;
    }

    private final FitnessSyncer getFitnessSyncer() {
        return (FitnessSyncer) this.fitnessSyncer.getValue();
    }

    private final HealthCheckViewModel getHealthCheckViewModel() {
        return (HealthCheckViewModel) this.healthCheckViewModel.getValue();
    }

    private final List<HomeDataType> getSupportTodayHealthList() {
        HealthDataTypeUtilsV2 healthDataTypeUtilsV2 = HealthDataTypeUtilsV2.INSTANCE;
        HomeDataType homeDataType = HomeDataType.TODAY_HEALTH;
        return this.mDataModelMap.containsKey(healthDataTypeUtilsV2.convertToHealthTypeInt(homeDataType)) ? CollectionsKt.listOf((Object[]) new HomeDataType[]{HomeDataType.HR, HomeDataType.SLEEP, HomeDataType.SPO2, HomeDataType.STRESS, homeDataType}) : CollectionsKt.emptyList();
    }

    @NeedLogin(needCompleteUserInfo = true, needLogin = true, needLoginDialogGuide = false)
    private final void goCheckLoginUserInfo(int type) throws Throwable {
        a0f a0fVarC = bv9.c(ajc$tjp_0, this, this, r05.c(type));
        NeedLoginAspect needLoginAspectAspectOf = NeedLoginAspect.aspectOf();
        hvj hvjVarC = new d4d(new Object[]{this, r05.c(type), a0fVarC}).c(69648);
        Annotation annotation = ajc$anno$0;
        if (annotation == null) {
            annotation = HealthV2Fragment.class.getDeclaredMethod("goCheckLoginUserInfo", Integer.TYPE).getAnnotation(NeedLogin.class);
            ajc$anno$0 = annotation;
        }
        needLoginAspectAspectOf.aroundNeedLogin(hvjVarC, (NeedLogin) annotation);
    }

    public static final /* synthetic */ void goCheckLoginUserInfo_aroundBody0(HealthV2Fragment healthV2Fragment, int i, a0f a0fVar) {
        if (i == 1) {
            healthV2Fragment.checkLoginInfoAndNotifyChange();
        }
    }

    private final void initDataItemCard(List<Integer> displayList) {
        ScopeExtKt.scopeLife$default((Fragment) this, (Lifecycle.Event) null, (CoroutineDispatcher) null, (Function2) new AnonymousClass1(displayList, this, null), 3, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void initDataItemCard$default(HealthV2Fragment healthV2Fragment, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            list = null;
        }
        healthV2Fragment.initDataItemCard(list);
    }

    private final void initDataSortMap(List<Integer> displayList) {
        for (Map.Entry<Integer, i66> entry : this.mDataModelMap.entrySet()) {
            int i = entry.getValue().f8992b;
            if (i != 1 && i != 19 && !displayList.contains(Integer.valueOf(i))) {
                entry.getValue().e = -1;
            }
        }
        long todayZeroTimeInSec = FitnessDateUtils.getTodayZeroTimeInSec();
        int iCheckLoginAccountStatusInfo = HealthUtil.INSTANCE.checkLoginAccountStatusInfo();
        int i2 = 0;
        if (iCheckLoginAccountStatusInfo == 0) {
            i66 i66Var = this.mDataModelMap.get(1);
            if (i66Var != null) {
                i66Var.e = -1;
            }
        } else {
            refreshDataModelSortId(todayZeroTimeInSec, 1, 0).f8993k = iCheckLoginAccountStatusInfo;
        }
        Iterator<T> it = displayList.iterator();
        while (it.hasNext()) {
            refreshDataModelSortId(todayZeroTimeInSec, ((Number) it.next()).intValue(), i2 + 2);
            i2++;
        }
        if (displayList.contains(30000)) {
            upDateTodayHealth();
        }
    }

    private final void initRefreshLayout() {
        this.mTrigger = new csc(getContext());
        int i = qmk.nu;
        int i2 = qmk.kq;
        LoadAction loadAction = new LoadAction(ExtUtilsKt.getDp(34), new int[]{i, i2, i2, qmk.ou}, new o3d(this));
        csc cscVar = this.mTrigger;
        if (cscVar != null) {
            cscVar.d(loadAction);
        }
        csc cscVar2 = this.mTrigger;
        if (cscVar2 != null) {
            cscVar2.M(this.swipeContainer);
        }
    }

    private static final Unit initRefreshLayout$lambda$11(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.triggerDataSync(true);
        return Unit.INSTANCE;
    }

    private final void initResource() {
        setLayoutManager();
        FragmentActivity mActivity = getMActivity();
        Intrinsics.checkNotNull(mActivity);
        HealthAdapterV2 healthAdapterV2 = new HealthAdapterV2(mActivity, this.mDataList, getViewLifecycleOwner(), getHealthCheckViewModel());
        this.mAdapter = healthAdapterV2;
        Intrinsics.checkNotNull(healthAdapterV2);
        healthAdapterV2.setOnGoPageClickListener(this);
        RecyclerView recyclerView = this.recycler;
        Intrinsics.checkNotNull(recyclerView);
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView recyclerView2 = this.recycler;
        if (recyclerView2 != null) {
            recyclerView2.setItemViewCacheSize(16);
        }
    }

    private final void initTitleView() {
        HomeHeaderTabTitleView homeHeaderTabTitleView = this.header;
        if (homeHeaderTabTitleView != null) {
            String string = getString(qmk.mb);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            HomeHeaderTabTitleView.setTitle$default(homeHeaderTabTitleView, string, 0, 2, (Object) null);
        }
        HomeHeaderTabTitleView homeHeaderTabTitleView2 = this.header;
        if (homeHeaderTabTitleView2 != null) {
            homeHeaderTabTitleView2.setMenuClickListener(new p3d(this));
        }
    }

    private static final void initTitleView$lambda$16(HealthV2Fragment healthV2Fragment, View view) {
        Intrinsics.checkNotNull(view);
        BaseMainFragment.showPopupMenu$default(healthV2Fragment, view, null, 2, null);
    }

    private final void initUserGenderChange() {
        C28911 c28911 = new C28911();
        this.mUserInfoChangeListener = c28911;
        AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).addUserInfoChangeListener(c28911);
    }

    private final boolean needCheckUserInfo(int sportType) {
        return sportType == 3 || sportType == 2 || sportType == 15 || sportType == 4 || sportType == 1;
    }

    private final void notifyInsert(int sportType, i66 dataModelInsert) {
        if (dataModelInsert == null) {
            return;
        }
        int size = this.mDataList.size();
        for (int i = 0; i < size; i++) {
            if (this.mDataList.get(i).f8992b == sportType) {
                this.mDataList.set(i, dataModelInsert);
                HealthAdapterV2 healthAdapterV2 = this.mAdapter;
                if (healthAdapterV2 != null) {
                    int itemPosition = healthAdapterV2.getItemPosition(i);
                    HealthAdapterV2 healthAdapterV3 = this.mAdapter;
                    if (healthAdapterV3 != null) {
                        healthAdapterV3.notifyItemChanged(itemPosition);
                        return;
                    }
                    return;
                }
                return;
            }
        }
    }

    private static final void onViewCreated$lambda$3(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.getAndDrawHomeData();
    }

    private static final Unit onViewCreated$lambda$4(HealthV2Fragment healthV2Fragment, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        BaseMainFragment.showPopupMenu$default(healthV2Fragment, it, null, 2, null);
        return Unit.INSTANCE;
    }

    private static final void onViewCreated$lambda$5(Function1 function1, View view) {
        function1.invoke(view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final void onViewCreated$lambda$6(HealthV2Fragment healthV2Fragment) {
        ((HealthViewModel) healthV2Fragment.getMViewModel()).checkSportRecover(healthV2Fragment.getActivity());
    }

    private static final Unit onViewCreated$lambda$7(HealthV2Fragment healthV2Fragment, Map map) {
        Logger.d(TAG, ">>>> HomeData map = " + map);
        healthV2Fragment.startDrawWithFitnessData(map);
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$8(HealthV2Fragment healthV2Fragment) {
        healthV2Fragment.requestOtherInfo();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final void onVisible$lambda$1(HealthV2Fragment healthV2Fragment) {
        ((HealthViewModel) healthV2Fragment.getMViewModel()).getSchema();
    }

    private final i66 refreshDataModelSortId(long time, int type, int sortId) {
        i66 i66VarCreateDefaultDataModel = this.mDataModelMap.get(Integer.valueOf(type));
        if (i66VarCreateDefaultDataModel == null) {
            i66VarCreateDefaultDataModel = HealthUtil.INSTANCE.createDefaultDataModel(time, type, sortId);
        }
        i66VarCreateDefaultDataModel.e = sortId;
        this.mDataModelMap.put(Integer.valueOf(type), i66VarCreateDefaultDataModel);
        return i66VarCreateDefaultDataModel;
    }

    private final void refreshPage() {
        rm2.b bVarF;
        csc cscVar = this.mTrigger;
        if (cscVar != null && (bVarF = cscVar.f()) != null) {
            bVarF.notifyLoadComplete();
        }
        getAndDrawHomeData();
    }

    private final void registerDataChangedBroadcast() {
        if (this.mDataChangedReceiverRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("recent_data_changed_broadcast");
        intentFilter.addAction(FitnessDataConstants.ACTION_DATA_LIST_CHANGED_BROADCAST);
        if (Build.VERSION.SDK_INT >= 33) {
            requireActivity().registerReceiver(this.mDataChangedReceiver, intentFilter, 2);
        } else {
            mz4.registerReceiver(requireActivity(), this.mDataChangedReceiver, intentFilter, 2);
        }
        this.mDataChangedReceiverRegistered = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void requestAlipayTouchActivities() {
        if (getView() == null) {
            return;
        }
        if (!AppUtil.INSTANCE.isPlayChannel() && RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            ((HealthViewModel) getMViewModel()).queryPaymentActivities().observe(getViewLifecycleOwner(), new HealthV2Fragment$sam$androidx_lifecycle_Observer$0(new s3d(this)));
            return;
        }
        HealthAlipayTouchView alipayTouchView = ((hza) getMBinding()).A;
        Intrinsics.checkNotNullExpressionValue(alipayTouchView, "alipayTouchView");
        ViewExtKt.gone(alipayTouchView);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit requestAlipayTouchActivities$lambda$9(HealthV2Fragment healthV2Fragment, ActivitiesPromotionResult activitiesPromotionResult) {
        ActivitiesPromotionResultMine mine;
        ((hza) healthV2Fragment.getMBinding()).A.setAlipayTouchInfo(Integer.valueOf(MembershipAlipayTouch.INSTANCE.convertApiTypeToPayType((activitiesPromotionResult == null || (mine = activitiesPromotionResult.getMine()) == null) ? null : mine.getIdentity())));
        return Unit.INSTANCE;
    }

    private final void requestBannersV4() {
        requestHealthTabBanner();
        requestOperationalBanners();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void requestHealthTabBanner() {
        if (getView() == null) {
            return;
        }
        ((HealthViewModel) getMViewModel()).queryHealthTabBannerList().observe(getViewLifecycleOwner(), new HealthV2Fragment$sam$androidx_lifecycle_Observer$0(new a4d(this)));
    }

    private static final Unit requestHealthTabBanner$lambda$13(HealthV2Fragment healthV2Fragment, List list) {
        Intrinsics.checkNotNull(list);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            Integer numValueOf = Integer.valueOf(((BannerItemV2) obj).getResourceId());
            Object arrayList = linkedHashMap.get(numValueOf);
            if (arrayList == null) {
                arrayList = new ArrayList();
                linkedHashMap.put(numValueOf, arrayList);
            }
            ((List) arrayList).add(obj);
        }
        List<BannerItemV2> list2 = (List) linkedHashMap.get(1);
        Logger.d(TAG, "requestHealthTabBanner, list = " + list2);
        List<BannerItemV2> list3 = list2;
        if (list3 == null || list3.isEmpty()) {
            AnnouncementBannerView announcementBannerView = healthV2Fragment.announcementBannerView;
            if (announcementBannerView != null) {
                ViewExtKt.setVisible(announcementBannerView, false);
            }
        } else {
            AnnouncementBannerView announcementBannerView2 = healthV2Fragment.announcementBannerView;
            if (announcementBannerView2 != null) {
                ViewExtKt.setVisible(announcementBannerView2, true);
            }
            AnnouncementBannerView announcementBannerView3 = healthV2Fragment.announcementBannerView;
            if (announcementBannerView3 != null) {
                announcementBannerView3.updateAnnounceBannerList(list2);
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void requestOperationalBanners() {
        if (getView() == null) {
            return;
        }
        ((HealthViewModel) getMViewModel()).queryOperationalBannerList().observe(getViewLifecycleOwner(), new HealthV2Fragment$sam$androidx_lifecycle_Observer$0(new n3d(this)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit requestOperationalBanners$lambda$10(HealthV2Fragment healthV2Fragment, List list) {
        OperationalBannerAdapter operationalBannerAdapter = new OperationalBannerAdapter(healthV2Fragment);
        ((hza) healthV2Fragment.getMBinding()).F.setAdapter(operationalBannerAdapter);
        RecyclerView healthBannerRecyclerView = ((hza) healthV2Fragment.getMBinding()).F;
        Intrinsics.checkNotNullExpressionValue(healthBannerRecyclerView, "healthBannerRecyclerView");
        ItemDecorationExtKt.clearAllItemDecorations(healthBannerRecyclerView);
        ((hza) healthV2Fragment.getMBinding()).F.addItemDecoration(new bxl(ExtUtilsKt.getDp(12)));
        Intrinsics.checkNotNull(list);
        operationalBannerAdapter.bindData(list);
        return Unit.INSTANCE;
    }

    private final void requestOtherInfo() {
        requestBannersV4();
        requestAlipayTouchActivities();
    }

    private final void setLayoutManager() {
        int i;
        if (SettingsPropertiesUtil.INSTANCE.isFoldDisplayAndNotFolded()) {
            i = 3;
        } else {
            Context context = getContext();
            i = (context == null || !UIUtils.INSTANCE.isTabletAndLandscape(context)) ? 2 : 4;
        }
        int i2 = i;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getMActivity(), i2);
        gridLayoutManager.H(new C28921(i2));
        RecyclerView recyclerView = this.recycler;
        if (recyclerView != null) {
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        RecyclerView recyclerView2 = this.recycler;
        Intrinsics.checkNotNull(recyclerView2);
        ItemDecorationExtKt.clearAllItemDecorations(recyclerView2);
        Logger.i(TAG, "mDataList.size=" + this.mDataList.size());
        GridItemDecoration gridItemDecoration = new GridItemDecoration(this.mDataList.size(), i2, 0, ExtUtilsKt.getDp(10), 0, ExtUtilsKt.getDp(10));
        RecyclerView recyclerView3 = this.recycler;
        Intrinsics.checkNotNull(recyclerView3);
        recyclerView3.addItemDecoration(gridItemDecoration);
        RecyclerView recyclerView4 = this.recycler;
        Intrinsics.checkNotNull(recyclerView4);
        recyclerView4.requestLayout();
        RecyclerView recyclerView5 = this.recycler;
        Intrinsics.checkNotNull(recyclerView5);
        recyclerView5.invalidate();
    }

    private final void startDrawWithFitnessData(Map<HomeDataType, ? extends Object> dataMap) {
        if (dataMap == null || dataMap.isEmpty()) {
            return;
        }
        for (HomeDataType homeDataType : dataMap.keySet()) {
            Object obj = dataMap.get(homeDataType);
            Logger.i(TAG, "dataMap size:" + dataMap.size() + " " + homeDataType);
            bindReportInner(homeDataType, obj);
        }
        upDateTodayHealth();
    }

    private final void triggerDataSync(boolean manualRefresh) {
        FitnessLogUtils.i(TAG, "triggerDataSync: manual = " + manualRefresh);
        if (manualRefresh) {
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).getUserInfoFromWeb();
        }
        ScopeExtKt.scope(Dispatchers.getIO(), new C28931(manualRefresh, null));
    }

    private final void unregisterDataChangedBroadcast() {
        if (this.mDataChangedReceiverRegistered) {
            requireActivity().unregisterReceiver(this.mDataChangedReceiver);
            this.mDataChangedReceiverRegistered = false;
        }
    }

    private final void upDateTodayHealth() {
        notifyInsert(30000, this.mDataModelMap.get(30000));
    }

    public static /* synthetic */ Unit w(HealthV2Fragment healthV2Fragment, DailyBasicReport dailyBasicReport) {
        return getAndDrawHomeData$lambda$19(healthV2Fragment, dailyBasicReport);
    }

    public static /* synthetic */ void x(Function1 function1, View view) {
        onViewCreated$lambda$5(function1, view);
    }

    public final int getAccountStatus() {
        return this.accountStatus;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.main.export.BaseMainFragment
    @Nullable
    public View getContentView() {
        return ((hza) getMBinding()).C;
    }

    @Nullable
    public final DailyGoalReport getMDailyGoalReport() {
        return this.mDailyGoalReport;
    }

    /* JADX INFO: renamed from: isFemale, reason: from getter */
    public final boolean getIsFemale() {
        return this.isFemale;
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        requestOtherInfo();
        setLayoutManager();
    }

    @Override // com.xiaomi.fitness.health.curse.CurseManager.OnConfigChange
    public void onDataChange() {
        Logger.d(TAG, "CurseManager observer onDataChange!");
        bindFemaleHealthReport();
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        CurseManager.INSTANCE.getInstance().removeObserver(this);
        DeviceSettingManager.INSTANCE.getInstance().removeUpdateListener(this.settingInfoListener);
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).removeDeviceChangeListener(this.deviceChangedListener);
        OnUserInfoChange onUserInfoChange = this.mUserInfoChangeListener;
        if (onUserInfoChange != null) {
            AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).removeUserInfoChangeListener(onUserInfoChange);
        }
        super.onDestroy();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        unregisterDataChangedBroadcast();
        super.onDestroyView();
    }

    @Override // com.xiaomi.fitness.v2.HealthAdapterV2.OnGoPageClickListener
    public void onGoPage(@NotNull RecyclerView.d0 viewHolder, int position, @Nullable ActivityOptions activityOptions) throws Throwable {
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
        if (position == -1) {
            return;
        }
        Bundle bundleCreateBundle = createBundle(position);
        HealthAdapterV2 healthAdapterV2 = this.mAdapter;
        Intrinsics.checkNotNull(healthAdapterV2);
        int itemType = healthAdapterV2.getItemType(position);
        if (itemType == 1) {
            goCheckLoginUserInfo(itemType);
            return;
        }
        if (needCheckUserInfo(itemType)) {
            HealthUtil.INSTANCE.goDataPageCheckUserInfo(getContext(), itemType, bundleCreateBundle, activityOptions, HealthUtil.ORIGIN_HOME_CARD);
        } else if (itemType != 10000) {
            HealthUtil.INSTANCE.goDataPage(requireActivity(), getContext(), itemType, bundleCreateBundle, activityOptions, HealthUtil.ORIGIN_HOME_CARD);
        } else {
            bundleCreateBundle.putSerializable("local_date", LocalDate.now());
            HealthUtil.INSTANCE.goDataPage(requireActivity(), getContext(), itemType, bundleCreateBundle, activityOptions, HealthUtil.ORIGIN_HOME_CARD);
        }
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseFragment
    public void onInvisible() {
        super.onInvisible();
        long jCurrentTimeMillis = (System.currentTimeMillis() - this.oneTrackStartTime) / ((long) 1000);
        MainHelper mainExtKt = MainExtKt.getInstance(MainHelper.INSTANCE);
        String string = getString(qmk.mb);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        mainExtKt.onetrackRecordTabShowTime(jCurrentTimeMillis, "status_tab_page", string);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        this.recycler = ((hza) getMBinding()).G;
        forbiddenRecyclerItemAnimation();
        this.dataHeaderView = ((hza) getMBinding()).D;
        this.swipeContainer = ((hza) getMBinding()).I;
        this.header = ((hza) getMBinding()).E;
        this.announcementBannerView = ((hza) getMBinding()).B;
        this.heathBannerRecyclerView = ((hza) getMBinding()).F;
        initResource();
        initTitleView();
        initDataItemCard$default(this, null, 1, null);
        initRefreshLayout();
        new Handler(Looper.getMainLooper()).postDelayed(new u3d(this), 300L);
        registerDataChangedBroadcast();
        DeviceSettingManager.INSTANCE.getInstance().addUpdateListener(this.settingInfoListener);
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).addDeviceChangeListener(this.deviceChangedListener);
        CurseManager.INSTANCE.getInstance().addDataObserver(this);
        v3d v3dVar = new v3d(this);
        DataHeaderV2View dataHeaderV2View = this.dataHeaderView;
        if (dataHeaderV2View != null) {
            dataHeaderV2View.setAddDevListener(new w3d(v3dVar));
        }
        initUserGenderChange();
        getRootView().postDelayed(new x3d(this), 1000L);
        FitnessDataExtKt.getInstance(IRepositoryProvider.INSTANCE).getHomeDataRepository().getHomeLiveData().observe(getViewLifecycleOwner(), new HealthV2Fragment$sam$androidx_lifecycle_Observer$0(new y3d(this)));
        IPopMedalListRepo popMedalExtKt = PopMedalExtKt.getInstance(IPopMedalListRepo.INSTANCE);
        MutableLiveData<PopMedalData> medalToPopLiveData = ((HealthViewModel) getMViewModel()).getMedalToPopLiveData();
        FragmentActivity fragmentActivityRequireActivity = requireActivity();
        Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
        popMedalExtKt.startObservingPopupMedalData(medalToPopLiveData, fragmentActivityRequireActivity);
        int dimen = (int) BaseFragmentExtKt.getDimen(this, y6k.i);
        ScrollViewEffectiveExposeBiz scrollViewEffectiveExposeBiz = new ScrollViewEffectiveExposeBiz();
        NestedScrollView scrollView = ((hza) getMBinding()).H;
        Intrinsics.checkNotNullExpressionValue(scrollView, "scrollView");
        scrollViewEffectiveExposeBiz.startTrackItemExpose(scrollView, new LinkedHashSet(), dimen);
        HealthUtil.INSTANCE.listenOnLogInAndProceed(new WeakReference<>(new z3d(this)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseFragment
    public void onVisible() {
        super.onVisible();
        BaseFragmentExtKt.setStatusBarColor(this, w4k.Z3);
        this.oneTrackStartTime = System.currentTimeMillis();
        triggerDataSync(false);
        getRootView().postDelayed(new t3d(this), 1000L);
        PopMedalExtKt.getInstance(IPopMedalListRepo.INSTANCE).showPopupDialogIfNeed(((HealthViewModel) getMViewModel()).getMedalToPopLiveData());
        if (this.shouldRefreshBanners) {
            requestBannersV4();
            this.shouldRefreshBanners = false;
        }
        PreloadManagerExtKt.getInstance(ResPreloadManager.INSTANCE).preloadIfNeed();
        requestAlipayTouchActivities();
    }

    public final void setAccountStatus(int i) {
        this.accountStatus = i;
    }

    public final void setFemale(boolean z) {
        this.isFemale = z;
    }

    public final void setMDailyGoalReport(@Nullable DailyGoalReport dailyGoalReport) {
        this.mDailyGoalReport = dailyGoalReport;
    }
}
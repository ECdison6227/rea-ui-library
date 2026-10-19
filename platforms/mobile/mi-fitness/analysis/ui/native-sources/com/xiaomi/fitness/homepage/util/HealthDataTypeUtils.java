package com.xiaomi.fitness.homepage.util;

import com.mi.physical_status.utils.PhysicalStatusDataUtils;
import com.mi.physical_status_export.data.TrainState;
import com.xiaomi.fit.data.common.log.FitnessLogUtils;
import com.xiaomi.fit.fitness.export.api.FitnessDataGetter;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyBasicReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyPhysicalStatusReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyRunningIndicatorReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyTrainingLoadReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyVo2MaxReport;
import com.xiaomi.fit.fitness.export.data.annotation.HomeDataType;
import com.xiaomi.fit.fitness.export.data.item.PhysicalStatusItem;
import com.xiaomi.fit.fitness.export.data.item.RunningIndicatorItem;
import com.xiaomi.fit.fitness.export.data.item.Vo2MaxItem;
import com.xiaomi.fit.fitness.export.di.FitnessDataExtKt;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.baseui.widget.LevelBarView;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.ResourceExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.TimeDateUtil;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.export.bean.Feature;
import com.xiaomi.fitness.health.physicalstatus.PhysicalStatusDataUtil;
import com.xiaomi.fitness.health.runningindicator.RunningIndicatorUtil;
import com.xiaomi.fitness.health.threetarget.model.TrainingIndexDataItem;
import com.xiaomi.fitness.health.trainload.TrainingLoadDataUtil;
import com.xiaomi.fitness.settingitem.DeviceSettingConfigKey;
import com.xiaomi.fitness.settingitem.DeviceSettingManager;
import com.xiaomi.fitness.settingitem.settingitem.HealthData;
import com.xiaomi.fitness.settingitem.settingitem.HealthDataSetting;
import com.xiaomi.fitness.util.VO2MaxDataUtil;
import defpackage.i66;
import defpackage.qmk;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000f\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0017\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0017\u0010\r\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\r\u0010\fJ\u000f\u0010\u000e\u001a\u00020\nH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u0017\u0010\u0010\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\u0010\u0010\fJ\u001b\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u0011H\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0011\u0010\u0015\u001a\u0004\u0018\u00010\u0014H\u0002¢\u0006\u0004\b\u0015\u0010\u0016J#\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u00172\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\nH\u0002¢\u0006\u0004\b\u001b\u0010\u001cJ\u0019\u0010\u001e\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u001d\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001e\u0010\u001fJ\u0019\u0010 \u001a\u0004\u0018\u00010\u00172\u0006\u0010\u001d\u001a\u00020\bH\u0002¢\u0006\u0004\b \u0010\u001fJ\u0019\u0010!\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0018\u001a\u00020\u0017H\u0002¢\u0006\u0004\b!\u0010\"J\u0015\u0010#\u001a\b\u0012\u0004\u0012\u00020\b0\u0004H\u0002¢\u0006\u0004\b#\u0010\u0007J\u0016\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0086@¢\u0006\u0004\b$\u0010%J\u0016\u0010&\u001a\b\u0012\u0004\u0012\u00020\b0\u0004H\u0086@¢\u0006\u0004\b&\u0010%J\u0013\u0010(\u001a\b\u0012\u0004\u0012\u00020'0\u0004¢\u0006\u0004\b(\u0010\u0007J \u0010-\u001a\u00020,2\u0006\u0010)\u001a\u00020'2\u0006\u0010+\u001a\u00020*H\u0086@¢\u0006\u0004\b-\u0010.J\u001b\u00102\u001a\u0002012\f\u00100\u001a\b\u0012\u0004\u0012\u00020/0\u0004¢\u0006\u0004\b2\u00103J\u0019\u00105\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\bH\u0000¢\u0006\u0004\b4\u0010\u001fJ\u0019\u00107\u001a\u0004\u0018\u00010\b2\b\u00106\u001a\u0004\u0018\u00010'¢\u0006\u0004\b7\u00108J\u0017\u00109\u001a\u0004\u0018\u00010'2\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b9\u0010:J\u0013\u0010;\u001a\b\u0012\u0004\u0012\u00020'0\u0004¢\u0006\u0004\b;\u0010\u0007R\u0014\u0010<\u001a\u00020\u00178\u0002X\u0082T¢\u0006\u0006\n\u0004\b<\u0010=R\u001e\u0010>\u001a\n\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b>\u0010?¨\u0006@"}, d2 = {"Lcom/xiaomi/fitness/homepage/util/HealthDataTypeUtils;", "", "<init>", "()V", "", "Lcom/xiaomi/fitness/homepage/util/HealthTypeBean;", "getAllSupportDataTypeList", "()Ljava/util/List;", "", "healthType", "", "isDefaultDisplayDataType", "(I)Z", "supportTheDataTypeInBoundDevices", "isSupportPhysicalStatus", "()Z", "hasDataOfTheDataType", "", "getCloudHealthTypeInfo", "()Ljava/util/Map;", "Lcom/xiaomi/fitness/settingitem/settingitem/HealthDataSetting;", "getCloudHealthDataSetting", "()Lcom/xiaomi/fitness/settingitem/settingitem/HealthDataSetting;", "", "type", "enable", "Lcom/xiaomi/fitness/settingitem/settingitem/HealthData;", "createHealthData", "(Ljava/lang/String;Ljava/lang/Boolean;)Lcom/xiaomi/fitness/settingitem/settingitem/HealthData;", "itemType", "getDeviceFeatureByType", "(I)Ljava/lang/String;", "getPersistKeyByType", "convertToDataTypeInt", "(Ljava/lang/String;)Ljava/lang/Integer;", "getAllDataItemTypes", "getAllDataTypeList", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHomeDisplayTypeList", "Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;", "getSupportiveHomeDataTypeListForTrainingIndex", "homeDataType", "Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyBasicReport;", "report", "Lcom/xiaomi/fitness/health/threetarget/model/TrainingIndexDataItem;", "getTrainingIndexDataItem", "(Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyBasicReport;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Li66;", "dataList", "", "saveHealthDataSettingToCloud", "(Ljava/util/List;)V", "convertToDataTypeStr$health_chinaProductRelease", "convertToDataTypeStr", "homeType", "convertToHealthTypeInt", "(Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;)Ljava/lang/Integer;", "convertToHomeDataType", "(I)Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;", "getRequestHomeTypeList", "TAG", "Ljava/lang/String;", "manualDataTypeList", "Ljava/util/List;", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nHealthDataTypeUtils.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HealthDataTypeUtils.kt\ncom/xiaomi/fitness/homepage/util/HealthDataTypeUtils\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,731:1\n1869#2,2:732\n1#3:734\n*S KotlinDebug\n*F\n+ 1 HealthDataTypeUtils.kt\ncom/xiaomi/fitness/homepage/util/HealthDataTypeUtils\n*L\n438#1:732,2\n*E\n"})
public final class HealthDataTypeUtils {

    @NotNull
    public static final HealthDataTypeUtils INSTANCE = new HealthDataTypeUtils();

    @NotNull
    private static final String TAG = "HealthDataTypeUtils";

    @Nullable
    private static List<? extends HealthData> manualDataTypeList;

    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[HomeDataType.values().length];
            try {
                iArr[HomeDataType.V02_MAX.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[HomeDataType.TRAINING_LOAD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[HomeDataType.RUNNING_INDICATOR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[HomeDataType.PHYSICAL_STATUS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[HomeDataType.PHYSICAL_STATUS_V2.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[HomeDataType.STEP.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[HomeDataType.CALORIE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[HomeDataType.VALID_STAND.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr[HomeDataType.MH_STRENGTH.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr[HomeDataType.SLEEP.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr[HomeDataType.ENERGY.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr[HomeDataType.HEARING_HEALTH.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr[HomeDataType.PAI.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                iArr[HomeDataType.WOMEN_HEALTH.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                iArr[HomeDataType.HR.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                iArr[HomeDataType.SPO2.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                iArr[HomeDataType.STRESS.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                iArr[HomeDataType.SPORT_REPORT.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                iArr[HomeDataType.WEIGHT.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                iArr[HomeDataType.BLOOD_PRESSURE.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                iArr[HomeDataType.TEMPERATURE.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                iArr[HomeDataType.RAINBOW.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                iArr[HomeDataType.VITALITY.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                iArr[HomeDataType.ECG_MEDICAL.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                iArr[HomeDataType.BLOOD_SUGAR.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.util.HealthDataTypeUtils$getAllDataTypeList$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "Lcom/xiaomi/fitness/homepage/util/HealthTypeBean;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.homepage.util.HealthDataTypeUtils$getAllDataTypeList$2", f = "HealthDataTypeUtils.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends HealthTypeBean>>, Object> {
        int label;

        public AnonymousClass2(Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends HealthTypeBean>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<HealthTypeBean>>) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            return HealthDataTypeUtils.access$getAllSupportDataTypeList(HealthDataTypeUtils.INSTANCE);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<HealthTypeBean>> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.util.HealthDataTypeUtils$getHomeDisplayTypeList$2, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.homepage.util.HealthDataTypeUtils$getHomeDisplayTypeList$2", f = "HealthDataTypeUtils.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nHealthDataTypeUtils.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HealthDataTypeUtils.kt\ncom/xiaomi/fitness/homepage/util/HealthDataTypeUtils$getHomeDisplayTypeList$2\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,731:1\n1869#2,2:732\n*S KotlinDebug\n*F\n+ 1 HealthDataTypeUtils.kt\ncom/xiaomi/fitness/homepage/util/HealthDataTypeUtils$getHomeDisplayTypeList$2\n*L\n143#1:732,2\n*E\n"})
    public static final class C28992 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<Integer>>, Object> {
        int label;

        public C28992(Continuation<? super C28992> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C28992(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super List<Integer>> continuation) {
            return invoke2(coroutineScope, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            ArrayList arrayList = new ArrayList();
            for (HealthTypeBean healthTypeBean : HealthDataTypeUtils.access$getAllSupportDataTypeList(HealthDataTypeUtils.INSTANCE)) {
                if (healthTypeBean.getEnable()) {
                    arrayList.add(Boxing.boxInt(healthTypeBean.getHealthType()));
                }
            }
            return arrayList;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<Integer>> continuation) {
            return ((C28992) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.homepage.util.HealthDataTypeUtils$getTrainingIndexDataItem$1, reason: invalid class name */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.homepage.util.HealthDataTypeUtils", f = "HealthDataTypeUtils.kt", i = {0, 0}, l = {388}, m = "getTrainingIndexDataItem", n = {"homeDataType", "report"}, s = {"L$0", "L$1"})
    public static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return HealthDataTypeUtils.this.getTrainingIndexDataItem(null, null, this);
        }
    }

    private HealthDataTypeUtils() {
    }

    public static final /* synthetic */ List access$getAllSupportDataTypeList(HealthDataTypeUtils healthDataTypeUtils) {
        return healthDataTypeUtils.getAllSupportDataTypeList();
    }

    private final Integer convertToDataTypeInt(String type) {
        switch (type.hashCode()) {
            case -2030863703:
                return !type.equals("blood_glucose") ? null : 403;
            case -2003821937:
                return !type.equals("mh_intensity") ? null : 15;
            case -1563643941:
                return !type.equals("ecg_medical") ? null : 402;
            case -1298713976:
                return !type.equals("energy") ? null : 8;
            case -891989580:
                return !type.equals("stress") ? null : 7;
            case -812619221:
                return !type.equals("vo2max") ? null : 12;
            case -791592328:
                return !type.equals("weight") ? null : 10000;
            case -568136405:
                return !type.equals("training_load") ? null : 20;
            case 110744:
                return !type.equals("pai") ? null : 10;
            case 3537088:
                return !type.equals("spo2") ? null : 13;
            case 3540684:
                return !type.equals("step") ? null : 3;
            case 95027346:
                return !type.equals("curse") ? null : 9;
            case 109522647:
                return !type.equals("sleep") ? null : 6;
            case 321701236:
                return !type.equals("temperature") ? null : 21;
            case 548738829:
                return !type.equals("calorie") ? null : 2;
            case 560044778:
                return !type.equals("blood_pressure") ? null : 16;
            case 795728692:
                return !type.equals("hearing") ? null : 17;
            case 868936591:
                return !type.equals("running_indicator") ? null : 22;
            case 976395354:
                return !type.equals("physical_status") ? null : 24;
            case 1312635980:
                return !type.equals(Feature.STANDING) ? null : 5;
            case 1605680418:
                return !type.equals("vitality") ? null : 23;
            case 1930449209:
                return !type.equals("heart_rate") ? null : 4;
            default:
                return null;
        }
    }

    private final HealthData createHealthData(String type, Boolean enable) {
        HealthData healthData = new HealthData();
        healthData.setType(type);
        if (enable != null) {
            healthData.setEnable(enable.booleanValue());
        }
        return healthData;
    }

    public static /* synthetic */ HealthData createHealthData$default(HealthDataTypeUtils healthDataTypeUtils, String str, Boolean bool, int i, Object obj) {
        if ((i & 2) != 0) {
            bool = null;
        }
        return healthDataTypeUtils.createHealthData(str, bool);
    }

    private final List<Integer> getAllDataItemTypes() {
        ArrayList arrayList = new ArrayList();
        boolean zIsFemale = AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).isFemale();
        arrayList.add(6);
        arrayList.add(4);
        arrayList.add(3);
        arrayList.add(2);
        arrayList.add(15);
        arrayList.add(5);
        arrayList.add(13);
        arrayList.add(7);
        arrayList.add(23);
        arrayList.add(10);
        if (zIsFemale) {
            arrayList.add(9);
        }
        arrayList.add(10000);
        arrayList.add(8);
        arrayList.add(12);
        arrayList.add(16);
        arrayList.add(17);
        arrayList.add(20);
        arrayList.add(21);
        arrayList.add(22);
        arrayList.add(402);
        if (!AppUtil.INSTANCE.isPlayChannel()) {
            arrayList.add(403);
        }
        if (!zIsFemale) {
            arrayList.add(9);
        }
        arrayList.add(24);
        return arrayList;
    }

    private final List<HealthTypeBean> getAllSupportDataTypeList() {
        List<Integer> allDataItemTypes = getAllDataItemTypes();
        Map<Integer, HealthTypeBean> cloudHealthTypeInfo = getCloudHealthTypeInfo();
        ArrayList arrayList = new ArrayList();
        FitnessLogUtils.i(TAG, "getAllDataTypeList start");
        Iterator<Integer> it = allDataItemTypes.iterator();
        while (it.hasNext()) {
            int iIntValue = it.next().intValue();
            HealthTypeBean healthTypeBean = cloudHealthTypeInfo.get(Integer.valueOf(iIntValue));
            boolean manualSet = healthTypeBean != null ? healthTypeBean.getManualSet() : false;
            if (healthTypeBean == null || !healthTypeBean.getEnable()) {
                HealthTypeBean healthTypeBean2 = new HealthTypeBean(iIntValue, healthTypeBean == null, manualSet);
                if (isDefaultDisplayDataType(iIntValue)) {
                    FitnessLogUtils.i(TAG, "default support " + healthTypeBean2);
                    arrayList.add(healthTypeBean2);
                } else if (iIntValue == 9 && AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).isFemale()) {
                    FitnessLogUtils.i(TAG, "isFemale, support " + healthTypeBean2);
                    arrayList.add(healthTypeBean2);
                } else if (hasDataOfTheDataType(iIntValue)) {
                    FitnessLogUtils.i(TAG, "has data, support " + healthTypeBean2);
                    arrayList.add(healthTypeBean2);
                } else if (supportTheDataTypeInBoundDevices(iIntValue)) {
                    FitnessLogUtils.i(TAG, "device support " + healthTypeBean2);
                    arrayList.add(healthTypeBean2);
                } else if (iIntValue == 9) {
                    healthTypeBean2.setEnable(false);
                    FitnessLogUtils.i(TAG, "isMale, support " + healthTypeBean2);
                    arrayList.add(healthTypeBean2);
                }
            } else {
                HealthTypeBean healthTypeBean3 = new HealthTypeBean(iIntValue, true, manualSet);
                FitnessLogUtils.i(TAG, "cloud support " + healthTypeBean3);
                arrayList.add(healthTypeBean3);
            }
        }
        return arrayList;
    }

    private final HealthDataSetting getCloudHealthDataSetting() {
        Object objValueToObject = DeviceSettingManager.INSTANCE.getInstance().readSettingItem("xiaomiwear_app", DeviceSettingConfigKey.KEY_HEALTH_DATA).valueToObject(HealthDataSetting.class);
        HealthDataSetting healthDataSetting = objValueToObject instanceof HealthDataSetting ? (HealthDataSetting) objValueToObject : null;
        FitnessLogUtils.d(TAG, "getCloudHealthDataSetting is " + healthDataSetting);
        return healthDataSetting;
    }

    private final Map<Integer, HealthTypeBean> getCloudHealthTypeInfo() {
        Iterable<HealthData> healthmanuallist;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        HealthDataSetting cloudHealthDataSetting = getCloudHealthDataSetting();
        if (cloudHealthDataSetting == null || (healthmanuallist = cloudHealthDataSetting.getHealthmanuallist()) == null) {
            healthmanuallist = AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin() ? null : manualDataTypeList;
        }
        if (healthmanuallist != null) {
            for (HealthData healthData : healthmanuallist) {
                HealthDataTypeUtils healthDataTypeUtils = INSTANCE;
                String type = healthData.getType();
                Intrinsics.checkNotNullExpressionValue(type, "getType(...)");
                Integer numConvertToDataTypeInt = healthDataTypeUtils.convertToDataTypeInt(type);
                if (numConvertToDataTypeInt == null) {
                    FitnessLogUtils.i(TAG, "getCloudHealthTypes, " + healthData.getType() + " to null");
                } else {
                    int iIntValue = numConvertToDataTypeInt.intValue();
                    Boolean enable = healthData.getEnable();
                    Intrinsics.checkNotNullExpressionValue(enable, "getEnable(...)");
                    linkedHashMap.put(numConvertToDataTypeInt, new HealthTypeBean(iIntValue, enable.booleanValue(), true));
                }
            }
        }
        return linkedHashMap;
    }

    private final String getDeviceFeatureByType(int itemType) {
        if (itemType == 4) {
            return "heart_rate";
        }
        if (itemType == 10) {
            return "pai";
        }
        if (itemType == 10000) {
            return Feature.BODY_FAT;
        }
        if (itemType == 7) {
            return "pressure";
        }
        if (itemType == 8) {
            return "energy";
        }
        if (itemType == 12) {
            return "vo2max";
        }
        if (itemType == 13) {
            return Feature.SAO2;
        }
        if (itemType == 402) {
            return !AppUtil.INSTANCE.isPlayChannel() ? Feature.ECG_MEASURE_REMIND : Feature.ECG_GLOBAL_MEASURE_REMIND;
        }
        if (itemType == 403) {
            return "blood_sugar";
        }
        switch (itemType) {
            case 20:
                return Feature.TRAINING_LOAD;
            case 21:
                return "temperature";
            case 22:
                return Feature.RUNNING_INDEX;
            case 23:
                return "vitality";
            case 24:
                return "tsb";
            default:
                return null;
        }
    }

    private final String getPersistKeyByType(int itemType) {
        if (itemType == 12) {
            return "vo2_max";
        }
        if (itemType == 17) {
            return "headset";
        }
        if (itemType == 27) {
            return "temperature_trend";
        }
        if (itemType == 402) {
            return "mc_ecg";
        }
        if (itemType == 403) {
            return "blood_sugar";
        }
        switch (itemType) {
            case 8:
                return "energy";
            case 9:
                return "menstruation";
            case 10:
                return "pai";
            default:
                switch (itemType) {
                    case 20:
                        return "training_load";
                    case 21:
                        return "single_temperature";
                    case 22:
                        return "running_ability_index";
                    case 23:
                        return "vitality";
                    case 24:
                        return "physical_fitness_status";
                    default:
                        return null;
                }
        }
    }

    private final boolean hasDataOfTheDataType(int healthType) {
        String persistKeyByType = getPersistKeyByType(healthType);
        return (persistKeyByType == null || FitnessDataGetter.getRecentOneData$default(FitnessDataExtKt.getInstance(FitnessDataGetter.INSTANCE), persistKeyByType, 0L, null, 6, null) == null) ? false : true;
    }

    private final boolean isDefaultDisplayDataType(int healthType) {
        if (healthType == 12 || healthType == 13 || healthType == 15 || healthType == 16 || healthType == 403 || healthType == 10000) {
            return true;
        }
        switch (healthType) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    private final boolean isSupportPhysicalStatus() {
        String did;
        WearableDeviceManager.Companion companion = WearableDeviceManager.INSTANCE;
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(companion).getCurrentDeviceModel();
        if (wearableDeviceModel == null || (did = wearableDeviceModel.getDid()) == null) {
            did = "";
        }
        boolean z = (DeviceManagerExtKt.getInstance(companion).getDeviceCapabilityV2(did) & 32768) != 0;
        Logger.i(TAG, "did:" + did + " , isSupportPhysicalStatus:" + z + " , capability:" + DeviceManagerExtKt.getInstance(companion).getDeviceCapabilityV2(did));
        return z;
    }

    private final boolean supportTheDataTypeInBoundDevices(int healthType) {
        if (healthType == 17) {
            return AppUtil.INSTANCE.supportMiuiFunction();
        }
        for (T t : DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceModels()) {
            String deviceFeatureByType = getDeviceFeatureByType(healthType);
            if (deviceFeatureByType != null && DeviceModelExtKt.isSupportFeature(t, deviceFeatureByType)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public final String convertToDataTypeStr$health_chinaProductRelease(int type) {
        if (type == 12) {
            return "vo2max";
        }
        if (type == 13) {
            return "spo2";
        }
        if (type == 402) {
            return "ecg_medical";
        }
        if (type == 403) {
            return "blood_glucose";
        }
        if (type == 10000) {
            return "weight";
        }
        switch (type) {
            case 2:
                return "calorie";
            case 3:
                return "step";
            case 4:
                return "heart_rate";
            case 5:
                return Feature.STANDING;
            case 6:
                return "sleep";
            case 7:
                return "stress";
            case 8:
                return "energy";
            case 9:
                return "curse";
            case 10:
                return "pai";
            default:
                switch (type) {
                    case 15:
                        return "mh_intensity";
                    case 16:
                        return "blood_pressure";
                    case 17:
                        return "hearing";
                    default:
                        switch (type) {
                            case 20:
                                return "training_load";
                            case 21:
                                return "temperature";
                            case 22:
                                return "running_indicator";
                            case 23:
                                return "vitality";
                            case 24:
                                return "physical_status";
                            default:
                                return null;
                        }
                }
        }
    }

    @Nullable
    public final Integer convertToHealthTypeInt(@Nullable HomeDataType homeType) {
        switch (homeType == null ? -1 : WhenMappings.$EnumSwitchMapping$0[homeType.ordinal()]) {
            case 1:
                return 12;
            case 2:
                return 20;
            case 3:
                return 22;
            case 4:
                return 24;
            case 5:
            default:
                return null;
            case 6:
                return 3;
            case 7:
                return 2;
            case 8:
                return 5;
            case 9:
                return 15;
            case 10:
                return 6;
            case 11:
                return 8;
            case 12:
                return 17;
            case 13:
                return 10;
            case 14:
                return 9;
            case 15:
                return 4;
            case 16:
                return 13;
            case 17:
                return 7;
            case 18:
                return 19;
            case 19:
                return 10000;
            case 20:
                return 16;
            case 21:
                return 21;
            case 22:
                return 14;
            case 23:
                return 23;
            case 24:
                return 402;
            case 25:
                return 403;
        }
    }

    @Nullable
    public final HomeDataType convertToHomeDataType(int healthType) {
        if (healthType == 402) {
            return HomeDataType.ECG_MEDICAL;
        }
        if (healthType == 403) {
            return HomeDataType.BLOOD_SUGAR;
        }
        if (healthType == 10000) {
            return HomeDataType.WEIGHT;
        }
        switch (healthType) {
            case 2:
                return HomeDataType.CALORIE;
            case 3:
                return HomeDataType.STEP;
            case 4:
                return HomeDataType.HR;
            case 5:
                return HomeDataType.VALID_STAND;
            case 6:
                return HomeDataType.SLEEP;
            case 7:
                return HomeDataType.STRESS;
            case 8:
                return HomeDataType.ENERGY;
            case 9:
                return HomeDataType.WOMEN_HEALTH;
            case 10:
                return HomeDataType.PAI;
            default:
                switch (healthType) {
                    case 12:
                        return HomeDataType.V02_MAX;
                    case 13:
                        return HomeDataType.SPO2;
                    case 14:
                        return HomeDataType.RAINBOW;
                    case 15:
                        return HomeDataType.MH_STRENGTH;
                    case 16:
                        return HomeDataType.BLOOD_PRESSURE;
                    case 17:
                        return HomeDataType.HEARING_HEALTH;
                    default:
                        switch (healthType) {
                            case 19:
                                return HomeDataType.SPORT_REPORT;
                            case 20:
                                return HomeDataType.TRAINING_LOAD;
                            case 21:
                                return HomeDataType.TEMPERATURE;
                            case 22:
                                return HomeDataType.RUNNING_INDICATOR;
                            case 23:
                                return HomeDataType.VITALITY;
                            case 24:
                                return HomeDataType.PHYSICAL_STATUS;
                            default:
                                return null;
                        }
                }
        }
    }

    @Nullable
    public final Object getAllDataTypeList(@NotNull Continuation<? super List<HealthTypeBean>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass2(null), continuation);
    }

    @Nullable
    public final Object getHomeDisplayTypeList(@NotNull Continuation<? super List<Integer>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new C28992(null), continuation);
    }

    @NotNull
    public final List<HomeDataType> getRequestHomeTypeList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(HomeDataType.SPORT_REPORT);
        arrayList.add(HomeDataType.SLEEP);
        arrayList.add(HomeDataType.HR);
        arrayList.add(HomeDataType.STEP);
        arrayList.add(HomeDataType.CALORIE);
        arrayList.add(HomeDataType.VALID_STAND);
        arrayList.add(HomeDataType.MH_STRENGTH);
        arrayList.add(HomeDataType.BLOOD_PRESSURE);
        arrayList.add(HomeDataType.STRESS);
        arrayList.add(HomeDataType.SPO2);
        arrayList.add(HomeDataType.PAI);
        arrayList.add(HomeDataType.ENERGY);
        arrayList.add(HomeDataType.V02_MAX);
        arrayList.add(HomeDataType.WEIGHT);
        arrayList.add(HomeDataType.HEARING_HEALTH);
        arrayList.add(HomeDataType.WOMEN_HEALTH);
        arrayList.add(HomeDataType.TRAINING_LOAD);
        arrayList.add(HomeDataType.TEMPERATURE);
        arrayList.add(HomeDataType.RUNNING_INDICATOR);
        arrayList.add(HomeDataType.VITALITY);
        arrayList.add(HomeDataType.ECG_MEDICAL);
        arrayList.add(HomeDataType.BLOOD_SUGAR);
        arrayList.add(HomeDataType.PHYSICAL_STATUS);
        return arrayList;
    }

    @NotNull
    public final List<HomeDataType> getSupportiveHomeDataTypeListForTrainingIndex() {
        ArrayList arrayList = new ArrayList();
        if (isSupportPhysicalStatus()) {
            arrayList.add(HomeDataType.PHYSICAL_STATUS_V2);
        }
        if ((supportTheDataTypeInBoundDevices(20) || hasDataOfTheDataType(20)) && !isSupportPhysicalStatus()) {
            arrayList.add(HomeDataType.TRAINING_LOAD);
            Logger.i(TAG, "getSupportiveHomeDataTypeListForTrainingIndex supports INT_TYPE_TRAINING_LOAD");
        }
        if (supportTheDataTypeInBoundDevices(12) || hasDataOfTheDataType(12)) {
            arrayList.add(HomeDataType.V02_MAX);
            Logger.i(TAG, "getSupportiveHomeDataTypeListForTrainingIndex supports INT_TYPE_VO2_MAX");
        }
        if (supportTheDataTypeInBoundDevices(22) || hasDataOfTheDataType(22)) {
            arrayList.add(HomeDataType.RUNNING_INDICATOR);
            Logger.i(TAG, "getSupportiveHomeDataTypeListForTrainingIndex supports INT_TYPE_RUNNING_INDICATOR");
        }
        if ((supportTheDataTypeInBoundDevices(24) || hasDataOfTheDataType(24)) && !isSupportPhysicalStatus()) {
            arrayList.add(HomeDataType.PHYSICAL_STATUS);
            Logger.i(TAG, "getSupportiveHomeDataTypeListForTrainingIndex supports INT_TYPE_PHYSICAL_STATUS");
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0019  */
    @Nullable
    public final Object getTrainingIndexDataItem(@NotNull HomeDataType homeDataType, @NotNull DailyBasicReport dailyBasicReport, @NotNull Continuation<? super TrainingIndexDataItem> continuation) {
        AnonymousClass1 anonymousClass1;
        String str;
        Integer runningIndicatorLevel;
        DailyBasicReport dailyBasicReport2 = dailyBasicReport;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            int i = anonymousClass1.label;
            if ((i & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label = i - Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        Object objAwait = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = anonymousClass1.label;
        int level = 0;
        if (i2 == 0) {
            ResultKt.throwOnFailure(objAwait);
            int i3 = WhenMappings.$EnumSwitchMapping$0[homeDataType.ordinal()];
            if (i3 == 1) {
                Intrinsics.checkNotNull(dailyBasicReport2, "null cannot be cast to non-null type com.xiaomi.fit.fitness.export.data.aggregation.DailyVo2MaxReport");
                DailyVo2MaxReport dailyVo2MaxReport = (DailyVo2MaxReport) dailyBasicReport2;
                Vo2MaxItem latestItem = dailyVo2MaxReport.getLatestItem();
                Vo2MaxItem latestItem2 = dailyVo2MaxReport.getLatestItem();
                if ((latestItem2 != null ? Boxing.boxInt(latestItem2.getVo2Max()) : null) == null) {
                    return new TrainingIndexDataItem(dailyVo2MaxReport.getTime(), null, HomeDataType.V02_MAX, null, true, ResourceExtKt.getString(qmk.n9), 0, null, null, null, ResourceExtKt.getString(qmk.ar), ResourceExtKt.getString(qmk.Yq), 970, null);
                }
                List<LevelBarView.LevelItem> vo2MaxLevelItems = VO2MaxDataUtil.INSTANCE.getVo2MaxLevelItems();
                Vo2MaxItem latestItem3 = dailyVo2MaxReport.getLatestItem();
                int vo2Max = latestItem3 != null ? latestItem3.getVo2Max() : 0;
                String dateMMddFormat = TimeDateUtil.getDateMMddFormat(dailyVo2MaxReport.getTime());
                if (latestItem != null) {
                    level = VO2MaxDataUtil.getLevel(latestItem);
                    dateMMddFormat = dateMMddFormat + " " + VO2MaxDataUtil.getLevelString(level);
                }
                return new TrainingIndexDataItem(dailyVo2MaxReport.getTime(), dailyVo2MaxReport.getSid(), HomeDataType.V02_MAX, null, false, null, level - 1, String.valueOf(vo2Max), dateMMddFormat, vo2MaxLevelItems, ResourceExtKt.getString(qmk.ar), ResourceExtKt.getString(qmk.Yq), 40, null);
            }
            if (i3 == 2) {
                TrainingLoadDataUtil trainingLoadDataUtil = TrainingLoadDataUtil.INSTANCE;
                List<LevelBarView.LevelItem> trainLoadLevelList = trainingLoadDataUtil.getTrainLoadLevelList();
                Intrinsics.checkNotNull(dailyBasicReport2, "null cannot be cast to non-null type com.xiaomi.fit.fitness.export.data.aggregation.DailyTrainingLoadReport");
                DailyTrainingLoadReport dailyTrainingLoadReport = (DailyTrainingLoadReport) dailyBasicReport2;
                Logger.d(TAG, "getTrainingIndexDataItem trainingLoadReport=" + dailyTrainingLoadReport);
                int trainingLoadLevel = dailyTrainingLoadReport.getTrainingLoadLevel();
                int wtlSum = dailyTrainingLoadReport.getWtlSum();
                String trainingLoadGradeShortTitle = trainingLoadDataUtil.getTrainingLoadGradeShortTitle(ApplicationExtKt.getApplication(), trainingLoadLevel);
                String dateMMddFormat2 = TimeDateUtil.getDateMMddFormat(dailyTrainingLoadReport.getTime());
                if (dailyTrainingLoadReport.getWtlSum() >= 0) {
                    str = dateMMddFormat2 + " " + trainingLoadGradeShortTitle;
                } else {
                    str = "";
                }
                return new TrainingIndexDataItem(dailyTrainingLoadReport.getTime(), dailyTrainingLoadReport.getSid(), HomeDataType.TRAINING_LOAD, null, false, ResourceExtKt.getString(qmk.Vd), trainingLoadLevel >= 1 ? trainingLoadLevel - 1 : 0, String.valueOf(wtlSum), str, trainLoadLevelList, ResourceExtKt.getString(qmk.q9), ResourceExtKt.getString(qmk.s9), 8, null);
            }
            if (i3 == 3) {
                RunningIndicatorUtil runningIndicatorUtil = RunningIndicatorUtil.INSTANCE;
                List<LevelBarView.LevelItem> runningAbilityIndicatorLevelItems = runningIndicatorUtil.getRunningAbilityIndicatorLevelItems();
                Intrinsics.checkNotNull(dailyBasicReport2, "null cannot be cast to non-null type com.xiaomi.fit.fitness.export.data.aggregation.DailyRunningIndicatorReport");
                DailyRunningIndicatorReport dailyRunningIndicatorReport = (DailyRunningIndicatorReport) dailyBasicReport2;
                RunningIndicatorItem latestItem4 = dailyRunningIndicatorReport.getLatestItem();
                String dateMMddFormat3 = TimeDateUtil.getDateMMddFormat(latestItem4 != null ? latestItem4.getTime() : 0L);
                RunningIndicatorItem latestItem5 = dailyRunningIndicatorReport.getLatestItem();
                Float fBoxFloat = latestItem5 != null ? Boxing.boxFloat(latestItem5.getRunningIndicator()) : null;
                new DecimalFormat("0.0").format(Boxing.boxFloat(fBoxFloat != null ? fBoxFloat.floatValue() : 0.0f));
                if (fBoxFloat == null) {
                    return new TrainingIndexDataItem(dailyRunningIndicatorReport.getTime(), null, HomeDataType.RUNNING_INDICATOR, null, true, ResourceExtKt.getString(qmk.n9), 0, null, null, null, ResourceExtKt.getString(qmk.we), ResourceExtKt.getString(qmk.ue), 970, null);
                }
                if (latestItem4 != null && (runningIndicatorLevel = latestItem4.getRunningIndicatorLevel()) != null) {
                    level = runningIndicatorLevel.intValue();
                }
                String runningAbilityLevelName = runningIndicatorUtil.getRunningAbilityLevelName(Boxing.boxInt(level));
                String str2 = new DecimalFormat("0.0").format(fBoxFloat.floatValue());
                String str3 = dateMMddFormat3 + " " + runningAbilityLevelName;
                long time = dailyRunningIndicatorReport.getTime();
                String sid = dailyRunningIndicatorReport.getSid();
                HomeDataType homeDataType2 = HomeDataType.RUNNING_INDICATOR;
                Intrinsics.checkNotNull(str2);
                return new TrainingIndexDataItem(time, sid, homeDataType2, null, false, null, level - 1, str2, str3, runningAbilityIndicatorLevelItems, ResourceExtKt.getString(qmk.we), ResourceExtKt.getString(qmk.ue), 40, null);
            }
            if (i3 == 4) {
                PhysicalStatusDataUtil physicalStatusDataUtil = PhysicalStatusDataUtil.INSTANCE;
                List<LevelBarView.LevelItem> physicalStatusLevelList = physicalStatusDataUtil.getPhysicalStatusLevelList();
                Intrinsics.checkNotNull(dailyBasicReport2, "null cannot be cast to non-null type com.xiaomi.fit.fitness.export.data.aggregation.DailyPhysicalStatusReport");
                DailyPhysicalStatusReport dailyPhysicalStatusReport = (DailyPhysicalStatusReport) dailyBasicReport2;
                PhysicalStatusItem latestPhysicalStatusItem = dailyPhysicalStatusReport.getLatestPhysicalStatusItem();
                String dateMMddFormat4 = TimeDateUtil.getDateMMddFormat(latestPhysicalStatusItem != null ? latestPhysicalStatusItem.getTime() : 0L);
                Integer numBoxInt = latestPhysicalStatusItem != null ? Boxing.boxInt(latestPhysicalStatusItem.getPhysicalStatusLevel()) : null;
                Logger.d(TAG, "latestStatus=" + numBoxInt);
                if (numBoxInt == null) {
                    return new TrainingIndexDataItem(dailyPhysicalStatusReport.getTime(), null, HomeDataType.PHYSICAL_STATUS, null, true, ResourceExtKt.getString(qmk.m9), 0, null, null, null, ResourceExtKt.getString(qmk.fd), ResourceExtKt.getString(qmk.ed), 970, null);
                }
                return new TrainingIndexDataItem(dailyPhysicalStatusReport.getTime(), dailyPhysicalStatusReport.getSid(), HomeDataType.PHYSICAL_STATUS, null, false, null, numBoxInt.intValue(), physicalStatusDataUtil.getPhysicalStatusTitle(ApplicationExtKt.getApplication(), numBoxInt.intValue()), dateMMddFormat4, physicalStatusLevelList, ResourceExtKt.getString(qmk.fd), ResourceExtKt.getString(qmk.ed), 40, null);
            }
            if (i3 != 5) {
                return new TrainingIndexDataItem(dailyBasicReport2.getTime(), dailyBasicReport2.getSid(), HomeDataType.V02_MAX, null, true, null, 0, null, null, null, "", "", 1000, null);
            }
            Deferred deferredAsync$default = BuildersKt.async$default(GlobalScope.INSTANCE, Dispatchers.getMain(), null, new HealthDataTypeUtils$getTrainingIndexDataItem$trainState$1(null), 2, null);
            anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(homeDataType);
            anonymousClass1.L$1 = dailyBasicReport2;
            anonymousClass1.label = 1;
            objAwait = deferredAsync$default.await(anonymousClass1);
            if (objAwait == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i2 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            dailyBasicReport2 = (DailyBasicReport) anonymousClass1.L$1;
            ResultKt.throwOnFailure(objAwait);
        }
        Triple triple = (Triple) objAwait;
        FitnessLogUtils.i(TAG, "trainingState:" + ((int) ((TrainState) triple.getThird()).getTrainingState()));
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        long seconds = timeUnit.toSeconds(System.currentTimeMillis());
        String sid2 = dailyBasicReport2.getSid();
        HomeDataType homeDataType3 = HomeDataType.PHYSICAL_STATUS_V2;
        PhysicalStatusDataUtils physicalStatusDataUtils = PhysicalStatusDataUtils.INSTANCE;
        return new TrainingIndexDataItem(seconds, sid2, homeDataType3, null, false, null, ((TrainState) triple.getThird()).getTrainingState() != 0 ? ((TrainState) triple.getThird()).getTrainingState() - 1 : 0, physicalStatusDataUtils.getPhysicalStatusTitle(ApplicationExtKt.getApplication(), ((TrainState) triple.getThird()).getTrainingState() == 0 ? -2 : ((TrainState) triple.getThird()).getTrainingState() - 1), TimeDateUtil.getDateMMddFormat(timeUnit.toSeconds(System.currentTimeMillis())), physicalStatusDataUtils.getPhysicalStatusLevelList(), ResourceExtKt.getString(qmk.Au), ResourceExtKt.getString(qmk.tu), 40, null);
    }

    public final void saveHealthDataSettingToCloud(@NotNull List<? extends i66> dataList) {
        Intrinsics.checkNotNullParameter(dataList, "dataList");
        HealthDataSetting healthDataSetting = new HealthDataSetting();
        List<HealthData> healthdatalist = healthDataSetting.getHealthdatalist();
        List<HealthData> healthmanuallist = healthDataSetting.getHealthmanuallist();
        Iterator<? extends i66> it = dataList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            i66 next = it.next();
            String strConvertToDataTypeStr$health_chinaProductRelease = convertToDataTypeStr$health_chinaProductRelease(next.f8992b);
            if (strConvertToDataTypeStr$health_chinaProductRelease != null) {
                boolean z = next.e >= 0;
                if (z) {
                    healthdatalist.add(createHealthData$default(INSTANCE, strConvertToDataTypeStr$health_chinaProductRelease, null, 2, null));
                }
                if (next.l) {
                    healthmanuallist.add(INSTANCE.createHealthData(strConvertToDataTypeStr$health_chinaProductRelease, Boolean.valueOf(z)));
                }
            }
        }
        FitnessLogUtils.d(TAG, "save cloud healthData(" + healthmanuallist + ")");
        DeviceSettingManager.INSTANCE.getInstance().sendUserSettingItem(DeviceSettingConfigKey.KEY_HEALTH_DATA, healthDataSetting, false);
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            return;
        }
        manualDataTypeList = healthmanuallist;
    }
}
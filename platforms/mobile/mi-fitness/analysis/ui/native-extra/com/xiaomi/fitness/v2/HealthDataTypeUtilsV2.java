package com.xiaomi.fitness.v2;

import com.xiaomi.fit.data.common.log.FitnessLogUtils;
import com.xiaomi.fit.fitness.export.api.FitnessDataGetter;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyBasicReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyRunningIndicatorReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyTrainingLoadReport;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyVo2MaxReport;
import com.xiaomi.fit.fitness.export.data.annotation.HomeDataType;
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
import com.xiaomi.fitness.data.HealthPrefer;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.export.bean.Feature;
import com.xiaomi.fitness.health.runningindicator.RunningIndicatorUtil;
import com.xiaomi.fitness.health.threetarget.model.TrainingIndexDataItem;
import com.xiaomi.fitness.health.trainload.TrainingLoadDataUtil;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.settingitem.DeviceSettingConfigKey;
import com.xiaomi.fitness.settingitem.DeviceSettingManager;
import com.xiaomi.fitness.settingitem.settingitem.HealthData;
import com.xiaomi.fitness.settingitem.settingitem.HealthDataSetting;
import com.xiaomi.fitness.util.VO2MaxDataUtil;
import defpackage.i66;
import defpackage.qmk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0010\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u0017\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0017\u0010\r\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\r\u0010\fJ\u0017\u0010\u000e\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0002¢\u0006\u0004\b\u000e\u0010\fJ\u001b\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00050\u000fH\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\u0002¢\u0006\u0004\b\u0013\u0010\u0014J#\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\u00152\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\nH\u0002¢\u0006\u0004\b\u0019\u0010\u001aJ\u0019\u0010\u001c\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001b\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\u0019\u0010\u001e\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001b\u001a\u00020\bH\u0002¢\u0006\u0004\b\u001e\u0010\u001dJ\u0019\u0010\u001f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0016\u001a\u00020\u0015H\u0002¢\u0006\u0004\b\u001f\u0010 J\u0015\u0010!\u001a\b\u0012\u0004\u0012\u00020\b0\u0004H\u0002¢\u0006\u0004\b!\u0010\u0007J\u0016\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0086@¢\u0006\u0004\b\"\u0010#J\u0016\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u0004H\u0086@¢\u0006\u0004\b$\u0010#J\u001d\u0010*\u001a\u00020)2\u0006\u0010&\u001a\u00020%2\u0006\u0010(\u001a\u00020'¢\u0006\u0004\b*\u0010+J\u001b\u0010/\u001a\u00020.2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020,0\u0004¢\u0006\u0004\b/\u00100J\u0019\u00102\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\bH\u0000¢\u0006\u0004\b1\u0010\u001dJ\u0019\u00104\u001a\u0004\u0018\u00010\b2\b\u00103\u001a\u0004\u0018\u00010%¢\u0006\u0004\b4\u00105J\u0017\u00106\u001a\u0004\u0018\u00010%2\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b6\u00107J\u0013\u00108\u001a\b\u0012\u0004\u0012\u00020%0\u0004¢\u0006\u0004\b8\u0010\u0007J\u0013\u00109\u001a\b\u0012\u0004\u0012\u00020\b0\u0004¢\u0006\u0004\b9\u0010\u0007R\u0014\u0010:\u001a\u00020\u00158\u0002X\u0082T¢\u0006\u0006\n\u0004\b:\u0010;R\u001e\u0010<\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b<\u0010=¨\u0006>"}, d2 = {"Lcom/xiaomi/fitness/v2/HealthDataTypeUtilsV2;", "", "<init>", "()V", "", "Lcom/xiaomi/fitness/v2/HealthTypeBean;", "getAllSupportDataTypeList", "()Ljava/util/List;", "", "healthType", "", "isDefaultDisplayDataType", "(I)Z", "supportTheDataTypeInBoundDevices", "hasDataOfTheDataType", "", "getCloudHealthTypeInfo", "()Ljava/util/Map;", "Lcom/xiaomi/fitness/settingitem/settingitem/HealthDataSetting;", "getCloudHealthDataSetting", "()Lcom/xiaomi/fitness/settingitem/settingitem/HealthDataSetting;", "", "type", "enable", "Lcom/xiaomi/fitness/settingitem/settingitem/HealthData;", "createHealthData", "(Ljava/lang/String;Ljava/lang/Boolean;)Lcom/xiaomi/fitness/settingitem/settingitem/HealthData;", "itemType", "getDeviceFeatureByType", "(I)Ljava/lang/String;", "getPersistKeyByType", "convertToDataTypeInt", "(Ljava/lang/String;)Ljava/lang/Integer;", "getAllDataItemTypes", "getAllDataTypeList", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHomeDisplayTypeList", "Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;", "homeDataType", "Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyBasicReport;", "report", "Lcom/xiaomi/fitness/health/threetarget/model/TrainingIndexDataItem;", "getTrainingIndexDataItem", "(Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyBasicReport;)Lcom/xiaomi/fitness/health/threetarget/model/TrainingIndexDataItem;", "Li66;", "dataList", "", "saveHealthDataSettingToCloud", "(Ljava/util/List;)V", "convertToDataTypeStr$health_chinaProductRelease", "convertToDataTypeStr", "homeType", "convertToHealthTypeInt", "(Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;)Ljava/lang/Integer;", "convertToHomeDataType", "(I)Lcom/xiaomi/fit/fitness/export/data/annotation/HomeDataType;", "getRequestHomeTypeList", "getSupportedHealthDataTypeInTodayHealth", "TAG", "Ljava/lang/String;", "manualDataTypeList", "Ljava/util/List;", "health_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nHealthDataTypeUtilsV2.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HealthDataTypeUtilsV2.kt\ncom/xiaomi/fitness/v2/HealthDataTypeUtilsV2\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,642:1\n1869#2,2:643\n1869#2,2:646\n1#3:645\n*S KotlinDebug\n*F\n+ 1 HealthDataTypeUtilsV2.kt\ncom/xiaomi/fitness/v2/HealthDataTypeUtilsV2\n*L\n314#1:643,2\n567#1:646,2\n*E\n"})
public final class HealthDataTypeUtilsV2 {

    @NotNull
    public static final HealthDataTypeUtilsV2 INSTANCE = new HealthDataTypeUtilsV2();

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
                iArr[HomeDataType.STEP.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[HomeDataType.CALORIE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[HomeDataType.VALID_STAND.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[HomeDataType.MH_STRENGTH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[HomeDataType.SLEEP.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr[HomeDataType.ENERGY.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr[HomeDataType.HEARING_HEALTH.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr[HomeDataType.PAI.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr[HomeDataType.WOMEN_HEALTH.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr[HomeDataType.HR.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                iArr[HomeDataType.SPO2.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                iArr[HomeDataType.STRESS.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                iArr[HomeDataType.SPORT_REPORT.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                iArr[HomeDataType.WEIGHT.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                iArr[HomeDataType.BLOOD_PRESSURE.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                iArr[HomeDataType.TEMPERATURE.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                iArr[HomeDataType.RAINBOW.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                iArr[HomeDataType.VITALITY.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                iArr[HomeDataType.ECG_MEDICAL.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                iArr[HomeDataType.BLOOD_SUGAR.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                iArr[HomeDataType.PHYSICAL_STATUS.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                iArr[HomeDataType.TODAY_HEALTH.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.v2.HealthDataTypeUtilsV2$getAllDataTypeList$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "Lcom/xiaomi/fitness/v2/HealthTypeBean;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.v2.HealthDataTypeUtilsV2$getAllDataTypeList$2", f = "HealthDataTypeUtilsV2.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
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
            return HealthDataTypeUtilsV2.access$getAllSupportDataTypeList(HealthDataTypeUtilsV2.INSTANCE);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<HealthTypeBean>> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.v2.HealthDataTypeUtilsV2$getHomeDisplayTypeList$2, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.v2.HealthDataTypeUtilsV2$getHomeDisplayTypeList$2", f = "HealthDataTypeUtilsV2.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nHealthDataTypeUtilsV2.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HealthDataTypeUtilsV2.kt\ncom/xiaomi/fitness/v2/HealthDataTypeUtilsV2$getHomeDisplayTypeList$2\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,642:1\n1869#2,2:643\n*S KotlinDebug\n*F\n+ 1 HealthDataTypeUtilsV2.kt\ncom/xiaomi/fitness/v2/HealthDataTypeUtilsV2$getHomeDisplayTypeList$2\n*L\n134#1:643,2\n*E\n"})
    public static final class C36832 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<Integer>>, Object> {
        int label;

        public C36832(Continuation<? super C36832> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C36832(continuation);
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
            for (HealthTypeBean healthTypeBean : HealthDataTypeUtilsV2.access$getAllSupportDataTypeList(HealthDataTypeUtilsV2.INSTANCE)) {
                if (healthTypeBean.getEnable()) {
                    arrayList.add(Boxing.boxInt(healthTypeBean.getHealthType()));
                }
            }
            return arrayList;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<Integer>> continuation) {
            return ((C36832) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    private HealthDataTypeUtilsV2() {
    }

    public static final /* synthetic */ List access$getAllSupportDataTypeList(HealthDataTypeUtilsV2 healthDataTypeUtilsV2) {
        return healthDataTypeUtilsV2.getAllSupportDataTypeList();
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
            case 845747557:
                return !type.equals("health_check") ? null : 30000;
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

    public static /* synthetic */ HealthData createHealthData$default(HealthDataTypeUtilsV2 healthDataTypeUtilsV2, String str, Boolean bool, int i, Object obj) {
        if ((i & 2) != 0) {
            bool = null;
        }
        return healthDataTypeUtilsV2.createHealthData(str, bool);
    }

    private final List<Integer> getAllDataItemTypes() {
        ArrayList arrayList = new ArrayList();
        HealthPrefer healthPrefer = HealthPrefer.INSTANCE;
        if (!healthPrefer.getHEALTH_CARD_SORT().isEmpty()) {
            arrayList.addAll(healthPrefer.getHEALTH_CARD_SORT());
        }
        ArrayList arrayList2 = new ArrayList();
        boolean zIsFemale = AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).isFemale();
        arrayList2.add(6);
        arrayList2.add(4);
        arrayList2.add(16);
        arrayList2.add(402);
        AppUtil appUtil = AppUtil.INSTANCE;
        if (!appUtil.isPlayChannel()) {
            arrayList2.add(403);
        }
        arrayList2.add(13);
        arrayList2.add(10000);
        arrayList2.add(21);
        if (zIsFemale) {
            arrayList2.add(9);
        }
        arrayList2.add(7);
        if (!appUtil.isPlayChannel() && RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            arrayList2.add(17);
        }
        arrayList2.add(23);
        arrayList2.add(10);
        arrayList2.add(8);
        if (!zIsFemale) {
            arrayList2.add(9);
        }
        arrayList2.add(30000);
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Number) it.next()).intValue();
            if (!arrayList.contains(Integer.valueOf(iIntValue))) {
                arrayList.add(Integer.valueOf(iIntValue));
            }
        }
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
            Logger.i(TAG, "getAllSupportDataTypeList type=" + iIntValue + " ");
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
                if (healthData.getType() != null) {
                    HealthDataTypeUtilsV2 healthDataTypeUtilsV2 = INSTANCE;
                    String type = healthData.getType();
                    Intrinsics.checkNotNullExpressionValue(type, "getType(...)");
                    Integer numConvertToDataTypeInt = healthDataTypeUtilsV2.convertToDataTypeInt(type);
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
        if (itemType == 30000) {
            return Feature.HEALTH_CHECKUP;
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
        if (itemType == 17) {
            return "headset";
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

    private final boolean supportTheDataTypeInBoundDevices(int healthType) {
        if (healthType == 17) {
            return AppUtil.INSTANCE.supportMiuiFunction();
        }
        if (healthType == 30000) {
            return DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).hasFeatureSupportDevice(CollectionsKt.listOf(Feature.HEALTH_CHECKUP));
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
        if (type == 30000) {
            return "health_check";
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
                return 3;
            case 5:
                return 2;
            case 6:
                return 5;
            case 7:
                return 15;
            case 8:
                return 6;
            case 9:
                return 8;
            case 10:
                return 17;
            case 11:
                return 10;
            case 12:
                return 9;
            case 13:
                return 4;
            case 14:
                return 13;
            case 15:
                return 7;
            case 16:
                return 19;
            case 17:
                return 10000;
            case 18:
                return 16;
            case 19:
                return 21;
            case 20:
                return 14;
            case 21:
                return 23;
            case 22:
                return 402;
            case 23:
                return 403;
            case 24:
                return 24;
            case 25:
                return 30000;
            default:
                return null;
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
        return BuildersKt.withContext(Dispatchers.getIO(), new C36832(null), continuation);
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
        arrayList.add(HomeDataType.TODAY_HEALTH);
        return arrayList;
    }

    @NotNull
    public final List<Integer> getSupportedHealthDataTypeInTodayHealth() {
        ArrayList arrayList = new ArrayList();
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
        if (wearableDeviceModel != null && DeviceModelExtKt.isSupportFeature(wearableDeviceModel, "heart_rate")) {
            arrayList.add(6);
        }
        if (wearableDeviceModel != null && DeviceModelExtKt.isSupportFeature(wearableDeviceModel, Feature.SAO2)) {
            arrayList.add(13);
        }
        if (wearableDeviceModel != null && DeviceModelExtKt.isSupportFeature(wearableDeviceModel, "pressure")) {
            arrayList.add(7);
        }
        if (wearableDeviceModel != null && DeviceModelExtKt.isSupportFeature(wearableDeviceModel, Feature.ASSIST_SLEEP)) {
            arrayList.add(6);
        }
        return arrayList;
    }

    @NotNull
    public final TrainingIndexDataItem getTrainingIndexDataItem(@NotNull HomeDataType homeDataType, @NotNull DailyBasicReport report) {
        Integer runningIndicatorLevel;
        Intrinsics.checkNotNullParameter(homeDataType, "homeDataType");
        Intrinsics.checkNotNullParameter(report, "report");
        int i = WhenMappings.$EnumSwitchMapping$0[homeDataType.ordinal()];
        int level = 0;
        if (i == 1) {
            DailyVo2MaxReport dailyVo2MaxReport = (DailyVo2MaxReport) report;
            Vo2MaxItem latestItem = dailyVo2MaxReport.getLatestItem();
            Vo2MaxItem latestItem2 = dailyVo2MaxReport.getLatestItem();
            if ((latestItem2 != null ? Integer.valueOf(latestItem2.getVo2Max()) : null) == null) {
                return new TrainingIndexDataItem(dailyVo2MaxReport.getTime(), null, HomeDataType.V02_MAX, null, true, null, 0, null, null, null, ResourceExtKt.getString(qmk.Zq), ResourceExtKt.getString(qmk.Yq), 1002, null);
            }
            List<LevelBarView.LevelItem> vo2MaxLevelItems = VO2MaxDataUtil.INSTANCE.getVo2MaxLevelItems();
            Vo2MaxItem latestItem3 = dailyVo2MaxReport.getLatestItem();
            int vo2Max = latestItem3 != null ? latestItem3.getVo2Max() : 0;
            String dateMMddFormat = TimeDateUtil.getDateMMddFormat(dailyVo2MaxReport.getTime());
            if (latestItem != null) {
                level = VO2MaxDataUtil.getLevel(latestItem);
                dateMMddFormat = dateMMddFormat + " " + VO2MaxDataUtil.getLevelString(level);
            }
            return new TrainingIndexDataItem(dailyVo2MaxReport.getTime(), null, HomeDataType.V02_MAX, null, false, null, level - 1, String.valueOf(vo2Max), dateMMddFormat, vo2MaxLevelItems, ResourceExtKt.getString(qmk.Zq), ResourceExtKt.getString(qmk.Yq), 42, null);
        }
        if (i == 2) {
            TrainingLoadDataUtil trainingLoadDataUtil = TrainingLoadDataUtil.INSTANCE;
            List<LevelBarView.LevelItem> trainLoadLevelList = trainingLoadDataUtil.getTrainLoadLevelList();
            DailyTrainingLoadReport dailyTrainingLoadReport = (DailyTrainingLoadReport) report;
            int trainingLoadLevel = dailyTrainingLoadReport.getTrainingLoadLevel();
            int wtlSum = dailyTrainingLoadReport.getWtlSum();
            String trainingLoadGradeShortTitle = trainingLoadDataUtil.getTrainingLoadGradeShortTitle(ApplicationExtKt.getApplication(), trainingLoadLevel);
            String string = ApplicationExtKt.getApplication().getResources().getString(qmk.Oa, trainingLoadGradeShortTitle);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            Logger.d(TAG, "getTrainingLoadLevel level=" + trainingLoadLevel + " levelStr=" + trainingLoadGradeShortTitle);
            if (dailyTrainingLoadReport.getWtlSum() < 0) {
                string = "";
            }
            return new TrainingIndexDataItem(dailyTrainingLoadReport.getTime(), null, HomeDataType.TRAINING_LOAD, null, false, null, trainingLoadLevel - 1, String.valueOf(wtlSum), string, trainLoadLevelList, ResourceExtKt.getString(qmk.q9), ResourceExtKt.getString(qmk.s9), 42, null);
        }
        if (i != 3) {
            return new TrainingIndexDataItem(report.getTime(), null, HomeDataType.V02_MAX, null, true, null, 0, null, null, null, "", "", 1002, null);
        }
        RunningIndicatorUtil runningIndicatorUtil = RunningIndicatorUtil.INSTANCE;
        List<LevelBarView.LevelItem> runningAbilityIndicatorLevelItems = runningIndicatorUtil.getRunningAbilityIndicatorLevelItems();
        DailyRunningIndicatorReport dailyRunningIndicatorReport = (DailyRunningIndicatorReport) report;
        RunningIndicatorItem latestItem4 = dailyRunningIndicatorReport.getLatestItem();
        String dateMMddFormat2 = TimeDateUtil.getDateMMddFormat(latestItem4 != null ? latestItem4.getTime() : 0L);
        RunningIndicatorItem latestItem5 = dailyRunningIndicatorReport.getLatestItem();
        Object objValueOf = latestItem5 != null ? Float.valueOf(latestItem5.getRunningIndicator()) : null;
        if (objValueOf == null) {
            return new TrainingIndexDataItem(dailyRunningIndicatorReport.getTime(), null, HomeDataType.RUNNING_INDICATOR, null, true, null, 0, null, null, null, ResourceExtKt.getString(qmk.we), ResourceExtKt.getString(qmk.ue), 1002, null);
        }
        if (latestItem4 != null && (runningIndicatorLevel = latestItem4.getRunningIndicatorLevel()) != null) {
            level = runningIndicatorLevel.intValue();
        }
        return new TrainingIndexDataItem(dailyRunningIndicatorReport.getTime(), null, HomeDataType.RUNNING_INDICATOR, null, false, null, level - 1, String.valueOf(objValueOf), dateMMddFormat2 + " " + runningIndicatorUtil.getRunningAbilityLevelName(Integer.valueOf(level)), runningAbilityIndicatorLevelItems, ResourceExtKt.getString(qmk.we), ResourceExtKt.getString(qmk.ue), 42, null);
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
        FitnessLogUtils.i(TAG, "save cloud healthData(" + healthdatalist + "), manualData(" + healthmanuallist + ")");
        DeviceSettingManager.INSTANCE.getInstance().sendUserSettingItem(DeviceSettingConfigKey.KEY_HEALTH_DATA, healthDataSetting, false);
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            return;
        }
        manualDataTypeList = healthmanuallist;
    }
}
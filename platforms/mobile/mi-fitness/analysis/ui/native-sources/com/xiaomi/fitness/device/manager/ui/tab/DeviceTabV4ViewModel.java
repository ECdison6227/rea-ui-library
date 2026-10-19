package com.xiaomi.fitness.device.manager.ui.tab;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import com.example.statistics.export.IOnetrack;
import com.example.statistics.export.NestRecyclerViewItemEffectiveExposeBiz;
import com.example.statistics.export.OneTrackExtKt;
import com.example.statistics.export.OneTrackStatBean;
import com.fitness.banner.export.BannerExtKt;
import com.fitness.banner.export.BannerRequestParamV2;
import com.fitness.banner.export.IBanner;
import com.fitness.banner.export.v2.BannerItemV2;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.mi.fitness.checkupdate.export.CheckUpdateManager;
import com.mibi.sdk.component.Constants;
import com.miui.tsmclient.entity.CardInfo;
import com.xiaomi.fitness.baseui.BaseViewModel;
import com.xiaomi.fitness.baseui.recyclerview.itembindings.ItemBinding;
import com.xiaomi.fitness.baseui.recyclerview.viewholder.BaseViewHolder;
import com.xiaomi.fitness.baseui.utils.PermissionsUtil;
import com.xiaomi.fitness.baseui.view.BaseActivity;
import com.xiaomi.fitness.baseui.view.BaseFragment;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.AnyExtKt;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.LocationExtKt;
import com.xiaomi.fitness.common.extensions.StringExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.ToastUtil;
import com.xiaomi.fitness.config.preference.ChooseAppPreference;
import com.xiaomi.fitness.device.contact.export.DeviceContact;
import com.xiaomi.fitness.device.contact.export.DeviceSyncExtKt;
import com.xiaomi.fitness.device.contact.export.OnSyncCallback;
import com.xiaomi.fitness.device.contact.export.SyncResult;
import com.xiaomi.fitness.device.contact.export.SyncStateListener;
import com.xiaomi.fitness.device.manager.DeviceManager;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.DevicePreference;
import com.xiaomi.fitness.device.manager.DeviceStatus;
import com.xiaomi.fitness.device.manager.OnDeviceStatusListener;
import com.xiaomi.fitness.device.manager.a;
import com.xiaomi.fitness.device.manager.bean.BatteryInfo;
import com.xiaomi.fitness.device.manager.bean.DeviceParameter;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.HuamiApiCaller;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.export.bean.TabContentItem;
import com.xiaomi.fitness.device.manager.export.bean.TabDeviceBannerSettingsItem;
import com.xiaomi.fitness.device.manager.export.bean.WatchInfo;
import com.xiaomi.fitness.device.manager.export.bean.WearableDeviceInfo;
import com.xiaomi.fitness.device.manager.extensions.BluetoothExtKt;
import com.xiaomi.fitness.device.manager.extensions.DeviceExtKt;
import com.xiaomi.fitness.device.manager.extensions.OhterExtKt;
import com.xiaomi.fitness.device.manager.l;
import com.xiaomi.fitness.device.manager.utils.BluetoothPermissionUtil;
import com.xiaomi.fitness.device.model.api.WearableDeviceManagerWrapper;
import com.xiaomi.fitness.devicesettings.export.IDevicePowerModeManager;
import com.xiaomi.fitness.devicesettings.export.IDevicePowerModeManagerExtKt;
import com.xiaomi.fitness.devicesettings.export.N62SVersionPreference;
import com.xiaomi.fitness.devicesettings.export.sync.DeviceSettingsSyncHelper;
import com.xiaomi.fitness.devicesettings.export.sync.DeviceSettingsSyncHelperExtKt;
import com.xiaomi.fitness.devicesettings.export.wearmode.WearModeHelper;
import com.xiaomi.fitness.devicesettings.export.wearmode.WearModeHelperExtKt;
import com.xiaomi.fitness.devicesettings.huami.vibratemode.VibrateModeProgressBar;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.medal.export.IPopMedalListRepo;
import com.xiaomi.fitness.medal.export.data.PopMedalData;
import com.xiaomi.fitness.net.response.ApiException;
import com.xiaomi.fitness.notify.export.INotifyRemindListener;
import com.xiaomi.fitness.notify.export.NotifyHelperExtKt;
import com.xiaomi.fitness.settingitem.DeviceSettingConfigKey;
import com.xiaomi.fitness.settingitem.DeviceSettingManager;
import com.xiaomi.fitness.settingitem.settingitem.ChooseCommonApp;
import com.xiaomi.fitness.watch.face.data.FaceEntranceRespWrapper;
import com.xiaomi.fitness.watch.face.export.FaceHelper;
import com.xiaomi.fitness.watch.face.export.FaceHelperExtKt;
import com.xiaomi.fitness.watch.face.export.FaceWearOsHelper;
import com.xiaomi.fitness.watch.face.export.FaceWearOsHelperExtKt;
import com.xiaomi.fitness.watch.face.export.data.FaceData;
import com.xiaomi.fitness.watch.face.export.exportview.FaceSizeConstant;
import com.xiaomi.fitness.wearos.export.WearOSPreference;
import com.xiaomi.hm.health.bt.sdk.data.DeviceBatteryInfo;
import com.xiaomi.wearable.ProxyManagerKt;
import com.xiaomi.wearable.hm.g;
import defpackage.a9k;
import defpackage.ai7;
import defpackage.bi7;
import defpackage.ci7;
import defpackage.di7;
import defpackage.ef3;
import defpackage.ei7;
import defpackage.fi7;
import defpackage.gi7;
import defpackage.hi7;
import defpackage.hmk;
import defpackage.ii7;
import defpackage.j3r;
import defpackage.ji7;
import defpackage.ki7;
import defpackage.l3r;
import defpackage.l62;
import defpackage.li7;
import defpackage.nh7;
import defpackage.oh7;
import defpackage.ph7;
import defpackage.pwn;
import defpackage.qh7;
import defpackage.rh7;
import defpackage.sh7;
import defpackage.th7;
import defpackage.ti7;
import defpackage.uh7;
import defpackage.v12;
import defpackage.vh7;
import defpackage.vit;
import defpackage.wck;
import defpackage.wh7;
import defpackage.wic;
import defpackage.xh7;
import defpackage.yh7;
import defpackage.zh7;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.DelayKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0096\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u001c\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b*\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010%\n\u0002\b\b*\u000e§\u0002ª\u0002µ\u0002¸\u0002»\u0002¾\u0002Á\u0002\b\u0007\u0018\u0000 ß\u00022\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002ß\u0002B\u0019\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\n\u001a\u00020\tH\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\f\u001a\u00020\tH\u0002¢\u0006\u0004\b\f\u0010\u000bJ7\u0010\u0014\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u0011H\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u000f\u0010\u0016\u001a\u00020\u0011H\u0002¢\u0006\u0004\b\u0016\u0010\u0017J\u001b\u0010\u001a\u001a\u00020\u0019*\u00020\u00182\u0006\u0010\u0010\u001a\u00020\u000fH\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ)\u0010\u001f\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001cH\u0002¢\u0006\u0004\b\u001f\u0010 J\u0017\u0010!\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001cH\u0002¢\u0006\u0004\b!\u0010\"J\u0019\u0010#\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002¢\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\u00020\u00112\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002¢\u0006\u0004\b%\u0010&J\u0017\u0010'\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\rH\u0002¢\u0006\u0004\b'\u0010&J!\u0010)\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010(\u001a\u00020\u001cH\u0002¢\u0006\u0004\b)\u0010*J\u000f\u0010+\u001a\u00020\tH\u0002¢\u0006\u0004\b+\u0010\u000bJ\u0017\u0010-\u001a\u00020\t2\u0006\u0010,\u001a\u00020\u000fH\u0002¢\u0006\u0004\b-\u0010.J\u0019\u0010/\u001a\u00020\t2\b\u0010,\u001a\u0004\u0018\u00010\u000fH\u0002¢\u0006\u0004\b/\u0010.J\u000f\u00100\u001a\u00020\tH\u0002¢\u0006\u0004\b0\u0010\u000bJ#\u00102\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u00101\u001a\u00020\u0011H\u0002¢\u0006\u0004\b2\u00103J#\u00104\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u00101\u001a\u00020\u0011H\u0002¢\u0006\u0004\b4\u00103J\u000f\u00105\u001a\u00020\u0011H\u0002¢\u0006\u0004\b5\u0010\u0017J\u000f\u00106\u001a\u00020\u0011H\u0002¢\u0006\u0004\b6\u0010\u0017J#\u00107\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u00101\u001a\u00020\u0011H\u0002¢\u0006\u0004\b7\u00103J\u000f\u00108\u001a\u00020\tH\u0002¢\u0006\u0004\b8\u0010\u000bJ\u0015\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001c09H\u0002¢\u0006\u0004\b:\u0010;J\u001f\u0010?\u001a\u00020>2\u0006\u0010<\u001a\u00020\u001c2\u0006\u0010=\u001a\u00020\u001cH\u0002¢\u0006\u0004\b?\u0010@J\u000f\u0010A\u001a\u00020\tH\u0002¢\u0006\u0004\bA\u0010\u000bJ\u000f\u0010B\u001a\u00020\tH\u0002¢\u0006\u0004\bB\u0010\u000bJ-\u0010I\u001a\u00020\t2\f\u0010E\u001a\b\u0012\u0004\u0012\u00020D0C2\u0006\u0010G\u001a\u00020F2\u0006\u0010H\u001a\u00020\u0011H\u0002¢\u0006\u0004\bI\u0010JJ\u001f\u0010N\u001a\u00020\t2\u0006\u0010K\u001a\u00020\u001c2\u0006\u0010M\u001a\u00020LH\u0002¢\u0006\u0004\bN\u0010OJ\u000f\u0010P\u001a\u00020\tH\u0002¢\u0006\u0004\bP\u0010\u000bJ\u000f\u0010Q\u001a\u00020\u0011H\u0002¢\u0006\u0004\bQ\u0010\u0017J\u0011\u0010S\u001a\u0004\u0018\u00010RH\u0002¢\u0006\u0004\bS\u0010TJ\u0019\u0010W\u001a\u00020\t2\b\u0010V\u001a\u0004\u0018\u00010UH\u0016¢\u0006\u0004\bW\u0010XJ\u000f\u0010Y\u001a\u00020\tH\u0014¢\u0006\u0004\bY\u0010\u000bJ\u0017\u0010\\\u001a\u00020\t2\u0006\u0010[\u001a\u00020ZH\u0016¢\u0006\u0004\b\\\u0010]J\u001d\u0010b\u001a\u00020\t2\u0006\u0010_\u001a\u00020^2\u0006\u0010a\u001a\u00020`¢\u0006\u0004\bb\u0010cJ\r\u0010d\u001a\u00020\t¢\u0006\u0004\bd\u0010\u000bJ\u0017\u0010e\u001a\u00020\t2\b\b\u0002\u0010\u0012\u001a\u00020\u0011¢\u0006\u0004\be\u0010fJ\r\u0010g\u001a\u00020\t¢\u0006\u0004\bg\u0010\u000bJ\r\u0010h\u001a\u00020\t¢\u0006\u0004\bh\u0010\u000bJ\r\u0010i\u001a\u00020\t¢\u0006\u0004\bi\u0010\u000bJ\r\u0010j\u001a\u00020\t¢\u0006\u0004\bj\u0010\u000bJ!\u0010l\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\b\u0002\u0010k\u001a\u00020\u0011¢\u0006\u0004\bl\u0010mJ!\u0010n\u001a\u00020\t2\u0006\u00101\u001a\u00020\u00112\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r¢\u0006\u0004\bn\u0010oJ\u0017\u0010p\u001a\u00020\t2\u0006\u0010[\u001a\u00020ZH\u0016¢\u0006\u0004\bp\u0010]J-\u0010t\u001a\u00020\t2\u001e\u0010s\u001a\u001a\u0012\u0004\u0012\u00020>\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020r\u0012\u0004\u0012\u00020\t0q¢\u0006\u0004\bt\u0010uJ9\u0010{\u001a\u00020\t2\u0006\u0010v\u001a\u00020\u001c2\u0006\u0010w\u001a\u00020\r2\u0006\u0010x\u001a\u00020\u00112\u0012\u0010z\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\t0y¢\u0006\u0004\b{\u0010|J\u001d\u0010~\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010a\u001a\u00020}¢\u0006\u0004\b~\u0010\u007fJ\u0017\u0010\u0080\u0001\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\r¢\u0006\u0005\b\u0080\u0001\u0010$J\u000f\u0010\u0081\u0001\u001a\u00020\t¢\u0006\u0005\b\u0081\u0001\u0010\u000bJ!\u0010\u0082\u0001\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010(\u001a\u00020\u001c¢\u0006\u0005\b\u0082\u0001\u0010*J\u0019\u0010\u0084\u0001\u001a\u00020\t2\u0007\u0010\u0083\u0001\u001a\u00020\u001c¢\u0006\u0006\b\u0084\u0001\u0010\u0085\u0001J\u000f\u0010\u0086\u0001\u001a\u00020\t¢\u0006\u0005\b\u0086\u0001\u0010\u000bJ\u001d\u0010\u0088\u0001\u001a\u00020\t2\u000b\b\u0002\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0011¢\u0006\u0006\b\u0088\u0001\u0010\u0089\u0001J\u0013\u0010\u008b\u0001\u001a\u0005\u0018\u00010\u008a\u0001¢\u0006\u0006\b\u008b\u0001\u0010\u008c\u0001J\u000f\u0010\u008d\u0001\u001a\u00020\u0011¢\u0006\u0005\b\u008d\u0001\u0010\u0017J\u000f\u0010\u008e\u0001\u001a\u00020\t¢\u0006\u0005\b\u008e\u0001\u0010\u000bJ\u000f\u0010\u008f\u0001\u001a\u00020\u0011¢\u0006\u0005\b\u008f\u0001\u0010\u0017J\u000f\u0010\u0090\u0001\u001a\u00020\t¢\u0006\u0005\b\u0090\u0001\u0010\u000bJ\u000f\u0010\u0091\u0001\u001a\u00020\t¢\u0006\u0005\b\u0091\u0001\u0010\u000bJ \u0010\u0093\u0001\u001a\u00020\t2\u000e\u0010\u0092\u0001\u001a\t\u0012\u0005\u0012\u00030\u008a\u000109¢\u0006\u0006\b\u0093\u0001\u0010\u0094\u0001J\u000f\u0010\u0095\u0001\u001a\u00020\u0011¢\u0006\u0005\b\u0095\u0001\u0010\u0017R\u0015\u0010\u0004\u001a\u00020\u00038\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u0004\u0010\u0096\u0001R\u0015\u0010\u0006\u001a\u00020\u00058\u0002X\u0082\u0004¢\u0006\u0007\n\u0005\b\u0006\u0010\u0097\u0001R\u001b\u0010\u0098\u0001\u001a\u0004\u0018\u00010\r8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0098\u0001\u0010\u0099\u0001R1\u0010\u009c\u0001\u001a\n\u0012\u0005\u0012\u00030\u009b\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u009c\u0001\u0010\u009d\u0001\u001a\u0006\b\u009e\u0001\u0010\u009f\u0001\"\u0006\b \u0001\u0010¡\u0001R0\u0010¢\u0001\u001a\t\u0012\u0004\u0012\u00020\u001c0\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b¢\u0001\u0010\u009d\u0001\u001a\u0006\b£\u0001\u0010\u009f\u0001\"\u0006\b¤\u0001\u0010¡\u0001R!\u0010ª\u0001\u001a\u00030¥\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\b¦\u0001\u0010§\u0001\u001a\u0006\b¨\u0001\u0010©\u0001R'\u0010¯\u0001\u001a\t\u0012\u0004\u0012\u00020>0«\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\b¬\u0001\u0010§\u0001\u001a\u0006\b\u00ad\u0001\u0010®\u0001R#\u0010±\u0001\u001a\t\u0012\u0004\u0012\u00020>0°\u00018\u0006¢\u0006\u0010\n\u0006\b±\u0001\u0010²\u0001\u001a\u0006\b³\u0001\u0010´\u0001R#\u0010µ\u0001\u001a\t\u0012\u0004\u0012\u00020>0°\u00018\u0006¢\u0006\u0010\n\u0006\bµ\u0001\u0010²\u0001\u001a\u0006\b¶\u0001\u0010´\u0001R#\u0010·\u0001\u001a\t\u0012\u0004\u0012\u00020>0°\u00018\u0006¢\u0006\u0010\n\u0006\b·\u0001\u0010²\u0001\u001a\u0006\b¸\u0001\u0010´\u0001R'\u0010»\u0001\u001a\t\u0012\u0004\u0012\u00020>0«\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\b¹\u0001\u0010§\u0001\u001a\u0006\bº\u0001\u0010®\u0001R!\u0010À\u0001\u001a\u00030¼\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\b½\u0001\u0010§\u0001\u001a\u0006\b¾\u0001\u0010¿\u0001R!\u0010Å\u0001\u001a\u00030Á\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\bÂ\u0001\u0010§\u0001\u001a\u0006\bÃ\u0001\u0010Ä\u0001Rg\u0010Í\u0001\u001aM\u0012\u0018\u0012\u0016\u0018\u00010r¢\u0006\u000f\bÇ\u0001\u0012\n\bÈ\u0001\u0012\u0005\b\b(É\u0001\u0012\u0016\u0012\u00140\u001c¢\u0006\u000f\bÇ\u0001\u0012\n\bÈ\u0001\u0012\u0005\b\b(Ê\u0001\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020>0Ë\u00010Æ\u0001j\t\u0012\u0004\u0012\u00020>`Ì\u00018\u0006¢\u0006\u0010\n\u0006\bÍ\u0001\u0010Î\u0001\u001a\u0006\bÏ\u0001\u0010Ð\u0001R!\u0010Õ\u0001\u001a\u00030Ñ\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\bÒ\u0001\u0010§\u0001\u001a\u0006\bÓ\u0001\u0010Ô\u0001R'\u0010Ø\u0001\u001a\t\u0012\u0004\u0012\u00020D0«\u00018FX\u0086\u0084\u0002¢\u0006\u0010\n\u0006\bÖ\u0001\u0010§\u0001\u001a\u0006\b×\u0001\u0010®\u0001R#\u0010Ù\u0001\u001a\t\u0012\u0004\u0012\u00020D0°\u00018\u0006¢\u0006\u0010\n\u0006\bÙ\u0001\u0010²\u0001\u001a\u0006\bÚ\u0001\u0010´\u0001R%\u0010Ý\u0001\u001a\b\u0012\u0004\u0012\u00020\u001c098FX\u0086\u0084\u0002¢\u0006\u000f\n\u0006\bÛ\u0001\u0010§\u0001\u001a\u0005\bÜ\u0001\u0010;R\u001c\u0010ß\u0001\u001a\u0005\u0018\u00010Þ\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bß\u0001\u0010à\u0001R\u001c\u0010â\u0001\u001a\u0005\u0018\u00010á\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bâ\u0001\u0010ã\u0001R\u001c\u0010å\u0001\u001a\u0005\u0018\u00010ä\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bå\u0001\u0010æ\u0001R\u001c\u0010è\u0001\u001a\u0005\u0018\u00010ç\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bè\u0001\u0010é\u0001R\u0019\u0010ê\u0001\u001a\u00020\u00118\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bê\u0001\u0010ë\u0001R?\u0010í\u0001\u001a\u0018\u0012\u0013\u0012\u0011\u0012\u0004\u0012\u00020\u0011\u0012\u0006\u0012\u0004\u0018\u00010\r0ì\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bí\u0001\u0010\u009d\u0001\u001a\u0006\bî\u0001\u0010\u009f\u0001\"\u0006\bï\u0001\u0010¡\u0001R9\u0010ñ\u0001\u001a\u0012\u0012\r\u0012\u000b ð\u0001*\u0004\u0018\u00010\u00110\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bñ\u0001\u0010\u009d\u0001\u001a\u0006\bò\u0001\u0010\u009f\u0001\"\u0006\bó\u0001\u0010¡\u0001R0\u0010ô\u0001\u001a\t\u0012\u0004\u0012\u00020\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bô\u0001\u0010\u009d\u0001\u001a\u0006\bõ\u0001\u0010\u009f\u0001\"\u0006\bö\u0001\u0010¡\u0001R%\u0010ù\u0001\u001a\b\u0012\u0004\u0012\u00020D0C8BX\u0082\u0084\u0002¢\u0006\u000f\n\u0006\b÷\u0001\u0010§\u0001\u001a\u0005\bø\u0001\u0010;R%\u0010ü\u0001\u001a\b\u0012\u0004\u0012\u00020D0C8BX\u0082\u0084\u0002¢\u0006\u000f\n\u0006\bú\u0001\u0010§\u0001\u001a\u0005\bû\u0001\u0010;R=\u0010ý\u0001\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r0ì\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bý\u0001\u0010\u009d\u0001\u001a\u0006\bþ\u0001\u0010\u009f\u0001\"\u0006\bÿ\u0001\u0010¡\u0001R0\u0010\u0080\u0002\u001a\t\u0012\u0004\u0012\u00020\r0\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0080\u0002\u0010\u009d\u0001\u001a\u0006\b\u0081\u0002\u0010\u009f\u0001\"\u0006\b\u0082\u0002\u0010¡\u0001R=\u0010\u0083\u0002\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00110ì\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0083\u0002\u0010\u009d\u0001\u001a\u0006\b\u0084\u0002\u0010\u009f\u0001\"\u0006\b\u0085\u0002\u0010¡\u0001R=\u0010\u0086\u0002\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u001c0ì\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0086\u0002\u0010\u009d\u0001\u001a\u0006\b\u0087\u0002\u0010\u009f\u0001\"\u0006\b\u0088\u0002\u0010¡\u0001R2\u0010\u0089\u0002\u001a\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0089\u0002\u0010\u009d\u0001\u001a\u0006\b\u008a\u0002\u0010\u009f\u0001\"\u0006\b\u008b\u0002\u0010¡\u0001R0\u0010\u008c\u0002\u001a\t\u0012\u0004\u0012\u00020\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u008c\u0002\u0010\u009d\u0001\u001a\u0006\b\u008d\u0002\u0010\u009f\u0001\"\u0006\b\u008e\u0002\u0010¡\u0001R=\u0010\u008f\u0002\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u001c0ì\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u008f\u0002\u0010\u009d\u0001\u001a\u0006\b\u0090\u0002\u0010\u009f\u0001\"\u0006\b\u0091\u0002\u0010¡\u0001R\u0019\u0010\u0092\u0002\u001a\u00020\u00118\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0092\u0002\u0010ë\u0001R\u0019\u0010\u0093\u0002\u001a\u00020\u001c8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0093\u0002\u0010\u0094\u0002R\u0019\u0010\u0095\u0002\u001a\u00020Z8\u0002@\u0002X\u0082.¢\u0006\b\n\u0006\b\u0095\u0002\u0010\u0096\u0002R!\u0010\u009b\u0002\u001a\u00030\u0097\u00028BX\u0082\u0084\u0002¢\u0006\u0010\n\u0006\b\u0098\u0002\u0010§\u0001\u001a\u0006\b\u0099\u0002\u0010\u009a\u0002R*\u0010\u009d\u0002\u001a\u00030\u009c\u00028\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b\u009d\u0002\u0010\u009e\u0002\u001a\u0006\b\u009f\u0002\u0010 \u0002\"\u0006\b¡\u0002\u0010¢\u0002R\u001b\u0010£\u0002\u001a\u0004\u0018\u00010>8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b£\u0002\u0010¤\u0002R\u001b\u0010¥\u0002\u001a\u0004\u0018\u00010>8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b¥\u0002\u0010¤\u0002R\u0019\u0010¦\u0002\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b¦\u0002\u0010\u0099\u0001R\u0018\u0010¨\u0002\u001a\u00030§\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b¨\u0002\u0010©\u0002R\u0018\u0010«\u0002\u001a\u00030ª\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b«\u0002\u0010¬\u0002R\u0018\u0010®\u0002\u001a\u00030\u00ad\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b®\u0002\u0010¯\u0002R6\u0010°\u0002\u001a!\u0012\u0017\u0012\u0015\u0018\u00010\u000f¢\u0006\u000e\bÇ\u0001\u0012\t\bÈ\u0001\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\t0y8\u0002X\u0082\u0004¢\u0006\b\n\u0006\b°\u0002\u0010±\u0002R\u0018\u0010³\u0002\u001a\u00030²\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b³\u0002\u0010´\u0002R\u0018\u0010¶\u0002\u001a\u00030µ\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b¶\u0002\u0010·\u0002R\u0018\u0010¹\u0002\u001a\u00030¸\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b¹\u0002\u0010º\u0002R\u0018\u0010¼\u0002\u001a\u00030»\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b¼\u0002\u0010½\u0002R\u0018\u0010¿\u0002\u001a\u00030¾\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b¿\u0002\u0010À\u0002R\u001a\u0010Â\u0002\u001a\u00030Á\u00028\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bÂ\u0002\u0010Ã\u0002R9\u0010Ä\u0002\u001a\u0012\u0012\r\u0012\u000b ð\u0001*\u0004\u0018\u00010\u00110\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bÄ\u0002\u0010\u009d\u0001\u001a\u0006\bÅ\u0002\u0010\u009f\u0001\"\u0006\bÆ\u0002\u0010¡\u0001R9\u0010Ç\u0002\u001a\u0012\u0012\r\u0012\u000b ð\u0001*\u0004\u0018\u00010\u00110\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bÇ\u0002\u0010\u009d\u0001\u001a\u0006\bÈ\u0002\u0010\u009f\u0001\"\u0006\bÉ\u0002\u0010¡\u0001R9\u0010Ê\u0002\u001a\u0012\u0012\r\u0012\u000b ð\u0001*\u0004\u0018\u00010\u00110\u00110\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bÊ\u0002\u0010\u009d\u0001\u001a\u0006\bË\u0002\u0010\u009f\u0001\"\u0006\bÌ\u0002\u0010¡\u0001R\u0019\u0010Í\u0002\u001a\u00020\u001c8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bÍ\u0002\u0010\u0094\u0002R*\u0010Ï\u0002\u001a\u00030Î\u00028\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\bÏ\u0002\u0010Ð\u0002\u001a\u0006\bÑ\u0002\u0010Ò\u0002\"\u0006\bÓ\u0002\u0010Ô\u0002R1\u0010Õ\u0002\u001a\n\u0012\u0005\u0012\u00030\u008a\u00010\u009a\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bÕ\u0002\u0010\u009d\u0001\u001a\u0006\bÖ\u0002\u0010\u009f\u0001\"\u0006\b×\u0002\u0010¡\u0001R!\u0010Û\u0002\u001a\u000f\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020>0Ø\u00028F¢\u0006\b\u001a\u0006\bÙ\u0002\u0010Ú\u0002R\u0017\u0010Þ\u0002\u001a\u00020\r8VX\u0096\u0004¢\u0006\b\u001a\u0006\bÜ\u0002\u0010Ý\u0002¨\u0006à\u0002"}, d2 = {"Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel;", "Lcom/xiaomi/fitness/baseui/BaseViewModel;", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabModel;", "Lcom/xiaomi/fitness/device/model/api/WearableDeviceManagerWrapper;", "mDeviceManager", "Lcom/xiaomi/fitness/device/contact/export/DeviceContact;", "mDeviceContact", "<init>", "(Lcom/xiaomi/fitness/device/model/api/WearableDeviceManagerWrapper;Lcom/xiaomi/fitness/device/contact/export/DeviceContact;)V", "", "providerTabDeviceBannerSettingsItem", "()V", "readN62SWatchInfo", "", "did", "Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;", "deviceModel", "", "isNeedSync", "needShowDevice", "onConnectSuccess", "(Ljava/lang/String;Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;ZZ)V", "isStatusEquals", "()Z", "Lcom/xiaomi/hm/health/bt/sdk/data/DeviceBatteryInfo;", "Lcom/xiaomi/fitness/device/manager/bean/BatteryInfo;", "buildBatteryInfo", "(Lcom/xiaomi/hm/health/bt/sdk/data/DeviceBatteryInfo;Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;)Lcom/xiaomi/fitness/device/manager/bean/BatteryInfo;", "", "errorCode", "retryTimes", "onConnectFailure", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;II)V", "displayErrorCode", "(I)Z", "onDisconnect", "(Ljava/lang/String;)V", "showRecoveryConnect", "(Ljava/lang/String;)Z", "isActiveDeviceModel", "status", "updateDialStatus", "(Ljava/lang/String;I)V", "showDevice", "currentModel", "addFaceEntranceItem", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;)V", "loadWearOsWatch", "addAccessoryDeviceItem", "force", "requestLocationSwitch", "(Ljava/lang/String;Z)V", "requestPermissionIfNeed", "needLocationDevice", "hasLocationPermission", "realConnect", "refreshAccessoryWearMode", "", "createAllFeatureCardIds", "()Ljava/util/List;", "itemType", "type", "Lcom/xiaomi/fitness/device/manager/export/bean/TabContentItem;", "createNormalContentItem", "(II)Lcom/xiaomi/fitness/device/manager/export/bean/TabContentItem;", "addBannerItemWhenHasDevice", "addBannerItemWhenNoDevice", "", "Lcom/fitness/banner/export/v2/BannerItemV2;", "observableList", "Lcom/fitness/banner/export/BannerRequestParamV2;", "bannerRequestParam", "hasDevice", "fillBannerList", "(Ljava/util/List;Lcom/fitness/banner/export/BannerRequestParamV2;Z)V", "app", "", "time", "recordChosenAppCloud", "(IJ)V", "checkAndUpdateChosenApp", "hasSupportDevice", "Lcom/xiaomi/fitness/settingitem/settingitem/ChooseCommonApp;", "getCloudChosenItem", "()Lcom/xiaomi/fitness/settingitem/settingitem/ChooseCommonApp;", "Landroid/os/Bundle;", "savedInstanceState", "attach", "(Landroid/os/Bundle;)V", "onCleared", "Landroidx/lifecycle/LifecycleOwner;", CardInfo.KEY_OWNER, "onCreate", "(Landroidx/lifecycle/LifecycleOwner;)V", "Lcom/xiaomi/fitness/device/manager/export/bean/WearableDeviceInfo;", Constants.KEY_DEVICE_INFO, "Lcom/xiaomi/fitness/device/manager/a;", "callback", "bindDevice", "(Lcom/xiaomi/fitness/device/manager/export/bean/WearableDeviceInfo;Lcom/xiaomi/fitness/device/manager/a;)V", "switchRegin", "loadDevice", "(Z)V", "updateBatteryIfNeed", "syncDeviceIfNeed", "showSystemRedPoint", "refreshFaceEntranceRedPoint", "isAuto", "syncDevice", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;Z)V", "connectDevice", "(ZLjava/lang/String;)V", LifecycleConstantKt.ON_DESTROY, "Lkotlin/Function3;", "Landroid/view/View;", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "setOnItemClickListener", "(Lkotlin/jvm/functions/Function3;)V", "mode", "accessoryDid", com.miui.tsmclient.util.Constants.EXTRA_FROM_APP, "Lkotlin/Function1;", "block", "switchAccessoryMode", "(ILjava/lang/String;ZLkotlin/jvm/functions/Function1;)V", "Lcom/xiaomi/fitness/device/manager/l;", "removeDelete", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;Lcom/xiaomi/fitness/device/manager/l;)V", "unbind", "connectDeviceIfNeed", "updateAccessoryStatus", "currentChosen", "onAppChosen", "(I)V", "recordChosenApp", "showChooseAppDialog", "checkChosenApp", "(Ljava/lang/Boolean;)V", "Lcom/xiaomi/fitness/medal/export/data/PopMedalData;", "getFirstMedalFromPopMedalDataSP", "()Lcom/xiaomi/fitness/medal/export/data/PopMedalData;", "popMedalListFromSpIsEmpty", "removeMedalAlreadyPoppedFromSp", "shouldPullPopMedalDataFromNetwork", "setPopMedalDataAlreadyPulled", "setShouldPullPopMedalDataFromNetwork", "list", "savePopMedalListToSp", "(Ljava/util/List;)V", "getComeFromMedalDetailPageFromSP", "Lcom/xiaomi/fitness/device/model/api/WearableDeviceManagerWrapper;", "Lcom/xiaomi/fitness/device/contact/export/DeviceContact;", "mCurrentDeviceDid", "Ljava/lang/String;", "Landroidx/lifecycle/MutableLiveData;", "Lcom/xiaomi/fitness/device/manager/ui/tab/ConnectStatus;", "connectStatus", "Landroidx/lifecycle/MutableLiveData;", "getConnectStatus", "()Landroidx/lifecycle/MutableLiveData;", "setConnectStatus", "(Landroidx/lifecycle/MutableLiveData;)V", "bondLossRecovery", "getBondLossRecovery", "setBondLossRecovery", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabItemAdapter;", "adapter$delegate", "Lkotlin/Lazy;", "getAdapter", "()Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabItemAdapter;", "adapter", "Lcom/xiaomi/fitness/baseui/recyclerview/itembindings/ItemBinding;", "itemBindings$delegate", "getItemBindings", "()Lcom/xiaomi/fitness/baseui/recyclerview/itembindings/ItemBinding;", "itemBindings", "Landroidx/databinding/ObservableArrayList;", "tabItems", "Landroidx/databinding/ObservableArrayList;", "getTabItems", "()Landroidx/databinding/ObservableArrayList;", "settingTabItems", "getSettingTabItems", "marketTabItems", "getMarketTabItems", "settingItemBindings$delegate", "getSettingItemBindings", "settingItemBindings", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceSettingsItemAdapter;", "settingAdapter$delegate", "getSettingAdapter", "()Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceSettingsItemAdapter;", "settingAdapter", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceMarketItemAdapter;", "marketAdapter$delegate", "getMarketAdapter", "()Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceMarketItemAdapter;", "marketAdapter", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "view", "viewType", "Lcom/xiaomi/fitness/baseui/recyclerview/viewholder/BaseViewHolder;", "Lcom/xiaomi/fitness/baseui/recyclerview/viewholder/ViewHolderFactory;", "settingViewHolder", "Lkotlin/jvm/functions/Function2;", "getSettingViewHolder", "()Lkotlin/jvm/functions/Function2;", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceBannerAdapter;", "bannerAdapter$delegate", "getBannerAdapter", "()Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceBannerAdapter;", "bannerAdapter", "bannerItemBinding$delegate", "getBannerItemBinding", "bannerItemBinding", "bannerItems", "getBannerItems", "mAllFeatureCardIds$delegate", "getMAllFeatureCardIds", "mAllFeatureCardIds", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabDeviceContentItem;", "mDialStatusItem", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabDeviceContentItem;", "Lcom/xiaomi/fitness/device/manager/export/bean/TabDeviceBannerSettingsItem;", "mTabDeviceBannerSettingsItem", "Lcom/xiaomi/fitness/device/manager/export/bean/TabDeviceBannerSettingsItem;", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabFaceEntranceItem;", "mFaceEntranceItem", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabFaceEntranceItem;", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabAccessoryDeviceContentItem;", "mAccessoryDeviceContentItem", "Lcom/xiaomi/fitness/device/manager/ui/tab/TabAccessoryDeviceContentItem;", "connectedOnBackgroundNeedSync", "Z", "Lkotlin/Pair;", "showConnectPermission", "getShowConnectPermission", "setShowConnectPermission", "kotlin.jvm.PlatformType", "showOpenLocationSwitch", "getShowOpenLocationSwitch", "setShowOpenLocationSwitch", "mLoadingFlag", "getMLoadingFlag", "setMLoadingFlag", "hasDeviceBannerList$delegate", "getHasDeviceBannerList", "hasDeviceBannerList", "noDeviceBannerList$delegate", "getNoDeviceBannerList", "noDeviceBannerList", "exitAccessory", "getExitAccessory", "setExitAccessory", "manageAccessory", "getManageAccessory", "setManageAccessory", "existAccessoryMode", "getExistAccessoryMode", "setExistAccessoryMode", "wearModeSwitchSuccess", "getWearModeSwitchSuccess", "setWearModeSwitchSuccess", "syncDeviceLiveData", "getSyncDeviceLiveData", "setSyncDeviceLiveData", "wearModeSwitchSuccessFromApp", "getWearModeSwitchSuccessFromApp", "setWearModeSwitchSuccessFromApp", "exitAccessoryModeFromDevice", "getExitAccessoryModeFromDevice", "setExitAccessoryModeFromDevice", "userClick", "mRetryConnectCount", "I", "mOwner", "Landroidx/lifecycle/LifecycleOwner;", "Lcom/example/statistics/export/NestRecyclerViewItemEffectiveExposeBiz;", "effectiveExposeBiz$delegate", "getEffectiveExposeBiz", "()Lcom/example/statistics/export/NestRecyclerViewItemEffectiveExposeBiz;", "effectiveExposeBiz", "Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "checkUpdate", "Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "getCheckUpdate", "()Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "setCheckUpdate", "(Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;)V", "moreTabItem", "Lcom/xiaomi/fitness/device/manager/export/bean/TabContentItem;", "systemTabItem", "currentFaceId", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$faceEventListener$1", "faceEventListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$faceEventListener$1;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$bondLossReceiver$1", "bondLossReceiver", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$bondLossReceiver$1;", "Lcom/xiaomi/fitness/device/manager/OnDeviceStatusListener;", "mDeviceStatusListener", "Lcom/xiaomi/fitness/device/manager/OnDeviceStatusListener;", "mDeviceChangeListener", "Lkotlin/jvm/functions/Function1;", "Lcom/xiaomi/fitness/device/contact/export/SyncStateListener;", "mSyncStateListener", "Lcom/xiaomi/fitness/device/contact/export/SyncStateListener;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$mOnAccessoryDeviceStateListener$1", "mOnAccessoryDeviceStateListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$mOnAccessoryDeviceStateListener$1;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$updateSettingInfoListener$1", "updateSettingInfoListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$updateSettingInfoListener$1;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$deviceChargeStatusDataHandler$1", "deviceChargeStatusDataHandler", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$deviceChargeStatusDataHandler$1;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$devicePowerModeListener$1", "devicePowerModeListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$devicePowerModeListener$1;", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$checkUpdateListener$1", "checkUpdateListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$checkUpdateListener$1;", "mGoUncommonUsedAppFlag", "getMGoUncommonUsedAppFlag", "setMGoUncommonUsedAppFlag", "mUpdateAppSuccessFlag", "getMUpdateAppSuccessFlag", "setMUpdateAppSuccessFlag", "mChoiceAppFlag", "getMChoiceAppFlag", "setMChoiceAppFlag", "mChosenApp", "Lcom/xiaomi/fitness/medal/export/IPopMedalListRepo;", "mPopMedalRepo", "Lcom/xiaomi/fitness/medal/export/IPopMedalListRepo;", "getMPopMedalRepo", "()Lcom/xiaomi/fitness/medal/export/IPopMedalListRepo;", "setMPopMedalRepo", "(Lcom/xiaomi/fitness/medal/export/IPopMedalListRepo;)V", "medalToPopLiveData", "getMedalToPopLiveData", "setMedalToPopLiveData", "", "getContentItems", "()Ljava/util/Map;", "contentItems", "getTAG", "()Ljava/lang/String;", "TAG", "Companion", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nDeviceTabViewModelV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeviceTabViewModelV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,1667:1\n774#2:1668\n865#2,2:1669\n1869#2,2:1671\n1869#2,2:1673\n1869#2,2:1675\n1761#2,3:1677\n1869#2,2:1680\n1761#2,3:1682\n1761#2,3:1685\n1878#2,3:1688\n1761#2,3:1691\n360#2,7:1694\n360#2,7:1701\n*S KotlinDebug\n*F\n+ 1 DeviceTabViewModelV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel\n*L\n896#1:1668\n896#1:1669,2\n898#1:1671,2\n905#1:1673,2\n916#1:1675,2\n938#1:1677,3\n1250#1:1680,2\n1313#1:1682,3\n1328#1:1685,3\n968#1:1688,3\n1362#1:1691,3\n1369#1:1694,7\n1371#1:1701,7\n*E\n"})
public final class DeviceTabV4ViewModel extends BaseViewModel<DeviceTabModel> {
    public static final int CHOISE_COMMON_APP_DEFAULT = 1;
    public static final int CHOISE_COMMON_APP_SPORT_HEALTH = 3;
    public static final int CHOISE_COMMON_APP_WEARABLE = 2;

    @NotNull
    public static final String PACKAGE_NAME_WEARABLE = "com.xiaomi.wearable";
    public static final int REQUEST_PERMISSION_INTERVAL = 172800000;

    /* JADX INFO: renamed from: adapter$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy adapter;

    /* JADX INFO: renamed from: bannerAdapter$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy bannerAdapter;

    /* JADX INFO: renamed from: bannerItemBinding$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy bannerItemBinding;

    @NotNull
    private final ObservableArrayList<BannerItemV2> bannerItems;

    @NotNull
    private final DeviceTabV4ViewModel$bondLossReceiver$1 bondLossReceiver;

    @NotNull
    private MutableLiveData<Integer> bondLossRecovery;
    public CheckUpdateManager checkUpdate;

    @NotNull
    private DeviceTabV4ViewModel$checkUpdateListener$1 checkUpdateListener;

    @NotNull
    private MutableLiveData<ConnectStatus> connectStatus;
    private boolean connectedOnBackgroundNeedSync;

    @NotNull
    private String currentFaceId;

    @NotNull
    private final DeviceTabV4ViewModel$deviceChargeStatusDataHandler$1 deviceChargeStatusDataHandler;

    @NotNull
    private final DeviceTabV4ViewModel$devicePowerModeListener$1 devicePowerModeListener;

    /* JADX INFO: renamed from: effectiveExposeBiz$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy effectiveExposeBiz;

    @NotNull
    private MutableLiveData<Pair<Integer, Boolean>> existAccessoryMode;

    @NotNull
    private MutableLiveData<Pair<String, String>> exitAccessory;

    @NotNull
    private MutableLiveData<Pair<String, Integer>> exitAccessoryModeFromDevice;

    @NotNull
    private final DeviceTabV4ViewModel$faceEventListener$1 faceEventListener;

    /* JADX INFO: renamed from: hasDeviceBannerList$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy hasDeviceBannerList;

    /* JADX INFO: renamed from: itemBindings$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy itemBindings;

    @Nullable
    private TabAccessoryDeviceContentItem mAccessoryDeviceContentItem;

    /* JADX INFO: renamed from: mAllFeatureCardIds$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy mAllFeatureCardIds;

    @NotNull
    private MutableLiveData<Boolean> mChoiceAppFlag;
    private int mChosenApp;

    @Nullable
    private String mCurrentDeviceDid;

    @NotNull
    private final Function1<WearableDeviceModel, Unit> mDeviceChangeListener;

    @NotNull
    private final DeviceContact mDeviceContact;

    @NotNull
    private final WearableDeviceManagerWrapper mDeviceManager;

    @NotNull
    private final OnDeviceStatusListener mDeviceStatusListener;

    @Nullable
    private TabDeviceContentItem mDialStatusItem;

    @Nullable
    private TabFaceEntranceItem mFaceEntranceItem;

    @NotNull
    private MutableLiveData<Boolean> mGoUncommonUsedAppFlag;

    @NotNull
    private MutableLiveData<Boolean> mLoadingFlag;

    @NotNull
    private final DeviceTabV4ViewModel$mOnAccessoryDeviceStateListener$1 mOnAccessoryDeviceStateListener;
    private LifecycleOwner mOwner;
    public IPopMedalListRepo mPopMedalRepo;
    private int mRetryConnectCount;

    @NotNull
    private final SyncStateListener mSyncStateListener;

    @Nullable
    private TabDeviceBannerSettingsItem mTabDeviceBannerSettingsItem;

    @NotNull
    private MutableLiveData<Boolean> mUpdateAppSuccessFlag;

    @NotNull
    private MutableLiveData<String> manageAccessory;

    /* JADX INFO: renamed from: marketAdapter$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy marketAdapter;

    @NotNull
    private final ObservableArrayList<TabContentItem> marketTabItems;

    @NotNull
    private MutableLiveData<PopMedalData> medalToPopLiveData;

    @Nullable
    private TabContentItem moreTabItem;

    /* JADX INFO: renamed from: noDeviceBannerList$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy noDeviceBannerList;

    /* JADX INFO: renamed from: settingAdapter$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy settingAdapter;

    /* JADX INFO: renamed from: settingItemBindings$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy settingItemBindings;

    @NotNull
    private final ObservableArrayList<TabContentItem> settingTabItems;

    @NotNull
    private final Function2<View, Integer, BaseViewHolder<TabContentItem>> settingViewHolder;

    @NotNull
    private MutableLiveData<Pair<Boolean, String>> showConnectPermission;

    @NotNull
    private MutableLiveData<Boolean> showOpenLocationSwitch;

    @NotNull
    private MutableLiveData<WearableDeviceModel> syncDeviceLiveData;

    @Nullable
    private TabContentItem systemTabItem;

    @NotNull
    private final ObservableArrayList<TabContentItem> tabItems;

    @NotNull
    private final DeviceTabV4ViewModel$updateSettingInfoListener$1 updateSettingInfoListener;
    private boolean userClick;

    @NotNull
    private MutableLiveData<Pair<String, Integer>> wearModeSwitchSuccess;

    @NotNull
    private MutableLiveData<Boolean> wearModeSwitchSuccessFromApp;

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$attach$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$attach$1", "Lcom/xiaomi/fitness/notify/export/INotifyRemindListener;", "onHideRemindBannerItemView", "", "onShowRemindBannerItemView", "remindType", "", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    @SourceDebugExtension({"SMAP\nDeviceTabViewModelV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeviceTabViewModelV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$attach$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,1667:1\n360#2,7:1668\n*S KotlinDebug\n*F\n+ 1 DeviceTabViewModelV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$attach$1\n*L\n279#1:1668,7\n*E\n"})
    public static final class AnonymousClass1 implements INotifyRemindListener {
        public AnonymousClass1() {
        }

        @Override // com.xiaomi.fitness.notify.export.INotifyRemindListener
        public void onHideRemindBannerItemView() {
            Iterator<TabContentItem> it = DeviceTabV4ViewModel.this.getTabItems().iterator();
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    i = -1;
                    break;
                } else if (it.next().getType() == 200) {
                    break;
                } else {
                    i++;
                }
            }
            if (i != -1) {
                DeviceTabV4ViewModel.this.getTabItems().remove(i);
            }
        }

        @Override // com.xiaomi.fitness.notify.export.INotifyRemindListener
        public void onShowRemindBannerItemView(int remindType) {
            OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData(new OneTrackStatBean("expose_v3", "56.51.0.1.36385"), "name", "Device_page_PermissionApplication");
            AnyExtKt.main$default(null, new DeviceTabV4ViewModel$attach$1$onShowRemindBannerItemView$1(DeviceTabV4ViewModel.this, remindType, null), 1, null);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$onConnectSuccess$2, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$onConnectSuccess$2", "Lcom/xiaomi/wearable/hm/g$a;", "", "code", "", "onException", "(I)V", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass2 extends g.a {
        final /* synthetic */ WearableDeviceModel $deviceModel;

        public AnonymousClass2(WearableDeviceModel wearableDeviceModel) {
            this.$deviceModel = wearableDeviceModel;
        }

        @Override // com.xiaomi.wearable.hm.g
        public void onException(int code) {
            DeviceTabV4ViewModel.this.getConnectStatus().postValue(new ConnectStatus(code, this.$deviceModel, 0));
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$readN62SWatchInfo$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006J\u0017\u0010\t\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$readN62SWatchInfo$1", "Lti7;", "Lcom/xiaomi/fitness/device/manager/export/bean/WatchInfo;", "result", "", "onResult", "(Lcom/xiaomi/fitness/device/manager/export/bean/WatchInfo;)V", "", "errorCode", "onError", "(I)V", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C21921 implements ti7<WatchInfo> {
        final /* synthetic */ WearableDeviceModel $deviceModel;

        public C21921(WearableDeviceModel wearableDeviceModel) {
            this.$deviceModel = wearableDeviceModel;
        }

        @Override // defpackage.ti7
        public void onError(int errorCode) {
            Logger.i(DeviceTabV4ViewModel.this.getTAG(), "readN62SWatchInfo  readWatchInfo error");
        }

        @Override // defpackage.ti7
        public /* bridge */ /* synthetic */ void onResult(WatchInfo watchInfo) {
            onResult2(watchInfo);
        }

        /* JADX INFO: renamed from: onResult, reason: avoid collision after fix types in other method */
        public void onResult2(WatchInfo result) {
            String firmwareVersion;
            Logger.i(DeviceTabV4ViewModel.this.getTAG(), "readN62SWatchInfo onResult" + (result != null ? result.getFirmwareVersion() : null));
            N62SVersionPreference n62SVersionPreference = N62SVersionPreference.INSTANCE;
            String did = this.$deviceModel.getDid();
            if (result == null || (firmwareVersion = result.getFirmwareVersion()) == null) {
                firmwareVersion = "";
            }
            n62SVersionPreference.setN62SVersion(did, firmwareVersion);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$recordChosenApp$2, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$recordChosenApp$2", f = "DeviceTabViewModelV4.kt", i = {}, l = {1490}, m = "invokeSuspend", n = {}, s = {})
    public static final class C21932 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        public C21932(Continuation<? super C21932> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return DeviceTabV4ViewModel.this.new C21932(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return invoke2(coroutineScope, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                this.label = 1;
                if (DelayKt.delay(VibrateModeProgressBar.sLongToLong, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            DeviceTabV4ViewModel.connectDevice$default(DeviceTabV4ViewModel.this, true, null, 2, null);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C21932) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$switchAccessoryMode$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\"\u0010\n\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u0016¨\u0006\f"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$switchAccessoryMode$1", "Lcom/xiaomi/fitness/device/contact/export/OnSyncCallback;", "onSuccess", "", "did", "", "type", "", "result", "Lcom/xiaomi/fitness/device/contact/export/SyncResult;", "onError", "code", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C21941 implements OnSyncCallback {
        final /* synthetic */ Function1<Boolean, Unit> $block;
        final /* synthetic */ int $mode;
        final /* synthetic */ DeviceTabV4ViewModel this$0;

        /* JADX WARN: Multi-variable type inference failed */
        public C21941(Function1<? super Boolean, Unit> function1, int i, DeviceTabV4ViewModel deviceTabV4ViewModel) {
            this.$block = function1;
            this.$mode = i;
            this.this$0 = deviceTabV4ViewModel;
        }

        @Override // com.xiaomi.fitness.device.contact.export.OnSyncCallback
        public void onError(String did, int type, int code) {
            Logger.i(this.this$0.getTAG(), "setWearMode fail: " + code);
            this.$block.invoke(Boolean.FALSE);
        }

        @Override // com.xiaomi.fitness.device.contact.export.OnSyncCallback
        public void onSuccess(String did, int type, SyncResult result) {
            if (result == null || !result.isSuccess()) {
                onError(did, type, 255);
                return;
            }
            if (did != null) {
                int i = this.$mode;
                DeviceTabV4ViewModel deviceTabV4ViewModel = this.this$0;
                if (i == 1) {
                    deviceTabV4ViewModel.getWearModeSwitchSuccessFromApp().postValue(Boolean.TRUE);
                }
                DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).switchDeviceWearMode(did, 0, true);
            }
            this.$block.invoke(Boolean.TRUE);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabV4ViewModel$unbind$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004J\u0017\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabV4ViewModel$unbind$1", "Lcom/xiaomi/fitness/device/manager/l$a;", "", "onSuccess", "()V", "", "code", "onFail", "(I)V", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class BinderC21951 extends l.a {
        final /* synthetic */ String $did;
        final /* synthetic */ boolean $isWearOS;

        public BinderC21951(boolean z, String str) {
            this.$isWearOS = z;
            this.$did = str;
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onFail(int code) {
            DeviceTabV4ViewModel.this.getMLoadingFlag().postValue(Boolean.FALSE);
            if (code == 604) {
                ToastUtil.INSTANCE.showToast(hmk.common_network_error_retry_later);
            }
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onSuccess() {
            DeviceTabV4ViewModel.this.getMLoadingFlag().postValue(Boolean.FALSE);
            Logger.i(DeviceTabV4ViewModel.this.getTAG(), "unbinddevice success devices num = " + DeviceTabV4ViewModel.access$getMDeviceManager$p(DeviceTabV4ViewModel.this).getDeviceSize());
            AnyExtKt.main$default(null, new DeviceTabV4ViewModel$unbind$1$onSuccess$1(DeviceTabV4ViewModel.this, this.$isWearOS, this.$did, null), 1, null);
        }
    }

    public DeviceTabV4ViewModel(@NotNull WearableDeviceManagerWrapper mDeviceManager, @NotNull DeviceContact mDeviceContact) {
        Intrinsics.checkNotNullParameter(mDeviceManager, "mDeviceManager");
        Intrinsics.checkNotNullParameter(mDeviceContact, "mDeviceContact");
        this.mDeviceManager = mDeviceManager;
        this.mDeviceContact = mDeviceContact;
        this.connectStatus = new MutableLiveData<>();
        this.bondLossRecovery = new MutableLiveData<>();
        this.adapter = LazyKt.lazy(new hi7());
        this.itemBindings = LazyKt.lazy(new li7(this));
        this.tabItems = new ObservableArrayList<>();
        this.settingTabItems = new ObservableArrayList<>();
        this.marketTabItems = new ObservableArrayList<>();
        this.settingItemBindings = LazyKt.lazy(new oh7(this));
        this.settingAdapter = LazyKt.lazy(new ph7());
        this.marketAdapter = LazyKt.lazy(new qh7());
        this.settingViewHolder = new rh7();
        this.bannerAdapter = LazyKt.lazy(new sh7(this));
        this.bannerItemBinding = LazyKt.lazy(new th7(this));
        this.bannerItems = new ObservableArrayList<>();
        this.mAllFeatureCardIds = LazyKt.lazy(new uh7(this));
        this.showConnectPermission = new MutableLiveData<>();
        Boolean bool = Boolean.FALSE;
        this.showOpenLocationSwitch = new MutableLiveData<>(bool);
        this.mLoadingFlag = new MutableLiveData<>();
        this.hasDeviceBannerList = LazyKt.lazy(new vh7());
        this.noDeviceBannerList = LazyKt.lazy(new ii7());
        this.exitAccessory = new MutableLiveData<>();
        this.manageAccessory = new MutableLiveData<>();
        this.existAccessoryMode = new MutableLiveData<>();
        this.wearModeSwitchSuccess = new MutableLiveData<>();
        this.syncDeviceLiveData = new MutableLiveData<>();
        this.wearModeSwitchSuccessFromApp = new MutableLiveData<>();
        this.exitAccessoryModeFromDevice = new MutableLiveData<>();
        this.effectiveExposeBiz = LazyKt.lazy(new ji7());
        this.currentFaceId = "";
        this.faceEventListener = new DeviceTabV4ViewModel$faceEventListener$1(this);
        this.bondLossReceiver = new DeviceTabV4ViewModel$bondLossReceiver$1(this);
        this.mDeviceStatusListener = new DeviceTabV4ViewModel$mDeviceStatusListener$1(this);
        this.mDeviceChangeListener = new ki7(this);
        this.mSyncStateListener = new DeviceTabV4ViewModel$mSyncStateListener$1(this);
        this.mOnAccessoryDeviceStateListener = new DeviceTabV4ViewModel$mOnAccessoryDeviceStateListener$1(this);
        this.updateSettingInfoListener = new DeviceTabV4ViewModel$updateSettingInfoListener$1(this);
        this.deviceChargeStatusDataHandler = new DeviceTabV4ViewModel$deviceChargeStatusDataHandler$1(this);
        this.devicePowerModeListener = new DeviceTabV4ViewModel$devicePowerModeListener$1(this);
        this.checkUpdateListener = new DeviceTabV4ViewModel$checkUpdateListener$1(this);
        this.mGoUncommonUsedAppFlag = new MutableLiveData<>(bool);
        this.mUpdateAppSuccessFlag = new MutableLiveData<>(bool);
        this.mChoiceAppFlag = new MutableLiveData<>(bool);
        this.mChosenApp = 1;
        this.medalToPopLiveData = new MutableLiveData<>();
    }

    public static /* synthetic */ Unit a(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel) {
        return mDeviceChangeListener$lambda$14(deviceTabV4ViewModel, wearableDeviceModel);
    }

    public static final /* synthetic */ void access$checkAndUpdateChosenApp(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        deviceTabV4ViewModel.checkAndUpdateChosenApp();
    }

    public static final /* synthetic */ String access$getCurrentFaceId$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.currentFaceId;
    }

    public static final /* synthetic */ String access$getMCurrentDeviceDid$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.mCurrentDeviceDid;
    }

    public static final /* synthetic */ WearableDeviceManagerWrapper access$getMDeviceManager$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.mDeviceManager;
    }

    public static final /* synthetic */ TabDeviceContentItem access$getMDialStatusItem$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.mDialStatusItem;
    }

    public static final /* synthetic */ TabDeviceBannerSettingsItem access$getMTabDeviceBannerSettingsItem$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.mTabDeviceBannerSettingsItem;
    }

    public static final /* synthetic */ TabContentItem access$getMoreTabItem$p(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.moreTabItem;
    }

    public static final /* synthetic */ void access$onConnectFailure(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, int i, int i2) {
        deviceTabV4ViewModel.onConnectFailure(wearableDeviceModel, i, i2);
    }

    public static final /* synthetic */ void access$onDisconnect(DeviceTabV4ViewModel deviceTabV4ViewModel, String str) {
        deviceTabV4ViewModel.onDisconnect(str);
    }

    public static final /* synthetic */ void access$providerTabDeviceBannerSettingsItem(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        deviceTabV4ViewModel.providerTabDeviceBannerSettingsItem();
    }

    public static final /* synthetic */ void access$readN62SWatchInfo(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        deviceTabV4ViewModel.readN62SWatchInfo();
    }

    public static final /* synthetic */ void access$setConnectedOnBackgroundNeedSync$p(DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z) {
        deviceTabV4ViewModel.connectedOnBackgroundNeedSync = z;
    }

    public static final /* synthetic */ void access$setCurrentFaceId$p(DeviceTabV4ViewModel deviceTabV4ViewModel, String str) {
        deviceTabV4ViewModel.currentFaceId = str;
    }

    public static final /* synthetic */ void access$setMRetryConnectCount$p(DeviceTabV4ViewModel deviceTabV4ViewModel, int i) {
        deviceTabV4ViewModel.mRetryConnectCount = i;
    }

    public static final /* synthetic */ void access$showDevice(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        deviceTabV4ViewModel.showDevice();
    }

    public static final /* synthetic */ void access$updateDialStatus(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, int i) {
        deviceTabV4ViewModel.updateDialStatus(str, i);
    }

    private static final DeviceTabItemAdapter adapter_delegate$lambda$0() {
        return new DeviceTabItemAdapter();
    }

    private final void addAccessoryDeviceItem() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) CollectionsKt.getOrNull(this.mDeviceManager.getAccessoryDeviceModels(), 0);
        if (wearableDeviceModel == null) {
            return;
        }
        TabAccessoryDeviceContentItem tabAccessoryDeviceContentItem = new TabAccessoryDeviceContentItem(wearableDeviceModel.getDid(), DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getAccessoryMode(wearableDeviceModel.getDid()), 203, 203, a9k.f560r, StringExtKt.toLiveData(DeviceModelExtKt.getBeanPreviewUrl(wearableDeviceModel)), StringExtKt.toLiveData(DeviceModelExtKt.getName(wearableDeviceModel)), null, false, null, null, 1920, null);
        this.mAccessoryDeviceContentItem = tabAccessoryDeviceContentItem;
        tabAccessoryDeviceContentItem.setStatus(wearableDeviceModel.getDeviceStatus());
        Logger.i(getTAG(), "AccessoryInfo", wearableDeviceModel.getDeviceInfo() + " isAccessory = " + com.xiaomi.fit.device.extensions.DeviceModelExtKt.isAccessory(wearableDeviceModel) + ",isConnected = " + wearableDeviceModel.isDeviceConnected() + ",deviceStatus=" + wearableDeviceModel.getDeviceStatus() + ",isCurrent" + wearableDeviceModel.isCurrent() + ",mode=" + this.mDeviceManager.getAccessoryMode(wearableDeviceModel.getDid()), new Object[0]);
        ObservableArrayList<TabContentItem> observableArrayList = this.tabItems;
        TabAccessoryDeviceContentItem tabAccessoryDeviceContentItem2 = this.mAccessoryDeviceContentItem;
        Intrinsics.checkNotNull(tabAccessoryDeviceContentItem2);
        observableArrayList.add(tabAccessoryDeviceContentItem2);
    }

    private final void addBannerItemWhenHasDevice() {
        ObservableArrayList<TabContentItem> observableArrayList = this.tabItems;
        if (observableArrayList == null || !observableArrayList.isEmpty()) {
            Iterator<TabContentItem> it = observableArrayList.iterator();
            while (it.hasNext()) {
                if (it.next().getType() == 3) {
                    return;
                }
            }
        }
        getHasDeviceBannerList().clear();
        getNoDeviceBannerList().clear();
        List<BannerItemV2> hasDeviceBannerList = getHasDeviceBannerList();
        List listListOf = CollectionsKt.listOf(12);
        String lowerCase = RegionExtKt.getInstance(RegionManager.INSTANCE).getLocalCountry().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        fillBannerList(hasDeviceBannerList, new BannerRequestParamV2(listListOf, lowerCase, null, null, 12, null), true);
    }

    private final void addBannerItemWhenNoDevice() {
        ObservableArrayList<TabContentItem> observableArrayList = this.tabItems;
        if (observableArrayList == null || !observableArrayList.isEmpty()) {
            Iterator<TabContentItem> it = observableArrayList.iterator();
            while (it.hasNext()) {
                if (it.next().getType() == 3) {
                    return;
                }
            }
        }
        getHasDeviceBannerList().clear();
        getNoDeviceBannerList().clear();
        List<BannerItemV2> noDeviceBannerList = getNoDeviceBannerList();
        List listListOf = CollectionsKt.listOf(11);
        String lowerCase = RegionExtKt.getInstance(RegionManager.INSTANCE).getLocalCountry().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        fillBannerList(noDeviceBannerList, new BannerRequestParamV2(listListOf, lowerCase, null, null, 12, null), false);
    }

    private final void addFaceEntranceItem(WearableDeviceModel currentModel) {
        ObservableArrayList<TabContentItem> observableArrayList = this.tabItems;
        if (observableArrayList == null || !observableArrayList.isEmpty()) {
            for (TabContentItem tabContentItem : observableArrayList) {
                TabFaceEntranceItem tabFaceEntranceItem = this.mFaceEntranceItem;
                if (tabFaceEntranceItem != null && tabContentItem.getType() == tabFaceEntranceItem.getType()) {
                    return;
                }
            }
        }
        TabContentItem tabContentItem2 = getContentItems().get(102);
        if (tabContentItem2 != null) {
            TabFaceEntranceItem tabFaceEntranceItem2 = tabContentItem2 instanceof TabFaceEntranceItem ? (TabFaceEntranceItem) tabContentItem2 : new TabFaceEntranceItem();
            this.mFaceEntranceItem = tabFaceEntranceItem2;
            this.tabItems.add(tabFaceEntranceItem2);
            TabFaceEntranceItem tabFaceEntranceItem3 = this.mFaceEntranceItem;
            if (tabFaceEntranceItem3 != null) {
                tabFaceEntranceItem3.onDeviceChanged(currentModel.getDid());
            }
            TabFaceEntranceItem tabFaceEntranceItem4 = this.mFaceEntranceItem;
            if (tabFaceEntranceItem4 != null) {
                tabFaceEntranceItem4.getItemEnable(currentModel);
            }
        }
        List<FaceData> faceEntranceItem = DevicePreference.INSTANCE.getFaceEntranceItem(currentModel.getDid());
        TabFaceEntranceItem tabFaceEntranceItem5 = this.mFaceEntranceItem;
        if (tabFaceEntranceItem5 != null) {
            tabFaceEntranceItem5.setDataList(faceEntranceItem);
        }
        TabFaceEntranceItem tabFaceEntranceItem6 = this.mFaceEntranceItem;
        if (tabFaceEntranceItem6 != null) {
            tabFaceEntranceItem6.setLoadTime(System.currentTimeMillis());
        }
        FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).requestLoadFaceEntranceV4(currentModel, new ci7(this, currentModel), new di7(this));
    }

    private static final Unit addFaceEntranceItem$lambda$26(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, FaceEntranceRespWrapper it) {
        TabFaceEntranceItem tabFaceEntranceItem;
        Intrinsics.checkNotNullParameter(it, "it");
        Logger.i(deviceTabV4ViewModel.getTAG(), "requestLoadFaceEntrance success: " + it);
        FaceSizeConstant.INSTANCE.setCurrentType(it.getDeviceConfig().getShape());
        List<FaceData> faceList = it.getFaceList();
        if (faceList != null && !faceList.isEmpty()) {
            TabFaceEntranceItem tabFaceEntranceItem2 = deviceTabV4ViewModel.mFaceEntranceItem;
            if (tabFaceEntranceItem2 != null) {
                tabFaceEntranceItem2.setDataList(it.getFaceList());
            }
            DevicePreference devicePreference = DevicePreference.INSTANCE;
            String did = wearableDeviceModel.getDid();
            List<FaceData> faceList2 = it.getFaceList();
            Intrinsics.checkNotNull(faceList2);
            devicePreference.setFaceEntranceItem(did, faceList2);
        }
        boolean enableMarketV4 = FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).getEnableMarketV4();
        if ((!AppUtil.INSTANCE.isPlayChannel() || enableMarketV4) && (tabFaceEntranceItem = deviceTabV4ViewModel.mFaceEntranceItem) != null) {
            tabFaceEntranceItem.setRecentOnlineCount(enableMarketV4, it);
        }
        List<FaceData> faceList3 = it.getFaceList();
        if (faceList3 == null) {
            faceList3 = new ArrayList<>();
        }
        int i = 0;
        for (Object obj : faceList3) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String id = ((FaceData) obj).getId();
            StringBuilder sb = new StringBuilder();
            sb.append(i2);
            sb.append("-");
            sb.append(id);
            faceList3.size();
            i = i2;
        }
        OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData(new OneTrackStatBean("expose_v3", "56.51.0.1.36389"), "name", "Device_page_Dialmarket", "type", "device_tab");
        return Unit.INSTANCE;
    }

    private static final Unit addFaceEntranceItem$lambda$27(DeviceTabV4ViewModel deviceTabV4ViewModel, int i) {
        Logger.i(deviceTabV4ViewModel.getTAG(), "requestLoadFaceEntrance fail: " + i);
        return Unit.INSTANCE;
    }

    public static /* synthetic */ ItemBinding b(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return settingItemBindings_delegate$lambda$2(deviceTabV4ViewModel);
    }

    private static final DeviceBannerAdapter bannerAdapter_delegate$lambda$6(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        LifecycleOwner lifecycleOwner = deviceTabV4ViewModel.mOwner;
        if (lifecycleOwner == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mOwner");
            lifecycleOwner = null;
        }
        return new DeviceBannerAdapter(lifecycleOwner);
    }

    private static final ItemBinding bannerItemBinding_delegate$lambda$7(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.getMModel().getBannerItemV4Bindings().bindExtra(v12.f15099k, deviceTabV4ViewModel);
    }

    private final BatteryInfo buildBatteryInfo(DeviceBatteryInfo deviceBatteryInfo, WearableDeviceModel wearableDeviceModel) {
        WearableDeviceInfo deviceInfo = wearableDeviceModel.getDeviceInfo();
        BatteryInfo batteryInfo = deviceInfo.getBatteryInfo();
        batteryInfo.setBattery(deviceBatteryInfo.getLevel());
        batteryInfo.setChargeFull(deviceBatteryInfo.isChargeFull());
        batteryInfo.setCharging(deviceBatteryInfo.isCharging());
        batteryInfo.setLastCharge(deviceBatteryInfo.getLastCharge());
        deviceInfo.setBatteryInfo(batteryInfo);
        wearableDeviceModel.setDeviceInfo(deviceInfo);
        return batteryInfo;
    }

    public static /* synthetic */ NestRecyclerViewItemEffectiveExposeBiz c() {
        return effectiveExposeBiz_delegate$lambda$11();
    }

    private final void checkAndUpdateChosenApp() {
        ChooseCommonApp cloudChosenItem = getCloudChosenItem();
        if (cloudChosenItem == null) {
            ChooseAppPreference chooseAppPreference = ChooseAppPreference.INSTANCE;
            if (chooseAppPreference.getChooseAppLastTime() == 0 && chooseAppPreference.getChooseApp() == -1) {
                chooseAppPreference.setChooseApp(1);
            }
            DeviceSettingManager.INSTANCE.getInstance().addUpdateListener(this.updateSettingInfoListener);
            return;
        }
        String tag = getTAG();
        int chooseCommonApp = cloudChosenItem.getChooseCommonApp();
        long lastUpdateTime = cloudChosenItem.getLastUpdateTime();
        ChooseAppPreference chooseAppPreference2 = ChooseAppPreference.INSTANCE;
        Logger.i(tag, "checkAndUpdateChoiseCommonApp chosenCommonAppItem " + chooseCommonApp + " lastUpdateTime is " + lastUpdateTime + ", local chosen app is " + chooseAppPreference2.getChooseApp() + ", local chosen time is " + chooseAppPreference2.getChooseAppLastTime());
        if (cloudChosenItem.getChooseCommonApp() != 1 && cloudChosenItem.getChooseCommonApp() != 2 && cloudChosenItem.getChooseCommonApp() != 3) {
            chooseAppPreference2.setChooseApp(1);
            return;
        }
        if (chooseAppPreference2.getChooseAppLastTime() <= cloudChosenItem.getLastUpdateTime()) {
            if (chooseAppPreference2.getChooseAppLastTime() >= cloudChosenItem.getLastUpdateTime()) {
                Logger.i(getTAG(), "chosen no need update");
                return;
            }
            Logger.i(getTAG(), "chosen update web");
            chooseAppPreference2.setChooseAppLastTime(cloudChosenItem.getLastUpdateTime());
            chooseAppPreference2.setChooseApp(cloudChosenItem.getChooseCommonApp());
            return;
        }
        Logger.i(getTAG(), "chosen update local " + chooseAppPreference2.getChooseApp());
        recordChosenAppCloud(chooseAppPreference2.getChooseApp(), chooseAppPreference2.getChooseAppLastTime());
    }

    public static /* synthetic */ void checkChosenApp$default(DeviceTabV4ViewModel deviceTabV4ViewModel, Boolean bool, int i, Object obj) {
        if ((i & 1) != 0) {
            bool = Boolean.FALSE;
        }
        deviceTabV4ViewModel.checkChosenApp(bool);
    }

    public static /* synthetic */ void connectDevice$default(DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = null;
        }
        deviceTabV4ViewModel.connectDevice(z, str);
    }

    private final List<Integer> createAllFeatureCardIds() {
        return CollectionsKt.listOf((Object[]) new Integer[]{19, 6, 7, 8, 9, 10, 18, 11, 12});
    }

    private final TabContentItem createNormalContentItem(int itemType, int type) {
        int iBuildTabTitle = getMModel().buildTabTitle(type);
        if (iBuildTabTitle == -1) {
            return new TabContentItem(itemType, type, 0, StringExtKt.toLiveData(""), null, false, null, null, null, null, null, null, null, false, null, null, 65524, null);
        }
        String string = ApplicationExtKt.getApplication().getString(iBuildTabTitle);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        return new TabContentItem(itemType, type, 0, StringExtKt.toLiveData(string), null, false, null, null, null, null, null, null, null, false, null, null, 65524, null);
    }

    public static /* synthetic */ DeviceTabItemAdapter d() {
        return adapter_delegate$lambda$0();
    }

    private final boolean displayErrorCode(int errorCode) {
        return errorCode == 2005 && !WearOSPreference.INSTANCE.getIS_WEAROS_BIND();
    }

    public static /* synthetic */ DeviceSettingsItemViewHolder e(View view, int i) {
        return settingViewHolder$lambda$5(view, i);
    }

    private static final NestRecyclerViewItemEffectiveExposeBiz effectiveExposeBiz_delegate$lambda$11() {
        return new NestRecyclerViewItemEffectiveExposeBiz();
    }

    public static /* synthetic */ ItemBinding f(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return bannerItemBinding_delegate$lambda$7(deviceTabV4ViewModel);
    }

    private final void fillBannerList(List<BannerItemV2> observableList, BannerRequestParamV2 bannerRequestParam, boolean hasDevice) {
        BannerExtKt.getINSTANCE(IBanner.INSTANCE).getBannerListAsyncV2(bannerRequestParam, new ei7(observableList, hasDevice, this), new fi7(bannerRequestParam), ViewModelKt.getViewModelScope(this));
    }

    private static final Unit fillBannerList$lambda$43(List list, boolean z, DeviceTabV4ViewModel deviceTabV4ViewModel, List it) {
        boolean z2;
        int i;
        Intrinsics.checkNotNullParameter(it, "it");
        list.clear();
        Iterator it2 = it.iterator();
        while (it2.hasNext()) {
            BannerItemV2 bannerItemV2 = (BannerItemV2) it2.next();
            if ((z && bannerItemV2.getResourceId() == 12) || (!z && bannerItemV2.getResourceId() == 11)) {
                list.add(bannerItemV2);
            }
            ObservableArrayList<TabContentItem> observableArrayList = deviceTabV4ViewModel.tabItems;
            int i2 = 0;
            if (observableArrayList != null && observableArrayList.isEmpty()) {
                z2 = false;
                break;
            }
            Iterator<TabContentItem> it3 = observableArrayList.iterator();
            while (true) {
                if (!it3.hasNext()) {
                    z2 = false;
                    break;
                }
                if (it3.next().getType() == 3) {
                    z2 = true;
                    break;
                }
            }
            List list2 = list;
            if (!list2.isEmpty() && !z2) {
                TabContentItem tabContentItemCreateNormalContentItem = deviceTabV4ViewModel.createNormalContentItem(18, 3);
                Iterator<TabContentItem> it4 = deviceTabV4ViewModel.tabItems.iterator();
                int i3 = 0;
                while (true) {
                    i = -1;
                    if (!it4.hasNext()) {
                        i3 = -1;
                        break;
                    }
                    if (it4.next().getType() == 102) {
                        break;
                    }
                    i3++;
                }
                Iterator<TabContentItem> it5 = deviceTabV4ViewModel.tabItems.iterator();
                while (it5.hasNext()) {
                    if (it5.next().getType() == 1) {
                        i = i2;
                        break;
                    }
                    i2++;
                }
                Logger.i(deviceTabV4ViewModel.getTAG(), "fillBannerList " + list.size() + " " + z);
                if (i3 >= 0) {
                    deviceTabV4ViewModel.tabItems.add(i3 + 1, tabContentItemCreateNormalContentItem);
                } else if (i > 0) {
                    deviceTabV4ViewModel.tabItems.add(i, tabContentItemCreateNormalContentItem);
                } else {
                    deviceTabV4ViewModel.tabItems.add(tabContentItemCreateNormalContentItem);
                }
            }
            deviceTabV4ViewModel.bannerItems.clear();
            deviceTabV4ViewModel.bannerItems.addAll(list2);
        }
        NestRecyclerViewItemEffectiveExposeBiz.trackOnDataChange$default(deviceTabV4ViewModel.getEffectiveExposeBiz(), "device banner data change", null, 2, null);
        return Unit.INSTANCE;
    }

    private static final Unit fillBannerList$lambda$44(BannerRequestParamV2 bannerRequestParamV2, ApiException it) {
        Intrinsics.checkNotNullParameter(it, "it");
        l62.a("DeviceTabViewModel fillBannerList " + bannerRequestParamV2, it);
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Unit g(DeviceTabV4ViewModel deviceTabV4ViewModel, int i) {
        return addFaceEntranceItem$lambda$27(deviceTabV4ViewModel, i);
    }

    private final ChooseCommonApp getCloudChosenItem() {
        Object objValueToObject = DeviceSettingManager.INSTANCE.getInstance().readSettingItem("xiaomiwear_app", DeviceSettingConfigKey.KEY_CHOOSE_COMMON_APP).valueToObject(ChooseCommonApp.class);
        if (objValueToObject instanceof ChooseCommonApp) {
            return (ChooseCommonApp) objValueToObject;
        }
        return null;
    }

    private final NestRecyclerViewItemEffectiveExposeBiz getEffectiveExposeBiz() {
        return (NestRecyclerViewItemEffectiveExposeBiz) this.effectiveExposeBiz.getValue();
    }

    private final List<BannerItemV2> getHasDeviceBannerList() {
        return (List) this.hasDeviceBannerList.getValue();
    }

    private final List<BannerItemV2> getNoDeviceBannerList() {
        return (List) this.noDeviceBannerList.getValue();
    }

    public static /* synthetic */ List h() {
        return hasDeviceBannerList_delegate$lambda$9();
    }

    private static final List hasDeviceBannerList_delegate$lambda$9() {
        return new ArrayList();
    }

    private final boolean hasLocationPermission() {
        return PermissionsUtil.checkPermission("android.permission.ACCESS_FINE_LOCATION");
    }

    private final boolean hasSupportDevice() {
        Iterator it = DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceModels().iterator();
        while (it.hasNext()) {
            if (DeviceExtKt.isSupportDevice(((WearableDeviceModel) it.next()).getProduct())) {
                return true;
            }
        }
        return false;
    }

    public static /* synthetic */ boolean i(WearableDeviceModel wearableDeviceModel) {
        return providerTabDeviceBannerSettingsItem$lambda$13$lambda$12(wearableDeviceModel);
    }

    private final boolean isActiveDeviceModel(String did) {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceModel(did);
        return wearableDeviceModel != null && wearableDeviceModel.isConnectActive();
    }

    private final boolean isStatusEquals() {
        ObservableInt status;
        TabDeviceContentItem tabDeviceContentItem = this.mDialStatusItem;
        return ((tabDeviceContentItem == null || (status = tabDeviceContentItem.getStatus()) == null) ? 1 : status.get()) == this.mDeviceManager.getDeviceStatus();
    }

    private static final ItemBinding itemBindings_delegate$lambda$1(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.getMModel().getTabItemBindings().bindExtra(v12.f15099k, deviceTabV4ViewModel);
    }

    public static /* synthetic */ Unit j(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, FaceEntranceRespWrapper faceEntranceRespWrapper) {
        return addFaceEntranceItem$lambda$26(deviceTabV4ViewModel, wearableDeviceModel, faceEntranceRespWrapper);
    }

    public static /* synthetic */ void k(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, DeviceBatteryInfo deviceBatteryInfo) {
        onConnectSuccess$lambda$15(deviceTabV4ViewModel, wearableDeviceModel, deviceBatteryInfo);
    }

    public static /* synthetic */ Unit l(DeviceTabV4ViewModel deviceTabV4ViewModel, Function3 function3, TabContentItem tabContentItem, int i, View view) {
        return setOnItemClickListener$lambda$36(deviceTabV4ViewModel, function3, tabContentItem, i, view);
    }

    public static /* synthetic */ void loadDevice$default(DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        deviceTabV4ViewModel.loadDevice(z);
    }

    private final void loadWearOsWatch(WearableDeviceModel currentModel) {
        FaceWearOsHelperExtKt.getInstance(FaceWearOsHelper.INSTANCE).loadWearOsWatch(currentModel);
    }

    public static /* synthetic */ Unit m(BannerRequestParamV2 bannerRequestParamV2, ApiException apiException) {
        return fillBannerList$lambda$44(bannerRequestParamV2, apiException);
    }

    private static final List mAllFeatureCardIds_delegate$lambda$8(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.createAllFeatureCardIds();
    }

    private static final Unit mDeviceChangeListener$lambda$14(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel) {
        MutableLiveData<String> rightTextWithDot;
        Logger.i(deviceTabV4ViewModel.getTAG(), "current device(" + deviceTabV4ViewModel.mCurrentDeviceDid + ") has been changed to " + wearableDeviceModel);
        TabContentItem tabContentItem = deviceTabV4ViewModel.moreTabItem;
        if (tabContentItem != null && (rightTextWithDot = tabContentItem.getRightTextWithDot()) != null) {
            rightTextWithDot.postValue("");
        }
        deviceTabV4ViewModel.bondLossRecovery.postValue(2023);
        if (wearableDeviceModel == null) {
            deviceTabV4ViewModel.tabItems.clear();
            deviceTabV4ViewModel.settingTabItems.clear();
            deviceTabV4ViewModel.marketTabItems.clear();
        }
        Logger.i(deviceTabV4ViewModel.getTAG(), "device change devices num = " + deviceTabV4ViewModel.mDeviceManager.getDeviceSize());
        deviceTabV4ViewModel.loadDevice(false);
        deviceTabV4ViewModel.readN62SWatchInfo();
        return Unit.INSTANCE;
    }

    private static final DeviceMarketItemAdapter marketAdapter_delegate$lambda$4() {
        return new DeviceMarketItemAdapter();
    }

    public static /* synthetic */ Unit n(DeviceTabV4ViewModel deviceTabV4ViewModel, int i) {
        return realConnect$lambda$32(deviceTabV4ViewModel, i);
    }

    private final boolean needLocationDevice() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
        return (wearableDeviceModel != null && DeviceModelExtKt.isHuaMi(wearableDeviceModel)) || (wearableDeviceModel != null && DeviceModelExtKt.isBle(wearableDeviceModel) && ProxyManagerKt.isBleOpen());
    }

    private static final List noDeviceBannerList_delegate$lambda$10() {
        return new ArrayList();
    }

    public static /* synthetic */ List o() {
        return noDeviceBannerList_delegate$lambda$10();
    }

    private final void onConnectFailure(WearableDeviceModel deviceModel, int errorCode, int retryTimes) {
        int i;
        if (deviceModel == null || !deviceModel.isCurrent()) {
            return;
        }
        if (DeviceModelExtKt.isBluetooth(deviceModel) || com.xiaomi.fitness.device.manager.export.DeviceModelExtKt.isDualCoreWearOS(deviceModel)) {
            if (retryTimes > 0) {
                i = retryTimes;
            } else {
                i = this.mRetryConnectCount;
                if (i <= 0) {
                    i = 0;
                }
            }
            if (this.userClick || displayErrorCode(errorCode)) {
                this.connectStatus.postValue(new ConnectStatus(errorCode, deviceModel, i));
            }
            this.userClick = false;
        }
        Logger.i(getTAG(), "isActiveDeviceModel = " + isActiveDeviceModel(deviceModel.getDid()) + ", retryTimes = " + retryTimes);
        if (isActiveDeviceModel(deviceModel.getDid())) {
            updateDialStatus(deviceModel.getDid(), 1);
        } else {
            updateDialStatus(deviceModel.getDid(), 9);
        }
    }

    private final void onConnectSuccess(String did, WearableDeviceModel deviceModel, boolean isNeedSync, boolean needShowDevice) {
        MutableLiveData<String> faceUrl;
        Logger.i(getTAG(), "onConnectSuccess  refresh ui " + deviceModel + " isAccessory = " + (deviceModel != null ? Boolean.valueOf(com.xiaomi.fit.device.extensions.DeviceModelExtKt.isAccessory(deviceModel)) : null));
        if (deviceModel == null || !Intrinsics.areEqual(did, this.mCurrentDeviceDid) || com.xiaomi.fit.device.extensions.DeviceModelExtKt.isAccessory(deviceModel)) {
            return;
        }
        this.userClick = true;
        if (needShowDevice) {
            showDevice();
        }
        NotifyHelperExtKt.getNotificationManagerExport().updateRemindWhenConnectDevice(did);
        DeviceSettingsSyncHelperExtKt.getInstance(DeviceSettingsSyncHelper.INSTANCE).oneTrackDeviceSetting();
        if (!isStatusEquals()) {
            updateDialStatus(did, 3);
            if (deviceModel.getDeviceStatus() != 3) {
                deviceModel.setDeviceStatus(3);
            }
        }
        if (isNeedSync) {
            this.syncDeviceLiveData.postValue(deviceModel);
        }
        this.connectStatus.setValue(new ConnectStatus(2004, deviceModel, 0));
        TabDeviceContentItem tabDeviceContentItem = this.mDialStatusItem;
        if (tabDeviceContentItem != null && (faceUrl = tabDeviceContentItem.getFaceUrl()) != null) {
            faceUrl.postValue(DeviceStatusView.URL_LOAD_WATCH_FACE);
        }
        if (DeviceModelExtKt.isHuaMi(deviceModel)) {
            HuamiApiCaller huaMiApiCaller = this.mDeviceManager.getHuaMiApiCaller(DeviceModelExtKt.getMac(deviceModel));
            if (huaMiApiCaller != null) {
                huaMiApiCaller.setBatteryChangedCallback(new xh7(this, deviceModel));
            }
            if (huaMiApiCaller != null) {
                huaMiApiCaller.registerBTExceptionListener(new AnonymousClass2(deviceModel));
            }
        }
    }

    public static /* synthetic */ void onConnectSuccess$default(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, WearableDeviceModel wearableDeviceModel, boolean z, boolean z2, int i, Object obj) {
        if ((i & 4) != 0) {
            z = true;
        }
        if ((i & 8) != 0) {
            z2 = true;
        }
        deviceTabV4ViewModel.onConnectSuccess(str, wearableDeviceModel, z, z2);
    }

    private static final void onConnectSuccess$lambda$15(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, DeviceBatteryInfo deviceBatteryInfo) {
        Intrinsics.checkNotNull(deviceBatteryInfo);
        deviceTabV4ViewModel.buildBatteryInfo(deviceBatteryInfo, wearableDeviceModel);
        TabDeviceContentItem tabDeviceContentItem = deviceTabV4ViewModel.mDialStatusItem;
        if (tabDeviceContentItem != null) {
            tabDeviceContentItem.setStatus(-100);
        }
    }

    private final void onDisconnect(String did) {
        Logger.i(getTAG(), "onDisconnect  refresh ui");
        if (did == null || !Intrinsics.areEqual(did, this.mCurrentDeviceDid)) {
            Logger.i(getTAG(), did + " is not equal current device(" + this.mCurrentDeviceDid + ")");
            return;
        }
        this.userClick = false;
        if (OhterExtKt.isAppAbandon(ApplicationExtKt.getApplication())) {
            updateDialStatus(did, 8);
        } else if (isActiveDeviceModel(did)) {
            updateDialStatus(did, 4);
        } else {
            updateDialStatus(did, 9);
        }
    }

    public static /* synthetic */ Unit p(List list, boolean z, DeviceTabV4ViewModel deviceTabV4ViewModel, List list2) {
        return fillBannerList$lambda$43(list, z, deviceTabV4ViewModel, list2);
    }

    private final void providerTabDeviceBannerSettingsItem() {
        TabContentItem tabContentItem = getContentItems().get(200);
        if (tabContentItem != null) {
            this.mTabDeviceBannerSettingsItem = tabContentItem instanceof TabDeviceBannerSettingsItem ? (TabDeviceBannerSettingsItem) tabContentItem : new TabDeviceBannerSettingsItem(16, 200, 0, StringExtKt.toLiveData(""), null, false, null, null, null, null, null, new bi7(), 2036, null);
        }
    }

    private static final boolean providerTabDeviceBannerSettingsItem$lambda$13$lambda$12(WearableDeviceModel TabDeviceBannerSettingsItem) {
        Intrinsics.checkNotNullParameter(TabDeviceBannerSettingsItem, "$this$TabDeviceBannerSettingsItem");
        return false;
    }

    public static /* synthetic */ Unit q(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, int i) {
        return updateAccessoryStatus$lambda$47$lambda$46(deviceTabV4ViewModel, str, i);
    }

    public static /* synthetic */ ItemBinding r(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return itemBindings_delegate$lambda$1(deviceTabV4ViewModel);
    }

    private final void readN62SWatchInfo() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
        Logger.i(getTAG(), "readN62SWatchInfo device model: " + (wearableDeviceModel != null ? DeviceModelExtKt.getModel(wearableDeviceModel) : null));
        Set of = SetsKt.setOf((Object[]) new String[]{"mijia.watch.n62s", "miwear.watch.n67cn"});
        if (wearableDeviceModel == null || !of.contains(DeviceModelExtKt.getModel(wearableDeviceModel))) {
            return;
        }
        if (wearableDeviceModel.isDeviceConnected()) {
            wearableDeviceModel.readWatchInfo(new C21921(wearableDeviceModel));
        } else {
            Logger.i(getTAG(), "readN62SWatchInfo isDeviceConnected");
        }
    }

    private final void realConnect(String did, boolean force) {
        Logger.i(getTAG(), "realConnect " + did);
        LifecycleOwner lifecycleOwner = this.mOwner;
        if (lifecycleOwner == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mOwner");
            lifecycleOwner = null;
        }
        BaseActivity baseActivity = (BaseActivity) ((BaseFragment) lifecycleOwner).getActivity();
        if (baseActivity == null) {
            Logger.i(getTAG(), "activity is null, return!");
        } else {
            BluetoothExtKt.openBluetooth(baseActivity, new zh7(did, this, force), new ai7(this));
        }
    }

    public static /* synthetic */ void realConnect$default(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        deviceTabV4ViewModel.realConnect(str, z);
    }

    private static final Unit realConnect$lambda$31(String str, DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z) {
        if (str != null) {
            WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) deviceTabV4ViewModel.mDeviceManager.getDeviceModel(str);
            if (wearableDeviceModel == null) {
                return Unit.INSTANCE;
            }
            if (!wearableDeviceModel.isConnectActive() && !z) {
                Logger.i(deviceTabV4ViewModel.getTAG(), "realConnect: [accessory] phone id not equal");
                deviceTabV4ViewModel.updateAccessoryStatus(str, 9);
                return Unit.INSTANCE;
            }
            deviceTabV4ViewModel.mDeviceManager.connectDevice(str);
        } else {
            WearableDeviceModel wearableDeviceModel2 = (WearableDeviceModel) deviceTabV4ViewModel.mDeviceManager.getCurrentDeviceModel();
            if (wearableDeviceModel2 == null) {
                return Unit.INSTANCE;
            }
            if (!wearableDeviceModel2.isConnectActive() && !z) {
                Logger.i(deviceTabV4ViewModel.getTAG(), "realConnect: phone id not equal");
                deviceTabV4ViewModel.updateDialStatus(wearableDeviceModel2.getDid(), 9);
                return Unit.INSTANCE;
            }
            DevicePreference.INSTANCE.setIN_BIND_NEW_MODE(false);
            DeviceManager.connectDevice$default(deviceTabV4ViewModel.mDeviceManager, null, 1, null);
        }
        return Unit.INSTANCE;
    }

    private static final Unit realConnect$lambda$32(DeviceTabV4ViewModel deviceTabV4ViewModel, int i) {
        Logger.i(deviceTabV4ViewModel.getTAG(), "Cannot open bluetooth");
        return Unit.INSTANCE;
    }

    private final void recordChosenAppCloud(int app, long time) {
        Logger.i(getTAG(), "recordChosenApp upload to cloud app = " + app + " , time = " + time);
        ChooseCommonApp chooseCommonApp = new ChooseCommonApp();
        chooseCommonApp.setLastUpdateTime(time);
        chooseCommonApp.setChooseCommonApp(app);
        DeviceSettingManager.INSTANCE.getInstance().sendUserSettingItem(DeviceSettingConfigKey.KEY_CHOOSE_COMMON_APP, chooseCommonApp, false);
    }

    private final void refreshAccessoryWearMode() {
        Logger.i(getTAG(), "refreshAccessoryMode ");
        for (WearableDeviceModel wearableDeviceModel : DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getAccessoryDeviceModels()) {
            if (wearableDeviceModel.isDeviceConnected()) {
                updateAccessoryStatus(wearableDeviceModel.getDid(), 3);
            } else {
                connectDevice(true, wearableDeviceModel.getDid());
            }
        }
    }

    private final void requestLocationSwitch(String did, boolean force) {
        long last_request_location_switch = DevicePreference.INSTANCE.getLAST_REQUEST_LOCATION_SWITCH();
        if (force || System.currentTimeMillis() - last_request_location_switch >= 172800000) {
            this.showOpenLocationSwitch.postValue(Boolean.TRUE);
        }
    }

    public static /* synthetic */ void requestLocationSwitch$default(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        deviceTabV4ViewModel.requestLocationSwitch(str, z);
    }

    private final void requestPermissionIfNeed(String did, boolean force) {
        DevicePreference devicePreference = DevicePreference.INSTANCE;
        long last_request_permission_time = devicePreference.getLAST_REQUEST_PERMISSION_TIME();
        long last_show_blue_data_dialog_time = devicePreference.getLAST_SHOW_BLUE_DATA_DIALOG_TIME();
        if (!force) {
            if (last_request_permission_time > 0 || devicePreference.getAGREE_BLUE_DATA_ENABLE()) {
                if (System.currentTimeMillis() - last_request_permission_time < 172800000) {
                    return;
                }
            } else if (System.currentTimeMillis() - last_show_blue_data_dialog_time < 172800000) {
                return;
            }
        }
        this.showConnectPermission.postValue(new Pair<>(Boolean.TRUE, did));
    }

    public static /* synthetic */ void requestPermissionIfNeed$default(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        deviceTabV4ViewModel.requestPermissionIfNeed(str, z);
    }

    public static /* synthetic */ Unit s(String str, int i, DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z) {
        return updateAccessoryStatus$lambda$47$lambda$46$lambda$45(str, i, deviceTabV4ViewModel, z);
    }

    private static final Unit setOnItemClickListener$lambda$36(DeviceTabV4ViewModel deviceTabV4ViewModel, Function3 function3, TabContentItem tabContentItem, int i, View itemView) {
        Intrinsics.checkNotNullParameter(tabContentItem, "tabContentItem");
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        int id = itemView.getId();
        if (id != wck.b0) {
            if (id == wck.a) {
                if (!(tabContentItem instanceof TabAccessoryDeviceContentItem)) {
                    return Unit.INSTANCE;
                }
                TabAccessoryDeviceContentItem tabAccessoryDeviceContentItem = (TabAccessoryDeviceContentItem) tabContentItem;
                int i2 = tabAccessoryDeviceContentItem.getStatus().get();
                String did = tabAccessoryDeviceContentItem.getDid();
                switch (i2) {
                    case 1:
                    case 4:
                    case 8:
                    case 9:
                        deviceTabV4ViewModel.manageAccessory.postValue(did);
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                        Logger.i(deviceTabV4ViewModel.getTAG(), "currentdid " + deviceTabV4ViewModel.mCurrentDeviceDid);
                        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) deviceTabV4ViewModel.mDeviceManager.getCurrentDeviceModel();
                        if (wearableDeviceModel == null) {
                            deviceTabV4ViewModel.switchAccessoryMode(0, did, true, new yh7(did));
                        } else {
                            deviceTabV4ViewModel.exitAccessory.postValue(new Pair<>(did, wearableDeviceModel.getDid()));
                        }
                        break;
                }
            } else {
                function3.invoke(tabContentItem, Integer.valueOf(i), itemView);
            }
        } else {
            Logger.i(deviceTabV4ViewModel.getTAG(), "OnChildClickListener  isConnectd:" + deviceTabV4ViewModel.mDeviceManager.isConnected() + "  status:" + DeviceStatus.INSTANCE.statusToString(deviceTabV4ViewModel.mDeviceManager.getDeviceStatus()));
            if (deviceTabV4ViewModel.mDeviceManager.isConnected()) {
                wic.a(ApplicationExtKt.getApplication());
                syncDevice$default(deviceTabV4ViewModel, (WearableDeviceModel) deviceTabV4ViewModel.mDeviceManager.getCurrentDeviceModel(), false, 2, null);
                OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData(new OneTrackStatBean("click_v3", "56.51.0.1.36388"), "name", "Device_page_Connect", "status", 1);
            } else if (deviceTabV4ViewModel.mDeviceManager.getDeviceStatus() == 4 || deviceTabV4ViewModel.mDeviceManager.getDeviceStatus() == 1 || deviceTabV4ViewModel.mDeviceManager.getDeviceStatus() == 9) {
                Logger.i(deviceTabV4ViewModel.getTAG(), "OnChildClickListener  connectDevice");
                deviceTabV4ViewModel.mRetryConnectCount++;
                DevicePreference.INSTANCE.setIN_BIND_NEW_MODE(false);
                connectDevice$default(deviceTabV4ViewModel, true, null, 2, null);
                OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData(new OneTrackStatBean("click_v3", "56.51.0.1.36388"), "name", "Device_page_Connect", "status", 0);
                deviceTabV4ViewModel.userClick = true;
            } else if (deviceTabV4ViewModel.mDeviceManager.getDeviceStatus() == 8) {
                deviceTabV4ViewModel.mChoiceAppFlag.postValue(Boolean.TRUE);
            }
        }
        return Unit.INSTANCE;
    }

    private static final Unit setOnItemClickListener$lambda$36$lambda$35$lambda$34(String str, boolean z) {
        WearableDeviceManager.Companion companion = WearableDeviceManager.INSTANCE;
        DeviceManagerExtKt.getInstance(companion).switchDevice(DeviceManagerExtKt.getInstance(companion).getDeviceModel(str));
        return Unit.INSTANCE;
    }

    private static final DeviceSettingsItemAdapter settingAdapter_delegate$lambda$3() {
        return new DeviceSettingsItemAdapter();
    }

    private static final ItemBinding settingItemBindings_delegate$lambda$2(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return deviceTabV4ViewModel.getMModel().getSettingTabItemBindings().bindExtra(v12.f15099k, deviceTabV4ViewModel);
    }

    private static final DeviceSettingsItemViewHolder settingViewHolder$lambda$5(View view, int i) {
        Intrinsics.checkNotNull(view);
        return new DeviceSettingsItemViewHolder(view);
    }

    private final void showDevice() {
        MutableLiveData<String> rightTextWithDot;
        ObservableBoolean remind;
        TabDeviceContentItem tabDeviceContentItem;
        TabDeviceContentItem tabDeviceContentItem2;
        Logger.i(getTAG(), "showDevice() called");
        this.tabItems.clear();
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
        if (wearableDeviceModel == null) {
            return;
        }
        if (DeviceModelExtKt.isWearableDevice(wearableDeviceModel)) {
            String name = DeviceModelExtKt.getName(wearableDeviceModel);
            String bigIcon = DeviceModelExtKt.getBigIcon(wearableDeviceModel);
            TabContentItem tabContentItem = getContentItems().get(101);
            if (tabContentItem != null) {
                if (tabContentItem instanceof TabDeviceContentItem) {
                    tabDeviceContentItem = (TabDeviceContentItem) tabContentItem;
                    tabDeviceContentItem.setIconUrl(bigIcon != null ? StringExtKt.toLiveData(bigIcon) : null);
                    tabContentItem.getTitle().setValue(name);
                } else {
                    Logger.e(getTAG(), "getDeviceContentItem 101 error");
                    tabDeviceContentItem = new TabDeviceContentItem(101, 5, 0, bigIcon != null ? StringExtKt.toLiveData(bigIcon) : null, StringExtKt.toLiveData(name), null, false, null, null, 484, null);
                }
                this.mDialStatusItem = tabDeviceContentItem;
                tabDeviceContentItem.setSupportSwitchWearMode((DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceCapability(wearableDeviceModel.getDid()) & 1024) != 0);
                int currentPowerMode = IDevicePowerModeManagerExtKt.getInstance(IDevicePowerModeManager.INSTANCE).getCurrentPowerMode();
                if (currentPowerMode == 0) {
                    TabDeviceContentItem tabDeviceContentItem3 = this.mDialStatusItem;
                    if (tabDeviceContentItem3 != null) {
                        tabDeviceContentItem3.setCoreType(DeviceStatusView.CoreType.INSTANCE.getDefault());
                    }
                } else if (currentPowerMode == 1) {
                    TabDeviceContentItem tabDeviceContentItem4 = this.mDialStatusItem;
                    if (tabDeviceContentItem4 != null) {
                        tabDeviceContentItem4.setCoreType(DeviceStatusView.CoreType.INSTANCE.getE_Core());
                    }
                } else if (currentPowerMode == 2 && (tabDeviceContentItem2 = this.mDialStatusItem) != null) {
                    tabDeviceContentItem2.setCoreType(DeviceStatusView.CoreType.INSTANCE.getP_Core());
                }
                this.tabItems.add(this.mDialStatusItem);
            }
            addAccessoryDeviceItem();
            providerTabDeviceBannerSettingsItem();
            this.tabItems.add(this.mTabDeviceBannerSettingsItem);
            if (!DeviceModelExtKt.isWearOS(wearableDeviceModel)) {
                addFaceEntranceItem(wearableDeviceModel);
            }
            addBannerItemWhenHasDevice();
            Logger.i(getTAG(), "ChoiseCommonAppPreference.getChoiseCommonApp() " + ChooseAppPreference.INSTANCE.getChooseApp());
            if (OhterExtKt.isAppAbandon(ApplicationExtKt.getApplication())) {
                TabDeviceContentItem tabDeviceContentItem5 = this.mDialStatusItem;
                Intrinsics.checkNotNull(tabDeviceContentItem5);
                tabDeviceContentItem5.setStatus(8);
            } else if (!wearableDeviceModel.isConnectActive()) {
                TabDeviceContentItem tabDeviceContentItem6 = this.mDialStatusItem;
                Intrinsics.checkNotNull(tabDeviceContentItem6);
                tabDeviceContentItem6.setStatus(9);
            } else if (wearableDeviceModel.isDeviceConnected()) {
                TabDeviceContentItem tabDeviceContentItem7 = this.mDialStatusItem;
                Intrinsics.checkNotNull(tabDeviceContentItem7);
                int i = tabDeviceContentItem7.getStatus().get();
                if (i != 7 && i != 6 && i != 3) {
                    TabDeviceContentItem tabDeviceContentItem8 = this.mDialStatusItem;
                    Intrinsics.checkNotNull(tabDeviceContentItem8);
                    tabDeviceContentItem8.setStatus(3);
                }
            } else {
                TabDeviceContentItem tabDeviceContentItem9 = this.mDialStatusItem;
                Intrinsics.checkNotNull(tabDeviceContentItem9);
                tabDeviceContentItem9.setStatus(4);
            }
            this.marketTabItems.clear();
            if (com.xiaomi.fitness.device.manager.extensions.DeviceModelExtKt.isSupportFeatureCardV4(wearableDeviceModel, 15)) {
                this.marketTabItems.addAll(getMModel().getDeviceFeatureList(wearableDeviceModel, 15));
                if (!this.marketTabItems.isEmpty()) {
                    this.tabItems.add(createNormalContentItem(11, 15));
                }
            }
            this.tabItems.add(createNormalContentItem(11, 1));
            this.settingTabItems.clear();
            List<Integer> mAllFeatureCardIds = getMAllFeatureCardIds();
            ArrayList arrayList = new ArrayList();
            for (Object obj : mAllFeatureCardIds) {
                if (com.xiaomi.fitness.device.manager.extensions.DeviceModelExtKt.isSupportFeatureCardV4(wearableDeviceModel, ((Number) obj).intValue())) {
                    arrayList.add(obj);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                TabContentItem deviceNormalTabList = getMModel().getDeviceNormalTabList(wearableDeviceModel, ((Number) it.next()).intValue());
                if (deviceNormalTabList != null) {
                    this.settingTabItems.add(deviceNormalTabList);
                }
            }
            for (TabContentItem tabContentItem2 : this.settingTabItems) {
                if (tabContentItem2.getType() == 11) {
                    this.moreTabItem = tabContentItem2;
                    boolean showUpdateRedPoint = getCheckUpdate().getShowUpdateRedPoint();
                    TabContentItem tabContentItem3 = this.moreTabItem;
                    Logger.i("updatehint", "more tab item showUpdateRedPoint " + showUpdateRedPoint + " current " + ((tabContentItem3 == null || (remind = tabContentItem3.getRemind()) == null) ? null : Boolean.valueOf(remind.get())));
                    TabContentItem tabContentItem4 = this.moreTabItem;
                    if (tabContentItem4 != null && (rightTextWithDot = tabContentItem4.getRightTextWithDot()) != null) {
                        rightTextWithDot.postValue(showUpdateRedPoint ? " " : "");
                    }
                    getCheckUpdate().addHasUpdateListener(this.checkUpdateListener);
                }
            }
            for (TabContentItem tabContentItem5 : this.settingTabItems) {
                if (tabContentItem5.getType() == 9) {
                    this.systemTabItem = tabContentItem5;
                }
            }
        }
        loadWearOsWatch(wearableDeviceModel);
        NotifyHelperExtKt.getNotificationManagerExport().updateNotificationRemind(false);
    }

    private final boolean showRecoveryConnect(String did) {
        WearableDeviceModel wearableDeviceModel;
        return (did == null || (wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getDeviceModel(did)) == null || !wearableDeviceModel.isCurrent() || DeviceModelExtKt.isWearOS(wearableDeviceModel)) ? false : true;
    }

    public static /* synthetic */ void syncDevice$default(DeviceTabV4ViewModel deviceTabV4ViewModel, WearableDeviceModel wearableDeviceModel, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        deviceTabV4ViewModel.syncDevice(wearableDeviceModel, z);
    }

    public static /* synthetic */ DeviceMarketItemAdapter t() {
        return marketAdapter_delegate$lambda$4();
    }

    public static /* synthetic */ Unit u(String str, DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z) {
        return realConnect$lambda$31(str, deviceTabV4ViewModel, z);
    }

    private static final Unit updateAccessoryStatus$lambda$47$lambda$46(DeviceTabV4ViewModel deviceTabV4ViewModel, String str, int i) {
        String tag = deviceTabV4ViewModel.getTAG();
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceModel(str);
        Logger.i(tag, "isAccessory = " + (wearableDeviceModel != null ? Boolean.valueOf(com.xiaomi.fit.device.extensions.DeviceModelExtKt.isAccessory(wearableDeviceModel)) : null) + ",mode = " + i);
        if (i == 0 || i == 2) {
            deviceTabV4ViewModel.switchAccessoryMode(i, str, false, new nh7(str, i, deviceTabV4ViewModel));
        }
        return Unit.INSTANCE;
    }

    private static final Unit updateAccessoryStatus$lambda$47$lambda$46$lambda$45(String str, int i, DeviceTabV4ViewModel deviceTabV4ViewModel, boolean z) {
        WearableDeviceManager.Companion companion = WearableDeviceManager.INSTANCE;
        DeviceManagerExtKt.getInstance(companion).switchDeviceWearMode(str, i, false);
        if (DeviceManagerExtKt.getInstance(companion).getCurrentDeviceModel() != 0) {
            DeviceManagerExtKt.getInstance(companion).disconnectDevice(str);
            deviceTabV4ViewModel.loadDevice(false);
        } else {
            DeviceManagerExtKt.getInstance(companion).switchDevice(DeviceManagerExtKt.getInstance(companion).getDeviceModel(str));
        }
        return Unit.INSTANCE;
    }

    private final void updateDialStatus(String did, int status) {
        if (Intrinsics.areEqual(did, this.mCurrentDeviceDid)) {
            TabDeviceContentItem tabDeviceContentItem = this.mDialStatusItem;
            if (tabDeviceContentItem != null) {
                tabDeviceContentItem.setStatus(status);
                return;
            }
            return;
        }
        Logger.i(getTAG(), "Cannot update status to " + status + ", because " + did + " is not equal current device(" + this.mCurrentDeviceDid + ")");
    }

    public static /* synthetic */ DeviceBannerAdapter v(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return bannerAdapter_delegate$lambda$6(deviceTabV4ViewModel);
    }

    public static /* synthetic */ List w(DeviceTabV4ViewModel deviceTabV4ViewModel) {
        return mAllFeatureCardIds_delegate$lambda$8(deviceTabV4ViewModel);
    }

    public static /* synthetic */ DeviceSettingsItemAdapter x() {
        return settingAdapter_delegate$lambda$3();
    }

    public static /* synthetic */ Unit y(String str, boolean z) {
        return setOnItemClickListener$lambda$36$lambda$35$lambda$34(str, z);
    }

    @Override // com.xiaomi.fitness.baseui.BaseViewModel, com.xiaomi.fitness.baseui.AbsViewModel
    public void attach(@Nullable Bundle savedInstanceState) {
        super.attach(savedInstanceState);
        FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).addFaceEventListener(this.faceEventListener);
        NotifyHelperExtKt.getNotificationManagerExport().addNotifyRemindListener(new AnonymousClass1());
    }

    public final void bindDevice(@NotNull WearableDeviceInfo deviceInfo, @NotNull a callback) {
        Intrinsics.checkNotNullParameter(deviceInfo, "deviceInfo");
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (deviceInfo.getMac() == null) {
            return;
        }
        WearableDeviceManagerWrapper wearableDeviceManagerWrapper = this.mDeviceManager;
        String model = deviceInfo.getModel();
        String mac = deviceInfo.getMac();
        Intrinsics.checkNotNull(mac);
        wearableDeviceManagerWrapper.bindDevice(new DeviceParameter(model, mac, deviceInfo.getBleName(), deviceInfo.getNodeID(), null, false, deviceInfo.getDid(), 11, 32, null), callback);
    }

    public final void checkChosenApp(@Nullable Boolean showChooseAppDialog) {
        checkAndUpdateChosenApp();
        if (!AppUtil.hasPackage(ApplicationExtKt.getApplication(), "com.xiaomi.wearable")) {
            if (this.mDeviceManager.getDeviceStatus() == 8) {
                connectDevice$default(this, false, null, 2, null);
                return;
            }
            return;
        }
        if (hasSupportDevice()) {
            int chooseApp = ChooseAppPreference.INSTANCE.getChooseApp();
            Logger.i(getTAG(), "chosenApp is " + chooseApp + " showChooseAppDialog is " + showChooseAppDialog);
            Boolean bool = Boolean.TRUE;
            if (Intrinsics.areEqual(showChooseAppDialog, bool)) {
                this.mChoiceAppFlag.postValue(bool);
                return;
            }
            if (chooseApp == 1) {
                this.mChoiceAppFlag.postValue(bool);
                return;
            }
            if (chooseApp != 2) {
                if (chooseApp == 3 && !this.mDeviceManager.isConnected() && ef3.e() && BluetoothPermissionUtil.INSTANCE.hasConnectPermission()) {
                    connectDevice$default(this, false, null, 2, null);
                    return;
                }
                return;
            }
            String str = this.mCurrentDeviceDid;
            if (str != null) {
                updateDialStatus(str, 8);
                this.mDeviceManager.disconnectDevice(str);
                WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
                if (wearableDeviceModel != null) {
                    wearableDeviceModel.setDeviceStatus(8);
                }
            }
        }
    }

    public final void connectDevice(boolean force, @Nullable String did) {
        if (Build.VERSION.SDK_INT >= 31) {
            if (PermissionsUtil.checkPermission("android.permission.BLUETOOTH_CONNECT")) {
                realConnect(did, force);
                return;
            } else {
                requestPermissionIfNeed(did, force);
                return;
            }
        }
        if (!needLocationDevice()) {
            realConnect(did, force);
            return;
        }
        if (!hasLocationPermission()) {
            requestPermissionIfNeed(did, force);
        } else if (LocationExtKt.isGpsEnable(ApplicationExtKt.getApplication())) {
            realConnect(did, force);
        } else {
            requestLocationSwitch(did, force);
        }
    }

    public final void connectDeviceIfNeed() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
        if (wearableDeviceModel != null) {
            this.mCurrentDeviceDid = wearableDeviceModel.getDid();
            if (wearableDeviceModel.isDeviceConnected()) {
                onConnectSuccess$default(this, this.mCurrentDeviceDid, wearableDeviceModel, false, false, 12, null);
            } else {
                connectDevice$default(this, false, null, 2, null);
            }
        }
    }

    @NotNull
    public final DeviceTabItemAdapter getAdapter() {
        return (DeviceTabItemAdapter) this.adapter.getValue();
    }

    @NotNull
    public final DeviceBannerAdapter getBannerAdapter() {
        return (DeviceBannerAdapter) this.bannerAdapter.getValue();
    }

    @NotNull
    public final ItemBinding<BannerItemV2> getBannerItemBinding() {
        return (ItemBinding) this.bannerItemBinding.getValue();
    }

    @NotNull
    public final ObservableArrayList<BannerItemV2> getBannerItems() {
        return this.bannerItems;
    }

    @NotNull
    public final MutableLiveData<Integer> getBondLossRecovery() {
        return this.bondLossRecovery;
    }

    @NotNull
    public final CheckUpdateManager getCheckUpdate() {
        CheckUpdateManager checkUpdateManager = this.checkUpdate;
        if (checkUpdateManager != null) {
            return checkUpdateManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("checkUpdate");
        return null;
    }

    public final boolean getComeFromMedalDetailPageFromSP() {
        return getMPopMedalRepo().cameFromMedalDetailPage();
    }

    @NotNull
    public final MutableLiveData<ConnectStatus> getConnectStatus() {
        return this.connectStatus;
    }

    @NotNull
    public final Map<Integer, TabContentItem> getContentItems() {
        return getMModel().getContentItems();
    }

    @NotNull
    public final MutableLiveData<Pair<Integer, Boolean>> getExistAccessoryMode() {
        return this.existAccessoryMode;
    }

    @NotNull
    public final MutableLiveData<Pair<String, String>> getExitAccessory() {
        return this.exitAccessory;
    }

    @NotNull
    public final MutableLiveData<Pair<String, Integer>> getExitAccessoryModeFromDevice() {
        return this.exitAccessoryModeFromDevice;
    }

    @Nullable
    public final PopMedalData getFirstMedalFromPopMedalDataSP() {
        return getMPopMedalRepo().getFirstMedal();
    }

    @NotNull
    public final ItemBinding<TabContentItem> getItemBindings() {
        return (ItemBinding) this.itemBindings.getValue();
    }

    @NotNull
    public final List<Integer> getMAllFeatureCardIds() {
        return (List) this.mAllFeatureCardIds.getValue();
    }

    @NotNull
    public final MutableLiveData<Boolean> getMChoiceAppFlag() {
        return this.mChoiceAppFlag;
    }

    @NotNull
    public final MutableLiveData<Boolean> getMGoUncommonUsedAppFlag() {
        return this.mGoUncommonUsedAppFlag;
    }

    @NotNull
    public final MutableLiveData<Boolean> getMLoadingFlag() {
        return this.mLoadingFlag;
    }

    @NotNull
    public final IPopMedalListRepo getMPopMedalRepo() {
        IPopMedalListRepo iPopMedalListRepo = this.mPopMedalRepo;
        if (iPopMedalListRepo != null) {
            return iPopMedalListRepo;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mPopMedalRepo");
        return null;
    }

    @NotNull
    public final MutableLiveData<Boolean> getMUpdateAppSuccessFlag() {
        return this.mUpdateAppSuccessFlag;
    }

    @NotNull
    public final MutableLiveData<String> getManageAccessory() {
        return this.manageAccessory;
    }

    @NotNull
    public final DeviceMarketItemAdapter getMarketAdapter() {
        return (DeviceMarketItemAdapter) this.marketAdapter.getValue();
    }

    @NotNull
    public final ObservableArrayList<TabContentItem> getMarketTabItems() {
        return this.marketTabItems;
    }

    @NotNull
    public final MutableLiveData<PopMedalData> getMedalToPopLiveData() {
        return this.medalToPopLiveData;
    }

    @NotNull
    public final DeviceSettingsItemAdapter getSettingAdapter() {
        return (DeviceSettingsItemAdapter) this.settingAdapter.getValue();
    }

    @NotNull
    public final ItemBinding<TabContentItem> getSettingItemBindings() {
        return (ItemBinding) this.settingItemBindings.getValue();
    }

    @NotNull
    public final ObservableArrayList<TabContentItem> getSettingTabItems() {
        return this.settingTabItems;
    }

    @NotNull
    public final Function2<View, Integer, BaseViewHolder<TabContentItem>> getSettingViewHolder() {
        return this.settingViewHolder;
    }

    @NotNull
    public final MutableLiveData<Pair<Boolean, String>> getShowConnectPermission() {
        return this.showConnectPermission;
    }

    @NotNull
    public final MutableLiveData<Boolean> getShowOpenLocationSwitch() {
        return this.showOpenLocationSwitch;
    }

    @NotNull
    public final MutableLiveData<WearableDeviceModel> getSyncDeviceLiveData() {
        return this.syncDeviceLiveData;
    }

    @Override // com.xiaomi.fitness.baseui.BaseViewModel
    @NotNull
    public String getTAG() {
        return "DeviceTabViewModelV4";
    }

    @NotNull
    public final ObservableArrayList<TabContentItem> getTabItems() {
        return this.tabItems;
    }

    @NotNull
    public final MutableLiveData<Pair<String, Integer>> getWearModeSwitchSuccess() {
        return this.wearModeSwitchSuccess;
    }

    @NotNull
    public final MutableLiveData<Boolean> getWearModeSwitchSuccessFromApp() {
        return this.wearModeSwitchSuccessFromApp;
    }

    public final void loadDevice(boolean isNeedSync) {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
        if (wearableDeviceModel == null) {
            if (!DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getAccessoryDeviceModels().isEmpty()) {
                this.tabItems.clear();
                addAccessoryDeviceItem();
                refreshAccessoryWearMode();
                return;
            } else {
                this.tabItems.clear();
                this.tabItems.add(createNormalContentItem(11, 4));
                IOnetrack.Companion companion = IOnetrack.INSTANCE;
                OneTrackExtKt.getInstance(companion).reportData(new OneTrackStatBean("expose_v3", "56.51.0.1.36380"), "name", "Device_page_Adddevice", "status", 0, "position_name", "Device_Card_Add");
                OneTrackExtKt.getInstance(companion).reportData(new OneTrackStatBean("expose_v3", "56.51.0.1.36380"), "name", "Device_page_Adddevice", "status", 0, "position_name", "Device_Card_Scan");
                addBannerItemWhenNoDevice();
                return;
            }
        }
        this.mCurrentDeviceDid = wearableDeviceModel.getDid();
        showDevice();
        Logger.i(getTAG(), "loadDevice: " + wearableDeviceModel.isDeviceConnected());
        if (wearableDeviceModel.isDeviceConnected()) {
            onConnectSuccess(this.mCurrentDeviceDid, wearableDeviceModel, isNeedSync, false);
        } else {
            connectDevice$default(this, false, null, 2, null);
        }
        refreshAccessoryWearMode();
    }

    public final void onAppChosen(int currentChosen) {
        this.mChosenApp = currentChosen;
        int chooseApp = ChooseAppPreference.INSTANCE.getChooseApp();
        com.xiaomi.fit.device.extensions.DeviceModelExtKt.logi(getTAG(), "onAppChosen() called with: currentChosen = " + currentChosen + ", oldChosen = " + chooseApp);
        if (chooseApp == currentChosen) {
            return;
        }
        if (chooseApp != 1) {
            if (chooseApp == 2 && currentChosen == 3) {
                this.mGoUncommonUsedAppFlag.postValue(Boolean.TRUE);
                return;
            }
            return;
        }
        if (currentChosen == 2) {
            this.mUpdateAppSuccessFlag.postValue(Boolean.TRUE);
        } else {
            if (currentChosen != 3) {
                return;
            }
            this.mGoUncommonUsedAppFlag.postValue(Boolean.TRUE);
        }
    }

    @Override // androidx.lifecycle.ViewModel
    public void onCleared() {
        FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).removeFaceEventListener(this.faceEventListener);
        getCheckUpdate().removeHasUpdateListener(this.checkUpdateListener);
        super.onCleared();
    }

    @Override // com.xiaomi.fitness.baseui.AbsViewModel, androidx.lifecycle.DefaultLifecycleObserver
    public void onCreate(@NotNull LifecycleOwner owner) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        super.onCreate(owner);
        this.mOwner = owner;
        this.mDeviceManager.addDeviceStatusListener(this.mDeviceStatusListener);
        this.mDeviceManager.addDeviceChangeListener(this.mDeviceChangeListener);
        this.mDeviceManager.addAccessoryDeviceStateListener(this.mOnAccessoryDeviceStateListener);
        DeviceSyncExtKt.getInstance(DeviceContact.INSTANCE).addDataHandler(2, this.deviceChargeStatusDataHandler);
        IDevicePowerModeManagerExtKt.getInstance(IDevicePowerModeManager.INSTANCE).addPowerModeChangeListener(this.devicePowerModeListener);
    }

    @Override // com.xiaomi.fitness.baseui.AbsViewModel, androidx.lifecycle.DefaultLifecycleObserver
    public void onDestroy(@NotNull LifecycleOwner owner) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        super.onDestroy(owner);
        this.mDeviceManager.removeDeviceStatusListener(this.mDeviceStatusListener);
        this.mDeviceManager.removeDeviceChangeListener(this.mDeviceChangeListener);
        DeviceSyncExtKt.getInstance(DeviceContact.INSTANCE).removeDataHandler(2, this.deviceChargeStatusDataHandler);
        this.mDeviceManager.removeAccessoryDeviceStateListener(this.mOnAccessoryDeviceStateListener);
        this.mDeviceContact.removeSyncStateListener(this.mSyncStateListener);
        NotifyHelperExtKt.getNotificationManagerExport().removeNotifyRemindListener();
        DeviceSettingManager.INSTANCE.getInstance().removeUpdateListener(this.updateSettingInfoListener);
        IDevicePowerModeManagerExtKt.getInstance(IDevicePowerModeManager.INSTANCE).removePowerModeChangeListener(this.devicePowerModeListener);
        FaceWearOsHelperExtKt.getInstance(FaceWearOsHelper.INSTANCE).dispose();
    }

    public final boolean popMedalListFromSpIsEmpty() {
        return getMPopMedalRepo().isMedalListEmpty();
    }

    public final void recordChosenApp() {
        int i = this.mChosenApp;
        if (i != 2 && i != 3) {
            com.xiaomi.fit.device.extensions.DeviceModelExtKt.logi("recordChosenApp chosen app is error (" + i + ")");
            return;
        }
        com.xiaomi.fit.device.extensions.DeviceModelExtKt.logi("recordChosenApp is " + i);
        long jCurrentTimeMillis = System.currentTimeMillis();
        ChooseAppPreference chooseAppPreference = ChooseAppPreference.INSTANCE;
        chooseAppPreference.setChooseApp(this.mChosenApp);
        chooseAppPreference.setChooseAppLastTime(jCurrentTimeMillis);
        recordChosenAppCloud(this.mChosenApp, jCurrentTimeMillis);
        String str = this.mCurrentDeviceDid;
        if (str != null) {
            DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).disconnectDevice(str);
        }
        OhterExtKt.startWearableApp(ApplicationExtKt.getApplication(), this.mChosenApp, jCurrentTimeMillis);
        int i2 = this.mChosenApp;
        if (i2 == 3) {
            AnyExtKt.main$default(null, new C21932(null), 1, null);
        } else if (i2 == 2) {
            updateDialStatus(this.mCurrentDeviceDid, 8);
        }
    }

    public final void refreshFaceEntranceRedPoint() {
        TabFaceEntranceItem tabFaceEntranceItem;
        if (AppUtil.INSTANCE.isPlayChannel() || (tabFaceEntranceItem = this.mFaceEntranceItem) == null) {
            return;
        }
        tabFaceEntranceItem.refreshRedPoint();
    }

    public final void removeDelete(@NotNull WearableDeviceModel deviceModel, @NotNull l callback) {
        Intrinsics.checkNotNullParameter(deviceModel, "deviceModel");
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.mDeviceManager.unbindDevice(deviceModel.getDid(), callback);
    }

    public final void removeMedalAlreadyPoppedFromSp() {
        getMPopMedalRepo().removePoppedMedalFromSp();
    }

    public final void savePopMedalListToSp(@NotNull List<PopMedalData> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        getMPopMedalRepo().savePopMedalList(list);
    }

    public final void setBondLossRecovery(@NotNull MutableLiveData<Integer> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.bondLossRecovery = mutableLiveData;
    }

    public final void setCheckUpdate(@NotNull CheckUpdateManager checkUpdateManager) {
        Intrinsics.checkNotNullParameter(checkUpdateManager, "<set-?>");
        this.checkUpdate = checkUpdateManager;
    }

    public final void setConnectStatus(@NotNull MutableLiveData<ConnectStatus> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.connectStatus = mutableLiveData;
    }

    public final void setExistAccessoryMode(@NotNull MutableLiveData<Pair<Integer, Boolean>> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.existAccessoryMode = mutableLiveData;
    }

    public final void setExitAccessory(@NotNull MutableLiveData<Pair<String, String>> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.exitAccessory = mutableLiveData;
    }

    public final void setExitAccessoryModeFromDevice(@NotNull MutableLiveData<Pair<String, Integer>> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.exitAccessoryModeFromDevice = mutableLiveData;
    }

    public final void setMChoiceAppFlag(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.mChoiceAppFlag = mutableLiveData;
    }

    public final void setMGoUncommonUsedAppFlag(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.mGoUncommonUsedAppFlag = mutableLiveData;
    }

    public final void setMLoadingFlag(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.mLoadingFlag = mutableLiveData;
    }

    public final void setMPopMedalRepo(@NotNull IPopMedalListRepo iPopMedalListRepo) {
        Intrinsics.checkNotNullParameter(iPopMedalListRepo, "<set-?>");
        this.mPopMedalRepo = iPopMedalListRepo;
    }

    public final void setMUpdateAppSuccessFlag(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.mUpdateAppSuccessFlag = mutableLiveData;
    }

    public final void setManageAccessory(@NotNull MutableLiveData<String> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.manageAccessory = mutableLiveData;
    }

    public final void setMedalToPopLiveData(@NotNull MutableLiveData<PopMedalData> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.medalToPopLiveData = mutableLiveData;
    }

    public final void setOnItemClickListener(@NotNull Function3<? super TabContentItem, ? super Integer, ? super View, Unit> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        getAdapter().setOnItemClickListener(listener);
        getAdapter().setOnChildClickListener(new gi7(this, listener));
        getSettingAdapter().setOnItemClickListener(listener);
        getMarketAdapter().setOnItemClickListener(listener);
    }

    public final void setPopMedalDataAlreadyPulled() {
        getMPopMedalRepo().setShouldPullPopMedalData(false);
    }

    public final void setShouldPullPopMedalDataFromNetwork() {
        getMPopMedalRepo().setShouldPullPopMedalData(true);
    }

    public final void setShowConnectPermission(@NotNull MutableLiveData<Pair<Boolean, String>> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.showConnectPermission = mutableLiveData;
    }

    public final void setShowOpenLocationSwitch(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.showOpenLocationSwitch = mutableLiveData;
    }

    public final void setSyncDeviceLiveData(@NotNull MutableLiveData<WearableDeviceModel> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.syncDeviceLiveData = mutableLiveData;
    }

    public final void setWearModeSwitchSuccess(@NotNull MutableLiveData<Pair<String, Integer>> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.wearModeSwitchSuccess = mutableLiveData;
    }

    public final void setWearModeSwitchSuccessFromApp(@NotNull MutableLiveData<Boolean> mutableLiveData) {
        Intrinsics.checkNotNullParameter(mutableLiveData, "<set-?>");
        this.wearModeSwitchSuccessFromApp = mutableLiveData;
    }

    public final boolean shouldPullPopMedalDataFromNetwork() {
        return getMPopMedalRepo().getShouldPullPopMedalData();
    }

    public final void showSystemRedPoint() {
        TabContentItem tabContentItem;
        MutableLiveData<String> rightTextWithDot;
        Logger.i("showSystemRedPoint", "systemTabItem " + this.systemTabItem + "  isKeepAliveSetTrue = " + pwn.y(ApplicationExtKt.getApplication()));
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
        if (wearableDeviceModel == null || (tabContentItem = this.systemTabItem) == null || (rightTextWithDot = tabContentItem.getRightTextWithDot()) == null) {
            return;
        }
        rightTextWithDot.postValue((pwn.y(ApplicationExtKt.getApplication()) || DeviceModelExtKt.isWearOS(wearableDeviceModel)) ? "" : " ");
    }

    public final void switchAccessoryMode(int mode, @NotNull String accessoryDid, boolean fromApp, @NotNull Function1<? super Boolean, Unit> block) {
        Intrinsics.checkNotNullParameter(accessoryDid, "accessoryDid");
        Intrinsics.checkNotNullParameter(block, "block");
        if (!fromApp) {
            block.invoke(Boolean.TRUE);
            return;
        }
        vit vitVar = new vit();
        vitVar.e = 2;
        vitVar.f = 15;
        l3r.k kVar = new l3r.k();
        kVar.c = mode;
        j3r j3rVar = new j3r();
        l3r l3rVar = new l3r();
        l3rVar.e = kVar;
        j3rVar.s0(l3rVar);
        vitVar.c0(j3rVar);
        DeviceContact.call$default(DeviceSyncExtKt.getInstance(DeviceContact.INSTANCE), accessoryDid, vitVar, true, (OnSyncCallback) new C21941(block, mode, this), 0, 16, (Object) null);
    }

    public final void switchRegin() {
        Logger.i(getTAG(), "switchRegin devices num = " + this.mDeviceManager.getDeviceSize());
        loadDevice(false);
    }

    public final void syncDevice(@Nullable WearableDeviceModel deviceModel, boolean isAuto) {
        if (deviceModel != null) {
            this.mDeviceContact.addSyncStateListener(this.mSyncStateListener);
            this.mDeviceContact.syncData(deviceModel.getDid(), isAuto);
            WearableDeviceModel.readWatchInfo$default(deviceModel, null, 1, null);
            BuildersKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new DeviceTabV4ViewModel$syncDevice$1$1(null), 3, null);
        }
    }

    public final void syncDeviceIfNeed() {
        Logger.i(getTAG(), "syncDeviceIfNeed = " + this.connectedOnBackgroundNeedSync);
        if (this.connectedOnBackgroundNeedSync) {
            syncDevice$default(this, (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel(), false, 2, null);
            this.connectedOnBackgroundNeedSync = false;
        }
    }

    public final void unbind(@NotNull String did) {
        Intrinsics.checkNotNullParameter(did, "did");
        this.mLoadingFlag.postValue(Boolean.TRUE);
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getDeviceModel(did);
        boolean z = false;
        if (wearableDeviceModel != null && DeviceModelExtKt.isWearOS(wearableDeviceModel)) {
            z = true;
        }
        this.mDeviceManager.unbindDevice(did, new BinderC21951(z, did));
    }

    public final void updateAccessoryStatus(@Nullable String did, int status) {
        WearableDeviceModel wearableDeviceModel;
        TabAccessoryDeviceContentItem tabAccessoryDeviceContentItem;
        if (did != null) {
            TabAccessoryDeviceContentItem tabAccessoryDeviceContentItem2 = this.mAccessoryDeviceContentItem;
            if (Intrinsics.areEqual(did, tabAccessoryDeviceContentItem2 != null ? tabAccessoryDeviceContentItem2.getDid() : null) && (tabAccessoryDeviceContentItem = this.mAccessoryDeviceContentItem) != null) {
                tabAccessoryDeviceContentItem.setStatus(status);
            }
            if (status == 3 && (wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getDeviceModel(did)) != null && com.xiaomi.fit.device.extensions.DeviceModelExtKt.isAccessory(wearableDeviceModel)) {
                WearModeHelperExtKt.getInstance(WearModeHelper.INSTANCE).refreshWearMode(did, new wh7(this, did));
            }
        }
    }

    public final void updateBatteryIfNeed() {
        Logger.i(getTAG(), "updateBatteryIfNeed isConnected=" + this.mDeviceManager.isConnected() + ",deviceStatus=" + this.mDeviceManager.getDeviceStatus());
        if (this.mDeviceManager.isConnected() && this.mDeviceManager.getDeviceStatus() == 7) {
            WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) this.mDeviceManager.getCurrentDeviceModel();
            updateDialStatus(wearableDeviceModel != null ? wearableDeviceModel.getDid() : null, 7);
        }
    }
}
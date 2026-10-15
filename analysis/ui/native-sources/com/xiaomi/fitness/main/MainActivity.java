package com.xiaomi.fitness.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.i;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.appupgrade.export.AppUpgradeHelper;
import com.example.statistics.export.AccountType;
import com.example.statistics.export.IOnetrack;
import com.example.statistics.export.OneTrackExtKt;
import com.facebook.react.a;
import com.facebook.react.bridge.ReactContext;
import com.fitness.permission.manager.export.IPermissionManager;
import com.fitness.permission.manager.export.PermissionExportExtKt;
import com.fitness.task.export.ITask;
import com.fitness.task.export.TaskHelperExtKt;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mi.fitness.checkupdate.export.CheckUpdateManager;
import com.mi.health.appupgrade.AppUpgradeUtil;
import com.mi.health.earphone.export.EarphoneExtKt;
import com.mi.health.uri_parser.UriParserExportKt;
import com.mi.health.uri_parser.links.HttpLinkActivity;
import com.mibi.sdk.common.account.AccountInteractActivity;
import com.mibi.sdk.component.params.SdkContants;
import com.miui.circulate.device.api.DeviceInfo;
import com.xiaomi.account.openauth.XiaomiOAuthorize;
import com.xiaomi.fit.data.common.log.FitnessLogUtils;
import com.xiaomi.fit.fitness.export.api.FitnessServerSyncer;
import com.xiaomi.fit.fitness.export.di.FitnessDataExtKt;
import com.xiaomi.fit.fitness.export.sp.FitnessDataPreference;
import com.xiaomi.fitness.about.utils.MainCommonAppUrlsKt;
import com.xiaomi.fitness.account.api.bean.RegionBean;
import com.xiaomi.fitness.account.api.bean.RegionInfo;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.manager.AccountManager;
import com.xiaomi.fitness.account.manager.AppLinkOAuthActivity;
import com.xiaomi.fitness.account.region.RegionPreference;
import com.xiaomi.fitness.account.token.TokenManager;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.baseui.dialog.CommonDialog;
import com.xiaomi.fitness.baseui.dialog.DialogParams;
import com.xiaomi.fitness.baseui.extension.LaunchExtensionKt;
import com.xiaomi.fitness.baseui.utils.NotificationPermission;
import com.xiaomi.fitness.baseui.utils.PermissionsUtil;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.LinkSpanHelper;
import com.xiaomi.fitness.common.extensions.NetworkExtKt;
import com.xiaomi.fitness.common.extensions.PermissionBuilder;
import com.xiaomi.fitness.common.extensions.PermissionExtKt;
import com.xiaomi.fitness.common.extensions.ResourceExtKt;
import com.xiaomi.fitness.common.extensions.ToastExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.AppVersionUtil;
import com.xiaomi.fitness.common.utils.LocaleUtil;
import com.xiaomi.fitness.common.utils.MD5Util;
import com.xiaomi.fitness.common.utils.RomUtils;
import com.xiaomi.fitness.common.utils.SystemSetting;
import com.xiaomi.fitness.common.utils.UIUtils;
import com.xiaomi.fitness.component.StandardComponentEvent;
import com.xiaomi.fitness.config.preference.ChooseAppPreference;
import com.xiaomi.fitness.device.contact.export.DeviceContact;
import com.xiaomi.fitness.device.contact.export.DeviceSyncExtKt;
import com.xiaomi.fitness.device.manager.DeviceManager;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.OnProductChangeListener;
import com.xiaomi.fitness.device.manager.ProductManager;
import com.xiaomi.fitness.device.manager.bean.Product;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceTool;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.export.WearableProductManager;
import com.xiaomi.fitness.device.manager.export.bean.WearableDeviceInfo;
import com.xiaomi.fitness.device.manager.export.scan.ScannedDeviceInfo;
import com.xiaomi.fitness.devicesettings.export.SettingsPageUtilExtKt;
import com.xiaomi.fitness.extensions.ViewExtKt;
import com.xiaomi.fitness.login.export.CheckerUserSettingManager;
import com.xiaomi.fitness.login.export.CheckerUserSettingManagerExtKt;
import com.xiaomi.fitness.login.export.EventStatus;
import com.xiaomi.fitness.login.export.EventType;
import com.xiaomi.fitness.login.export.FunctionCode;
import com.xiaomi.fitness.login.export.PrivacyEasyModePreference;
import com.xiaomi.fitness.login.export.RegionExtKt;
import com.xiaomi.fitness.login.export.RegionManager;
import com.xiaomi.fitness.login.utils.RegionUtil;
import com.xiaomi.fitness.main.export.ConfigPreference;
import com.xiaomi.fitness.main.export.IntentRelay;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.fitness.main.export.MainTab;
import com.xiaomi.fitness.main.export.MainTabRedPointInterface;
import com.xiaomi.fitness.main.export.MainTabRedPointManage;
import com.xiaomi.fitness.main.export.StartupDialogTracker;
import com.xiaomi.fitness.main.utils.ActivityRecognitionPermissionUtil;
import com.xiaomi.fitness.main.utils.DialogHelper;
import com.xiaomi.fitness.main.utils.GlobalDialogManager;
import com.xiaomi.fitness.main.utils.SimplifiedChineseModel;
import com.xiaomi.fitness.medal.export.MedalManager;
import com.xiaomi.fitness.medal.export.MedalManagerExtKt;
import com.xiaomi.fitness.membership.export.MembershipHelper;
import com.xiaomi.fitness.membership.export.MembershipHelperExtKt;
import com.xiaomi.fitness.nfc.export.NfcManager;
import com.xiaomi.fitness.nfc.export.NfcManagerExtKt;
import com.xiaomi.fitness.privacy.export.IPrivacySensitiveImpl;
import com.xiaomi.fitness.privacy.export.PrivacyHelper;
import com.xiaomi.fitness.privacy.export.PrivacyHelperPointKt;
import com.xiaomi.fitness.push.export.PushManager;
import com.xiaomi.fitness.push.export.PushManagerKt;
import com.xiaomi.fitness.relative.export.IRelativeCareAccessApi;
import com.xiaomi.fitness.relative.export.RelativeCareAccessExtKt;
import com.xiaomi.fitness.rn.export.DeviceShareManager;
import com.xiaomi.fitness.rn.export.DeviceShareManagerExtKt;
import com.xiaomi.fitness.service.ServiceConnector;
import com.xiaomi.fitness.settingitem.DeviceSettingConfigKey;
import com.xiaomi.fitness.settingitem.DeviceSettingManager;
import com.xiaomi.fitness.settingitem.settingitem.PrivacySensitive;
import com.xiaomi.fitness.sport_export.IJustDanceApi;
import com.xiaomi.fitness.sport_export.JustDanceExtKt;
import com.xiaomi.fitness.sport_manager_export.di.SportManagerExtKt;
import com.xiaomi.fitness.sport_manager_export.launch.ISportRemoteState;
import com.xiaomi.fitness.user.info.export.SetPageManageExtKt;
import com.xiaomi.fitness.user.info.export.SetPageManager;
import com.xiaomi.fitness.util.SportRecordBundleKey;
import com.xiaomi.fitness.watch.face.data.FaceEntranceRespWrapper;
import com.xiaomi.fitness.watch.face.data.OperationConfig;
import com.xiaomi.fitness.watch.face.data.OperationDesc;
import com.xiaomi.fitness.watch.face.data.RecentOnlineData;
import com.xiaomi.fitness.watch.face.data.RecentOnlineListData;
import com.xiaomi.fitness.watch.face.data.RecentOnlineTabDataV4;
import com.xiaomi.fitness.watch.face.export.FaceHelper;
import com.xiaomi.fitness.watch.face.export.FaceHelperExtKt;
import com.xiaomi.fitness.watch.face.schema.FaceSchema;
import com.xiaomi.fitness.wearos.export.FastPairManager;
import com.xiaomi.fitness.wearos.export.WearOSUtil;
import com.xiaomi.fitness.wearos.export.WearOsExtKt;
import com.xiaomi.fitness.webview.WebViewUtilKt;
import com.xiaomi.onetrack.api.am;
import com.xiaomi.smarthome.framework.plugin.rn.report.RnReportConst;
import defpackage.aeg;
import defpackage.b5k;
import defpackage.bcg;
import defpackage.bdg;
import defpackage.bf;
import defpackage.dcg;
import defpackage.ddg;
import defpackage.dg6;
import defpackage.fcg;
import defpackage.fdg;
import defpackage.gdk;
import defpackage.hcg;
import defpackage.hdg;
import defpackage.j3r;
import defpackage.jcg;
import defpackage.jdg;
import defpackage.kcg;
import defpackage.lcg;
import defpackage.ldg;
import defpackage.mcg;
import defpackage.mf6;
import defpackage.mz4;
import defpackage.nfg;
import defpackage.ocg;
import defpackage.pau;
import defpackage.pcg;
import defpackage.pwn;
import defpackage.qaj;
import defpackage.qcg;
import defpackage.rcg;
import defpackage.rgk;
import defpackage.scg;
import defpackage.sj7;
import defpackage.tcg;
import defpackage.u7e;
import defpackage.ubg;
import defpackage.ucg;
import defpackage.umk;
import defpackage.v7e;
import defpackage.vcg;
import defpackage.vit;
import defpackage.w2r;
import defpackage.wbg;
import defpackage.wcg;
import defpackage.xcg;
import defpackage.ybg;
import defpackage.z02;
import defpackage.z9j;
import defpackage.zbg;
import defpackage.zcg;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Route(path = MainExtKt.MAIN_ACTIVITY_PATH)
@Metadata(d1 = {"\u0000Î\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010%\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0005\n\u0002\b\t*\bå\u0001ô\u0001ý\u0001\u0082\u0002\b\u0007\u0018\u0000 \u0089\u00022\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u00042\u00020\u00052\u00020\u00062\u00020\u0007:\u0002\u0089\u0002B\u0007¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\u000b\u0010\tJ\u000f\u0010\f\u001a\u00020\nH\u0002¢\u0006\u0004\b\f\u0010\tJ+\u0010\u0013\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011H\u0002¢\u0006\u0004\b\u0013\u0010\u0014J9\u0010\u0018\u001a\u00020\n2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011H\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u000f\u0010\u001a\u001a\u00020\nH\u0002¢\u0006\u0004\b\u001a\u0010\tJ\u0017\u0010\u001c\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u0011H\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ\u000f\u0010\u001e\u001a\u00020\nH\u0002¢\u0006\u0004\b\u001e\u0010\tJ\u0017\u0010!\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u001fH\u0002¢\u0006\u0004\b!\u0010\"J\u0017\u0010#\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u001fH\u0002¢\u0006\u0004\b#\u0010\"J\u0017\u0010&\u001a\u00020\n2\u0006\u0010%\u001a\u00020$H\u0002¢\u0006\u0004\b&\u0010'J\u0019\u0010)\u001a\u00020\n2\b\u0010(\u001a\u0004\u0018\u00010\u001fH\u0002¢\u0006\u0004\b)\u0010\"J\u000f\u0010*\u001a\u00020\nH\u0002¢\u0006\u0004\b*\u0010\tJ\u0017\u0010+\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u0011H\u0002¢\u0006\u0004\b+\u0010\u001dJ\u000f\u0010,\u001a\u00020\nH\u0002¢\u0006\u0004\b,\u0010\tJ\u001b\u0010/\u001a\u00020\n*\u00020\u00032\u0006\u0010.\u001a\u00020-H\u0002¢\u0006\u0004\b/\u00100J\u001b\u00103\u001a\u00020\n*\u0002012\u0006\u00103\u001a\u000202H\u0002¢\u0006\u0004\b3\u00104J\u000f\u00105\u001a\u00020\nH\u0002¢\u0006\u0004\b5\u0010\tJ\u000f\u00106\u001a\u00020\nH\u0002¢\u0006\u0004\b6\u0010\tJ\u000f\u00107\u001a\u00020\nH\u0002¢\u0006\u0004\b7\u0010\tJ\u000f\u00108\u001a\u00020\nH\u0002¢\u0006\u0004\b8\u0010\tJ\u0017\u0010:\u001a\u00020\n2\u0006\u00109\u001a\u000202H\u0002¢\u0006\u0004\b:\u0010;J\u000f\u0010<\u001a\u00020\nH\u0002¢\u0006\u0004\b<\u0010\tJ\u000f\u0010=\u001a\u00020\nH\u0002¢\u0006\u0004\b=\u0010\tJ\u000f\u0010>\u001a\u00020\nH\u0002¢\u0006\u0004\b>\u0010\tJ\u000f\u0010?\u001a\u00020\nH\u0002¢\u0006\u0004\b?\u0010\tJ\u001f\u0010D\u001a\u00020\n2\u0006\u0010A\u001a\u00020@2\u0006\u0010C\u001a\u00020BH\u0002¢\u0006\u0004\bD\u0010EJ\u0017\u0010G\u001a\u00020\n2\u0006\u0010F\u001a\u00020\rH\u0002¢\u0006\u0004\bG\u0010HJ\u0017\u0010I\u001a\u0002022\u0006\u0010F\u001a\u00020\rH\u0002¢\u0006\u0004\bI\u0010JJ\u000f\u0010K\u001a\u00020\nH\u0002¢\u0006\u0004\bK\u0010\tJ\u000f\u0010L\u001a\u00020\nH\u0002¢\u0006\u0004\bL\u0010\tJ\u000f\u0010M\u001a\u00020\nH\u0003¢\u0006\u0004\bM\u0010\tJ\u000f\u0010N\u001a\u00020\nH\u0002¢\u0006\u0004\bN\u0010\tJ\u000f\u0010O\u001a\u00020\nH\u0002¢\u0006\u0004\bO\u0010\tJ\u000f\u0010P\u001a\u00020\nH\u0002¢\u0006\u0004\bP\u0010\tJ\u000f\u0010Q\u001a\u00020\nH\u0002¢\u0006\u0004\bQ\u0010\tJ\u000f\u0010R\u001a\u000202H\u0002¢\u0006\u0004\bR\u0010SJ\u000f\u0010T\u001a\u000202H\u0002¢\u0006\u0004\bT\u0010SJ\u0017\u0010W\u001a\u00020\n2\u0006\u0010V\u001a\u00020UH\u0002¢\u0006\u0004\bW\u0010XJ\u000f\u0010Y\u001a\u00020\nH\u0003¢\u0006\u0004\bY\u0010\tJ\u000f\u0010[\u001a\u00020ZH\u0002¢\u0006\u0004\b[\u0010\\J\u0017\u0010_\u001a\u00020\n2\u0006\u0010^\u001a\u00020]H\u0016¢\u0006\u0004\b_\u0010`J\u0019\u0010b\u001a\u00020\n2\b\u0010a\u001a\u0004\u0018\u00010\u0011H\u0014¢\u0006\u0004\bb\u0010\u001dJ\u0017\u0010e\u001a\u00020\n2\u0006\u0010d\u001a\u00020cH\u0016¢\u0006\u0004\be\u0010fJ\r\u0010g\u001a\u00020\n¢\u0006\u0004\bg\u0010\tJ\u0017\u0010h\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u001fH\u0014¢\u0006\u0004\bh\u0010\"J\u0013\u0010i\u001a\u00020\n*\u00020\u0003H\u0016¢\u0006\u0004\bi\u0010jJ\u000f\u0010k\u001a\u00020\nH\u0014¢\u0006\u0004\bk\u0010\tJ\u000f\u0010l\u001a\u00020\nH\u0016¢\u0006\u0004\bl\u0010\tJ\u000f\u0010m\u001a\u00020\nH\u0015¢\u0006\u0004\bm\u0010\tJ\u0015\u0010o\u001a\u00020\n2\u0006\u0010n\u001a\u000202¢\u0006\u0004\bo\u0010;J\u000f\u0010p\u001a\u00020\nH\u0014¢\u0006\u0004\bp\u0010\tJ\u000f\u0010q\u001a\u000202H\u0014¢\u0006\u0004\bq\u0010SJ)\u0010u\u001a\u00020\n2\u0006\u0010r\u001a\u00020]2\u0006\u0010s\u001a\u00020]2\b\u0010t\u001a\u0004\u0018\u00010\u001fH\u0014¢\u0006\u0004\bu\u0010vJ\u0015\u0010w\u001a\u00020\n2\u0006\u0010V\u001a\u00020U¢\u0006\u0004\bw\u0010XJ\u000f\u0010x\u001a\u00020\nH\u0014¢\u0006\u0004\bx\u0010\tJ!\u0010|\u001a\u0002022\u0006\u0010y\u001a\u00020]2\b\u0010{\u001a\u0004\u0018\u00010zH\u0016¢\u0006\u0004\b|\u0010}J\u0012\u0010\u007f\u001a\u0004\u0018\u00010~H\u0016¢\u0006\u0005\b\u007f\u0010\u0080\u0001J:\u0010\u0085\u0001\u001a\u00020\n2\u0012\u0010\u0082\u0001\u001a\r\u0012\u0006\b\u0001\u0012\u00020\r\u0018\u00010\u0081\u00012\u0006\u0010r\u001a\u00020]2\n\u0010\u0084\u0001\u001a\u0005\u0018\u00010\u0083\u0001H\u0016¢\u0006\u0006\b\u0085\u0001\u0010\u0086\u0001J\u0011\u0010\u0087\u0001\u001a\u00020\nH\u0016¢\u0006\u0005\b\u0087\u0001\u0010\tJ&\u0010\u008a\u0001\u001a\u00020\n2\t\u0010\u0088\u0001\u001a\u0004\u0018\u00010\r2\t\u0010\u0089\u0001\u001a\u0004\u0018\u00010\r¢\u0006\u0006\b\u008a\u0001\u0010\u008b\u0001J#\u0010\u008d\u0001\u001a\u00020\n2\u0007\u0010\u008c\u0001\u001a\u00020\r2\u0006\u0010n\u001a\u000202H\u0016¢\u0006\u0006\b\u008d\u0001\u0010\u008e\u0001R'\u0010\u008f\u0001\u001a\u0002028\u0014@\u0014X\u0094\u000e¢\u0006\u0016\n\u0006\b\u008f\u0001\u0010\u0090\u0001\u001a\u0005\b\u0091\u0001\u0010S\"\u0005\b\u0092\u0001\u0010;R!\u0010\u0098\u0001\u001a\u00030\u0093\u00018BX\u0082\u0084\u0002¢\u0006\u0010\n\u0006\b\u0094\u0001\u0010\u0095\u0001\u001a\u0006\b\u0096\u0001\u0010\u0097\u0001R \u0010\u009a\u0001\u001a\t\u0012\u0002\b\u0003\u0018\u00010\u0099\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u009a\u0001\u0010\u009b\u0001R\u0019\u0010\u009c\u0001\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u009c\u0001\u0010\u0090\u0001R\u0019\u0010\u009d\u0001\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u009d\u0001\u0010\u0090\u0001R\u0019\u0010\u009e\u0001\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u009e\u0001\u0010\u0090\u0001R\u0019\u0010\u009f\u0001\u001a\u0002028\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u009f\u0001\u0010\u0090\u0001R1\u0010¢\u0001\u001a\n\u0012\u0005\u0012\u00030¡\u00010 \u00018\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b¢\u0001\u0010£\u0001\u001a\u0006\b¤\u0001\u0010¥\u0001\"\u0006\b¦\u0001\u0010§\u0001R*\u0010©\u0001\u001a\u00030¨\u00018\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b©\u0001\u0010ª\u0001\u001a\u0006\b«\u0001\u0010¬\u0001\"\u0006\b\u00ad\u0001\u0010®\u0001R*\u0010°\u0001\u001a\u00030¯\u00018\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b°\u0001\u0010±\u0001\u001a\u0006\b²\u0001\u0010³\u0001\"\u0006\b´\u0001\u0010µ\u0001R*\u0010·\u0001\u001a\u00030¶\u00018\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b·\u0001\u0010¸\u0001\u001a\u0006\b¹\u0001\u0010º\u0001\"\u0006\b»\u0001\u0010¼\u0001R*\u0010¾\u0001\u001a\u00030½\u00018\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0006\b¾\u0001\u0010¿\u0001\u001a\u0006\bÀ\u0001\u0010Á\u0001\"\u0006\bÂ\u0001\u0010Ã\u0001R*\u0010Å\u0001\u001a\u00030Ä\u00018\u0006@\u0006X\u0086.¢\u0006\u0018\n\u0006\bÅ\u0001\u0010Æ\u0001\u001a\u0006\bÇ\u0001\u0010È\u0001\"\u0006\bÉ\u0001\u0010Ê\u0001R\u0019\u0010Ë\u0001\u001a\u00020]8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bË\u0001\u0010Ì\u0001R\u001b\u0010Í\u0001\u001a\u0004\u0018\u00010-8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bÍ\u0001\u0010Î\u0001R!\u0010Ó\u0001\u001a\u00030Ï\u00018BX\u0082\u0084\u0002¢\u0006\u0010\n\u0006\bÐ\u0001\u0010\u0095\u0001\u001a\u0006\bÑ\u0001\u0010Ò\u0001R(\u0010Ô\u0001\u001a\u00020]8\u0006@\u0006X\u0086\u000e¢\u0006\u0017\n\u0006\bÔ\u0001\u0010Ì\u0001\u001a\u0006\bÕ\u0001\u0010Ö\u0001\"\u0005\b×\u0001\u0010`R'\u0010Ø\u0001\u001a\u0002028\u0006@\u0006X\u0086\u000e¢\u0006\u0016\n\u0006\bØ\u0001\u0010\u0090\u0001\u001a\u0005\bØ\u0001\u0010S\"\u0005\bÙ\u0001\u0010;R'\u0010Ú\u0001\u001a\u0002028\u0006@\u0006X\u0086\u000e¢\u0006\u0016\n\u0006\bÚ\u0001\u0010\u0090\u0001\u001a\u0005\bÛ\u0001\u0010S\"\u0005\bÜ\u0001\u0010;R&\u0010Þ\u0001\u001a\u0011\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\r0Ý\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bÞ\u0001\u0010ß\u0001R)\u0010á\u0001\u001a\u000f\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\n0à\u00018\u0006¢\u0006\u0010\n\u0006\bá\u0001\u0010â\u0001\u001a\u0006\bã\u0001\u0010ä\u0001R\u0018\u0010æ\u0001\u001a\u00030å\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bæ\u0001\u0010ç\u0001R(\u0010è\u0001\u001a\u0011\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\n\u0018\u00010à\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bè\u0001\u0010â\u0001R\u001c\u0010ê\u0001\u001a\u0005\u0018\u00010é\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bê\u0001\u0010ë\u0001R(\u0010ì\u0001\u001a\u0011\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\n\u0018\u00010à\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bì\u0001\u0010â\u0001R(\u0010í\u0001\u001a\u0011\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\n\u0018\u00010à\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bí\u0001\u0010â\u0001R\u001b\u0010î\u0001\u001a\u0004\u0018\u00010Z8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bî\u0001\u0010ï\u0001R \u0010ð\u0001\u001a\t\u0012\u0002\b\u0003\u0018\u00010\u0099\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\bð\u0001\u0010\u009b\u0001R\u001e\u0010ò\u0001\u001a\t\u0012\u0004\u0012\u00020\n0ñ\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bò\u0001\u0010ó\u0001R\u0018\u0010õ\u0001\u001a\u00030ô\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bõ\u0001\u0010ö\u0001R'\u0010÷\u0001\u001a\u0002028\u0006@\u0006X\u0086\u000e¢\u0006\u0016\n\u0006\b÷\u0001\u0010\u0090\u0001\u001a\u0005\b÷\u0001\u0010S\"\u0005\bø\u0001\u0010;R8\u0010ü\u0001\u001a#\u0012\u0018\u0012\u0016\u0018\u00010ù\u0001¢\u0006\u000e\bú\u0001\u0012\t\bû\u0001\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\n0à\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bü\u0001\u0010â\u0001R(\u0010\u0084\u0001\u001a\u0011\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\n\u0018\u00010à\u00018\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0084\u0001\u0010â\u0001R\u0018\u0010þ\u0001\u001a\u00030ý\u00018\u0002X\u0082\u0004¢\u0006\b\n\u0006\bþ\u0001\u0010ÿ\u0001R\u0017\u0010\u0080\u0002\u001a\u00020\r8\u0002X\u0082D¢\u0006\b\n\u0006\b\u0080\u0002\u0010\u0081\u0002R\u0018\u0010\u0083\u0002\u001a\u00030\u0082\u00028\u0002X\u0082\u0004¢\u0006\b\n\u0006\b\u0083\u0002\u0010\u0084\u0002R\u0017\u0010\u0086\u0002\u001a\u00020]8VX\u0096\u0004¢\u0006\b\u001a\u0006\b\u0085\u0002\u0010Ö\u0001R\u0017\u0010\u0088\u0002\u001a\u00020]8VX\u0096\u0004¢\u0006\b\u001a\u0006\b\u0087\u0002\u0010Ö\u0001¨\u0006\u008a\u0002"}, d2 = {"Lcom/xiaomi/fitness/main/MainActivity;", "Lcom/xiaomi/fitness/baseui/view/BaseBindingActivity;", "Lcom/xiaomi/fitness/main/MainViewModel;", "Laeg;", "Lmf6;", "Lz9j;", "Lv7e;", "Lcom/xiaomi/fitness/main/export/MainTabRedPointInterface;", "<init>", "()V", "", "registerDataChangedBroadcast", "removeServiceConnectionListener", "", "modelId", "Landroid/bluetooth/BluetoothDevice;", "device", "Landroid/os/Bundle;", "fastPairExtras", "doFastPairProductListener", "(Ljava/lang/String;Landroid/bluetooth/BluetoothDevice;Landroid/os/Bundle;)V", "", "Lcom/xiaomi/fitness/device/manager/bean/Product;", "productList", "launchBindDevice", "(Ljava/util/List;Ljava/lang/String;Landroid/bluetooth/BluetoothDevice;Landroid/os/Bundle;)V", "showLoginDialogIfNeed", SdkContants.KEY_BUNDLE, "checkCarCard", "(Landroid/os/Bundle;)V", "showConnectNewPhoneIfNeed", "Landroid/content/Intent;", "_intent", "processIntent", "(Landroid/content/Intent;)V", "processAppLink", "Landroid/net/Uri;", SportRecordBundleKey.BUNDLE_PARAM_URI, "doAppLinkAction", "(Landroid/net/Uri;)V", "intent", "initBundleParams", "gotoTargetPage", "initIndex", "shouldShowLoginWhenTokenInvalid", "Landroidx/fragment/app/Fragment;", "showFragment", "selectSportTab", "(Laeg;Landroidx/fragment/app/Fragment;)V", "Lcom/google/android/material/tabs/TabLayout$g;", "", "selected", "(Lcom/google/android/material/tabs/TabLayout$g;Z)V", "showKeepLinkDialog", "requestDeviceTabRedPoint", "refreshDeviceTabIcon", "triggerSyncWithServer", "isShowGuide", "syncLocalStatusToWeb", "(Z)V", "showPrivacyDialog", "showPrivacySensitiveDialog", "showUserInfoGuide", "showSyncWebDialog", "", "delayMillis", "Ljava/lang/Runnable;", "r", "postDelayed", "(JLjava/lang/Runnable;)V", "scanResult", "handleQRScanResult", "(Ljava/lang/String;)V", "checkWhetherScanResultCanBeQrCodeId", "(Ljava/lang/String;)Z", "dealWithMedal", "checkCommonApplication", "showGoToWearableAppDialog", "handlePrivacySensitiveDialog", "handleAutoLogin", "checkUpdaterDialog", "checkHealthKitUpgradeDialog", "isKitRouteEnabled", "()Z", "kitAppIsInstall", "Landroidx/fragment/app/FragmentActivity;", "activity", "showRecognitionPermissionGuide", "(Landroidx/fragment/app/FragmentActivity;)V", "registerMediaImagePermissionChangeReceiver", "Lpau;", "createReactActivityDelegate", "()Lpau;", "", "featureId", "setWindowFeature", "(I)V", "savedInstanceState", "onCreate", "Landroid/content/res/Configuration;", StandardComponentEvent.KEY_NEW_CONFIG, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", "doFastPairing", "onNewIntent", "bindView", "(Laeg;)V", "onRelease", "onBackPressed", LifecycleConstantKt.ON_RESUME, "isShow", "showMainTabRedPoint", LifecycleConstantKt.ON_PAUSE, "isNeedRestore", AccountInteractActivity.KEY_REQUEST_CODE, "resultCode", "data", "onActivityResult", "(IILandroid/content/Intent;)V", "showRecognitionPermission", LifecycleConstantKt.ON_DESTROY, "keyCode", "Landroid/view/KeyEvent;", "event", "onKeyUp", "(ILandroid/view/KeyEvent;)Z", "Lu7e;", "getActivityDelegate", "()Lu7e;", "", "permissions", "Lqaj;", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "requestPermissions", "([Ljava/lang/String;ILqaj;)V", "invokeDefaultOnBackPressed", "appName", RnReportConst.ApiReport.PARAM_MODULE, "attachModule", "(Ljava/lang/String;Ljava/lang/String;)V", "tabId", "showTabRedPoint", "(Ljava/lang/String;Z)V", "enableAutoSetLayoutUiMode", "Z", "getEnableAutoSetLayoutUiMode", "setEnableAutoSetLayoutUiMode", "Lcom/xiaomi/fit/fitness/export/api/FitnessServerSyncer;", "serverSyncer$delegate", "Lkotlin/Lazy;", "getServerSyncer", "()Lcom/xiaomi/fit/fitness/export/api/FitnessServerSyncer;", "serverSyncer", "Lcom/xiaomi/fitness/baseui/dialog/CommonDialog;", "mGoToAppDialog", "Lcom/xiaomi/fitness/baseui/dialog/CommonDialog;", "mShowGuide", "mCheckShowGuide", "mLoginOnetrack", "mFirstSetCommonProperties", "", "Lcom/xiaomi/fitness/main/export/MainTab;", "mMainTabs", "Ljava/util/Set;", "getMMainTabs", "()Ljava/util/Set;", "setMMainTabs", "(Ljava/util/Set;)V", "Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "checkUpdate", "Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "getCheckUpdate", "()Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;", "setCheckUpdate", "(Lcom/mi/fitness/checkupdate/export/CheckUpdateManager;)V", "Lcom/xiaomi/fitness/push/export/PushManager;", "pushManager", "Lcom/xiaomi/fitness/push/export/PushManager;", "getPushManager", "()Lcom/xiaomi/fitness/push/export/PushManager;", "setPushManager", "(Lcom/xiaomi/fitness/push/export/PushManager;)V", "Lcom/example/appupgrade/export/AppUpgradeHelper;", "appUpgradeHelper", "Lcom/example/appupgrade/export/AppUpgradeHelper;", "getAppUpgradeHelper", "()Lcom/example/appupgrade/export/AppUpgradeHelper;", "setAppUpgradeHelper", "(Lcom/example/appupgrade/export/AppUpgradeHelper;)V", "Lcom/xiaomi/fitness/account/user/UserInfoManager;", "mUserInfoManager", "Lcom/xiaomi/fitness/account/user/UserInfoManager;", "getMUserInfoManager", "()Lcom/xiaomi/fitness/account/user/UserInfoManager;", "setMUserInfoManager", "(Lcom/xiaomi/fitness/account/user/UserInfoManager;)V", "Lcom/google/android/material/tabs/TabLayout;", "mainTlBottom", "Lcom/google/android/material/tabs/TabLayout;", "getMainTlBottom", "()Lcom/google/android/material/tabs/TabLayout;", "setMainTlBottom", "(Lcom/google/android/material/tabs/TabLayout;)V", "mCurrentTab", "I", "mCurrentFragment", "Landroidx/fragment/app/Fragment;", "Lcom/xiaomi/fitness/main/utils/SimplifiedChineseModel;", "simplifiedChineseModel$delegate", "getSimplifiedChineseModel", "()Lcom/xiaomi/fitness/main/utils/SimplifiedChineseModel;", "simplifiedChineseModel", "sportPageIndex", "getSportPageIndex", "()I", "setSportPageIndex", "isShowKeepAliveDialog", "setShowKeepAliveDialog", "hasNoticeLoginForAppLink", "getHasNoticeLoginForAppLink", "setHasNoticeLoginForAppLink", "", "faceEntranceOperationTitle", "Ljava/util/Map;", "Lkotlin/Function1;", "mNetworkListener", "Lkotlin/jvm/functions/Function1;", "getMNetworkListener", "()Lkotlin/jvm/functions/Function1;", "com/xiaomi/fitness/main/MainActivity$mDataChangedReceiver$1", "mDataChangedReceiver", "Lcom/xiaomi/fitness/main/MainActivity$mDataChangedReceiver$1;", "mGuideFinishListener", "Landroid/os/Handler;", "mTimerHandler", "Landroid/os/Handler;", "mSyncWebListener", "mAccountListener", "mDelegate", "Lpau;", "mDialog", "Lkotlin/Function0;", "countryChangeListener", "Lkotlin/jvm/functions/Function0;", "com/xiaomi/fitness/main/MainActivity$mServiceConnection$1", "mServiceConnection", "Lcom/xiaomi/fitness/main/MainActivity$mServiceConnection$1;", "isInBackground", "setInBackground", "Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;", "Lkotlin/ParameterName;", "name", "deviceChangeListener", "com/xiaomi/fitness/main/MainActivity$listenerDeviceManager$1", "listenerDeviceManager", "Lcom/xiaomi/fitness/main/MainActivity$listenerDeviceManager$1;", "SERVICE_PACKAGE", "Ljava/lang/String;", "com/xiaomi/fitness/main/MainActivity$mMediaImagePermissionChangeReceiver$1", "mMediaImagePermissionChangeReceiver", "Lcom/xiaomi/fitness/main/MainActivity$mMediaImagePermissionChangeReceiver$1;", "getLayoutId", "layoutId", "getViewModelId", "viewModelId", "Companion", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nMainActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MainActivity.kt\ncom/xiaomi/fitness/main/MainActivity\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 GsonExt.kt\ncom/xiaomi/fitness/common/extensions/GsonExtKt\n*L\n1#1,1597:1\n1869#2,2:1598\n1056#2:1600\n1878#2,3:1601\n1869#2,2:1606\n1#3:1604\n29#4:1605\n*S KotlinDebug\n*F\n+ 1 MainActivity.kt\ncom/xiaomi/fitness/main/MainActivity\n*L\n510#1:1598,2\n709#1:1600\n709#1:1601,3\n929#1:1606,2\n1354#1:1605\n*E\n"})
public final class MainActivity extends Hilt_MainActivity<MainViewModel, aeg> implements mf6, z9j, v7e, MainTabRedPointInterface {

    @NotNull
    private static final String ACTION_CHOICE_APP = "choose_app";

    @NotNull
    private static final String ACTION_CHOICE_APP_TIME = "choose_app_time";

    @NotNull
    private static final String ACTION_GLOBAL_DIALOG = "fitness.intent.action.ACTION_GLOBAL_DIALOG";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final String EXTRA_KEY_MEDIA_IMAGE_PERMISSION = "extra_key_media_image_permission";

    @NotNull
    public static final String RELATIVE_CARE_QR_CODE_ID_PREFIX = "com.mi.health://relativecare/qrcodeid/";

    @NotNull
    public static final String SPORT_TAB_INDEX = "sport_tab_index";

    @NotNull
    private static final String TAG = "MainActivity";

    @NotNull
    private static final String WEARABLE_PACKAGE_NAME = "com.xiaomi.wearable";
    public AppUpgradeHelper appUpgradeHelper;
    public CheckUpdateManager checkUpdate;
    private boolean enableAutoSetLayoutUiMode;
    private boolean hasNoticeLoginForAppLink;
    private boolean isShowKeepAliveDialog;

    @Nullable
    private Function1<? super Boolean, Unit> listener;

    @Nullable
    private Function1<? super Boolean, Unit> mAccountListener;
    private boolean mCheckShowGuide;

    @Nullable
    private Fragment mCurrentFragment;

    @Nullable
    private pau mDelegate;

    @Nullable
    private CommonDialog<?> mDialog;
    private boolean mFirstSetCommonProperties;

    @Nullable
    private CommonDialog<?> mGoToAppDialog;

    @Nullable
    private Function1<? super Boolean, Unit> mGuideFinishListener;
    private boolean mLoginOnetrack;
    public Set<MainTab> mMainTabs;
    private boolean mShowGuide;

    @Nullable
    private Function1<? super Boolean, Unit> mSyncWebListener;

    @Nullable
    private Handler mTimerHandler;
    public UserInfoManager mUserInfoManager;
    public TabLayout mainTlBottom;
    public PushManager pushManager;
    private int sportPageIndex;

    /* JADX INFO: renamed from: serverSyncer$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy serverSyncer = LazyKt.lazy(new fdg());
    private int mCurrentTab = -1;

    /* JADX INFO: renamed from: simplifiedChineseModel$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy simplifiedChineseModel = LazyKt.lazy(new hdg());

    @NotNull
    private final Map<String, String> faceEntranceOperationTitle = new LinkedHashMap();

    @NotNull
    private final Function1<Boolean, Unit> mNetworkListener = new MainActivity$mNetworkListener$1(this);

    @NotNull
    private final MainActivity$mDataChangedReceiver$1 mDataChangedReceiver = new MainActivity$mDataChangedReceiver$1(this);

    @NotNull
    private final Function0<Unit> countryChangeListener = new jdg();

    @NotNull
    private final MainActivity$mServiceConnection$1 mServiceConnection = new MainActivity$mServiceConnection$1(this);
    private boolean isInBackground = true;

    @NotNull
    private final Function1<WearableDeviceModel, Unit> deviceChangeListener = new ldg(this);

    @NotNull
    private final MainActivity$listenerDeviceManager$1 listenerDeviceManager = new MainActivity$listenerDeviceManager$1(this);

    @NotNull
    private final String SERVICE_PACKAGE = AppUpgradeUtil.PKG_NAME_TARGET;

    @NotNull
    private final MainActivity$mMediaImagePermissionChangeReceiver$1 mMediaImagePermissionChangeReceiver = new MainActivity$mMediaImagePermissionChangeReceiver$1();

    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/xiaomi/fitness/main/MainActivity$Companion;", "", "<init>", "()V", "TAG", "", "ACTION_CHOICE_APP", "ACTION_CHOICE_APP_TIME", "WEARABLE_PACKAGE_NAME", "ACTION_GLOBAL_DIALOG", "EXTRA_KEY_MEDIA_IMAGE_PERMISSION", "RELATIVE_CARE_QR_CODE_ID_PREFIX", "SPORT_TAB_INDEX", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$bindView$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u0007\u0010\u0006J\u0019\u0010\b\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\b\u0010\u0006¨\u0006\t"}, d2 = {"com/xiaomi/fitness/main/MainActivity$bindView$1", "Lcom/google/android/material/tabs/TabLayout$d;", "Lcom/google/android/material/tabs/TabLayout$g;", FaceSchema.FACE_PAGE_TAB, "", "onTabSelected", "(Lcom/google/android/material/tabs/TabLayout$g;)V", "onTabUnselected", "onTabReselected", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass1 implements TabLayout.d {
        final /* synthetic */ aeg $this_bindView;

        public AnonymousClass1(aeg aegVar) {
            this.$this_bindView = aegVar;
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabReselected(TabLayout.g tab) {
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabSelected(TabLayout.g tab) {
            Object objI = tab != null ? tab.i() : null;
            Intrinsics.checkNotNull(objI, "null cannot be cast to non-null type androidx.fragment.app.Fragment");
            Fragment fragment = (Fragment) objI;
            if (MainActivity.access$getSimplifiedChineseModel(MainActivity.this).isPrivacyEasyMode()) {
                MainActivity.access$selectSportTab(MainActivity.this, this.$this_bindView, fragment);
                return;
            }
            MainActivity mainActivity = MainActivity.this;
            LaunchExtensionKt.showFragment(mainActivity, gdk.c, fragment, MainActivity.access$getMCurrentFragment$p(mainActivity));
            MainActivity.access$setMCurrentFragment$p(MainActivity.this, fragment);
            MainActivity.access$shouldShowLoginWhenTokenInvalid(MainActivity.this);
            MainActivity.access$selected(MainActivity.this, tab, true);
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabUnselected(TabLayout.g tab) {
            if (tab != null) {
                MainActivity.access$selected(MainActivity.this, tab, false);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$doFastPairProductListener$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"com/xiaomi/fitness/main/MainActivity$doFastPairProductListener$1", "Lcom/xiaomi/fitness/device/manager/OnProductChangeListener;", "onChange", "", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29091 extends OnProductChangeListener {
        final /* synthetic */ BluetoothDevice $device;
        final /* synthetic */ Bundle $fastPairExtras;
        final /* synthetic */ String $modelId;

        public C29091(String str, BluetoothDevice bluetoothDevice, Bundle bundle) {
            this.$modelId = str;
            this.$device = bluetoothDevice;
            this.$fastPairExtras = bundle;
        }

        @Override // com.xiaomi.fitness.device.manager.OnProductChangeListener
        public void onChange() {
            WearableProductManager.Companion companion = WearableProductManager.INSTANCE;
            List productList$default = ProductManager.getProductList$default(DeviceManagerExtKt.getInstance(companion), null, 1, null);
            Logger.i("MainActivity", "fast pair product change listener onChange product.size = " + productList$default.size());
            MainActivity.access$launchBindDevice(MainActivity.this, productList$default, this.$modelId, this.$device, this.$fastPairExtras);
            DeviceManagerExtKt.getInstance(companion).removeProductChangeListener(this);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$handlePrivacySensitiveDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, d2 = {"com/xiaomi/fitness/main/MainActivity$handlePrivacySensitiveDialog$1", "Lcom/xiaomi/fitness/privacy/export/IPrivacySensitiveImpl;", "onPrivacyAccepted", "", "onPrivacyDenied", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29101 extends IPrivacySensitiveImpl {
        @Override // com.xiaomi.fitness.privacy.export.IPrivacySensitiveImpl, com.xiaomi.fitness.privacy.export.IPrivacySensitive
        public void onPrivacyAccepted() {
            PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE).setKeyUserSetting(true);
        }

        @Override // com.xiaomi.fitness.privacy.export.IPrivacySensitiveImpl, com.xiaomi.fitness.privacy.export.IPrivacySensitive
        public void onPrivacyDenied() {
            PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE).setKeyUserSetting(true);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$showGoToWearableAppDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J'\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\nJ'\u0010\r\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"com/xiaomi/fitness/main/MainActivity$showGoToWearableAppDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "", "isChecked", "onDialogMultiClick", "(Ljava/lang/String;IZ)V", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29111 extends sj7 {
        public C29111() {
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            Intrinsics.checkNotNullParameter(dialogName, "dialogName");
            Intrinsics.checkNotNullParameter(dialog, "dialog");
            super.onDialogClick(dialogName, dialog, which);
            if (which == -2) {
                dialog.dismiss();
                MainActivity.access$setMGoToAppDialog$p(MainActivity.this, null);
            } else {
                if (which != -1) {
                    return;
                }
                dialog.dismiss();
                Intent launchIntentForPackage = MainActivity.this.getPackageManager().getLaunchIntentForPackage("com.xiaomi.wearable");
                if (launchIntentForPackage != null) {
                    MainActivity.this.startActivity(launchIntentForPackage);
                }
                MainActivity.access$setMGoToAppDialog$p(MainActivity.this, null);
            }
        }

        @Override // defpackage.sj7
        public void onDialogMultiClick(String dialogName, int which, boolean isChecked) {
            Intrinsics.checkNotNullParameter(dialogName, "dialogName");
            super.onDialogMultiClick(dialogName, which, isChecked);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$showKeepLinkDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/main/MainActivity$showKeepLinkDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29121 extends sj7 {
        final /* synthetic */ int $titleResId;

        public C29121(int i) {
            this.$titleResId = i;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            super.onDialogClick(dialogName, dialog, which);
            if (which == -1) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                WebViewUtilKt.startWebView$default(MainCommonAppUrlsKt.getKeepAliveHelpUrl(LocaleUtil.getCurrentLocale(), "app"), ResourceExtKt.getString(this.$titleResId), false, false, null, false, null, false, 252, null);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$showRecognitionPermissionGuide$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0017¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/main/MainActivity$showRecognitionPermissionGuide$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29131 extends sj7 {
        final /* synthetic */ FragmentActivity $activity;
        final /* synthetic */ CommonDialog<?> $permissionDialog;

        public C29131(FragmentActivity fragmentActivity, CommonDialog<?> commonDialog) {
            this.$activity = fragmentActivity;
            this.$permissionDialog = commonDialog;
        }

        @Override // defpackage.sj7
        @RequiresApi(29)
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            super.onDialogClick(dialogName, dialog, which);
            if (which == -2) {
                this.$permissionDialog.dismiss();
            } else {
                if (which != -1) {
                    return;
                }
                SystemSetting.INSTANCE.goToSetting(this.$activity);
                this.$permissionDialog.dismiss();
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.main.MainActivity$showSyncWebDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J'\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/main/MainActivity$showSyncWebDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "main_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C29141 extends sj7 {
        public C29141() {
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
            MainActivity.access$triggerSyncWithServer(MainActivity.this);
        }
    }

    public static /* synthetic */ Unit A(MainActivity mainActivity, WearableDeviceModel wearableDeviceModel) {
        return deviceChangeListener$lambda$21(mainActivity, wearableDeviceModel);
    }

    public static /* synthetic */ Unit B(String str) {
        return showRecognitionPermission$lambda$41$lambda$40$lambda$38(str);
    }

    public static /* synthetic */ Unit C(MainActivity mainActivity, boolean z) {
        return checkUpdaterDialog$lambda$34(mainActivity, z);
    }

    public static /* synthetic */ Unit D() {
        return onResume$lambda$14();
    }

    public static /* synthetic */ Unit E(Function0 function0) {
        return showRecognitionPermission$lambda$42(function0);
    }

    public static /* synthetic */ Unit F(PermissionBuilder permissionBuilder) {
        return showRecognitionPermission$lambda$41$lambda$40(permissionBuilder);
    }

    public static /* synthetic */ Unit G(String str, Intent intent) {
        return handleQRScanResult$lambda$26(str, intent);
    }

    public static /* synthetic */ Unit H(FragmentActivity fragmentActivity, MainActivity mainActivity, String str) {
        return showRecognitionPermission$lambda$41$lambda$37(fragmentActivity, mainActivity, str);
    }

    public static /* synthetic */ Unit I(RegionInfo regionInfo) {
        return handleAutoLogin$lambda$33(regionInfo);
    }

    public static /* synthetic */ Unit J(MainActivity mainActivity) {
        return dealWithMedal$lambda$27(mainActivity);
    }

    public static /* synthetic */ FitnessServerSyncer K() {
        return serverSyncer_delegate$lambda$0();
    }

    public static /* synthetic */ Unit L(MainActivity mainActivity, boolean z) {
        return checkUpdaterDialog$lambda$35(mainActivity, z);
    }

    public static /* synthetic */ boolean M(MainActivity mainActivity) {
        return checkHealthKitUpgradeDialog$lambda$36(mainActivity);
    }

    public static /* synthetic */ void N(View view, String str) {
        showGoToWearableAppDialog$lambda$30(view, str);
    }

    public static /* synthetic */ void O(MainActivity mainActivity) {
        checkCommonApplication$lambda$29(mainActivity);
    }

    public static /* synthetic */ void P(MainActivity mainActivity) {
        showSyncWebDialog$lambda$25$lambda$24(mainActivity);
    }

    public static /* synthetic */ Unit Q(String str) {
        return showRecognitionPermission$lambda$41$lambda$40$lambda$39(str);
    }

    public static /* synthetic */ Unit R(MainActivity mainActivity, boolean z) {
        return handlePrivacySensitiveDialog$lambda$31(mainActivity, z);
    }

    public static /* synthetic */ Unit S(MainActivity mainActivity, boolean z) {
        return showPrivacyDialog$lambda$22(mainActivity, z);
    }

    public static /* synthetic */ Unit T(MainActivity mainActivity, boolean z) {
        return onCreate$lambda$5(mainActivity, z);
    }

    public static /* synthetic */ SimplifiedChineseModel U() {
        return simplifiedChineseModel_delegate$lambda$1();
    }

    public static /* synthetic */ Unit V(MainActivity mainActivity, ScannedDeviceInfo scannedDeviceInfo) {
        return onCreate$lambda$7(mainActivity, scannedDeviceInfo);
    }

    public static /* synthetic */ Unit W(FragmentActivity fragmentActivity, MainActivity mainActivity) {
        return showRecognitionPermission$lambda$41(fragmentActivity, mainActivity);
    }

    public static final /* synthetic */ Fragment access$getMCurrentFragment$p(MainActivity mainActivity) {
        return mainActivity.mCurrentFragment;
    }

    public static final /* synthetic */ SimplifiedChineseModel access$getSimplifiedChineseModel(MainActivity mainActivity) {
        return mainActivity.getSimplifiedChineseModel();
    }

    public static final /* synthetic */ void access$handlePrivacySensitiveDialog(MainActivity mainActivity) {
        mainActivity.handlePrivacySensitiveDialog();
    }

    public static final /* synthetic */ void access$launchBindDevice(MainActivity mainActivity, List list, String str, BluetoothDevice bluetoothDevice, Bundle bundle) {
        mainActivity.launchBindDevice(list, str, bluetoothDevice, bundle);
    }

    public static final /* synthetic */ void access$removeServiceConnectionListener(MainActivity mainActivity) {
        mainActivity.removeServiceConnectionListener();
    }

    public static final /* synthetic */ void access$selectSportTab(MainActivity mainActivity, aeg aegVar, Fragment fragment) {
        mainActivity.selectSportTab(aegVar, fragment);
    }

    public static final /* synthetic */ void access$selected(MainActivity mainActivity, TabLayout.g gVar, boolean z) {
        mainActivity.selected(gVar, z);
    }

    public static final /* synthetic */ void access$setMCurrentFragment$p(MainActivity mainActivity, Fragment fragment) {
        mainActivity.mCurrentFragment = fragment;
    }

    public static final /* synthetic */ void access$setMGoToAppDialog$p(MainActivity mainActivity, CommonDialog commonDialog) {
        mainActivity.mGoToAppDialog = commonDialog;
    }

    public static final /* synthetic */ void access$shouldShowLoginWhenTokenInvalid(MainActivity mainActivity) {
        mainActivity.shouldShowLoginWhenTokenInvalid();
    }

    public static final /* synthetic */ void access$triggerSyncWithServer(MainActivity mainActivity) {
        mainActivity.triggerSyncWithServer();
    }

    private final void checkCarCard(Bundle bundle) {
        if (Intrinsics.areEqual(bundle.get("key_tab"), Integer.valueOf(MainTab.INSTANCE.getINDEX_DEVICE())) && bundle.getBoolean("nfc", false)) {
            int i = bundle.getInt("act", -1);
            if (i != 0) {
                if (i != 1) {
                    return;
                }
                String string = bundle.getString("card_name");
                NfcManager nfcManagerExtKt = NfcManagerExtKt.getInstance(NfcManager.INSTANCE);
                if (string == null) {
                    string = "";
                }
                nfcManagerExtKt.showDeleteCarDialog(this, string);
                return;
            }
            String string2 = bundle.getString("mailbox_id");
            Intrinsics.checkNotNull(string2);
            Logger.i("FITNESS.PUSH", "mailboxId:" + string2);
            NfcManagerExtKt.getInstance(NfcManager.INSTANCE).showCanIssueCarDialog(this, string2);
        }
    }

    private final void checkCommonApplication() {
        if (ConfigPreference.INSTANCE.getIS_APP_CHOSEN_OPEN() && !AppUtil.INSTANCE.isPlayChannel() && AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            int chooseApp = ChooseAppPreference.INSTANCE.getChooseApp();
            boolean intentStartApp = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).getIntentStartApp();
            Logger.i("MainActivity", "checkCommonApplication commonApp=" + chooseApp + ", intentStartActivity is " + intentStartApp);
            if (intentStartApp && chooseApp == 2) {
                Logger.i("MainActivity", "checkCommonApplication SET INTENT START APP FALSE");
                WearableDeviceManager.Companion companion = WearableDeviceManager.INSTANCE;
                WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(companion).getCurrentDeviceModel();
                if (wearableDeviceModel != null) {
                    DeviceManagerExtKt.getInstance(companion).disconnectDevice(wearableDeviceModel.getDid());
                }
                postDelayed(500L, new vcg(this));
            }
        }
    }

    private static final void checkCommonApplication$lambda$29(MainActivity mainActivity) {
        mainActivity.showGoToWearableAppDialog();
    }

    private final void checkHealthKitUpgradeDialog() {
        if (RomUtils.INSTANCE.isMiui() && !AppUtil.INSTANCE.isPlayChannel() && isKitRouteEnabled()) {
            AppUpgradeHelper appUpgradeHelper = getAppUpgradeHelper();
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            Intrinsics.checkNotNullExpressionValue(supportFragmentManager, "getSupportFragmentManager(...)");
            appUpgradeHelper.doCheckHealthKitUpgrade(supportFragmentManager, new tcg(this));
        }
    }

    private static final boolean checkHealthKitUpgradeDialog$lambda$36(MainActivity mainActivity) {
        StartupDialogTracker startupDialogTracker = StartupDialogTracker.INSTANCE;
        boolean z = (startupDialogTracker.hasDialogShownThisSession() || mainActivity.getAppUpgradeHelper().isHKUpgradeShownToday()) ? false : true;
        if (z) {
            startupDialogTracker.markDialogShown();
        }
        return z;
    }

    private final void checkUpdaterDialog() {
        FitnessDataPreference fitnessDataPreference = FitnessDataPreference.INSTANCE;
        if (!Intrinsics.areEqual(fitnessDataPreference.getDEVICE_SYNC_INVALID_VERSION(), AppVersionUtil.INSTANCE.getAppVersionName()) || fitnessDataPreference.getDEVICE_SYNC_INVALID_MODEL_INFO().length() <= 0) {
            AppUpgradeHelper appUpgradeHelper = getAppUpgradeHelper();
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            Intrinsics.checkNotNullExpressionValue(supportFragmentManager, "getSupportFragmentManager(...)");
            appUpgradeHelper.doCheckAppUpgrade(supportFragmentManager, new lcg(this));
            return;
        }
        String updater_dialog_app_in_black_list = ConfigPreference.INSTANCE.getUPDATER_DIALOG_APP_IN_BLACK_LIST();
        if (updater_dialog_app_in_black_list.length() > 0) {
            List list = (List) new Gson().fromJson(updater_dialog_app_in_black_list, new MainActivity$checkUpdaterDialog$$inlined$fromJson$1().getType());
            if (!list.isEmpty() && list.contains(fitnessDataPreference.getDEVICE_SYNC_INVALID_MODEL_INFO())) {
                Logger.i("DEVICE_SYNC_INVALID_VERSION " + fitnessDataPreference.getDEVICE_SYNC_INVALID_VERSION() + ", deviceMode in black " + fitnessDataPreference.getDEVICE_SYNC_INVALID_MODEL_INFO(), new Object[0]);
                AppUpgradeHelper appUpgradeHelper2 = getAppUpgradeHelper();
                FragmentManager supportFragmentManager2 = getSupportFragmentManager();
                Intrinsics.checkNotNullExpressionValue(supportFragmentManager2, "getSupportFragmentManager(...)");
                appUpgradeHelper2.doCheckAppUpgrade(supportFragmentManager2, new kcg(this));
                return;
            }
        }
        Logger.i("DEVICE_SYNC_INVALID_VERSION " + fitnessDataPreference.getDEVICE_SYNC_INVALID_VERSION() + ", deviceMode " + fitnessDataPreference.getDEVICE_SYNC_INVALID_MODEL_INFO(), new Object[0]);
        StartupDialogTracker.INSTANCE.markDialogShown();
        AppUpgradeHelper appUpgradeHelper3 = getAppUpgradeHelper();
        FragmentManager supportFragmentManager3 = getSupportFragmentManager();
        Intrinsics.checkNotNullExpressionValue(supportFragmentManager3, "getSupportFragmentManager(...)");
        appUpgradeHelper3.doCheckAppUpgradeInSyncError(supportFragmentManager3);
    }

    private static final Unit checkUpdaterDialog$lambda$34(MainActivity mainActivity, boolean z) {
        if (!z) {
            mainActivity.checkHealthKitUpgradeDialog();
        }
        return Unit.INSTANCE;
    }

    private static final Unit checkUpdaterDialog$lambda$35(MainActivity mainActivity, boolean z) {
        if (!z) {
            mainActivity.checkHealthKitUpgradeDialog();
        }
        return Unit.INSTANCE;
    }

    private final boolean checkWhetherScanResultCanBeQrCodeId(String scanResult) {
        return StringsKt.startsWith$default(scanResult, "com.mi.health://relativecare/qrcodeid/", false, 2, (Object) null);
    }

    private static final Unit countryChangeListener$lambda$4() {
        String currentLocalCountry = RegionUtil.getCurrentLocalCountry();
        Logger.i("MainActivity", "Region Change : Send to Device " + currentLocalCountry + " ");
        j3r j3rVar = new j3r();
        w2r w2rVar = new w2r();
        w2r.d dVar = new w2r.d();
        dVar.c = currentLocalCountry;
        w2rVar.e = dVar;
        j3rVar.m0(w2rVar);
        vit vitVar = new vit();
        vitVar.e = 2;
        vitVar.f = 44;
        vitVar.c0(j3rVar);
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
        if (wearableDeviceModel != null) {
            if (wearableDeviceModel.isDeviceConnected()) {
                DeviceContact.call$default(DeviceSyncExtKt.getInstance(DeviceContact.INSTANCE), vitVar, false, new MainActivity$countryChangeListener$1$1$1(), 0, 8, null);
            } else {
                Logger.i("MainActivity", "Region Change : Device is disconnect");
            }
        }
        return Unit.INSTANCE;
    }

    private final pau createReactActivityDelegate() {
        return new pau(this);
    }

    private final void dealWithMedal() {
        MedalManagerExtKt.getInstance(MedalManager.INSTANCE).checkBirthday();
        MembershipHelperExtKt.getInstance(MembershipHelper.INSTANCE).showVipExpiredFaceDialog(new qcg(this));
    }

    private static final Unit dealWithMedal$lambda$27(MainActivity mainActivity) {
        MedalManager.requestMedalList$default(MedalManagerExtKt.getInstance(MedalManager.INSTANCE), mainActivity, 0, 2, null);
        return Unit.INSTANCE;
    }

    private static final Unit deviceChangeListener$lambda$21(MainActivity mainActivity, WearableDeviceModel wearableDeviceModel) {
        if (wearableDeviceModel != null) {
            Logger.d("MainActivity", "deviceChangeListener isIgnoringBattery=" + pwn.y(mainActivity));
            if (!pwn.y(mainActivity) && !DeviceModelExtKt.isWearOS(wearableDeviceModel)) {
                KeepaliveTimePreference keepaliveTimePreference = KeepaliveTimePreference.INSTANCE;
                long keepaliveLastTime = keepaliveTimePreference.getKeepaliveLastTime();
                ConfigPreference configPreference = ConfigPreference.INSTANCE;
                Logger.i("MainActivity", "deviceChangeListener KeepaliveTimePreference.getKeepaliveLastTime()= " + keepaliveLastTime + " ConfigPreference.KEEPALIVE_REMAIN_TIME = " + configPreference.getAUTO_SHOW_KEEPALIVE());
                if ((System.currentTimeMillis() / ((long) 1000)) - keepaliveTimePreference.getKeepaliveLastTime() > configPreference.getAUTO_SHOW_KEEPALIVE()) {
                    mainActivity.showKeepLinkDialog();
                } else {
                    Logger.i("MainActivity", "deviceChangeListener showTime less than " + configPreference.getAUTO_SHOW_KEEPALIVE() + " s");
                }
                mainActivity.refreshDeviceTabIcon();
            }
            FaceHelper.Companion companion = FaceHelper.INSTANCE;
            boolean enableMarketV4 = FaceHelperExtKt.getInstance(companion).getEnableMarketV4();
            if (!AppUtil.INSTANCE.isPlayChannel() || enableMarketV4) {
                FaceHelperExtKt.getInstance(companion).requestLoadFaceEntranceV4(wearableDeviceModel, new ybg(enableMarketV4, wearableDeviceModel, new WeakReference(mainActivity), mainActivity), new xcg());
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x005e  */
    private static final Unit deviceChangeListener$lambda$21$lambda$19(boolean z, WearableDeviceModel wearableDeviceModel, WeakReference weakReference, MainActivity mainActivity, FaceEntranceRespWrapper wrapper) {
        String title;
        Object next;
        OperationDesc desc;
        int i;
        List<RecentOnlineListData> list;
        Intrinsics.checkNotNullParameter(wrapper, "wrapper");
        if (z) {
            List<RecentOnlineTabDataV4> recentOnlineDataV4 = wrapper.getRecentOnlineDataV4();
            if (recentOnlineDataV4 == null) {
                return Unit.INSTANCE;
            }
            Iterator<T> it = recentOnlineDataV4.iterator();
            do {
                title = null;
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((RecentOnlineTabDataV4) next).getKey(), "new_recommend"));
            RecentOnlineTabDataV4 recentOnlineTabDataV4 = (RecentOnlineTabDataV4) next;
            int count = recentOnlineTabDataV4 != null ? recentOnlineTabDataV4.getCount() : 0;
            FaceHelper.Companion companion = FaceHelper.INSTANCE;
            FaceHelperExtKt.getInstance(companion).setFaceEntranceRedPoint(wearableDeviceModel.getDid(), count >= 5);
            String faceEntranceOperationTitle = FaceHelperExtKt.getInstance(companion).getFaceEntranceOperationTitle(wearableDeviceModel.getDid());
            OperationConfig operationConfig = wrapper.getOperationConfig();
            if (operationConfig != null && (desc = operationConfig.getDesc()) != null) {
                title = desc.getTitle();
            }
            mainActivity.faceEntranceOperationTitle.put(wearableDeviceModel.getDid(), title);
            FaceHelperExtKt.getInstance(companion).setFaceEntranceOperationRedPoint(wearableDeviceModel.getDid(), (title == null || title.length() <= 0 || Intrinsics.areEqual(title, faceEntranceOperationTitle)) ? false : true);
            MainActivity mainActivity2 = (MainActivity) weakReference.get();
            if (mainActivity2 != null) {
                mainActivity2.refreshDeviceTabIcon();
            }
            Logger.i("MainActivity", "requestLoadFaceEntranceV4 success: " + count);
        } else {
            RecentOnlineData recentOnlineDataV2 = wrapper.getRecentOnlineDataV2();
            if (recentOnlineDataV2 == null) {
                return Unit.INSTANCE;
            }
            if (!recentOnlineDataV2.getHasRecent() || (list = recentOnlineDataV2.getList()) == null || list.isEmpty()) {
                i = 0;
            } else {
                long enterFaceEntranceTime = FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).getEnterFaceEntranceTime(wearableDeviceModel.getDid());
                List<RecentOnlineListData> list2 = recentOnlineDataV2.getList();
                if (list2 != null) {
                    Iterator<T> it2 = list2.iterator();
                    i = 0;
                    while (it2.hasNext()) {
                        if (((RecentOnlineListData) it2.next()).getTime() > enterFaceEntranceTime) {
                            i++;
                        }
                    }
                } else {
                    i = 0;
                }
            }
            FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).setFaceEntranceRedPoint(wearableDeviceModel.getDid(), i >= 5);
            MainActivity mainActivity3 = (MainActivity) weakReference.get();
            if (mainActivity3 != null) {
                mainActivity3.refreshDeviceTabIcon();
            }
            Logger.i("MainActivity", "requestLoadFaceEntranceV4 success: " + i);
        }
        return Unit.INSTANCE;
    }

    private static final Unit deviceChangeListener$lambda$21$lambda$20(int i) {
        Logger.i("MainActivity", "requestLoadFaceEntranceV4 fail: " + i);
        return Unit.INSTANCE;
    }

    private final void doAppLinkAction(Uri uri) {
        Intent intent = new Intent(this, (Class<?>) AppLinkOAuthActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private final void doFastPairProductListener(String modelId, BluetoothDevice device, Bundle fastPairExtras) {
        WearableProductManager.Companion companion = WearableProductManager.INSTANCE;
        List<? extends Product> productList$default = ProductManager.getProductList$default(DeviceManagerExtKt.getInstance(companion), null, 1, null);
        if (productList$default.isEmpty()) {
            Logger.i("MainActivity", "fast pair product list is empty add product change listener");
            DeviceManagerExtKt.getInstance(companion).addProductChangeListener(new C29091(modelId, device, fastPairExtras));
        } else {
            Logger.i("MainActivity", "fast pair product list is not empty");
            launchBindDevice(productList$default, modelId, device, fastPairExtras);
        }
    }

    private final FitnessServerSyncer getServerSyncer() {
        return (FitnessServerSyncer) this.serverSyncer.getValue();
    }

    private final SimplifiedChineseModel getSimplifiedChineseModel() {
        return (SimplifiedChineseModel) this.simplifiedChineseModel.getValue();
    }

    private final void gotoTargetPage() {
        MainTab mainTab;
        Bundle extras = getIntent().getExtras();
        String string = extras != null ? extras.getString(MainTab.PAGE_TYPE) : null;
        Bundle extras2 = getIntent().getExtras();
        String string2 = extras2 != null ? extras2.getString(MainTab.SON_PAGE_TYPE) : null;
        Logger.i("MainActivity", "gotoTargetPage " + string + " " + string2);
        if (Intrinsics.areEqual(string, "shareDevices")) {
            DeviceShareManagerExtKt.getInstance(DeviceShareManager.INSTANCE).gotoDeviceShareListActivity(this, string2);
        } else {
            if (!Intrinsics.areEqual(string, "otherDevice") || (mainTab = (MainTab) CollectionsKt.elementAtOrNull(getMMainTabs(), 1)) == null) {
                return;
            }
            DeviceManagerExtKt.getInstance(DeviceTool.INSTANCE).selectOtherDeviceTab(mainTab.getFragment());
        }
    }

    private final void handleAutoLogin() {
        this.mCheckShowGuide = true;
        Logger.d("MainActivity", "MainActivity is Login");
        checkUpdaterDialog();
        RegionManager.Companion companion = RegionManager.INSTANCE;
        RegionExtKt.getInstance(companion).setUserRegion(new RegionBean.CountryInfo(RegionExtKt.getInstance(companion).getLocalCountry()), new dcg());
    }

    private static final Unit handleAutoLogin$lambda$33(RegionInfo it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Logger.d("MainActivity", "setUserRegion country is " + it.getRegion());
        return Unit.INSTANCE;
    }

    private final void handlePrivacySensitiveDialog() {
        Object objValueToObject = DeviceSettingManager.INSTANCE.getInstance().readSettingItem("xiaomiwear_app", DeviceSettingConfigKey.KEY_PRIVACY_SENSITIVE).valueToObject(PrivacySensitive.class);
        PrivacySensitive privacySensitive = objValueToObject instanceof PrivacySensitive ? (PrivacySensitive) objValueToObject : null;
        Logger.d("MainActivity", "config-" + privacySensitive);
        if (privacySensitive != null && privacySensitive.isAgreeSensitive()) {
            showUserInfoGuide();
            return;
        }
        StartupDialogTracker.INSTANCE.markDialogShown();
        PrivacyHelper.Companion companion = PrivacyHelper.INSTANCE;
        PrivacyHelperPointKt.getInstance(companion).showPrivacySensitiveDialogIfNeed(this, new C29101());
        PrivacyHelperPointKt.getInstance(companion).addUserSettingFinishedListener(new zcg(this));
    }

    private static final Unit handlePrivacySensitiveDialog$lambda$31(MainActivity mainActivity, boolean z) {
        if (PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE).getKeyUserSetting()) {
            mainActivity.showUserInfoGuide();
        }
        return Unit.INSTANCE;
    }

    private final void handleQRScanResult(String scanResult) {
        List listEmptyList;
        Logger.i("MainActivity", "handleQRScanResult: " + scanResult);
        if (TextUtils.isEmpty(scanResult) || !(StringsKt.startsWith$default(scanResult, "https:", false, 2, (Object) null) || StringsKt.startsWith$default(scanResult, "http:", false, 2, (Object) null))) {
            if (!checkWhetherScanResultCanBeQrCodeId(scanResult)) {
                ToastExtKt.toastShort(this, umk.common_not_support);
                return;
            }
            String strSubstring = scanResult.substring(38);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            try {
                long j = Long.parseLong(strSubstring);
                if (j > 0) {
                    IRelativeCareAccessApi.goToConfirmUserinfoPage$default(RelativeCareAccessExtKt.getINSTANCE(IRelativeCareAccessApi.INSTANCE), this, null, null, Long.valueOf(j), 6, null);
                    return;
                }
                Logger.i("MainActivity", "tried to convert scan result to qrCodeId but qrCodeLong=" + j + " <=0");
                ToastExtKt.toastShort(this, umk.common_not_support);
                return;
            } catch (Exception e) {
                Logger.i("MainActivity", "tried to convert scan result to qrCodeId but failed. message=" + e.getMessage());
                ToastExtKt.toastShort(this, umk.common_not_support);
                return;
            }
        }
        if (StringsKt.startsWith$default(scanResult, "https://watch.iot.mi.com/html/user-guide/index.html?", false, 2, (Object) null)) {
            WebViewUtilKt.startWebView$default(scanResult, getString(umk.s), false, false, null, false, null, false, 252, null);
            return;
        }
        if (!StringsKt.startsWith$default(scanResult, "https://jd.ubicdn.com", false, 2, (Object) null)) {
            LaunchExtensionKt.startActivity(this, (Class<?>) HttpLinkActivity.class, new mcg(scanResult));
            return;
        }
        String query = new URL(scanResult).getQuery();
        if (query == null || (listEmptyList = StringsKt.split$default((CharSequence) query, new String[]{dg6.CONNECT_STR}, false, 0, 6, (Object) null)) == null) {
            listEmptyList = CollectionsKt.emptyList();
        }
        Iterator it = listEmptyList.iterator();
        String str = "";
        while (true) {
            String str2 = "";
            while (true) {
                if (!it.hasNext()) {
                    IJustDanceApi.gotoControlActivity$default(JustDanceExtKt.getInstance(IJustDanceApi.INSTANCE), this, str, str2, false, 8, null);
                    return;
                }
                List listSplit$default = StringsKt.split$default((CharSequence) it.next(), new String[]{"="}, false, 0, 6, (Object) null);
                if (Intrinsics.areEqual(listSplit$default.get(0), DeviceInfo.Builder.PrivateDataKey.IP) && (str = (String) CollectionsKt.getOrNull(listSplit$default, 1)) == null) {
                    str = "";
                }
                if (!Intrinsics.areEqual(listSplit$default.get(0), am.K) || (str2 = (String) CollectionsKt.getOrNull(listSplit$default, 1)) != null) {
                }
            }
        }
    }

    private static final Unit handleQRScanResult$lambda$26(String str, Intent it) {
        Intrinsics.checkNotNullParameter(it, "it");
        it.putExtra(UriParserExportKt.KEY_LINK_URI, str);
        it.putExtra(UriParserExportKt.KEY_FROM, 3);
        return Unit.INSTANCE;
    }

    private final void initBundleParams(Intent intent) {
        Bundle extras = intent != null ? intent.getExtras() : null;
        boolean z = false;
        if (extras != null) {
            initIndex(extras);
            checkCarCard(extras);
            boolean z2 = extras.getBoolean(MainTab.KEY_SHOW_CHOOSE_COMMON_APP, false);
            Bundle extras2 = intent.getExtras();
            this.sportPageIndex = extras2 != null ? extras2.getInt(SPORT_TAB_INDEX) : 0;
            gotoTargetPage();
            z = z2;
        }
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).setShowCommonAppDialog(z);
        Logger.i("MainActivity", "mCurrentTab is " + this.mCurrentTab + " shouldShowCommonDialog is " + z);
    }

    private final void initIndex(Bundle bundle) {
        int iIntValue;
        Object obj = bundle.get("key_tab");
        if (obj instanceof Integer) {
            iIntValue = ((Number) obj).intValue();
        } else {
            iIntValue = obj instanceof String ? Integer.parseInt((String) obj) : -1;
        }
        this.mCurrentTab = iIntValue;
    }

    private final boolean isKitRouteEnabled() {
        return kitAppIsInstall() && ConfigPreference.INSTANCE.getHEALTHKIT_ROUTE_ENABLED();
    }

    private final boolean kitAppIsInstall() {
        try {
            AppUtil.getApp().getPackageManager().getPackageInfo(this.SERVICE_PACKAGE, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        } catch (Exception e) {
            Logger.e("MainActivity", "isConnected query failed", e);
            return false;
        }
    }

    private final void launchBindDevice(List<? extends Product> productList, String modelId, BluetoothDevice device, Bundle fastPairExtras) {
        for (Product product : productList) {
            List<String> wearOsModelIds = product.getWearOsModelIds();
            if (wearOsModelIds != null && CollectionsKt.contains(wearOsModelIds, modelId)) {
                Logger.i("MainActivity", "fast pair product matched " + product.getModel());
                WearOSUtil.Companion companion = WearOSUtil.INSTANCE;
                if (WearOsExtKt.getInstance(companion).isWearosRequirementNotPass()) {
                    ToastExtKt.toastLong(this, "Please upgrade Google GMS version first");
                    return;
                }
                if (WearOsExtKt.getInstance(companion).isUnderOobe()) {
                    Logger.i("MainActivity", "fast pair returned as it's already under oobe");
                    return;
                }
                String productName = product.getProductName();
                if (productName == null) {
                    productName = "";
                }
                String address = device.getAddress();
                Intrinsics.checkNotNullExpressionValue(address, "getAddress(...)");
                DeviceManagerExtKt.launchToBindDevice$default(this, productName, address, null, product, 5, "", fastPairExtras, false, 128, null);
                return;
            }
        }
    }

    private static final Unit onCreate$lambda$5(MainActivity mainActivity, boolean z) {
        Logger.d("MainActivity", "getAppUpgrade ");
        if (z && RegionPreference.INSTANCE.getPRIVACY_AGREE()) {
            mainActivity.mShowGuide = true;
            mainActivity.handleAutoLogin();
            Intent intent = mainActivity.getIntent();
            Intrinsics.checkNotNullExpressionValue(intent, "getIntent(...)");
            mainActivity.processAppLink(intent);
        }
        return Unit.INSTANCE;
    }

    private static final Unit onCreate$lambda$7(MainActivity mainActivity, ScannedDeviceInfo scannedDeviceInfo) {
        DialogHelper dialogHelper = DialogHelper.INSTANCE;
        FragmentManager supportFragmentManager = mainActivity.getSupportFragmentManager();
        Intrinsics.checkNotNullExpressionValue(supportFragmentManager, "getSupportFragmentManager(...)");
        Intrinsics.checkNotNull(scannedDeviceInfo);
        dialogHelper.boundEarPhone(supportFragmentManager, scannedDeviceInfo, new ucg(scannedDeviceInfo, mainActivity));
        return Unit.INSTANCE;
    }

    private static final Unit onCreate$lambda$7$lambda$6(ScannedDeviceInfo scannedDeviceInfo, MainActivity mainActivity) {
        Logger.i("MainActivity", "goto bind ear phone");
        bf.c().a(DeviceManagerExtKt.BIND_HEAD_PHONE_DEVICE_PATH).withInt("from", 1).withString("key_device_info", null).withParcelable("key_device_info", scannedDeviceInfo).navigation(mainActivity);
        return Unit.INSTANCE;
    }

    private static final Unit onResume$lambda$14() {
        StartupDialogTracker.INSTANCE.markDialogShown();
        return Unit.INSTANCE;
    }

    private final void postDelayed(long delayMillis, Runnable r2) {
        if (this.mTimerHandler == null) {
            this.mTimerHandler = new Handler(Looper.getMainLooper());
        }
        Handler handler = this.mTimerHandler;
        Intrinsics.checkNotNull(handler);
        handler.postDelayed(r2, delayMillis);
    }

    private final void processAppLink(Intent _intent) {
        IntentRelay intentRelay = IntentRelay.INSTANCE;
        Uri appLinkUri = intentRelay.getAppLinkUri(_intent);
        if (appLinkUri == null) {
            return;
        }
        Logger.d("MainActivity", "[AppLink] processAppLink uri:" + appLinkUri);
        if (XiaomiOAuthorize.fromOAuthUri(appLinkUri) == null) {
            return;
        }
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            doAppLinkAction(appLinkUri);
            intentRelay.consume(_intent);
        } else {
            if (this.hasNoticeLoginForAppLink) {
                return;
            }
            this.hasNoticeLoginForAppLink = true;
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).accountLogin();
        }
    }

    private final void processIntent(Intent _intent) {
        if (_intent != null) {
            String action = _intent.getAction();
            if (action != null && action.hashCode() == -2087046445 && action.equals(PushManagerKt.sMainPageHandlerPaddingAction)) {
                getPushManager().processPendingMessage();
            } else {
                processAppLink(_intent);
            }
        }
    }

    private final void refreshDeviceTabIcon() {
        WearableDeviceModel wearableDeviceModel;
        ImageView imageView;
        TabLayout.g gVarB = getMainTlBottom().B(2);
        View viewE = gVarB != null ? gVarB.e() : null;
        ViewGroup viewGroup = viewE instanceof ViewGroup ? (ViewGroup) viewE : null;
        if (viewGroup == null || (wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel()) == null || (imageView = (ImageView) viewGroup.findViewById(gdk.h)) == null) {
            return;
        }
        boolean z = (DeviceModelExtKt.isWearOS(wearableDeviceModel) || pwn.y(this)) ? false : true;
        String str = this.faceEntranceOperationTitle.get(wearableDeviceModel.getDid());
        ViewExtKt.setVisible(imageView, ((str == null || str.length() == 0) && FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).getFaceEntranceRedPoint(wearableDeviceModel.getDid())) || FaceHelperExtKt.getInstance(FaceHelper.INSTANCE).getFaceEntranceOperationRedPoint(wearableDeviceModel.getDid()) || z);
        Logger.d("MainActivity", "deviceChangeListener isIgnoringBattery=" + pwn.y(this));
    }

    private final void registerDataChangedBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("recent_data_changed_broadcast");
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(this.mDataChangedReceiver, intentFilter, 2);
        } else {
            mz4.registerReceiver(this, this.mDataChangedReceiver, intentFilter, 4);
        }
    }

    @SuppressLint({"UnspecifiedRegisterReceiverFlag"})
    private final void registerMediaImagePermissionChangeReceiver() {
        if (Build.VERSION.SDK_INT >= 29) {
            Logger.i("MainActivity", "not need register media image permission");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GLOBAL_DIALOG);
        registerReceiver(this.mMediaImagePermissionChangeReceiver, intentFilter);
    }

    private final void removeServiceConnectionListener() {
        ServiceConnector.removeServiceConnectListener$default(ServiceConnector.INSTANCE.getInstance(), this.mServiceConnection, 0, 2, null);
    }

    private final void requestDeviceTabRedPoint() {
        try {
            DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).addDeviceChangeListener(this.deviceChangeListener);
        } catch (Exception e) {
            Logger.e("MainActivity", "requestDeviceTabRedPoint error: " + e.getMessage());
        }
    }

    private final void selectSportTab(aeg aegVar, Fragment fragment) {
        MainActivity mainActivity;
        if (aegVar.B.getSelectedTabPosition() == MainTab.INSTANCE.getINDEX_SPORT()) {
            mainActivity = this;
            getSimplifiedChineseModel().showFragment(mainActivity, aegVar.B.getSelectedTabPosition(), gdk.c, fragment, this.mCurrentFragment);
            mainActivity.mCurrentFragment = fragment;
        } else {
            mainActivity = this;
        }
        SimplifiedChineseModel simplifiedChineseModel = getSimplifiedChineseModel();
        TabLayout mainTlBottom = aegVar.B;
        Intrinsics.checkNotNullExpressionValue(mainTlBottom, "mainTlBottom");
        simplifiedChineseModel.selectTab(this, mainTlBottom, mainActivity.mCurrentTab);
    }

    private final void selected(TabLayout.g gVar, boolean z) {
        int i = z ? b5k.a : b5k.c;
        if (gVar.e() instanceof TextView) {
            View viewE = gVar.e();
            Intrinsics.checkNotNull(viewE, "null cannot be cast to non-null type android.widget.TextView");
            ((TextView) viewE).setTextColor(getColor(i));
        }
        if (gVar.e() instanceof ConstraintLayout) {
            View viewE2 = gVar.e();
            Intrinsics.checkNotNull(viewE2, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout");
            TextView textView = (TextView) ((ConstraintLayout) viewE2).findViewById(gdk.e);
            if (textView != null) {
                textView.setTextColor(getColor(i));
            }
        }
    }

    private static final FitnessServerSyncer serverSyncer_delegate$lambda$0() {
        return FitnessDataExtKt.getInstance(FitnessServerSyncer.INSTANCE);
    }

    private final void shouldShowLoginWhenTokenInvalid() {
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin()) {
            AccountManagerExtKt.getInstance(TokenManager.INSTANCE).update();
        }
    }

    private final void showConnectNewPhoneIfNeed() {
        if (MainExtKt.getBindNew()) {
            SettingsPageUtilExtKt.getSettingsPageUtil().gotoConnectNewPhonePage(this);
            MainExtKt.setBindNew(false);
        }
    }

    @SuppressLint({"StringFormatInvalid"})
    private final void showGoToWearableAppDialog() {
        String string = ApplicationExtKt.getApplication().getResources().getString(umk.p, MainCommonAppUrlsKt.getSelectCommonApplicationUrl(LocaleUtil.getCurrentLocale$default(LocaleUtil.INSTANCE, false, false, 3, null)));
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        Spannable spannableA = LinkSpanHelper.a(this, string, Integer.valueOf(b5k.a), new pcg());
        CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).setIntentStartApp(false);
        CommonDialog<?> commonDialog = this.mGoToAppDialog;
        if (commonDialog == null || !commonDialog.isShowing()) {
            if (this.mGoToAppDialog == null) {
                this.mGoToAppDialog = new CommonDialog.c("goto").setDialogTitle(umk.o).setDialogDescriptionString(new DialogParams.DialogDescriptionString(spannableA)).setPositiveText(umk.n).setNegativeText(umk.l).setCanceledOnBackPressed(false).setCancelable(false).create();
            }
            CommonDialog<?> commonDialog2 = this.mGoToAppDialog;
            Intrinsics.checkNotNull(commonDialog2 != null ? commonDialog2.addDialogCallback(new C29111()) : null, "null cannot be cast to non-null type com.xiaomi.fitness.baseui.dialog.CommonDialog<*>");
            Logger.d("MainActivity", "SYNC WEB showIfNeed");
            StartupDialogTracker.INSTANCE.markDialogShown();
            CommonDialog<?> commonDialog3 = this.mGoToAppDialog;
            if (commonDialog3 != null) {
                commonDialog3.showIfNeed(getSupportFragmentManager());
            }
        }
    }

    private static final void showGoToWearableAppDialog$lambda$30(View view, String str) {
        Intrinsics.checkNotNull(str);
        WebViewUtilKt.startWebView$default(str, null, false, false, null, false, null, false, 254, null);
    }

    private final void showKeepLinkDialog() {
        WearableDeviceInfo deviceInfo;
        WearableDeviceInfo deviceInfo2;
        Logger.d("MainActivity", "showKeepLinkDialog isShowKeepAliveDialog = " + this.isShowKeepAliveDialog + " ");
        CommonDialog<?> commonDialog = this.mDialog;
        if ((commonDialog == null || !commonDialog.isShowing()) && !this.isShowKeepAliveDialog) {
            WearableDeviceManager.Companion companion = WearableDeviceManager.INSTANCE;
            WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(companion).getCurrentDeviceModel();
            int i = (wearableDeviceModel == null || (deviceInfo2 = wearableDeviceModel.getDeviceInfo()) == null || !deviceInfo2.isBandType()) ? umk.f : umk.f14906b;
            CommonDialog.c canceledOnTouchOutside = new CommonDialog.c("keep_link_permission").setDialogTitle(i).setCanceledOnTouchOutside(true);
            WearableDeviceModel wearableDeviceModel2 = (WearableDeviceModel) DeviceManagerExtKt.getInstance(companion).getCurrentDeviceModel();
            CommonDialog<?> commonDialogCreate = canceledOnTouchOutside.setDialogDescription((wearableDeviceModel2 == null || (deviceInfo = wearableDeviceModel2.getDeviceInfo()) == null || !deviceInfo.isBandType()) ? umk.c : umk.d).setCancelable(true).setPositiveText(umk.e).create();
            this.mDialog = commonDialogCreate;
            Intrinsics.checkNotNull(commonDialogCreate != null ? commonDialogCreate.addDialogCallback(new C29121(i)) : null, "null cannot be cast to non-null type com.xiaomi.fitness.baseui.dialog.CommonDialog<*>");
            KeepaliveTimePreference.INSTANCE.setKeepaliveLastTime(System.currentTimeMillis() / ((long) 1000));
            this.isShowKeepAliveDialog = true;
            StartupDialogTracker.INSTANCE.markDialogShown();
            CommonDialog<?> commonDialog2 = this.mDialog;
            if (commonDialog2 != null) {
                commonDialog2.showIfNeed(getSupportFragmentManager());
            }
        }
    }

    private final void showLoginDialogIfNeed() {
        if (AccountManagerExtKt.getInstance(AccountManager.INSTANCE).isLogin() || !MainExtKt.getShowLoginDialog()) {
            return;
        }
        CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).showLoginDialog(this);
        MainExtKt.setShowLoginDialog(false);
    }

    private final void showPrivacyDialog() {
        CheckerUserSettingManager.Companion companion = CheckerUserSettingManager.INSTANCE;
        CheckerUserSettingManagerExtKt.getInstance(companion).requestData(this);
        if (this.listener == null) {
            this.listener = new ubg(this);
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(companion);
            Function1<? super Boolean, Unit> function1 = this.listener;
            Intrinsics.checkNotNull(function1);
            checkerUserSettingManagerExtKt.addPrivacyDialogFinishedListener(function1);
        }
    }

    private static final Unit showPrivacyDialog$lambda$22(MainActivity mainActivity, boolean z) {
        if (CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).getPrivacyDialogFinished()) {
            Logger.i("MainActivity", "showPrivacySensitiveDialog finish ");
            mainActivity.showPrivacySensitiveDialog();
        }
        return Unit.INSTANCE;
    }

    private final void showPrivacySensitiveDialog() {
        if (this.listener != null) {
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
            Function1<? super Boolean, Unit> function1 = this.listener;
            Intrinsics.checkNotNull(function1);
            checkerUserSettingManagerExtKt.removePrivacyDialogFinishedListener(function1);
        }
        if (PrivacyHelperPointKt.getInstance(PrivacyHelper.INSTANCE).getKeyUserSetting()) {
            showUserInfoGuide();
            return;
        }
        if (AppUtil.INSTANCE.isPlayChannel() || !RegionExtKt.isInland(RegionExtKt.getInstance(RegionManager.INSTANCE))) {
            showUserInfoGuide();
            return;
        }
        DeviceSettingManager.Companion companion = DeviceSettingManager.INSTANCE;
        if (companion.getInstance().getUserLastUpdateTime() != 0) {
            handlePrivacySensitiveDialog();
        } else {
            Logger.d("MainActivity", "showPrivacySensitiveDialog addUpdateListener");
            companion.getInstance().addUpdateListener(this.listenerDeviceManager);
        }
    }

    private static final Unit showRecognitionPermission$lambda$41(FragmentActivity fragmentActivity, MainActivity mainActivity) {
        IPermissionManager instance = PermissionExportExtKt.getINSTANCE(IPermissionManager.INSTANCE);
        String string = mainActivity.getString(umk.u);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        instance.requestPermission(fragmentActivity, "android.permission.ACTIVITY_RECOGNITION", string, new rcg(fragmentActivity, mainActivity), new scg());
        return Unit.INSTANCE;
    }

    private static final Unit showRecognitionPermission$lambda$41$lambda$37(FragmentActivity fragmentActivity, MainActivity mainActivity, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (!ActivityRecognitionPermissionUtil.INSTANCE.checkPermissionDialogEnable(fragmentActivity)) {
            mainActivity.showRecognitionPermissionGuide(fragmentActivity);
        }
        return Unit.INSTANCE;
    }

    private static final Unit showRecognitionPermission$lambda$41$lambda$40(PermissionBuilder requestPermission) {
        Intrinsics.checkNotNullParameter(requestPermission, "$this$requestPermission");
        requestPermission.granted(new fcg());
        requestPermission.denied(new hcg());
        return Unit.INSTANCE;
    }

    private static final Unit showRecognitionPermission$lambda$41$lambda$40$lambda$38(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        FitnessDataPreference.INSTANCE.setRecognitionPermission(PermissionsUtil.checkPermission("android.permission.ACTIVITY_RECOGNITION"));
        CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).reportPermissionEvent(EventType.PERMISSION_HEALTH, EventStatus.AGREE, FunctionCode.MAIN);
        return Unit.INSTANCE;
    }

    private static final Unit showRecognitionPermission$lambda$41$lambda$40$lambda$39(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).reportPermissionEvent(EventType.PERMISSION_HEALTH, EventStatus.REJECT, FunctionCode.MAIN);
        return Unit.INSTANCE;
    }

    private static final Unit showRecognitionPermission$lambda$42(Function0 function0) {
        function0.invoke();
        return Unit.INSTANCE;
    }

    private final void showRecognitionPermissionGuide(FragmentActivity activity) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        Intrinsics.checkNotNullExpressionValue(supportFragmentManager, "getSupportFragmentManager(...)");
        CommonDialog commonDialogCreate = new CommonDialog.c("activity_recognition_permission").setDialogTitle(umk.w).setDialogDescription(umk.v).setPositiveText(umk.z).setNegativeText(umk.a).setCancelable(false).create();
        Intrinsics.checkNotNullExpressionValue(commonDialogCreate, "create(...)");
        Intrinsics.checkNotNull(commonDialogCreate.addDialogCallback(new C29131(activity, commonDialogCreate)), "null cannot be cast to non-null type com.xiaomi.fitness.baseui.dialog.CommonDialog<*>");
        StartupDialogTracker.INSTANCE.markDialogShown();
        commonDialogCreate.showIfNeed(supportFragmentManager);
        FitnessLogUtils.i("MiuiFitnessFunction", " recognition permission guide show");
    }

    private final void showSyncWebDialog() {
        RegionPreference regionPreference = RegionPreference.INSTANCE;
        if (regionPreference.getDefaultSyncWeb()) {
            dealWithMedal();
            return;
        }
        StartupDialogTracker.INSTANCE.markDialogShown();
        MainCommonAppUrlsKt.showSyncWebCommonDialog(this, new C29141());
        if (this.mSyncWebListener == null) {
            this.mSyncWebListener = new wbg(this);
        }
        Function1<? super Boolean, Unit> function1 = this.mSyncWebListener;
        Intrinsics.checkNotNull(function1);
        regionPreference.addSyncWebListener(function1);
    }

    private static final Unit showSyncWebDialog$lambda$25(MainActivity mainActivity, boolean z) {
        if (RegionPreference.INSTANCE.getDefaultSyncWeb()) {
            mainActivity.postDelayed(1000L, new ocg(mainActivity));
        }
        return Unit.INSTANCE;
    }

    private static final void showSyncWebDialog$lambda$25$lambda$24(MainActivity mainActivity) {
        mainActivity.dealWithMedal();
    }

    private final void showUserInfoGuide() {
        if (!this.mCheckShowGuide) {
            showSyncWebDialog();
            return;
        }
        if (this.mGuideFinishListener == null) {
            this.mGuideFinishListener = new wcg(this);
            SetPageManager setPageManageExtKt = SetPageManageExtKt.getInstance(SetPageManager.INSTANCE);
            Function1<? super Boolean, Unit> function1 = this.mGuideFinishListener;
            Intrinsics.checkNotNull(function1);
            setPageManageExtKt.addGuideFinishListener(function1);
        }
        syncLocalStatusToWeb(this.mShowGuide);
        this.mCheckShowGuide = false;
    }

    private static final Unit showUserInfoGuide$lambda$23(MainActivity mainActivity, boolean z) {
        boolean showGuidePageFinish = SetPageManageExtKt.getInstance(SetPageManager.INSTANCE).getShowGuidePageFinish();
        if (showGuidePageFinish) {
            mainActivity.showSyncWebDialog();
        }
        Logger.i("MainActivity", "showUserInfoGuide mFirstSetCommonProperties is " + mainActivity.mFirstSetCommonProperties + " SetPageManager.instance.getShowGuidePageFinish() is " + showGuidePageFinish);
        if (!mainActivity.mFirstSetCommonProperties) {
            OneTrackExtKt.getInstance(IOnetrack.INSTANCE).setCommonProperty();
            mainActivity.mFirstSetCommonProperties = true;
        }
        return Unit.INSTANCE;
    }

    private static final SimplifiedChineseModel simplifiedChineseModel_delegate$lambda$1() {
        return new SimplifiedChineseModel();
    }

    private final void syncLocalStatusToWeb(boolean isShowGuide) {
        Logger.d("MainActivity", "checkUserProfile start");
        CheckerUserSettingManager.Companion companion = CheckerUserSettingManager.INSTANCE;
        CheckerUserSettingManagerExtKt.getInstance(companion).checkUserProfile(isShowGuide);
        if (CheckerUserSettingManagerExtKt.getInstance(companion).getSelectMode() != -1) {
            Logger.d("MainActivity", "getSelectMode " + CheckerUserSettingManagerExtKt.getInstance(companion).getSelectMode());
            CheckerUserSettingManagerExtKt.getInstance(companion).setSelectModeToWeb(CheckerUserSettingManagerExtKt.getInstance(companion).getSelectMode());
        }
    }

    public static /* synthetic */ Unit t(MainActivity mainActivity, boolean z) {
        return showSyncWebDialog$lambda$25(mainActivity, z);
    }

    private final void triggerSyncWithServer() {
        FitnessServerSyncer.syncWithServer$default(getServerSyncer(), true, "cloud_sync_switch", null, 4, null);
    }

    public static /* synthetic */ Unit u() {
        return countryChangeListener$lambda$4();
    }

    public static /* synthetic */ Unit v(ScannedDeviceInfo scannedDeviceInfo, MainActivity mainActivity) {
        return onCreate$lambda$7$lambda$6(scannedDeviceInfo, mainActivity);
    }

    public static /* synthetic */ Unit w(MainActivity mainActivity, boolean z) {
        return showUserInfoGuide$lambda$23(mainActivity, z);
    }

    public static /* synthetic */ Unit x(int i) {
        return deviceChangeListener$lambda$21$lambda$20(i);
    }

    public static /* synthetic */ Unit y(boolean z, WearableDeviceModel wearableDeviceModel, WeakReference weakReference, MainActivity mainActivity, FaceEntranceRespWrapper faceEntranceRespWrapper) {
        return deviceChangeListener$lambda$21$lambda$19(z, wearableDeviceModel, weakReference, mainActivity, faceEntranceRespWrapper);
    }

    public final void attachModule(@Nullable String appName, @Nullable String module) {
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.a(appName, module);
        }
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.ViewDataBinder
    public /* bridge */ /* synthetic */ void bindView(i iVar) {
        bindView((aeg) iVar);
    }

    public final void doFastPairing() {
        Bundle fastPairExtras;
        BluetoothDevice bluetoothDevice;
        if (!AppUtil.INSTANCE.isPlayChannel() || (fastPairExtras = WearOsExtKt.getInstance(FastPairManager.INSTANCE).getFastPairExtras()) == null || !fastPairExtras.containsKey("android.bluetooth.device.extra.DEVICE") || (bluetoothDevice = (BluetoothDevice) fastPairExtras.getParcelable("android.bluetooth.device.extra.DEVICE")) == null) {
            return;
        }
        String string = fastPairExtras.getString(WearOsExtKt.EXTRA_MODEL_ID);
        Logger.i("MainActivity", "fast pair modelId " + string);
        ServiceConnector.Companion companion = ServiceConnector.INSTANCE;
        if (ServiceConnector.isConnected$default(companion.getInstance(), 0, 1, null)) {
            doFastPairProductListener(string, bluetoothDevice, fastPairExtras);
        } else {
            ServiceConnector.addServiceConnectListener$default(companion.getInstance(), this.mServiceConnection, 0, 2, null);
        }
    }

    @Override // defpackage.v7e
    @Nullable
    public u7e getActivityDelegate() {
        return this.mDelegate;
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

    @NotNull
    public final CheckUpdateManager getCheckUpdate() {
        CheckUpdateManager checkUpdateManager = this.checkUpdate;
        if (checkUpdateManager != null) {
            return checkUpdateManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("checkUpdate");
        return null;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public boolean getEnableAutoSetLayoutUiMode() {
        return this.enableAutoSetLayoutUiMode;
    }

    public final boolean getHasNoticeLoginForAppLink() {
        return this.hasNoticeLoginForAppLink;
    }

    @Override // com.xiaomi.fitness.baseui.ViewCreator
    public int getLayoutId() {
        return rgk.a;
    }

    @NotNull
    public final Set<MainTab> getMMainTabs() {
        Set<MainTab> set = this.mMainTabs;
        if (set != null) {
            return set;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mMainTabs");
        return null;
    }

    @NotNull
    public final Function1<Boolean, Unit> getMNetworkListener() {
        return this.mNetworkListener;
    }

    @NotNull
    public final UserInfoManager getMUserInfoManager() {
        UserInfoManager userInfoManager = this.mUserInfoManager;
        if (userInfoManager != null) {
            return userInfoManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mUserInfoManager");
        return null;
    }

    @NotNull
    public final TabLayout getMainTlBottom() {
        TabLayout tabLayout = this.mainTlBottom;
        if (tabLayout != null) {
            return tabLayout;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mainTlBottom");
        return null;
    }

    @NotNull
    public final PushManager getPushManager() {
        PushManager pushManager = this.pushManager;
        if (pushManager != null) {
            return pushManager;
        }
        Intrinsics.throwUninitializedPropertyAccessException("pushManager");
        return null;
    }

    public final int getSportPageIndex() {
        return this.sportPageIndex;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public int getViewModelId() {
        return z02.a;
    }

    @Override // defpackage.mf6
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    /* JADX INFO: renamed from: isInBackground, reason: from getter */
    public final boolean getIsInBackground() {
        return this.isInBackground;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public boolean isNeedRestore() {
        return false;
    }

    /* JADX INFO: renamed from: isShowKeepAliveDialog, reason: from getter */
    public final boolean getIsShowKeepAliveDialog() {
        return this.isShowKeepAliveDialog;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String stringExtra;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 666) {
            if (data == null || (stringExtra = data.getStringExtra("scan_result")) == null) {
                return;
            } else {
                handleQRScanResult(stringExtra);
            }
        }
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.d(requestCode, resultCode, data);
        }
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        ReactContext reactContextC;
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            a aVarC = pauVar.c();
            Activity currentActivity = (aVarC == null || (reactContextC = aVarC.C()) == null) ? null : reactContextC.getCurrentActivity();
            pau pauVar2 = this.mDelegate;
            Intrinsics.checkNotNull(pauVar2);
            if (!pauVar2.e() || currentActivity != this) {
                super.onBackPressed();
            }
        }
        finish();
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        setRequestedOrientation(UIUtils.INSTANCE.isTabletOrLandscape(this) ? -1 : 1);
        super.onConfigurationChanged(newConfig);
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.f(newConfig);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.main.Hilt_MainActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartupDialogTracker.INSTANCE.resetForNewSession();
        UIUtils uIUtils = UIUtils.INSTANCE;
        if (!uIUtils.isTabletOrLandscape(this) && getRequestedOrientation() != 1) {
            setRequestedOrientation(1);
        }
        this.mDelegate = createReactActivityDelegate();
        Logger.i("MainActivity", "MainActivity onCreate");
        initBundleParams(getIntent());
        setActionBarDisplayable(false);
        uIUtils.setNavigationBarColor(this, b5k.f1860b);
        AccountManager.Companion companion = AccountManager.INSTANCE;
        if (AccountManagerExtKt.getInstance(companion).isLogin()) {
            this.mShowGuide = false;
            handleAutoLogin();
        }
        if (this.mAccountListener == null) {
            this.mAccountListener = new zbg(this);
            AccountManager accountManagerExtKt = AccountManagerExtKt.getInstance(companion);
            Function1<? super Boolean, Unit> function1 = this.mAccountListener;
            Intrinsics.checkNotNull(function1);
            accountManagerExtKt.addAccountListener(function1);
        }
        NetworkExtKt.registerConnectChangeListener(this.mNetworkListener);
        MainTabRedPointManage.INSTANCE.setRedPointHandler(this);
        registerDataChangedBroadcast();
        registerMediaImagePermissionChangeReceiver();
        getPushManager().processPendingMessage();
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "getIntent(...)");
        processAppLink(intent);
        Trace.endSection();
        TabLayout mainTlBottom = ((aeg) getMBinding()).B;
        Intrinsics.checkNotNullExpressionValue(mainTlBottom, "mainTlBottom");
        setMainTlBottom(mainTlBottom);
        doFastPairing();
        showLoginDialogIfNeed();
        DeviceManager.requestDeviceList$default(com.xiaomi.fitness.device.manager.DeviceManagerExtKt.getInstance(DeviceManager.INSTANCE), null, null, 3, null);
        if (ActivityRecognitionPermissionUtil.INSTANCE.isHyperOSNAndroidV(this) && !PrivacyEasyModePreference.INSTANCE.isEasyMode() && !PermissionsUtil.checkPermission("android.permission.ACTIVITY_RECOGNITION")) {
            showRecognitionPermission(this);
        }
        requestDeviceTabRedPoint();
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.g(savedInstanceState);
        }
        EarphoneExtKt.getShowEarphoneDialog().observe(this, new MainActivity$sam$androidx_lifecycle_Observer$0(new bcg(this)));
        RegionExtKt.getInstance(RegionManager.INSTANCE).addCountryChangeListener(this.countryChangeListener);
    }

    @Override // com.xiaomi.fitness.main.Hilt_MainActivity, com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseViewModelActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            try {
                Intrinsics.checkNotNull(pauVar);
                pauVar.h();
                this.mDelegate = null;
                Logger.i("MainActivity", "React Native delegate destroyed successfully");
            } catch (Exception e) {
                Logger.e("MainActivity", "Error destroying React Native delegate: " + e.getMessage());
            }
        }
        MainTabRedPointManage.INSTANCE.setRedPointHandler(null);
        NetworkExtKt.unregisterConnectChangeListener(this.mNetworkListener);
        super.onDestroy();
        unregisterReceiver(this.mDataChangedReceiver);
        if (Build.VERSION.SDK_INT < 29) {
            unregisterReceiver(this.mMediaImagePermissionChangeReceiver);
            GlobalDialogManager.INSTANCE.cancelAllDialogs();
        }
        if (this.listener != null) {
            CheckerUserSettingManager checkerUserSettingManagerExtKt = CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE);
            Function1<? super Boolean, Unit> function1 = this.listener;
            Intrinsics.checkNotNull(function1);
            checkerUserSettingManagerExtKt.removePrivacyDialogFinishedListener(function1);
        }
        if (this.mGuideFinishListener != null) {
            SetPageManager setPageManageExtKt = SetPageManageExtKt.getInstance(SetPageManager.INSTANCE);
            Function1<? super Boolean, Unit> function2 = this.mGuideFinishListener;
            Intrinsics.checkNotNull(function2);
            setPageManageExtKt.removeGuideFinishListener(function2);
            this.mGuideFinishListener = null;
        }
        DeviceSettingManager.INSTANCE.getInstance().removeUpdateListener(this.listenerDeviceManager);
        Handler handler = this.mTimerHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        Function1<? super Boolean, Unit> function3 = this.mSyncWebListener;
        if (function3 != null) {
            RegionPreference regionPreference = RegionPreference.INSTANCE;
            Intrinsics.checkNotNull(function3);
            regionPreference.removeSyncWebListener(function3);
            this.mSyncWebListener = null;
        }
        if (this.mAccountListener != null) {
            AccountManager accountManagerExtKt = AccountManagerExtKt.getInstance(AccountManager.INSTANCE);
            Function1<? super Boolean, Unit> function4 = this.mAccountListener;
            Intrinsics.checkNotNull(function4);
            accountManagerExtKt.removeAccountListener(function4);
        }
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).removeDeviceChangeListener(this.deviceChangeListener);
        RegionExtKt.getInstance(RegionManager.INSTANCE).removeCountryChangeListener(this.countryChangeListener);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, @Nullable KeyEvent event) {
        pau pauVar = this.mDelegate;
        return (pauVar != null && pauVar.i(keyCode, event)) || super.onKeyUp(keyCode, event);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onNewIntent(@NotNull Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        Logger.d("MainActivity", "MainActivity onNewIntent");
        initBundleParams(intent);
        processIntent(intent);
        int intExtra = intent.getIntExtra(ACTION_CHOICE_APP, -1);
        long longExtra = intent.getLongExtra(ACTION_CHOICE_APP_TIME, 0L);
        Logger.i("MainActivity", "checkCommonApplication onNewIntent after is " + intExtra + " commonAPPTime is " + longExtra);
        if (intExtra != -1 && longExtra != 0) {
            ChooseAppPreference chooseAppPreference = ChooseAppPreference.INSTANCE;
            chooseAppPreference.setChooseApp(intExtra);
            chooseAppPreference.setChooseAppLastTime(longExtra);
            Logger.i("MainActivity", "checkCommonApplication SET INTENT START APP TRUE 1");
            CheckerUserSettingManagerExtKt.getInstance(CheckerUserSettingManager.INSTANCE).setIntentStartApp(true);
        }
        doFastPairing();
        checkCommonApplication();
        showLoginDialogIfNeed();
        TaskHelperExtKt.getInstance(ITask.INSTANCE).checkTask(intent, this);
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.isInBackground = true;
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.k();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public void onRelease() {
        ((aeg) getMBinding()).B.r();
        this.mCurrentFragment = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity, com.xiaomi.fitness.baseui.view.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    @RequiresApi(24)
    public void onResume() {
        super.onResume();
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.m();
        }
        refreshDeviceTabIcon();
        this.isInBackground = false;
        AccountManager.Companion companion = AccountManager.INSTANCE;
        if (AccountManagerExtKt.getInstance(companion).isLogin()) {
            showPrivacyDialog();
            if (!AppUtil.INSTANCE.isPlayChannel()) {
                DeviceShareManagerExtKt.getInstance(DeviceShareManager.INSTANCE).checkDeviceShareDialog(this, new jcg());
            }
        }
        Intent intent = getIntent();
        int index_sport = -1;
        Logger.i("MainActivity", "checkCommonApplication onResume commonApp is" + (intent != null ? Integer.valueOf(intent.getIntExtra(ACTION_CHOICE_APP, -1)) : null));
        checkCommonApplication();
        getCheckUpdate().checkHasPendingCheckUpdate();
        int i = this.mCurrentTab;
        if (i > -1 && i < ((aeg) getMBinding()).B.getTabCount()) {
            if (getSimplifiedChineseModel().isPrivacyEasyMode()) {
                TabLayout tabLayout = ((aeg) getMBinding()).B;
                MainTab.Companion companion2 = MainTab.INSTANCE;
                TabLayout.g gVarB = tabLayout.B(companion2.getINDEX_SPORT());
                if (gVarB != null) {
                    gVarB.m();
                }
                index_sport = companion2.getINDEX_SPORT();
            } else {
                if (this.mCurrentTab == MainTab.INSTANCE.getINDEX_SPORT()) {
                    TabLayout.g gVarB2 = getMainTlBottom().B(this.mCurrentTab);
                    Object objI = gVarB2 != null ? gVarB2.i() : null;
                    Intrinsics.checkNotNull(objI, "null cannot be cast to non-null type androidx.fragment.app.Fragment");
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPORT_TAB_INDEX, this.sportPageIndex);
                    Unit unit = Unit.INSTANCE;
                    ((Fragment) objI).setArguments(bundle);
                    this.sportPageIndex = 0;
                }
                TabLayout.g gVarB3 = ((aeg) getMBinding()).B.B(this.mCurrentTab);
                if (gVarB3 != null) {
                    gVarB3.m();
                }
            }
            this.mCurrentTab = index_sport;
        }
        if (AccountManagerExtKt.getInstance(companion).isLogin() && !this.mLoginOnetrack) {
            HashMap map = new HashMap();
            String strMD5 = MD5Util.MD5(getMUserInfoManager().getUserId());
            if (strMD5 != null) {
                AccountType accountType = AccountType.XIAOMI;
                Logger.d("MainActivity", "onLoginSuccess ontrack userid is " + strMD5 + " AccountType.XIAOMI is " + accountType + " mMap is " + map);
                OneTrackExtKt.getInstance(IOnetrack.INSTANCE).login(strMD5, accountType, map, false);
            }
            this.mLoginOnetrack = true;
        }
        NotificationPermission.INSTANCE.checkNotifyAndAlarmPermissionForAll(this);
        SportManagerExtKt.getInstance(ISportRemoteState.INSTANCE).checkBbLocationPermission();
        showConnectNewPhoneIfNeed();
        if (Build.VERSION.SDK_INT < 29 || PrivacyEasyModePreference.INSTANCE.isEasyMode()) {
            return;
        }
        boolean zCheckPermission = PermissionsUtil.checkPermission("android.permission.ACTIVITY_RECOGNITION");
        FitnessDataPreference fitnessDataPreference = FitnessDataPreference.INSTANCE;
        if (zCheckPermission != fitnessDataPreference.getGetRecognitionPermission()) {
            fitnessDataPreference.setRecognitionPermission(zCheckPermission);
        }
    }

    @Override // defpackage.z9j
    public void requestPermissions(@Nullable String[] permissions, int requestCode, @Nullable qaj listener) {
        Intrinsics.checkNotNull(permissions);
        super.requestPermissions(permissions, requestCode);
        pau pauVar = this.mDelegate;
        if (pauVar != null) {
            Intrinsics.checkNotNull(pauVar);
            pauVar.n(permissions, requestCode, listener);
        }
    }

    public final void setAppUpgradeHelper(@NotNull AppUpgradeHelper appUpgradeHelper) {
        Intrinsics.checkNotNullParameter(appUpgradeHelper, "<set-?>");
        this.appUpgradeHelper = appUpgradeHelper;
    }

    public final void setCheckUpdate(@NotNull CheckUpdateManager checkUpdateManager) {
        Intrinsics.checkNotNullParameter(checkUpdateManager, "<set-?>");
        this.checkUpdate = checkUpdateManager;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingActivity
    public void setEnableAutoSetLayoutUiMode(boolean z) {
        this.enableAutoSetLayoutUiMode = z;
    }

    public final void setHasNoticeLoginForAppLink(boolean z) {
        this.hasNoticeLoginForAppLink = z;
    }

    public final void setInBackground(boolean z) {
        this.isInBackground = z;
    }

    public final void setMMainTabs(@NotNull Set<MainTab> set) {
        Intrinsics.checkNotNullParameter(set, "<set-?>");
        this.mMainTabs = set;
    }

    public final void setMUserInfoManager(@NotNull UserInfoManager userInfoManager) {
        Intrinsics.checkNotNullParameter(userInfoManager, "<set-?>");
        this.mUserInfoManager = userInfoManager;
    }

    public final void setMainTlBottom(@NotNull TabLayout tabLayout) {
        Intrinsics.checkNotNullParameter(tabLayout, "<set-?>");
        this.mainTlBottom = tabLayout;
    }

    public final void setPushManager(@NotNull PushManager pushManager) {
        Intrinsics.checkNotNullParameter(pushManager, "<set-?>");
        this.pushManager = pushManager;
    }

    public final void setShowKeepAliveDialog(boolean z) {
        this.isShowKeepAliveDialog = z;
    }

    public final void setSportPageIndex(int i) {
        this.sportPageIndex = i;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseActivity
    public void setWindowFeature(int featureId) {
    }

    public final void showMainTabRedPoint(boolean isShow) {
        ImageView imageView;
        TabLayout.g gVarB = getMainTlBottom().B(MainTab.INSTANCE.getINDEX_ME());
        View viewE = gVarB != null ? gVarB.e() : null;
        ViewGroup viewGroup = viewE instanceof ViewGroup ? (ViewGroup) viewE : null;
        if (viewGroup == null || (imageView = (ImageView) viewGroup.findViewById(gdk.h)) == null) {
            return;
        }
        ViewExtKt.setVisible(imageView, isShow);
    }

    public final void showRecognitionPermission(@NotNull FragmentActivity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        bdg bdgVar = new bdg(activity, this);
        if (PermissionExtKt.supportMiuiPermissionEnable()) {
            bdgVar.invoke();
            return;
        }
        String string = getString(umk.t);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        PermissionsUtil.showPermissionExplanationDialog$default(PermissionsUtil.INSTANCE, activity, new String[]{"android.permission.ACTIVITY_RECOGNITION"}, string, new ddg(bdgVar), null, 16, null);
    }

    @Override // com.xiaomi.fitness.main.export.MainTabRedPointInterface
    public void showTabRedPoint(@NotNull String tabId, boolean isShow) {
        Intrinsics.checkNotNullParameter(tabId, "tabId");
        Logger.i("MainActivity", "setTabBadges" + tabId + "   小红点是否显示：" + isShow);
        if (StringsKt.contains$default((CharSequence) tabId, (CharSequence) MainTab.INSTANCE.getMY_TAB_RED_POINT_KEY(), false, 2, (Object) null)) {
            showMainTabRedPoint(isShow);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void bindView(@NotNull aeg aegVar) {
        Intrinsics.checkNotNullParameter(aegVar, "<this>");
        aegVar.B.addOnTabSelectedListener((TabLayout.d) new AnonymousClass1(aegVar));
        int i = 0;
        for (Object obj : CollectionsKt.sortedWith(getMMainTabs(), new MainActivity$bindView$$inlined$sortedBy$1())) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            MainTab mainTab = (MainTab) obj;
            TabLayout.g gVarE = ((aeg) getMBinding()).B.E();
            Intrinsics.checkNotNullExpressionValue(gVarE, "newTab(...)");
            nfg nfgVarX0 = nfg.x0(getLayoutInflater());
            Intrinsics.checkNotNullExpressionValue(nfgVarX0, "inflate(...)");
            gVarE.p(nfgVarX0.getRoot());
            nfgVarX0.A.setImageDrawable(ResourceExtKt.getResourceDrawable(this, mainTab.getIcon()));
            nfgVarX0.B.setText(mainTab.getName());
            gVarE.t(mainTab.getFragment());
            getSimplifiedChineseModel().setTabClickListener(this, gVarE, i);
            aegVar.B.h(gVarE, i);
            i = i2;
        }
        SimplifiedChineseModel simplifiedChineseModel = getSimplifiedChineseModel();
        TabLayout mainTlBottom = ((aeg) getMBinding()).B;
        Intrinsics.checkNotNullExpressionValue(mainTlBottom, "mainTlBottom");
        simplifiedChineseModel.selectDefaultTab(mainTlBottom);
    }
}
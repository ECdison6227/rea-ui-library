package com.xiaomi.fitness.eco.device.manager.ui.tab;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.xiaomi.accountsdk.account.XMPassportSettings;
import com.xiaomi.fitness.account.extensions.AccountManagerExtKt;
import com.xiaomi.fitness.account.user.UserInfoManager;
import com.xiaomi.fitness.baseui.dialog.CommonDialog;
import com.xiaomi.fitness.baseui.dialog.DialogParams;
import com.xiaomi.fitness.baseui.view.BaseActivity;
import com.xiaomi.fitness.baseui.view.PageState;
import com.xiaomi.fitness.blood_sugar.BloodSugarExtKt;
import com.xiaomi.fitness.blood_sugar.BloodSugarManager;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.AnyExtKt;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.MultiplePermissionsBuilder;
import com.xiaomi.fitness.common.extensions.NetworkExtKt;
import com.xiaomi.fitness.common.extensions.ToastExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.common.utils.MarketUtil;
import com.xiaomi.fitness.common.utils.SystemSetting;
import com.xiaomi.fitness.common.utils.ToastUtil;
import com.xiaomi.fitness.device.manager.DeviceModel;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.a;
import com.xiaomi.fitness.device.manager.e;
import com.xiaomi.fitness.device.manager.export.BluetoothPermissionTool;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceTabInfoProvider;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.l;
import com.xiaomi.fitness.eco.device.contact.export.EcoSyncResult;
import com.xiaomi.fitness.eco.device.contact.export.OnEcoSyncCallback;
import com.xiaomi.fitness.eco.device.manager.EcoDevicePreference;
import com.xiaomi.fitness.eco.device.manager.export.EcoDeviceManager;
import com.xiaomi.fitness.eco.device.manager.export.EcoDeviceManagerExtKt;
import com.xiaomi.fitness.eco.device.manager.export.EcoDeviceModel;
import com.xiaomi.fitness.eco.device.manager.export.bean.EcoDeviceInfo;
import com.xiaomi.fitness.eco.device.manager.ui.EcoDeviceDetail;
import com.xiaomi.fitness.eco.device.manager.ui.EcoViewHelper;
import com.xiaomi.fitness.eco.device.manager.utils.BluetoothPermissionUtil;
import com.xiaomi.fitness.electronic.scale.export.EcoDialogHelper;
import com.xiaomi.fitness.electronic.scale.export.EcoShareDialogExtKt;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleCMDHelper;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleCMDHelperExtKt;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleManager;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleManagerExtKt;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleRequest;
import com.xiaomi.fitness.electronic.scale.export.ElectronicScaleRequestExtKt;
import com.xiaomi.fitness.electronic.scale.export.bean.DelShareResponse;
import com.xiaomi.fitness.electronic.scale.export.bean.ScaleData;
import com.xiaomi.fitness.net.response.ApiException;
import com.xiaomi.fitness.rn.export.RNDeviceManager;
import com.xiaomi.fitness.rn.export.RNDeviceModel;
import com.xiaomi.fitness.rn.export.RNProductManager;
import com.xiaomi.fitness.rn.export.RnSettingsExportApi;
import com.xiaomi.fitness.rn.export.extensions.RNDeviceManagerExtKt;
import com.xiaomi.fitness.rn.export.extensions.RnSettingsExportApiExtKt;
import com.xiaomi.fitness.rn.extetions.ProductExtKt;
import defpackage.bwi;
import defpackage.cwi;
import defpackage.dwi;
import defpackage.ewi;
import defpackage.fwi;
import defpackage.gwi;
import defpackage.hwi;
import defpackage.iwi;
import defpackage.jgk;
import defpackage.jwi;
import defpackage.kwi;
import defpackage.lm8;
import defpackage.lmk;
import defpackage.lwi;
import defpackage.mwi;
import defpackage.nwi;
import defpackage.owi;
import defpackage.pwi;
import defpackage.qwi;
import defpackage.rwi;
import defpackage.sj7;
import defpackage.swi;
import defpackage.twi;
import defpackage.u02;
import defpackage.yck;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0083\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0005*\u0001I\b\u0007\u0018\u0000 L2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0001LB\u0011\b\u0007\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\n\u001a\u00020\tH\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0017\u0010\u000e\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u0017\u0010\u0010\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u0010\u0010\u000fJ\u001f\u0010\u0013\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0011H\u0002¢\u0006\u0004\b\u0013\u0010\u0014J'\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0015H\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u001f\u0010\u001c\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0015H\u0002¢\u0006\u0004\b\u001c\u0010\u001dJ)\u0010!\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u001fH\u0002¢\u0006\u0004\b!\u0010\"J\u0017\u0010#\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u001fH\u0002¢\u0006\u0004\b#\u0010$J\u0017\u0010%\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u001fH\u0002¢\u0006\u0004\b%\u0010$J\u000f\u0010&\u001a\u00020\tH\u0002¢\u0006\u0004\b&\u0010\u000bJ\u0017\u0010(\u001a\u00020\t2\u0006\u0010'\u001a\u00020\u0011H\u0002¢\u0006\u0004\b(\u0010)J\u0017\u0010*\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u0015H\u0002¢\u0006\u0004\b*\u0010+J\u000f\u0010-\u001a\u00020,H\u0002¢\u0006\u0004\b-\u0010.J!\u00103\u001a\u00020\t2\u0006\u00100\u001a\u00020/2\b\u00102\u001a\u0004\u0018\u000101H\u0016¢\u0006\u0004\b3\u00104J\u000f\u00105\u001a\u00020\tH\u0016¢\u0006\u0004\b5\u0010\u000bJ\u000f\u00106\u001a\u00020\tH\u0016¢\u0006\u0004\b6\u0010\u000bJ\u0017\u00108\u001a\u00020\u001f2\u0006\u00107\u001a\u00020,H\u0016¢\u0006\u0004\b8\u00109J\u000f\u0010:\u001a\u00020,H\u0016¢\u0006\u0004\b:\u0010.J\u000f\u0010;\u001a\u00020,H\u0016¢\u0006\u0004\b;\u0010.R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0006\u0010<R\u001c\u0010>\u001a\b\u0012\u0002\b\u0003\u0018\u00010=8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b>\u0010?R\u001c\u0010@\u001a\b\u0012\u0002\b\u0003\u0018\u00010=8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b@\u0010?R\u0016\u0010A\u001a\u00020,8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bA\u0010BR5\u0010G\u001a#\u0012\u0019\u0012\u0017\u0012\u0002\b\u0003\u0018\u00010D¢\u0006\f\bE\u0012\b\bF\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\t0C8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bG\u0010HR\u0014\u0010J\u001a\u00020I8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bJ\u0010K¨\u0006M"}, d2 = {"Lcom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4;", "Lcom/xiaomi/fitness/baseui/view/BaseBindingFragment;", "Lcom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabV4ViewModel;", "Llm8;", "Lcom/xiaomi/fitness/device/manager/export/DeviceTabInfoProvider;", "Lcom/xiaomi/fitness/eco/device/manager/export/EcoDeviceManager;", "mEcoDeviceManager", "<init>", "(Lcom/xiaomi/fitness/eco/device/manager/export/EcoDeviceManager;)V", "", "clearLiveData", "()V", "Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;", "deviceModel", "lostDevice", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;)V", "rebindDevice", "", "retry", "showRebindFail", "(Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;Z)V", "Lcom/xiaomi/fitness/eco/device/manager/ui/EcoDeviceDetail;", "it", "Landroidx/fragment/app/FragmentActivity;", "activity", "deviceDetail", "doUnbind", "(Lcom/xiaomi/fitness/eco/device/manager/ui/EcoDeviceDetail;Landroidx/fragment/app/FragmentActivity;Lcom/xiaomi/fitness/eco/device/manager/ui/EcoDeviceDetail;)V", "delShare", "(Landroidx/fragment/app/FragmentActivity;Lcom/xiaomi/fitness/eco/device/manager/ui/EcoDeviceDetail;)V", "Lcom/xiaomi/fitness/eco/device/manager/export/EcoDeviceModel;", "", "did", "unbindMember", "(Lcom/xiaomi/fitness/eco/device/manager/export/EcoDeviceModel;Landroidx/fragment/app/FragmentActivity;Ljava/lang/String;)V", "unbind", "(Ljava/lang/String;)V", "unbindEarPhone", "requestPermissionForConnect", "requestPermissionAgain", "bluetoothPermissionDialog", "(Z)V", "showPairGuideConfirmDialog", "(Lcom/xiaomi/fitness/eco/device/manager/ui/EcoDeviceDetail;)V", "", "getBloodSugar", "()I", "Landroid/view/View;", "view", "Landroid/os/Bundle;", "savedInstanceState", "onViewCreated", "(Landroid/view/View;Landroid/os/Bundle;)V", LifecycleConstantKt.ON_DESTROY, LifecycleConstantKt.ON_RESUME, "type", "tabName", "(I)Ljava/lang/String;", "deviceCount", "index", "Lcom/xiaomi/fitness/eco/device/manager/export/EcoDeviceManager;", "Lcom/xiaomi/fitness/baseui/dialog/CommonDialog;", "mLostDeviceDialog", "Lcom/xiaomi/fitness/baseui/dialog/CommonDialog;", "mRebindDialog", "mRebindRetryCount", "I", "Lkotlin/Function1;", "Lcom/xiaomi/fitness/device/manager/DeviceModel;", "Lkotlin/ParameterName;", "name", "rnDeviceChangeListener", "Lkotlin/jvm/functions/Function1;", "com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$onEcoDeviceStatusListener$1", "onEcoDeviceStatusListener", "Lcom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$onEcoDeviceStatusListener$1;", "Companion", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nOtherDeviceTabFragmentV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OtherDeviceTabFragmentV4.kt\ncom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 GsonExt.kt\ncom/xiaomi/fitness/common/extensions/GsonExtKt\n*L\n1#1,670:1\n774#2:671\n865#2,2:672\n774#2:674\n865#2,2:675\n29#3:677\n*S KotlinDebug\n*F\n+ 1 OtherDeviceTabFragmentV4.kt\ncom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4\n*L\n644#1:671\n644#1:672,2\n651#1:674\n651#1:675,2\n415#1:677\n*E\n"})
public final class OtherDeviceTabFragmentV4 extends Hilt_OtherDeviceTabFragmentV4<OtherDeviceTabV4ViewModel, lm8> implements DeviceTabInfoProvider {

    @NotNull
    public static final String TAG = "OtherDeviceTabFragmentV4";

    @NotNull
    private final EcoDeviceManager mEcoDeviceManager;

    @Nullable
    private CommonDialog<?> mLostDeviceDialog;

    @Nullable
    private CommonDialog<?> mRebindDialog;
    private int mRebindRetryCount;

    @NotNull
    private final OtherDeviceTabFragmentV4$onEcoDeviceStatusListener$1 onEcoDeviceStatusListener;

    @NotNull
    private final Function1<DeviceModel<?>, Unit> rnDeviceChangeListener;

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$bluetoothPermissionDialog$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$bluetoothPermissionDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    @SourceDebugExtension({"SMAP\nOtherDeviceTabFragmentV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OtherDeviceTabFragmentV4.kt\ncom/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$bluetoothPermissionDialog$1\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,670:1\n1#2:671\n*E\n"})
    public static final class AnonymousClass1 extends sj7 {
        final /* synthetic */ boolean $requestPermissionAgain;
        final /* synthetic */ OtherDeviceTabFragmentV4 this$0;

        public AnonymousClass1(boolean z, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
            this.$requestPermissionAgain = z;
            this.this$0 = otherDeviceTabFragmentV4;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            if (which == -1) {
                if (this.$requestPermissionAgain) {
                    OtherDeviceTabFragmentV4.access$requestPermissionForConnect(this.this$0);
                } else {
                    FragmentActivity activity = this.this$0.getActivity();
                    if (activity != null) {
                        SystemSetting.INSTANCE.goToSetting(activity);
                    }
                }
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$doUnbind$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\"\u0010\n\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u0016¨\u0006\f"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$doUnbind$1", "Lcom/xiaomi/fitness/eco/device/contact/export/OnEcoSyncCallback;", "onSuccess", "", "did", "", "type", "", "result", "Lcom/xiaomi/fitness/eco/device/contact/export/EcoSyncResult;", "onError", "code", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C25241 implements OnEcoSyncCallback {
        final /* synthetic */ FragmentActivity $activity;
        final /* synthetic */ String $deviceId;
        final /* synthetic */ OtherDeviceTabFragmentV4 this$0;

        public C25241(String str, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, FragmentActivity fragmentActivity) {
            this.$deviceId = str;
            this.this$0 = otherDeviceTabFragmentV4;
            this.$activity = fragmentActivity;
        }

        @Override // com.xiaomi.fitness.eco.device.contact.export.OnEcoSyncCallback
        public void onError(String did, int type, int code) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "unbind error type:" + type + ",code" + code);
            this.this$0.dismissDialog();
            FragmentActivity fragmentActivity = this.$activity;
            if (fragmentActivity != null) {
                String string = this.this$0.getString(lmk.F0);
                Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                ToastExtKt.toastShort(fragmentActivity, string);
            }
        }

        @Override // com.xiaomi.fitness.eco.device.contact.export.OnEcoSyncCallback
        public void onSuccess(String did, int type, EcoSyncResult result) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "unbind success" + (result != null ? result.getPacket() : null));
            EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).disconnectDevice(this.$deviceId);
            OtherDeviceTabFragmentV4.access$unbind(this.this$0, this.$deviceId);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$lostDevice$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$lostDevice$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C25251 extends sj7 {
        final /* synthetic */ WearableDeviceModel $deviceModel;

        public C25251(WearableDeviceModel wearableDeviceModel) {
            this.$deviceModel = wearableDeviceModel;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            super.onDialogClick(dialogName, dialog, which);
            if (dialog != null) {
                dialog.dismiss();
            }
            OtherDeviceTabFragmentV4.access$setMLostDeviceDialog$p(OtherDeviceTabFragmentV4.this, null);
            if (which == -2) {
                OtherDeviceTabFragmentV4.access$unbindEarPhone(OtherDeviceTabFragmentV4.this, this.$deviceModel.getDid());
            } else {
                if (which != -1) {
                    return;
                }
                OtherDeviceTabFragmentV4.access$setMRebindRetryCount$p(OtherDeviceTabFragmentV4.this, 0);
                OtherDeviceTabFragmentV4.access$rebindDevice(OtherDeviceTabFragmentV4.this, this.$deviceModel);
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$rebindDevice$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000/\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006J\u0017\u0010\t\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\u0004H\u0016¢\u0006\u0004\b\u000b\u0010\fJ\u0017\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\r\u0010\nJ\u0019\u0010\u0010\u001a\u00020\u00042\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0016¢\u0006\u0004\b\u0010\u0010\u0011J\u0017\u0010\u0012\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\u0012\u0010\nJ#\u0010\u0016\u001a\u00020\u00042\b\u0010\u0013\u001a\u0004\u0018\u00010\u00022\b\u0010\u0015\u001a\u0004\u0018\u00010\u0014H\u0016¢\u0006\u0004\b\u0016\u0010\u0017J\u000f\u0010\u0018\u001a\u00020\u0004H\u0016¢\u0006\u0004\b\u0018\u0010\fJ\u000f\u0010\u0019\u001a\u00020\u0004H\u0016¢\u0006\u0004\b\u0019\u0010\fJ\u000f\u0010\u001a\u001a\u00020\u0004H\u0016¢\u0006\u0004\b\u001a\u0010\f¨\u0006\u001b"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$rebindDevice$1", "Lcom/xiaomi/fitness/device/manager/a$a;", "", "did", "", "onBindSuccess", "(Ljava/lang/String;)V", "", "status", "onBindFailure", "(I)V", "onConnectSuccess", "()V", "onConnectFailure", "", "key", "onPairingSuccess", "([B)V", "onPairingFailure", "code", "Lcom/xiaomi/fitness/device/manager/e;", "callback", "showPairingCode", "(Ljava/lang/String;Lcom/xiaomi/fitness/device/manager/e;)V", "dismissPairingCode", "onRequestDeviceSuccess", "onRequestDeviceFailure", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class BinderC25261 extends a.AbstractBinderC0459a {
        final /* synthetic */ WearableDeviceModel $deviceModel;

        public BinderC25261(WearableDeviceModel wearableDeviceModel) {
            this.$deviceModel = wearableDeviceModel;
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void dismissPairingCode() {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "dismissPairingCode");
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onBindFailure(int status) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onBindFailure");
            OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4 = OtherDeviceTabFragmentV4.this;
            OtherDeviceTabFragmentV4.access$showRebindFail(otherDeviceTabFragmentV4, this.$deviceModel, OtherDeviceTabFragmentV4.access$getMRebindRetryCount$p(otherDeviceTabFragmentV4) < 5);
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onBindSuccess(String did) {
            Context context = OtherDeviceTabFragmentV4.this.getContext();
            if (context != null) {
                ToastExtKt.toastShort(context, lmk.o);
            }
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onConnectFailure(int status) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onConnectFailure");
            OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4 = OtherDeviceTabFragmentV4.this;
            OtherDeviceTabFragmentV4.access$showRebindFail(otherDeviceTabFragmentV4, this.$deviceModel, OtherDeviceTabFragmentV4.access$getMRebindRetryCount$p(otherDeviceTabFragmentV4) < 5);
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onConnectSuccess() {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onConnectSuccess");
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onPairingFailure(int status) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onPairingFailure");
            OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4 = OtherDeviceTabFragmentV4.this;
            OtherDeviceTabFragmentV4.access$showRebindFail(otherDeviceTabFragmentV4, this.$deviceModel, OtherDeviceTabFragmentV4.access$getMRebindRetryCount$p(otherDeviceTabFragmentV4) < 5);
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onPairingSuccess(byte[] key) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onPairingSuccess");
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onRequestDeviceFailure() {
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void onRequestDeviceSuccess() {
        }

        @Override // com.xiaomi.fitness.device.manager.a
        public void showPairingCode(String code, e callback) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "showPairingCode code:" + code);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$showPairGuideConfirmDialog$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$showPairGuideConfirmDialog$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C25271 extends sj7 {
        final /* synthetic */ EcoDeviceDetail $deviceDetail;

        public C25271(EcoDeviceDetail ecoDeviceDetail) {
            this.$deviceDetail = ecoDeviceDetail;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            Context context;
            if (dialog != null) {
                dialog.dismiss();
            }
            if (which == -1 && (context = OtherDeviceTabFragmentV4.this.getContext()) != null) {
                String mac = this.$deviceDetail.getMac();
                String name = this.$deviceDetail.getName();
                String shareId = this.$deviceDetail.getShareId();
                Intrinsics.checkNotNull(shareId);
                String userId = this.$deviceDetail.getUserId();
                Intrinsics.checkNotNull(userId);
                ElectronicScaleManagerExtKt.launchToElectronicPairGuideActivity(context, mac, name, shareId, userId, this.$deviceDetail.getDid(), this.$deviceDetail.getModel());
            }
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$showRebindFail$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J+\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$showRebindFail$1", "Lsj7;", "", "dialogName", "Landroid/content/DialogInterface;", "dialog", "", "which", "", "onDialogClick", "(Ljava/lang/String;Landroid/content/DialogInterface;I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C25281 extends sj7 {
        final /* synthetic */ WearableDeviceModel $deviceModel;

        public C25281(WearableDeviceModel wearableDeviceModel) {
            this.$deviceModel = wearableDeviceModel;
        }

        @Override // defpackage.sj7
        public void onDialogClick(String dialogName, DialogInterface dialog, int which) {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (which != -1) {
                DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).disconnectDevice(this.$deviceModel.getDid());
                return;
            }
            OtherDeviceTabFragmentV4.access$setMRebindRetryCount$p(OtherDeviceTabFragmentV4.this, OtherDeviceTabFragmentV4.access$getMRebindRetryCount$p(OtherDeviceTabFragmentV4.this) + 1);
            OtherDeviceTabFragmentV4.access$rebindDevice(OtherDeviceTabFragmentV4.this, this.$deviceModel);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$unbind$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004J\u0017\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$unbind$1", "Lcom/xiaomi/fitness/device/manager/l$a;", "", "onSuccess", "()V", "", "code", "onFail", "(I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class BinderC25291 extends l.a {
        public BinderC25291() {
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onFail(int code) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onFail()");
            AnyExtKt.main$default(null, new OtherDeviceTabFragmentV4$unbind$1$onFail$1(OtherDeviceTabFragmentV4.this, null), 1, null);
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onSuccess() {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "onSuccess()");
            AnyExtKt.main$default(null, new OtherDeviceTabFragmentV4$unbind$1$onSuccess$1(OtherDeviceTabFragmentV4.this, null), 1, null);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$unbindEarPhone$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004J\u0017\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$unbindEarPhone$1", "Lcom/xiaomi/fitness/device/manager/l$a;", "", "onSuccess", "()V", "", "code", "onFail", "(I)V", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class BinderC25301 extends l.a {
        public BinderC25301() {
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onFail(int code) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "unbindEarPhone onFail");
            AnyExtKt.main$default(null, new OtherDeviceTabFragmentV4$unbindEarPhone$1$onFail$1(OtherDeviceTabFragmentV4.this, null), 1, null);
        }

        @Override // com.xiaomi.fitness.device.manager.l
        public void onSuccess() {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "unbindEarPhone onSuccess");
            AnyExtKt.main$default(null, new OtherDeviceTabFragmentV4$unbindEarPhone$1$onSuccess$1(OtherDeviceTabFragmentV4.this, null), 1, null);
        }
    }

    /* JADX INFO: renamed from: com.xiaomi.fitness.eco.device.manager.ui.tab.OtherDeviceTabFragmentV4$unbindMember$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\"\u0010\n\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u0016¨\u0006\f"}, d2 = {"com/xiaomi/fitness/eco/device/manager/ui/tab/OtherDeviceTabFragmentV4$unbindMember$1", "Lcom/xiaomi/fitness/eco/device/contact/export/OnEcoSyncCallback;", "onSuccess", "", "did", "", "type", "", "result", "Lcom/xiaomi/fitness/eco/device/contact/export/EcoSyncResult;", "onError", "code", "eco-device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class C25311 implements OnEcoSyncCallback {
        final /* synthetic */ FragmentActivity $activity;

        public C25311(FragmentActivity fragmentActivity) {
            this.$activity = fragmentActivity;
        }

        @Override // com.xiaomi.fitness.eco.device.contact.export.OnEcoSyncCallback
        public void onError(String did, int type, int code) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "ScaleCmdHelper.delSharedMember error type:" + type + ",code" + code);
            OtherDeviceTabFragmentV4.this.dismissDialog();
            FragmentActivity fragmentActivity = this.$activity;
            String string = fragmentActivity.getString(lmk.F0);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            ToastExtKt.toastShort(fragmentActivity, string);
        }

        @Override // com.xiaomi.fitness.eco.device.contact.export.OnEcoSyncCallback
        public void onSuccess(String did, int type, EcoSyncResult result) {
            Logger.i(OtherDeviceTabFragmentV4.TAG, "ScaleCmdHelper.delSharedMember success" + (result != null ? result.getPacket() : null));
            EcoDeviceManager ecoDeviceManagerExtKt = EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE);
            Intrinsics.checkNotNull(did);
            ecoDeviceManagerExtKt.disconnectDevice(did);
            OtherDeviceTabFragmentV4.access$unbind(OtherDeviceTabFragmentV4.this, did);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OtherDeviceTabFragmentV4(@NotNull EcoDeviceManager mEcoDeviceManager) {
        super(jgk.l, u02.d);
        Intrinsics.checkNotNullParameter(mEcoDeviceManager, "mEcoDeviceManager");
        this.mEcoDeviceManager = mEcoDeviceManager;
        this.rnDeviceChangeListener = new rwi(this);
        this.onEcoDeviceStatusListener = new OtherDeviceTabFragmentV4$onEcoDeviceStatusListener$1(this);
    }

    public static /* synthetic */ Unit B(FragmentActivity fragmentActivity, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, boolean z) {
        return requestPermissionForConnect$lambda$19$lambda$18(fragmentActivity, otherDeviceTabFragmentV4, z);
    }

    public static /* synthetic */ boolean C(WearableDeviceModel wearableDeviceModel) {
        return getBloodSugar$lambda$20(wearableDeviceModel);
    }

    public static /* synthetic */ Unit D(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, ConnectStatus connectStatus) {
        return onViewCreated$lambda$10(otherDeviceTabFragmentV4, connectStatus);
    }

    public static /* synthetic */ Unit E(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        return onViewCreated$lambda$7$lambda$6$lambda$3$lambda$1(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity);
    }

    public static /* synthetic */ Unit F(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        return onViewCreated$lambda$7$lambda$6$lambda$3$lambda$2(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity);
    }

    public static /* synthetic */ Unit G(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, DeviceModel deviceModel) {
        return rnDeviceChangeListener$lambda$13(otherDeviceTabFragmentV4, deviceModel);
    }

    public static /* synthetic */ Unit H(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
        return requestPermissionForConnect$lambda$19$lambda$18$lambda$17$lambda$15(otherDeviceTabFragmentV4);
    }

    public static /* synthetic */ Unit I(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, int i, View view) {
        return onViewCreated$lambda$0(otherDeviceTabFragmentV4, ecoDeviceDetail, i, view);
    }

    public static /* synthetic */ Unit J(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        return onViewCreated$lambda$7$lambda$6$lambda$5(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity);
    }

    public static /* synthetic */ boolean K(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, int i, View view) {
        return onViewCreated$lambda$7(otherDeviceTabFragmentV4, ecoDeviceDetail, i, view);
    }

    public static /* synthetic */ Unit L(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, FragmentActivity fragmentActivity, ApiException apiException) {
        return delShare$lambda$12(otherDeviceTabFragmentV4, fragmentActivity, apiException);
    }

    public static /* synthetic */ Unit M(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, boolean z) {
        return onViewCreated$lambda$7$lambda$6$lambda$4(otherDeviceTabFragmentV4, z);
    }

    public static /* synthetic */ Unit N(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, List list) {
        return requestPermissionForConnect$lambda$19$lambda$18$lambda$14(otherDeviceTabFragmentV4, list);
    }

    public static /* synthetic */ Unit O(EcoDeviceDetail ecoDeviceDetail, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, FragmentActivity fragmentActivity) {
        return onViewCreated$lambda$7$lambda$6$lambda$3(ecoDeviceDetail, otherDeviceTabFragmentV4, fragmentActivity);
    }

    public static /* synthetic */ Unit P(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, int i, View view) {
        return onViewCreated$lambda$8(otherDeviceTabFragmentV4, ecoDeviceDetail, i, view);
    }

    public static /* synthetic */ Unit Q(List list) {
        return requestPermissionForConnect$lambda$19$lambda$18$lambda$17$lambda$16(list);
    }

    public static /* synthetic */ Unit R(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity, ScaleData scaleData) {
        return delShare$lambda$11(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity, scaleData);
    }

    public static final /* synthetic */ int access$getMRebindRetryCount$p(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
        return otherDeviceTabFragmentV4.mRebindRetryCount;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final /* synthetic */ OtherDeviceTabV4ViewModel access$getMViewModel(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
        return (OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel();
    }

    public static final /* synthetic */ void access$rebindDevice(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, WearableDeviceModel wearableDeviceModel) {
        otherDeviceTabFragmentV4.rebindDevice(wearableDeviceModel);
    }

    public static final /* synthetic */ void access$requestPermissionForConnect(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
        otherDeviceTabFragmentV4.requestPermissionForConnect();
    }

    public static final /* synthetic */ void access$setMLostDeviceDialog$p(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, CommonDialog commonDialog) {
        otherDeviceTabFragmentV4.mLostDeviceDialog = commonDialog;
    }

    public static final /* synthetic */ void access$setMRebindRetryCount$p(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, int i) {
        otherDeviceTabFragmentV4.mRebindRetryCount = i;
    }

    public static final /* synthetic */ void access$showRebindFail(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, WearableDeviceModel wearableDeviceModel, boolean z) {
        otherDeviceTabFragmentV4.showRebindFail(wearableDeviceModel, z);
    }

    public static final /* synthetic */ void access$unbind(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, String str) {
        otherDeviceTabFragmentV4.unbind(str);
    }

    public static final /* synthetic */ void access$unbindEarPhone(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, String str) {
        otherDeviceTabFragmentV4.unbindEarPhone(str);
    }

    private final void bluetoothPermissionDialog(boolean requestPermissionAgain) {
        CommonDialog commonDialogCreate = new CommonDialog.c("location").setDialogTitle(lmk.R).setDialogDescription(Build.VERSION.SDK_INT >= 31 ? lmk.U : lmk.V).setPositiveText(lmk.L0).setNegativeText(lmk.common_cancel).create();
        commonDialogCreate.setCancelable(false);
        commonDialogCreate.addDialogCallback(new AnonymousClass1(requestPermissionAgain, this));
        FragmentActivity activity = getActivity();
        commonDialogCreate.showIfNeed(activity != null ? activity.getSupportFragmentManager() : null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void clearLiveData() {
        ((OtherDeviceTabV4ViewModel) getMViewModel()).setBleReconnect(new MutableLiveData<>());
    }

    private final void delShare(FragmentActivity activity, EcoDeviceDetail deviceDetail) {
        PageState.showLoading$default(this, lmk.D0, false, 2, null);
        ElectronicScaleRequest electronicScaleRequestExtKt = ElectronicScaleRequestExtKt.getInstance(ElectronicScaleRequest.INSTANCE);
        String userId = deviceDetail.getUserId();
        Intrinsics.checkNotNull(userId);
        String shareUserId = deviceDetail.getShareUserId();
        Intrinsics.checkNotNull(shareUserId);
        String shareId = deviceDetail.getShareId();
        Intrinsics.checkNotNull(shareId);
        ElectronicScaleRequest.delShare$default(electronicScaleRequestExtKt, userId, shareUserId, shareId, null, new nwi(this, deviceDetail, activity), new owi(this, activity), 8, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit delShare$lambda$11(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity, ScaleData it) {
        Intrinsics.checkNotNullParameter(it, "it");
        otherDeviceTabFragmentV4.dismissDialog();
        if (((DelShareResponse) new Gson().fromJson(it.getResp(), new OtherDeviceTabFragmentV4$delShare$lambda$11$$inlined$fromJson$1().getType())).getCode() == 200) {
            ((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel()).removeItem(ecoDeviceDetail.getDid());
        } else {
            otherDeviceTabFragmentV4.dismissDialog();
            String string = fragmentActivity.getString(lmk.F0);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            ToastExtKt.toastShort(fragmentActivity, string);
        }
        return Unit.INSTANCE;
    }

    private static final Unit delShare$lambda$12(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, FragmentActivity fragmentActivity, ApiException it) {
        Intrinsics.checkNotNullParameter(it, "it");
        otherDeviceTabFragmentV4.dismissDialog();
        String string = fragmentActivity.getString(lmk.F0);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        ToastExtKt.toastShort(fragmentActivity, string);
        return Unit.INSTANCE;
    }

    private final void doUnbind(EcoDeviceDetail it, FragmentActivity activity, EcoDeviceDetail deviceDetail) {
        EcoDeviceInfo deviceInfo;
        if (deviceDetail.getDeviceType() == 4) {
            Logger.i(TAG, "doUnbind deviceType == 4");
            unbindEarPhone(deviceDetail.getDid());
            return;
        }
        if (!NetworkExtKt.isNetworkAvailable()) {
            if (activity != null) {
                String string = getResources().getString(lmk.common_hint_network_unavailable);
                Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
                ToastExtKt.toastShort(activity, string);
                return;
            }
            return;
        }
        String did = it.getDid();
        EcoDeviceModel ecoDeviceModel = (EcoDeviceModel) EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).getDeviceModel(did);
        if (deviceDetail.getIsPendingPairing()) {
            delShare(activity, deviceDetail);
            return;
        }
        if (ecoDeviceModel != null && (deviceInfo = ecoDeviceModel.getDeviceInfo()) != null && !deviceInfo.isOwner()) {
            unbindMember(ecoDeviceModel, activity, did);
        } else if (ecoDeviceModel == null || !ecoDeviceModel.isDeviceConnected()) {
            unbind(did);
        } else {
            ElectronicScaleCMDHelperExtKt.getInstance(ElectronicScaleCMDHelper.INSTANCE).restoreFactory(did, new C25241(did, this, activity));
        }
    }

    private final int getBloodSugar() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).findDeviceModel(new swi());
        return (wearableDeviceModel == null || !wearableDeviceModel.isDeviceConnected()) ? 0 : 1;
    }

    private static final boolean getBloodSugar$lambda$20(WearableDeviceModel it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return BloodSugarExtKt.isBloodSugar(it);
    }

    private final void lostDevice(WearableDeviceModel deviceModel) {
        CommonDialog<?> commonDialog = this.mLostDeviceDialog;
        if (commonDialog == null || !commonDialog.isShowing()) {
            CommonDialog<?> commonDialogCreate = new CommonDialog.c("lostEarPhone").setDialogDescriptionString(new DialogParams.DialogDescriptionString(lmk.m, DeviceModelExtKt.getName(deviceModel))).setNegativeText(lmk.d0).setPositiveText(lmk.l).setCancelable(false).setCanceledOnBackPressed(false).create();
            this.mLostDeviceDialog = commonDialogCreate;
            Intrinsics.checkNotNull(commonDialogCreate);
            commonDialogCreate.addDialogCallback(new C25251(deviceModel)).showIfNeed(getChildFragmentManager());
        }
    }

    private static final Unit onViewCreated$lambda$0(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail deviceDetail, int i, View view) {
        Intrinsics.checkNotNullParameter(deviceDetail, "deviceDetail");
        Intrinsics.checkNotNullParameter(view, "<unused var>");
        if (deviceDetail.getIsEco()) {
            if (deviceDetail.getIsPendingPairing()) {
                otherDeviceTabFragmentV4.showPairGuideConfirmDialog(deviceDetail);
                return Unit.INSTANCE;
            }
            Application applicationContext = XMPassportSettings.getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
            ElectronicScaleManagerExtKt.launchToElectronicScaleMainActivity$default(applicationContext, deviceDetail.getDid(), deviceDetail.getMac(), false, 4, null);
        } else if (deviceDetail.getDeviceType() == 2) {
            BloodSugarManager bloodSugarExtKt = BloodSugarExtKt.getInstance(BloodSugarManager.INSTANCE);
            FragmentActivity fragmentActivityRequireActivity = otherDeviceTabFragmentV4.requireActivity();
            Intrinsics.checkNotNullExpressionValue(fragmentActivityRequireActivity, "requireActivity(...)");
            bloodSugarExtKt.goBloodSugarDevice(fragmentActivityRequireActivity);
        } else if (deviceDetail.getDeviceType() == 3) {
            RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).openPlugin(deviceDetail.getDid());
        } else if (deviceDetail.getDeviceType() == 4) {
            Logger.i(TAG, "connectStatus == " + deviceDetail.getConnectStatus());
            if (deviceDetail.getConnectStatus() == 3 || deviceDetail.getConnectStatus() == 7 || deviceDetail.getConnectStatus() == 6) {
                Application application = ApplicationExtKt.getApplication();
                EcoViewHelper ecoViewHelper = EcoViewHelper.INSTANCE;
                if (MarketUtil.checkAppInstalled(application, ecoViewHelper.getEarPhoneAppPackage())) {
                    FragmentManager childFragmentManager = otherDeviceTabFragmentV4.getChildFragmentManager();
                    Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
                    ecoViewHelper.openEarPhone(childFragmentManager, deviceDetail);
                } else {
                    FragmentManager childFragmentManager2 = otherDeviceTabFragmentV4.getChildFragmentManager();
                    Intrinsics.checkNotNullExpressionValue(childFragmentManager2, "getChildFragmentManager(...)");
                    ecoViewHelper.openEarPhoneOnMarket(childFragmentManager2);
                }
            } else {
                ToastUtil.INSTANCE.showToast(lmk.s0);
            }
        }
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$10(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, ConnectStatus connectStatus) {
        if (otherDeviceTabFragmentV4.isInvalid()) {
            return Unit.INSTANCE;
        }
        if (connectStatus.getCode() == 2005) {
            otherDeviceTabFragmentV4.lostDevice(connectStatus.getDeviceModel());
        } else {
            Logger.i(TAG, "connectDevice bleReconnect code:" + connectStatus.getCode());
        }
        return Unit.INSTANCE;
    }

    private static final boolean onViewCreated$lambda$7(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail deviceDetail, int i, View itemView) {
        FragmentActivity activity;
        Intrinsics.checkNotNullParameter(deviceDetail, "deviceDetail");
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        if (Intrinsics.areEqual(deviceDetail.getDid(), OtherDeviceTabV4ViewModel.EMPTY_DID) || Intrinsics.areEqual(deviceDetail.getDid(), OtherDeviceTabV4ViewModel.ADD_NEW_DID) || (activity = otherDeviceTabFragmentV4.getActivity()) == null) {
            return true;
        }
        if (deviceDetail.getIsEco()) {
            EcoViewHelper.INSTANCE.showUnbindWindow(activity, itemView, deviceDetail, new iwi(deviceDetail, otherDeviceTabFragmentV4, activity));
            return true;
        }
        if (deviceDetail.getDeviceType() == 3) {
            RnSettingsExportApiExtKt.getInstance(RnSettingsExportApi.INSTANCE).showDeleteDeviceDialog(new WeakReference<>(activity), null, deviceDetail.getDid(), deviceDetail.getProductId(), new jwi(otherDeviceTabFragmentV4));
            return true;
        }
        if (deviceDetail.getDeviceType() != 4) {
            return true;
        }
        Logger.i(TAG, "setOnItemLongClickListener deviceType == 4");
        EcoViewHelper ecoViewHelper = EcoViewHelper.INSTANCE;
        FragmentManager childFragmentManager = otherDeviceTabFragmentV4.getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
        ecoViewHelper.confirmUnboundEarPhone(childFragmentManager, deviceDetail, new kwi(otherDeviceTabFragmentV4, deviceDetail, activity));
        return true;
    }

    private static final Unit onViewCreated$lambda$7$lambda$6$lambda$3(EcoDeviceDetail ecoDeviceDetail, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, FragmentActivity fragmentActivity) {
        if (ecoDeviceDetail.getIsPendingPairing()) {
            EcoViewHelper ecoViewHelper = EcoViewHelper.INSTANCE;
            FragmentManager childFragmentManager = otherDeviceTabFragmentV4.getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
            ecoViewHelper.showDelDeviceSharedDialog(childFragmentManager, new gwi(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity));
            return Unit.INSTANCE;
        }
        EcoViewHelper ecoViewHelper2 = EcoViewHelper.INSTANCE;
        FragmentManager childFragmentManager2 = otherDeviceTabFragmentV4.getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager2, "getChildFragmentManager(...)");
        ecoViewHelper2.confirmUnbound(childFragmentManager2, ecoDeviceDetail, new hwi(otherDeviceTabFragmentV4, ecoDeviceDetail, fragmentActivity));
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$7$lambda$6$lambda$3$lambda$1(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        otherDeviceTabFragmentV4.doUnbind(ecoDeviceDetail, fragmentActivity, ecoDeviceDetail);
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$7$lambda$6$lambda$3$lambda$2(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        otherDeviceTabFragmentV4.doUnbind(ecoDeviceDetail, fragmentActivity, ecoDeviceDetail);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit onViewCreated$lambda$7$lambda$6$lambda$4(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, boolean z) {
        if (z) {
            ((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel()).updateDeviceItems();
        }
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$7$lambda$6$lambda$5(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail ecoDeviceDetail, FragmentActivity fragmentActivity) {
        otherDeviceTabFragmentV4.doUnbind(ecoDeviceDetail, fragmentActivity, ecoDeviceDetail);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0070  */
    /* JADX WARN: Code duplicated, block: B:16:0x0081  */
    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit onViewCreated$lambda$8(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, EcoDeviceDetail tabContentItem, int i, View itemView) {
        Intrinsics.checkNotNullParameter(tabContentItem, "tabContentItem");
        Intrinsics.checkNotNullParameter(itemView, "itemView");
        Logger.i(TAG, "setOnChildClickListener, tabContentItem:" + tabContentItem + ", position:" + i + ", itemView:" + itemView);
        if (itemView.getId() == yck.u) {
            Logger.i(TAG, "click device,did is " + tabContentItem.getDid() + ",status is " + tabContentItem.getConnectStatus());
            int connectStatus = tabContentItem.getConnectStatus();
            if (connectStatus == 1) {
                ((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel()).preConnectThirdPartyDeviceByDid(tabContentItem.getDid());
            } else if (connectStatus == 7 || connectStatus == 3) {
                OtherDeviceTabV4ViewModel.syncWearDevice$default((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel(), tabContentItem.getDid(), false, 2, null);
            } else if (connectStatus == 4) {
                ((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel()).preConnectThirdPartyDeviceByDid(tabContentItem.getDid());
            } else if (connectStatus == 5) {
                OtherDeviceTabV4ViewModel.syncWearDevice$default((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel(), tabContentItem.getDid(), false, 2, null);
            }
        }
        return Unit.INSTANCE;
    }

    private static final Unit onViewCreated$lambda$9(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, Boolean bool) {
        if (bool.booleanValue()) {
            otherDeviceTabFragmentV4.requestPermissionForConnect();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void rebindDevice(WearableDeviceModel deviceModel) {
        Logger.i(TAG, "rebindDevice");
        ((OtherDeviceTabV4ViewModel) getMViewModel()).bindDevice(deviceModel.getDeviceInfo(), new BinderC25261(deviceModel));
    }

    private final void requestPermissionForConnect() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            BluetoothPermissionUtil.INSTANCE.showBluetoothSyncDataDialogIfNeed(activity, new mwi(activity, this));
        }
    }

    private static final Unit requestPermissionForConnect$lambda$19$lambda$18(FragmentActivity fragmentActivity, OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, boolean z) {
        if (z) {
            DeviceManagerExtKt.getInstance(BluetoothPermissionTool.INSTANCE).requestBluetoothIfNeed(fragmentActivity, false, new bwi(otherDeviceTabFragmentV4), new lwi(otherDeviceTabFragmentV4));
        }
        return Unit.INSTANCE;
    }

    private static final Unit requestPermissionForConnect$lambda$19$lambda$18$lambda$14(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        EcoDevicePreference.INSTANCE.setLAST_REQUEST_PERMISSION_TIME(System.currentTimeMillis());
        otherDeviceTabFragmentV4.bluetoothPermissionDialog(false);
        return Unit.INSTANCE;
    }

    private static final Unit requestPermissionForConnect$lambda$19$lambda$18$lambda$17(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, MultiplePermissionsBuilder requestBluetoothIfNeed) {
        Intrinsics.checkNotNullParameter(requestBluetoothIfNeed, "$this$requestBluetoothIfNeed");
        requestBluetoothIfNeed.allGranted(new pwi(otherDeviceTabFragmentV4));
        requestBluetoothIfNeed.denied(new qwi());
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit requestPermissionForConnect$lambda$19$lambda$18$lambda$17$lambda$15(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4) {
        ((OtherDeviceTabV4ViewModel) otherDeviceTabFragmentV4.getMViewModel()).updateDeviceItems();
        return Unit.INSTANCE;
    }

    private static final Unit requestPermissionForConnect$lambda$19$lambda$18$lambda$17$lambda$16(List it) {
        Intrinsics.checkNotNullParameter(it, "it");
        EcoDevicePreference.INSTANCE.setLAST_REQUEST_PERMISSION_TIME(System.currentTimeMillis());
        return Unit.INSTANCE;
    }

    private static final Unit rnDeviceChangeListener$lambda$13(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, DeviceModel deviceModel) {
        Logger.i(TAG, "onRNDeviceChange " + deviceModel);
        AnyExtKt.main$default(null, new OtherDeviceTabFragmentV4$rnDeviceChangeListener$1$1(otherDeviceTabFragmentV4, null), 1, null);
        return Unit.INSTANCE;
    }

    private final void showPairGuideConfirmDialog(EcoDeviceDetail deviceDetail) {
        View viewInflate = LayoutInflater.from(getContext()).inflate(jgk.f9823k, (ViewGroup) null);
        ElectronicScaleManager electronicScaleManagerExtKt = ElectronicScaleManagerExtKt.getInstance(ElectronicScaleManager.INSTANCE);
        View viewFindViewById = viewInflate.findViewById(yck.m);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        electronicScaleManagerExtKt.loadImageFromNet((ImageView) viewFindViewById, ElectronicScaleManagerExtKt.IMAGE_ELECTRONIC_SCALE_PAIR_GUIDE_1, ElectronicScaleManagerExtKt.IMAGE_ELECTRONIC_SCALE_PAIR_GUIDE_1_NIGHT);
        CommonDialog commonDialogCreate = new CommonDialog.c("pair_guide_dialog").setDialogDescription(lmk.G0).setPositiveText(lmk.z).setDialogTitle(lmk.A0).setNegativeText(lmk.i).setCustomView(viewInflate).create();
        commonDialogCreate.addDialogCallback(new C25271(deviceDetail));
        FragmentActivity activity = getActivity();
        commonDialogCreate.showIfNeed(activity != null ? activity.getSupportFragmentManager() : null);
    }

    private final void showRebindFail(WearableDeviceModel deviceModel, boolean retry) {
        if (isInvalid()) {
            return;
        }
        CommonDialog<?> commonDialog = this.mRebindDialog;
        if (commonDialog != null && commonDialog.isShowing()) {
            CommonDialog<?> commonDialog2 = this.mRebindDialog;
            Intrinsics.checkNotNull(commonDialog2);
            commonDialog2.dismiss();
        }
        CommonDialog.c cVar = new CommonDialog.c("RebindEarPhoneFail");
        if (retry) {
            cVar.setDialogTitle(lmk.c0).setDialogDescription(lmk.b0).setNegativeText(lmk.i).setPositiveText(lmk.e0);
        } else {
            cVar.setDialogDescription(lmk.a0).setNeutralText(lmk.common_known);
        }
        CommonDialog<?> commonDialogCreate = cVar.create();
        this.mRebindDialog = commonDialogCreate;
        Intrinsics.checkNotNull(commonDialogCreate);
        commonDialogCreate.addDialogCallback(new C25281(deviceModel)).showIfNeed(getChildFragmentManager());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void unbind(String did) {
        ((OtherDeviceTabV4ViewModel) getMViewModel()).unbindDevice(did, new BinderC25291());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void unbindEarPhone(String did) {
        Logger.i(TAG, "unbindEarPhone");
        ((OtherDeviceTabV4ViewModel) getMViewModel()).unbindEarPhoneDevice(did, new BinderC25301());
    }

    private final void unbindMember(EcoDeviceModel deviceModel, FragmentActivity activity, String did) {
        if (deviceModel == null || deviceModel.isDeviceConnected()) {
            PageState.showLoading$default(this, lmk.D0, false, 2, null);
            ElectronicScaleCMDHelperExtKt.getInstance(ElectronicScaleCMDHelper.INSTANCE).delSharedMember(Long.parseLong(AccountManagerExtKt.getInstance(UserInfoManager.INSTANCE).getUserId()), did, new C25311(activity));
        } else {
            EcoDialogHelper ecoShareDialogExtKt = EcoShareDialogExtKt.getInstance(EcoDialogHelper.INSTANCE);
            Intrinsics.checkNotNull(activity, "null cannot be cast to non-null type com.xiaomi.fitness.baseui.view.BaseActivity");
            ecoShareDialogExtKt.showConnectDialogForUnbindMember((BaseActivity) activity);
        }
    }

    public static /* synthetic */ Unit w(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, MultiplePermissionsBuilder multiplePermissionsBuilder) {
        return requestPermissionForConnect$lambda$19$lambda$18$lambda$17(otherDeviceTabFragmentV4, multiplePermissionsBuilder);
    }

    public static /* synthetic */ Unit x(OtherDeviceTabFragmentV4 otherDeviceTabFragmentV4, Boolean bool) {
        return onViewCreated$lambda$9(otherDeviceTabFragmentV4, bool);
    }

    @Override // com.xiaomi.fitness.device.manager.export.DeviceTabInfoProvider
    public int deviceCount() {
        int size;
        int size2 = 0;
        try {
            Iterable deviceModels = RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).getDeviceModels();
            ArrayList arrayList = new ArrayList();
            for (Object obj : deviceModels) {
                if (ProductExtKt.getInstance(RNProductManager.INSTANCE).getProductByModel(DeviceModelExtKt.getModel((RNDeviceModel) obj)) != null) {
                    arrayList.add(obj);
                }
            }
            size = arrayList.size();
        } catch (Exception e) {
            Logger.e(TAG, "deviceCount error", e);
            size = 0;
        }
        try {
            Iterable localDeviceModels = DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getLocalDeviceModels();
            ArrayList arrayList2 = new ArrayList();
            for (Object obj2 : localDeviceModels) {
                if (com.xiaomi.fitness.device.manager.export.DeviceModelExtKt.isEarPhone((WearableDeviceModel) obj2)) {
                    arrayList2.add(obj2);
                }
            }
            size2 = arrayList2.size();
        } catch (Exception e2) {
            Logger.e(TAG, "localDeviceCount error", e2);
        }
        Logger.i(TAG, "miJiaDeviceSize:" + size + ",shouldShowLocalDeviceSize:" + size2);
        return this.mEcoDeviceManager.getEcoDeviceSize() + size + size2 + getBloodSugar();
    }

    @Override // com.xiaomi.fitness.device.manager.export.DeviceTabInfoProvider
    public int index() {
        return 4;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).removeDeviceChangeListener(this.rnDeviceChangeListener);
        EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).removeDeviceStatusListener(this.onEcoDeviceStatusListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        OtherDeviceTabV4ViewModel.updateDeviceList$default((OtherDeviceTabV4ViewModel) getMViewModel(), false, 1, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        setActionBarDisplayable(false);
        RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).addDeviceChangeListener(this.rnDeviceChangeListener);
        EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).addDeviceStatusListener(this.onEcoDeviceStatusListener);
        ((OtherDeviceTabV4ViewModel) getMViewModel()).getAdapter().setOnItemClickListener(new twi(this));
        ((OtherDeviceTabV4ViewModel) getMViewModel()).getAdapter().setOnItemLongClickListener(new cwi(this));
        ((OtherDeviceTabV4ViewModel) getMViewModel()).getAdapter().setOnChildClickListener(new dwi(this));
        clearLiveData();
        ((OtherDeviceTabV4ViewModel) getMViewModel()).getShowConnectPermission().observe(getViewLifecycleOwner(), new OtherDeviceTabFragmentV4$sam$androidx_lifecycle_Observer$0(new ewi(this)));
        ((OtherDeviceTabV4ViewModel) getMViewModel()).getBleReconnect().observe(getViewLifecycleOwner(), new OtherDeviceTabFragmentV4$sam$androidx_lifecycle_Observer$0(new fwi(this)));
    }

    @Override // com.xiaomi.fitness.device.manager.export.DeviceTabInfoProvider
    @NotNull
    public String tabName(int type) {
        String string = ApplicationExtKt.getApplication().getString(lmk.S);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        return string;
    }
}
package com.xiaomi.fitness.device.manager.ui.tab;

import com.xiaomi.fitness.baseui.BaseViewModel;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.eco.device.manager.export.OtherDeviceLoaderManager;
import com.xiaomi.fitness.eco.device.manager.export.OtherDeviceLoaderManagerExtKt;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\u0006\u0010\f\u001a\u00020\rR\u0014\u0010\u0005\u001a\u00020\u0006X\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u000b¨\u0006\u000e"}, d2 = {"Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentViewModel;", "Lcom/xiaomi/fitness/baseui/BaseViewModel;", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentModel;", "<init>", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "isPlayChannel", "", "()Z", "updateDevices", "", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DeviceTabParentViewModel extends BaseViewModel<DeviceTabParentModel> {

    @NotNull
    private final String TAG = "DeviceTabParentViewModel";
    private final boolean isPlayChannel = AppUtil.INSTANCE.isPlayChannel();

    @Override // com.xiaomi.fitness.baseui.BaseViewModel
    @NotNull
    public String getTAG() {
        return this.TAG;
    }

    /* JADX INFO: renamed from: isPlayChannel, reason: from getter */
    public final boolean getIsPlayChannel() {
        return this.isPlayChannel;
    }

    public final void updateDevices() {
        OtherDeviceLoaderManagerExtKt.getInstance(OtherDeviceLoaderManager.INSTANCE).updateDevices();
    }
}
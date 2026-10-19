package com.xiaomi.fitness.device.manager.ui.tab;

import androidx.lifecycle.MutableLiveData;
import com.fitness.banner.export.v2.BannerItemV2;
import com.mi.fitness.alexa.export.AlexaExportUtilKt;
import com.xiaomi.fitness.baseui.BaseModel;
import com.xiaomi.fitness.baseui.recyclerview.itembindings.ItemBinding;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.common.extensions.StringExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.device.manager.DeviceModelExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.IItemChangesWithDevice;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.device.manager.export.WearableDeviceModel;
import com.xiaomi.fitness.device.manager.export.bean.Feature;
import com.xiaomi.fitness.device.manager.export.bean.TabContentItem;
import com.xiaomi.fitness.device.manager.extensions.DeviceTabModelExtKt;
import defpackage.a9k;
import defpackage.hgk;
import defpackage.hmk;
import defpackage.ng7;
import defpackage.og7;
import defpackage.pg7;
import defpackage.pwn;
import defpackage.qg7;
import defpackage.v12;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\t\b\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0006J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00070\u001b2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0006J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0006H\u0002J\u000e\u0010%\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u0006J\b\u0010'\u001a\u00020\u0006H\u0002J\u0018\u0010(\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u00062\u0006\u0010)\u001a\u00020\u0018H\u0002R0\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00058\u0006@\u0006X\u0087.¢\u0006\u0014\n\u0000\u0012\u0004\b\b\u0010\u0003\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R!\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u001d8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b \u0010!\u001a\u0004\b\u001e\u0010\u001f¨\u0006*"}, d2 = {"Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabModel;", "Lcom/xiaomi/fitness/baseui/BaseModel;", "<init>", "()V", "contentItems", "", "", "Lcom/xiaomi/fitness/device/manager/export/bean/TabContentItem;", "getContentItems$annotations", "getContentItems", "()Ljava/util/Map;", "setContentItems", "(Ljava/util/Map;)V", "tabItemBindings", "Lcom/xiaomi/fitness/baseui/recyclerview/itembindings/ItemBinding;", "getTabItemBindings", "()Lcom/xiaomi/fitness/baseui/recyclerview/itembindings/ItemBinding;", "settingTabItemBindings", "getSettingTabItemBindings", "bannerItemV4Bindings", "Lcom/fitness/banner/export/v2/BannerItemV2;", "getBannerItemV4Bindings", "getDeviceNormalTabList", "currentModel", "Lcom/xiaomi/fitness/device/manager/export/WearableDeviceModel;", "type", "getDeviceFeatureList", "", "hideList", "", "getHideList", "()Ljava/util/Set;", "hideList$delegate", "Lkotlin/Lazy;", "tempHide", "", "featureId", "buildTabTitle", "cardType", "getVoiceAssistantTitle", "buildTabIcon", "deviceModel", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nDeviceTabModel.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeviceTabModel.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabModel\n+ 2 ItemBindingExt.kt\ncom/xiaomi/fitness/baseui/extension/ItemBindingExtKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,217:1\n27#2,5:218\n27#2,5:223\n27#2,5:228\n1617#3,9:233\n1869#3:242\n1870#3:244\n1626#3:245\n774#3:246\n865#3,2:247\n1869#3,2:249\n1869#3,2:251\n1617#3,9:253\n1869#3:262\n1870#3:264\n1626#3:265\n774#3:266\n865#3,2:267\n1869#3,2:269\n1#4:243\n1#4:263\n*S KotlinDebug\n*F\n+ 1 DeviceTabModel.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabModel\n*L\n36#1:218,5\n64#1:223,5\n68#1:228,5\n78#1:233,9\n78#1:242\n78#1:244\n78#1:245\n82#1:246\n82#1:247,2\n87#1:249,2\n98#1:251,2\n128#1:253,9\n128#1:262\n128#1:264\n128#1:265\n132#1:266\n132#1:267,2\n136#1:269,2\n78#1:243\n128#1:263\n*E\n"})
public final class DeviceTabModel extends BaseModel {

    @NotNull
    private final ItemBinding<BannerItemV2> bannerItemV4Bindings;
    public Map<Integer, TabContentItem> contentItems;

    /* JADX INFO: renamed from: hideList$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy hideList;

    @NotNull
    private final ItemBinding<TabContentItem> settingTabItemBindings;

    @NotNull
    private final ItemBinding<TabContentItem> tabItemBindings;

    public DeviceTabModel() {
        ng7 ng7Var = new ng7();
        ItemBinding.Companion companion = ItemBinding.INSTANCE;
        this.tabItemBindings = companion.of(new DeviceTabModel$special$$inlined$itemBindingOf$1(ng7Var));
        this.settingTabItemBindings = companion.of(new DeviceTabModel$special$$inlined$itemBindingOf$2(new og7()));
        this.bannerItemV4Bindings = companion.of(new DeviceTabModel$special$$inlined$itemBindingOf$3(new pg7()));
        this.hideList = LazyKt.lazy(new qg7());
    }

    public static /* synthetic */ Unit a(ItemBinding itemBinding, int i, TabContentItem tabContentItem) {
        return settingTabItemBindings$lambda$1(itemBinding, i, tabContentItem);
    }

    public static /* synthetic */ Unit b(ItemBinding itemBinding, int i, TabContentItem tabContentItem) {
        return tabItemBindings$lambda$0(itemBinding, i, tabContentItem);
    }

    private static final Unit bannerItemV4Bindings$lambda$2(ItemBinding itemBinding, int i, BannerItemV2 item) {
        Intrinsics.checkNotNullParameter(itemBinding, "itemBinding");
        Intrinsics.checkNotNullParameter(item, "item");
        itemBinding.set(v12.g, hgk.v);
        return Unit.INSTANCE;
    }

    private final int buildTabIcon(int cardType, WearableDeviceModel deviceModel) {
        if (cardType == 15) {
            return a9k.E;
        }
        if (cardType == 18) {
            if (DeviceModelExtKt.isWearOS(deviceModel)) {
                return a9k.s;
            }
            if (AlexaExportUtilKt.isSupportAlexa(deviceModel)) {
                return a9k.D;
            }
            return DeviceModelExtKt.isSupportFeature(deviceModel, Feature.SUPER_XIAOAI) ? a9k.A : a9k.C;
        }
        if (cardType == 19) {
            return a9k.v;
        }
        switch (cardType) {
            case 6:
                return a9k.y;
            case 7:
                return a9k.w;
            case 8:
                return a9k.u;
            case 9:
                return a9k.B;
            case 10:
                return a9k.z;
            case 11:
                return a9k.x;
            default:
                return -1;
        }
    }

    public static /* synthetic */ HashSet c() {
        return hideList_delegate$lambda$11();
    }

    public static /* synthetic */ Unit d(ItemBinding itemBinding, int i, BannerItemV2 bannerItemV2) {
        return bannerItemV4Bindings$lambda$2(itemBinding, i, bannerItemV2);
    }

    public static /* synthetic */ void getContentItems$annotations() {
    }

    private final Set<Integer> getHideList() {
        return (Set) this.hideList.getValue();
    }

    private final int getVoiceAssistantTitle() {
        WearableDeviceModel wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel();
        if (wearableDeviceModel == null || !DeviceModelExtKt.isSupportFeature(wearableDeviceModel, Feature.SUPER_XIAOAI)) {
            return (wearableDeviceModel == null || !DeviceModelExtKt.isWearOS(wearableDeviceModel)) ? hmk.t2 : hmk.u2;
        }
        return hmk.Z1;
    }

    private static final HashSet hideList_delegate$lambda$11() {
        HashSet hashSet = new HashSet();
        hashSet.add(150);
        hashSet.add(148);
        return hashSet;
    }

    private static final Unit settingTabItemBindings$lambda$1(ItemBinding itemBinding, int i, TabContentItem item) {
        Intrinsics.checkNotNullParameter(itemBinding, "itemBinding");
        Intrinsics.checkNotNullParameter(item, "item");
        itemBinding.set(v12.g, hgk.N);
        return Unit.INSTANCE;
    }

    private static final Unit tabItemBindings$lambda$0(ItemBinding itemBinding, int i, TabContentItem item) {
        Intrinsics.checkNotNullParameter(itemBinding, "itemBinding");
        Intrinsics.checkNotNullParameter(item, "item");
        int type = item.getType();
        if (type == 1) {
            itemBinding.set(v12.g, hgk.O);
        } else if (type == 15) {
            itemBinding.set(v12.g, hgk.M);
        } else if (type == 102) {
            itemBinding.set(v12.g, hgk.C);
        } else if (type == 200) {
            itemBinding.set(v12.g, hgk.E);
        } else if (type == 203) {
            itemBinding.set(v12.g, hgk.s);
        } else if (type == 3) {
            itemBinding.set(v12.g, hgk.w);
        } else if (type == 4) {
            itemBinding.set(v12.a, hgk.u);
        } else if (type == 5) {
            itemBinding.set(v12.g, hgk.B);
        }
        return Unit.INSTANCE;
    }

    private final boolean tempHide(int featureId) {
        boolean zContains = getHideList().contains(Integer.valueOf(featureId));
        if (zContains) {
            Logger.w("hide feature item:" + featureId, new Object[0]);
        }
        return zContains;
    }

    public final int buildTabTitle(int cardType) {
        switch (cardType) {
            case 6:
                return hmk.p1;
            case 7:
                return hmk.C0;
            case 8:
                return hmk.t;
            case 9:
                return hmk.f2;
            case 10:
                return hmk.R1;
            case 11:
                return hmk.m1;
            case 12:
                return hmk.x0;
            case 13:
                return hmk.H0;
            case 14:
                return hmk.q2;
            case 15:
                return hmk.s;
            case 16:
            case 17:
            default:
                return -1;
            case 18:
                return getVoiceAssistantTitle();
            case 19:
                return hmk.w0;
        }
    }

    @NotNull
    public final ItemBinding<BannerItemV2> getBannerItemV4Bindings() {
        return this.bannerItemV4Bindings;
    }

    @NotNull
    public final Map<Integer, TabContentItem> getContentItems() {
        Map<Integer, TabContentItem> map = this.contentItems;
        if (map != null) {
            return map;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contentItems");
        return null;
    }

    @NotNull
    public final List<TabContentItem> getDeviceFeatureList(@NotNull WearableDeviceModel currentModel, int type) {
        Intrinsics.checkNotNullParameter(currentModel, "currentModel");
        ArrayList arrayList = new ArrayList();
        List<Integer> listBuildBleV4FeatureIds = DeviceModelExtKt.isBluetooth(currentModel) ? DeviceTabModelExtKt.buildBleV4FeatureIds(type) : DeviceModelExtKt.isWearOS(currentModel) ? DeviceTabModelExtKt.buildWearOSV4FeatureIds(type) : DeviceTabModelExtKt.buildHuamiV4FeatureIds(type);
        ArrayList arrayList2 = new ArrayList();
        Iterator<T> it = listBuildBleV4FeatureIds.iterator();
        while (it.hasNext()) {
            Object obj = (TabContentItem) getContentItems().get(Integer.valueOf(((Number) it.next()).intValue()));
            if (obj instanceof IItemChangesWithDevice) {
                ((IItemChangesWithDevice) obj).onDeviceChanged(currentModel.getDid());
            }
            if (obj != null) {
                arrayList2.add(obj);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : arrayList2) {
            TabContentItem tabContentItem = (TabContentItem) obj2;
            if (!tempHide(tabContentItem.getType()) && tabContentItem.getItemVisible(currentModel)) {
                arrayList3.add(obj2);
            }
        }
        Iterator it2 = arrayList3.iterator();
        while (it2.hasNext()) {
            ((TabContentItem) it2.next()).getItemEnable(currentModel);
        }
        if (!arrayList3.isEmpty()) {
            arrayList.addAll(arrayList3);
        }
        return arrayList;
    }

    @Nullable
    public final TabContentItem getDeviceNormalTabList(@NotNull WearableDeviceModel currentModel, int type) {
        List<Integer> listBuildWearOSV4FeatureIds;
        int iBuildTabTitle;
        WearableDeviceModel wearableDeviceModel;
        Intrinsics.checkNotNullParameter(currentModel, "currentModel");
        if (DeviceModelExtKt.isBluetooth(currentModel)) {
            listBuildWearOSV4FeatureIds = DeviceTabModelExtKt.buildBleV4FeatureIds(type);
        } else if (com.xiaomi.fitness.device.manager.export.DeviceModelExtKt.isDualCoreWearOS(currentModel)) {
            listBuildWearOSV4FeatureIds = DeviceTabModelExtKt.buildDualCoreFeatureIds(type);
        } else {
            listBuildWearOSV4FeatureIds = DeviceModelExtKt.isWearOS(currentModel) ? DeviceTabModelExtKt.buildWearOSV4FeatureIds(type) : DeviceTabModelExtKt.buildHuamiV4FeatureIds(type);
        }
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = listBuildWearOSV4FeatureIds.iterator();
        while (it.hasNext()) {
            Object obj = (TabContentItem) getContentItems().get(Integer.valueOf(((Number) it.next()).intValue()));
            if (obj instanceof IItemChangesWithDevice) {
                ((IItemChangesWithDevice) obj).onDeviceChanged(currentModel.getDid());
            }
            if (obj != null) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object obj2 : arrayList) {
            TabContentItem tabContentItem = (TabContentItem) obj2;
            if (!tempHide(tabContentItem.getType()) && tabContentItem.getItemVisible(currentModel)) {
                arrayList2.add(obj2);
            }
        }
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            ((TabContentItem) it2.next()).getItemEnable(currentModel);
        }
        if (arrayList2.isEmpty() || (iBuildTabTitle = buildTabTitle(type)) == -1) {
            return null;
        }
        Iterator it3 = arrayList2.iterator();
        int i = 0;
        while (it3.hasNext()) {
            if (!((TabContentItem) it3.next()).getEnable()) {
                i++;
            }
        }
        boolean z = i != arrayList2.size();
        int iBuildTabIcon = buildTabIcon(type, currentModel);
        String string = ApplicationExtKt.getApplication().getString(iBuildTabTitle);
        Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
        MutableLiveData<String> liveData = StringExtKt.toLiveData(string);
        MutableLiveData<String> liveData2 = (type != 9 || pwn.y(ApplicationExtKt.getApplication()) || (wearableDeviceModel = (WearableDeviceModel) DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).getCurrentDeviceModel()) == null || DeviceModelExtKt.isWearOS(wearableDeviceModel)) ? StringExtKt.toLiveData("") : StringExtKt.toLiveData(" ");
        return new TabContentItem(11, type, iBuildTabIcon, liveData, null, false, null, null, null, null, null, null, null, z, null, liveData2, 24560, null);
    }

    @NotNull
    public final ItemBinding<TabContentItem> getSettingTabItemBindings() {
        return this.settingTabItemBindings;
    }

    @NotNull
    public final ItemBinding<TabContentItem> getTabItemBindings() {
        return this.tabItemBindings;
    }

    public final void setContentItems(@NotNull Map<Integer, TabContentItem> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.contentItems = map;
    }
}
package com.xiaomi.fitness.device.manager.ui.tab;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.statistics.export.IOnetrack;
import com.example.statistics.export.OneTrackExtKt;
import com.example.statistics.export.OneTrackStatBean;
import com.xiaomi.fitness.baseui.extension.BaseFragmentExtKt;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.common.extensions.AnyExtKt;
import com.xiaomi.fitness.common.extensions.ListExtKt;
import com.xiaomi.fitness.common.log.Logger;
import com.xiaomi.fitness.device.manager.DeviceModel;
import com.xiaomi.fitness.device.manager.export.DeviceManagerExtKt;
import com.xiaomi.fitness.device.manager.export.DeviceTabInfoProvider;
import com.xiaomi.fitness.device.manager.export.WearableDeviceManager;
import com.xiaomi.fitness.eco.device.manager.export.EcoDeviceManager;
import com.xiaomi.fitness.eco.device.manager.export.EcoDeviceManagerExtKt;
import com.xiaomi.fitness.rn.export.RNDeviceManager;
import com.xiaomi.fitness.rn.export.extensions.RNDeviceManagerExtKt;
import com.xiaomi.fitness.widget.view.HomeHeaderTabTitleView;
import defpackage.ah7;
import defpackage.bh7;
import defpackage.c3j;
import defpackage.ch7;
import defpackage.hgk;
import defpackage.is6;
import defpackage.q4k;
import defpackage.v12;
import defpackage.wg7;
import defpackage.xg7;
import defpackage.yg7;
import defpackage.zg7;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000y\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\b\u0005*\u0001?\b\u0007\u0018\u0000 B2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001BB\t\b\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u001d\u0010\n\u001a\u00020\t2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u0017\u0010\u000e\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\tH\u0002¢\u0006\u0004\b\u0010\u0010\u0005J#\u0010\u0014\u001a\u00020\t2\b\b\u0002\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\u0013\u001a\u00020\u0011H\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u0017\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u0016H\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u0011\u0010\u001a\u001a\u0004\u0018\u00010\fH\u0016¢\u0006\u0004\b\u001a\u0010\u001bJ!\u0010\u001e\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001cH\u0016¢\u0006\u0004\b\u001e\u0010\u001fJ\u000f\u0010 \u001a\u00020\tH\u0014¢\u0006\u0004\b \u0010\u0005J\r\u0010!\u001a\u00020\t¢\u0006\u0004\b!\u0010\u0005J\u000f\u0010\"\u001a\u00020\tH\u0016¢\u0006\u0004\b\"\u0010\u0005R\u0016\u0010#\u001a\u00020\u00118\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b#\u0010$R\u001a\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00070%8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b&\u0010'R\u001a\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00070%8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b(\u0010'R\u0018\u0010*\u001a\u0004\u0018\u00010)8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b*\u0010+R3\u0010.\u001a\r\u0012\t\u0012\u00070\u0007¢\u0006\u0002\b-0,8\u0006@\u0006X\u0087.¢\u0006\u0018\n\u0004\b.\u0010/\u0012\u0004\b4\u0010\u0005\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u0016\u00105\u001a\u00020\u00168\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b5\u00106R5\u0010<\u001a#\u0012\u0019\u0012\u0017\u0012\u0002\b\u0003\u0018\u000108¢\u0006\f\b9\u0012\b\b:\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020\t078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b<\u0010=R5\u0010>\u001a#\u0012\u0019\u0012\u0017\u0012\u0002\b\u0003\u0018\u000108¢\u0006\f\b9\u0012\b\b:\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020\t078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b>\u0010=R\u0014\u0010@\u001a\u00020?8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b@\u0010A¨\u0006C"}, d2 = {"Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4;", "Lcom/xiaomi/fitness/main/export/BaseMainFragment;", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentViewModel;", "Lis6;", "<init>", "()V", "", "Lcom/xiaomi/fitness/device/manager/export/DeviceTabInfoProvider;", "tabs", "", "initTitleView", "(Ljava/util/List;)V", "Landroid/view/View;", "view", "showPopupMenu", "(Landroid/view/View;)V", "initView", "", "fromEco", "fromRN", "refreshTabs", "(ZZ)V", "", "index", "setSelect", "(I)V", "getContentView", "()Landroid/view/View;", "Landroid/os/Bundle;", "savedInstanceState", "onViewCreated", "(Landroid/view/View;Landroid/os/Bundle;)V", "onVisible", "selectOtherDeviceTab", LifecycleConstantKt.ON_DESTROY, "needSelectOtherDeviceTab", "Z", "", "currentTabs", "Ljava/util/List;", "showTabs", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabPagerAdapter;", "pageAdapter", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabPagerAdapter;", "", "Lkotlin/jvm/JvmSuppressWildcards;", "mTabs", "Ljava/util/Set;", "getMTabs", "()Ljava/util/Set;", "setMTabs", "(Ljava/util/Set;)V", "getMTabs$annotations", "mDefaultIndex", "I", "Lkotlin/Function1;", "Lcom/xiaomi/fitness/device/manager/DeviceModel;", "Lkotlin/ParameterName;", "name", "deviceModel", "deviceChangeListener", "Lkotlin/jvm/functions/Function1;", "rnDeviceChangeListener", "com/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4$onEcoDeviceStatusListener$1", "onEcoDeviceStatusListener", "Lcom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4$onEcoDeviceStatusListener$1;", "Companion", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nDeviceTabParentFragmentV4.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeviceTabParentFragmentV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,266:1\n1#2:267\n1056#3:268\n1563#3:269\n1634#3,3:270\n774#3:273\n865#3,2:274\n1056#3:276\n1869#3,2:277\n774#3:279\n865#3,2:280\n1761#3,3:282\n360#3,7:285\n774#3:292\n865#3,2:293\n*S KotlinDebug\n*F\n+ 1 DeviceTabParentFragmentV4.kt\ncom/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4\n*L\n91#1:268\n91#1:269\n91#1:270,3\n129#1:273\n129#1:274,2\n135#1:276\n137#1:277,2\n158#1:279\n158#1:280,2\n194#1:282,3\n203#1:285,7\n96#1:292\n96#1:293,2\n*E\n"})
public final class DeviceTabParentFragmentV4 extends Hilt_DeviceTabParentFragmentV4<DeviceTabParentViewModel, is6> {

    @NotNull
    private static final String TAG = "DeviceTabParentFragmentV4";

    @NotNull
    private final List<DeviceTabInfoProvider> currentTabs;

    @NotNull
    private final Function1<DeviceModel<?>, Unit> deviceChangeListener;
    private int mDefaultIndex;
    public Set<DeviceTabInfoProvider> mTabs;
    private boolean needSelectOtherDeviceTab;

    @NotNull
    private final DeviceTabParentFragmentV4$onEcoDeviceStatusListener$1 onEcoDeviceStatusListener;

    @Nullable
    private DeviceTabPagerAdapter pageAdapter;

    @NotNull
    private final Function1<DeviceModel<?>, Unit> rnDeviceChangeListener;

    @NotNull
    private final List<DeviceTabInfoProvider> showTabs;

    /* JADX INFO: renamed from: com.xiaomi.fitness.device.manager.ui.tab.DeviceTabParentFragmentV4$initView$3, reason: invalid class name */
    @Metadata(d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"com/xiaomi/fitness/device/manager/ui/tab/DeviceTabParentFragmentV4$initView$3", "Landroidx/viewpager/widget/ViewPager$k;", "", "position", "", "onPageSelected", "(I)V", "device-manager_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass3 extends ViewPager.k {
        public AnonymousClass3() {
        }

        @Override // androidx.viewpager.widget.ViewPager.k, androidx.viewpager.widget.ViewPager.i
        public void onPageSelected(int position) {
            DeviceTabInfoProvider deviceTabInfoProvider = (DeviceTabInfoProvider) CollectionsKt.getOrNull(DeviceTabParentFragmentV4.access$getShowTabs$p(DeviceTabParentFragmentV4.this), position);
            int iIndex = deviceTabInfoProvider != null ? deviceTabInfoProvider.index() : 3;
            DeviceTabParentFragmentV4.access$setMDefaultIndex$p(DeviceTabParentFragmentV4.this, iIndex);
            DeviceTabParentFragmentV4.access$setSelect(DeviceTabParentFragmentV4.this, iIndex);
        }
    }

    public DeviceTabParentFragmentV4() {
        super(hgk.p, v12.f15099k);
        this.currentTabs = new ArrayList();
        this.showTabs = new ArrayList();
        this.deviceChangeListener = new wg7(this);
        this.rnDeviceChangeListener = new xg7(this);
        this.onEcoDeviceStatusListener = new DeviceTabParentFragmentV4$onEcoDeviceStatusListener$1(this);
    }

    public static /* synthetic */ Unit B(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, DeviceModel deviceModel) {
        return rnDeviceChangeListener$lambda$25(deviceTabParentFragmentV4, deviceModel);
    }

    public static /* synthetic */ Unit C(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, MenuItem menuItem) {
        return showPopupMenu$lambda$8(deviceTabParentFragmentV4, menuItem);
    }

    public static /* synthetic */ boolean D(DeviceTabInfoProvider deviceTabInfoProvider) {
        return refreshTabs$lambda$21(deviceTabInfoProvider);
    }

    public static /* synthetic */ Unit E(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, DeviceModel deviceModel) {
        return deviceChangeListener$lambda$24(deviceTabParentFragmentV4, deviceModel);
    }

    public static /* synthetic */ boolean F(DeviceTabInfoProvider deviceTabInfoProvider) {
        return refreshTabs$lambda$20(deviceTabInfoProvider);
    }

    public static final /* synthetic */ List access$getShowTabs$p(DeviceTabParentFragmentV4 deviceTabParentFragmentV4) {
        return deviceTabParentFragmentV4.showTabs;
    }

    public static final /* synthetic */ void access$setMDefaultIndex$p(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, int i) {
        deviceTabParentFragmentV4.mDefaultIndex = i;
    }

    public static final /* synthetic */ void access$setSelect(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, int i) {
        deviceTabParentFragmentV4.setSelect(i);
    }

    private static final Unit deviceChangeListener$lambda$24(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, DeviceModel deviceModel) {
        Logger.i(TAG, "onDeviceChange " + deviceModel);
        AnyExtKt.main$default(null, new DeviceTabParentFragmentV4$deviceChangeListener$1$1(deviceTabParentFragmentV4, null), 1, null);
        return Unit.INSTANCE;
    }

    public static /* synthetic */ void getMTabs$annotations() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void initTitleView(List<? extends DeviceTabInfoProvider> tabs) {
        int i;
        Object next;
        Object next2;
        setTitle((String) null);
        List<? extends DeviceTabInfoProvider> list = tabs;
        Iterator<T> it = list.iterator();
        do {
            i = 3;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (((DeviceTabInfoProvider) next).index() != 3);
        DeviceTabInfoProvider deviceTabInfoProvider = (DeviceTabInfoProvider) next;
        int iDeviceCount = deviceTabInfoProvider != null ? deviceTabInfoProvider.deviceCount() : 0;
        Iterator<T> it2 = list.iterator();
        do {
            if (!it2.hasNext()) {
                next2 = null;
                break;
            }
            next2 = it2.next();
        } while (((DeviceTabInfoProvider) next2).index() != 4);
        DeviceTabInfoProvider deviceTabInfoProvider2 = (DeviceTabInfoProvider) next2;
        int iDeviceCount2 = deviceTabInfoProvider2 != null ? deviceTabInfoProvider2.deviceCount() : 0;
        if (iDeviceCount == 0 && iDeviceCount2 == 0) {
            i = 1;
        } else if (iDeviceCount2 != 0) {
            i = iDeviceCount == 0 ? 2 : 4;
        }
        HomeHeaderTabTitleView homeHeaderTabTitleView = ((is6) getMBinding()).A;
        List listSortedWith = CollectionsKt.sortedWith(list, new DeviceTabParentFragmentV4$initTitleView$$inlined$sortedBy$1());
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listSortedWith, 10));
        Iterator it3 = listSortedWith.iterator();
        while (it3.hasNext()) {
            arrayList.add(((DeviceTabInfoProvider) it3.next()).tabName(i));
        }
        HomeHeaderTabTitleView.setTitle$default(homeHeaderTabTitleView, arrayList, 0, 2, (Object) null);
        ((is6) getMBinding()).A.setTitleSelectListener(new yg7(this));
        ((is6) getMBinding()).A.setMenuClickListener(new zg7(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final Unit initTitleView$lambda$5(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, int i) {
        String str;
        String str2;
        Logger.i(TAG, "select tab: " + i);
        ((is6) deviceTabParentFragmentV4.getMBinding()).B.setCurrentItem(i);
        Set<DeviceTabInfoProvider> mTabs = deviceTabParentFragmentV4.getMTabs();
        ArrayList arrayList = new ArrayList();
        for (Object obj : mTabs) {
            DeviceTabInfoProvider deviceTabInfoProvider = (DeviceTabInfoProvider) obj;
            if (deviceTabInfoProvider.index() > 2 && deviceTabInfoProvider.deviceCount() > 0) {
                arrayList.add(obj);
            }
        }
        DeviceTabInfoProvider deviceTabInfoProvider2 = (DeviceTabInfoProvider) CollectionsKt.getOrNull(arrayList, i);
        if (deviceTabInfoProvider2 == null) {
            return Unit.INSTANCE;
        }
        if (deviceTabInfoProvider2.index() == 3) {
            str = "Device_page_Weardevicetab";
            str2 = "56.51.0.1.36382";
        } else {
            str = "Device_page_Otherdevicetab";
            str2 = "56.51.0.1.36383";
        }
        OneTrackExtKt.getInstance(IOnetrack.INSTANCE).reportData(new OneTrackStatBean("click_v3", str2), "name", str);
        return Unit.INSTANCE;
    }

    private static final void initTitleView$lambda$6(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, View view) {
        Intrinsics.checkNotNull(view);
        deviceTabParentFragmentV4.showPopupMenu(view);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void initView() {
        Object obj;
        Object next;
        this.currentTabs.clear();
        this.showTabs.clear();
        Set<DeviceTabInfoProvider> mTabs = getMTabs();
        ArrayList arrayList = new ArrayList();
        for (Object obj2 : mTabs) {
            DeviceTabInfoProvider deviceTabInfoProvider = (DeviceTabInfoProvider) obj2;
            if (deviceTabInfoProvider.index() > 2 && deviceTabInfoProvider.deviceCount() > 0) {
                arrayList.add(obj2);
            }
        }
        this.currentTabs.addAll(arrayList);
        Iterator<T> it = getMTabs().iterator();
        do {
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (((DeviceTabInfoProvider) next).index() != 3);
        DeviceTabInfoProvider deviceTabInfoProvider2 = (DeviceTabInfoProvider) next;
        if (arrayList.isEmpty() && deviceTabInfoProvider2 != null) {
            ListExtKt.add(arrayList, deviceTabInfoProvider2);
        }
        Iterator it2 = CollectionsKt.sortedWith(arrayList, new DeviceTabParentFragmentV4$initView$$inlined$sortedBy$1()).iterator();
        while (it2.hasNext()) {
            this.showTabs.add((DeviceTabInfoProvider) it2.next());
        }
        initTitleView(this.showTabs);
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
        this.pageAdapter = new DeviceTabPagerAdapter(childFragmentManager, this.showTabs);
        ((is6) getMBinding()).B.setAdapter(this.pageAdapter);
        ((is6) getMBinding()).B.addOnPageChangeListener(new AnonymousClass3());
        for (Object obj3 : this.showTabs) {
            if (((DeviceTabInfoProvider) obj3).deviceCount() > 0) {
                obj = obj3;
                break;
            }
        }
        DeviceTabInfoProvider deviceTabInfoProvider3 = (DeviceTabInfoProvider) obj;
        int iIndex = deviceTabInfoProvider3 != null ? deviceTabInfoProvider3.index() : 3;
        this.mDefaultIndex = iIndex;
        setSelect(iIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void refreshTabs(boolean fromEco, boolean fromRN) {
        int iIndex;
        Object obj;
        Object next;
        Object next2;
        Object next3;
        Object next4;
        boolean z;
        Set<DeviceTabInfoProvider> mTabs = getMTabs();
        ArrayList arrayList = new ArrayList();
        for (Object obj2 : mTabs) {
            DeviceTabInfoProvider deviceTabInfoProvider = (DeviceTabInfoProvider) obj2;
            if (deviceTabInfoProvider.index() > 2 && deviceTabInfoProvider.deviceCount() > 0) {
                arrayList.add(obj2);
            }
        }
        Iterator<T> it = this.currentTabs.iterator();
        do {
            iIndex = 3;
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (((DeviceTabInfoProvider) next).index() != 3);
        DeviceTabInfoProvider deviceTabInfoProvider2 = (DeviceTabInfoProvider) next;
        Iterator<T> it2 = this.currentTabs.iterator();
        do {
            if (!it2.hasNext()) {
                next2 = null;
                break;
            }
            next2 = it2.next();
        } while (((DeviceTabInfoProvider) next2).index() != 4);
        DeviceTabInfoProvider deviceTabInfoProvider3 = (DeviceTabInfoProvider) next2;
        this.currentTabs.clear();
        this.currentTabs.addAll(arrayList);
        Iterator it3 = arrayList.iterator();
        do {
            if (!it3.hasNext()) {
                next3 = null;
                break;
            }
            next3 = it3.next();
        } while (((DeviceTabInfoProvider) next3).index() != 3);
        DeviceTabInfoProvider deviceTabInfoProvider4 = (DeviceTabInfoProvider) next3;
        Iterator it4 = arrayList.iterator();
        do {
            if (!it4.hasNext()) {
                next4 = null;
                break;
            }
            next4 = it4.next();
        } while (((DeviceTabInfoProvider) next4).index() != 4);
        DeviceTabInfoProvider deviceTabInfoProvider5 = (DeviceTabInfoProvider) next4;
        if (deviceTabInfoProvider5 != null && deviceTabInfoProvider3 == null) {
            this.showTabs.add(deviceTabInfoProvider5);
        }
        boolean z2 = true;
        if (deviceTabInfoProvider2 != null || deviceTabInfoProvider3 == null || (!(deviceTabInfoProvider4 == null && deviceTabInfoProvider5 == null) && (deviceTabInfoProvider4 == null || deviceTabInfoProvider5 == null))) {
            z = false;
        } else {
            if (deviceTabInfoProvider4 == null) {
                for (Object obj3 : getMTabs()) {
                    if (((DeviceTabInfoProvider) obj3).index() == 3) {
                        obj = obj3;
                        break;
                    }
                }
                ListExtKt.add(this.showTabs, 0, (DeviceTabInfoProvider) obj);
            } else {
                this.showTabs.add(0, deviceTabInfoProvider4);
            }
            z = true;
        }
        if (deviceTabInfoProvider3 != null && deviceTabInfoProvider5 == null) {
            CollectionsKt.removeAll((List) this.showTabs, (Function1) new bh7());
        }
        if (deviceTabInfoProvider4 == null && deviceTabInfoProvider5 != null && ((deviceTabInfoProvider2 == null && deviceTabInfoProvider3 == null) || (deviceTabInfoProvider2 != null && deviceTabInfoProvider3 != null))) {
            CollectionsKt.removeAll((List) this.showTabs, (Function1) new ch7());
            z = true;
        }
        if (z) {
            FragmentManager childFragmentManager = getChildFragmentManager();
            Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
            this.pageAdapter = new DeviceTabPagerAdapter(childFragmentManager, this.showTabs);
            ((is6) getMBinding()).B.setAdapter(this.pageAdapter);
        } else {
            DeviceTabPagerAdapter deviceTabPagerAdapter = this.pageAdapter;
            if (deviceTabPagerAdapter != null) {
                deviceTabPagerAdapter.notifyDataSetChanged();
            }
        }
        initTitleView(this.showTabs);
        List<DeviceTabInfoProvider> list = this.showTabs;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator<T> it5 = list.iterator();
            do {
                if (!it5.hasNext()) {
                    z2 = false;
                    break;
                }
            } while (!(((DeviceTabInfoProvider) it5.next()).index() == this.mDefaultIndex));
        } else {
            z2 = false;
            break;
        }
        if (z2) {
            iIndex = this.mDefaultIndex;
        } else {
            DeviceTabInfoProvider deviceTabInfoProvider6 = (DeviceTabInfoProvider) CollectionsKt.firstOrNull((List) this.showTabs);
            if (deviceTabInfoProvider6 != null) {
                iIndex = deviceTabInfoProvider6.index();
            }
        }
        this.mDefaultIndex = iIndex;
        setSelect(iIndex);
    }

    public static /* synthetic */ void refreshTabs$default(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        deviceTabParentFragmentV4.refreshTabs(z, z2);
    }

    private static final boolean refreshTabs$lambda$20(DeviceTabInfoProvider it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.index() == 4;
    }

    private static final boolean refreshTabs$lambda$21(DeviceTabInfoProvider it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.index() == 3;
    }

    private static final Unit rnDeviceChangeListener$lambda$25(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, DeviceModel deviceModel) {
        Logger.i(TAG, "onRNDeviceChange " + deviceModel);
        AnyExtKt.main$default(null, new DeviceTabParentFragmentV4$rnDeviceChangeListener$1$1(deviceTabParentFragmentV4, null), 1, null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void setSelect(int index) {
        Iterator<DeviceTabInfoProvider> it = this.showTabs.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (it.next().index() == index) {
                break;
            } else {
                i++;
            }
        }
        if (i >= 0) {
            c3j adapter = ((is6) getMBinding()).B.getAdapter();
            Intrinsics.checkNotNull(adapter);
            if (i < adapter.getCount()) {
                ((is6) getMBinding()).B.setCurrentItem(i);
                ((is6) getMBinding()).A.updateSelect(i);
            }
        }
    }

    private final void showPopupMenu(View view) {
        showPopupMenu(view, new ah7(this));
    }

    private static final Unit showPopupMenu$lambda$8(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, MenuItem it) {
        Object next;
        Intrinsics.checkNotNullParameter(it, "it");
        Iterator<T> it2 = deviceTabParentFragmentV4.getMTabs().iterator();
        do {
            if (!it2.hasNext()) {
                next = null;
                break;
            }
            next = it2.next();
        } while (((DeviceTabInfoProvider) next).index() != 3);
        DeviceTabFragmentV4 deviceTabFragmentV4 = next instanceof DeviceTabFragmentV4 ? (DeviceTabFragmentV4) next : null;
        if (deviceTabFragmentV4 != null) {
            deviceTabFragmentV4.checkChosenAppIfNeed();
        }
        return Unit.INSTANCE;
    }

    public static /* synthetic */ void w(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, View view) {
        initTitleView$lambda$6(deviceTabParentFragmentV4, view);
    }

    public static /* synthetic */ Unit x(DeviceTabParentFragmentV4 deviceTabParentFragmentV4, int i) {
        return initTitleView$lambda$5(deviceTabParentFragmentV4, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.main.export.BaseMainFragment
    @Nullable
    public View getContentView() {
        return ((is6) getMBinding()).B;
    }

    @NotNull
    public final Set<DeviceTabInfoProvider> getMTabs() {
        Set<DeviceTabInfoProvider> set = this.mTabs;
        if (set != null) {
            return set;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mTabs");
        return null;
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).removeDeviceChangeListener(this.deviceChangeListener);
        RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).removeDeviceChangeListener(this.rnDeviceChangeListener);
        EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).removeDeviceStatusListener(this.onEcoDeviceStatusListener);
    }

    @Override // com.xiaomi.fitness.main.export.BaseMainFragment, com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        setActionBarDisplayable(false);
        initView();
        DeviceManagerExtKt.getInstance(WearableDeviceManager.INSTANCE).addDeviceChangeListener(this.deviceChangeListener);
        RNDeviceManagerExtKt.getInstance(RNDeviceManager.INSTANCE).addDeviceChangeListener(this.rnDeviceChangeListener);
        EcoDeviceManagerExtKt.getInstance(EcoDeviceManager.INSTANCE).addDeviceStatusListener(this.onEcoDeviceStatusListener);
        BaseFragmentExtKt.setStatusBarColor(this, q4k.c);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.xiaomi.fitness.baseui.view.BaseFragment
    public void onVisible() {
        super.onVisible();
        if (this.needSelectOtherDeviceTab) {
            c3j adapter = ((is6) getMBinding()).B.getAdapter();
            if ((adapter != null ? adapter.getCount() : 0) > 1) {
                ((is6) getMBinding()).B.setCurrentItem(1);
            }
            this.needSelectOtherDeviceTab = false;
        }
        ((DeviceTabParentViewModel) getMViewModel()).updateDevices();
    }

    public final void selectOtherDeviceTab() {
        this.needSelectOtherDeviceTab = true;
    }

    public final void setMTabs(@NotNull Set<DeviceTabInfoProvider> set) {
        Intrinsics.checkNotNullParameter(set, "<set-?>");
        this.mTabs = set;
    }
}
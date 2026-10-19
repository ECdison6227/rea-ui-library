package com.xiaomi.fitness.main.export;

import androidx.fragment.app.Fragment;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\u0003¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u00032\b\b\u0003\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0003\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\f¨\u0006\u001f"}, d2 = {"Lcom/xiaomi/fitness/main/export/MainTab;", "", "index", "", "name", "icon", "fragment", "Landroidx/fragment/app/Fragment;", "backgroundColor", "<init>", "(IIILandroidx/fragment/app/Fragment;I)V", "getIndex", "()I", "getName", "getIcon", "getFragment", "()Landroidx/fragment/app/Fragment;", "getBackgroundColor", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "Companion", "main-export_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class MainTab {
    private static final int INDEX_HEALTH = 0;
    public static final int INDEX_INVALID = -1;

    @NotNull
    public static final String KEY_SHOW_CHOOSE_COMMON_APP = "show_choose_common_app";

    @NotNull
    public static final String KEY_TAB = "key_tab";

    @NotNull
    public static final String PAGE_TYPE = "PAGE_TYPE";

    @NotNull
    public static final String SON_PAGE_TYPE = "SON_PAGE_TYPE";
    private final int backgroundColor;

    @NotNull
    private final Fragment fragment;
    private final int icon;
    private final int index;
    private final int name;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);
    private static final int INDEX_SPORT = 1;
    private static final int INDEX_DEVICE = 2;
    private static final int INDEX_ME = 3;

    @NotNull
    private static final String HEALTH_TAB_RED_POINT_KEY = "root.health";

    @NotNull
    private static final String SPORT_TAB_RED_POINT_KEY = "root.sport";

    @NotNull
    private static final String DEVICE_TAB_RED_POINT_KEY = "root.devices";

    @NotNull
    private static final String MY_TAB_RED_POINT_KEY = "root.my";

    @NotNull
    private static final ConcurrentHashMap<String, Integer> classToIndexMap = new ConcurrentHashMap<>();

    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010'\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u0005J\u0016\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u00020\n8\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\f\u0010\u0003\u001a\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u00020\n8\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0010\u0010\u0003\u001a\u0004\b\u0011\u0010\u000eR\u001c\u0010\u0012\u001a\u00020\n8\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0013\u0010\u0003\u001a\u0004\b\u0014\u0010\u000eR\u001c\u0010\u0015\u001a\u00020\n8\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0016\u0010\u0003\u001a\u0004\b\u0017\u0010\u000eR\u001c\u0010\u0018\u001a\u00020\u00058\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u0019\u0010\u0003\u001a\u0004\b\u001a\u0010\u001bR\u001c\u0010\u001c\u001a\u00020\u00058\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b\u001d\u0010\u0003\u001a\u0004\b\u001e\u0010\u001bR\u001c\u0010\u001f\u001a\u00020\u00058\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b \u0010\u0003\u001a\u0004\b!\u0010\u001bR\u001c\u0010\"\u001a\u00020\u00058\u0006X\u0087D¢\u0006\u000e\n\u0000\u0012\u0004\b#\u0010\u0003\u001a\u0004\b$\u0010\u001bR\u001a\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0&X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006-"}, d2 = {"Lcom/xiaomi/fitness/main/export/MainTab$Companion;", "", "<init>", "()V", "KEY_TAB", "", MainTab.PAGE_TYPE, MainTab.SON_PAGE_TYPE, "KEY_SHOW_CHOOSE_COMMON_APP", "INDEX_INVALID", "", "INDEX_HEALTH", "getINDEX_HEALTH$annotations", "getINDEX_HEALTH", "()I", "INDEX_SPORT", "getINDEX_SPORT$annotations", "getINDEX_SPORT", "INDEX_DEVICE", "getINDEX_DEVICE$annotations", "getINDEX_DEVICE", "INDEX_ME", "getINDEX_ME$annotations", "getINDEX_ME", "HEALTH_TAB_RED_POINT_KEY", "getHEALTH_TAB_RED_POINT_KEY$annotations", "getHEALTH_TAB_RED_POINT_KEY", "()Ljava/lang/String;", "SPORT_TAB_RED_POINT_KEY", "getSPORT_TAB_RED_POINT_KEY$annotations", "getSPORT_TAB_RED_POINT_KEY", "DEVICE_TAB_RED_POINT_KEY", "getDEVICE_TAB_RED_POINT_KEY$annotations", "getDEVICE_TAB_RED_POINT_KEY", "MY_TAB_RED_POINT_KEY", "getMY_TAB_RED_POINT_KEY$annotations", "getMY_TAB_RED_POINT_KEY", "classToIndexMap", "Ljava/util/concurrent/ConcurrentHashMap;", "getIndexByTabFragmentClass", "className", "registerTabFragment", "", "clazzName", "index", "main-export_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public static /* synthetic */ void getDEVICE_TAB_RED_POINT_KEY$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getHEALTH_TAB_RED_POINT_KEY$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getINDEX_DEVICE$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getINDEX_HEALTH$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getINDEX_ME$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getINDEX_SPORT$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getMY_TAB_RED_POINT_KEY$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getSPORT_TAB_RED_POINT_KEY$annotations() {
        }

        @NotNull
        public final String getDEVICE_TAB_RED_POINT_KEY() {
            return MainTab.access$getDEVICE_TAB_RED_POINT_KEY$cp();
        }

        @NotNull
        public final String getHEALTH_TAB_RED_POINT_KEY() {
            return MainTab.access$getHEALTH_TAB_RED_POINT_KEY$cp();
        }

        public final int getINDEX_DEVICE() {
            return MainTab.access$getINDEX_DEVICE$cp();
        }

        public final int getINDEX_HEALTH() {
            return MainTab.access$getINDEX_HEALTH$cp();
        }

        public final int getINDEX_ME() {
            return MainTab.access$getINDEX_ME$cp();
        }

        public final int getINDEX_SPORT() {
            return MainTab.access$getINDEX_SPORT$cp();
        }

        public final int getIndexByTabFragmentClass(@NotNull String className) {
            Intrinsics.checkNotNullParameter(className, "className");
            Integer num = (Integer) MainTab.access$getClassToIndexMap$cp().get(className);
            if (num != null) {
                return num.intValue();
            }
            return -1;
        }

        @NotNull
        public final String getMY_TAB_RED_POINT_KEY() {
            return MainTab.access$getMY_TAB_RED_POINT_KEY$cp();
        }

        @NotNull
        public final String getSPORT_TAB_RED_POINT_KEY() {
            return MainTab.access$getSPORT_TAB_RED_POINT_KEY$cp();
        }

        public final void registerTabFragment(@NotNull String clazzName, int index) {
            Intrinsics.checkNotNullParameter(clazzName, "clazzName");
            MainTab.access$getClassToIndexMap$cp().put(clazzName, Integer.valueOf(index));
        }

        private Companion() {
        }
    }

    public MainTab(int i, int i2, int i3, @NotNull Fragment fragment, int i4) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        this.index = i;
        this.name = i2;
        this.icon = i3;
        this.fragment = fragment;
        this.backgroundColor = i4;
    }

    public static final /* synthetic */ ConcurrentHashMap access$getClassToIndexMap$cp() {
        return classToIndexMap;
    }

    public static final /* synthetic */ String access$getDEVICE_TAB_RED_POINT_KEY$cp() {
        return DEVICE_TAB_RED_POINT_KEY;
    }

    public static final /* synthetic */ String access$getHEALTH_TAB_RED_POINT_KEY$cp() {
        return HEALTH_TAB_RED_POINT_KEY;
    }

    public static final /* synthetic */ int access$getINDEX_DEVICE$cp() {
        return INDEX_DEVICE;
    }

    public static final /* synthetic */ int access$getINDEX_HEALTH$cp() {
        return INDEX_HEALTH;
    }

    public static final /* synthetic */ int access$getINDEX_ME$cp() {
        return INDEX_ME;
    }

    public static final /* synthetic */ int access$getINDEX_SPORT$cp() {
        return INDEX_SPORT;
    }

    public static final /* synthetic */ String access$getMY_TAB_RED_POINT_KEY$cp() {
        return MY_TAB_RED_POINT_KEY;
    }

    public static final /* synthetic */ String access$getSPORT_TAB_RED_POINT_KEY$cp() {
        return SPORT_TAB_RED_POINT_KEY;
    }

    public static /* synthetic */ MainTab copy$default(MainTab mainTab, int i, int i2, int i3, Fragment fragment, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = mainTab.index;
        }
        if ((i5 & 2) != 0) {
            i2 = mainTab.name;
        }
        if ((i5 & 4) != 0) {
            i3 = mainTab.icon;
        }
        if ((i5 & 8) != 0) {
            fragment = mainTab.fragment;
        }
        if ((i5 & 16) != 0) {
            i4 = mainTab.backgroundColor;
        }
        int i6 = i4;
        int i7 = i3;
        return mainTab.copy(i, i2, i7, fragment, i6);
    }

    @NotNull
    public static final String getDEVICE_TAB_RED_POINT_KEY() {
        return INSTANCE.getDEVICE_TAB_RED_POINT_KEY();
    }

    @NotNull
    public static final String getHEALTH_TAB_RED_POINT_KEY() {
        return INSTANCE.getHEALTH_TAB_RED_POINT_KEY();
    }

    public static final int getINDEX_DEVICE() {
        return INSTANCE.getINDEX_DEVICE();
    }

    public static final int getINDEX_HEALTH() {
        return INSTANCE.getINDEX_HEALTH();
    }

    public static final int getINDEX_ME() {
        return INSTANCE.getINDEX_ME();
    }

    public static final int getINDEX_SPORT() {
        return INSTANCE.getINDEX_SPORT();
    }

    @NotNull
    public static final String getMY_TAB_RED_POINT_KEY() {
        return INSTANCE.getMY_TAB_RED_POINT_KEY();
    }

    @NotNull
    public static final String getSPORT_TAB_RED_POINT_KEY() {
        return INSTANCE.getSPORT_TAB_RED_POINT_KEY();
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getIndex() {
        return this.index;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getIcon() {
        return this.icon;
    }

    @NotNull
    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Fragment getFragment() {
        return this.fragment;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getBackgroundColor() {
        return this.backgroundColor;
    }

    @NotNull
    public final MainTab copy(int index, int name, int icon, @NotNull Fragment fragment, int backgroundColor) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        return new MainTab(index, name, icon, fragment, backgroundColor);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MainTab)) {
            return false;
        }
        MainTab mainTab = (MainTab) other;
        return this.index == mainTab.index && this.name == mainTab.name && this.icon == mainTab.icon && Intrinsics.areEqual(this.fragment, mainTab.fragment) && this.backgroundColor == mainTab.backgroundColor;
    }

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }

    @NotNull
    public final Fragment getFragment() {
        return this.fragment;
    }

    public final int getIcon() {
        return this.icon;
    }

    public final int getIndex() {
        return this.index;
    }

    public final int getName() {
        return this.name;
    }

    public int hashCode() {
        return (((((((Integer.hashCode(this.index) * 31) + Integer.hashCode(this.name)) * 31) + Integer.hashCode(this.icon)) * 31) + this.fragment.hashCode()) * 31) + Integer.hashCode(this.backgroundColor);
    }

    @NotNull
    public String toString() {
        return "MainTab(index=" + this.index + ", name=" + this.name + ", icon=" + this.icon + ", fragment=" + this.fragment + ", backgroundColor=" + this.backgroundColor + ")";
    }
}
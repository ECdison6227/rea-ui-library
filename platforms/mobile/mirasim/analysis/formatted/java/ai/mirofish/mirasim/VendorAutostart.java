package ai.mirofish.mirasim;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class VendorAutostart {
    private VendorAutostart() {
    }

    private static final class Candidate {
        final String cls;
        final String pkg;

        Candidate(String str, String str2) {
            this.pkg = str;
            this.cls = str2;
        }
    }

    private static List<Candidate> candidatesFor(String str) {
        ArrayList arrayList = new ArrayList();
        str.hashCode();
        switch (str) {
            case "oneplus":
                arrayList.add(new Candidate("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity"));
                break;
            case "huawei":
            case "honor":
                arrayList.add(new Candidate("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"));
                arrayList.add(new Candidate("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                break;
            case "realme":
            case "oppo":
                arrayList.add(new Candidate("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                arrayList.add(new Candidate("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity"));
                arrayList.add(new Candidate("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity"));
                break;
            case "xiaomi":
            case "poco":
            case "redmi":
                arrayList.add(new Candidate("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                break;
            case "asus":
                arrayList.add(new Candidate("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity"));
                arrayList.add(new Candidate("com.asus.mobilemanager", "com.asus.mobilemanager.powersaver.PowerSaverSettings"));
                break;
            case "iqoo":
            case "vivo":
                arrayList.add(new Candidate("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                arrayList.add(new Candidate("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                arrayList.add(new Candidate("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"));
                break;
            case "letv":
                arrayList.add(new Candidate("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                break;
            case "nokia":
                arrayList.add(new Candidate("com.evenwell.powersaving.g3", "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"));
                break;
            case "samsung":
                arrayList.add(new Candidate("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.usage.CheckableAppListActivity"));
                arrayList.add(new Candidate("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity"));
                arrayList.add(new Candidate("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"));
                break;
        }
        return arrayList;
    }

    private static String brand() {
        return (Build.MANUFACTURER == null ? "" : Build.MANUFACTURER).toLowerCase(Locale.ROOT).trim();
    }

    static Intent resolve(Context context) {
        PackageManager packageManager = context.getPackageManager();
        for (Candidate candidate : candidatesFor(brand())) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(candidate.pkg, candidate.cls));
            if (packageManager.resolveActivity(intent, 0) != null) {
                return intent;
            }
        }
        return null;
    }
}
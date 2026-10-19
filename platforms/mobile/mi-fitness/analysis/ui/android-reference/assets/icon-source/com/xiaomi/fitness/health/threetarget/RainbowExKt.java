package com.xiaomi.fitness.health.threetarget;

import android.content.Context;
import com.xiaomi.fit.fitness.export.data.aggregation.DailyGoalReport;
import com.xiaomi.fit.fitness.export.data.aggregation.GoalItem;
import com.xiaomi.fitness.RTLUtil;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import com.xiaomi.fitness.health.threetarget.model.TargetDetailModel;
import defpackage.i9k;
import defpackage.qmk;
import defpackage.xjr;
import defpackage.zjk;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u0011\u0010\u0002\u001a\u00020\u0001*\u00020\u0000¢\u0006\u0004\b\u0002\u0010\u0003\u001a\u0011\u0010\u0005\u001a\u00020\u0004*\u00020\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u0011\u0010\b\u001a\u00020\u0007*\u00020\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u0019\u0010\f\u001a\u00020\u0007*\u00020\u00002\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\f\u0010\r\u001a#\u0010\u0010\u001a\u00020\u0007*\u00020\u00002\u0006\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\u000f\u001a\u00020\u000e¢\u0006\u0004\b\u0010\u0010\u0011\u001a\u0011\u0010\u0012\u001a\u00020\u0007*\u00020\u0000¢\u0006\u0004\b\u0012\u0010\t\u001a\u0011\u0010\u0014\u001a\u00020\u0013*\u00020\u0000¢\u0006\u0004\b\u0014\u0010\u0015\u001a\u0011\u0010\u0016\u001a\u00020\u000e*\u00020\u0000¢\u0006\u0004\b\u0016\u0010\u0017\u001a\u0011\u0010\u0018\u001a\u00020\u000e*\u00020\u0000¢\u0006\u0004\b\u0018\u0010\u0017\u001a\u0011\u0010\u0019\u001a\u00020\u000e*\u00020\u0000¢\u0006\u0004\b\u0019\u0010\u0017\u001a\u0011\u0010\u001a\u001a\u00020\u000e*\u00020\u0000¢\u0006\u0004\b\u001a\u0010\u0017\u001a\u0019\u0010\u001d\u001a\u00020\u001b*\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001b¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u0011\u0010 \u001a\u00020\u001f*\u00020\u001b¢\u0006\u0004\b \u0010!\u001a\u0011\u0010\"\u001a\u00020\u000e*\u00020\u001b¢\u0006\u0004\b\"\u0010#\u001a\u0011\u0010$\u001a\u00020\u0013*\u00020\u001b¢\u0006\u0004\b$\u0010%¨\u0006&"}, d2 = {"Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;", "", "getPercent", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;)F", "Lcom/xiaomi/fitness/health/threetarget/model/TargetDetailModel;", "createDetailModel", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;)Lcom/xiaomi/fitness/health/threetarget/model/TargetDetailModel;", "", "getTargetValueStr", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;)Ljava/lang/String;", "Landroid/content/Context;", "context", "getTargetModelName", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;Landroid/content/Context;)Ljava/lang/String;", "", "isShort", "getTargetOnlyName", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;Landroid/content/Context;Z)Ljava/lang/String;", "getTargetModelUnit", "", "getTargetImg", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;)I", "isSTepTarget", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/GoalItem;)Z", "isCalorieTarget", "isMHIntensityTarget", "isStandTarget", "Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;", "recentlyReport", "copyRecentlyReport", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;)Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;", "Lxjr;", "generateThreeCircleModel", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;)Lxjr;", "checkHasData", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;)Z", "getRainbowAchieveStatus", "(Lcom/xiaomi/fit/fitness/export/data/aggregation/DailyGoalReport;)I", "health_chinaProductRelease"}, k = 2, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nRainbowEx.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RainbowEx.kt\ncom/xiaomi/fitness/health/threetarget/RainbowExKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,167:1\n1761#2,3:168\n*S KotlinDebug\n*F\n+ 1 RainbowEx.kt\ncom/xiaomi/fitness/health/threetarget/RainbowExKt\n*L\n154#1:168,3\n*E\n"})
public final class RainbowExKt {
    public static final boolean checkHasData(@NotNull DailyGoalReport dailyGoalReport) {
        Intrinsics.checkNotNullParameter(dailyGoalReport, "<this>");
        List<GoalItem> listGoalList = dailyGoalReport.goalList();
        if ((listGoalList instanceof Collection) && listGoalList.isEmpty()) {
            return false;
        }
        Iterator<T> it = listGoalList.iterator();
        while (it.hasNext()) {
            if (((GoalItem) it.next()).getAchievedValue() > 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static final DailyGoalReport copyRecentlyReport(@NotNull DailyGoalReport dailyGoalReport, @NotNull DailyGoalReport recentlyReport) {
        Intrinsics.checkNotNullParameter(dailyGoalReport, "<this>");
        Intrinsics.checkNotNullParameter(recentlyReport, "recentlyReport");
        ArrayList arrayList = new ArrayList();
        for (GoalItem goalItem : recentlyReport.goalList()) {
            arrayList.add(new GoalItem(goalItem.getType(), goalItem.getGoalValue(), 0));
        }
        return new DailyGoalReport(dailyGoalReport.getTime(), arrayList);
    }

    @NotNull
    public static final TargetDetailModel createDetailModel(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        return new TargetDetailModel(goalItem.getType(), getTargetModelName(goalItem, ApplicationExtKt.getApplication()), getTargetValueStr(goalItem));
    }

    @NotNull
    public static final xjr generateThreeCircleModel(@NotNull DailyGoalReport dailyGoalReport) {
        float percent;
        int itemColor;
        float percent2;
        int itemColor2;
        Intrinsics.checkNotNullParameter(dailyGoalReport, "<this>");
        List<GoalItem> listGoalList = dailyGoalReport.goalList();
        float percent3 = 0.0f;
        int itemColor3 = -1;
        if (listGoalList.isEmpty()) {
            percent = 0.0f;
            itemColor = -1;
        } else {
            GoalItem goalItem = listGoalList.get(0);
            percent = getPercent(goalItem);
            itemColor = RainbowExExportKt.getItemColor(goalItem);
        }
        if (listGoalList.size() > 1) {
            GoalItem goalItem2 = listGoalList.get(1);
            percent2 = getPercent(goalItem2);
            itemColor2 = RainbowExExportKt.getItemColor(goalItem2);
        } else {
            percent2 = 0.0f;
            itemColor2 = -1;
        }
        if (listGoalList.size() > 2) {
            GoalItem goalItem3 = listGoalList.get(2);
            percent3 = getPercent(goalItem3);
            itemColor3 = RainbowExExportKt.getItemColor(goalItem3);
        }
        xjr xjrVarB = xjr.b(percent, percent2, percent3);
        xjrVarB.c(itemColor, itemColor2, itemColor3);
        Intrinsics.checkNotNull(xjrVarB);
        return xjrVarB;
    }

    public static final float getPercent(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        if (goalItem.getGoalValue() != 0) {
            return Math.min(1.0f, (goalItem.getAchievedValue() * 1.0f) / goalItem.getGoalValue());
        }
        return 0.0f;
    }

    public static final int getRainbowAchieveStatus(@NotNull DailyGoalReport dailyGoalReport) {
        Intrinsics.checkNotNullParameter(dailyGoalReport, "<this>");
        int achievedCount = dailyGoalReport.getAchievedCount();
        if (achievedCount != 0) {
            return (achievedCount == 1 || achievedCount == 2) ? 1 : 0;
        }
        return 2;
    }

    public static final int getTargetImg(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        int type = goalItem.getType();
        if (type == 1) {
            return i9k.z4;
        }
        if (type == 2) {
            return i9k.x4;
        }
        if (type != 3) {
            return type != 4 ? i9k.b2 : i9k.b2;
        }
        return i9k.y4;
    }

    @NotNull
    public static final String getTargetModelName(@NotNull GoalItem goalItem, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        Intrinsics.checkNotNullParameter(context, "context");
        int type = goalItem.getType();
        if (type == 1) {
            String string = context.getString(qmk.Lo);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            return string;
        }
        if (type == 2) {
            String string2 = context.getString(qmk.f13236io);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            return string2;
        }
        if (type == 3) {
            String string3 = context.getString(qmk.Jo);
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            return string3;
        }
        if (type != 4) {
            return "";
        }
        String string4 = context.getString(qmk.Go);
        Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
        return string4;
    }

    @NotNull
    public static final String getTargetModelUnit(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        int type = goalItem.getType();
        if (type == 1) {
            String quantityString = ApplicationExtKt.getApplication().getResources().getQuantityString(zjk.common_unit_step, goalItem.getAchievedValue());
            Intrinsics.checkNotNullExpressionValue(quantityString, "getQuantityString(...)");
            return quantityString;
        }
        if (type == 2) {
            String quantityString2 = ApplicationExtKt.getApplication().getResources().getQuantityString(zjk.common_unit_calorie, goalItem.getAchievedValue());
            Intrinsics.checkNotNullExpressionValue(quantityString2, "getQuantityString(...)");
            return quantityString2;
        }
        if (type == 3) {
            String quantityString3 = ApplicationExtKt.getApplication().getResources().getQuantityString(zjk.common_unit_rowing_machine_count, goalItem.getAchievedValue());
            Intrinsics.checkNotNullExpressionValue(quantityString3, "getQuantityString(...)");
            return quantityString3;
        }
        if (type != 4) {
            return "";
        }
        String quantityString4 = ApplicationExtKt.getApplication().getResources().getQuantityString(zjk.m2, goalItem.getAchievedValue());
        Intrinsics.checkNotNullExpressionValue(quantityString4, "getQuantityString(...)");
        return quantityString4;
    }

    @NotNull
    public static final String getTargetOnlyName(@NotNull GoalItem goalItem, @NotNull Context context, boolean z) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        Intrinsics.checkNotNullParameter(context, "context");
        int type = goalItem.getType();
        if (type == 1) {
            String string = context.getString(qmk.Kc);
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            return string;
        }
        if (type == 2) {
            String string2 = context.getString(qmk.Jc);
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            return string2;
        }
        if (type == 3) {
            String string3 = context.getString(qmk.Jo);
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            return string3;
        }
        if (type != 4) {
            return "";
        }
        if (z) {
            String string4 = context.getString(qmk.Eo);
            Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
            return string4;
        }
        String string5 = context.getString(qmk.Fo);
        Intrinsics.checkNotNullExpressionValue(string5, "getString(...)");
        return string5;
    }

    public static /* synthetic */ String getTargetOnlyName$default(GoalItem goalItem, Context context, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return getTargetOnlyName(goalItem, context, z);
    }

    @NotNull
    public static final String getTargetValueStr(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        if (RTLUtil.INSTANCE.isRtl(ApplicationExtKt.getApplication())) {
            return goalItem.getGoalValue() + "/" + goalItem.getAchievedValue();
        }
        return goalItem.getAchievedValue() + "/" + goalItem.getGoalValue();
    }

    public static final boolean isCalorieTarget(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        return goalItem.getType() == 2;
    }

    public static final boolean isMHIntensityTarget(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        return goalItem.getType() == 4;
    }

    public static final boolean isSTepTarget(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        return goalItem.getType() == 1;
    }

    public static final boolean isStandTarget(@NotNull GoalItem goalItem) {
        Intrinsics.checkNotNullParameter(goalItem, "<this>");
        return goalItem.getType() == 3;
    }
}
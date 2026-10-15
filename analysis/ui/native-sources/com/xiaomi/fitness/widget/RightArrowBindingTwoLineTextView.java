package com.xiaomi.fitness.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.miui.circulate.device.api.Column;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u0000 \n2\u00020\u0001:\u0001\nB'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\t¨\u0006\u000b"}, d2 = {"Lcom/xiaomi/fitness/widget/RightArrowBindingTwoLineTextView;", "Lcom/xiaomi/fitness/widget/RightArrowTwoLineTextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "Companion", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RightArrowBindingTwoLineTextView extends RightArrowTwoLineTextView {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0007J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0007J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0007J\u001a\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u000f\u001a\u0004\u0018\u00010\rH\u0007J\u0018\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\tH\u0007J\u0018\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\tH\u0007J\u001a\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\rH\u0007J\u001a\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007J\u001a\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\rH\u0007J\u001a\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0001\u0010\u0019\u001a\u00020\tH\u0007J\u001a\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\rH\u0007J\u001a\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0001\u0010\u0019\u001a\u00020\tH\u0007J\u0018\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0016H\u0007J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0016H\u0007¨\u0006\u001e"}, d2 = {"Lcom/xiaomi/fitness/widget/RightArrowBindingTwoLineTextView$Companion;", "", "<init>", "()V", "setIcon", "", "view", "Lcom/xiaomi/fitness/widget/RightArrowBindingTwoLineTextView;", "icon", "", "iconDrawable", "Landroid/graphics/drawable/Drawable;", "url", "", "setTitle", "title", "titleRes", "setSubtitle", "subtitleRes", Column.SUBTITLE, "showMore", "more", "", "remindText", "text", "textRes", "rightTextWithDot", "setEnable", "isEnabled", "setEnableDefault", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void showMore$default(Companion companion, RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = true;
            }
            companion.showMore(rightArrowBindingTwoLineTextView, z);
        }

        @JvmStatic
        public final void remindText(@NotNull RightArrowBindingTwoLineTextView view, @Nullable String text) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setRemindText(text);
        }

        @JvmStatic
        public final void rightTextWithDot(@NotNull RightArrowBindingTwoLineTextView view, @Nullable String text) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setRightTextWithDot(text);
        }

        @JvmStatic
        public final void setEnable(@NotNull RightArrowBindingTwoLineTextView view, boolean isEnabled) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setEnable(isEnabled);
        }

        @JvmStatic
        public final void setEnableDefault(@NotNull RightArrowBindingTwoLineTextView view, boolean isEnabled) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setDefaultEnable(isEnabled);
        }

        @JvmStatic
        public final void setIcon(@NotNull RightArrowBindingTwoLineTextView view, int icon) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setIcon(icon);
        }

        @JvmStatic
        public final void setSubtitle(@NotNull RightArrowBindingTwoLineTextView view, int subtitleRes) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setSubtitle(subtitleRes);
        }

        @JvmStatic
        public final void setTitle(@NotNull RightArrowBindingTwoLineTextView view, @Nullable String title) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setTitle(title);
        }

        @JvmStatic
        public final void showMore(@NotNull RightArrowBindingTwoLineTextView view, boolean more) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.showMore(more);
        }

        private Companion() {
        }

        @JvmStatic
        public final void remindText(@NotNull RightArrowBindingTwoLineTextView view, int textRes) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setRemindText(textRes);
        }

        @JvmStatic
        public final void rightTextWithDot(@NotNull RightArrowBindingTwoLineTextView view, int textRes) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setRightTextWithDot(textRes);
        }

        @JvmStatic
        public final void setIcon(@NotNull RightArrowBindingTwoLineTextView view, @Nullable Drawable iconDrawable) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setIcon(iconDrawable);
        }

        @JvmStatic
        public final void setSubtitle(@NotNull RightArrowBindingTwoLineTextView view, @Nullable String subtitle) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setSubtitle(subtitle);
        }

        @JvmStatic
        public final void setTitle(@NotNull RightArrowBindingTwoLineTextView view, int titleRes) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setTitle(titleRes);
        }

        @JvmStatic
        public final void setIcon(@NotNull RightArrowBindingTwoLineTextView view, @Nullable String url) {
            Intrinsics.checkNotNullParameter(view, "view");
            view.setIcon(url);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowBindingTwoLineTextView(@NotNull Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @JvmStatic
    public static final void remindText(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, int i) {
        INSTANCE.remindText(rightArrowBindingTwoLineTextView, i);
    }

    @JvmStatic
    public static final void rightTextWithDot(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, int i) {
        INSTANCE.rightTextWithDot(rightArrowBindingTwoLineTextView, i);
    }

    @JvmStatic
    public static final void setEnable(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, boolean z) {
        INSTANCE.setEnable(rightArrowBindingTwoLineTextView, z);
    }

    @JvmStatic
    public static final void setEnableDefault(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, boolean z) {
        INSTANCE.setEnableDefault(rightArrowBindingTwoLineTextView, z);
    }

    @JvmStatic
    public static final void setIcon(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, int i) {
        INSTANCE.setIcon(rightArrowBindingTwoLineTextView, i);
    }

    @JvmStatic
    public static final void setSubtitle(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, int i) {
        INSTANCE.setSubtitle(rightArrowBindingTwoLineTextView, i);
    }

    @JvmStatic
    public static final void setTitle(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, int i) {
        INSTANCE.setTitle(rightArrowBindingTwoLineTextView, i);
    }

    @JvmStatic
    public static final void showMore(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, boolean z) {
        INSTANCE.showMore(rightArrowBindingTwoLineTextView, z);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowBindingTwoLineTextView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @JvmStatic
    public static final void remindText(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable String str) {
        INSTANCE.remindText(rightArrowBindingTwoLineTextView, str);
    }

    @JvmStatic
    public static final void rightTextWithDot(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable String str) {
        INSTANCE.rightTextWithDot(rightArrowBindingTwoLineTextView, str);
    }

    @JvmStatic
    public static final void setIcon(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable Drawable drawable) {
        INSTANCE.setIcon(rightArrowBindingTwoLineTextView, drawable);
    }

    @JvmStatic
    public static final void setSubtitle(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable String str) {
        INSTANCE.setSubtitle(rightArrowBindingTwoLineTextView, str);
    }

    @JvmStatic
    public static final void setTitle(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable String str) {
        INSTANCE.setTitle(rightArrowBindingTwoLineTextView, str);
    }

    public /* synthetic */ RightArrowBindingTwoLineTextView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    @JvmStatic
    public static final void setIcon(@NotNull RightArrowBindingTwoLineTextView rightArrowBindingTwoLineTextView, @Nullable String str) {
        INSTANCE.setIcon(rightArrowBindingTwoLineTextView, str);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowBindingTwoLineTextView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
    }
}
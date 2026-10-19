package com.xiaomi.fitness.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import coil.transform.CircleCropTransformation;
import coil.transform.RoundedCornersTransformation;
import com.mibi.sdk.component.Constants;
import com.miui.tsmclient.net.TSMAuthContants;
import com.xiaomi.fitness.extensions.ViewExtKt;
import defpackage.b6k;
import defpackage.iek;
import defpackage.jak;
import defpackage.qrk;
import defpackage.r7k;
import defpackage.vhk;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\"\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017J\u000e\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0007J\u0010\u0010\u0014\u001a\u00020\u00152\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\u000e\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u0013J\u0010\u0010\u0014\u001a\u00020\u00152\b\b\u0001\u0010!\u001a\u00020\u0007J\u0010\u0010\"\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017J\u0010\u0010\"\u001a\u00020\u00152\b\b\u0001\u0010!\u001a\u00020\u0007J\u0018\u0010#\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010$\u001a\u00020\u001aJ\u0018\u0010#\u001a\u00020\u00152\b\b\u0001\u0010!\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u001aJ\u0010\u0010%\u001a\u00020\u00152\b\u0010&\u001a\u0004\u0018\u00010\u001eJ\u0010\u0010%\u001a\u00020\u00152\b\b\u0001\u0010'\u001a\u00020\u0007J\u000e\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\u0007J\u000e\u0010*\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u0013J\u000e\u0010,\u001a\u00020\u00152\u0006\u0010-\u001a\u00020\u0013J\u000e\u0010.\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0007J\u0010\u0010/\u001a\u00020\u00152\b\u00100\u001a\u0004\u0018\u00010\u001eJ\u0010\u0010/\u001a\u00020\u00152\b\b\u0001\u00101\u001a\u00020\u0007J\u0010\u0010/\u001a\u00020\u00152\b\u00100\u001a\u0004\u0018\u000102J\u0010\u00103\u001a\u00020\u00152\b\u00104\u001a\u0004\u0018\u00010\u001eJ\u000e\u00105\u001a\u00020\u00152\u0006\u00106\u001a\u00020\u001aJ\u0010\u00103\u001a\u00020\u00152\b\b\u0001\u00101\u001a\u00020\u0007J\u0010\u00107\u001a\u00020\u00152\b\b\u0001\u00108\u001a\u00020\u0007J\u0010\u00109\u001a\u00020\u00152\b\b\u0001\u0010:\u001a\u00020\u0007J\u000e\u0010;\u001a\u00020\u00152\u0006\u0010<\u001a\u00020\u0013J\u000e\u0010=\u001a\u00020\u00152\u0006\u0010=\u001a\u00020\u0013J\u0010\u0010>\u001a\u00020\u00152\b\u0010?\u001a\u0004\u0018\u00010\u0017J\u0010\u0010@\u001a\u00020\u00152\b\u0010A\u001a\u0004\u0018\u00010\u001eJ\u000e\u0010B\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0007J\u0010\u0010C\u001a\u00020\u00152\b\b\u0001\u0010:\u001a\u00020\u0007J\u000e\u0010D\u001a\u00020\u00152\u0006\u0010E\u001a\u00020FJ\u0010\u0010G\u001a\u00020\u00152\u0006\u0010H\u001a\u00020\u0013H\u0016J&\u0010I\u001a\u00020\u00152\u0006\u0010J\u001a\u00020\u00072\u0006\u0010K\u001a\u00020\u00072\u0006\u0010L\u001a\u00020\u00072\u0006\u0010M\u001a\u00020\u0007J\u0006\u0010N\u001a\u00020\rJ;\u0010O\u001a\u00020\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00172\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010P\u001a\u0004\u0018\u00010Q2\n\b\u0002\u0010R\u001a\u0004\u0018\u00010\u001e¢\u0006\u0002\u0010SJ\u000e\u0010T\u001a\u00020\u00152\u0006\u0010U\u001a\u00020\u0013J\u0012\u0010V\u001a\u00020\u00132\b\u0010W\u001a\u0004\u0018\u00010XH\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006Y"}, d2 = {"Lcom/xiaomi/fitness/widget/RightArrowSingleLineTextView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mIvFeatureItemStartIcon", "Landroid/widget/ImageView;", "mTvFeatureItemTitle", "Landroid/widget/TextView;", "mTvFeatureItemTitleRightIcon", "mTvFeatureItemRemind", "mIvFeatureItemMore", "mTvFeatureItemRightText", "allowClickWhenDisabled", "", "setIcon", "", "icon", "Landroid/graphics/drawable/Drawable;", "iconCircle", "iconCorners", "", "setRemindTextColor", "color", "url", "", "setStartIconVisible", "visible", "iconRes", "setCircleIcon", "setRoundedIcon", "corner", "setTitle", "title", "titleRes", "updateTitleMaxLines", "lines", "setRemindTextEnable", "isEnabled", "setTitleBold", "isBold", "setTitleTextColor", "setRemindText", "remindText", "remindTextRes", "Landroid/text/SpannableString;", "setRightTextWithDot", "rightText", "setRightTextSize", "rightSize", "setRemindDotColor", "remindDotColor", "setRemindDotDrawable", "drawable", "showMore", "more", "showRightIcon", "showRightIconBG", "rightIconBG", "showRightIconText", "rightIconText", "setRightIconTextColor", "setEndButtonIcon", "setEndButton", "btn", "Landroid/view/View;", "setEnable", "enable", "setViewPadding", "left", "top", TSMAuthContants.PARAM_KEY_RIGHT, "bottom", "getTvFeatureItemRemind", "setArrowViewPadding", "iconDrawable", "Landroid/graphics/drawable/Icon;", Constants.KEY_RECHARGE_ICON_URL, "(Landroid/graphics/drawable/Drawable;Ljava/lang/Integer;Landroid/graphics/drawable/Icon;Ljava/lang/String;)V", "setDefaultEnable", "enabled", "onTouchEvent", "event", "Landroid/view/MotionEvent;", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nRightArrowSingleLineTextView.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RightArrowSingleLineTextView.kt\ncom/xiaomi/fitness/widget/RightArrowSingleLineTextView\n+ 2 Extensions.kt\ncoil/-SingletonExtensions\n+ 3 Extensions.kt\ncoil/-SingletonExtensions$load$1\n*L\n1#1,378:1\n54#2,3:379\n24#2:382\n57#2,6:383\n63#2,2:390\n54#2,3:392\n24#2:395\n59#2,6:396\n54#2,3:402\n24#2:405\n59#2,6:406\n54#2,3:412\n24#2:415\n59#2,6:416\n54#2,3:422\n24#2:425\n59#2,6:426\n57#3:389\n*S KotlinDebug\n*F\n+ 1 RightArrowSingleLineTextView.kt\ncom/xiaomi/fitness/widget/RightArrowSingleLineTextView\n*L\n151#1:379,3\n151#1:382\n151#1:383,6\n151#1:390,2\n167#1:392,3\n167#1:395\n167#1:396,6\n175#1:402,3\n175#1:405\n175#1:406,6\n183#1:412,3\n183#1:415\n183#1:416,6\n191#1:422,3\n191#1:425\n191#1:426,6\n151#1:389\n*E\n"})
public class RightArrowSingleLineTextView extends LinearLayout {
    private boolean allowClickWhenDisabled;

    @NotNull
    private final ImageView mIvFeatureItemMore;

    @NotNull
    private final ImageView mIvFeatureItemStartIcon;

    @NotNull
    private final TextView mTvFeatureItemRemind;

    @NotNull
    private final TextView mTvFeatureItemRightText;

    @NotNull
    private final TextView mTvFeatureItemTitle;

    @NotNull
    private final TextView mTvFeatureItemTitleRightIcon;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowSingleLineTextView(@NotNull Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static /* synthetic */ void setArrowViewPadding$default(RightArrowSingleLineTextView rightArrowSingleLineTextView, Drawable drawable, Integer num, Icon icon, String str, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setArrowViewPadding");
        }
        if ((i & 1) != 0) {
            drawable = null;
        }
        if ((i & 2) != 0) {
            num = -1;
        }
        if ((i & 4) != 0) {
            icon = null;
        }
        if ((i & 8) != 0) {
            str = null;
        }
        rightArrowSingleLineTextView.setArrowViewPadding(drawable, num, icon, str);
    }

    private final void setIcon(Drawable icon, boolean iconCircle, float iconCorners) {
        setArrowViewPadding$default(this, icon, null, null, null, 14, null);
        if (icon == null) {
            ViewExtKt.gone(this.mIvFeatureItemStartIcon);
            return;
        }
        if (iconCircle) {
            setCircleIcon(icon);
        } else if (iconCorners > 0.0f) {
            setRoundedIcon(icon, iconCorners);
        } else {
            setIcon(icon);
        }
    }

    @NotNull
    /* JADX INFO: renamed from: getTvFeatureItemRemind, reason: from getter */
    public final TextView getMTvFeatureItemRemind() {
        return this.mTvFeatureItemRemind;
    }

    @Override // android.view.View
    public boolean onTouchEvent(@Nullable MotionEvent event) {
        if (isEnabled() || !this.allowClickWhenDisabled) {
            return super.onTouchEvent(event);
        }
        if (event != null && event.getAction() == 1) {
            performClick();
        }
        return true;
    }

    public final void setArrowViewPadding(@Nullable Drawable icon, @Nullable Integer iconRes, @Nullable Icon iconDrawable, @Nullable String iconUrl) {
    }

    public final void setCircleIcon(@Nullable Drawable icon) {
        setArrowViewPadding$default(this, icon, null, null, null, 14, null);
        ViewExtKt.setVisible(this.mIvFeatureItemStartIcon, icon != null);
        ImageView imageView = this.mIvFeatureItemStartIcon;
        ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
        ImageRequest.Builder builderTarget = new ImageRequest.Builder(imageView.getContext()).data(icon).target(imageView);
        builderTarget.transformations(new CircleCropTransformation());
        imageLoader.enqueue(builderTarget.build());
    }

    public final void setDefaultEnable(boolean enabled) {
        setEnable(enabled);
        this.mIvFeatureItemStartIcon.setAlpha(enabled ? 1.0f : 0.25f);
    }

    public void setEnable(boolean enable) {
        setEnabled(enable);
        this.mIvFeatureItemMore.setEnabled(enable);
        this.mIvFeatureItemStartIcon.setEnabled(enable);
        this.mTvFeatureItemRemind.setEnabled(enable);
        this.mTvFeatureItemTitle.setEnabled(enable);
    }

    public final void setEndButton(@NotNull View btn) {
        Intrinsics.checkNotNullParameter(btn, "btn");
        removeView(this.mIvFeatureItemMore);
        addView(btn);
    }

    public final void setEndButtonIcon(int drawable) {
        this.mIvFeatureItemMore.setImageResource(drawable);
    }

    public final void setRemindDotColor(int remindDotColor) {
        ViewExtKt.visible(this.mTvFeatureItemRemind);
        int dimen = (int) ViewExtKt.getDimen(this, r7k.d);
        ViewExtKt.setWidthAndHeight(this.mTvFeatureItemRemind, dimen, dimen);
        Drawable drawable = ViewExtKt.getDrawable(this, jak.a);
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(remindDotColor);
        }
        this.mTvFeatureItemRemind.setBackground(drawable);
    }

    public final void setRemindDotDrawable(int drawable) {
        ViewExtKt.visible(this.mTvFeatureItemRemind);
        int dimen = (int) ViewExtKt.getDimen(this, r7k.d);
        ViewExtKt.setWidthAndHeight(this.mTvFeatureItemRemind, dimen, dimen);
        this.mTvFeatureItemRemind.setBackground(ViewExtKt.getDrawable(this, drawable));
    }

    public final void setRemindText(@Nullable String remindText) {
        if (remindText == null || remindText.length() == 0) {
            ViewExtKt.gone(this.mTvFeatureItemRemind);
            return;
        }
        this.mTvFeatureItemRemind.setBackground(null);
        ViewExtKt.visible(this.mTvFeatureItemRemind);
        ViewExtKt.setWidthAndHeight(this.mTvFeatureItemRemind, -2, -2);
        this.mTvFeatureItemRemind.setText(remindText);
    }

    public final void setRemindTextColor(int color) {
        this.mTvFeatureItemRemind.setTextColor(color);
    }

    public final void setRemindTextEnable(boolean isEnabled) {
        this.mTvFeatureItemRemind.setEnabled(isEnabled);
    }

    public final void setRightIconTextColor(int color) {
        this.mTvFeatureItemTitleRightIcon.setTextColor(color);
    }

    public final void setRightTextSize(float rightSize) {
        this.mTvFeatureItemRightText.setTextSize(rightSize);
    }

    public final void setRightTextWithDot(@Nullable String rightText) {
        if (rightText != null && rightText.length() != 0) {
            ViewExtKt.visible(this.mTvFeatureItemRightText);
            this.mTvFeatureItemRightText.setText(rightText);
            setRemindDotColor(ViewExtKt.getColor(this, b6k.d));
        } else {
            ViewExtKt.gone(this.mTvFeatureItemRightText);
            if (this.mTvFeatureItemRemind.getText().toString().length() == 0) {
                ViewExtKt.gone(this.mTvFeatureItemRemind);
            }
        }
    }

    public final void setRoundedIcon(@Nullable Drawable icon, float corner) {
        setArrowViewPadding$default(this, icon, null, null, null, 14, null);
        ViewExtKt.setVisible(this.mIvFeatureItemStartIcon, icon != null);
        ImageView imageView = this.mIvFeatureItemStartIcon;
        ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
        ImageRequest.Builder builderTarget = new ImageRequest.Builder(imageView.getContext()).data(icon).target(imageView);
        builderTarget.transformations(new RoundedCornersTransformation(corner));
        imageLoader.enqueue(builderTarget.build());
    }

    public final void setStartIconVisible(boolean visible) {
        ViewExtKt.setVisible(this.mIvFeatureItemStartIcon, visible);
    }

    public final void setTitle(@Nullable String title) {
        this.mTvFeatureItemTitle.setText(title);
    }

    public final void setTitleBold(boolean isBold) {
        if (isBold) {
            this.mTvFeatureItemTitle.setTypeface(Typeface.defaultFromStyle(1));
        }
    }

    public final void setTitleTextColor(int color) {
        this.mTvFeatureItemTitle.setTextColor(color);
    }

    public final void setViewPadding(int left, int top, int right, int bottom) {
        setPadding(left, top, right, bottom);
    }

    public final void showMore(boolean more) {
        ViewExtKt.setVisible(this.mIvFeatureItemMore, more);
    }

    public final void showRightIcon(boolean showRightIcon) {
        ViewExtKt.setVisible(this.mTvFeatureItemTitleRightIcon, showRightIcon);
    }

    public final void showRightIconBG(@Nullable Drawable rightIconBG) {
        this.mTvFeatureItemTitleRightIcon.setBackground(rightIconBG);
    }

    public final void showRightIconText(@Nullable String rightIconText) {
        this.mTvFeatureItemTitleRightIcon.setText(rightIconText);
    }

    public final void updateTitleMaxLines(int lines) {
        this.mTvFeatureItemTitle.setSingleLine(false);
        this.mTvFeatureItemTitle.setMaxLines(lines);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowSingleLineTextView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public final void setTitle(int titleRes) {
        this.mTvFeatureItemTitle.setText(titleRes);
    }

    public /* synthetic */ RightArrowSingleLineTextView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public RightArrowSingleLineTextView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, qrk.P3);
        Intrinsics.checkNotNullExpressionValue(typedArrayObtainStyledAttributes, "obtainStyledAttributes(...)");
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(qrk.R3);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(qrk.S3, false);
        float f = typedArrayObtainStyledAttributes.getFloat(qrk.T3, 0.0f);
        String string = typedArrayObtainStyledAttributes.getString(qrk.e4);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(qrk.b4, false);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(qrk.c4, false);
        Drawable drawable2 = typedArrayObtainStyledAttributes.getDrawable(qrk.d4);
        boolean z4 = typedArrayObtainStyledAttributes.getBoolean(qrk.a4, true);
        int color = typedArrayObtainStyledAttributes.getColor(qrk.U3, ViewExtKt.getColor(this, b6k.d));
        int color2 = typedArrayObtainStyledAttributes.getColor(qrk.Y3, ViewExtKt.getColor(this, b6k.l));
        boolean z5 = typedArrayObtainStyledAttributes.getBoolean(qrk.W3, true);
        boolean z6 = typedArrayObtainStyledAttributes.getBoolean(qrk.Z3, true);
        boolean z7 = typedArrayObtainStyledAttributes.getBoolean(qrk.f4, false);
        int integer = typedArrayObtainStyledAttributes.getInteger(qrk.g4, -1);
        String string2 = typedArrayObtainStyledAttributes.getString(qrk.V3);
        typedArrayObtainStyledAttributes.getString(qrk.X3);
        this.allowClickWhenDisabled = typedArrayObtainStyledAttributes.getBoolean(qrk.Q3, false);
        typedArrayObtainStyledAttributes.recycle();
        setOrientation(0);
        setGravity(16);
        View viewInflate = LayoutInflater.from(context).inflate(vhk.h, (ViewGroup) this, true);
        View viewFindViewById = viewInflate.findViewById(iek.t);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        this.mIvFeatureItemStartIcon = (ImageView) viewFindViewById;
        View viewFindViewById2 = viewInflate.findViewById(iek.O);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
        TextView textView = (TextView) viewFindViewById2;
        this.mTvFeatureItemTitle = textView;
        View viewFindViewById3 = viewInflate.findViewById(iek.P);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
        this.mTvFeatureItemTitleRightIcon = (TextView) viewFindViewById3;
        View viewFindViewById4 = viewInflate.findViewById(iek.L);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById4, "findViewById(...)");
        TextView textView2 = (TextView) viewFindViewById4;
        this.mTvFeatureItemRemind = textView2;
        View viewFindViewById5 = viewInflate.findViewById(iek.s);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById5, "findViewById(...)");
        this.mIvFeatureItemMore = (ImageView) viewFindViewById5;
        View viewFindViewById6 = viewInflate.findViewById(iek.M);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById6, "findViewById(...)");
        this.mTvFeatureItemRightText = (TextView) viewFindViewById6;
        setArrowViewPadding$default(this, drawable, null, null, null, 14, null);
        setIcon(drawable, z, f);
        setTitle(string);
        if (integer > 0) {
            textView.setMaxLines(integer);
        }
        showMore(z4);
        if (z3) {
            showRightIcon(z3);
            showRightIconBG(drawable2);
            setRightIconTextColor(color2);
        }
        if (z6) {
            viewInflate.setBackgroundResource(jak.h);
        }
        setRemindTextEnable(z5);
        setTitleBold(z7);
        if (z2) {
            if (string2 != null && string2.length() != 0) {
                setRemindText(string2);
                setRemindTextEnable(z2);
                return;
            } else {
                setRemindDotColor(color);
                return;
            }
        }
        ViewExtKt.gone(textView2);
    }

    public final void setIcon(@Nullable Drawable icon) {
        setArrowViewPadding$default(this, icon, null, null, null, 14, null);
        ViewExtKt.setVisible(this.mIvFeatureItemStartIcon, icon != null);
        this.mIvFeatureItemStartIcon.setImageDrawable(icon);
    }

    public final void setRemindText(int remindTextRes) {
        ViewExtKt.visible(this.mTvFeatureItemRemind);
        ViewExtKt.setWidthAndHeight(this.mTvFeatureItemRemind, -2, -2);
        this.mTvFeatureItemRemind.setText(remindTextRes);
    }

    public final void setRightTextWithDot(int remindTextRes) {
        ViewExtKt.visible(this.mTvFeatureItemRightText);
        this.mTvFeatureItemRightText.setText(remindTextRes);
        setRemindDotColor(ViewExtKt.getColor(this, b6k.d));
    }

    public final void setIcon(@Nullable String url) {
        setArrowViewPadding$default(this, null, null, null, url, 7, null);
        ViewExtKt.setVisible(this.mIvFeatureItemStartIcon, true ^ (url == null || url.length() == 0));
        ImageView imageView = this.mIvFeatureItemStartIcon;
        Coil.imageLoader(imageView.getContext()).enqueue(new ImageRequest.Builder(imageView.getContext()).data(url).target(imageView).build());
    }

    public final void setRemindText(@Nullable SpannableString remindText) {
        if (remindText != null && remindText.length() != 0) {
            this.mTvFeatureItemRemind.setBackground(null);
            ViewExtKt.visible(this.mTvFeatureItemRemind);
            ViewExtKt.setWidthAndHeight(this.mTvFeatureItemRemind, -2, -2);
            this.mTvFeatureItemRemind.setText(remindText);
            return;
        }
        ViewExtKt.gone(this.mTvFeatureItemRemind);
    }

    public final void setCircleIcon(int iconRes) {
        setArrowViewPadding$default(this, null, Integer.valueOf(iconRes), null, null, 13, null);
        ViewExtKt.visible(this.mIvFeatureItemStartIcon);
        ImageView imageView = this.mIvFeatureItemStartIcon;
        Integer numValueOf = Integer.valueOf(iconRes);
        ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
        ImageRequest.Builder builderTarget = new ImageRequest.Builder(imageView.getContext()).data(numValueOf).target(imageView);
        builderTarget.transformations(new CircleCropTransformation());
        imageLoader.enqueue(builderTarget.build());
    }

    public final void setRoundedIcon(int iconRes, float corner) {
        setArrowViewPadding$default(this, null, Integer.valueOf(iconRes), null, null, 13, null);
        ViewExtKt.visible(this.mIvFeatureItemStartIcon);
        ImageView imageView = this.mIvFeatureItemStartIcon;
        Integer numValueOf = Integer.valueOf(iconRes);
        ImageLoader imageLoader = Coil.imageLoader(imageView.getContext());
        ImageRequest.Builder builderTarget = new ImageRequest.Builder(imageView.getContext()).data(numValueOf).target(imageView);
        builderTarget.transformations(new RoundedCornersTransformation(corner));
        imageLoader.enqueue(builderTarget.build());
    }

    public final void setIcon(int iconRes) {
        setArrowViewPadding$default(this, null, Integer.valueOf(iconRes), null, null, 13, null);
        ViewExtKt.visible(this.mIvFeatureItemStartIcon);
        this.mIvFeatureItemStartIcon.setImageResource(iconRes);
    }
}
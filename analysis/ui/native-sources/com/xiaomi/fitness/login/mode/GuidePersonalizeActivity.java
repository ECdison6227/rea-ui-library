package com.xiaomi.fitness.login.mode;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.imageview.ShapeableImageView;
import com.xiaomi.fitness.common.utils.RomUtils;
import com.xiaomi.fitness.common.utils.SettingsPropertiesUtil;
import com.xiaomi.fitness.login.preference.ModePreference;
import com.xiaomi.fitness.main.export.MainExtKt;
import com.xiaomi.fitness.settingitem.DeviceSettingConfigKey;
import defpackage.fdk;
import defpackage.l9k;
import defpackage.qgk;
import defpackage.r6c;
import defpackage.s6c;
import defpackage.tmk;
import defpackage.z4k;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\u00020\u0002:\u0001\u001cB\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\u000eH\u0016J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0006H\u0002J\u0018\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u001bH\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/xiaomi/fitness/login/mode/GuidePersonalizeActivity;", "Lcom/xiaomi/fitness/baseui/view/BaseActivity;", "Landroid/view/View$OnClickListener;", "<init>", "()V", "layoutId", "", "getLayoutId", "()I", "mSelectorMode", "mPersonalizeTextView", "Landroid/widget/TextView;", "mConciseTextView", "mSelectorConcise", "Landroid/view/View;", "mSelectorPersonal", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onClick", "v", DeviceSettingConfigKey.KEY_SELECT_MODE, "mode", "onSaveInstanceState", "outState", "outPersistentState", "Landroid/os/PersistableBundle;", "Companion", "login_chinaProductRelease"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GuidePersonalizeActivity extends Hilt_GuidePersonalizeActivity implements View.OnClickListener {
    private static final int CONCISE_THEME = 2;
    private static final int DEFAULT_THEME;

    @NotNull
    private static final String KEY_SELECT = "key_select";
    private static final float TEXTVIEW_SIZE = 14.0f;
    private static final int VITALITY_THEME = 1;
    private TextView mConciseTextView;
    private TextView mPersonalizeTextView;
    private View mSelectorConcise;
    private int mSelectorMode = DEFAULT_THEME;
    private View mSelectorPersonal;

    static {
        RomUtils romUtils = RomUtils.INSTANCE;
        DEFAULT_THEME = (romUtils.isMiuiLite() || romUtils.getTotalRam() <= 6) ? 2 : 1;
    }

    private static final void onCreate$lambda$0(GuidePersonalizeActivity guidePersonalizeActivity, View view) {
        guidePersonalizeActivity.selectMode(1);
    }

    private static final void onCreate$lambda$1(GuidePersonalizeActivity guidePersonalizeActivity, View view) {
        guidePersonalizeActivity.selectMode(2);
    }

    public static /* synthetic */ void s(GuidePersonalizeActivity guidePersonalizeActivity, View view) {
        onCreate$lambda$0(guidePersonalizeActivity, view);
    }

    private final void selectMode(int mode) {
        View view = null;
        if (mode == 1) {
            TextView textView = this.mPersonalizeTextView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mPersonalizeTextView");
                textView = null;
            }
            textView.setSelected(true);
            TextView textView2 = this.mConciseTextView;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConciseTextView");
                textView2 = null;
            }
            textView2.setSelected(false);
            View view2 = this.mSelectorConcise;
            if (view2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSelectorConcise");
                view2 = null;
            }
            view2.setBackgroundResource(l9k.a);
            View view3 = this.mSelectorPersonal;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSelectorPersonal");
                view3 = null;
            }
            view3.setBackgroundResource(l9k.f10558b);
            this.mSelectorMode = 1;
        } else {
            TextView textView3 = this.mPersonalizeTextView;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mPersonalizeTextView");
                textView3 = null;
            }
            textView3.setSelected(false);
            TextView textView4 = this.mConciseTextView;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mConciseTextView");
                textView4 = null;
            }
            textView4.setSelected(true);
            View view4 = this.mSelectorConcise;
            if (view4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSelectorConcise");
                view4 = null;
            }
            view4.setBackgroundResource(l9k.f10558b);
            View view5 = this.mSelectorPersonal;
            if (view5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSelectorPersonal");
                view5 = null;
            }
            view5.setBackgroundResource(l9k.a);
            this.mSelectorMode = 2;
        }
        View view6 = this.mSelectorConcise;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSelectorConcise");
            view6 = null;
        }
        view6.setPadding(20, 20, 20, 20);
        View view7 = this.mSelectorPersonal;
        if (view7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSelectorPersonal");
        } else {
            view = view7;
        }
        view.setPadding(20, 20, 20, 20);
    }

    public static /* synthetic */ void t(GuidePersonalizeActivity guidePersonalizeActivity, View view) {
        onCreate$lambda$1(guidePersonalizeActivity, view);
    }

    @Override // com.xiaomi.fitness.baseui.ViewCreator
    /* JADX INFO: renamed from: getLayoutId */
    public int getLayoutRes() {
        return qgk.a;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(@Nullable View v) {
        ModePreference.INSTANCE.setSelectMode(this.mSelectorMode);
        MainExtKt.showMainActivity$default(this, null, null, 6, null);
        finish();
    }

    @Override // com.xiaomi.fitness.login.mode.Hilt_GuidePersonalizeActivity, com.xiaomi.fitness.baseui.view.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SettingsPropertiesUtil.INSTANCE.isExpand(this)) {
            setActionBarExpandState(0);
        } else {
            setActionBarExpandState(1);
        }
        setTitle(getString(tmk.W));
        hideBack();
        setTitleBackground(z4k.g);
        setEndTextView(fdk.R, tmk.i, null, 14.0f, this);
        ((TextView) findViewById(fdk.m)).setOnClickListener(this);
        View viewFindViewById = findViewById(fdk.U);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        ShapeableImageView shapeableImageView = (ShapeableImageView) viewFindViewById;
        View viewFindViewById2 = findViewById(fdk.p);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "findViewById(...)");
        ShapeableImageView shapeableImageView2 = (ShapeableImageView) viewFindViewById2;
        View viewFindViewById3 = findViewById(fdk.V);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "findViewById(...)");
        this.mPersonalizeTextView = (TextView) viewFindViewById3;
        View viewFindViewById4 = findViewById(fdk.o);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById4, "findViewById(...)");
        this.mSelectorConcise = viewFindViewById4;
        View viewFindViewById5 = findViewById(fdk.T);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById5, "findViewById(...)");
        this.mSelectorPersonal = viewFindViewById5;
        View viewFindViewById6 = findViewById(fdk.q);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById6, "findViewById(...)");
        this.mConciseTextView = (TextView) viewFindViewById6;
        Integer numValueOf = savedInstanceState != null ? Integer.valueOf(savedInstanceState.getInt("key_select", DEFAULT_THEME)) : null;
        selectMode(numValueOf != null ? numValueOf.intValue() : DEFAULT_THEME);
        shapeableImageView.setOnClickListener(new r6c(this));
        shapeableImageView2.setOnClickListener(new s6c(this));
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(@NotNull Bundle outState, @NotNull PersistableBundle outPersistentState) {
        Intrinsics.checkNotNullParameter(outState, "outState");
        Intrinsics.checkNotNullParameter(outPersistentState, "outPersistentState");
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("key_select", this.mSelectorMode);
    }
}
package com.xiaomi.fitness.homepage.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.xiaomi.fitness.common.extensions.ResourceExtKt;
import com.xiaomi.fitness.common.utils.AppUtil;
import com.xiaomi.fitness.common.utils.DisplayUtil;
import defpackage.cdk;
import defpackage.i9k;
import defpackage.ogk;

/* JADX INFO: loaded from: classes10.dex */
public class DataHeaderTargetItemV2View extends ConstraintLayout {
    public TextView D;
    public TextView E;
    public TextView F;

    public DataHeaderTargetItemV2View(Context context) {
        super(context);
        p(context);
    }

    private void p(Context context) {
        LayoutInflater.from(context).inflate(ogk.E6, this);
    }

    public void o(int i, String str, String str2, String str3) {
        this.D.setText(str);
        this.F.setText(str2);
        this.E.setText(str3);
        Drawable resourceDrawable = ResourceExtKt.getResourceDrawable(getContext(), i);
        int iDip2px = DisplayUtil.dip2px(20.0f);
        resourceDrawable.setBounds(0, 0, iDip2px, iDip2px);
        Drawable resourceDrawable2 = ResourceExtKt.getResourceDrawable(getContext(), i9k.I2);
        Drawable resourceDrawable3 = ResourceExtKt.getResourceDrawable(getContext(), i9k.j2);
        if (AppUtil.isRTLDirection()) {
            this.E.setCompoundDrawablesWithIntrinsicBounds(resourceDrawable3, (Drawable) null, resourceDrawable, (Drawable) null);
        } else {
            this.E.setCompoundDrawablesWithIntrinsicBounds(resourceDrawable, (Drawable) null, resourceDrawable2, (Drawable) null);
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.D = (TextView) findViewById(cdk.dv);
        this.F = (TextView) findViewById(cdk.Ol);
        this.E = (TextView) findViewById(cdk.Ju);
    }

    public DataHeaderTargetItemV2View(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        p(context);
    }

    public DataHeaderTargetItemV2View(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        p(context);
    }
}
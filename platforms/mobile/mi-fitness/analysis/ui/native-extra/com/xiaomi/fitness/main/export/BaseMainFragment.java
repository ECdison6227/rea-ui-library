package com.xiaomi.fitness.main.export;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.i;
import com.xiaomi.fitness.baseui.AbsViewModel;
import com.xiaomi.fitness.baseui.view.BaseBindingFragment;
import com.xiaomi.fitness.common.app.LifecycleConstantKt;
import com.xiaomi.fitness.component.StandardComponentEvent;
import defpackage.m12;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b&\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u0001*\b\b\u0001\u0010\u0004*\u00020\u00032\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005B\u001f\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\u000b\u0010\fB\u0019\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\r\u001a\u00020\u0006¢\u0006\u0004\b\u000b\u0010\u000eB\u0011\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u000b\u0010\u000fB\t\b\u0016¢\u0006\u0004\b\u000b\u0010\u0010J\u0017\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0014\u0010\u0015J\u0017\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0016H\u0016¢\u0006\u0004\b\u0018\u0010\u0019J\u0011\u0010\u001b\u001a\u0004\u0018\u00010\u001aH&¢\u0006\u0004\b\u001b\u0010\u001cJ\u0019\u0010\u001f\u001a\u00020\u00132\b\u0010\u001e\u001a\u0004\u0018\u00010\u001dH\u0016¢\u0006\u0004\b\u001f\u0010 J/\u0010%\u001a\u0004\u0018\u00010\u001a2\b\u0010\"\u001a\u0004\u0018\u00010!2\b\u0010$\u001a\u0004\u0018\u00010#2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001dH\u0016¢\u0006\u0004\b%\u0010&J!\u0010(\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u001a2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001dH\u0016¢\u0006\u0004\b(\u0010)J\u000f\u0010*\u001a\u00020\u0013H\u0016¢\u0006\u0004\b*\u0010\u0010J/\u0010.\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u001a2\u0016\b\u0002\u0010-\u001a\u0010\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u0013\u0018\u00010+H\u0004¢\u0006\u0004\b.\u0010/R\u0014\u0010\b\u001a\u00020\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\b\u00100R\"\u00102\u001a\u0002018\u0006@\u0006X\u0087.¢\u0006\u0012\n\u0004\b2\u00103\u001a\u0004\b4\u00105\"\u0004\b6\u00107¨\u00068"}, d2 = {"Lcom/xiaomi/fitness/main/export/BaseMainFragment;", "Lcom/xiaomi/fitness/baseui/AbsViewModel;", "VM", "Landroidx/databinding/i;", "VDB", "Lcom/xiaomi/fitness/baseui/view/BaseBindingFragment;", "", "contentLayoutId", "mViewModelId", "", "sharedViewModel", "<init>", "(IIZ)V", "viewModelId", "(II)V", "(I)V", "()V", "Landroid/content/Context;", "context", "", "onAttach", "(Landroid/content/Context;)V", "Landroid/content/res/Configuration;", StandardComponentEvent.KEY_NEW_CONFIG, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", "Landroid/view/View;", "getContentView", "()Landroid/view/View;", "Landroid/os/Bundle;", "savedInstanceState", "onCreate", "(Landroid/os/Bundle;)V", "Landroid/view/LayoutInflater;", "inflater", "Landroid/view/ViewGroup;", "container", "onInflateView", "(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;", "view", "onViewCreated", "(Landroid/view/View;Landroid/os/Bundle;)V", LifecycleConstantKt.ON_DESTROY, "Lkotlin/Function1;", "Landroid/view/MenuItem;", "onMenuClickListener", "showPopupMenu", "(Landroid/view/View;Lkotlin/jvm/functions/Function1;)V", "I", "Lcom/xiaomi/fitness/main/export/BaseMainFragmentDelegate;", "mBaseFragmentDelegate", "Lcom/xiaomi/fitness/main/export/BaseMainFragmentDelegate;", "getMBaseFragmentDelegate", "()Lcom/xiaomi/fitness/main/export/BaseMainFragmentDelegate;", "setMBaseFragmentDelegate", "(Lcom/xiaomi/fitness/main/export/BaseMainFragmentDelegate;)V", "main-export_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class BaseMainFragment<VM extends AbsViewModel, VDB extends i> extends BaseBindingFragment<VM, VDB> {
    public BaseMainFragmentDelegate mBaseFragmentDelegate;
    private final int mViewModelId;

    public BaseMainFragment(int i, int i2, boolean z) {
        super(i, i2, z);
        this.mViewModelId = i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void showPopupMenu$default(BaseMainFragment baseMainFragment, View view, Function1 function1, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: showPopupMenu");
        }
        if ((i & 2) != 0) {
            function1 = null;
        }
        baseMainFragment.showPopupMenu(view, function1);
    }

    @Nullable
    public abstract View getContentView();

    @NotNull
    public final BaseMainFragmentDelegate getMBaseFragmentDelegate() {
        BaseMainFragmentDelegate baseMainFragmentDelegate = this.mBaseFragmentDelegate;
        if (baseMainFragmentDelegate != null) {
            return baseMainFragmentDelegate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mBaseFragmentDelegate");
        return null;
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onAttach(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super.onAttach(context);
        getMBaseFragmentDelegate().onAttach(context, this);
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        getMBaseFragmentDelegate().onConfigurationChanged(newConfig, getContentView());
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMBaseFragmentDelegate().onCreate(savedInstanceState);
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        getMBaseFragmentDelegate().onDestroy();
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, defpackage.ryd
    @Nullable
    public View onInflateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseMainFragmentDelegate mBaseFragmentDelegate = getMBaseFragmentDelegate();
        Intrinsics.checkNotNull(inflater);
        Pair pairOnInflateView = mBaseFragmentDelegate.onInflateView(inflater, container, savedInstanceState, getMContentLayoutId());
        setMBinding((i) pairOnInflateView.getSecond());
        setRootView((View) pairOnInflateView.getFirst());
        return getRootView();
    }

    @Override // com.xiaomi.fitness.baseui.view.BaseBindingFragment, com.xiaomi.fitness.baseui.view.BaseViewModelFragment, com.xiaomi.fitness.baseui.view.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, savedInstanceState);
        getMBaseFragmentDelegate().onViewCreated(view, getContentView(), savedInstanceState);
    }

    public final void setMBaseFragmentDelegate(@NotNull BaseMainFragmentDelegate baseMainFragmentDelegate) {
        Intrinsics.checkNotNullParameter(baseMainFragmentDelegate, "<set-?>");
        this.mBaseFragmentDelegate = baseMainFragmentDelegate;
    }

    public final void showPopupMenu(@NotNull View view, @Nullable Function1<? super MenuItem, Unit> onMenuClickListener) {
        Intrinsics.checkNotNullParameter(view, "view");
        getMBaseFragmentDelegate().showPopupMenu(getActivity(), view, onMenuClickListener);
    }

    public BaseMainFragment(int i, int i2) {
        this(i, i2, false);
    }

    public BaseMainFragment(int i) {
        this(i, m12.a, false);
    }

    public BaseMainFragment() {
        this(-1, m12.a, false);
    }
}
package com.xiaomi.fitness.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.xiaomi.fitness.common.extensions.ApplicationExtKt;
import defpackage.b6k;
import defpackage.iek;
import defpackage.vhk;
import defpackage.wjf;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0002\u001d\u001eB'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\u0018\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0007J\u001e\u0010\u0012\u001a\u00020\u00102\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00162\b\b\u0002\u0010\u0015\u001a\u00020\u0007J\u001e\u0010\u0017\u001a\u00020\u00102\u0016\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00100\u000fj\u0002`\u0011J\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0007J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cR\u0012\u0010\n\u001a\u00060\u000bR\u00020\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u000e\u001a\u0016\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fj\u0004\u0018\u0001`\u0011X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mAdapter", "Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView$HomeHeaderTabTitleAdapter;", "menuImage", "Landroid/widget/ImageView;", "onHomeHeaderTitleSelectListener", "Lkotlin/Function1;", "", "Lcom/xiaomi/fitness/widget/view/OnHomeHeaderTitleSelectListener;", "setTitle", "title", "", "select", "", "setTitleSelectListener", "position", "updateSelect", "setMenuClickListener", ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, "Landroid/view/View$OnClickListener;", "HomeHeaderTabTitleAdapter", "HomeHeaderTabTitleHolder", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class HomeHeaderTabTitleView extends ConstraintLayout {

    @NotNull
    private final HomeHeaderTabTitleAdapter mAdapter;

    @NotNull
    private final ImageView menuImage;

    @Nullable
    private Function1<? super Integer, Unit> onHomeHeaderTitleSelectListener;

    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0082\u0004\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00030\u0001B\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u001e\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\nJ\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\nJ\u000e\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\nJ\u001c\u0010\u0013\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nH\u0016J\b\u0010\u0017\u001a\u00020\nH\u0016J\u001c\u0010\u0018\u001a\u00020\f2\n\u0010\u0019\u001a\u00060\u0002R\u00020\u00032\u0006\u0010\u0011\u001a\u00020\nH\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView$HomeHeaderTabTitleAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView$HomeHeaderTabTitleHolder;", "Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;", "<init>", "(Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;)V", "titles", "", "", "currentSelect", "", "setTitles", "", "title", "", "select", "onTitleSelect", "position", "updateSelect", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "getItemCount", "onBindViewHolder", "holder", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public final class HomeHeaderTabTitleAdapter extends RecyclerView.Adapter<HomeHeaderTabTitleHolder> {
        private int currentSelect;

        @NotNull
        private final List<String> titles = new ArrayList();

        public HomeHeaderTabTitleAdapter() {
        }

        public static /* synthetic */ Unit j(HomeHeaderTabTitleAdapter homeHeaderTabTitleAdapter, int i) {
            return onBindViewHolder$lambda$0(homeHeaderTabTitleAdapter, i);
        }

        private static final Unit onBindViewHolder$lambda$0(HomeHeaderTabTitleAdapter homeHeaderTabTitleAdapter, int i) {
            homeHeaderTabTitleAdapter.onTitleSelect(i);
            return Unit.INSTANCE;
        }

        public static /* synthetic */ void setTitles$default(HomeHeaderTabTitleAdapter homeHeaderTabTitleAdapter, List list, int i, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            homeHeaderTabTitleAdapter.setTitles(list, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.titles.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.d0 d0Var, int i) {
            onBindViewHolder((HomeHeaderTabTitleHolder) d0Var, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ RecyclerView.d0 onCreateViewHolder(ViewGroup viewGroup, int i) {
            return onCreateViewHolder(viewGroup, i);
        }

        public final void onTitleSelect(int position) {
            notifyItemChanged(this.currentSelect);
            this.currentSelect = position;
            notifyItemChanged(position);
            Function1 function1Access$getOnHomeHeaderTitleSelectListener$p = HomeHeaderTabTitleView.access$getOnHomeHeaderTitleSelectListener$p(HomeHeaderTabTitleView.this);
            if (function1Access$getOnHomeHeaderTitleSelectListener$p != null) {
                function1Access$getOnHomeHeaderTitleSelectListener$p.invoke(Integer.valueOf(position));
            }
        }

        public final void setTitles(@NotNull List<String> title, int select) {
            Intrinsics.checkNotNullParameter(title, "title");
            this.currentSelect = select;
            this.titles.clear();
            this.titles.addAll(title);
            notifyDataSetChanged();
        }

        public final void updateSelect(int position) {
            notifyItemChanged(this.currentSelect);
            this.currentSelect = position;
            notifyItemChanged(position);
        }

        public void onBindViewHolder(@NotNull HomeHeaderTabTitleHolder holder, int position) {
            Intrinsics.checkNotNullParameter(holder, "holder");
            holder.bindTitle(this.titles.get(position), this.currentSelect == position);
            holder.onSelect(new a(this));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NotNull
        public HomeHeaderTabTitleHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            wjf wjfVarX0 = wjf.x0(LayoutInflater.from(parent.getContext()), parent, false);
            Intrinsics.checkNotNullExpressionValue(wjfVarX0, "inflate(...)");
            return new HomeHeaderTabTitleHolder(HomeHeaderTabTitleView.this, wjfVarX0);
        }
    }

    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0082\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u001d\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b\u000b\u0010\fJ!\u0010\u0010\u001a\u00020\n2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\n0\r¢\u0006\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0012¨\u0006\u0013"}, d2 = {"Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView$HomeHeaderTabTitleHolder;", "Landroidx/recyclerview/widget/RecyclerView$d0;", "Lwjf;", "binding", "<init>", "(Lcom/xiaomi/fitness/widget/view/HomeHeaderTabTitleView;Lwjf;)V", "", "title", "", "isCurrent", "", "bindTitle", "(Ljava/lang/String;Z)V", "Lkotlin/Function1;", "", "position", "onSelect", "(Lkotlin/jvm/functions/Function1;)V", "Lwjf;", "widget_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public final class HomeHeaderTabTitleHolder extends RecyclerView.d0 {

        @NotNull
        private final wjf binding;
        final /* synthetic */ HomeHeaderTabTitleView this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public HomeHeaderTabTitleHolder(@NotNull HomeHeaderTabTitleView homeHeaderTabTitleView, wjf binding) {
            super(binding.getRoot());
            Intrinsics.checkNotNullParameter(binding, "binding");
            this.this$0 = homeHeaderTabTitleView;
            this.binding = binding;
        }

        public static /* synthetic */ void a(Function1 function1, HomeHeaderTabTitleHolder homeHeaderTabTitleHolder, View view) {
            onSelect$lambda$0(function1, homeHeaderTabTitleHolder, view);
        }

        private static final void onSelect$lambda$0(Function1 function1, HomeHeaderTabTitleHolder homeHeaderTabTitleHolder, View view) {
            function1.invoke(Integer.valueOf(homeHeaderTabTitleHolder.getBindingAdapterPosition()));
        }

        public final void bindTitle(@NotNull String title, boolean isCurrent) {
            Intrinsics.checkNotNullParameter(title, "title");
            this.binding.A.setText(title);
            this.binding.A.setTextColor(ApplicationExtKt.getApplication().getColor(isCurrent ? b6k.o : b6k.f));
        }

        public final void onSelect(@NotNull Function1<? super Integer, Unit> position) {
            Intrinsics.checkNotNullParameter(position, "position");
            this.binding.getRoot().setOnClickListener(new b(position, this));
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public HomeHeaderTabTitleView(@NotNull Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static final /* synthetic */ Function1 access$getOnHomeHeaderTitleSelectListener$p(HomeHeaderTabTitleView homeHeaderTabTitleView) {
        return homeHeaderTabTitleView.onHomeHeaderTitleSelectListener;
    }

    public static /* synthetic */ void setTitle$default(HomeHeaderTabTitleView homeHeaderTabTitleView, String str, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        homeHeaderTabTitleView.setTitle(str, i);
    }

    public final void setMenuClickListener(@NotNull View.OnClickListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.menuImage.setOnClickListener(listener);
    }

    public final void setTitle(@NotNull String title, int select) {
        Intrinsics.checkNotNullParameter(title, "title");
        this.mAdapter.setTitles(CollectionsKt.mutableListOf(title), select);
    }

    public final void setTitleSelectListener(@NotNull Function1<? super Integer, Unit> position) {
        Intrinsics.checkNotNullParameter(position, "position");
        this.onHomeHeaderTitleSelectListener = position;
    }

    public final void updateSelect(int position) {
        this.mAdapter.updateSelect(position);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public HomeHeaderTabTitleView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public static /* synthetic */ void setTitle$default(HomeHeaderTabTitleView homeHeaderTabTitleView, List list, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        homeHeaderTabTitleView.setTitle((List<String>) list, i);
    }

    public final void setTitle(@NotNull List<String> title, int select) {
        Intrinsics.checkNotNullParameter(title, "title");
        this.mAdapter.setTitles(title, select);
    }

    public /* synthetic */ HomeHeaderTabTitleView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @JvmOverloads
    public HomeHeaderTabTitleView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        LayoutInflater.from(context).inflate(vhk.e, (ViewGroup) this, true);
        RecyclerView recyclerView = (RecyclerView) findViewById(iek.G);
        View viewFindViewById = findViewById(iek.o);
        Intrinsics.checkNotNullExpressionValue(viewFindViewById, "findViewById(...)");
        this.menuImage = (ImageView) viewFindViewById;
        HomeHeaderTabTitleAdapter homeHeaderTabTitleAdapter = new HomeHeaderTabTitleAdapter();
        this.mAdapter = homeHeaderTabTitleAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(ApplicationExtKt.getApplication(), 0, false));
        recyclerView.setAdapter(homeHeaderTabTitleAdapter);
    }
}
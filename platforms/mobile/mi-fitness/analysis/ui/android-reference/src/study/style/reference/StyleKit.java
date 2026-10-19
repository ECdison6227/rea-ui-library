package study.style.reference;

import android.content.Context;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.io.IOException;

/** Native, reusable visual primitives. Layout uses dp; text uses sp. No page bitmap rendering. */
public final class StyleKit {
    private final java.util.ArrayDeque<Runnable> sheetQueue=new java.util.ArrayDeque<>();
    private Dialog activeSheet;
    private boolean externalModalOpen;
    private boolean disposed;
    public final Context context;
    public final Tokens t;
    public final float density;
    public StyleKit(Context c, Tokens tokens) { context=c; t=tokens; density=c.getResources().getDisplayMetrics().density; }
    public int dp(float n) { return Math.round(n*density); }
    public FrameLayout frame() { return new FrameLayout(context); }
    public TextView text(String value, float sp, int weight, int color) {
        TextView v = new TextView(context); v.setText(value); v.setTextSize(sp); v.setTextColor(color);
        v.setTypeface(android.os.Build.VERSION.SDK_INT>=28?Typeface.create(Typeface.create("sans-serif",Typeface.NORMAL),weight,false):Typeface.create("sans-serif",weight>=600?Typeface.BOLD:Typeface.NORMAL));
        v.setIncludeFontPadding(true); v.setGravity(Gravity.CENTER_VERTICAL); return v;
    }
    public TextView latin(String value, float sp, String face, int color) {
        TextView v=text(value,sp,400,color);
        try { v.setTypeface(Typeface.createFromAsset(context.getAssets(),face)); } catch(RuntimeException ignored) {}
        v.setIncludeFontPadding(false); return v;
    }
    public void at(FrameLayout p, View v, float x, float y, float w, float h) {
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(w<0?(int)w:dp(w),h<0?(int)h:dp(h));
        lp.leftMargin=dp(x); lp.topMargin=dp(y); p.addView(v,lp);
    }
    public GradientDrawable round(int color,float radius) { GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d; }
    public FrameLayout surface() { FrameLayout p=frame();p.setBackground(round(t.surface,18));return p; }
    public FrameLayout surface(float radius) { FrameLayout p=frame();p.setBackground(round(t.surface,radius));return p; }
    public void setChecked(FrameLayout row,boolean checked) { SwitchControl c=(SwitchControl)row.getChildAt(row.getChildCount()-1);c.checked=checked;c.update(); }
    public void action(View v, String label, Runnable callback) {
        v.setContentDescription(label);v.setFocusable(true);v.setClickable(true);
        v.setOnClickListener(w->callback.run());
    }
    public ImageView image(String asset) {
        ImageView v=new ImageView(context);v.setScaleType(ImageView.ScaleType.FIT_CENTER);
        try { v.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(asset))); }
        catch(IOException ignored) {} return v;
    }
    public String tabAsset(String name, boolean selected) {
        String stem="tab_"+name+"_"+(selected?"selected":"idle")+"_"+t.mode();
        if(name.equals("device") || (name.equals("mine")&&selected&&t.night))return stem+".png";
        return stem+".webp";
    }
    public FrameLayout topBar(String title, float width, boolean back, Runnable onBack, Runnable onAdd) {
        FrameLayout bar=frame();
        if(back) {
            Glyph arrow=new Glyph(context,"back",t.night?t.text:0xff333333);at(bar,arrow,20,22.55f,48,40);action(arrow,"返回",onBack);
            at(bar,text(title,32,400,t.text),26,74.5f,width-52,47);
        } else {
            at(bar,text(title,32,300,t.text),26,20,width-100,47);
            ImageView plus=image("header_add_"+t.mode()+".webp");plus.setTag("add-action");at(bar,plus,width-56,23,36,36);action(plus,"添加",onAdd);
        }
        return bar;
    }
    public FrameLayout bottomNav(float width, int selected, String[] labels, java.util.function.IntConsumer change) {
        FrameLayout nav=frame();nav.setBackgroundColor(t.navigation);
        View line=new View(context);line.setBackgroundColor(t.divider);at(nav,line,0,0,width,0.36f);
        String[] names={"health","sport","device","mine"};
        for(int i=0;i<4;i++) {
            final int index=i;FrameLayout cell=frame();float w=width/4;
            at(cell,image(tabAsset(names[i],selected==i)),(w-26)/2,12,26,26);
            TextView label=text(labels[i],11,700,selected==i?t.primary:(t.night?0xff878787:0xffb3b3b3));
            label.setGravity(Gravity.CENTER);at(cell,label,0,39.6f,w,16);
            cell.setSelected(selected==i);action(cell,labels[i],()->change.accept(index));at(nav,cell,i*w,0,w,66.67f);
        } return nav;
    }
    public FrameLayout profile(float width, String name, String subtitle, Runnable onClick) {
        FrameLayout p=frame();
        at(p,image("mine_default_author_avatar_"+t.rnMode()+".webp"),13,0,50,50);
        at(p,text(name,20,400,t.text),75,1.85f,width-120,26.9f);
        at(p,text(subtitle,12,400,t.text),75,30.2f,width-120,16);
        FrameLayout badge=frame();badge.setBackground(round(t.surface,30));at(badge,image("mine_medal_empty_"+t.mode()+".webp"),8,8,20,20);at(p,badge,width-52,8,36,36);
        action(p,name,onClick);return p;
    }
    public FrameLayout shortcutGrid(float width,String[] labels,String[] assets,java.util.function.IntConsumer click) {
        FrameLayout grid=frame();float cellWidth=width/labels.length;
        for(int i=0;i<labels.length;i++) {
            final int index=i;FrameLayout cell=frame();
            at(cell,image(assets[i]),(cellWidth-24)/2,0,24,24);
            TextView label=text(labels[i],14,400,t.text);label.setGravity(Gravity.CENTER);
            at(cell,label,0,25.1f,cellWidth,30.9f);action(cell,labels[i],()->click.accept(index));at(grid,cell,i*cellWidth,0,cellWidth,70);
        }return grid;
    }
    public FrameLayout chevronRow(float width,String title,String subtitle,String value,String icon,float height,int weight,Runnable click) {
        FrameLayout row=frame();float start=0;
        if(icon!=null){at(row,image(icon),0,(height-26)/2,26,26);start=38;}
        float valueWidth=value==null?0:100;
        float titleWidth=width-start-25-valueWidth;
        if(subtitle==null)at(row,text(title,16,weight,t.text),start,0,titleWidth,height);
        else{
            at(row,text(title,16,weight,t.text),start,13.45f,titleWidth,23.64f);
            TextView sub=text(subtitle,12,400,t.secondary);sub.setMaxLines(4);
            at(row,sub,start,37.09f,width-start-45,17.82f);
        }
        if(value!=null){TextView v=text(value,14,400,t.secondary);v.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);at(row,v,width-130,0,100,height);}
        at(row,new Glyph(context,"chevron",t.night?0xff666666:0xffe0e0e0),width-24,(height-20)/2,20,20);
        action(row,title,click);return row;
    }
    public FrameLayout switchRow(float width,String title,String subtitle,boolean checked,java.util.function.Consumer<Boolean> change) {
        FrameLayout row=frame();at(row,text(title,16,700,t.text),0,13.45f,width-60,23.64f);
        at(row,text(subtitle,12,400,t.secondary),0,37.09f,width-60,17.82f);
        SwitchControl control=new SwitchControl(context,t,checked);
        at(row,control,width-45,21.45f,45,25);control.setOnClickListener(v->change.accept(!control.checked));
        return row;
    }
    public TextView primaryButton(String title, boolean enabled, Runnable click) {
        TextView b=text(title,17,700,t.night?0xffe5e5e5:Color.WHITE);b.setGravity(Gravity.CENTER);
        b.setBackground(round(t.primary,28));b.setAlpha(enabled?1:0.4f);b.setEnabled(enabled);action(b,title,click);return b;
    }
    /** A guest prerequisite pattern; D standard rounded corners instead of MIUI smooth geometry. */
    public void confirmSheet(String message,String positive,Runnable confirm,Runnable cancel) {
        if(disposed)return;
        if(activeSheet!=null||externalModalOpen){sheetQueue.addLast(()->confirmSheet(message,positive,confirm,cancel));return;}
        Dialog dialog=new Dialog(context);dialog.setCancelable(true);dialog.setCanceledOnTouchOutside(true);FrameLayout outer=frame();float width=context.getResources().getDisplayMetrics().widthPixels/density;
        activeSheet=dialog;
        FrameLayout card=surface(28);at(outer,card,12,0,width-24,180);
        TextView copy=text(message,17,400,t.text);copy.setGravity(Gravity.TOP);at(card,copy,24,24,width-72,62);
        float buttonWidth=(width-88)/2;
        TextView negative=primaryButton("取消",true,()->{dialog.dismiss();cancel.run();});negative.setTextColor(t.text);negative.setBackground(round(t.night?0xff303030:0xfff0f0f0,18));negative.setTypeface(Typeface.create("sans-serif",0));
        at(card,negative,24,122,buttonWidth,51);
        TextView yes=primaryButton(positive,true,()->{dialog.dismiss();confirm.run();});yes.setBackground(round(t.primary,18));yes.setTypeface(Typeface.create("sans-serif",0));at(card,yes,40+buttonWidth,122,buttonWidth,51);
        dialog.setContentView(outer,new ViewGroup.LayoutParams(dp(width),dp(192)));Window win=dialog.getWindow();win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));win.setDimAmount(.5f);win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);win.setGravity(Gravity.BOTTOM);win.setLayout(-1,dp(192));dialog.setOnCancelListener(d->cancel.run());dialog.setOnDismissListener(d->{activeSheet=null;drain();});dialog.show();win.setLayout(-1,dp(192));
    }
    private void drain(){if(!disposed&&!externalModalOpen&&activeSheet==null&&!sheetQueue.isEmpty())sheetQueue.removeFirst().run();}
    public void externalModal(boolean open){externalModalOpen=open;if(!open)drain();}
    public void dispose(){disposed=true;sheetQueue.clear();if(activeSheet!=null)activeSheet.dismiss();}
    public FrameLayout metricCard(float width,String title,String description,String asset,boolean bars,Runnable click) {
        FrameLayout p=surface(15);at(p,image(asset),15,14,28,28);
        at(p,text(title,17,700,t.text),15,46.5f,width-30,25.1f);
        at(p,text(description,14,400,t.text),15,71.6f,width-35,21.1f);
        at(p,new Glyph(context,"chevron",0xffcccccc),width-34,82,16,16);
        if(bars){for(int i=0;i<24;i++){View l=new View(context);l.setBackgroundColor(t.page);at(p,l,16+i*(width-32)/24,142,1.8f,40);}}
        else {View block=new View(context);block.setBackground(round(t.page,4));at(p,block,15,138,width-30,40);}
        action(p,title,click);return p;
    }
    public static final class SwitchControl extends View {
        boolean checked;final Tokens t;final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        SwitchControl(Context c,Tokens t,boolean initial){super(c);this.t=t;checked=initial;setFocusable(true);setClickable(true);update();}
        void update(){setContentDescription("新消息通知，当前已"+(checked?"开启":"关闭"));setSelected(checked);invalidate();}
        @Override protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();p.setColor(checked?t.primary:0xffcccccc);c.drawRoundRect(0,0,w,h,h/2,h/2,p);p.setColor(Color.WHITE);c.drawCircle(checked?w-h/2:h/2,h/2,h*.27f,p);}
        @Override public CharSequence getAccessibilityClassName(){return "android.widget.Switch";}
        @Override public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo i){super.onInitializeAccessibilityNodeInfo(i);i.setCheckable(true);i.setChecked(checked);}
    }
    public static final class Glyph extends View {
        final String kind;final int color;final Paint p=new Paint(3);
        public Glyph(Context c,String k,int co){super(c);kind=k;color=co;}
        @Override protected void onDraw(Canvas canvas){canvas.save();canvas.scale(getWidth()/40f,getHeight()/40f);p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1.8f);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);
            Path path=new Path();
            if(kind.equals("plus")){canvas.drawCircle(20,20,12.5f,p);canvas.drawLine(14.5f,20,25.5f,20,p);canvas.drawLine(20,14.5f,20,25.5f,p);}
            else if(kind.equals("back")){canvas.drawLine(8,20,25.2f,20,p);path.moveTo(15.5f,12.7f);path.lineTo(8,20);path.lineTo(15.5f,27.3f);canvas.drawPath(path,p);}
            else if(kind.equals("chevron")){p.setStrokeWidth(3);path.moveTo(15,11);path.lineTo(24,20);path.lineTo(15,29);canvas.drawPath(path,p);}
            else if(kind.equals("message")){canvas.drawRoundRect(8,8,31,29,5,5,p);path.moveTo(16,29);path.lineTo(20,32);path.lineTo(24,29);canvas.drawPath(path,p);p.setStyle(Paint.Style.FILL);for(int i=0;i<3;i++)canvas.drawCircle(14+i*6,18,1.5f,p);}
            else {canvas.drawCircle(20,20,15,p);canvas.drawCircle(20,15,5,p);canvas.drawRoundRect(12,24,28,29,3,3,p);}
            canvas.restore();
        }
    }
}

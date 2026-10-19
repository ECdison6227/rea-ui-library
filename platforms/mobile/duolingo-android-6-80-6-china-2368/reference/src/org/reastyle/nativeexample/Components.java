package org.reastyle.nativeexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/** Native geometry primitives. Token facts are B; rendering, pressed feedback and focus policy are D. */
public final class Components {
    public static int SNOW = Color.rgb(255,255,253), POLAR = Color.rgb(247,247,247);
    public static int SWAN = 0xffe5e5e5, HARE = 0xffafafaf, EEL = 0xff4b4b4b, WOLF = 0xff777777;
    public static int GREEN = 0xff58cc02, GREEN_LIP = 0xff58a700;
    public static int BLUE = 0xff1cb0f6, BLUE_LIP = 0xff1899d6;
    public static int SELECTED = 0xffddf4ff, SELECTED_BORDER = 0xff84d8ff, RED = 0xffff4b4b;
    public static boolean reduceMotion;
    public static boolean motionEnabled(){return !reduceMotion&&ValueAnimator.areAnimatorsEnabled();}
    /** D target palette. Night declarations are candidates, not an all-source-theme claim. */
    public static void configurePalette(boolean dark){
        SNOW=dark?0xff131f22:0xfffffffd;POLAR=dark?0xff202f36:0xfff7f7f7;
        SWAN=dark?0xff37464f:0xffe5e5e5;HARE=dark?0xff81949b:0xffafafaf;
        EEL=dark?0xfff1f7fb:0xff4b4b4b;WOLF=dark?0xffb3c4cc:0xff777777;
        GREEN=dark?0xff93d333:0xff58cc02;GREEN_LIP=0xff58a700;
        BLUE=dark?0xff49c0f8:0xff1cb0f6;BLUE_LIP=0xff1899d6;
        SELECTED=dark?0xff16465d:0xffddf4ff;SELECTED_BORDER=dark?0xff49c0f8:0xff84d8ff;RED=dark?0xffee5555:0xffff4b4b;
    }
    private Components() {}
    public static int dp(Context c, float d) { return Math.round(d * c.getResources().getDisplayMetrics().density); }

    public static class Face extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        public int face, lip, border;
        public float radius, thickness, stroke;
        public Face(Context c, int face, int lip, float radiusDp, float lipDp, float borderDp) {
            this.face=face; this.lip=lip; this.border=lip;
            radius=dp(c,radiusDp); thickness=dp(c,lipDp); stroke=dp(c,borderDp);
        }
        @Override public void draw(Canvas c) {
            RectF r = new RectF(getBounds());
            if (thickness > 0) { paint.setColor(lip); c.drawRoundRect(r,radius,radius,paint); }
            r.bottom -= thickness;
            paint.setStyle(Paint.Style.FILL); paint.setColor(face); c.drawRoundRect(r,radius,radius,paint);
            if (stroke > 0) {
                paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(stroke); paint.setColor(border);
                r.inset(stroke/2,stroke/2); c.drawRoundRect(r,radius,radius,paint); paint.setStyle(Paint.Style.FILL);
            }
        }
        @Override public void setAlpha(int value) { paint.setAlpha(value); invalidateSelf(); }
        @Override public void setColorFilter(ColorFilter f) { paint.setColorFilter(f); invalidateSelf(); }
        @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    }

    public static class Button extends TextView {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final boolean transparent;
        private int faceColor, lipColor;
        private float pressedFraction;
        private ValueAnimator pressedAnimator;
        public Button(Context c, String label, Typeface typeface, int face, int lip, boolean transparent) {
            super(c); this.faceColor=face; this.lipColor=lip; this.transparent=transparent;
            setText(label); setTypeface(typeface); setTextSize(17); setLetterSpacing(.05f);
            setAllCaps(true); setGravity(Gravity.CENTER); setIncludeFontPadding(false);
            setMinHeight(dp(c,52)); setPadding(dp(c,16),dp(c,10),dp(c,16),dp(c,10)+(transparent?0:dp(c,4)));
            setClickable(true); setFocusable(true); setBackground(null);
            setTextColor(transparent?face:Color.WHITE);
        }
        public void setPalette(int face,int lip) { faceColor=face; lipColor=lip; invalidate(); }
        @Override public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setTextColor(enabled?(transparent?faceColor:Color.WHITE):HARE);
            invalidate();
        }
        @Override protected void drawableStateChanged() { super.drawableStateChanged(); invalidate(); }
        @Override public void setPressed(boolean pressed){
            super.setPressed(pressed);if(pressedAnimator!=null)pressedAnimator.cancel();
            float target=pressed&&isEnabled()?1f:0f;
            if(!motionEnabled()){pressedFraction=target;invalidate();return;}
            pressedAnimator=ValueAnimator.ofFloat(pressedFraction,target);pressedAnimator.setDuration(80);
            pressedAnimator.setInterpolator(new LinearInterpolator());pressedAnimator.addUpdateListener(a->{pressedFraction=(float)a.getAnimatedValue();invalidate();});pressedAnimator.start();
        }
        @Override protected void onDetachedFromWindow(){if(pressedAnimator!=null)pressedAnimator.cancel();super.onDetachedFromWindow();}
        @Override protected void onDraw(Canvas canvas) {
            if (!transparent) {
                float w=getWidth(),h=getHeight(),r=dp(getContext(),16),lip=dp(getContext(),4);
                float offset=isEnabled()?pressedFraction*lip:lip;
                if(isEnabled()){paint.setColor(lipColor);canvas.drawRoundRect(0,0,w,h,r,r,paint);}
                paint.setColor(isEnabled()?faceColor:SWAN); canvas.drawRoundRect(0,offset,w,h-lip+offset,r,r,paint);
                if (isFocused()) {
                    paint.setStyle(Paint.Style.STROKE); paint.setStrokeWidth(dp(getContext(),2)); paint.setColor(BLUE_LIP);
                    canvas.drawRoundRect(dp(getContext(),2),dp(getContext(),2)+offset,w-dp(getContext(),2),h-lip+offset-dp(getContext(),2),r,r,paint);
                    paint.setStyle(Paint.Style.FILL);
                }
                canvas.save(); canvas.translate(0,offset); super.onDraw(canvas); canvas.restore();
            } else super.onDraw(canvas);
        }
        @Override public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info); info.setClassName("android.widget.Button");
        }
    }

    public static class ChoiceCard extends LinearLayout implements Checkable {
        private final Face backgroundFace;
        private final TextView label;
        private boolean checked;
        private final boolean multiple;
        public ChoiceCard(Context c, String title, Typeface face, boolean multiple) {
            super(c); this.multiple=multiple;
            setOrientation(HORIZONTAL); setGravity(Gravity.CENTER_VERTICAL);
            setPadding(dp(c,16),0,dp(c,16),dp(c,4));
            setMinimumHeight(dp(c,64)); setClickable(true); setFocusable(true);
            setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
            backgroundFace=new Face(c,SNOW,SWAN,16,2,2); setBackground(backgroundFace);
            label=new TextView(c); label.setText(title); label.setTypeface(face); label.setTextSize(19);
            label.setTextColor(EEL); label.setIncludeFontPadding(false);
            setContentDescription(title);
        }
        public TextView label() { return label; }
        public void addLabel() { addView(label,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1)); }
        @Override public void setChecked(boolean value) {
            checked=value; setSelected(value);
            backgroundFace.face=value?SELECTED:SNOW;
            backgroundFace.lip=value?SELECTED_BORDER:SWAN;
            backgroundFace.border=value?SELECTED_BORDER:SWAN;
            label.setTextColor(value?BLUE:EEL);
            refreshDrawableState(); backgroundFace.invalidateSelf();
            sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
        }
        @Override public boolean isChecked() { return checked; }
        @Override public void toggle() { setChecked(!checked); }
        @Override public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(multiple?"android.widget.CheckBox":"android.widget.RadioButton");
            info.setCheckable(true); info.setChecked(checked); info.setSelected(checked);
        }
    }

    public static class Progress extends View {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float value;
        private ValueAnimator animator;
        public Progress(Context c,float value) { super(c); this.value=value; setContentDescription("Progress"); }
        public void setProgress(float value) { this.value=Math.max(0,Math.min(1,value)); invalidate(); }
        public void animateProgress(float target){
            if(animator!=null)animator.cancel();target=Math.max(0,Math.min(1,target));
            if(!motionEnabled()){setProgress(target);return;}
            animator=ValueAnimator.ofFloat(value,target);animator.setDuration(180);animator.setInterpolator(new LinearInterpolator());animator.addUpdateListener(a->setProgress((float)a.getAnimatedValue()));animator.start();
        }
        @Override protected void onDetachedFromWindow(){if(animator!=null)animator.cancel();super.onDetachedFromWindow();}
        @Override protected void onDraw(Canvas c) {
            float r=getHeight()/2f;p.setColor(SWAN);c.drawRoundRect(0,0,getWidth(),getHeight(),r,r,p);
            if(value>0){p.setColor(GREEN);c.drawRoundRect(0,0,Math.max(getHeight(),getWidth()*value),getHeight(),r,r,p);}
        }
        @Override public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);info.setClassName("android.widget.ProgressBar");
            info.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_FLOAT,0,1,value));
        }
    }

    public static class Bubble extends Drawable {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float scale;
        public Bubble(Context c) { scale=c.getResources().getDisplayMetrics().density; }
        @Override public void draw(Canvas c) {
            float left=17*scale,top=scale,right=getBounds().width()-scale,bottom=getBounds().height()-scale;
            Path body=new Path();body.addRoundRect(new RectF(left,top,right,bottom),16*scale,16*scale,Path.Direction.CW);
            float y=(top+bottom)/2;Path tail=new Path();tail.moveTo(left+scale,y-12*scale);tail.lineTo(2*scale,y);tail.lineTo(left+scale,y);tail.close();
            body.op(tail,Path.Op.UNION);p.setColor(SNOW);p.setStyle(Paint.Style.FILL);c.drawPath(body,p);
            p.setColor(SWAN);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2*scale);p.setStrokeJoin(Paint.Join.ROUND);c.drawPath(body,p);p.setStyle(Paint.Style.FILL);
        }
        @Override public void setAlpha(int a){p.setAlpha(a);}
        @Override public void setColorFilter(ColorFilter f){p.setColorFilter(f);}
        @Override public int getOpacity(){return PixelFormat.TRANSLUCENT;}
    }

    /** D vector geometry calibrated to the sampled source arrow; touch target stays48dp. */
    public static class BackArrow extends View {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        public BackArrow(Context c){super(c);setContentDescription("Back");setFocusable(true);setClickable(true);}
        @Override protected void onDraw(Canvas c){float cx=getWidth()/2f,cy=getHeight()/2f,s=dp(getContext(),1);p.setColor(HARE);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2.5f*s);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);Path q=new Path();q.moveTo(cx-1*s,cy-8*s);q.lineTo(cx-9*s,cy);q.lineTo(cx-1*s,cy+8*s);q.moveTo(cx-9*s,cy);q.lineTo(cx+8*s,cy);c.drawPath(q,p);}
    }

    /** Original drawing for Paperlane only; no source brand/character assets are used. */
    public static class PaperMark extends View {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        public PaperMark(Context c) { super(c);setContentDescription("Paperlane collection"); }
        @Override protected void onDraw(Canvas c) {
            float s=Math.min(getWidth(),getHeight())/100f;c.save();c.translate((getWidth()-100*s)/2,(getHeight()-100*s)/2);c.scale(s,s);
            p.setColor(SELECTED);c.drawRoundRect(9,23,91,88,15,15,p);
            p.setColor(BLUE_LIP);c.drawRoundRect(24,11,74,82,9,9,p);
            p.setColor(SNOW);c.drawRoundRect(20,7,70,77,9,9,p);
            p.setColor(SWAN);for(int y=25;y<=51;y+=13)c.drawRoundRect(30,y,60,y+4,2,2,p);
            p.setColor(GREEN);c.drawCircle(73,72,17,p);p.setColor(SNOW);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setStrokeCap(Paint.Cap.ROUND);
            Path check=new Path();check.moveTo(65,72);check.lineTo(71,78);check.lineTo(81,66);c.drawPath(check,p);p.setStyle(Paint.Style.FILL);c.restore();
        }
    }
}

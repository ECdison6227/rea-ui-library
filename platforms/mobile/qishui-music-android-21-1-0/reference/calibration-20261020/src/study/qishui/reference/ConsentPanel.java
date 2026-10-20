package study.qishui.reference;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONObject;

/** Native Android D reconstruction using A geometry/colors and B dp declarations. */
final class ConsentPanel extends FrameLayout {
    static final int SURFACE = Color.rgb(235,249,242), INK = Color.rgb(18,18,18), GREEN = Color.rgb(0,203,100);
    interface Actions { void accept(); void decline(); }
    private final Activity activity;
    private final TextView title, body, decline, accept;
    private final ScrollView scroll;
    private final float density;
    private final Actions actions;
    ConsentPanel(Activity context, Actions callback) {
        super(context); activity=context;actions=callback;density=getResources().getDisplayMetrics().density;
        setId(R.id.consent_panel);setClipToOutline(true);setBackground(round(Color.WHITE,28));
        View tint=new View(context);tint.setBackgroundColor(0x1402B55A);addView(tint,new LayoutParams(-1,-1));
        // A native element bounds at 440 dpi. These scale with density; they are not new global tokens.
        title=text(22,INK,true);title.setId(R.id.consent_title);title.setGravity(Gravity.CENTER_VERTICAL);
        title.setIncludeFontPadding(false);title.setLineSpacing(dp(30),0f);title.setLetterSpacing(0.0165f);((OpticalTextView)title).inkOffsetY=-6f*density/2.75f;
        addAt(title,dp(24),dp(32),dp(264),sourcePx(82));
        View topDivider=new View(context);topDivider.setBackgroundColor(0xFFD4E1DA);addAt(topDivider,0,sourcePx(220),-1,sourcePx(3));
        FrameLayout bodyFrame=new FrameLayout(context);addAt(bodyFrame,0,sourcePx(223),-1,sourcePx(826));
        scroll=new ScrollView(context);scroll.setId(R.id.consent_scroll);scroll.setVerticalScrollBarEnabled(false);scroll.setFillViewport(false);scroll.setOverScrollMode(OVER_SCROLL_NEVER);
        LayoutParams scrollParams=new LayoutParams(dp(264),-1);scrollParams.leftMargin=dp(24);bodyFrame.addView(scroll,scrollParams);
        body=text(16,INK,false);body.setId(R.id.consent_body);body.setFocusable(true);body.setIncludeFontPadding(true);body.setPadding(0,dp(12),0,dp(10));body.setLineSpacing(dp(26),0f);
        body.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);body.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
        scroll.addView(body,new ScrollView.LayoutParams(-1,-2));
        View fade=new View(context);fade.setId(R.id.consent_fade);fade.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x33EBF9F2,SURFACE}));
        LayoutParams fadeParams=new LayoutParams(-1,dp(30),Gravity.BOTTOM);bodyFrame.addView(fade,fadeParams);
        View bottomDivider=new View(context);bottomDivider.setBackgroundColor(0xFFD4E1DA);addAt(bottomDivider,0,sourcePx(1049),-1,sourcePx(3));
        decline=text(14,Color.rgb(188,199,194),true);decline.setId(R.id.consent_decline);decline.setGravity(Gravity.CENTER);decline.setIncludeFontPadding(false);decline.setClickable(true);decline.setEnabled(true);
        decline.setLetterSpacing(0.0390f);((OpticalTextView)decline).inkOffsetY=-1f*density/2.75f;
        decline.setOnClickListener(view->actions.decline());addAt(decline,dp(24),sourcePx(1085),dp(264),dp(20));
        accept=text(14,INK,true);accept.setId(R.id.consent_accept);accept.setGravity(Gravity.CENTER);accept.setIncludeFontPadding(false);accept.setBackground(round(GREEN,12));accept.setClickable(true);accept.setEnabled(true);
        accept.setLetterSpacing(0.1558f);((OpticalTextView)accept).inkOffsetY=-2f*density/2.75f;
        accept.setOnClickListener(view->actions.accept());addAt(accept,dp(24),sourcePx(1172),dp(264),dp(48));
        // Pressed appearance and animation are intentionally not asserted as source behavior.
    }
    void bind(JSONObject copy) {
        title.setText(CopyRepository.text(copy,"title"));decline.setText(CopyRepository.text(copy,"decline"));accept.setText(CopyRepository.text(copy,"accept"));
        String value=CopyRepository.text(copy,"body");SpannableString styled=new SpannableString(value);
        for(String term:CopyRepository.bold(copy)) {int start=0;while(!term.isEmpty()&&(start=value.indexOf(term,start))>=0){styled.setSpan(new StyleSpan(Typeface.BOLD),start,start+term.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);start+=term.length();}}
        body.setText(styled);scroll.post(()->scroll.scrollTo(0,0));
    }
    private TextView text(float size,int color,boolean bold) {TextView view=new OpticalTextView(activity);view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size);view.setTextColor(color);view.setTypeface(Typeface.DEFAULT,bold?Typeface.BOLD:Typeface.NORMAL);return view;}
    /** D optical correction candidate; keeps original layout bounds. Not recovered font metrics. */
    private static final class OpticalTextView extends TextView {
        float inkOffsetY=0f;
        OpticalTextView(Activity context){super(context);}
        @Override protected void onDraw(Canvas canvas){int save=canvas.save();canvas.translate(0,inkOffsetY);super.onDraw(canvas);canvas.restoreToCount(save);}
    }
    private void addAt(View child,int left,int top,int width,int height) {LayoutParams p=new LayoutParams(width,height);p.leftMargin=left;p.topMargin=top;addView(child,p);}
    int dp(float value) {return Math.round(value*density);}
    private int sourcePx(float value) {return Math.round(value*density/2.75f);}
    static GradientDrawable round(int color,float radiusDp,View view) {GradientDrawable drawable=new GradientDrawable();drawable.setColor(color);drawable.setCornerRadius(radiusDp*view.getResources().getDisplayMetrics().density);return drawable;}
    private GradientDrawable round(int color,float radiusDp) {return round(color,radiusDp,this);}
}

package org.reastyle.nativeexample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.content.res.Configuration;
import android.animation.ValueAnimator;
import android.view.animation.PathInterpolator;
import android.text.style.StyleSpan;
import java.io.FileOutputStream;
import org.json.JSONObject;
import org.json.JSONArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static org.reastyle.nativeexample.Components.*;

/** Independent D reference. It never calls the source backend and needs no Android permissions. */
public final class MainActivity extends Activity {
    private final FlowState state = new FlowState();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Typeface regular, bold;
    private boolean systemSourceFont, hold, failNext;
    private LinearLayout root;
    private Dialog modal;
    private Components.Button primary;
    private Components.ChoiceCard[] choices;
    private TextView validation;
    private View transferGuide;
    private boolean stopping;
    private boolean chinese;
    private String requestedFont="duo",specialScene="";
    private boolean firstRender=true;

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        Window w=getWindow();w.setStatusBarColor(SNOW);w.setNavigationBarColor(SNOW);
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        readIntent(getIntent());
        if(saved!=null){
            state.screen=FlowState.Screen.values()[saved.getInt("screen",0)];
            state.course=saved.getInt("course",-1);state.items=saved.getInt("items",0);
            state.name=saved.getString("name","My collection");
            state.validation=saved.getString("validation","");
        }
        render();
    }
    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);setIntent(intent);readIntent(intent);render();
    }
    private void readIntent(Intent intent) {
        systemSourceFont="system".equals(intent.getStringExtra("font"));
        requestedFont=intent.getStringExtra("font")==null?"duo":intent.getStringExtra("font");
        chinese="zh".equals(intent.getStringExtra("language"));Components.reduceMotion=intent.getBooleanExtra("reduceMotion",false);
        specialScene=intent.getStringExtra("scene");
        hold=intent.getBooleanExtra("hold",false);failNext=intent.getBooleanExtra("fail",false);
        state.open(intent.getStringExtra("scene"));
    }
    @Override protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);out.putInt("screen",state.screen.ordinal());
        out.putInt("course",state.course);out.putInt("items",state.items);
        out.putString("name",state.name);out.putString("validation",state.validation);
    }
    @Override protected void onStart() { super.onStart();stopping=false;scheduleTransfer(); }
    @Override protected void onStop() { stopping=true;handler.removeCallbacksAndMessages(null);super.onStop(); }
    @Override protected void onDestroy() { if(modal!=null)modal.dismiss();handler.removeCallbacksAndMessages(null);super.onDestroy(); }
    @Override public void onConfigurationChanged(Configuration c){super.onConfigurationChanged(c);render();}
    @Override public void onBackPressed() {
        if(modal!=null && modal.isShowing()) {
            if(state.screen==FlowState.Screen.PRIVACY||state.screen==FlowState.Screen.AGE)return;
            modal.dismiss();modal=null;return;
        }
        handler.removeCallbacksAndMessages(null);
        if(state.back())render();else finish();
    }

    private int d(float value){return dp(this,value);}
    private String tr(String en,String zh){return chinese?zh:en;}
    private boolean compact(){return getResources().getConfiguration().fontScale<=1.15f&&getResources().getConfiguration().screenWidthDp>=340;}
    private LinearLayout column(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.VERTICAL);return v;}
    private LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w,h);}
    private TextView text(String label,int size,int color,boolean heavy){
        TextView v=new TextView(this);v.setText(label);v.setTextSize(size);v.setTextColor(color);
        v.setTypeface(heavy?bold:regular);v.setIncludeFontPadding(false);v.setLineSpacing(0,1.2f);return v;
    }
    private void gap(LinearLayout parent,int height){parent.addView(new View(this),lp(1,d(height)));}
    private void useFonts(){
        if(state.isTransfer()||systemSourceFont){regular=Typeface.create("sans-serif",Typeface.NORMAL);bold=Typeface.create("sans-serif",Typeface.BOLD);}
        else {String family="din".equals(requestedFont)?"din_next_for_duolingo":"duolingosans";regular=Typeface.createFromAsset(getAssets(),"source-fonts/"+family+(family.equals("duolingosans")?"_regular":"")+".otf");bold=Typeface.createFromAsset(getAssets(),"source-fonts/"+family+"_bold.otf");}
    }
    private void render(){
        handler.removeCallbacksAndMessages(null);if(modal!=null){modal.dismiss();modal=null;}
        boolean dark=(getResources().getConfiguration().uiMode&Configuration.UI_MODE_NIGHT_MASK)==Configuration.UI_MODE_NIGHT_YES;
        Components.configurePalette(dark);getWindow().setStatusBarColor(SNOW);getWindow().setNavigationBarColor(SNOW);getWindow().getDecorView().setSystemUiVisibility(dark?0:View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        useFonts();root=column();root.setBackgroundColor(SNOW);root.setFocusableInTouchMode(true);setContentView(root);root.requestFocus();
        primary=null;choices=null;validation=null;transferGuide=null;
        if("motion-demo".equals(specialScene)){showMotionDemo();return;}
        if("design-families".equals(specialScene)){showFamilies();return;}
        switch(state.screen){
            case PRIVACY: case AGE: showPolicyBackground();break;
            case WELCOME: showWelcome();break;
            case COURSE: showCourse();break;
            case BUILDING: showBuilding();break;
            case COMPONENTS: showComponents();break;
            case TRANSFER: showTransfer();break;
            case TRANSFER_WORKING: showTransferWorking();break;
            case TRANSFER_ERROR: showTransferResult(false);break;
            case TRANSFER_DONE: showTransferResult(true);break;
            default: showLauncher();
        }
        writeFontMetrics();
        if(!firstRender&&Components.motionEnabled()){root.setAlpha(0);root.setTranslationX(d(16));root.animate().alpha(1).translationX(0).setDuration(200).setInterpolator(new PathInterpolator(.2f,0,0,1)).start();}
        firstRender=false;
        scheduleTransfer();
    }

    private void open(String scene){specialScene=scene;state.open(scene);render();}
    private Components.Button button(String label,boolean blue,boolean clear){
        return new Components.Button(this,label,bold,blue?BLUE:GREEN,blue?BLUE_LIP:GREEN_LIP,clear);
    }
    private void addFooter(String label,boolean enabled,Runnable action){
        LinearLayout footer=column();footer.setPadding(d(16),d(16),d(16),d(16));footer.setBackgroundColor(SNOW);
        primary=button(label,false,false);primary.setId(R.id.primaryButton);primary.setEnabled(enabled);
        primary.setOnClickListener(v->action.run());footer.addView(primary,lp(-1,compact()?d(52):-2));root.addView(footer,lp(-1,-2));
    }
    private void addToolbar(boolean brand){
        gap(root,16);LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);
        View back=new Components.BackArrow(this);back.setId(R.id.backButton);
        back.setContentDescription("Back");back.setFocusable(true);back.setOnClickListener(v->onBackPressed());
        LinearLayout.LayoutParams b=lp(d(40),d(48));b.leftMargin=d(4);bar.addView(back,b);
        if(brand){TextView name=text(PaperlaneContent.BRAND,23,EEL,true);bar.addView(name,lp(0,-2));LinearLayout.LayoutParams n=(LinearLayout.LayoutParams)name.getLayoutParams();n.weight=1;}
        else {Components.Progress progress=new Components.Progress(this,.12f);LinearLayout.LayoutParams p=lp(0,d(16));p.weight=1;p.leftMargin=d(4);p.rightMargin=d(16);bar.addView(progress,p);}
        root.addView(bar,lp(-1,d(48)));
    }
    private ImageView sourceImage(int resource){ImageView image=new ImageView(this);image.setImageResource(resource);image.setScaleType(ImageView.ScaleType.FIT_CENTER);image.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);return image;}
    private void addGuide(String message,boolean transfer){
        FrameLayout guide=new FrameLayout(this);
        View mark=transfer?new Components.PaperMark(this):sourceImage(R.drawable.duo_beginner);
        FrameLayout.LayoutParams art=new FrameLayout.LayoutParams(d(144),d(109),Gravity.LEFT|Gravity.CENTER_VERTICAL);guide.addView(mark,art);
        TextView bubble=text(message,19,EEL,false);bubble.setPadding(d(32),d(16),d(16),d(16));bubble.setGravity(Gravity.CENTER_VERTICAL);bubble.setBackground(new Components.Bubble(this));
        FrameLayout.LayoutParams p=new FrameLayout.LayoutParams(-1,-2,Gravity.CENTER_VERTICAL);p.leftMargin=d(124);p.rightMargin=d(27);guide.addView(bubble,p);
        root.addView(guide,lp(-1,compact()?d(109):d(109*Math.min(2,getResources().getConfiguration().fontScale))));gap(root,8);
        if(transfer)transferGuide=guide;
    }
    private void showLauncher(){
        regular=Typeface.create("sans-serif",0);bold=Typeface.create("sans-serif",1);
        ScrollView scroll=new ScrollView(this);LinearLayout page=column();page.setPadding(d(24),d(32),d(24),d(24));scroll.addView(page);root.addView(scroll,lp(-1,-1));
        page.addView(text("Native style reference",28,EEL,true));gap(page,12);
        page.addView(text("Local Android samples for the Duolingo 6.80.6-china research library.",17,WOLF,false));gap(page,24);
        launcherCard(page,"Source journey","Policy → age → greeting → course choice","source-privacy");
        launcherCard(page,"Course choice","Unselected, selected, building and Back","source-course");
        launcherCard(page,"Paperlane","A separate document-organizing sample","transfer-home");
        launcherCard(page,"Components","Ten native component specimens","components");
        launcherCard(page,"Design families","Plans, feedback, metrics and settings","design-families");
        launcherCard(page,"Motion","Progress, feedback, sheet and reduced motion","motion-demo");
        gap(page,12);page.addView(text("Independent reference • Source behavior is only partially observed • All sample data is local",13,WOLF,false));
    }
    private void launcherCard(LinearLayout page,String title,String subtitle,String scene){
        LinearLayout card=column();card.setPadding(d(16),d(16),d(16),d(16));card.setBackground(new Face(this,SNOW,SWAN,16,4,2));
        card.addView(text(title,20,EEL,true));gap(card,6);card.addView(text(subtitle,16,WOLF,false));card.setFocusable(true);card.setOnClickListener(v->open(scene));
        LinearLayout.LayoutParams p=lp(-1,-2);p.bottomMargin=d(16);page.addView(card,p);
    }

    private void showCourse(){
        addToolbar(false);addGuide(tr("What would you like to learn?","你想学习哪一门语言？"),false);
        ScrollView scroll=new ScrollView(this);scroll.setId(R.id.courseList);scroll.setFillViewport(false);scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayout list=column();list.setPadding(d(16),0,d(16),0);scroll.addView(list);
        FrameLayout headingRow=new FrameLayout(this);TextView heading=text(tr("For English speakers","以中文为母语的学习者"),19,EEL,true);heading.setGravity(Gravity.CENTER_VERTICAL);heading.setPadding(0,0,d(36),0);headingRow.addView(heading,new FrameLayout.LayoutParams(-1,-1));
        View chevron=new View(this){final Paint pen=new Paint(Paint.ANTI_ALIAS_FLAG);@Override protected void onDraw(android.graphics.Canvas c){pen.setColor(HARE);pen.setStrokeWidth(d(3));pen.setStyle(Paint.Style.STROKE);pen.setStrokeCap(Paint.Cap.ROUND);android.graphics.Path q=new android.graphics.Path();q.moveTo(d(7),d(20));q.lineTo(d(15),d(12));q.lineTo(d(23),d(20));c.drawPath(q,pen);}};
        FrameLayout.LayoutParams chevronP=new FrameLayout.LayoutParams(d(32),d(32),Gravity.RIGHT|Gravity.CENTER_VERTICAL);headingRow.addView(chevron,chevronP);headingRow.setContentDescription(tr("Interface language","界面语言"));headingRow.setFocusable(true);headingRow.setOnClickListener(v->new android.app.AlertDialog.Builder(this).setTitle(tr("Interface language · local sample","界面语言 · 本地样例")).setItems(new String[]{"English","中文"},(dialog,which)->{chinese=which==1;render();}).show());
        list.addView(headingRow,lp(-1,compact()?d(62):d(62*Math.min(2,getResources().getConfiguration().fontScale))));
        String[] labels=chinese?new String[]{"西班牙语","法语","德语","意大利语","日语"}:new String[]{"Spanish","French","German","Italian","Japanese"};
        int[] flags={R.drawable.flag_es,R.drawable.flag_fr,R.drawable.flag_de,R.drawable.flag_it,R.drawable.flag_ja};
        int[] ids={R.id.spanishCard,R.id.frenchCard,R.id.germanCard,R.id.italianCard,R.id.japaneseCard};
        choices=new Components.ChoiceCard[5];
        for(int i=0;i<labels.length;i++){
            final int index=i;Components.ChoiceCard card=new Components.ChoiceCard(this,labels[i],bold,false);card.setId(ids[i]);
            ImageView flag=sourceImage(flags[i]);flag.setTranslationY(d(1));card.label().setTranslationY(d(1));LinearLayout.LayoutParams icon=lp(d(41),d(32));icon.rightMargin=d(16);card.addView(flag,icon);card.addLabel();
            card.setChecked(state.course==i);card.setOnClickListener(v->{state.selectCourse(index);updateCourses();});
            LinearLayout.LayoutParams p=lp(-1,compact()?d(64):-2);if(!compact())card.setPadding(d(16),d(12),d(16),d(16));p.bottomMargin=d(12);list.addView(card,p);choices[i]=card;
        }
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        addFooter(tr("Continue","继续"),state.canContinueCourse(),()->{if(state.startCourse())render();});
    }
    private void updateCourses(){for(int i=0;i<choices.length;i++)choices[i].setChecked(i==state.course);primary.setEnabled(state.canContinueCourse());}
    private void showWelcome(){
        addToolbar(false);FrameLayout center=new FrameLayout(this);
        ImageView image=sourceImage(R.drawable.duo_happy);FrameLayout.LayoutParams art=new FrameLayout.LayoutParams(d(160),d(160),Gravity.CENTER);art.topMargin=d(20);center.addView(image,art);
        TextView greeting=text("Hi there! I’m Duo!",19,EEL,false);greeting.setGravity(Gravity.CENTER);greeting.setPadding(d(24),d(16),d(16),d(16));greeting.setBackground(new Components.Bubble(this));
        // At the observed 360x568dp client, content starts at screen y=88dp.
        // A top offset of102dp places the bubble near source y=190dp without negative centered margins.
        FrameLayout.LayoutParams b=new FrameLayout.LayoutParams(d(210),-2,Gravity.TOP|Gravity.CENTER_HORIZONTAL);b.topMargin=d(102);center.addView(greeting,b);
        center.addOnLayoutChangeListener((v,l,t,r,bottom,oldL,oldT,oldR,oldBottom)->{
            int safeTop=Math.min(d(102),Math.max(0,center.getHeight()-greeting.getMeasuredHeight()));
            FrameLayout.LayoutParams current=(FrameLayout.LayoutParams)greeting.getLayoutParams();
            if(current.topMargin!=safeTop){current.topMargin=safeTop;greeting.setLayoutParams(current);}
        });
        root.addView(center,new LinearLayout.LayoutParams(-1,0,1));addFooter("Continue",true,()->open("source-course"));
    }
    private void showBuilding(){
        addToolbar(false);FrameLayout content=new FrameLayout(this);
        LinearLayout group=column();group.setGravity(Gravity.CENTER);group.setPadding(d(32),0,d(32),d(28));
        ImageView image=sourceImage(R.drawable.duo_happy);group.addView(image,lp(d(150),d(130)));gap(group,32);
        TextView title=text("COURSE BUILDING…",25,HARE,true);title.setLetterSpacing(.05f);title.setGravity(Gravity.CENTER);title.setId(R.id.statusLabel);group.addView(title,lp(-1,-2));gap(group,32);
        String course=state.course>=0?new String[]{"Spanish","French","German","Italian","Japanese"}[state.course]:"Spanish";
        String copy="Get ready to join the 20 million people currently learning "+course+" with Duolingo!";
        TextView message=text(copy,22,WOLF,false);SpannableString styled=new SpannableString(copy);int start=copy.indexOf("20 million people");styled.setSpan(new android.text.style.TypefaceSpan(bold),start,start+17,0);message.setText(styled);message.setGravity(Gravity.CENTER);message.setLineSpacing(0,1.2f);group.addView(message,lp(-1,-2));
        content.addView(group,new FrameLayout.LayoutParams(-1,-2,Gravity.CENTER));root.addView(content,new LinearLayout.LayoutParams(-1,0,1));
        if(compact()&&getResources().getConfiguration().screenWidthDp==360)content.addOnLayoutChangeListener((v,l,t,r,b,ol,ot,or,ob)->{int[] rootAt=new int[2],titleAt=new int[2];root.getLocationOnScreen(rootAt);title.getLocationOnScreen(titleAt);group.setTranslationY(group.getTranslationY()+rootAt[1]+d(322.5f)-titleAt[1]);});
    }
    private void showPolicyBackground(){
        FrameLayout page=new FrameLayout(this);ImageView art=sourceImage(R.drawable.duo_happy);
        FrameLayout.LayoutParams p=new FrameLayout.LayoutParams(d(130),d(130),Gravity.CENTER);p.bottomMargin=d(130);page.addView(art,p);root.addView(page,lp(-1,-1));
        final boolean age=state.screen==FlowState.Screen.AGE;
        String body=age?"Duolingo cares about the protection of data for underage minors. Please confirm your age so that we can provide you with age appropriate data protection measures.":"Please read and understand our Terms of Service and Privacy Policy. Duolingo will use our Privacy Policy to help you understand how we collect, use, and store your personal data.";
        policySheet(age?"Underage Data Policy":"Duolingo Privacy Policy",body,age?"I AM 14 AND ABOVE":"AGREE AND CONTINUE",age?"I AM UNDER 14":"DECLINE",()->{
            state.screen=age?FlowState.Screen.WELCOME:FlowState.Screen.AGE;render();
        },this::finish);
    }
    private void policySheet(String title,String body,String ok,String cancel,Runnable positive,Runnable negative){
        LinearLayout panel=sheetPanel();
        TextView heading=text(title,25,EEL,true);heading.setId(R.id.modalTitle);heading.setGravity(Gravity.CENTER);panel.addView(heading,lp(-1,-2));gap(panel,20);
        TextView content=text(body,19,WOLF,false);content.setGravity(Gravity.CENTER);content.setLineSpacing(0,1.42f);content.setId(R.id.modalContent);
        if(state.screen==FlowState.Screen.PRIVACY){SpannableString spans=new SpannableString(body);for(String link:new String[]{"Terms of Service","Privacy Policy"}){int start=body.indexOf(link);if(start>=0)spans.setSpan(new ForegroundColorSpan(BLUE),start,start+link.length(),0);}content.setText(spans);}
        panel.addView(content,lp(-1,-2));gap(panel,24);
        Components.Button agree=button(ok,true,false);agree.setId(R.id.primaryButton);agree.setOnClickListener(v->positive.run());panel.addView(agree,lp(-1,d(52)));gap(panel,16);
        Components.Button decline=button(cancel,true,true);decline.setId(R.id.secondaryButton);decline.setOnClickListener(v->negative.run());panel.addView(decline,lp(-1,d(52)));
        showSheet(panel,false);
    }
    private LinearLayout sheetPanel(){
        LinearLayout panel=column();panel.setPadding(d(16),d(24),d(16),d(16));
        GradientDrawable bg=new GradientDrawable();bg.setColor(SNOW);float r=d(16);bg.setCornerRadii(new float[]{r,r,r,r,0,0,0,0});panel.setBackground(bg);return panel;
    }
    private void showSheet(LinearLayout panel,boolean cancelable){
        modal=new Dialog(this);modal.requestWindowFeature(Window.FEATURE_NO_TITLE);modal.setContentView(panel);
        modal.setCancelable(cancelable);modal.setCanceledOnTouchOutside(cancelable);
        Window w=modal.getWindow();w.setBackgroundDrawableResource(android.R.color.transparent);w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams attr=w.getAttributes();attr.gravity=Gravity.BOTTOM;attr.width=-1;attr.height=-2;attr.dimAmount=.4f;w.setAttributes(attr);
        w.setNavigationBarColor(SNOW);w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        modal.show();w.setLayout(-1,-2);panel.setAccessibilityPaneTitle("Confirmation");
        if(Components.motionEnabled()){panel.setTranslationY(d(80));panel.setAlpha(0);panel.animate().translationY(0).alpha(1).setDuration(220).setInterpolator(new PathInterpolator(.2f,0,0,1)).start();}
    }

    private void showTransfer(){
        addToolbar(true);addGuide(tr(PaperlaneContent.GUIDE,"先把需要的资料放到一起。"),true);
        ScrollView scroll=new ScrollView(this);LinearLayout form=column();form.setPadding(d(16),d(8),d(16),d(8));scroll.addView(form);
        TextView intro=text(tr(PaperlaneContent.INTRO,"选择资料并为集合命名。你可以返回继续编辑。"),17,WOLF,false);form.addView(intro);gap(form,20);
        form.addView(text(tr("Collection name","集合名称"),17,EEL,true));gap(form,8);
        EditText input=new EditText(this);input.setId(R.id.collectionName);input.setText(state.name);input.setSingleLine(true);input.setTextSize(19);input.setTypeface(regular);input.setTextColor(EEL);input.setHint("Name your collection");input.setHintTextColor(HARE);
        input.setPadding(d(16),d(12),d(16),d(12));input.setBackground(new Face(this,POLAR,SWAN,16,2,2));input.setSelectAllOnFocus(false);input.setContentDescription("Collection name");
        input.setOnFocusChangeListener((v,focused)->{if(focused)handler.postDelayed(()->{if(!input.hasFocus())return;if(transferGuide!=null)transferGuide.setVisibility(View.GONE);scroll.post(()->{scroll.scrollTo(0,Math.max(0,input.getTop()-d(40)));((android.view.inputmethod.InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(input,android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);});},150);else if(transferGuide!=null)transferGuide.setVisibility(View.VISIBLE);});
        form.addView(input,lp(-1,compact()?d(56):-2));validation=text(state.validation,14,RED,false);validation.setVisibility(state.validation.isEmpty()?View.GONE:View.VISIBLE);form.addView(validation);gap(form,20);
        form.addView(text(tr("Choose your items","选择资料"),19,EEL,true));gap(form,12);
        String[] names=chinese?new String[]{"调研笔记","设计草稿","参考资料"}:PaperlaneContent.ITEMS;int[] ids=PaperlaneContent.ITEM_IDS;choices=new Components.ChoiceCard[3];
        for(int i=0;i<names.length;i++){
            final int bit=i;Components.ChoiceCard card=new Components.ChoiceCard(this,names[i],bold,true);card.setId(ids[i]);
            TextView icon=text(new String[]{"01","02","03"}[i],15,BLUE,true);icon.setGravity(Gravity.CENTER);icon.setBackground(new Face(this,SELECTED,SELECTED,8,0,0));
            LinearLayout.LayoutParams iconP=lp(d(40),d(34));iconP.rightMargin=d(16);card.addView(icon,iconP);card.addLabel();card.setChecked((state.items&(1<<i))!=0);
            card.setOnClickListener(v->{state.toggleItem(bit);updateTransfer();});LinearLayout.LayoutParams p=lp(-1,compact()?d(64):-2);if(!compact())card.setPadding(d(16),d(12),d(16),d(16));p.bottomMargin=d(12);form.addView(card,p);choices[i]=card;
        }
        input.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int count,int after){}public void onTextChanged(CharSequence s,int st,int before,int count){state.name=s.toString();if(!state.validation.isEmpty()){state.validation="";validation.setVisibility(View.GONE);}}public void afterTextChanged(Editable e){}});
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));addFooter(transferCta(),state.items!=0,this::submitTransfer);
    }
    private String transferCta(){return chinese?(state.items==0?"选择资料":"整理 "+state.itemCount()+" 份资料"):(state.items==0?"Choose your items":"Organize "+state.itemCount()+" item"+(state.itemCount()==1?"":"s"));}
    private void updateTransfer(){for(int i=0;i<3;i++)choices[i].setChecked((state.items&(1<<i))!=0);primary.setText(transferCta());primary.setEnabled(state.items!=0);}
    private void submitTransfer(){
        if(state.startTransfer())render();
        else if(validation!=null&&!state.validation.isEmpty()){validation.setText(state.validation);validation.setVisibility(View.VISIBLE);findViewById(R.id.collectionName).requestFocus();}
    }
    private LinearLayout transferCenter(String heading,String detail){
        LinearLayout center=column();center.setGravity(Gravity.CENTER);center.setPadding(d(28),0,d(28),d(20));
        center.addView(new Components.PaperMark(this),lp(d(132),d(132)));gap(center,24);
        TextView title=text(heading,25,EEL,true);title.setGravity(Gravity.CENTER);title.setId(R.id.statusLabel);center.addView(title,lp(-1,-2));gap(center,16);
        TextView message=text(detail,19,WOLF,false);message.setGravity(Gravity.CENTER);center.addView(message,lp(-1,-2));root.addView(center,new LinearLayout.LayoutParams(-1,0,1));return center;
    }
    private void showTransferWorking(){addToolbar(true);LinearLayout c=transferCenter(tr("Putting things in order…","正在整理资料…"),tr("Your collection will be ready in a moment.","资料会保留在这个集合中。"));gap(c,24);Components.Progress progress=new Components.Progress(this,.1f);c.addView(progress,lp(-1,d(16)));progress.animateProgress(.75f);}
    private void scheduleTransfer(){
        handler.removeCallbacksAndMessages(null);
        if(stopping||hold||state.screen!=FlowState.Screen.TRANSFER_WORKING)return;
        final long token=state.pendingGeneration();final boolean fail=failNext;
        handler.postDelayed(()->{if(state.completeTransfer(token,fail)){failNext=false;render();}},1200); // D local fixture, not an observed duration.
    }
    private void showTransferResult(boolean success){
        addToolbar(true);transferCenter(success?tr("A place for everything.","资料已整理完成。"):tr("Let’s try that again.","再试一次。"),success?tr(state.itemCount()+" item"+(state.itemCount()==1?"":"s")+" organized in “"+state.name.trim()+"”.","已整理 "+state.itemCount()+" 份资料。"):tr("We couldn’t organize these items. Your choices are still here.","暂时无法整理，已保留你的选择。"));
        addFooter(success?"Back to collection":"Try again",true,()->{if(success){state.screen=FlowState.Screen.TRANSFER;render();}else if(state.startTransfer())render();});
    }

    private void showComponents(){
        addToolbar(false);ScrollView scroll=new ScrollView(this);LinearLayout specimens=column();specimens.setPadding(d(16),d(16),d(16),d(24));scroll.addView(specimens);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        specimens.addView(text("Native components",25,EEL,true));gap(specimens,20);
        specimenTitle(specimens,"1 · Action button");Components.Button active=button("Continue",false,false);specimens.addView(active,lp(-1,d(52)));gap(specimens,8);
        Components.Button disabled=button("Continue",false,false);disabled.setEnabled(false);specimens.addView(disabled,lp(-1,d(52)));
        specimenTitle(specimens,"2 · Navigation");TextView nav=text("‹  Back",19,HARE,true);nav.setFocusable(true);nav.setOnClickListener(v->onBackPressed());specimens.addView(nav,lp(-1,d(48)));
        specimenTitle(specimens,"3 · Choice card");Components.ChoiceCard option=new Components.ChoiceCard(this,"Choose this option",bold,false);option.addLabel();option.setOnClickListener(v->option.toggle());specimens.addView(option,lp(-1,d(64)));
        specimenTitle(specimens,"4 · Check box");CheckBox check=new CheckBox(this);check.setText("Remember this choice");check.setTextColor(EEL);check.setTextSize(19);check.setTypeface(regular);check.setButtonDrawable(R.drawable.checkbox);check.setPadding(d(8),d(8),d(8),d(8));specimens.addView(check,lp(-1,-2));
        specimenTitle(specimens,"5 · Text input");EditText edit=new EditText(this);edit.setHint("Write a short note");edit.setTextSize(19);edit.setTypeface(regular);edit.setTextColor(EEL);edit.setHintTextColor(HARE);edit.setPadding(d(16),d(16),d(16),d(16));edit.setBackground(new Face(this,POLAR,SWAN,16,2,2));specimens.addView(edit,lp(-1,d(88)));
        specimenTitle(specimens,"6 · Progress");specimens.addView(new Components.Progress(this,.45f),lp(-1,d(16)));
        specimenTitle(specimens,"7 · Loading state");TextView loading=text("Getting ready…",19,HARE,true);loading.setGravity(Gravity.CENTER);specimens.addView(loading,lp(-1,d(64)));
        specimenTitle(specimens,"8 · Modal confirmation");Components.Button modalButton=button("Open confirmation",true,false);modalButton.setOnClickListener(v->demoConfirmation());specimens.addView(modalButton,lp(-1,d(52)));
        specimenTitle(specimens,"9 · Pointing card");TextView tip=text("Choose one option, then continue.",19,EEL,false);tip.setPadding(d(30),d(16),d(16),d(16));tip.setBackground(new Components.Bubble(this));specimens.addView(tip,lp(-1,-2));
        specimenTitle(specimens,"10 · Guidance with next action");LinearLayout guide=column();guide.addView(text("Start with a small step.",25,EEL,true));gap(guide,12);guide.addView(text("Choose what matters first. You can return to the other items later.",19,WOLF,false));gap(guide,16);Components.Button next=button("See Paperlane",false,false);next.setOnClickListener(v->open("transfer-home"));guide.addView(next,lp(-1,d(52)));specimens.addView(guide);
    }
    private void specimenTitle(LinearLayout parent,String title){gap(parent,24);parent.addView(text(title,17,WOLF,true));gap(parent,12);}
    private void demoConfirmation(){
        LinearLayout panel=sheetPanel();TextView title=text("Keep your changes?",25,EEL,true);title.setGravity(Gravity.CENTER);panel.addView(title);gap(panel,16);
        TextView copy=text("You can keep this note or return to editing.",19,WOLF,false);copy.setGravity(Gravity.CENTER);panel.addView(copy);gap(panel,24);
        Components.Button keep=button("Keep changes",true,false);keep.setOnClickListener(v->{modal.dismiss();modal=null;});panel.addView(keep,lp(-1,d(52)));gap(panel,8);
        Components.Button cancel=button("Return to editing",true,true);cancel.setOnClickListener(v->{modal.dismiss();modal=null;});panel.addView(cancel,lp(-1,d(52)));showSheet(panel,true);
    }

    private void showMotionDemo(){
        addToolbar(true);LinearLayout body=column();body.setPadding(d(20),d(20),d(20),d(20));root.addView(body,lp(-1,-1));
        body.addView(text(tr("Motion and feedback","动效与反馈"),25,EEL,true));gap(body,24);
        Components.Progress progress=new Components.Progress(this,.15f);progress.setId(7001);body.addView(progress,lp(-1,d(16)));gap(body,24);
        Components.Button animate=button(tr("Animate progress","更新进度"),false,false);animate.setId(7002);animate.setOnClickListener(v->progress.animateProgress(.85f));body.addView(animate,lp(-1,d(52)));gap(body,24);
        TextView feedback=text(tr("Ready to continue","可以继续了"),19,EEL,true);feedback.setId(7003);feedback.setPadding(d(16),d(16),d(16),d(16));feedback.setBackground(new Face(this,SELECTED,SELECTED_BORDER,16,0,2));feedback.setVisibility(View.INVISIBLE);body.addView(feedback,lp(-1,-2));gap(body,16);
        Components.Button show=button(tr("Show feedback","显示反馈"),true,false);show.setId(7004);show.setOnClickListener(v->{feedback.setVisibility(View.VISIBLE);if(Components.motionEnabled()){feedback.setAlpha(0);feedback.setTranslationY(d(8));feedback.animate().alpha(1).translationY(0).setDuration(160).setInterpolator(new PathInterpolator(.2f,0,0,1)).start();}});body.addView(show,lp(-1,d(52)));gap(body,16);
        Components.Button sheet=button(tr("Open confirmation","打开确认"),true,false);sheet.setId(7005);sheet.setOnClickListener(v->demoConfirmation());body.addView(sheet,lp(-1,d(52)));
    }
    private void showFamilies(){
        addToolbar(true);ScrollView scroll=new ScrollView(this);LinearLayout body=column();body.setPadding(d(16),d(16),d(16),d(24));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        body.addView(text(tr("Design families","设计组件"),25,EEL,true));gap(body,16);
        LinearLayout offer=column();offer.setPadding(d(16),d(16),d(16),d(16));offer.setBackground(new Face(this,SNOW,SWAN,16,0,2));
        TextView band=text(tr("RECOMMENDED","推荐方案"),17,Color.WHITE,true);band.setPadding(d(12),d(8),d(12),d(8));GradientDrawable gradient=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xffab4bff,0xff448aff,0xff20c788});gradient.setCornerRadius(d(12));band.setBackground(gradient);offer.addView(band,lp(-1,-2));gap(offer,12);offer.addView(text(tr("Studio","个人工作室"),25,EEL,true));gap(offer,12);
        offer.addView(text(tr("✓  Unlimited collections\n✓  No interruptions","✓  创建任意数量的集合\n✓  专注整理资料"),19,EEL,false));gap(offer,16);Components.Button choose=button(tr("Choose this plan","选择此方案"),true,false);choose.setOnClickListener(v->demoConfirmation());offer.addView(choose,lp(-1,compact()?d(52):-2));body.addView(offer);gap(body,20);
        LinearLayout metrics=new LinearLayout(this);metrics.setOrientation(compact()?LinearLayout.HORIZONTAL:LinearLayout.VERTICAL);String[] values={"22","87%","35:58"};String[] labels=chinese?new String[]{"完成数量","完成比例","用时"}:new String[]{"TOTAL","ACCURACY","TIME"};int[] palette={0xffffc800,GREEN,BLUE};
        for(int i=0;i<3;i++){LinearLayout box=column();box.setPadding(d(8),d(8),d(8),d(12));box.setGravity(Gravity.CENTER);box.setBackground(new Face(this,SNOW,palette[i],16,0,2));box.addView(text(labels[i],13,palette[i],true));box.addView(text(values[i],23,palette[i],true));LinearLayout.LayoutParams q=compact()?lp(0,-2):lp(-1,-2);q.weight=compact()?1:0;q.rightMargin=d(6);q.bottomMargin=d(8);metrics.addView(box,q);}body.addView(metrics);gap(body,16);
        for(String label:chinese?new String[]{"偏好设置","通知","资料与隐私"}:new String[]{"Preferences","Notifications","Profile and privacy"}){TextView row=text(label+"  ›",19,EEL,true);row.setPadding(d(16),d(16),d(16),d(16));row.setBackground(new Face(this,SNOW,SWAN,12,0,2));row.setFocusable(true);body.addView(row,lp(-1,-2));gap(body,8);}
    }
    private void writeFontMetrics(){
        try{JSONObject report=new JSONObject();report.put("classification","D");report.put("fontScale",getResources().getConfiguration().fontScale);report.put("selected",requestedFont);JSONArray samples=new JSONArray();
            for(String family:new String[]{"din_next_for_duolingo","duolingosans"})for(int size:new int[]{19,20}){Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setTypeface(Typeface.createFromAsset(getAssets(),"source-fonts/"+family+"_bold.otf"));paint.setTextSize(size*getResources().getDisplayMetrics().scaledDensity);Rect bounds=new Rect();paint.getTextBounds("Spanish",0,7,bounds);JSONObject item=new JSONObject();item.put("family",family);item.put("sizeSp",size);item.put("advancePx",paint.measureText("Spanish"));item.put("inkBounds",bounds.toShortString());samples.put(item);}report.put("samples",samples);try(FileOutputStream out=openFileOutput("font-calibration.json",MODE_PRIVATE)){out.write(report.toString(2).getBytes("UTF-8"));}
        }catch(Exception e){android.util.Log.e("ReferenceMetrics","Could not write local font metrics",e);}
    }
}

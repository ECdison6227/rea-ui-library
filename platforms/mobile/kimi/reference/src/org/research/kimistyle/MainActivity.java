package org.research.kimistyle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** D: a native Android View reconstruction from documented observations. No WebView. */
public final class MainActivity extends Activity {
    private ReferenceState state = new ReferenceState();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private LinearLayout root;
    private Dialog overlay;
    private EditText composer;
    private View send;
    private int text, muted, surface, background, line;
    private final int blue = Color.rgb(35,129,255), red = Color.rgb(255,64,88);
    private String settingsReturn = "chat";
    private boolean rendering = false;
    private final String[] fontSizes = {"Follow System", "Small", "Standard", "Large", "Extra Large"};

    @Override public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        load();
        String scene = getIntent().getStringExtra("scene");
        if (scene != null) scenario(scene);
        render();
    }
    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); setIntent(intent);
        if (overlay != null) overlay.dismiss();
        String scene = intent.getStringExtra("scene");
        if (scene != null) scenario(scene);
        render();
    }
    @Override protected void onPause() { persist(); super.onPause(); }
    @Override public void onConfigurationChanged(Configuration c) { super.onConfigurationChanged(c); render(); }
    @Override public void onBackPressed() {
        if (overlay != null && overlay.isShowing()) { overlay.dismiss(); return; }
        if (state.page.equals("font")) state.abandonFont();
        else if (state.page.equals("theme")) state.page = "settings";
        else if (state.page.equals("settings")) state.page = settingsReturn;
        else if (state.page.equals("edit-document")) state.page = "documents";
        else if (!state.page.equals("chat")) state.page = "chat";
        else { persist(); super.onBackPressed(); return; }
        persist(); render();
    }
    private int dp(float v) { return Math.round(v * getResources().getDisplayMetrics().density); }
    private float scale() { return state.font.equals("Extra Large") ? 1.25f : state.font.equals("Large") ? 1.12f : state.font.equals("Small") ? .9f : 1f; }
    private GradientDrawable shape(int color, float radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private GradientDrawable border(int color, float radius) {
        GradientDrawable d = shape(color,radius); d.setStroke(dp(.7f), line); return d;
    }
    private LinearLayout column() { LinearLayout v = new LinearLayout(this); v.setOrientation(LinearLayout.VERTICAL); v.setClipChildren(false); v.setClipToPadding(false); return v; }
    private LinearLayout row() { LinearLayout v = new LinearLayout(this); v.setOrientation(LinearLayout.HORIZONTAL); v.setGravity(Gravity.CENTER_VERTICAL); v.setClipChildren(false); v.setClipToPadding(false); return v; }
    private TextView label(String value, float size, int color) {
        TextView v = new TextView(this); v.setText(value); v.setTextSize(size * scale()); v.setTextColor(color); v.setGravity(Gravity.CENTER_VERTICAL); v.setFontFeatureSettings("kern"); return v;
    }
    private void add(LinearLayout target, View view, int width, int height) { target.addView(view,new LinearLayout.LayoutParams(width < 0 ? width : dp(width),height < 0 ? height : dp(height))); }
    private void gap(LinearLayout v, int height) { add(v,new View(this),1,height); }
    private void weight(LinearLayout target, View view) { target.addView(view,new LinearLayout.LayoutParams(0,-1,1)); }
    private TextView button(String title, int color, Runnable action) {
        TextView v = label(title,16,color); v.setGravity(Gravity.CENTER); v.setPadding(dp(16),0,dp(16),0); v.setMinHeight(dp(48)); v.setBackground(shape(background,28)); v.setContentDescription(title); v.setOnClickListener(w -> action.run()); return v;
    }
    private View icon(String kind, String description, Runnable action) {
        IconView v = new IconView(kind); v.setContentDescription(description); v.setBackground(shape(surface,50)); v.setElevation(dp(3)); v.setOnClickListener(w -> action.run()); return v;
    }
    private void colours() {
        boolean dark = state.theme.equals("Dark mode") || (state.theme.equals("Follow System") && (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)==Configuration.UI_MODE_NIGHT_YES);
        text = dark ? Color.rgb(238,238,238) : Color.rgb(24,24,24);
        muted = dark ? Color.rgb(150,150,150) : Color.rgb(143,143,143);
        surface = dark ? Color.rgb(38,38,38) : Color.WHITE;
        background = dark ? Color.rgb(18,18,18) : Color.rgb(245,245,245);
        line = dark ? Color.rgb(70,70,70) : Color.rgb(222,222,222);
        getWindow().setStatusBarColor(state.page.equals("chat") ? surface : background);
        getWindow().setNavigationBarColor(state.page.equals("chat") ? surface : background);
        int layoutFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(layoutFlags | (dark ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR));
    }
    private void render() {
        rendering = true; colours(); root=column(); root.setPadding(dp(16),dp(state.page.equals("chat") ? 48 : 50),dp(16),dp(8));
        root.setBackgroundColor(state.page.equals("chat") ? surface : background); root.setFocusableInTouchMode(true);
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            final int topPadding = root.getPaddingTop();
            root.setOnApplyWindowInsetsListener((view, insets) -> {
                int keyboard = insets.getInsets(android.view.WindowInsets.Type.ime()).bottom;
                int navigation = insets.getInsets(android.view.WindowInsets.Type.navigationBars()).bottom;
                view.setPadding(dp(16), topPadding, dp(16), dp(8) + Math.max(0, keyboard - navigation));
                return insets;
            });
        }
        setContentView(root);
        root.requestApplyInsets();
        switch(state.page) {
            case "settings": settings(); break;
            case "theme": themes(); break;
            case "font": fonts(); break;
            case "documents": documents(); break;
            case "edit-document": documentEditor(); break;
            case "scenarios": scenarios(); break;
            default: state.page="chat"; chat();
        }
        rendering=false;
        persist();
        if (getIntent().getBooleanExtra("showDelete",false)) { getIntent().removeExtra("showDelete"); handler.post(this::deleteChat); }
    }
    private void header(String title, Runnable back, String trailing, Runnable action) {
        LinearLayout bar=row(); add(bar,icon("back","Back",back),44,44);
        TextView t=label(title,18,text); t.setTypeface(null,Typeface.BOLD); t.setGravity(Gravity.CENTER); weight(bar,t);
        if (trailing!=null) { TextView trailingButton=button(trailing,blue,action); trailingButton.setPadding(dp(8),0,dp(8),0); trailingButton.setSingleLine(true); add(bar,trailingButton,64,44); } else add(bar,new View(this),44,44);
        add(root,bar,-1,48); gap(root,15);
    }
    private void chat() {
        LinearLayout bar=row(); add(bar,icon("menu","Navigation button",this::navigation),48,48);
        String modelTitle=state.model.equals("Instant")?"Instant "+state.effort:"Kimi "+state.model;
        TextView model=label(modelTitle,16,text); SpannableString styledModel=new SpannableString(modelTitle);
        int secondaryStart=state.model.equals("Instant")?8:5;
        styledModel.setSpan(new ForegroundColorSpan(muted),secondaryStart,modelTitle.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(state.model.equals("Instant"))styledModel.setSpan(new StyleSpan(Typeface.BOLD),0,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        model.setText(styledModel); model.setPadding(dp(12),0,dp(12),0); model.setBackground(shape(surface,40)); model.setElevation(dp(3)); model.setContentDescription("Model selector"); model.setOnClickListener(v->models());
        LinearLayout.LayoutParams modelParams=new LinearLayout.LayoutParams(-2,dp(48)); modelParams.leftMargin=dp(10); bar.addView(model,modelParams);
        View space=new View(this); bar.addView(space,new LinearLayout.LayoutParams(0,1,1));
        add(bar,icon(state.capacityError?"new-chat":"plus","New chat",()->{ state.resetChat(); render(); }),48,48);
        if(!state.capacityError)add(bar,icon("more","More",this::chatMenu),48,48);
        LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,dp(52));bp.leftMargin=dp(-2);bp.rightMargin=dp(-2);root.addView(bar,bp);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); LinearLayout body=column(); body.setPadding(0,dp(state.capacityError?28:26),0,0); scroll.addView(body);
        if (state.status.equals("empty")) {
            body.setGravity(Gravity.CENTER); View avatar=new IconView("avatar"); add(body,avatar,72,72); gap(body,22);
            TextView welcome=label("How can I help you?",25,text); welcome.setGravity(Gravity.CENTER); welcome.setTypeface(null,Typeface.BOLD); add(body,welcome,-1,-2);
        } else if(state.capacityError && state.status.equals("error")) {
            capacityErrorBody(body);
        } else {
            TextView prompt=label(state.prompt,16,text); prompt.setPadding(dp(16),dp(10),dp(16),dp(10)); prompt.setBackground(shape(background,18));
            LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-2,-2); pp.gravity=Gravity.END; pp.bottomMargin=dp(20); body.addView(prompt,pp);
            LinearLayout thinking=row(); thinking.setPadding(dp(14),0,dp(14),0); thinking.setBackground(border(surface,10));
            TextView thought=label(state.status.equals("busy") ? "Thinking" : "Thinking complete",14,muted); weight(thinking,thought); add(thinking,label("›",28,muted),20,40); add(body,thinking,-1,40); gap(body,18);
            if (state.status.equals("busy")) add(body,label("Generating…",16,muted),-1,44);
            else {
                add(body,label(state.answer,16,state.status.equals("error")? red:text),-1,-2); gap(body,12);
                LinearLayout tools=row(); add(tools,button("Copy",muted,()->{ ((android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(android.content.ClipData.newPlainText("Reference answer",state.answer)); toast("Copied"); }),72,44);
                add(tools,button("Retry",state.status.equals("error")?red:muted,()->{ long epoch=state.retry(); render(); schedule(epoch,false); }),72,44); add(body,tools,-1,44);
            }
        }
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout input=row(); input.setPadding(dp(8),dp(8),dp(8),dp(8)); input.setBackground(shape(surface,36)); input.setElevation(dp(7));
        View addInput=icon("plus","More input options",()->message("Local reference", "This sample has no file upload service. Its request and error states use local fixtures."));addInput.setElevation(0);addInput.setBackground(shape(background,40));add(input,addInput,48,48);
        composer=new EditText(this); composer.setTextColor(text); composer.setHintTextColor(muted); composer.setTextSize(16*scale()); composer.setBackgroundColor(Color.TRANSPARENT); composer.setPadding(dp(12),0,dp(4),0); composer.setMinLines(1); composer.setMaxLines(4); composer.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE); composer.setHint(state.status.equals("busy")?"Send messages anytime…":state.model.equals("Instant")?"Ask anything. Images work too.":"Ask me. Task me."); composer.setContentDescription("Message draft"); composer.setText(state.draft);
        input.addView(composer,new LinearLayout.LayoutParams(0,-2,1));
        send=icon(state.status.equals("busy")?"stop":state.draft.trim().isEmpty()?"voice":"send",state.status.equals("busy")?"Stop output":state.draft.trim().isEmpty()?"Voice input":"Send message",()->{
            if(state.status.equals("busy")){ state.stop(); render(); }
            else if(state.draft.trim().isEmpty()) message("Voice input", "Use the original app to validate microphone permissions. This reference does not record audio.");
            else { long epoch=state.submit(); hideKeyboard(); render(); schedule(epoch,false); }
        });
        send.setElevation(0); send.setBackground(shape(state.status.equals("busy")||!state.draft.trim().isEmpty()?text:background,40)); add(input,send,48,48);
        composer.addTextChangedListener(new TextWatcher(){ public void beforeTextChanged(CharSequence s,int start,int count,int after){} public void onTextChanged(CharSequence s,int start,int before,int count){ if(rendering)return; state.draft=s.toString(); updateSend(); persist(); } public void afterTextChanged(Editable e){} });
        LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2);ip.leftMargin=dp(-4);ip.rightMargin=dp(-4);root.addView(input,ip); TextView footer=label("Local native reference",11,muted); footer.setGravity(Gravity.CENTER); add(root,footer,-1,18);
    }
    private void capacityErrorBody(LinearLayout body) {
        LinearLayout failed=row();failed.setGravity(Gravity.BOTTOM);
        View retry=icon("retry","Retry failed message",()->{long epoch=state.retry();render();schedule(epoch,false);});retry.setElevation(0);retry.setBackgroundColor(Color.TRANSPARENT);add(failed,retry,44,48);
        TextView prompt=label(state.prompt,16,text);prompt.setIncludeFontPadding(false);prompt.setLineSpacing(dp(26)-(prompt.getPaint().getFontMetrics().descent-prompt.getPaint().getFontMetrics().ascent),1);prompt.setPadding(dp(12),dp(8),dp(12),dp(8));prompt.setMinHeight(dp(69));prompt.setBackground(shape(background,18));
        failed.addView(prompt,new LinearLayout.LayoutParams(0,-2,1));add(body,failed,-1,-2);gap(body,20);
        TextView error=label(ReferenceState.CAPACITY_MESSAGE,16,muted);error.setIncludeFontPadding(false);error.setLineSpacing(dp(26)-(error.getPaint().getFontMetrics().descent-error.getPaint().getFontMetrics().ascent),1);error.setMinHeight(dp(53));SpannableString styled=new SpannableString(ReferenceState.CAPACITY_MESSAGE);styled.setSpan(new ForegroundColorSpan(blue),ReferenceState.CAPACITY_MESSAGE.indexOf("Upgrade"),styled.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);error.setText(styled);error.setContentDescription(ReferenceState.CAPACITY_MESSAGE);error.setOnClickListener(v->message("Local capacity fixture","This is a local comparison sample. It cannot open a subscription purchase. Retrying repeats the same local capacity error."));add(body,error,-1,-2);
    }
    private void updateSend() {
        IconView v=(IconView)send; v.kind=state.status.equals("busy")?"stop":state.draft.trim().isEmpty()?"voice":"send";
        send.setContentDescription(state.status.equals("busy")?"Stop output":state.draft.trim().isEmpty()?"Voice input":"Send message");
        send.setBackground(shape(state.status.equals("busy")||!state.draft.trim().isEmpty()?text:background,40)); send.invalidate();
    }
    private void schedule(long epoch, boolean document) {
        if(epoch<0)return;
        handler.postDelayed(()->{ boolean changed=document ? state.finishDocument(epoch) : state.complete(epoch); if(changed){ persist(); render(); } },6000);
    }
    private void navigation() {
        LinearLayout panel=panel("Library");
        add(panel,button("Chat reference",text,()->navigate("chat")),-1,52);
        add(panel,button("Fieldnotes · Documents",text,()->navigate("documents")),-1,52);
        add(panel,button("Appearance",text,()->{ settingsReturn=state.page; navigate("settings"); }),-1,52);
        add(panel,button("Research scenarios",muted,()->navigate("scenarios")),-1,52);
        show(panel,false);
    }
    private void navigate(String page) { if(overlay!=null)overlay.dismiss(); hideKeyboard(); state.page=page; render(); }
    private void models() {
        LinearLayout panel=panel("Choose a model");
        String[] names={"K3","K2.8","Instant"}; String[] details={"Chat & Agent, flagship all-rounder","Well-rounded and efficient for everyday tasks","Fast chat, quick replies"};
        for(int i=0;i<names.length;i++){ final String name=names[i]; option(panel,name,details[i],state.model.equals(name),()->{ state.model=name; state.capacityError=false; state.simulateFailure=false; navigate("chat"); }); }
        option(panel,"Thinking effort",state.effort,false,()->{ overlay.dismiss(); effort(); }); show(panel,true);
    }
    private void effort() {
        LinearLayout panel=panel("Thinking effort");
        for(String value:new String[]{"Low","High"}) option(panel,value,"Local reference option",state.effort.equals(value),()->{state.effort=value;navigate("chat");}); show(panel,true);
    }
    private void chatMenu() {
        LinearLayout panel=panel("Chat actions"); add(panel,button("Delete",red,this::deleteChat),-1,52); add(panel,button("Cancel",text,()->overlay.dismiss()),-1,52); show(panel,true);
    }
    private void deleteChat() { confirm("Delete the selected 1 chat", "Delete",()->{state.resetChat();render();}); }
    private void settings() {
        header("Appearance",()->{state.page=settingsReturn;render();},null,null);
        LinearLayout card=column(); card.setPadding(dp(16),0,dp(16),0); card.setBackground(shape(surface,28));
        setting(card,"Theme settings",state.theme,()->{state.page="theme";render();},true);
        setting(card,"Font size",state.font,()->{state.openFont();render();},true);
        setting(card,"Language","English",()->message("Language", "This reference implements English. Original localization assets remain in the research library."),false); add(root,card,-1,-2);
    }
    private void setting(LinearLayout card,String title,String value,Runnable action,boolean divider) {
        LinearLayout r=row(); TextView t=label(title,16,text); r.addView(t,new LinearLayout.LayoutParams(0,-1,1)); TextView v=label(value,16,muted); add(r,v,-2,-1); TextView arrow=label("›",28,muted); arrow.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL); add(r,arrow,22,-1);
        r.setContentDescription(title); r.setOnClickListener(w->action.run()); add(card,r,-1,56);
        if(divider){View l=new View(this);l.setBackgroundColor(line);add(card,l,-1,1);}
    }
    private void themes() {
        header("Theme settings",()->{state.page="settings";render();},null,null); LinearLayout card=column(); card.setBackground(shape(surface,28));
        for(String theme:new String[]{"Follow System","Light mode","Dark mode"}) option(card,theme,"",state.theme.equals(theme),()->{state.theme=theme;render();}); add(root,card,-1,-2);
    }
    private void fonts() {
        header("Font size",()->{state.abandonFont();render();},"Save",()->{state.saveFont();render();});
        TextView hint=label("Preview changes here. Tap Save to apply.",14,muted); add(root,hint,-1,48);
        LinearLayout sample=column(); sample.setPadding(dp(20),dp(20),dp(20),dp(20)); sample.setBackground(shape(surface,28));
        TextView preview=label("A little more room for your ideas.\n\nHow can I help you?",16,text); preview.setTextSize(state.pendingFont.equals("Extra Large")?22:state.pendingFont.equals("Large")?19:state.pendingFont.equals("Small")?14:16); add(sample,preview,-1,-2); add(root,sample,-1,-2); gap(root,20);
        LinearLayout options=column(); options.setBackground(shape(surface,24)); for(String size:fontSizes)option(options,size,"",state.pendingFont.equals(size),()->{state.pendingFont=size;render();}); add(root,options,-1,-2);
    }
    private void documents() {
        header("Fieldnotes",this::navigation,"New",()->{state.page="edit-document";render();});
        TextView sub=label("Your reference materials, in one place.",14,muted); add(root,sub,-1,40);
        if(state.document.isEmpty()){
            LinearLayout empty=column(); empty.setGravity(Gravity.CENTER); TextView title=label("Make room for your next idea",22,text); title.setGravity(Gravity.CENTER); title.setTypeface(null,Typeface.BOLD); add(empty,title,-1,-2); gap(empty,12);
            TextView desc=label("Add a note to keep useful material close.",14,muted);desc.setGravity(Gravity.CENTER);add(empty,desc,-1,-2);gap(empty,24);add(empty,button("Add a note",text,()->{state.page="edit-document";render();}),160,48);root.addView(empty,new LinearLayout.LayoutParams(-1,0,1));
        }else{
            LinearLayout card=column();card.setPadding(dp(20),dp(20),dp(20),dp(20));card.setBackground(shape(surface,28));
            TextView heading=label(state.document,20,text); heading.setTypeface(null,Typeface.BOLD); add(card,heading,-1,-2);gap(card,8);add(card,label("Note · Saved on this device",14,muted),-1,32);
            LinearLayout actions=row();add(actions,button("Edit",text,()->{state.documentDraft=state.document;state.page="edit-document";render();}),80,48);add(actions,button("Delete",red,()->confirm("Delete the selected 1 note","Delete",()->{state.document="";state.documentStatus="empty";render();})),100,48);add(card,actions,-1,48);add(root,card,-1,-2);
        }
        gap(root,16);add(root,button("Appearance",muted,()->{settingsReturn="documents";state.page="settings";render();}),-1,48);
    }
    private void documentEditor() {
        header("Edit note",()->{state.page="documents";render();},null,null);
        TextView help=label("Give your note a clear title. You can return later; your draft stays here.",14,muted); add(root,help,-1,-2);gap(root,16);
        EditText editor=new EditText(this);editor.setText(state.documentDraft);editor.setTextColor(text);editor.setHintTextColor(muted);editor.setHint("Note title");editor.setContentDescription("Note draft");editor.setTextSize(18*scale());editor.setPadding(dp(20),dp(16),dp(20),dp(16));editor.setBackground(shape(surface,24));editor.setMaxLines(5);editor.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){} public void onTextChanged(CharSequence s,int a,int b,int c){state.documentDraft=s.toString();persist();}public void afterTextChanged(Editable e){}});add(root,editor,-1,-2);gap(root,16);
        if(state.documentStatus.equals("error")){add(root,label("Couldn't save this note. Your draft is safe.",14,red),-1,48);}
        if(state.documentStatus.equals("busy")) {add(root,label("Saving…",16,muted),-1,48);add(root,button("Cancel saving",text,()->{state.cancelDocument();render();}),-1,48);}
        else add(root,button(state.documentStatus.equals("error")?"Retry saving":"Save note",text,()->{ if(state.documentDraft.trim().isEmpty()){editor.setError("Enter a note title");return;} hideKeyboard();long epoch=state.saveDocument();render();schedule(epoch,true); }),-1,48);
        gap(root,8);add(root,button("Return to notes",muted,()->{state.page="documents";render();}),-1,48);
    }
    private void scenarios() {
        header("Research scenarios",()->navigate("chat"),null,null); ScrollView sc=new ScrollView(this);LinearLayout list=column();sc.addView(list);add(root,sc,-1,-1);
        for(String scene:new String[]{"chat-empty","chat-complete","chat-busy","chat-error","source-capacity-error","chat-draft","settings","delete-dialog","documents-empty","documents-normal","documents-edit","documents-busy","documents-error"})add(list,button(scene,text,()->{scenario(scene);render();}),-1,48);
    }
    private void scenario(String scene) {
        handler.removeCallbacksAndMessages(null);
        state.requestEpoch++; state.documentEpoch++; state.simulateFailure=false; state.capacityError=false; state.page="chat";
        switch(scene){
            case "chat-empty": state.resetChat(); break;
            case "chat-complete": state.resetChat();state.prompt="Reply only OK";state.answer="OK";state.status="complete";break;
            case "chat-busy": state.resetChat();state.prompt="Reply only OK";state.status="busy";break;
            case "chat-error": state.resetChat();state.prompt="Reply only offline-test-OK";state.answer="Network is unavailable";state.status="error";break;
            case "source-capacity-error": state.resetChat();state.theme="Light mode";state.font="Follow System";state.model="K2.8";state.prompt="Run03 mdel K2.8 test. Reply only RUN03-K28-OK.";state.answer=ReferenceState.CAPACITY_MESSAGE;state.status="error";state.capacityError=true;state.simulateFailure=true;break;
            case "chat-draft": state.draft="Run03 native reference draft";break;
            case "settings": settingsReturn="chat";state.page="settings";break;
            case "delete-dialog": state.resetChat();state.prompt="Reply only OK";state.answer="OK";state.status="complete";getIntent().putExtra("showDelete",true);break;
            case "documents-empty": state.page="documents";state.document="";state.documentDraft="";state.documentStatus="empty";break;
            case "documents-normal": state.page="documents";state.document="Research reading list";state.documentDraft="";state.documentStatus="complete";break;
            case "documents-edit": state.page="edit-document";state.documentDraft="Run03 transfer draft";state.documentStatus="empty";break;
            case "documents-busy": state.page="edit-document";state.documentDraft="Run03 transfer draft";state.documentStatus="busy";break;
            case "documents-error": state.page="edit-document";state.documentDraft="Run03 transfer draft";state.documentStatus="error";break;
            case "enable-failure": state.simulateFailure=true;break;
            case "restore": state=new ReferenceState();break;
        }
        persist();
    }
    private LinearLayout panel(String title) { LinearLayout p=column();p.setPadding(dp(14),dp(16),dp(14),dp(14));p.setBackground(shape(surface,30)); TextView h=label(title,18,text);h.setTypeface(null,Typeface.BOLD);h.setPadding(dp(10),0,dp(10),dp(8));add(p,h,-1,-2);return p; }
    private void option(LinearLayout panel,String title,String subtitle,boolean selected,Runnable action) {
        LinearLayout r=row();r.setPadding(dp(18),dp(12),dp(18),dp(12)); LinearLayout c=column();TextView t=label(title,16,text);add(c,t,-1,-2);if(!subtitle.isEmpty())add(c,label(subtitle,12,muted),-1,-2);r.addView(c,new LinearLayout.LayoutParams(0,-2,1));if(selected)add(r,label("✓",18,blue),24,32);r.setContentDescription(title);r.setOnClickListener(v->action.run());add(panel,r,-1,-2);
    }
    private void show(LinearLayout panel,boolean bottom) {
        if(overlay!=null)overlay.dismiss();overlay=new Dialog(this);overlay.requestWindowFeature(Window.FEATURE_NO_TITLE);overlay.setContentView(panel);overlay.setCanceledOnTouchOutside(true);
        Window w=overlay.getWindow();w.setBackgroundDrawableResource(android.R.color.transparent);WindowManager.LayoutParams a=w.getAttributes();a.width=dp(bottom?370:340);a.height=-2;a.gravity=bottom?Gravity.BOTTOM:Gravity.CENTER;a.dimAmount=.6f;w.setAttributes(a);w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);overlay.show();w.setLayout(Math.min(dp(bottom?370:340),getResources().getDisplayMetrics().widthPixels-dp(32)),-2);
    }
    private void confirm(String title,String positive,Runnable action) {
        LinearLayout panel=panel(title);gap(panel,12);LinearLayout actions=row();TextView cancel=button("Cancel",text,()->overlay.dismiss());actions.addView(cancel,new LinearLayout.LayoutParams(0,dp(48),1));View space=new View(this);add(actions,space,8,1);TextView ok=button(positive,red,()->{overlay.dismiss();action.run();});actions.addView(ok,new LinearLayout.LayoutParams(0,dp(48),1));add(panel,actions,-1,48);show(panel,false);
    }
    private void message(String title,String content){LinearLayout panel=panel(title);TextView body=label(content,14,muted);body.setPadding(dp(10),dp(8),dp(10),dp(16));add(panel,body,-1,-2);add(panel,button("Got it",blue,()->overlay.dismiss()),-1,48);show(panel,false);}
    private void toast(String message){android.widget.Toast.makeText(this,message,android.widget.Toast.LENGTH_SHORT).show();}
    private void hideKeyboard(){((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(root.getWindowToken(),0);}
    private void persist(){SharedPreferences.Editor e=getSharedPreferences("reference",MODE_PRIVATE).edit();e.putString("draft",state.draft).putString("prompt",state.prompt).putString("answer",state.answer).putString("status",state.status).putString("model",state.model).putString("effort",state.effort).putString("theme",state.theme).putString("font",state.font).putString("document",state.document).putString("documentDraft",state.documentDraft).putString("documentStatus",state.documentStatus).putBoolean("capacityError",state.capacityError).apply();}
    private void load(){SharedPreferences p=getSharedPreferences("reference",MODE_PRIVATE);state.draft=p.getString("draft","");state.prompt=p.getString("prompt","");state.answer=p.getString("answer","");state.status=p.getString("status","empty");if(state.status.equals("busy")){state.status="stopped";state.answer="Output stopped";}state.model=p.getString("model","Instant");state.effort=p.getString("effort","High");state.theme=p.getString("theme","Follow System");state.font=p.getString("font","Follow System");state.document=p.getString("document","");state.documentDraft=p.getString("documentDraft","");state.documentStatus=p.getString("documentStatus","empty");if(state.documentStatus.equals("busy"))state.documentStatus="error";state.capacityError=p.getBoolean("capacityError",false);state.simulateFailure=state.capacityError;}
    private final class IconView extends View {
        String kind; Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        IconView(String kind){super(MainActivity.this);this.kind=kind;setMinimumHeight(dp(44));setMinimumWidth(dp(44));}
        @Override protected void onDraw(Canvas original){super.onDraw(original);Canvas c=original;c.save();c.scale(getWidth()/44f,getHeight()/44f);p.setColor((kind.equals("send")||kind.equals("stop"))?surface:text);p.setStrokeWidth(1.8f);p.setStrokeCap(Paint.Cap.ROUND);p.setStyle(Paint.Style.STROKE);
            switch(kind){
                case "menu":for(int y:new int[]{16,22,28})c.drawLine(15,y,29,y,p);break;
                case "back":c.drawLine(25,14,17,22,p);c.drawLine(17,22,25,30,p);break;
                case "plus":c.drawLine(15,22,29,22,p);c.drawLine(22,15,22,29,p);break;
                case "new-chat":c.drawRoundRect(13,12,31,29,8,8,p);c.drawLine(16,28,13,32,p);c.drawLine(13,32,21,30,p);c.drawLine(18,21,26,21,p);c.drawLine(22,17,22,25,p);break;
                case "retry":p.setColor(red);p.setStyle(Paint.Style.FILL);c.drawCircle(22,27,10,p);p.setColor(Color.WHITE);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1.6f);c.drawArc(17,22,27,32,-20,290,false,p);c.drawLine(27,21,27,25,p);c.drawLine(23,24,27,25,p);break;
                case "more":p.setStyle(Paint.Style.FILL);for(int y:new int[]{14,22,30})c.drawCircle(22,y,1.8f,p);break;
                case "send":c.drawLine(22,30,22,14,p);c.drawLine(16,20,22,14,p);c.drawLine(22,14,28,20,p);break;
                case "stop":p.setStyle(Paint.Style.FILL);c.drawRoundRect(16,16,28,28,1,1,p);break;
                case "voice":c.drawArc(11,10,28,34,-60,120,false,p);c.drawArc(14,15,23,29,-60,120,false,p);p.setStyle(Paint.Style.FILL);c.drawCircle(17,22,1.2f,p);break;
                case "avatar":p.setColor(blue);p.setStrokeWidth(5);c.drawCircle(22,22,16,p);p.setColor(text);p.setStrokeWidth(2.5f);c.drawLine(17,20,17,24,p);c.drawLine(26,20,26,24,p);break;
            }c.restore();
        }
    }
}

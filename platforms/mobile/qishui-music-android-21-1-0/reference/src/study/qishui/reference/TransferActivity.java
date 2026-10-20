package study.qishui.reference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Locale;

/** D cross-business example. Local study plans and timer; no network or source brand assets. */
public class TransferActivity extends Activity {
    private static final String[] GOALS={"阅读 20 页","复习 10 道题","整理一页笔记"};
    private ConsentFlow flow;private CopyRepository copy;private SharedPreferences preferences;
    private FrameLayout root;private TextView timerText,statusText;private ProgressBar progress;
    private CountDownTimer timer;private long durationMs=25*60*1000L,remainingMs;private boolean running,sessionScreen,help;
    private int goalIndex,completed;private String status="";
    @Override public void onCreate(Bundle state){super.onCreate(state);copy=new CopyRepository(this,"transfer-copy.json");preferences=getSharedPreferences("reference_transfer",MODE_PRIVATE);applyIntent(getIntent());}
    @Override protected void onNewIntent(Intent intent){super.onNewIntent(intent);setIntent(intent);pauseTimer();applyIntent(intent);}
    private void applyIntent(Intent intent){
        if(intent.getBooleanExtra("reset_demo",false))preferences.edit().clear().apply();
        durationMs=Math.max(5,Math.min(3600,intent.getIntExtra("duration_seconds",25*60)))*1000L;
        remainingMs=preferences.getLong("remaining",durationMs);goalIndex=preferences.getInt("goal",0)%GOALS.length;completed=preferences.getInt("completed",0);
        flow=new ConsentFlow(preferences.getBoolean("intro_seen",false));String scene=intent.getStringExtra("scene");
        if("intro".equals(scene))flow.showGuide();else if("reminder".equals(scene))flow.showReminder();else if("workspace".equals(scene)||"session".equals(scene)||"complete".equals(scene))flow.accept();
        sessionScreen="session".equals(scene);if("complete".equals(scene)){remainingMs=0;status="本次专注已完成。";}
        if(sessionScreen){remainingMs=durationMs;startTimer();}render();
    }
    private void render(){
        root=new FrameLayout(this);root.setBackgroundColor(Color.rgb(12,12,12));setContentView(root);
        if(flow.modal()){showIntro();return;}if(help){showHelp();return;}
        LinearLayout page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setPadding(dp(24),dp(28),dp(24),dp(20));root.addView(page,new FrameLayout.LayoutParams(-1,-1));
        TextView title=label(sessionScreen?"现在，专注一件事":"专注学习",26,Color.WHITE,true);title.setId(R.id.study_title);page.addView(title);
        page.addView(label(sessionScreen?GOALS[goalIndex]:"在自己的节奏里，完成今天的计划。",16,0xFFADB8B0,false));
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setGravity(Gravity.CENTER);card.setPadding(dp(18),dp(28),dp(18),dp(28));card.setBackground(ConsentPanel.round(0xFF1B2420,24,card));
        LinearLayout.LayoutParams cardParams=new LinearLayout.LayoutParams(-1,0,1);cardParams.topMargin=dp(28);cardParams.bottomMargin=dp(22);page.addView(card,cardParams);
        card.addView(label(sessionScreen?(running?"正在专注":"已暂停"):GOALS[goalIndex],20,0xFFEAF3ED,true));
        timerText=label(format(remainingMs),64,Color.WHITE,true);timerText.setId(R.id.study_timer);timerText.setGravity(Gravity.CENTER);card.addView(timerText,new LinearLayout.LayoutParams(-1,-2));
        card.addView(label("剩余时间",14,0xFFA8B7AD,false));
        progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);progress.setMax(1000);progress.setProgressTintList(ColorStateList.valueOf(ConsentPanel.GREEN));progress.setProgressBackgroundTintList(ColorStateList.valueOf(0xFF39433D));LinearLayout.LayoutParams progressParams=new LinearLayout.LayoutParams(-1,dp(4));progressParams.topMargin=dp(24);card.addView(progress,progressParams);updateTimer();
        statusText=label(status.isEmpty()?(sessionScreen?"暂停会保留本次进度。":"已完成 "+completed+" 次专注"):status,14,0xFFAFC0B4,false);statusText.setId(R.id.study_status);statusText.setGravity(Gravity.CENTER);statusText.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);page.addView(statusText);
        String primary=sessionScreen?(running?"暂停":"继续专注"):(remainingMs>0&&remainingMs<durationMs?"继续上次专注":"开始专注");
        page.addView(action(primary,true,R.id.primary_action,()->{if(sessionScreen){if(running){pauseTimer();status="已暂停，进度已保留。";}else{status="";startTimer();}}else{sessionScreen=true;status="";if(remainingMs<=0)remainingMs=durationMs;startTimer();}render();}));
        page.addView(action(sessionScreen?"返回计划":"如何开始",false,R.id.secondary_action,()->{if(sessionScreen){pauseTimer();sessionScreen=false;status="进度已保留，可以稍后继续。";}else help=true;persist();render();}));
        if(sessionScreen){page.addView(action("结束本次",false,View.NO_ID,()->{pauseTimer();remainingMs=0;sessionScreen=false;status="本次专注已结束。";persist();render();}));}
        else{page.addView(action("切换学习目标",false,View.NO_ID,()->{goalIndex=(goalIndex+1)%GOALS.length;status="目标已更新。";persist();render();}));}
    }
    private void showIntro(){
        ConsentPanel panel=new ConsentPanel(this,new ConsentPanel.Actions(){public void accept(){flow.accept();preferences.edit().putBoolean("intro_seen",true).apply();render();}public void decline(){if(flow.decline()==ConsentFlow.State.EXIT){preferences.edit().putBoolean("intro_seen",false).apply();persist();finishAndRemoveTask();}else render();}});
        panel.bind(copy.section(flow.state()==ConsentFlow.State.REMINDER?"REMINDER":"GUIDE"));center(panel);
    }
    private void showHelp(){
        ConsentPanel panel=new ConsentPanel(this,new ConsentPanel.Actions(){public void accept(){help=false;sessionScreen=true;status="";if(remainingMs<=0)remainingMs=durationMs;startTimer();render();}public void decline(){help=false;render();}});panel.bind(copy.section("HELP"));center(panel);
    }
    private void center(ConsentPanel panel){root.addView(panel,new FrameLayout.LayoutParams(dp(312),dp(494),Gravity.CENTER));root.post(()->{int[] position=new int[2];root.getLocationOnScreen(position);root.setPadding(0,0,0,position[1]);});}
    private void startTimer(){
        if(timer!=null)timer.cancel();if(remainingMs<=0)remainingMs=durationMs;running=true;
        timer=new CountDownTimer(remainingMs,250){public void onTick(long remaining){remainingMs=remaining;updateTimer();}public void onFinish(){remainingMs=0;running=false;completed++;sessionScreen=false;status="本次专注已完成。";persist();render();}}.start();persist();
    }
    private void pauseTimer(){if(timer!=null){timer.cancel();timer=null;}running=false;persist();}
    private void updateTimer(){if(timerText!=null)timerText.setText(format(remainingMs));if(progress!=null)progress.setProgress((int)Math.max(0,Math.min(1000,1000*(durationMs-remainingMs)/durationMs)));}
    private String format(long milliseconds){long seconds=(Math.max(0,milliseconds)+999)/1000;return String.format(Locale.ROOT,"%02d:%02d",seconds/60,seconds%60);}
    private void persist(){if(preferences!=null)preferences.edit().putLong("remaining",remainingMs).putInt("goal",goalIndex).putInt("completed",completed).apply();}
    private TextView label(String text,float size,int color,boolean bold){TextView v=new TextView(this);v.setText(text);v.setTextColor(color);v.setTextSize(size);v.setTypeface(Typeface.DEFAULT,bold?Typeface.BOLD:Typeface.NORMAL);v.setPadding(0,dp(6),0,dp(6));return v;}
    private View action(String text,boolean primary,int id,Runnable callback){TextView v=label(text,16,primary?ConsentPanel.INK:0xFFBBC8BF,true);if(id!=View.NO_ID)v.setId(id);v.setGravity(Gravity.CENTER);v.setIncludeFontPadding(false);if(primary)v.setBackground(ConsentPanel.round(ConsentPanel.GREEN,12,v));v.setOnClickListener(view->callback.run());LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(48));p.topMargin=dp(primary?12:4);v.setLayoutParams(p);return v;}
    private int dp(float n){return Math.round(n*getResources().getDisplayMetrics().density);}
    @Override protected void onStop(){super.onStop();if(running){pauseTimer();status="已暂停，进度已保留。";}}
    @Override protected void onResume(){super.onResume();if(root!=null&&!flow.modal())render();}
    @Override protected void onDestroy(){if(timer!=null)timer.cancel();persist();super.onDestroy();}
    @Override public void onBackPressed(){if(flow.modal()){flow.back();return;}if(help){help=false;render();return;}if(sessionScreen){pauseTimer();sessionScreen=false;status="进度已保留，可以稍后继续。";render();return;}super.onBackPressed();}
}

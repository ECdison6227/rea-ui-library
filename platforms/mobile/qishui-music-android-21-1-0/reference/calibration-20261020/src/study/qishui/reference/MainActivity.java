package study.qishui.reference;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Source-content comparison entry. This is an independent local reference app. */
public class MainActivity extends Activity {
    private ConsentFlow flow;private CopyRepository copy;private SharedPreferences preferences;private FrameLayout root;private ConsentPanel panel;
    @Override public void onCreate(Bundle saved) {super.onCreate(saved);copy=new CopyRepository(this,"source-copy.json");preferences=getSharedPreferences("reference_source",MODE_PRIVATE);applyIntent(getIntent());}
    @Override protected void onNewIntent(Intent intent) {super.onNewIntent(intent);setIntent(intent);applyIntent(intent);}
    private void applyIntent(Intent intent) {
        if(intent.getBooleanExtra("reset_consent",false))preferences.edit().clear().apply();
        flow=new ConsentFlow(preferences.getBoolean("accepted",false));String scene=intent.getStringExtra("scene");
        if("privacy".equals(scene))flow.showGuide();else if("reminder".equals(scene))flow.showReminder();else if("catalog".equals(scene))flow.accept();
        render();
    }
    private void render() {
        root=new FrameLayout(this);root.setBackgroundColor(Color.rgb(12,12,12));setContentView(root);
        if(!flow.modal()){catalog();return;}
        TextView note=label("个人信息弹层 · D 本地参考",13,0xFF5A5A5A,false);FrameLayout.LayoutParams noteParams=new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM);noteParams.bottomMargin=dp(48);note.setGravity(Gravity.CENTER);root.addView(note,noteParams);
        panel=new ConsentPanel(this,new ConsentPanel.Actions(){public void accept(){flow.accept();preferences.edit().putBoolean("accepted",true).apply();render();}public void decline(){if(flow.decline()==ConsentFlow.State.EXIT){preferences.edit().putBoolean("accepted",false).apply();finishAndRemoveTask();}else bind();}});
        FrameLayout.LayoutParams p=new FrameLayout.LayoutParams(dp(312),dp(494),Gravity.CENTER);root.addView(panel,p);bind();
        if(getIntent().getBooleanExtra("capture_alignment",true))root.post(()->{int[] position=new int[2];root.getLocationOnScreen(position);root.setPadding(0,0,0,position[1]);});
    }
    private void bind(){panel.bind(copy.section(flow.state()==ConsentFlow.State.REMINDER?"REMINDER":"GUIDE"));}
    private void catalog(){
        LinearLayout layout=new LinearLayout(this);layout.setOrientation(LinearLayout.VERTICAL);layout.setPadding(dp(24),dp(52),dp(24),dp(24));root.addView(layout,new FrameLayout.LayoutParams(-1,-1));
        layout.addView(label("原生组件参考",28,Color.WHITE,true));layout.addView(label("本地同意已保存。此页是 D 演示入口，没有网络、账号或原音乐业务。",16,0xFFAEB8B2,false));
        layout.addView(action("重新展示首次引导",()->{flow.showGuide();render();}));layout.addView(action("展示不同意后的提醒",()->{flow.showReminder();render();}));
        layout.addView(action("打开“专注学习”迁移样本",()->startActivity(new Intent(this,TransferActivity.class))));
        layout.addView(action("清除本地演示状态",()->{preferences.edit().clear().apply();flow=new ConsentFlow(false);render();}));
    }
    private TextView label(String text,float size,int color,boolean bold){TextView v=new TextView(this);v.setText(text);v.setTextSize(size);v.setTextColor(color);v.setTypeface(Typeface.DEFAULT,bold?Typeface.BOLD:Typeface.NORMAL);v.setPadding(0,dp(8),0,dp(8));return v;}
    private View action(String text,Runnable action){TextView v=label(text,16,ConsentPanel.INK,true);v.setGravity(Gravity.CENTER);v.setBackground(ConsentPanel.round(ConsentPanel.GREEN,12,v));v.setOnClickListener(view->action.run());LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(48));p.topMargin=dp(20);v.setLayoutParams(p);return v;}
    private int dp(float n){return Math.round(n*getResources().getDisplayMetrics().density);}
    @Override public void onBackPressed(){if(flow.modal())flow.back();else super.onBackPressed();}
}

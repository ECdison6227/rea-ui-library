package study.style.reference;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.util.*;

/** Independent native reconstruction. All business services are local D fixtures. */
public class MainActivity extends Activity {
    StyleKit ui; Tokens tokens; float width; FrameLayout host;
    final Map<String,View> pages=new HashMap<>();
    final Deque<String> stack=new ArrayDeque<>();
    String scene="health"; int tab=0; boolean transfer=false; boolean notifications=true;
    final ArrayList<String> documents=new ArrayList<>();
    boolean loading=false,failNext=false; int generation=0; boolean alive=true;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        String theme=getIntent().getStringExtra("theme");
        boolean night="night".equals(theme)||(!"day".equals(theme)&&(getResources().getConfiguration().uiMode&Configuration.UI_MODE_NIGHT_MASK)==Configuration.UI_MODE_NIGHT_YES);
        tokens=new Tokens(night);ui=new StyleKit(this,tokens);width=getResources().getDisplayMetrics().widthPixels/ui.density;
        getWindow().setStatusBarColor(tokens.page);getWindow().setNavigationBarColor(tokens.navigation);
        if(Build.VERSION.SDK_INT>=29){getWindow().setNavigationBarContrastEnforced(false);getWindow().setStatusBarContrastEnforced(false);}
        getWindow().getDecorView().setSystemUiVisibility(night?0:View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        host=ui.frame();setContentView(host);String requested=getIntent().getStringExtra("scene");
        transfer="transfer".equals(requested);scene=requested==null?"health":requested;tab=scene.equals("mine")?3:0;
        if(state!=null) { scene=state.getString("scene",scene);transfer=state.getBoolean("transfer",transfer); }
        show(scene);event("launch",scene);
    }
    @Override protected void onSaveInstanceState(Bundle state){super.onSaveInstanceState(state);state.putString("scene",scene);state.putBoolean("transfer",transfer);}
    @Override protected void onDestroy(){alive=false;generation++;if(ui!=null)ui.dispose();super.onDestroy();}
    void event(String action,String value){Log.i("STYLE_REFERENCE", "{\"action\":\""+action+"\",\"value\":\""+value.replace("\"","'")+"\",\"scene\":\""+scene+"\"}");}
    void show(String next) {
        scene=next;View page=pages.get(next);
        if(page==null){
            switch(next){case "mine":page=mine();break;case "settings":page=settings(false);break;case "transfer":page=transferPage();break;case "preferences":page=settings(true);break;case "components":page=components();break;default:page=health();}
            pages.put(next,page);
        }
        host.removeAllViews();host.addView(page,new FrameLayout.LayoutParams(-1,-1));
        getWindow().setStatusBarColor(next.equals("settings")||next.equals("preferences")||next.equals("health")?(tokens.night?Color.BLACK:Color.WHITE):tokens.page);
        event("show",next);
    }
    void push(String next){stack.push(scene);show(next);}
    @Override public void onBackPressed(){if(!stack.isEmpty()){show(stack.pop());event("back","restore");}else{event("back","root-exit");super.onBackPressed();}}
    FrameLayout base(boolean white){FrameLayout p=ui.frame();p.setBackgroundColor(white?(tokens.night?Color.BLACK:Color.WHITE):tokens.page);return p;}
    // A observed original client begins at physical y96; AOSP decor here begins at y136.
    // D explicit inset adapter. At other devices use actual window insets and revalidate.
    float insetCorrection(){return 14.54545f;}
    void header(FrameLayout root,View view,float h){ui.at(root,view,0,-insetCorrection(),width,h);}
    FrameLayout body(FrameLayout root,float header,boolean bottomNav){
        FrameLayout b=ui.frame();FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(-1,-1);lp.topMargin=ui.dp(header-insetCorrection());lp.bottomMargin=bottomNav?ui.dp(66.67f):0;root.addView(b,lp);return b;
    }
    void scroll(FrameLayout body,View content,float height) {ScrollView s=new ScrollView(this);s.setFillViewport(false);s.setVerticalScrollBarEnabled(false);s.setClipToPadding(false);s.setOverScrollMode(View.OVER_SCROLL_NEVER);s.addView(content,new ScrollView.LayoutParams(-1,ui.dp(height)));body.addView(s,new FrameLayout.LayoutParams(-1,-1));}
    void nav(FrameLayout root,int active,String[] labels,boolean adapt){
        FrameLayout n=ui.bottomNav(width,active,labels,i->{
            if(i==active){event("tab-reselect","keep-scroll");return;}
            if(adapt){if(i==3)push("preferences");else if(i==0)show("transfer");else new AlertDialog.Builder(this).setTitle(labels[i]).setMessage("这里还没有内容").setPositiveButton("知道了",null).show();return;}
            if(i==0||i==3){tab=i;show(i==0?"health":"mine");}else new AlertDialog.Builder(this).setTitle(labels[i]).setMessage("此页面未纳入三页参考范围").setPositiveButton("返回",null).show();
        });
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(-1,ui.dp(66.67f),Gravity.BOTTOM);root.addView(n,lp);
    }
    void requireAccount(String action){event("request-auth",action);if(action.equals("profile")||action.equals("login")){authBoundary(action);return;}ui.confirmSheet("此功能需要登录小米账号后使用，是否现在登录？","登录",()->{event("auth-confirm",action);new Handler(Looper.getMainLooper()).postDelayed(()->{if(alive)authBoundary(action);},1000);},()->event("auth-cancel",action));}
    void authBoundary(String action){ui.externalModal(true);AlertDialog d=new AlertDialog.Builder(this).setTitle("登录后继续").setMessage("已触发账号适配器。认证 SDK 不在三页参考范围内。").setNegativeButton("取消",(x,w)->event("auth-cancel",action)).create();d.setOnDismissListener(x->ui.externalModal(false));d.show();}
    void showAddMenu(){
        View anchor=host.findViewWithTag("add-action");if(anchor==null)return;event("open-menu","add");
        FrameLayout menu=ui.surface(12);PopupWindow popup=new PopupWindow(menu,ui.dp(145),ui.dp(108),true);popup.setBackgroundDrawable(ui.round(tokens.surface,12));popup.setElevation(ui.dp(10));popup.setOutsideTouchable(true);
        for(int i=0;i<2;i++){final String action=i==0?"添加设备":"扫一扫";TextView row=ui.text(action,14,400,tokens.text);ui.at(menu,row,16,i*54,113,54);ui.action(row,action,()->{popup.dismiss();event("choose-add-action",action);if(action.equals("添加设备")){event("request-auth","add-device");authBoundary("add-device");}else openDetailBoundary(action);});}
        popup.setOnDismissListener(()->event("close-menu","add"));popup.showAsDropDown(anchor,-ui.dp(11.27f),ui.dp(11.27f),Gravity.RIGHT);
    }
    void openDetailBoundary(String title){event("open-detail",title);new AlertDialog.Builder(this).setTitle(title).setMessage("已触发详情事件。详情内容不在本参考的三页范围内。").setPositiveButton("返回",(d,w)->event("detail-return",title)).show();}
    FrameLayout health(){
        FrameLayout root=base(false);root.setBackgroundColor(tokens.night?Color.BLACK:Color.WHITE);header(root,ui.topBar("健康",width,false,()->{},()->showAddMenu()),87);
        FrameLayout content=ui.frame();
        ImageView headerBg=ui.image("health_header_background_"+tokens.mode()+".webp");headerBg.setScaleType(ImageView.ScaleType.FIT_XY);ui.at(content,headerBg,0,0,width,269);
        ProgressHero hero=new ProgressHero(this,tokens);ui.action(hero,"活力指标",()->openDetailBoundary("活力指标"));ui.at(content,hero,0,0,width,168);
        ui.at(content,ui.image("health_hero_reflection.png"),(width-210)/2,100,210,80);
        FrameLayout biscuit=ui.frame();ImageView shareBg=ui.image("health_bg_share_"+tokens.mode()+".png");shareBg.setScaleType(ImageView.ScaleType.FIT_XY);ui.at(biscuit,shareBg,0,0,82,88);
        ui.at(biscuit,ui.image("health_food_biscuit.webp"),13,8,56,46);TextView count=ui.latin("x0",14,"misanslatin_semibold.otf",tokens.text);count.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);ui.at(biscuit,count,20,54,42,34);
        ui.action(biscuit,"约 0 片饼干",()->openDetailBoundary("活力指标分享"));ui.at(content,biscuit,width-82,80,82,88);
        float inner=width-24;
        FrameLayout summary=ui.surface(15);String[] names={"卡路里","步数","中高强度"};String[] targets={"/400千卡","/6000步","/30分钟"};String[] icons={"calorie","step","vitality"};
        for(int i=0;i<3;i++){
            final String label=names[i];float x=14+i*((inner-32)/3+8);
            String icon=i==0?"health_summary_calorie.png":i==1?"health_summary_step.webp":"health_summary_intensity_"+tokens.mode()+".webp";
            FrameLayout item=ui.frame();ui.at(item,ui.image(icon),2,7,14,14);
            ui.at(item,ui.text(names[i],14,400,tokens.text),18,0,(inner-18)/3-24,30.18f);
            ui.at(item,new StyleKit.Glyph(this,"chevron",0xffe0e0e0),(inner-18)/3-24,8,14,14);
            ui.at(item,ui.latin("0",24,"misanslatin_semibold.otf",tokens.text),0,32,70,32);
            ui.at(item,ui.text(targets[i],12,700,tokens.secondary),0,67,100,23);
            ui.action(item,label,()->openDetailBoundary(label));ui.at(summary,item,x,16,(inner-18)/3,100);
        }
        View divider=new View(this);divider.setBackgroundColor(tokens.divider);ui.at(summary,divider,14,123,inner-28,0.36f);
        FrameLayout standing=ui.frame();ui.at(standing,ui.image("health_summary_standing.webp"),18,22,14,20);
        ui.at(standing,ui.text("活动次数 0 次",14,400,tokens.text),38.18f,22.8f,inner-80,20.36f);
        ui.at(standing,new StyleKit.Glyph(this,"chevron",0xffe0e0e0),inner-37,24,18,18);ui.action(standing,"活动次数",()->openDetailBoundary("活动次数"));ui.at(summary,standing,0,123,inner,67);
        ui.at(content,summary,12,168,inner,190);
        FrameLayout login=ui.surface(15);ui.at(login,ui.image("health_guest_avatar.png"),20,20,30,30);
        ui.at(login,ui.text("点击登录",17,700,tokens.text),61,18,inner-110,23.64f);
        ui.at(login,ui.text("体验更丰富的设备管理和运动健康服务",12,400,tokens.text),61,44,inner-85,17.82f);
        ui.at(login,new StyleKit.Glyph(this,"chevron",0xffe0e0e0),inner-42,32,20,20);ui.action(login,"点击登录",()->requireAccount("login"));ui.at(content,login,12,372,inner,85);
        // A phone baseline has asymmetric native cell padding; do not impose this on RN/transfer grids.
        float cardWidth=inner/2;
        ui.at(content,ui.metricCard(cardWidth,"睡眠","记录您的睡眠数据","health_sleep_"+tokens.mode()+".webp",false,()->openDetailBoundary("睡眠")),12,467,cardWidth,245);
        ui.at(content,ui.metricCard(cardWidth-10,"心率","记录您的心率数据","health_heart_rate_"+tokens.mode()+".webp",true,()->openDetailBoundary("心率")),12+cardWidth+10,467,cardWidth-10,245);
        content.setBackgroundColor(tokens.page);
        scroll(body(root,87,true),content,740);nav(root,0,new String[]{"健康","运动","设备","我的"},false);return root;
    }
    FrameLayout mine(){
        FrameLayout root=base(false),header=ui.topBar(" ",width,false,()->{},()->showAddMenu());
        ui.at(header,ui.image("header_message_"+tokens.mode()+".webp"),width-104,23,36,36);
        ui.action(header.getChildAt(header.getChildCount()-1),"消息通知",()->requireAccount("messages"));
        header(root,header,62.55f);
        FrameLayout content=ui.frame();float inner=width-24;
        ui.at(content,ui.profile(inner,"未登录","无小米账号",()->requireAccount("profile")),12,10,inner,60);
        String[] labels={"我的活动","我的课程","我的亲友","我的奖品","我的订单"},images=new String[5];String[] names={"activity","course","friend","prize","order"};
        for(int i=0;i<5;i++)images[i]="mine_"+names[i]+"_"+tokens.rnMode()+".webp";
        ui.at(content,ui.shortcutGrid(inner,labels,images,i->requireAccount(names[i])),12,87,inner,70);
        FrameLayout vip=ui.surface();vip.setBackground(ui.round(tokens.night?0xff302a22:0xfffff8ee,18));
        FrameLayout banner=ui.frame();GradientDrawable bg=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xffffebc5,0xffffd39d});bg.setCornerRadii(new float[]{ui.dp(18),ui.dp(18),ui.dp(18),ui.dp(18),0,0,0,0});banner.setBackground(bg);ui.at(vip,banner,0,0,inner,58);
        ui.at(banner,ui.image("mine_VIP_"+tokens.rnMode()+".webp"),14,21,40,16);
        ui.at(banner,ui.text("小米运动健康会员",17,400,0xff915118),58,14,inner-145,30);
        TextView open=ui.text("立即开通",14,400,0xffffddb0);open.setGravity(Gravity.CENTER);open.setBackground(ui.round(0xff282724,24));ui.at(banner,open,inner-94,11,80,38);
        ui.at(vip,ui.text("【新增】公立医生在线问诊  ›",12,400,0xff9a571d),14,70,inner/2,24);
        ui.at(vip,ui.text("全量表盘，免费下载  ›",12,400,0xff9a571d),inner/2+8,70,inner/2-16,24);
        ui.at(vip,ui.text("专业健康管家 24 小时在线",12,400,0xffbc946d),14,96,inner/2+14,22);
        ui.at(vip,ui.text("总有一款让您满心欢喜",12,400,0xffbc946d),inner/2+8,96,inner/2-16,22);
        ui.action(vip,"小米运动健康会员",()->requireAccount("vip"));ui.at(content,vip,12,164.5f,inner,128);
        float half=(inner-10)/2;
        FrameLayout habit=promo(half,"小习惯","加入打卡",null);ui.at(content,habit,12,304.5f,half,172);
        ui.at(content,promo(half,"运动健康周报","10.05 ~ 10.11","mine_report.webp"),12+half+10,304.5f,half,81);
        ui.at(content,promo(half,"活力竞赛","和好友来场 PK","mine_trophy.webp"),12+half+10,395.5f,half,81);
        String[] menu={"任务中心","健康问诊","我的路线库","App 设置","三方数据管理","设备授权管理","系统权限","帮助与反馈","设备共享","App 版本","关于"};
        String[] icon={"task_center","health_visit_new","mine_track","app_setting","third-party-data-management","ic_device_authorization","system_permission","help-and-feedback","device_sharing","app_version","about"};
        float y=488.5f;
        for(int i=0;i<menu.length;i++){
            final String name=menu[i];float h=i==0?68:60;
            FrameLayout s=ui.surface();FrameLayout row=ui.chevronRow(inner-28,name,null,i==9?"V3.57.0":null,"mine_"+icon[i]+".webp",h,700,()->{if(name.equals("App 设置"))push("settings");else if(name.equals("App 版本")){event("request-upgrade","version");openDetailBoundary("检查更新");}else if(name.equals("系统权限")||name.equals("关于"))openDetailBoundary(name);else requireAccount(name);});
            ui.at(s,row,14,0,inner-28,h);ui.at(content,s,12,y,inner,h);y+=h+(i<3?12:0);
        }
        scroll(body(root,62.55f,true),content,y+12);nav(root,3,new String[]{"健康","运动","设备","我的"},false);return root;
    }
    FrameLayout promo(float w,String title,String sub,String image){FrameLayout p=ui.surface();ui.at(p,ui.text(title,17,700,tokens.text),14,12,w-30,35);ui.at(p,ui.text(sub,14,400,tokens.secondary),14,47,w-30,28);if(image!=null)ui.at(p,ui.image(image),w-40,26,28,28);ui.action(p,title,()->requireAccount(title));return p;}
    FrameLayout settings(boolean adapt){
        FrameLayout root=base(true);header(root,ui.topBar(adapt?"偏好设置":"设置",width,true,()->onBackPressed(),()->{}),131.27f);
        float w=width-52.36f;FrameLayout rows=ui.frame();
        ui.at(rows,ui.chevronRow(w,adapt?"工作区":"地区",null,adapt?"个人空间":"中国大陆",null,53.45f,700,()->{if(adapt)selectWorkspace();else requireAccount("region");}),0,13.1f,w,53.45f);
        View d=new View(this);d.setBackgroundColor(tokens.divider);ui.at(rows,d,0,86.55f,w,.36f);
        ui.at(rows,ui.chevronRow(w,adapt?"资料同步":"运动健康云服务",null,null,null,53.45f,700,()->{if(adapt)refreshDocuments();else requireAccount("cloud");}),0,106.9f,w,53.45f);
        ui.at(rows,ui.chevronRow(w,adapt?"共享访问":"运动健康设备双接授权",adapt?"仅对你选择的资料开放访问":"设备可在“米家”和“小米运动健康”同时使用",null,null,68.36f,700,()->{if(adapt)new AlertDialog.Builder(this).setTitle("共享访问").setMessage("尚未选择共享资料").setPositiveButton("知道了",null).show();else requireAccount("authorization");}),0,180.36f,w,68.36f);
        FrameLayout switchRow=ui.switchRow(w,"新消息通知",adapt?"资料处理完成时通知你":"关闭通知权限，可能会错过重要通知哦",true,c->{});
        // A b14/b15: commit locally; the earlier async login overlay was a separate event.
        View control=switchRow.getChildAt(switchRow.getChildCount()-1);
        control.setOnClickListener(v->{event("request-change","notifications");notifications=!notifications;((StyleKit.SwitchControl)control).checked=notifications;((StyleKit.SwitchControl)control).update();((TextView)switchRow.getChildAt(1)).setText(notifications?(adapt?"资料处理完成时通知你":"关闭通知权限，可能会错过重要通知哦"):"你可能错过重要消息通知，点击开关允许应用通知");event("commit-change",String.valueOf(notifications));});
        ui.at(rows,switchRow,0,248.72f,w,68.36f);
        ui.at(rows,ui.chevronRow(w,"无障碍设置","使用更多无障碍相关功能",null,null,68.36f,700,()->new AlertDialog.Builder(this).setTitle("无障碍设置").setMessage("跟随系统文字大小").setPositiveButton("返回",null).show()),0,317.08f,w,68.36f);
        ui.at(root,rows,26.18f,131.27f-insetCorrection(),w,430);
        TextView button=ui.primaryButton(adapt?"保存偏好":"退出登录",true,()->{if(adapt){event("save-preferences","local");onBackPressed();}else event("guest-logout","no-visible-change");});
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(ui.dp(width-56.72f),ui.dp(46.66f),Gravity.BOTTOM);lp.leftMargin=ui.dp(28.36f);lp.bottomMargin=ui.dp(26.91f);root.addView(button,lp);
        return root;
    }
    void selectWorkspace(){new AlertDialog.Builder(this).setTitle("工作区").setSingleChoiceItems(new String[]{"个人空间","团队资料"},0,(d,w)->{event("select-workspace",String.valueOf(w));d.dismiss();}).setNegativeButton("取消",null).show();}
    FrameLayout transferPage(){
        FrameLayout root=base(false);header(root,ui.topBar("资料",width,false,()->{},()->editDocument()),87);
        FrameLayout c=ui.frame();float inner=width-24;FrameLayout summary=ui.surface();
        ui.at(summary,ui.text("个人资料库",17,700,tokens.text),16,12,inner-32,30);
        ui.at(summary,ui.latin(String.valueOf(documents.size()),32,"misanslatin_semibold.otf",tokens.text),16,47,inner-32,45);
        ui.at(summary,ui.text("份资料 · 保存在此设备",12,400,tokens.secondary),16,93,inner-32,25);ui.at(c,summary,12,12,inner,134);
        if(documents.isEmpty()){
            FrameLayout empty=ui.surface();ui.at(empty,ui.text(loading?"正在整理资料":"这里还没有资料",20,500,tokens.text),24,24,inner-48,42);
            ui.at(empty,ui.text("添加第一份资料，慢慢建立自己的资料库",12,400,tokens.secondary),24,69,inner-48,48);
            ui.at(empty,ui.primaryButton("添加资料",!loading,()->editDocument()),24,134,inner-48,46.66f);ui.at(c,empty,12,158,inner,210);
        } else {float y=158;for(String name:documents){final String item=name;FrameLayout s=ui.surface();ui.at(s,ui.chevronRow(inner-28,item,"保存在此设备",null,"mine_mine_track.webp",68,700,()->new AlertDialog.Builder(this).setTitle(item).setMessage("你可以继续整理这份资料").setNegativeButton("返回",null).setPositiveButton("重命名",(d,w)->editDocument(item)).show()),14,0,inner-28,68);ui.at(c,s,12,y,inner,68);y+=80;}}
        float y=documents.isEmpty()?380:158+documents.size()*80;
        ui.at(c,ui.chevronRow(inner,"偏好设置",null,null,null,60,700,()->push("preferences")),12,y,inner,60);
        ui.at(c,ui.primaryButton(loading?"正在更新":"更新资料",!loading,()->refreshDocuments()),26,y+76,width-52,46.66f);
        TextView failure=ui.text("模拟下次更新失败",12,400,tokens.secondary);ui.at(c,failure,26,y+134,width-52,40);ui.action(failure,"模拟下次更新失败",()->{failNext=true;Toast.makeText(this,"下次更新将显示重试入口",Toast.LENGTH_SHORT).show();});
        scroll(body(root,87,true),c,Math.max(650,y+200));nav(root,0,new String[]{"资料","收藏","共享","我的"},true);return root;
    }
    void rebuildTransfer(){pages.remove("transfer");if(scene.equals("transfer"))show("transfer");}
    void editDocument(){editDocument(null);}
    void editDocument(String original){
        EditText input=new EditText(this);input.setSingleLine(true);input.setHint("资料名称");if(original!=null)input.setText(original);
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle(original==null?"添加资料":"重命名").setView(input).setNegativeButton("取消",(d,w)->event("edit-cancel","preserve")).setPositiveButton("保存",null).create();
        dialog.setOnShowListener(d->{dialog.getButton(-1).setOnClickListener(v->{String value=input.getText().toString().trim();if(value.isEmpty()){input.setError("请输入资料名称");event("validation","empty-name");return;}if(original!=null)documents.remove(original);documents.add(value);event("save-document",value);dialog.dismiss();rebuildTransfer();});input.requestFocus();dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);});dialog.show();
    }
    void refreshDocuments(){
        if(loading){event("refresh-reentry","ignored");return;}loading=true;final int request=++generation;event("refresh","start");rebuildTransfer();
        new Handler(Looper.getMainLooper()).postDelayed(()->{if(!alive||request!=generation)return;loading=false;if(failNext){failNext=false;event("refresh","error-retain-data");new AlertDialog.Builder(this).setTitle("更新未完成").setMessage("已有资料仍然保留，请稍后重试").setNegativeButton("稍后",null).setPositiveButton("重试",(d,w)->refreshDocuments()).show();}else{event("refresh","success");Toast.makeText(this,"资料已更新",Toast.LENGTH_SHORT).show();}rebuildTransfer();},700);
    }
    FrameLayout components(){FrameLayout root=base(false);header(root,ui.topBar("组件",width,false,()->{},()->push("transfer")),87);FrameLayout c=ui.frame();float w=width-24;
        ui.at(c,ui.profile(w,"个人空间","组件状态示例",()->requireAccount("profile")),12,12,w,60);
        ui.at(c,ui.metricCard((w-10)/2,"资料","整理你的资料","health_sleep_"+tokens.mode()+".webp",false,()->editDocument()),12,88,(w-10)/2,240);
        ui.at(c,ui.chevronRow(w,"带说明的设置行","保留辅助说明和下一步入口","未设置",null,70,700,()->selectWorkspace()),12,344,w,70);
        final FrameLayout[] demoSwitch=new FrameLayout[1];demoSwitch[0]=ui.switchRow(w,"新消息通知","受控状态示例",true,v->{ui.setChecked(demoSwitch[0],v);event("component-toggle",String.valueOf(v));});ui.at(c,demoSwitch[0],12,426,w,70);
        ui.at(c,ui.primaryButton("不可提交",false,()->{}),26,522,width-52,46.66f);
        ui.at(c,ui.primaryButton("打开迁移示例",true,()->push("transfer")),26,584,width-52,46.66f);
        ui.at(c,ui.primaryButton("测试弹窗队列",true,()->{ui.confirmSheet("第一条提示","继续",()->event("queue-close","first"),()->event("queue-cancel","first"));ui.confirmSheet("第二条提示","完成",()->event("queue-close","second"),()->event("queue-cancel","second"));}),26,650,width-52,46.66f);scroll(body(root,87,false),c,760);return root;}
    static final class ProgressHero extends View {
        final Paint p=new Paint(3);final Tokens t;ProgressHero(Context c,Tokens t){super(c);this.t=t;setContentDescription("活力指标");}
        @Override protected void onDraw(Canvas c){float d=getResources().getDisplayMetrics().density;float mid=getWidth()/2f;
            p.setShader(null);
            int[] colors=t.night?new int[]{0xff542b21,0xff4d4120,0xff1c3d4b}:new int[]{0xffffe0d4,0xfffff2d1,0xffd3f0ff};
            p.setStyle(Paint.Style.FILL);
            for(int i=0;i<3;i++){float outer=(100-i*(200f/7.2f)*(1+1/6.5f))*d,inner=outer-(200f/7.2f)*d;p.setColor(colors[i]);Path path=new Path();path.addArc(mid-outer,100*d-outer,mid+outer,100*d+outer,180,180);path.lineTo(mid+inner,100*d);path.arcTo(new RectF(mid-inner,100*d-inner,mid+inner,100*d+inner),0,-180);path.close();p.setPathEffect(new CornerPathEffect((200f/7.2f/6.5f/2)*d));c.drawPath(path,p);}
            p.setPathEffect(null);
            p.setStyle(Paint.Style.FILL);
        }
    }
}

package com.eztv.mud;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.ez.socket.SocketClient;
import com.ez.socket.callback.SocketCallback;
import com.ez.utils.BDebug;
import com.ez.utils.BObject;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Enum.direct;
import com.eztv.mud.bean.Enum.messageType;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.SendGameObject;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.RoomDetail;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.controller.MessageController;
import com.eztv.mud.recycleview.adapter.GameChatAdapter;
import com.eztv.mud.recycleview.adapter.GameObjectAdapter;
import com.eztv.mud.util.BAutoSize;
import com.eztv.mud.util.Util;
import com.eztv.mud.util.callback.IAnimatorListener;
import com.eztv.mud.window.GameChatWindow;
import com.eztv.mud.window.GameInputWindow;
import com.eztv.mud.window.GameWindow;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.player;
import static com.eztv.mud.Constant.reconnectDelay;
import static com.eztv.mud.bean.Cmd.doAttack;
import static com.eztv.mud.bean.Cmd.doTalk;
import static com.eztv.mud.bean.Cmd.getAttribute;
import static com.eztv.mud.bean.Cmd.getGG;
import static com.eztv.mud.bean.Cmd.getMapDetail;
import static com.eztv.mud.bean.Cmd.onObjectInRoom;
import static com.eztv.mud.bean.Cmd.onObjectOutRoom;
import static com.eztv.mud.controller.MessageController.doTalk;
import static com.eztv.mud.controller.MessageController.gotByDirect;
import static com.eztv.mud.controller.MessageController.send;
import static com.eztv.mud.recycleview.RvUtil.getGridLayoutManager;
import static com.eztv.mud.recycleview.RvUtil.getLinearLayoutManager;
import static com.eztv.mud.util.BAutoSize.WIDTH_DP;
import static com.eztv.mud.util.Util.getContentView;
import static com.eztv.mud.util.Util.jsonStr2Json;
import static com.eztv.mud.util.Util.msgBuild;

public class GameActivity extends AppCompatActivity implements SocketCallback {
    Context mContext;
    TextView self_describe,tv_map_name;
    RecyclerView rv_chat;
    RecyclerView rv_map_detail;
    Button btn_chat,btn_map,btn_bag,btn_state,game_btn_skill,game_btn_trade;
    Button btn_west,btn_east,btn_north,btn_south,btn_center;
    AppCompatActivity mActivity;
    GameObjectAdapter gameObjectAdapter;
    GameChatAdapter gameChatAdapter;
    ProgressBar bar_hp,bar_mp,bar_exp;
    TextView tv_hp,tv_mp,tv_exp;
    GameWindow gameWindow = new GameWindow();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message m) {
            switch (m.what){
                case 1:
                    Msg msg =  (Msg)m.obj;
                    switch (msg.getType()){
                        case action://指令模式
                            switch (msg.getCmd()){
                                case getMapDetail://获取地图信息
                                    getMapDetail(msg);
                                    break;
                                case "getSelf"://查看玩家自身
                                    getSelf(msg);
                                    break;
                                case getAttribute://获取玩家主角属性
                                    getAttribute(msg);
                                    break;
                                case doAttack://玩家战斗总结
                                    try{
                                        onAtackResponse(msg);
                                    }catch(Exception e){e.printStackTrace();}
                                    break;
                                case doTalk://玩家战斗总结
                                    onTalk(msg);
                                    break;
                                case "reward"://战斗奖惩
                                    onReward(msg);
                                    break;
                            }
                            break;
                        case chat:
                            switch (msg.getCmd()){//收到聊天内容
                                case "公聊":
                                case "私聊":
                                    getChat(msg);
                                    break;
                            }
                            break;
                        case normal:
                            switch (msg.getCmd()){
                                case onObjectInRoom:
                                    onObjectInRoom(msg);
                                    break;
                                case onObjectOutRoom:
                                    onObjectOutRoom(msg);
                                    break;
                                case getGG:
                                    getGG(msg);
                                    break;

                            }
                            break;
                        case input://请求输入框
                            switch (msg.getCmd()){
                                case "chat":
                                    onChatWin(msg);
                                    break;
                                default:
                                    onInputWin(msg);
                                    break;
                            }
                            break;
                        case unHandPop://不可关闭弹窗
                            switch (msg.getCmd()){
                                case "relive":
                                    onUnHandWin(msg);
                            }
                            break;
                        case pop://可以关闭的弹窗
                            onWin(msg);
                            break;
                        case mapPop://可以关闭的弹窗
                            onSkillAttackWin(msg);
                            break;
                    }
                    break;
                case 2:
                    Toast.makeText(mContext, m.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mContext = this;
        mActivity = this;
        MudApplication.getInstance().addActivity(this);
        BAutoSize.applyAdapt(super.getResources(), 375f, WIDTH_DP);
        SocketClient.getInstance().setSocketCallback(this);
        initData();
        initView();
        MessageController.getGG();
    }
    private void initData(){
        MessageController.getAttribute();//
        //查看房间
        MessageController.getMapDetail();
    }

    private void initView() {
        //状态条
        bar_hp =  findViewById(R.id.bar_hp);
        bar_mp =  findViewById(R.id.bar_mp);
        bar_exp =  findViewById(R.id.bar_exp);
        tv_hp = findViewById(R.id.tv_hp);
        tv_mp = findViewById(R.id.tv_mp);
        tv_exp = findViewById(R.id.tv_exp);

        //用户名和简单描述
        self_describe = findViewById(R.id.self_describe);
        String self_describe_str = player.getName()+"</font> lv:"+player.getLevel();
        self_describe.setText(Html.fromHtml(self_describe_str));


        //地图模块
        tv_map_name = findViewById(R.id.tv_map_name);
        btn_west = findViewById(R.id.btn_west);
        btn_east = findViewById(R.id.btn_east);
        btn_north = findViewById(R.id.btn_north);
        btn_south = findViewById(R.id.btn_south);
        btn_center = findViewById(R.id.btn_center);
        btn_west.setOnClickListener(new goLeft());
        btn_east.setOnClickListener(new goRight());
        btn_north.setOnClickListener(new goTop());
        btn_south.setOnClickListener(new goDown());

        //聊天模块
        rv_chat = findViewById(R.id.rv_chat);
        rv_chat.setLayoutManager(getLinearLayoutManager(mContext));
        gameChatAdapter = new GameChatAdapter(this,new ArrayList<>());
        rv_chat.setAdapter(gameChatAdapter);

        //地图详情模块
        rv_map_detail = findViewById(R.id.rv_map_detail);
        rv_map_detail.setLayoutManager(getGridLayoutManager(mContext,3));
        List<SendGameObject> gameObjects = new ArrayList<>();
        gameObjectAdapter = new GameObjectAdapter(this,gameObjects);
        gameObjectAdapter.setIGameObjectCallBack((view, obj) -> {doTalk(obj.getKey()); });
        rv_map_detail.setAdapter(gameObjectAdapter);


        //聊天模块
        btn_chat = findViewById(R.id.game_btn_chat);
        btn_chat.setOnClickListener(view -> send(msgBuild(messageType.input, "公聊",player.getKey(),"公聊")));

        //背包模块
        btn_bag = findViewById(R.id.game_btn_bag);
        btn_bag.setOnClickListener(view -> send(msgBuild(messageType.pop, "getBag",player.getKey(),null)));


        //我的模块
        btn_state = findViewById(R.id.game_btn_state);
        btn_state.setOnClickListener(view -> send(msgBuild(messageType.pop, "getMine",player.getKey(),null)));

        //技能模块
        game_btn_skill= findViewById(R.id.game_btn_skill);
        game_btn_skill.setOnClickListener(view -> send(msgBuild(messageType.pop, "getSkill",player.getKey(),null)));

        //交易模块
        game_btn_trade= findViewById(R.id.game_btn_trade);
        game_btn_trade.setOnClickListener(view -> send(msgBuild(messageType.pop, "getTrade",player.getKey(),null)));



        //地图全局模块
        btn_map = findViewById(R.id.game_btn_map);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        setResult(2);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            try {
                SocketClient.getInstance().getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SocketClient.getInstance().destroyInstance();
            MudApplication.getInstance().finishAllActivity();
//            setResult(2);
//            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class goTop implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            gotByDirect(direct.top);
        }
    }

    public class goLeft implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            gotByDirect(direct.left);
        }
    }

    public class goRight implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            gotByDirect(direct.right);
        }
    }

    public class goDown implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            gotByDirect(direct.down);
        }
    }

    @Override
    public void onError(Exception e) {
    }

    @Override
    public void onClosed(Socket socket) {
        //后期可以写成默认加载本地的密码做自动重连并登录
        //设置禁止重连
        SocketClient.getInstance().setNeedReconnect(false,reconnectDelay);
        sendHandleMessage(2,"断线了!请退出游戏重新进入吧。");
    }

    @Override
    public void onConnected(Socket socket) {

    }

    @Override
    public void onConnectFail(Socket socket, boolean needReconnect) {
        SocketClient.getInstance().setNeedReconnect(false,reconnectDelay);
        sendHandleMessage(2,"断线了!请退出游戏重新进入吧。");
    }

    @Override
    public void onReceive(Socket socket, byte[] bytes) {
        BDebug.trace("onReceive:"+new String(bytes));
        Msg msg =  JSONObject.toJavaObject(JSONObject.parseObject(new String(bytes)),Msg.class);
        sendHandleMessage(1,msg);
    }
    private void sendHandleMessage(int what,Object obj){
        Message m = new Message();
        m.what=what;
        m.obj = obj;
        handler.sendMessage(m);
    }
    //处理服务器发过来的信息，用来界面的展示//////////////////////////////////////////////////////////////////
    public void getMapDetail(Msg msg){//查看房间
        gameWindow.closeAll();
        RoomDetail roomDetail =  JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()),RoomDetail.class);
        gameObjectAdapter.clearAll();
        gameObjectAdapter.addList(roomDetail.getGameObjects());//添加npc

        tv_map_name.setText(roomDetail.getName());
        btn_center.setText(roomDetail.getName());
        btn_south.setText(roomDetail.getDown());
        btn_west.setText(roomDetail.getLeft());
        btn_east.setText(roomDetail.getRight());
        btn_north.setText(roomDetail.getTop());
        if(roomDetail.getDown().length()>0){
            btn_south.setVisibility(View.VISIBLE);
        }else {
            btn_south.setVisibility(View.INVISIBLE);
        }
        if(roomDetail.getLeft().length()>0){
            btn_west.setVisibility(View.VISIBLE);
        }else {
            btn_west.setVisibility(View.INVISIBLE);
        }
        if(roomDetail.getRight().length()>0){
            btn_east.setVisibility(View.VISIBLE);
        }else {
            btn_east.setVisibility(View.INVISIBLE);
        }
        if(roomDetail.getTop().length()>0){
            btn_north.setVisibility(View.VISIBLE);
        }else {
            btn_north.setVisibility(View.INVISIBLE);
        }
    }
    private void getChat(Msg msg) {
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()),Chat.class);
        gameChatAdapter.addChat(chat);
        rv_chat.smoothScrollToPosition(gameChatAdapter.size());
    }

    private void onObjectInRoom(Msg msg) {
        SendGameObject obj=JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()),SendGameObject.class) ;
        gameObjectAdapter.getList().add(obj);
        gameObjectAdapter.notifyItemInserted(gameObjectAdapter.getList().size());
        gameObjectAdapter.notifyItemRangeChanged(gameObjectAdapter.getList().size(),gameObjectAdapter.getList().size() - gameObjectAdapter.getList().size());
    }
    private void onObjectOutRoom(Msg msg) {
        SendGameObject obj = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()),SendGameObject.class);
        gameObjectAdapter.remove(obj);
    }
    private void onAtackResponse(Msg msg) {
        boolean isIAttack=true;//是由主角发起的
        AttackPack obj = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), AttackPack.class);
        List<String> floatMessage =  new ArrayList<String>();
        floatMessage.add(obj.getDesc());

        View whoView;
        View targetView;

        if(BObject.isEmpty(gameObjectAdapter.findPosByKey(obj.getTarget()))||BObject.isEmpty(gameObjectAdapter.findPosByKey(obj.getWho())))
            return;
        if(obj.getWho().equals(player.getKey())){ //主角攻击
            int pos=gameObjectAdapter.findPosByKey(obj.getTarget());
            if(gameObjectAdapter.findObjByKey(obj.getTarget())!=null)
            gameObjectAdapter.findObjByKey(obj.getTarget()).setAttribute(obj.getTargetAttribute());
            gameObjectAdapter.notifyItemChanged(pos);

            whoView = findViewById(R.id.self_describe);
            targetView = rv_map_detail.getChildAt(pos);

        }else{//其他东西攻击
            if(obj.getTarget().equals(player.getKey())){//主角被攻击
                targetView = findViewById(R.id.self_describe);
                readAttribute(obj.getTargetAttribute());
            }else{//东西被攻击
                int pos=gameObjectAdapter.findPosByKey(obj.getWho());
                gameObjectAdapter.findObjByKey(obj.getTarget()).setAttribute(obj.getTargetAttribute());
                gameObjectAdapter.notifyItemChanged(pos);
                targetView = rv_map_detail.getChildAt(pos);
                isIAttack = false;

            }
            whoView = rv_map_detail.getChildAt(gameObjectAdapter.findPosByKey(obj.getWho()));
        }

        showFloatMessage(floatMessage,targetView);
        final ImageView localImageView = new ImageView(mActivity);
        localImageView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        localImageView.setImageResource(R.mipmap.sword);
        if(whoView!=null&&targetView!=null&&isIAttack)
        Util.fightAnim(findViewById(R.id.game_layout),localImageView, whoView, targetView, new IAnimatorListener() {
            @Override
            public void onAnimationPrepare(Object Layout, View showView,View fromView,View toView) {
                ((ConstraintLayout)Layout).addView(localImageView);
            }
            @Override
            public void onAnimationFinish(Object Layout, View showView,View fromView,View toView) {
                handler.postDelayed(() -> ((ConstraintLayout)Layout).removeView(localImageView),300);
            }

        });


    }

    private void getAttribute(Msg msg) {
        Attribute obj = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Attribute.class);
        player.setAttribute(obj);
        readAttribute(obj);
    }
    private void getSelf(Msg msg) {
        SendGameObject obj = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), SendGameObject.class);
        player.setLevel(obj.getLevel());
        player.setAttribute(obj.getAttribute());
        readAttribute(obj.getAttribute());
        String self_describe_str = player.getName()+"</font> lv:"+player.getLevel();
        self_describe.setText(Html.fromHtml(self_describe_str));
    }
    private void readAttribute(Attribute obj){
        bar_exp.setMax((int)obj.getExp_max());
        bar_exp.setProgress((int)obj.getExp());
        bar_hp.setMax((int)obj.getHp_max());
        bar_hp.setProgress((int)obj.getHp());
        bar_mp.setMax((int)obj.getMp_max());
        bar_mp.setProgress((int)obj.getMp());
        tv_exp.setText(obj.getExp()+"/"+obj.getExp_max());
        tv_mp.setText(obj.getMp()+"/"+obj.getMp_max());
        tv_hp.setText(obj.getHp()+"/"+obj.getHp_max());
    }

    //文字悬浮
    private void showFloatMessage(List<String> list,View targetView){
        Rect localRect1 = Util.getViewAbsRect(findViewById(R.id.btn_north));
        int span=1;
        if(targetView!=null){
            localRect1 = Util.getViewAbsRect(targetView);
            span =0;
        }
        localRect1.offset(0, -125);
        for (int i = 0; i < list.size(); i++) {
            final TextView localTextView = new TextView(mActivity);
            localTextView.setText(Html.fromHtml(list.get(i)));
            localTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            localTextView.setTextColor(getResources().getColor(R.color.white));
            final Rect localRect2 = new Rect(localRect1);
            localRect2.offset(0, -(list.size() - i-span) * BAutoSize.value2px(mActivity,1,WIDTH_DP));
            final Rect localRect3 = new Rect(localRect2);
            localRect3.offset(0, -(list.size() - i-span) * BAutoSize.value2px(mActivity,20,WIDTH_DP));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Util.calculateAnim(findViewById(R.id.game_layout), localTextView,localRect2, localRect3, new IAnimatorListener() {
                        @Override
                        public void onAnimationPrepare(Object Layout, View showView, View fromView, View toView) {
                            ((ConstraintLayout) Layout).addView(localTextView);
                        }

                        @Override
                        public void onAnimationFinish(final Object Layout, View showView, View fromView, View toView) {
                            handler.postDelayed(() -> ((ConstraintLayout) Layout).removeView(localTextView),2500);

                        }

                    });
                }
            },i*100);
        }
    }


    private void getGG(Msg msg) {//获取服务器发送过来的公告
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Chat.class);
        gameChatAdapter.addChat(chat);
        gameChatAdapter.notifyDataSetChanged();
        rv_chat.smoothScrollToPosition(gameChatAdapter.size());
    }

    private void onTalk(Msg msg) {//弹出对话框
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        gameWindow.setContent(winMessage.getDesc()).build(mActivity,getContentView(mActivity),msg.getRole());
        gameWindow.setChoiceList(winMessage.getChoice(),winMessage).showBySimpleSize(mActivity);;
    }

    private void onChatWin(Msg msg) {//弹出【聊天】输入框
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        GameChatWindow inputWin =  new GameChatWindow();
        inputWin.build(mActivity,getContentView(mActivity),msg.getRole(),msg.getName());
        inputWin.setList(winMessage.getChoice(),winMessage).showBySimpleSize(mActivity);
    }
    private void onInputWin(Msg msg) {//弹出【窗口】输入框
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        GameInputWindow inputWin =  new GameInputWindow();
        inputWin.setContent(winMessage.getDesc());
        inputWin.build(mActivity,getContentView(mActivity),msg.getRole(),msg.getName());
        inputWin.setList(winMessage.getChoice(),winMessage).showBySimpleSize(mActivity);
    }
    private void onUnHandWin(Msg msg) {//不可操作弹窗
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        gameWindow.setContent(winMessage.getDesc()).build(mActivity,getContentView(mActivity),null);
        gameWindow.setChoiceList(winMessage.getChoice(),winMessage).showFull(mActivity);
    }
    private void onWin(Msg msg) {//可以操作的弹窗
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        gameWindow.setContent(winMessage.getDesc()).build(mActivity,self_describe,msg.getRole());
        //设置物品items rv
        gameWindow.setChoiceList(winMessage.getChoice(),winMessage).showBySimpleSize(mActivity);
    }
    private void onSkillAttackWin(Msg msg) {//地图行走位置的弹窗
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        gameWindow.setContent(winMessage.getDesc()).build(mActivity,self_describe,msg.getRole());
        gameWindow.setChoiceList(winMessage.getChoice(),winMessage).showBySkill(mActivity,msg.getRole());
    }

    private void onReward(Msg msg) {//奖惩
        WinMessage winMessage = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), WinMessage.class);
        showFloatMessage(winMessage.getFloatMessage(),findViewById(R.id.btn_north));
    }
}

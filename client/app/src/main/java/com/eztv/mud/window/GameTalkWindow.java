package com.eztv.mud.window;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ez.utils.BDebug;
import com.eztv.mud.R;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Monster;
import com.eztv.mud.bean.Npc;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.controller.MessageController;
import com.eztv.mud.handler.activity.GameHandle;
import com.eztv.mud.recycleview.RvUtil;
import com.eztv.mud.recycleview.adapter.GameButtonAdapter;
import com.eztv.mud.recycleview.adapter.GameObjectAdapter;
import com.eztv.mud.recycleview.callback.IButtonCallBack;
import com.eztv.mud.window.callback.IChatWindow;

import java.util.ArrayList;
import java.util.List;

public class GameTalkWindow extends BaseWindow implements IButtonCallBack {
    private View talkView;
    private TextView tv_title;
    private RecyclerView rv_answer;
    private Button btn_send;
    private String content="";
    private Activity activity;
    private GameButtonAdapter gameButtonAdapter;
    private String key ;

    public GameTalkWindow setContent(String desc) {
        this.content = desc;
        return this;
    }

    public GameTalkWindow build(Activity context, View target,String key){
        this.key = key;
        activity = context;
        talkView = context.getLayoutInflater().inflate(R.layout.window_talk,null);
        tv_title = talkView.findViewById(R.id.win_talk_detail);
        if(context!=null&&content!=null){
            tv_title.setText(Html.fromHtml(content));
            tv_title.setVisibility(View.VISIBLE);
        }
        rv_answer = talkView.findViewById(R.id.win_talk_rv_answer);
        super.setViewAndTarget(talkView,target);
        return this;
    }
    public void setList(List<Choice> choices){
        rv_answer.setLayoutManager(RvUtil.getGridLayoutManager(activity,3));
        if(choices==null)choices = new ArrayList<>();
        gameButtonAdapter = new GameButtonAdapter(activity,choices,key);
        gameButtonAdapter.setButtonCallBack(this::onClick);
        rv_answer.setAdapter(gameButtonAdapter);

    }

    @Override
    public void onClick(int pos, Choice choice, String key) {
        switch (choice.getCmd()){
            case "attack"://攻击
                MessageController.doAttack(Enum.gameObjectType.monster,key);
                break;
        }
        super.popupWindow.dismiss();
    }
}

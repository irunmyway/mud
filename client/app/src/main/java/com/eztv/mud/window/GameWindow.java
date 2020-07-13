package com.eztv.mud.window;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eztv.mud.R;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.controller.MessageController;
import com.eztv.mud.recycleview.RvUtil;
import com.eztv.mud.recycleview.adapter.GameButtonAdapter;
import com.eztv.mud.recycleview.callback.IButtonCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.controller.MessageController.send;
import static com.eztv.mud.util.Util.msgBuild;

public class GameWindow extends BaseWindow implements IButtonCallBack {
    private View talkView;
    private TextView tv_title;
    private RecyclerView rv_answer;
    private RecyclerView rv_items;
    private Button btn_send;
    private String content="";
    private Activity activity;
    private GameButtonAdapter gameButtonAdapter;
    private String key ;

    public GameWindow setContent(String desc) {
        this.content = desc;
        return this;
    }

    public GameWindow build(Activity context, View target, String key){
        this.key = key;
        activity = context;
        talkView = context.getLayoutInflater().inflate(R.layout.window_normal,null);
        tv_title = talkView.findViewById(R.id.win_detail);
        if(context!=null&&content!=null){
            tv_title.setText(Html.fromHtml(content));
            tv_title.setVisibility(View.VISIBLE);
        }
        rv_answer = talkView.findViewById(R.id.win_rv_answer);
        rv_items =  talkView.findViewById(R.id.win_rv_items);
        super.setViewAndTarget(talkView,target);
        return this;
    }
    public GameWindow setChoiceList(List<Choice> choices, WinMessage winMessage){
        rv_answer.setLayoutManager(RvUtil.getGridLayoutManager(activity,winMessage.getCol()));
        rv_items.setLayoutManager(RvUtil.getGridLayoutManager(activity,3));
        if(choices==null)choices = new ArrayList<>();
        gameButtonAdapter = new GameButtonAdapter(activity,choices,key);
        gameButtonAdapter.setButtonCallBack(this::onClick);
        rv_answer.setAdapter(gameButtonAdapter);
        return this;
    }

    @Override
    public void onClick(int pos, Choice choice, String key) {
        switch (choice.getCmd()){
            case "attack"://攻击
                MessageController.doAttack(Enum.gameObjectType.monster,key);
                break;
                default:send(msgBuild(choice.getType(), choice.getCmd(),choice.getMsg(),choice.getKey()));
        }
        super.popupWindow.dismiss();
    }

    public void dismiss(){
        if(super.popupWindow!=null)
        super.popupWindow.dismiss();
    }

}

package com.eztv.mud.window;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ez.utils.BDebug;
import com.ez.utils.BObject;
import com.eztv.mud.R;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.controller.MessageController;
import com.eztv.mud.recycleview.RvUtil;
import com.eztv.mud.recycleview.adapter.GameButtonAdapter;
import com.eztv.mud.recycleview.callback.IButtonCallBack;
import com.eztv.mud.window.callback.IChatWindow;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.player;
import static com.eztv.mud.bean.Enum.chat.私聊;
import static com.eztv.mud.controller.MessageController.send;
import static com.eztv.mud.util.Util.msgBuild;
import static com.eztv.mud.util.Util.object2JsonStr;

public class GameInputWindow extends BaseWindow implements IButtonCallBack {
    private IChatWindow iChatWindow;
    private View inputView;
    private TextView tv_title;
    private EditText edt_content;
    private String content;
    private RecyclerView rv_answer;
    private Activity activity;
    private GameButtonAdapter gameButtonAdapter;
    private String key ;

    public GameInputWindow setContent(String content) {
        this.content = content;
        return this;
    }

    public GameInputWindow build(Activity context, View target,String key,String name){
        this.key = key;
        this.content = name;
        inputView = context.getLayoutInflater().inflate(R.layout.window_input,null);
        tv_title = inputView.findViewById(R.id.win_chat_title);
        this.activity = activity;
        if(context!=null){
            tv_title.setText(Html.fromHtml(content));
            tv_title.setVisibility(View.VISIBLE);
        }
        edt_content = inputView.findViewById(R.id.win_chat_edt);
        rv_answer = inputView.findViewById(R.id.win_rv_answer);
        super.setViewAndTarget(inputView,target);
        return this;
    }


    public GameInputWindow setList(List<Choice> choices, WinMessage winMessage){
        rv_answer.setLayoutManager(RvUtil.getGridLayoutManager(activity,winMessage.getCol()));
        if(choices==null)choices = new ArrayList<>();
        gameButtonAdapter = new GameButtonAdapter(activity,choices,key);
        gameButtonAdapter.setButtonCallBack(this::onClick);
        rv_answer.setAdapter(gameButtonAdapter);
        return this;
    }


    @Override
    public void onClick(int pos, Choice choice, String key) {
        String str = edt_content.getText().toString().trim();
        switch (choice.getCmd()){
            case "公聊"://公聊
                if(BObject.isNotEmpty(str))if(str.length()>0){
                    Chat chat  = new Chat();
                    chat.setContent(str);
                    chat.setFrom(player.getKey());
                    chat.setFromName(player.getName());
                    send(msgBuild(Enum.messageType.chat, choice.getCmd(),object2JsonStr(chat),key));
                }
                break;
            case "私聊"://
                if(BObject.isNotEmpty(str))if(str.length()>0){
                    Chat chat  = new Chat();
                    chat.setContent(str);
                    chat.setFrom(player.getKey());
                    chat.setFromName(player.getName());
                    chat.setMsgType(私聊);
                    send(msgBuild(Enum.messageType.chat, choice.getCmd(),object2JsonStr(chat),key));
                }
                break;
            default:send(msgBuild(choice.getType(), choice.getCmd(),str, key));
        }
        if(choice.isClose())
        super.popupWindow.dismiss();
    }
}

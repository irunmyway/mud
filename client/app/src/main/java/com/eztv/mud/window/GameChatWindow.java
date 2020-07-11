package com.eztv.mud.window;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eztv.mud.R;
import com.eztv.mud.window.callback.IChatWindow;

public class GameChatWindow extends BaseWindow {
    private IChatWindow iChatWindow;
    private View chatView;
    private TextView tv_title;
    private EditText edt_content;
    private Button btn_send;
    private String content;

    public GameChatWindow setContent(String content) {
        this.content = content;
        return this;
    }

    public GameChatWindow build(Activity context,View target){
        chatView = context.getLayoutInflater().inflate(R.layout.window_chat,null);
        tv_title = chatView.findViewById(R.id.win_chat_title);

        if(context!=null){
            tv_title.setText(Html.fromHtml(content));
            tv_title.setVisibility(View.VISIBLE);
        }
        btn_send = chatView.findViewById(R.id.win_chat_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iChatWindow.onSend(((EditText)chatView.findViewById(R.id.win_chat_edt)).getText().toString(),GameChatWindow.super.popupWindow);
            }
        });
        super.setViewAndTarget(chatView,target);
        return this;
    }

    public GameChatWindow setSendListener(IChatWindow iChatWindow) {
        this.iChatWindow = iChatWindow;
        return this;
    }
}

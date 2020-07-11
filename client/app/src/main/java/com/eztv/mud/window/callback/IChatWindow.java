package com.eztv.mud.window.callback;

import android.widget.PopupWindow;

import com.eztv.mud.window.BaseWindow;

public interface IChatWindow {
    void onSend(String str, PopupWindow window);//发送聊天内容
}

package com.eztv.mud.recycleview.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ez.adapters.adapter.BaseAdapterRvList;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.utils.BObject;
import com.eztv.mud.R;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Enum;

import java.util.List;

public class GameChatAdapter extends BaseAdapterRvList<BaseViewHolder, Chat> {
    public GameChatAdapter(@NonNull Activity activity, List<Chat> list) {
        super(activity, list);
    }
    @Override
    protected void onBindVH(BaseViewHolder holder, int listPosition, Chat chat) {
        //当然，你也可以继承BaseViewHolder自己用黄油刀生成
        String str = "";
        if(BObject.isNotEmpty(chat))
        switch (chat.getMsgType()){
            case 公聊:
                str+="<font color=\"#FFD700\">【公聊】</font><u>"+chat.getPlayer().getName()+"</u>:";
                break;
            case 私聊:
                str+="<font color=\"#C71585\">【私聊】</font>";
                break;
            case 系统:
                str+="<font color=\"#DC143C\">【系统】</font>";
                break;
            default:
        }
        str+=chat.getContent();
        holder.setText(R.id.tv_chat_content, Html.fromHtml(str)).setViewVisible(R.id.tv_chat_content, str == null ? View.GONE : View.VISIBLE);
    }
    @NonNull
    @Override
    protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
        return new BaseViewHolder(parent,R.layout.item_chat);
    }

    public void addChat(Chat chat){
        mList.add(chat);
        notifyItemInserted(mList.size());
    }
}
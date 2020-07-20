package com.eztv.mud.recycleview.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ez.adapters.adapter.BaseAdapterRvList;
import com.ez.adapters.base.BaseViewHolder;
import com.eztv.mud.R;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.recycleview.callback.IButtonCallBack;
import com.eztv.mud.recycleview.callback.IGameObjectCallBack;

import java.util.List;

import static com.eztv.mud.bean.Cmd.doChat;
import static com.eztv.mud.controller.MessageController.send;
import static com.eztv.mud.util.Util.msgBuild;
import static com.eztv.mud.util.Util.object2JsonStr;

public class GameButtonAdapter extends BaseAdapterRvList<BaseViewHolder, Choice> {
    String key;
    Activity activity;
    IButtonCallBack iButtonCallBack;
    public GameButtonAdapter(@NonNull Activity activity, List<Choice> list,String key) {
        super(activity, list);
        this.key = key;
    }
    @Override
    protected void onBindVH(BaseViewHolder holder, int i, Choice obj) {
        String str = "";
        if(obj==null) return;
        str = obj.getName();//到时候添加物品颜色对照表
        holder.setText(R.id.tv_game_object_name, Html.fromHtml(str)).setViewVisible(R.id.tv_game_object_name, str == null ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(view -> {
            if(iButtonCallBack!=null){
                iButtonCallBack.onClick(i,obj,key);
            }
        });
    }
    @NonNull
    @Override
    protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
        return new BaseViewHolder(parent,R.layout.item_game_win_button);
    }

    public void setButtonCallBack(IButtonCallBack iButtonCallBack) {
        this.iButtonCallBack = iButtonCallBack;
    }
}
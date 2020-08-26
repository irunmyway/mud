package com.eztv.mud.recycleview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.ez.adapters.adapter.BaseAdapterRvList;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.utils.BObject;
import com.eztv.mud.R;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.SendGameObject;
import com.eztv.mud.recycleview.callback.IGameObjectCallBack;

import java.util.List;

import static com.ez.utils.BString.getHTMLStr;

public class GameObjectAdapter extends BaseAdapterRvList<BaseViewHolder, SendGameObject> {
    Activity activity;
    IGameObjectCallBack iGameObjectCallBack;

    public GameObjectAdapter(@NonNull Activity activity, List<SendGameObject> list) {
        super(activity, list);
        this.activity = activity;
    }

    @Override
    protected void onBindVH(BaseViewHolder holder, int listPosition, SendGameObject obj) {
        ProgressBar progressBar = holder.itemView.findViewById(R.id.hp_progress);
        String str = "";
//        if(obj instanceof Item)str+=((Item)obj).getName();//到时候添加物品颜色对照表
//        if(obj instanceof Item){
//            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_blue));
        switch (obj.getObjType()) {
            case player:
                str += obj.getName() + "(" + obj.getAttribute().getHp() + ")";
                progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_green));
                break;
            case npc:
                str += obj.getName() + "(" + obj.getAttribute().getHp() + ")";
                progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_blue));
                break;
            case monster:
                str += obj.getName() + "(" + obj.getAttribute().getHp() + ")";
                progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_brown));
                break;
            default:
                progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_green));
        }
        try {
            progressBar.setMax((int) obj.getAttribute().getHp_max());
            progressBar.setProgress((int) obj.getAttribute().getHp());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_game_object_name, getHTMLStr(str)).setViewVisible(R.id.tv_game_object_name, str == null ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(view -> {
            if (iGameObjectCallBack != null) {
                iGameObjectCallBack.onClick(holder.itemView, obj);
            }
        });
    }

    @NonNull
    @Override
    protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
        return new BaseViewHolder(parent, R.layout.item_game_object);
    }

    public void setIGameObjectCallBack(IGameObjectCallBack iGameObjectCallBack) {
        this.iGameObjectCallBack = iGameObjectCallBack;
    }

    public void setList(List<SendGameObject> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addList(List<SendGameObject> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void remove(SendGameObject gameObject) {
        try {
            int pos = 0;
            for (GameObject obj : mList) {
                if (obj.getKey() != null)
                    if (obj.getKey().equals(gameObject.getKey())) {
                        pos = mList.indexOf(obj);
                        mList.remove(obj);
                        break;
                    }
            }
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, mList.size() - pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int findPosByKey(String key) {
        int i = 0;
        for (SendGameObject obj : mList) {
            if (BObject.isNotEmpty(obj.getKey()))
                if (obj.getKey().equals(key))
                    return i;
            i++;
        }
        return i;
    }

    public SendGameObject findObjByKey(String key) {
        for (SendGameObject obj : mList) {
            if (BObject.isNotEmpty(obj.getKey()))
                if (obj.getKey().equals(key))
                    return obj;
        }
        return null;
    }
}
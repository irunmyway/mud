package com.eztv.mud.recycleview.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.ez.adapters.adapter.BaseAdapterRvList;
import com.ez.adapters.base.BaseViewHolder;
import com.ez.utils.BDebug;
import com.ez.utils.BObject;
import com.eztv.mud.R;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Monster;
import com.eztv.mud.bean.Npc;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.recycleview.callback.IGameObjectCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.player;

public class GameObjectAdapter extends BaseAdapterRvList<BaseViewHolder, GameObject> {
    Activity activity;
    IGameObjectCallBack iGameObjectCallBack;
    public GameObjectAdapter(@NonNull Activity activity, List<GameObject> list) {
        super(activity, list);
        this.activity = activity;
    }
    @Override
    protected void onBindVH(BaseViewHolder holder, int listPosition, GameObject obj) {
        ProgressBar progressBar = holder.itemView.findViewById(R.id.hp_progress);
        String str = "";
        if(obj instanceof Item)str+=((Item)obj).getName();//到时候添加物品颜色对照表
        if(obj instanceof Player)str+=((Player)obj).getName()+"("+((Player)obj).getPlayerData().getAttribute().getHp()+")";
        if(obj instanceof Npc)str+=((Npc)obj).getName()+"("+obj.getAttribute().getHp()+")";
        if(obj instanceof Monster)str+=((Monster)obj).getName()+"("+obj.getAttribute().getHp()+")";
        if(obj instanceof Item){
            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_blue));
        }else if(obj instanceof Player){
            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_green));
        }else if(obj instanceof Npc){
            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_blue));
        }else if(obj instanceof Monster){
            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_brown));
        }else{
            progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_green));
        }
        try{
            if(obj instanceof Player){
                progressBar.setMax((int) ((Player) obj).getPlayerData().getAttribute().getHp_max());
                progressBar.setProgress((int)  ((Player) obj).getPlayerData().getAttribute().getHp());
            }else{
                progressBar.setMax((int) obj.getAttribute().getHp_max());
                progressBar.setProgress((int) obj.getAttribute().getHp());
            }
        }catch(Exception e){}

        holder.setText(R.id.tv_game_object_name, Html.fromHtml(str)).setViewVisible(R.id.tv_game_object_name, str == null ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(view ->{
            if(iGameObjectCallBack!=null){
                iGameObjectCallBack.onClick(holder.itemView,obj);
            }
        });
    }
    @NonNull
    @Override
    protected BaseViewHolder onCreateVH(ViewGroup parent, LayoutInflater inflater) {
        return new BaseViewHolder(parent,R.layout.item_game_object);
    }

    public void setIGameObjectCallBack(IGameObjectCallBack iGameObjectCallBack) {
        this.iGameObjectCallBack = iGameObjectCallBack;
    }

    public void setList(List<GameObject> list){
        mList = list;
        notifyDataSetChanged();
    }
    public void addList(List<GameObject> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }
    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void remove(GameObject gameObject) {
        try{
            int pos=0;
            for (GameObject obj:mList) {
                if(obj.getKey()!=null)
                if(obj.getKey().equals(gameObject.getKey())){
                    pos = mList.indexOf(obj);
                    mList.remove(obj);
                    break;
                }
            }
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos,mList.size()-pos);
        }catch(Exception e){e.printStackTrace();}
    }
    public int findPosByKey(String key){
        int i = 0;
        for (GameObject obj : mList){
            if(BObject.isNotEmpty(obj.getKey()))
                if (obj.getKey().equals(key))
                    return i;
            i++;
        }
        return i;
    }
    public GameObject findObjByKey(String key){
        for (GameObject obj : mList){
            if(BObject.isNotEmpty(obj.getKey()))
                if (obj.getKey().equals(key))
                    return obj;
        }
        return null;
    }
}
package com.eztv.mud.window;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eztv.mud.R;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.SendGameObject;
import com.eztv.mud.recycleview.RvUtil;
import com.eztv.mud.recycleview.adapter.GameObjectAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameBagWindow extends BaseWindow {
    private View View;
    private TextView tv_money;
    private RecyclerView rv_bag;
    private Button btn;
    private String content;
    private Activity activity;

    public GameBagWindow setContent(String content) {
        this.content = content;
        return this;
    }

    public GameBagWindow build(Activity context, View target){
        activity = context;
        View = context.getLayoutInflater().inflate(R.layout.window_bag,null);
        //金钱展示
        tv_money = View.findViewById(R.id.win_bag_money);
        if(context!=null){
            tv_money.setText(Html.fromHtml(content));
            tv_money.setVisibility(View.VISIBLE);
        }
        //关闭按钮
        btn = View.findViewById(R.id.win_bag_close);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        //背包初始化
        rv_bag = View.findViewById(R.id.win_bag_items);
        initBag();
        super.setViewAndTarget(View,target);
        return this;
    }

    void initBag(){
        rv_bag.setLayoutManager(RvUtil.getGridLayoutManager(activity,2));
        List<SendGameObject> gameObjects = new ArrayList<>();
//        gameObjects.add(new Item("物品:小石头",3));
//        gameObjects.add(new Item("物品:小石头",3));
//        gameObjects.add(new Item("物品:金疮药金疮药",3));
//        gameObjects.add(new Item("物品:金疮药",3));
//        gameObjects.add(new Item("物品:金疮药",3));
//        rv_bag.setAdapter(new GameObjectAdapter(activity,gameObjects));
    }


}

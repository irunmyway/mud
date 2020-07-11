package com.eztv.mud.recycleview;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RvUtil {
    public static LinearLayoutManager getLinearLayoutManager(Context context){
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.VERTICAL);
        return lm;
    }

    public static GridLayoutManager getGridLayoutManager(Context context,int span){
        GridLayoutManager gm = new GridLayoutManager(context,span);
        gm.setOrientation(RecyclerView.VERTICAL);
        return gm;
    }
}

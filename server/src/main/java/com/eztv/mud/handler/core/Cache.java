package com.eztv.mud.handler.core;

import com.eztv.mud.handler.DataBaseHandler;
import com.eztv.mud.utils.BProp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cache implements ActionListener {
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();
    private Timer timer ;

    public Cache() {
        int minute = 5;
        try{
            minute = Integer.parseInt(BProp.getInstance().get("save_delay"));
        }catch(Exception e){e.printStackTrace();}
        this.timer = new Timer(minute*1000*60, this);
//        this.timer = new Timer(1000, this);

        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dataBaseHandler.cacheToDataBase();
    }
}

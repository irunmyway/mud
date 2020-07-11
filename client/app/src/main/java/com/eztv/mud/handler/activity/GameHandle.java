package com.eztv.mud.handler.activity;

import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.controller.MessageController;
import com.eztv.mud.recycleview.callback.IButtonCallBack;

public class GameHandle {
    private static GameHandle instance;
    private static ButtonListener buttonListener;



    public static GameHandle getInstance(){
        if(instance==null)instance = new GameHandle();
        return instance;
    }

    public ButtonListener getButtonListener(){
        if(buttonListener==null)buttonListener = new ButtonListener();
        return buttonListener;
    }



}
class ButtonListener implements IButtonCallBack{
    @Override
    public void onClick(int pos, Choice choice, String key) {


    }
}

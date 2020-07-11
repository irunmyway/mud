package com.eztv.mud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.ez.socket.SocketClient;
import com.ez.socket.callback.SocketCallback;
import com.ez.utils.BDebug;
import com.ez.utils.BStorage;
import com.ez.utils.BString;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Login;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.util.BAutoSize;
import com.eztv.mud.util.BShareDB;

import java.net.Socket;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-05 11:44
 * 功能: 注册功能 已完成
 **/
public class RegistActivity extends AppCompatActivity implements SocketCallback {
    Button btn_register,btn_close;
    String name,pwd,roleName;
    Enum.sex sex;
    Context mContext;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(SplashActivity.mContext, "注册成功!", Toast.LENGTH_SHORT).show();
                    save();
                    setResult(1);
                    finish();
                    break;
            }
        }
    };

    private void save() {
        BShareDB.saveData(mContext,"account",name);
        BShareDB.saveData(mContext,"pwd",pwd);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BAutoSize.applyAdapt(super.getResources(), 375f, BAutoSize.WIDTH_DP);
        mContext = this;
        MudApplication.getInstance().addActivity(this);
        SocketClient.getInstance().setSocketCallback(this);
        initView();
    }
    private void initView() {
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(view -> {
            setResult(1);
            finish();
        });
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(view -> {
            name = ((EditText) findViewById(R.id.et_user_name)).getText().toString().trim();
            pwd = ((EditText) findViewById(R.id.et_psw)).getText().toString().trim();
            roleName = ((EditText) findViewById(R.id.et_role_name)).getText().toString().trim();
            sex = ((RadioButton) findViewById(R.id.rb_male)).isChecked()? Enum.sex.male: Enum.sex.female;
            if ((name.length() > 4) && (pwd.length() > 4) && (roleName.length() <= 10) && (pwd.length() <= 16)&&(name.length() <= 16)) {
                if (!BString.isChinese(roleName)||roleName.length()<1) {
                    Toast.makeText(mContext, getString(R.string.game_login_notice1), Toast.LENGTH_SHORT).show();
                    return;
                }
                if((! BString.isUsername(name+pwd))){
                    Toast.makeText(mContext, getString(R.string.game_login_notice), Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始注册
                Login login = new Login();
                login.setType(Enum.messageType.login);
                login.setLogin(Enum.login.register);
                login.setName(name);
                login.setPasswd(pwd);
                login.setRole(roleName);
                login.setSex(sex);
                SocketClient.getInstance().sendMsgByLength(JSONObject.parseObject(JSONObject.toJSON(login).toString()).toJSONString().getBytes());
                login = null;
            }else{
                Toast.makeText(mContext, getString(R.string.game_login_notice), Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(1);
        finish();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onClosed(Socket socket) {

    }

    @Override
    public void onConnected(Socket socket) {

    }

    @Override
    public void onConnectFail(Socket socket, boolean needReconnect) {

    }

    @Override
    public void onReceive(Socket socket, byte[] bytes) {
        Msg msg =  JSONObject.toJavaObject(JSONObject.parseObject(new String(bytes)),Msg.class);
        if(msg.getType()==Enum.messageType.normal){//注册成功标志
            if(JSONObject.parseObject(msg.getMsg()).getString("name")!=null){
                sendHandleMessage(2,null);
            }
        }else if(msg.getType()==Enum.messageType.toast){//注册失败标志
            sendHandleMessage(1,msg.getMsg());
        }
    }
    private void sendHandleMessage(int what,Object obj){
        Message m = new Message();
        m.what=what;
        m.obj = obj;
        handler.sendMessage(m);
    }

}

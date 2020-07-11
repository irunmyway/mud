package com.eztv.mud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.ez.socket.SocketClient;
import com.ez.socket.callback.SocketCallback;
import com.ez.utils.BDebug;
import com.ez.utils.BString;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Login;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.util.BAutoSize;
import com.eztv.mud.util.BShareDB;

import java.io.IOException;
import java.net.Socket;

import static com.eztv.mud.Constant.IP;
import static com.eztv.mud.Constant.Port;
import static com.eztv.mud.Constant.player;
import static com.eztv.mud.Constant.reconnectDelay;

public class SplashActivity extends AppCompatActivity implements SocketCallback {
    Button btn_login,btn_register;
    EditText name,pwd;
    String strName,strPwd;
    public static Context mContext;
    boolean reconnect;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){//登录失败提示
                case 1:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2://登录成功。
                    save();
                    Intent it = new Intent(mContext,GameActivity.class);
                    startActivityForResult(it,1);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BAutoSize.applyAdapt(super.getResources(), 375f, BAutoSize.WIDTH_DP);
        CheckGrant();
        mContext = this;
        MudApplication.getInstance().addActivity(this);
        initSocket();
        initView();
    }
    private void initSocket(){
        //开启套接字监听
        try{
            SocketClient.getInstance().setSocketCallback(this);
            SocketClient.getInstance().setMaxReceiveMB(5);
            SocketClient.getInstance().createConnection(IP,Port,reconnectDelay,true).start();
        }catch(Exception e){e.printStackTrace();}
    }

    private void initView() {
        name = findViewById(R.id.et_user_name);
        pwd = findViewById(R.id.et_psw);
        name.setText(BShareDB.getData(mContext,"account","").toString());
        pwd.setText(BShareDB.getData(mContext,"pwd","").toString());
        strName = name.getText().toString().trim();
        strPwd = pwd.getText().toString().trim();
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//登录/////////////////////////////////////////////////////////////
                strName = name.getText().toString().trim();
                strPwd = pwd.getText().toString().trim();
                if ((strName.length() > 4) && (strPwd.length() > 4) &&  (strPwd.length() <= 16)&&(strName.length() <= 16)) {
                    if((! BString.isUsername(strName+strPwd))){
                        Toast.makeText(mContext, getString(R.string.game_login_notice), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //开始登录
                    Login login = new Login();
                    login.setType(Enum.messageType.login);
                    login.setLogin(Enum.login.login);
                    login.setName(strName);
                    login.setPasswd(strPwd);
                    SocketClient.getInstance().sendMsgByLength(JSONObject.parseObject(JSONObject.toJSON(login).toString()).toJSONString().getBytes());
                }else{
                    Toast.makeText(mContext, getString(R.string.game_login_notice), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btn_register= findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//注册按钮///////////////////////////////////////////////////////////
                Intent it = new Intent(mContext,RegistActivity.class);
                startActivityForResult(it,1);
            }
        });
    }
    private void save() {
        BShareDB.saveData(mContext,"account",strName);
        BShareDB.saveData(mContext,"pwd",strPwd);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocketClient.getInstance().setNeedReconnect(true,reconnectDelay);
        SocketClient.getInstance().setSocketCallback(SplashActivity.this);
        //SocketClient.getInstance().reConnect();
        strName = BShareDB.getData(mContext,"account","").toString();
        strPwd = BShareDB.getData(mContext,"pwd","").toString();
        name.setText(strName);
        pwd.setText(strPwd);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onClosed(Socket socket) {
        //短线了断线提示
        BDebug.trace("onClosed");
        sendHandleMessage(1,"断线了!");
        reconnect = true;
    }

    @Override
    public void onConnected(Socket socket) {
        BDebug.trace("在登录界面onConnected");
        //连接成功关闭窗口
        if(reconnect)sendHandleMessage(1,"重新连接成功!");
    }

    @Override
    public void onConnectFail(Socket socket, boolean needReconnect) {
        BDebug.trace("onConnectFail");
        SocketClient.getInstance().reConnect();
        sendHandleMessage(1,"连接失败了!");
        reconnect = true;
    }

    @Override
    public void onReceive(Socket socket, byte[] bytes) {
        Msg msg =  JSONObject.toJavaObject(JSONObject.parseObject(new String(bytes)),Msg.class);
        if(msg.getType()==Enum.messageType.normal){//注册成功标志
            if(JSONObject.parseObject(msg.getMsg()).getString("name")!=null){
                player = JSONObject.parseObject(msg.getMsg(),Player.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            try {
                SocketClient.getInstance().getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SocketClient.getInstance().destroyInstance();
            MudApplication.getInstance().finishAllActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //等待授予权限
    private void CheckGrant() {
        String[] permissions = {
                "android.permission.RECEIVE_BOOT_COMPLETED",
                "android.permission.READ_PHONE_STATE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.SYSTEM_ALERT_WINDOW"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                try {
                    Thread.sleep(3000);
                    CheckGrant();
                } catch (Exception e) {
                }
            } else {
            }
        }
    }
}

package com.eztv.mud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.eztv.mud.util.CheckUpdate;
import com.eztv.mud.util.Util;
import com.eztv.mud.util.callback.IUpdateCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import static com.eztv.mud.Constant.IP;
import static com.eztv.mud.Constant.Port;
import static com.eztv.mud.Constant.player;
import static com.eztv.mud.Constant.reconnectDelay;
import static com.eztv.mud.util.BScreen.getScreenWidth;

public class SplashActivity extends AppCompatActivity implements SocketCallback , IUpdateCallBack {
    Button btn_login,btn_register;
    EditText name,pwd;
    String strName,strPwd;
    String downUrl,updateText;
    public static Context mContext;
    AppCompatActivity mActivity;
    private PopupWindow mUpdateWindow;
    Button btn_update_ok,btn_update_cancel;
    TextView tv_update_content;
    ProgressBar progressbar;
    String appName ="";
    boolean reconnect;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){//登录失败提示
                case 1:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2://登录成功。
                    save();
                    Intent it = new Intent(mContext,GameActivity.class);
                    startActivityForResult(it,1);
                    break;
                case 3://升级
                    showUpdate();
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BAutoSize.applyAdapt(super.getResources(), 375f, BAutoSize.WIDTH_DP);
        CheckGrant();
        mContext = this;
        mActivity = this;
        MudApplication.getInstance().addActivity(this);
        CheckUpdate.getInstance().setiUpdateCallBack(this);//监听升级
        try{
            CheckUpdate.getInstance().checkUpdateByGithub(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        }catch(Exception e){e.printStackTrace();}
        try{
            appName= getPackageManager()
                    .getApplicationLabel((getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo))
                    .toString();
        }catch(Exception e){e.printStackTrace();}
        initSocket();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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

    private void showUpdate(){
        LayoutInflater inflater = getLayoutInflater();
        View ez_updateView = inflater.inflate(R.layout.win_update, null);
        btn_update_cancel =  ez_updateView.findViewById(R.id.btn_update_cancel);
        tv_update_content = ez_updateView.findViewById(R.id.tv_update_content);
        tv_update_content.setText(Html.fromHtml(updateText));
        btn_update_ok =  ez_updateView.findViewById(R.id.btn_update_ok);
        progressbar = (ez_updateView.findViewById(R.id.pb_downloading));
        btn_update_cancel.setOnClickListener(view -> {if(mUpdateWindow!=null)mUpdateWindow.dismiss();});
        btn_update_ok.setOnClickListener(view -> {
            btn_update_ok.setVisibility(View.INVISIBLE);
            btn_update_cancel.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            new download().start();
        });
        mUpdateWindow = new PopupWindow(ez_updateView, getScreenWidth(mContext) *9/ 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        mUpdateWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mUpdateWindow.setFocusable(false);
        mUpdateWindow.setOutsideTouchable(false);
        mUpdateWindow.update();
        mUpdateWindow.showAtLocation(Util.getContentView(mActivity), Gravity.CENTER, 0, 0);
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
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.SYSTEM_ALERT_WINDOW",
                "android.permission.REQUEST_INSTALL_PACKAGES"};
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

    @Override
    public void onUpdate(String downUrl, String text) {
        this.downUrl = downUrl;
        this.updateText = text;
        sendHandleMessage(3,null);
    }


    String DIR_DIY_DOWN = Environment.getExternalStorageDirectory().getPath() + "/" +appName  + "/download/";
    class download extends Thread {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                File filefolder = new File(DIR_DIY_DOWN);
                if (!filefolder.exists()) {
                    filefolder.mkdirs();
                }
                URL url = new URL(downUrl);
                connection = (HttpURLConnection) url.openConnection();
//                boolean isGitee = Update.ez_url.indexOf("gitee") > 0;
                if (true) {
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh;Intel Mac OS X 10_12_6) ");
                    connection.setRequestProperty("Cookie", "user_locale=zh-CN; oschina_new_user=false; remember_user_token=BAhbB1sGaQMU%2BRZJIiIkMmEkMTAkVDRmRHlDVFdWdy80bHFJRjN1NXpDTwY6BkVU--fc8dfbe5fa543c081e5fbb808b80678cdea91384; remove_bulletin=gitee-maintain-1583921812; remote_way=svn; tz=Asia%2FShanghai; Hm_lvt_24f17767262929947cc3631f99bfd274=1584001314,1584004331,1584005905,1584006413; Hm_lpvt_24f17767262929947cc3631f99bfd274=1584028654; gitee-session-n=BAh7CUkiD3Nlc3Npb25faWQGOgZFVEkiJTY0ZjIxODhiZmI4N2QwOWIyZmJlOTcyOTBiYWQ3NDg5BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAxT5FkkiIiQyYSQxMCRUNGZEeUNUV1Z3LzRscUlGM3U1ekNPBjsAVEkiHXdhcmRlbi51c2VyLnVzZXIuc2Vzc2lvbgY7AFR7BkkiFGxhc3RfcmVxdWVzdF9hdAY7AFRJdToJVGltZQ2QCR7ANRFCDAk6CXpvbmVJIghVVEMGOwBGOg1uYW5vX251bWkBfToNbmFub19kZW5pBjoNc3VibWljcm8iBxJQSSIQX2NzcmZfdG9rZW4GOwBGSSIxTWVOT3hlem44NitURzI5YnBoU3RtaEVlM2cxSlVmaStJVHBFVE0weXAyVT0GOwBG--2b165ed10131fe19a0eecda8d8c614a9f056b393");
                } else {
                    connection.setRequestProperty("Accept-Encoding", "identity");
                }
                connection.connect();
                File file = new File(DIR_DIY_DOWN, "mud.apk");
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                int count;
                int total = 0;
                int fileLength = 0;
                if (true) {
                    try {
                        fileLength = 2 * 1000 * 1000;
                    } catch (Exception e) {
                        fileLength = 0;
                    }

                } else {
                    fileLength = connection.getContentLength();
                }
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                    total += count;
                    progressbar.setProgress(total * 100 / fileLength);
                }
                installAPK();
            } catch (Exception e) {
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }

        }
    }

    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        File file = new File(DIR_DIY_DOWN, "mud.apk");
        Uri apkPath = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0以上
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.eztv.mud.provider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(apkPath, type);
        }
        mContext.startActivity(intent);
    }
}

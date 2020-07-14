package com.eztv.mud.util;

import android.util.Base64;

import com.eztv.mud.util.callback.IUpdateCallBack;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CheckUpdate {
    private IUpdateCallBack iUpdateCallBack;
    private static CheckUpdate instance;

    public static CheckUpdate getInstance() {
        if (instance==null)
            instance = new CheckUpdate();
        return instance;
    }

    public void checkUpdateByGithub(final String verName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    try {httpget("https://gitee.com/qq1025334900/eztv", "UA");
                        json = httpget("https://gitee.com/qq1025334900/mud/raw/master/mud.txt", "UA");
                    } catch (Exception e) {
                    }
                    JSONObject object = new JSONObject(json);
                    String serverVer = object.optString("appver");
                    if (serverVer.startsWith("V")) {
                        serverVer = serverVer.toUpperCase().replace("V", "");
                    }
                    String myver = verName;
                    if (myver.startsWith("V")) {
                        myver.toUpperCase().replace("V", "");
                    }
                    if (myver.compareTo(serverVer) < 0) {
                        try {
                            String ez_url = object.optString("appurl");
                            String ez_text = object.optString("apptext");
                            iUpdateCallBack.onUpdate(ez_url,ez_text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }

            }
        }).start();

    }


    public static String httpget(String geturl,String useragent) {
        try {
            URL url=new URL(geturl );

            URLConnection connection=url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",useragent);
            connection.connect();
            String result="";
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line+"\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setiUpdateCallBack(IUpdateCallBack iUpdateCallBack) {
        this.iUpdateCallBack = iUpdateCallBack;
    }
}

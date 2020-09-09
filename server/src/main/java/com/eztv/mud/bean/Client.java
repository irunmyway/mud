package com.eztv.mud.bean;

import com.eztv.mud.bean.net.Player;
import com.eztv.mud.script.ScriptExecutor;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.eztv.mud.Constant.通信检查;
import static com.eztv.mud.utils.BArray.byteMerger;
import static com.eztv.mud.utils.BArray.intToByteArray;
public class Client {
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Socket socket;
    private Player player; //绑定玩家
    private String role;//用户名称//当前登录的账号唯一标识
    private final int HEAD_LENGTH=2;//包头长度存储空间


    private ScriptExecutor scriptExecutor = new ScriptExecutor(this);//给每个玩家添加独立的脚本执行器

    public Client(Socket socket, Player player) {
        this.socket = socket;
        this.player = player;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {}
    }

    public Client(Socket socket, Player player,String role) {
        this.socket = socket;
        this.player = player;
        this.role = role;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {}
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ScriptExecutor getScriptExecutor() {
        return scriptExecutor;
    }

    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }

    //发送数据
    public void sendMsg(final Object args){
        new Thread(new Runnable() {
            public void run() {
                if (BObject.isEmpty(args)) {
                    return;
                }
                if (socket != null && socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        try {
                            if(args instanceof byte[]){
                                if(通信检查)
                                BDebug.trace("发送"+((byte[])args).length+"---"+new String((byte[])args));
                                out.write(byteMerger(intToByteArray(((byte[])args).length,HEAD_LENGTH),(byte[])args));
                            }
                            if(args instanceof String){
                                if(通信检查)
                                BDebug.trace("发送"+((String) args).getBytes().length+"---"+new String(((String) args).getBytes()));
                                out.write(byteMerger(intToByteArray(((String) args).getBytes().length,HEAD_LENGTH),((String) args).getBytes()));
                            }
                            out.flush();
                        } catch (Exception e) {
                            //e.printStackTrace();
//                            try {
//                                socket.close();
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
                        }
                    }
                }
            }
        }).start();
    }
}

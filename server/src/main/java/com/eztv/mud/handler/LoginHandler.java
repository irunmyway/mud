package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Login;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import com.eztv.mud.utils.BString;
import online.sanen.cdm.api.condition.C;

import java.util.Date;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.constant.Cmd.LOGIN_SUCCESS;

public class LoginHandler {
    public static void login(Client client, JSONObject jsonObject) {//登录注册模块
        Player player = new Player(client);
        Msg msg = new Msg();
        Login login = JSONObject.toJavaObject(jsonObject, Login.class);
        if (BObject.isNotEmpty(login.getLogin()))
            if (login.getLogin() == Enum.login.register) {//注册//////////////////////////////////////////////////////////////////////
                login.setCreateAt(new Date());
                login.setUpdateAt(new Date());
                boolean flag = false;
                if ((login.getName().length() > 4) && (login.getPasswd().length() > 4) && (login.getPasswd().length() <= 16) && (login.getName().length() <= 16)) {
                    if ((BString.isUsername(login.getName() + login.getPasswd()))) {
                        if (!(BString.isSql(login.getName() + login.getPasswd() + login.getRole() + login.getSex()))) {
                            try {
                                if (DataBase.getInstance().init().query(login).insert() > 0)//注册账号完成 //注册角色完成
                                    if (DataBase.getInstance().init().createSQL("insert into `role`(account,name,sex,createat) values('" + login.getName() + "','" + login.getRole() + "','" + login.getSex() + "',CURRENT_TIMESTAMP())").update() > 0)
                                        flag = true;
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        BDebug.trace("测试" + "123123");
                    }
                }
                if (flag == false) {
                    msg.setType(Enum.messageType.toast);
                    msg.setMsg("注册失败,可能用户名重复。");
                } else {
                    msg.setType(Enum.messageType.normal);
                    msg.setCmd(LOGIN_SUCCESS);//返回Player
                    player = getPlayer(login.getName(), login.getPasswd(), client);
                    msg.setMsg(JSONObject.parseObject(JSONObject.toJSON(player).toString()).toJSONString());
                    for (Client item : clients) {
                        if (item.equals(client)) {
                            client.setPlayer(player);
                            item = client;//初始化并绑定Player
                        }
                    }
                }
            } else {//登录模块//////////////////////////////////////////////////////////////////////
                if ((login.getName().length() > 4) && (login.getPasswd().length() > 4) && (login.getPasswd().length() <= 16) && (login.getName().length() <= 16)) {
                    if ((BString.isUsername(login.getName() + login.getPasswd()))) {
                        if (!(BString.isSql(login.getName() + login.getPasswd()))) {
                            player = getPlayer(login.getName(), login.getPasswd(), client);
                            if (player.getName() != null) {
                                msg.setType(Enum.messageType.normal);
                                msg.setCmd(LOGIN_SUCCESS);//返回Player
                                for (Client item : clients) {
                                    if (item.equals(client)) {
                                        //待完善//先保存Player 再做新的登录设置player防止数据丢失
                                        client.setPlayer(player);
                                        client.setRole(login.getName());//设置用户账号登录标识
                                        item = client;//初始化并绑定Player
                                    }
                                }
                                msg.setMsg(JSONObject.parseObject(JSONObject.toJSON(player).toString()).toJSONString());
                                msg.setRole(client.getRole());
                            } else {
                                msg.setType(Enum.messageType.toast);
                                msg.setMsg("账号或密码错误。");
                            }

                        }
                    }
                }
            }
        client.sendMsg(JSONObject.parseObject(JSONObject.toJSON(msg).toString()).toJSONString());
    }

    //登录成功
    public static Player getPlayer(String account, String password, Client client) {
        Player player = DataBase.getInstance().init().createSQL("select t1.name,t1.faction_position,t1.sex,t1.faction,t1.level,t1.data,t1.createat,t1.updateat from role t1,account t2 where t1.account = t2.account").addCondition(C.eq("t1.account", account)).addCondition(C.eq("t2.pwd", password)).unique(Player.class);
        try{
            player.setAccount(account);
            player.setClient(client);
        }catch (Exception e){return new Player();}
        player.setKey(BDate.getNowMills() + "");
        if(player.getSex()==Enum.sex.female){//女性
            player.setName( "<font color=\"#FF69B4\">"+player.getName()+"</font>");
        }else{
            player.setName( "<font color=\"#6495ED\">"+player.getName()+"</font>");
        }
        return DataHandler.getPlayer(client,player);
    }


}

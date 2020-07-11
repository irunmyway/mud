package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.DataBase;
import com.eztv.mud.Word;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.Login;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import com.eztv.mud.utils.BString;
import online.sanen.cdm.api.condition.C;

import java.util.Date;
import java.util.HashMap;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.bean.GameCmd.*;

public class LoginHandler {
    public static void login(Client client, JSONObject jsonObject) {//登录注册模块
        Player player = new Player();
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
                    player = getPlayer(login.getName(), login.getPasswd(), null);
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

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayer(String account, String password, Client client) {
        Player player = DataBase.getInstance().init().createSQL("select t1.name,t1.sex,t1.level,t1.data,t1.createat from role t1,account t2 where t1.account = t2.account").addCondition(C.eq("t1.account", account)).addCondition(C.eq("t2.pwd", password)).unique(Player.class);
        player.setKey(BDate.getNowMills() + "");

        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            if (player.getAttribute().getExp() < 1) {//玩家身上没有信息
                Attribute attribute = new Attribute();
                attribute.setHp(base.getHp());
                attribute.setMp(base.getMp());
                attribute.setExp(base.getExp());
                attribute.setHp_max(base.getHp());
                attribute.setExp_max(base.getExp());
                attribute.setMp_max(base.getMp());
                attribute.setAck(base.getAck());
                player.getPlayerData().setAttribute(attribute);
            }
        }
        return player;
    }
}

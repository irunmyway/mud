package com.eztv.mud.command.commands.player;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.command.commands.BaseCommand;

import static com.eztv.mud.GameUtil.getGameObject;
import static com.eztv.mud.GameUtil.jsonStr2Json;

public class Attack extends BaseCommand {
    public Attack(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        AttackPack attackPack = JSONObject.toJavaObject(jsonStr2Json(getMsg().getMsg()), AttackPack.class);
        if(attackPack.getTarget()!=null){
            //不能和自己打
            if(attackPack.getTarget().equals(getPlayer().getKey()))
                return;
        }
        GameObject gameObject=getGameObject(getClient(),attackPack.getTarget());
        getPlayer().setBattle(getClient(),getPlayer(),gameObject,attackPack.getTarget());
    }
}

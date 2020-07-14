package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Attribute;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.PlayerData;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-11 22:12
 用处：由于实体存在大量冗余信息 传输只传输部分
**/
public class SendGameObject extends GameObject {
    private int level;
    private Enum.gameObjectType objType;
    private PlayerData playerData;

    public Enum.gameObjectType getObjType() {
        return objType;
    }

    public void setObjType(Enum.gameObjectType objType) {
        this.objType = objType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
    public Attribute getAttribute() {
        if(objType==Enum.gameObjectType.player)
            return getPlayerData().getAttribute();
        return super.getAttribute();
    }
    public void setAttribute(Attribute attribute) {
        if(objType==Enum.gameObjectType.player){
            getPlayerData().setAttribute(attribute);
        }else{
            super.setAttribute(attribute);
        }
    }
}

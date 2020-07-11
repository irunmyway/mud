package com.eztv.mud.bean;

import com.eztv.mud.bean.net.Player;

public class Chat {
    private Enum.chat msgType=Enum.chat.公聊;
    private String content=""; //内容

    private Player player;//谁说话
    private Player target;//对谁说
    private Item item; //后期可用来展示

    public Chat() {
    }

    public Chat(Enum.chat msgType, String content) {
        this.msgType = msgType;
        this.content = content;
    }

    public Enum.chat getMsgType() {
        return msgType;
    }

    public void setMsgType(Enum.chat msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

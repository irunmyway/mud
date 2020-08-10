package com.eztv.mud.bean;

import com.eztv.mud.constant.Enum;

public class Chat {
    private Enum.chat msgType=Enum.chat.公聊;
    private String content=""; //内容

    private String from;//谁说话
    private String fromName;//谁说话
    private String to;//对谁说
    private String toName;//对谁说
    private Item item; //后期可用来展示

    public Chat() {
    }

    public Chat(Enum.chat msgType, String content) {
        this.msgType = msgType;
        this.content = content;
    }

    public static Chat system(String sendStr){
        Chat chat = new Chat();
        chat.setContent(sendStr);
        chat.setMsgType(Enum.chat.系统);
        return chat;
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


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

package com.eztv.mud.handler.bean.commands;


import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;

public abstract class BaseCommand {
    private Client client;
    private Msg msg;
    private String key;
    public BaseCommand(Client client, Msg msg,String key) {
        this.client = client;
        this.msg = msg;
        this.key = key;
        execute();
    }

    public abstract void execute() ;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

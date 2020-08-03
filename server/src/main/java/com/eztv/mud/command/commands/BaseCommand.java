package com.eztv.mud.command.commands;


import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;

public abstract class BaseCommand {
    private Client client;
    private Msg msg;
    private String key;

    public BaseCommand(Client client, Msg msg, String key) {
        this.client = client;
        this.msg = msg;
        this.key = key;
        execute();
    }

    public abstract void execute();

    public Client getClient() {
        return client;
    }
    public Player getPlayer() {
        return client.getPlayer();
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

package com.eztv.mud.command.commands;


import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand {
    private Client client;
    private Msg msg;
    private String key;
    private WinMessage winMsg = new WinMessage();
    private List<Choice> choice = new ArrayList<>();
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

    public WinMessage getWinMsg() {
        return winMsg;
    }

    public List<Choice> getChoice() {
        return choice;
    }
}

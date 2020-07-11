package com.eztv.mud.bean;

import java.util.ArrayList;
import java.util.List;

public class Choice implements Cloneable{
    private String name;
    private String cmd;
    private String msg;

    public String getName() {
        return name;
    }

    public static Choice createChoice(String name,String cmd,String msg){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        return c;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

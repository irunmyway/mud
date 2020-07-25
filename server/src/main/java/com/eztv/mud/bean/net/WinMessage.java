package com.eztv.mud.bean.net;

import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.Choice;

import java.util.ArrayList;
import java.util.List;

public class WinMessage {
    private List<String> floatMessage = new ArrayList<>();
    private String desc;
    private List<Choice> choice = new ArrayList<>();
    private int col=3;


    public List<String> getFloatMessage() {
        return floatMessage;
    }

    public void setFloatMessage(List<String> floatMessage) {
        this.floatMessage = floatMessage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void 添加选项集合(LuaUtil luaUtil){
        this.choice.addAll(luaUtil.getChoice());
    }
    public void 添加选项(Choice c){
        this.choice.add(c);
    }
}

package com.eztv.mud.bean.task;

import com.eztv.mud.utils.BDebug;

public class TaskAction {
    private String id;//目标id
    private int num;//目标数量
    private int process=0;//进度
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int addProcess(int num){
        return this.process+=num;
    }

    public int getProcess() {
        return process;
    }
}

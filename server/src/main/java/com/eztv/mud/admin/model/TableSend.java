package com.eztv.mud.admin.model;

import com.eztv.mud.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class TableSend<T> {
    int code = 0;
    String msg = "";
    int count =0;
    List<T> data = new ArrayList<T>();

    public TableSend() {
    }

    public TableSend(int code, String msg, int count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String toJson(){
        return GameUtil.object2JsonStr(this);
    }
}

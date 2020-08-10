package com.eztv.mud.bean;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;

import java.util.HashMap;
import java.util.Map;

public class MsgMap {
    private Map<String,Object> map = new HashMap<>();
    public MsgMap 到消息(String str) {
        try{
            return JSONObject.toJavaObject(GameUtil.jsonStr2Json(str),MsgMap.class);
        }catch(Exception e){return new MsgMap();}
    }
    public MsgMap 清空(){
        map.clear();return this;
    }
    public MsgMap 创建(){
       return new MsgMap();
    }
    public MsgMap 添加(String key,Object value){
        map.put(key,value);return this;
    }
    public MsgMap 删除(String key){
        map.remove(key);return this;
    }
    public Object 取值(String key){
        return map.get(key);
    }
    public String 到文本(){
        return GameUtil.object2JsonStr(this);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}

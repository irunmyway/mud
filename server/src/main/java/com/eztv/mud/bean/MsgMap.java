package com.eztv.mud.bean;

import com.eztv.mud.GameUtil;

import java.util.HashMap;
import java.util.Map;

public class MsgMap {
    private Map<String,Object> map = new HashMap<>();
    public void 清空(){
        map.clear();
    }
    public void 添加(String key,Object value){
        map.put(key,value);
    }
    public void 删除(String key){
        map.remove(key);
    }
    public String 到文本(String key){
        return GameUtil.object2JsonStr(map);
    }
}

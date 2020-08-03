package com.eztv.mud.admin.uitls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;

public class AdminUtil {
    public static JSONArray toJsonArr(Object object){
        return  JSONObject.parseArray( GameUtil.objectArr2JsonStr(object)) ;
    }
}

package com.eztv.mud.test;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        DataBase.getInstance().init();//加载数据库框架
        DataBase.getInstance().init().createSQL("delete from t_attribute").update();
        Long hp = 100L;
        Long mp = 100L;
        Long exp = 1000L;
        Long ack = 5L;
        for (int i=1;i<100;i++){//角色公式生成
            DataBase.getInstance().init().createSQL("insert into t_attribute values('"+i+"','"+hp+"','"+mp+"'," +
                    "'"+exp+"','"+ack+"')").update();
            hp+=i*30;
            mp+=i*50;
            exp+=i*100;
            ack+=i*5;
        }
//        int a = 10;
//        System.out.println(a -= 5);

    }

    public void sayHello() {
        System.out.println("hellow!!!");
    }

    @org.junit.Test
    public void test1() {//先进先出
        List<Integer> list = new ArrayList<Integer>(5);
        for (int i = 0; i < 10; i++) {
            list.add(i);
            if (list.size() > 5) {
                list.remove(0);
            }
        }
        for (int i :
                list) {
            BDebug.trace("测试" + i);
        }
    }

    @org.junit.Test
    public void test2(){
        Choice c = new Choice();
        c.setMsg("asd");
        List<Choice> list = new ArrayList<>();
        list.add(c);
        String arr = JSONObject.parseArray(JSONObject.toJSON(list).toString()).toJSONString();
        List<Choice> cl = JSONObject.toJavaObject(GameUtil.jsonStr2JsonArr(arr), List.class);
    }


}
class testClass{
    static testClass t ;
    public static testClass getT(){
        if(t==null){
            t= new testClass();
        }
        return t;
    }
}

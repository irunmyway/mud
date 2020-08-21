package com.eztv.mud.test;

import com.eztv.mud.bean.net.Player;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class TestJavaScript {

    @Test
    public void doJavaScript(){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String script = "lua/test.js";
        try{
            FileReader scriptFile = new FileReader(script);
            engine.eval(scriptFile);
            Invocable in = (Invocable)engine;
            Player player = new Player();
            in.invokeFunction("jsFun",player);
            System.out.println("通过脚本修改后："+player.getName());
//            String newCallTime = (String)invocable.invokeFunction("getCallTime", callTime, timestamp);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}

package com.eztv.mud.script;

import com.eztv.mud.utils.BDebug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-19 18:28
 * 功能: 脚本工厂
 **/
public class ScriptFactory {
    //到时候可以放缓存里
    public static Map<String,FileReader> scripts = new HashMap<>();
    public static Map<String,String> scriptStrings = new HashMap<>();
    public static FileReader getScript(String script){
        try {
            FileReader cacheScript = scripts.get(script);
            if(cacheScript==null){
                cacheScript= new FileReader(script);
                scripts.put(script,cacheScript);
            }
            return cacheScript;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getScriptString(String script){
        synchronized (scriptStrings){
            String ret =null;
            StringBuffer sb = new StringBuffer();
            try {
                ret = scriptStrings.get(script);
                if(ret==null){
                    FileReader cacheScript=null;
                    try{
                        cacheScript = new FileReader(script);
                    }catch(Exception e){return null;}
                    BufferedReader br = new BufferedReader(cacheScript);
                    String line = null;
                    while((line = br.readLine())!=null) {
                        sb.append(line).append("\n");
                    }
                    ret= sb.toString();
                    scriptStrings.put(script,ret);
                    br.close();
                    cacheScript.close();
                }
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public static void clearScriptStrings(){
        synchronized (scriptStrings){
            scriptStrings.clear();
        }
        System.out.println("更新脚本完成。");
    }
}

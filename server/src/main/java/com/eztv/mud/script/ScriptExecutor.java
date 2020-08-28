package com.eztv.mud.script;

import com.eztv.mud.Constant;
import com.eztv.mud.GameUtil;
import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Bag;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * This is the context for a script. It provides the interface between the
 * engine and the script as well as any API functionality for the included
 * script.
 *
 * @author lycis
 */
public class ScriptExecutor {

    private Globals luaGlobals = null;
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("javascript");
    Invocable in;
    private String curFile;
    private GameUtil gameUtil =  new GameUtil();
    private LuaUtil luaUtil =  new LuaUtil();
    private Bag bag = new Bag();
    private Attribute attribute = new Attribute();


    public ScriptExecutor() {
    }

    public ScriptExecutor load(String scriptPath) {
        switch (Constant.script){
            case lua:
                if(!scriptPath.endsWith(".lua"))scriptPath = scriptPath+".lua";
                if(luaGlobals==null)luaGlobals = JsePlatform.standardGlobals();
                luaGlobals.STDOUT = System.out;
                try {
                    curFile = scriptPath;
                    LuaValue script = luaGlobals.loadfile(scriptPath).call();
                } catch (LuaError e1) {
                }
                return this;
            case js:
                if(!scriptPath.endsWith(".js"))scriptPath = scriptPath+".js";
                try {
                    engine.put("游戏工具",gameUtil);
                    engine.put("脚本工具", luaUtil);
                    engine.put("背包", bag);
                    engine.put("属性",attribute );
                    String script = ScriptFactory.getScriptString(scriptPath);
                    if(script==null)script ="";
                    engine.eval(script);
                    in = (Invocable)engine;
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
                return this;
        }
       return null;
    }

    public Object execute(String function, Object... args) {
        switch (Constant.script){
            case lua:
                LuaValue func = luaGlobals.get(function);
                if(curFile==null) return null;
                if (func == null || func == LuaValue.NIL||curFile.equals(".lua")) {
                    return null;
                }
                if (!(func instanceof LuaFunction)) {
                    System.out.println("is not fun");
                    return null;
                }
                if (args.length < 1) {
                    return func.call();
                } else {
                    LuaValue[] luaArgs = new LuaValue[args.length];
                    for (int i = 0; i < args.length; ++i) {
                        luaArgs[i] = CoerceJavaToLua.coerce(args[i]);
                    }
                    try{
                        Varargs v = func.invoke(LuaValue.varargsOf(luaArgs));
                        if (v.narg() <= 0) {
//                    return LuaValue.NIL;
                            return null;
                        } else if (v.narg() == 1) {
                            return v.arg1();
                        } else {
                            return new LuaTable(v);
                        }
                    }catch(Exception e){e.printStackTrace();}
//            return LuaValue.NIL;
                    return null;
                }
            case js:
                try {
                    Object obj =  in.invokeFunction(function,args);
                    return obj==null?null:obj;
                } catch (Exception e) {
                    System.out.println(e.toString());
                    return null;
                }
        }
        return null;
    }

}

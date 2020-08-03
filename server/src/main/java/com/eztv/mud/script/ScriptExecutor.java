package com.eztv.mud.script;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * This is the context for a script. It provides the interface between the
 * engine and the script as well as any API functionality for the included
 * script.
 *
 * @author lycis
 */
public class ScriptExecutor {

    private Globals luaGlobals = null;
    private Object entity;
    private String curFile;

    public ScriptExecutor() {
    }

    public ScriptExecutor loadFile(Object entity, String scriptPath) {
        if(!scriptPath.endsWith(".lua"))scriptPath = scriptPath+".lua";
        this.entity = entity;
        // set API references
        if(luaGlobals==null)luaGlobals = JsePlatform.standardGlobals();
        luaGlobals.STDOUT = System.out;
        luaGlobals.set("self", CoerceJavaToLua.coerce(entity));
//	luaGlobals.set("inherit", new org.jrune.script.functions.Inherit(this));

        // load and compile script
        try {
            curFile = scriptPath;
            LuaValue script = luaGlobals.loadfile(scriptPath).call();
        } catch (LuaError e1) {
            //e1.printStackTrace();
        }
        return this;
    }

    public LuaValue execute(String function, Object... args) {
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

            // translate arguments into function parameters
            LuaValue[] luaArgs = new LuaValue[args.length];
            for (int i = 0; i < args.length; ++i) {
                luaArgs[i] = CoerceJavaToLua.coerce(args[i]);
            }
            try{
                // execute with parameters
                Varargs v = func.invoke(LuaValue.varargsOf(luaArgs));

                // return according value
                if (v.narg() <= 0) {
                    return LuaValue.NIL;
                } else if (v.narg() == 1) {
                    return v.arg1();
                } else {
                    return new LuaTable(v);
                }
            }catch(Exception e){e.printStackTrace();}
            return LuaValue.NIL;
        }
    }

}

package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.NpcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/npc/")
public class NpcController {
    @Autowired
    NpcImpl npc;
    @RequestMapping("getNpcList")
    public String getNpcList(String value,int page, int limit){
        if(value!=null)return npc.getNpcListByWhere(value, page, limit);
        return npc.getNpcList(page, limit);
    }
    @RequestMapping("createNpc")
    public String createNpc(){
        if(npc.createNpc()){
            return "success";
        }
        return null;
    }
    @RequestMapping("delNpc")
    public String delNpc(String id){
        if(npc.delNpc(id))return "success";
        return null;
    }
    @RequestMapping("saveNpc")
    public String saveNpc(String id, String name, String desc, String map, String script, String num) {
        if(npc.saveNpc(id,name,desc,map,script,num))return "";
        return null;
    }
}

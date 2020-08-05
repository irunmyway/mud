package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.MonsterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/monster/")
public class MonsterController {
    @Autowired
    MonsterImpl monster;
    @RequestMapping("getMonsterList")
    public String getMonsterList(String value,int page, int limit){
        if(value!=null)return monster.getMonsterListByWhere(value,page ,limit );
        return monster.getMonsterList(page,limit );
    }
    @RequestMapping("createMonster")
    public String createMonster(){
        if(monster.createMonster()){
            return "success";
        }
        return null;
    }
    @RequestMapping("delMonster")
    public String delMonster(String id){
        if(monster.delMonster(id))return "success";
        return null;
    }
    @RequestMapping("saveMonster")
    public String saveMonster(String id, String name, String script,int num,long refreshment,int map) {
        if(monster.saveMonster(id,name,script,num,refreshment,map))return "";
        return null;
    }
}

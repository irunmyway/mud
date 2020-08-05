package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.SkillImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/skill/")
public class SkillController {
    @Autowired
    SkillImpl skill;
    @RequestMapping("getSkillList")
    public String getSkillList(String value,int page, int limit){
        if(value!=null)return skill.getSkillListByWhere(value, page, limit);
        return skill.getSkillList(page, limit);
    }
    @RequestMapping("createSkill")
    public String createSkill(){
        if(skill.createSkill()){
            return "success";
        }
        return null;
    }
    @RequestMapping("delSkill")
    public String delSkill(String id){
        if(skill.delSkill(id))return "success";
        return null;
    }
    @RequestMapping("saveSkill")
    public String saveSkill(String id, String name, String script) {
        if(skill.saveSkill(id,name,script))return "";
        return null;
    }
}

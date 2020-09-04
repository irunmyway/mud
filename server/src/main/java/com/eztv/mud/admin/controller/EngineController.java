package com.eztv.mud.admin.controller;

import com.eztv.mud.Word;
import com.eztv.mud.utils.BFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/engine/")
public class EngineController {
    @RequestMapping("saveGG")
    public String saveGG(String content){
        String src = System.getProperty("user.dir")+"/gg";
        BFile.writeFile(src,content);
        Word.getInstance().initConf();
        Word.getInstance().initGG();//加载公告
        Word.getInstance().initColor();//加载公告
        Word.getInstance().initEnvironment();
        return null;
    }

    @RequestMapping("getGG")
    public String getGG(){
        return Word.getInstance().getGG();
    }
}

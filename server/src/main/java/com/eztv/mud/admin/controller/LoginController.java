package com.eztv.mud.admin.controller;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.model.TableSend;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String toIndex (Model model)
    {
        String mud_ip = GameUtil.getProp("mud_ip");
        model.addAttribute("mud_ip",mud_ip);
        return "index";
    }
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index (Model model)
    {
        String mud_ip = GameUtil.getProp("mud_ip");
        model.addAttribute("mud_ip",mud_ip);
        return "index";
    }

    @RequestMapping("logout")
    public String  login(){
        TableSend tableSend = new TableSend();
        tableSend.setCode(-1);
        return tableSend.toJson();
    }
}

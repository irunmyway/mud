package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.model.TableSend;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index ()
    {
        return "index";
    }

    @RequestMapping("logout")
    public String  login(){
        TableSend tableSend = new TableSend();
        tableSend.setCode(-1);
        return tableSend.toJson();
    }
}

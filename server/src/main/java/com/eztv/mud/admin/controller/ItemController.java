package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.ItemImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item/")
public class ItemController {
    @Autowired
    ItemImpl item;
    @RequestMapping("getItemList")
    public String getItemList(String value,int page, int limit){
        if(value!=null)return item.getItemListByWhere(value, page, limit);
        return item.getItemList(page, limit);
    }
    @RequestMapping("createItem")
    public String createItem(){
        if(item.createItem()){
            return "success";
        }
        return null;
    }
    @RequestMapping("delItem")
    public String delItem(String id){
        if(item.delItem(id))return "success";
        return null;
    }
    @RequestMapping("saveItem")
    public String saveItem(String id, String name, String script) {
        if(item.saveItem(id,name,script))return "";
        return null;
    }
}

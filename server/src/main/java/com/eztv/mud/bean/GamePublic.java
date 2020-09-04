package com.eztv.mud.bean;

import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.Table;

import java.util.Date;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-09-04 23:18
 * 功能: 工共存储变量
 **/
@Table(name = "t_game_public")
public class GamePublic {
    @Id
    String id ="";
    String data ="";
    Date createat;
    Date updateat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    public Date getUpdateat() {
        return updateat;
    }

    public void setUpdateat(Date updateat) {
        this.updateat = updateat;
    }
}

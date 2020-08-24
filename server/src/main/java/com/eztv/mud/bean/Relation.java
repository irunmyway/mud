package com.eztv.mud.bean;

import com.eztv.mud.constant.Enum;
import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.Table;

import java.util.Date;
import java.util.UUID;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-22 22:54
 * 功能: 关系 好友 仇人 伴侣 黑名单
 **/
@Table(name = "t_relation")
public class Relation {
    @Id
    private String id;
    private String role;
    private String toRole;
    private Enum.relation type;
    private Date createat;

    public Relation() {
        id = UUID.randomUUID().toString();
    }

    public Enum.relation getType() {
        return type;
    }

    public void setType(Enum.relation type) {
        this.type = type;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToRole() {
        return toRole;
    }

    public void setToRole(String toRole) {
        this.toRole = toRole;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Relation) {
            Relation relation = (Relation) obj;
            if (relation.getToRole().equals(getToRole()) && relation.getType() == ((Relation) obj).getType()) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

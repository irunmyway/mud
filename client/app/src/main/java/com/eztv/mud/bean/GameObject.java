package com.eztv.mud.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-02 20:40
 * 功能: 游戏物品人物等等所有元素的基类
 **/
public  class GameObject{
     String objectName;
     String objectDesc;
     private String desc;//实体介绍说明
     String key;
     private Attribute attribute = new Attribute();

     public String getObjectName() {
          return objectName;
     }

     public void setObjectName(String objectName) {
          this.objectName = objectName;
     }

     public String getObjectDesc() {
          return objectDesc;
     }

     public void setObjectDesc(String objectDesc) {
          this.objectDesc = objectDesc;
     }

     public String getKey() {
          return key;
     }

     public void setKey(String key) {
          this.key = key;
     }

     public Attribute getAttribute() {
          return attribute;
     }

     public void setAttribute(Attribute attribute) {
          this.attribute = attribute;
     }

     public String getDesc() {
          return desc;
     }

     public void setDesc(String desc) {
          this.desc = desc;
     }

}

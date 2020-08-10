package com.eztv.mud.bean;

import com.eztv.mud.bean.net.Player;
import online.sanen.cdm.template.jpa.Column;
import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-04 13:09
 * 功能: 房间
 **/
@Table(name = "t_map")
public class Room extends GameObject {
    @Id
    private int id=0;
    private String name = "";//地图名称也是中心
    @Column(name = "type")
    private String roomType="1";//安全区或者等等。。。。
    @Column(name = "west")
    private int left = -1;
    @Column(name = "north")
    private int top = -1;
    @Column(name = "east")
    private int right = -1;
    @Column(name = "south")
    private int down = -1;
    private int capacity;
    private String script;
    private int lv;//限制等级

    private Date createat;
    private Date updateat;

    List<GameObject> npcList = new ArrayList<>();
    List<GameObject> monsterList = new ArrayList<>();
    List<GameObject> playerList = new ArrayList<>();



    public synchronized void add(GameObject obj){
        if(obj instanceof Monster){
            monsterList.remove(obj);
            monsterList.add(obj);
        }
        if(obj instanceof Npc){
            npcList.remove(obj);
            npcList.add(obj);
        }
        if(obj instanceof Player){
            playerList.remove(obj);
            playerList.add(obj);
        }
    }
    public synchronized void remove(GameObject obj){
        if(obj instanceof Monster)monsterList.remove(obj);
        if(obj instanceof Npc)npcList.remove(obj);
        if(obj instanceof Player)playerList.remove(obj);
    }


    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public List<GameObject> getNpcList() {
        return npcList;
    }

    public void setNpcList(List<GameObject> npcList) {
        this.npcList = npcList;
    }

    public List<GameObject> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<GameObject> monsterList) {
        this.monsterList = monsterList;
    }

    public List<GameObject> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<GameObject> playerList) {
        this.playerList = playerList;
    }

    public Room() {
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String getScript() {
        return script==null?"":script;
    }

    @Override
    public void setScript(String script) {
        this.script = script;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }



    @Override
    public Attribute getAttribute() {
        return null;
    }

}

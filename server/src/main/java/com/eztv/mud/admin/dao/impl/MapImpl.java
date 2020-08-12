package com.eztv.mud.admin.dao.impl;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.config.DatabaseFactory;
import com.eztv.mud.admin.dao.MapDAO;
import com.eztv.mud.admin.model.MapSend;
import com.eztv.mud.admin.model.item.EdgeModel;
import com.eztv.mud.admin.model.item.MapModel;
import com.eztv.mud.admin.model.item.RoomModel;
import com.eztv.mud.utils.BDate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-31 10:31
 * 功能:
 **/
@Service
public class MapImpl implements MapDAO {
    DatabaseFactory db;

    private int distance=120;

    @Override
    public String getMap() {
        long x = 1;
        long y = 1;
        List<RoomModel> rooms = db.init().createSQL("select * from t_map_room").list(RoomModel.class);
        Map<Integer,RoomModel> maps = new HashMap<>();
        for (RoomModel room:rooms){
            maps.put(room.getId(),room);
        }
        List<MapModel> nodes = new ArrayList<>();
        List<EdgeModel> edges = new ArrayList<>();
        Map<Integer,RoomModel> single = new HashMap<>();
        synchronized (maps){
            for (RoomModel room:rooms){//地图
                MapModel node = new MapModel();
                EdgeModel edge = new EdgeModel();
                node.setId(room.getId());
                node.setLabel(room.getName());
                node.setDesc(room.getDesc());
                RoomModel parentRoom = hasParentPos(maps,room,node,edge);
                if(parentRoom==null){//默认节点位置
                    node.setX(x);
                    node.setY(y);
                    room.setX(x);
                    room.setY(y);
                    maps.replace(room.getId(),room);
                    for (RoomModel room1:rooms){//地图
                        if (room.getId() != 0) {
                            if (room1.getTop() == room.getId()) {//单独的房间 没有连接其他的
                                single.put(room.getId(),room1);
                            }
                        }
                    }
                }
                nodes.add(node);
                edges.add(edge);
            }
            for (RoomModel room1:rooms) {//地图
                if(single.containsKey(room1.getId())){//单独的房间 没有连接其他的
//                        BDebug.trace("测试"+room1.getName());
//                        BDebug.trace("测试"+single.get(room1.getId()).getName());
                }
            }
        }
        MapSend mapSend = new MapSend();
        mapSend.setNodes(nodes);
        mapSend.setEdges(edges);
        return  mapSend.toJson();
    }

    @Override
    public boolean saveRoom(int id, String name, String desc, String script) {
        if(db.init().createSQL(""+
                        "update t_map_room set " +
                        "name = ?, " +
                        "script=?," +
                        "`desc`=?," +
                        "refreshment=?," +
                        "map=? " +
                        "where id = ?"
                ,name,script,desc,id).update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createRoomByBase(int id, String direction, String name, String desc, String script) {
        String directionColumn = "north";
        switch (direction){
            case "r":
                directionColumn = "east";
                break;
            case "l":
                directionColumn = "west";
                break;
            case "d":
                directionColumn = "south";
                break;
        }
        int newId = BDate.getNowMillsByTen();
        if(db.init().createSQL(""+
                        "update t_map_room set " +directionColumn+
                        " = ? " +
                        "where id = ?"
                ,newId,id).update()>0){
            switch (direction){
                case "r":
                    directionColumn = "west";
                    break;
                case "l":
                    directionColumn = "east";
                    break;
                case "d":
                    directionColumn = "north";
                    break;
                default:
                    directionColumn = "south";
            }
            if(db.init().createSQL("insert into t_map_room(id,createat,"+directionColumn+",script,name,`desc`) values('"+ newId +"',current_timestamp(),?,?,?,?);",id,script,name,desc).update()>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean createRoom() {
        int newId = BDate.getNowMillsByTen();
        if(db.init().createSQL("insert into t_map_room(id,createat) values('"+ newId +"',current_timestamp());").update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean delRoom(int id) {
        if(db.init().createSQL("delete from t_map_room where id = ?",id).update()>0){
            return true;
        }
        return false;
    }

    //查看依赖的节点有位置信息 没有就让自己建立 有就参考依赖相对建立
    private RoomModel hasParentPos(Map<Integer,RoomModel> maps ,RoomModel room,MapModel node,EdgeModel edge){
        RoomModel parentRoom = null;
        if(maps.get(room.getLeft())!=null)
            if(maps.get(room.getLeft()).getX()!=0 || maps.get(room.getLeft()).getY()!=0){
                parentRoom = maps.get(room.getLeft());
                node.setX(parentRoom.getX()+distance);
                node.setY(parentRoom.getY());
                room.setX(parentRoom.getX()+distance);
                room.setY(parentRoom.getY());
            }
        if(maps.get(room.getRight())!=null)
            if(maps.get(room.getRight()).getX()!=0 || maps.get(room.getRight()).getY()!=0){
                parentRoom = maps.get(room.getRight());
                node.setX(parentRoom.getX()-distance);
                node.setY(parentRoom.getY());
                room.setX(parentRoom.getX()-distance);
                room.setY(parentRoom.getY());
            }
        if(maps.get(room.getTop())!=null)
            if(maps.get(room.getTop()).getX()!=0 || maps.get(room.getTop()).getY()!=0){
                parentRoom = maps.get(room.getTop());
                node.setX(parentRoom.getX());
                node.setY(parentRoom.getY()+distance);
                room.setX(parentRoom.getX());
                room.setY(parentRoom.getY()+distance);
            }

        if(maps.get(room.getDown())!=null)
            if(maps.get(room.getDown()).getX()!=0 || maps.get(room.getDown()).getY()!=0){
                parentRoom = maps.get(room.getDown());
                node.setX(parentRoom.getX());
                node.setY(parentRoom.getY()-distance);
                room.setX(parentRoom.getX());
                room.setY(parentRoom.getY()-distance);

            }
        if(parentRoom!=null){
            edge.setFrom(room.getId()+"");
            edge.setTo(parentRoom.getId()+"");
            edge.setArrows("to");
            parentRoom.setArrows("to");
            parentRoom.setFrom(room.getId()+"");
            parentRoom.setTo(parentRoom.getId()+"");
            room.setArrows("to");
            room.setTo(parentRoom.getId()+"");
            room.setFrom(room.getId()+"");
            if(parentRoom.getArrows().equals("to")){
                edge.setArrows("to,from");
                parentRoom.setArrows("to,from");
                room.setArrows("to,from");
            }
        }

        return parentRoom;
    }
    public String getMapOption(){
        List<RoomModel> rooms = db.init().createSQL("select * from t_map_room").list(RoomModel.class);
        List<Map<String,String>> list = new ArrayList<>();
        for (RoomModel room:rooms){
            HashMap o =  new HashMap<>();
            o.put("id",room.getId());
            o.put("name",room.getName());
            list.add(o);
        }
        return GameUtil.objectArr2JsonStr(list);
    }
}


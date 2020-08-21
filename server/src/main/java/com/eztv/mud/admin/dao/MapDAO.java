package com.eztv.mud.admin.dao;


public interface MapDAO {
    String getMapList(int page, int limit);
    String getMapListByWhere(String value,int page, int limit);
    boolean delMap(String id);
    boolean createMap();
    boolean saveMap(String id,String name);
}

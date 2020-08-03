package com.eztv.mud.admin.dao;


public interface ItemDAO {
    String getItemList(int page, int limit);
    String getItemListByWhere(String value,int page, int limit);
    boolean delItem(String id);
    boolean createItem();
    boolean saveItem(String id,String name,String script);
}

package com.ez.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BSql {

    /**
     * 从字段开头 模糊查询
     */
    public static String LIKE_BEGIN = "begin";
    /**
     * 从字段结尾 模糊查询
     */
    public static String LIKE_END = "end";
    /**
     * 从所有字段 模糊查询
     */
    public static String LIKE_ALL = "all";

    private static SQLiteDatabase _db;


    public static void create(File f) {
        _db = SQLiteDatabase.openOrCreateDatabase(f, null);
    }

    //创建工具类
    public static void create(SQLiteOpenHelper f) {
        _db = f.getWritableDatabase();
    }

    public static boolean createTable(String table, HashMap<String, String> values, boolean autokey) {
        _db.beginTransaction();
        try {
            String order = "CREATE TABLE IF NOT EXISTS " + table;
            String keyStr = "";
            Iterator iter = values.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = values.get(key);
                keyStr = keyStr + (keyStr.length() > 0 ? "," : "") + key + " " + val;
            }
            if (autokey == true) {
                order = order + "(" + "uid INTEGER PRIMARY KEY AUTOINCREMENT," + keyStr + ")";
            } else {
                order = order + "(" + keyStr + ")";
            }
            _db.execSQL(order);
            _db.setTransactionSuccessful();
        } catch (Exception e) {
            BDebug.trace("sql createTable Error: ", e.getMessage());
            return false;
        } finally {
            _db.endTransaction();
        }

        return true;
    }

    public static boolean createData(String table, HashMap<String, String> values) {
        _db.beginTransaction();
        try {
            String order = "insert into " + table;
            String keyStr = "";
            String valStr = "";
            Iterator iter = values.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = values.get(key);
                keyStr = keyStr + (keyStr.length() > 0 ? "," : "") + key;
                valStr = valStr + (valStr.length() > 0 ? "," : "") + "'" + val + "'";
            }
            order = order + "(" + keyStr + ") values " + "(" + valStr + ")";
            _db.execSQL(order);
            _db.setTransactionSuccessful();
        } catch (Exception e) {
            BDebug.trace("sql createData Error: ", e.getMessage());
            return false;
        } finally {
            _db.endTransaction();
        }
        return true;
    }

    public static boolean removeData(String table, HashMap<String, String> condition, boolean isAnd) {
        _db.beginTransaction();
        try {
            String addStr = isAnd == true ? " and " : " or ";
            String order = "delete from " + table;
            String keyStr = "";
            if (condition != null) {
                order = order + " where ";
                Iterator iter = condition.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    Object val = condition.get(key);
                    if (val.toString().contains(".")) {//多表联查
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + val;
                    } else {
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + "'" + val + "'";
                    }
                }
                order = order + keyStr;
            }
            _db.execSQL(order);
            _db.setTransactionSuccessful();
        } catch (Exception e) {
            BDebug.trace("sql removeData Error: ", e.getMessage());
            return false;
        } finally {
            _db.endTransaction();
        }

        return true;
    }

    public static boolean update(String table, HashMap<String, String> values, HashMap<String, String> condition, boolean isAnd) {
        _db.beginTransaction();
        try {
            String order = "update " + table + " set ";
            String keyStr = "";
            Iterator iter = values.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                Object val = values.get(key);
                keyStr = keyStr + (keyStr.length() > 0 ? "," : "") + key + "=" + "'" + val + "'";
            }
            order = order + keyStr;

            keyStr = "";
            String addStr = isAnd == true ? " and " : " or ";
            if (condition != null) {
                order = order + " where ";
                iter = condition.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    Object val = condition.get(key);
                    if (val.toString().contains(".")) {//多表联查
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + val;
                    } else {
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + "'" + val + "'";
                    }
                }
                order = order + keyStr;
            }
            _db.execSQL(order);
            _db.setTransactionSuccessful();
        } catch (Exception e) {
            BDebug.trace("sql update Error: ", e.getMessage());
            return false;
        } finally {
            _db.endTransaction();
        }
        return true;
    }

    public static List<Map<String, String>> query(String table, HashMap<String, String> condition, boolean isAnd, String like, String type) {
        List<Map<String, String>> list = null;

        try {
            String addStr = isAnd == true ? " and " : " or ";
            String order = "select * from " + table;
            String keyStr = "";
            if (condition != null) {
                order = order + " where ";
                Iterator iter = condition.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    Object val = condition.get(key);
                    if (val.toString().contains(".")) {//多表联查
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + val;
                    } else {
                        keyStr = keyStr + (keyStr.length() > 0 ? addStr : "") + key + "=" + "'" + val + "'";
                    }
                }
                order = order + keyStr;
            }

            if (like != null) {
                order = order + " like '";
                if (type.equals(BSql.LIKE_BEGIN)) {
                    order = order + like + "%";
                } else if (type.equals(BSql.LIKE_END)) {
                    order = order + "%" + like;
                } else if (type.equals(BSql.LIKE_ALL)) {
                    order = order + "%" + like + "%";
                }
                order = order + "'";
            }

            Cursor cursor = _db.rawQuery(order, null);
            //BDebug.trace(BSql.class ,cursor.getCount() ,cursor.getColumnCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                list = new ArrayList<Map<String, String>>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int j = 0; j < cursor.getColumnCount(); j++) {
                        String key = cursor.getColumnName(j);
                        String val = cursor.getString(j);
                        map.put(key, val);
                    }
                    list.add(map);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            BDebug.trace("sql query Error: ", e.getMessage());
        }
        return list;
    }

    public static List<Map<String, String>> query(String sql) {
        List<Map<String, String>> list = null;
        try {
            Cursor cursor = _db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                list = new ArrayList<Map<String, String>>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int j = 0; j < cursor.getColumnCount(); j++) {
                        String key = cursor.getColumnName(j);
                        String val = cursor.getString(j);
                        map.put(key, val);
                    }
                    list.add(map);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            BDebug.trace("sql query Error: ", e.getMessage());
        }
        return list;
    }

    public static boolean executeSql(String sql) {
        _db.beginTransaction();
        try {
            _db.execSQL(sql);
            _db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            BDebug.trace("sql update Error: ", e.getMessage());
            return false;
        } finally {
            _db.endTransaction();
        }
    }
}

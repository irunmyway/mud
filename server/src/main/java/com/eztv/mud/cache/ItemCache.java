package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Item;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.Item_PATH;

public class ItemCache {
    private static HashMap<String, LuaValue> itemScript = new HashMap<String, LuaValue>();
    private static List<Item> items;

    public static void initItem(Globals globals) {//初始化游戏物品
        itemScript.clear();
        items = DataBase.getInstance().init().createSQL("select * from t_item").list(Item.class);
        for (Item item : items) {
            try {
                if (item.getScript().length() > 0) {//模板
                    item.setScript(Item_PATH + item.getScript());
                    itemScript.put(item.getId() + "", globals.loadfile(item.getScript() + ".lua"));
                }
            } catch (Exception e) {e.printStackTrace();}
        }
        BDebug.trace("游戏物品加载完成 数量 : Items load item_num:【" + items.size() + "】");
    }

    public static List<Item> getItems() {
        return items;
    }
}

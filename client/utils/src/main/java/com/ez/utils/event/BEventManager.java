package com.ez.utils.event;

import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BEventManager {

    private static List<BEventItemVO> _items = new ArrayList<>();

    public static void addListener(String eventType, BListener listener) {

        if (listener == null) return;

        for (int i = 0; i < _items.size(); i++) {
            BEventItemVO vo = _items.get(i);
            if (vo.event.equals(listener)) {
                if (vo.types.contains(eventType) == false) {
                    vo.types.add(eventType);
                }
                return;
            }
        }

        BEventItemVO vo = new BEventItemVO(listener);
        vo.types.add(eventType);
        _items.add(vo);
    }


    public static void removeListener(String eventType, BListener listener) {
        if (listener == null) return;

        for (int i = 0; i < _items.size(); i++) {
            BEventItemVO vo = _items.get(i);

            if (vo.event.equals(listener)) {
                vo.types.remove(eventType);
                if (vo.types.size() <= 0) {
                    _items.remove(vo);
                }
                //break;
            }
        }

    }

    public static void dispathEvent(String eventType, BListener listener) {
        dispathEvent(eventType, null, listener);
    }

    public static void dispathEvent(String eventType, Object data, BListener listener) {
        for (int i = 0; i < _items.size(); i++) {
            BEventItemVO vo = _items.get(i);
            if (vo.event.equals(listener)) {
                if (vo.types.contains(eventType)) {
                    Map m = new HashMap();
                    m.put("eventType", eventType);
                    m.put("data", data);
                    Message msg = vo.getHandler().obtainMessage(0, m);
                    vo.getHandler().sendMessage(msg);
                }

                return;
            }
        }
    }
}

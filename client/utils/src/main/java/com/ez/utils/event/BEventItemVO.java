package com.ez.utils.event;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class BEventItemVO {

    private Handler _handler;

    public BListener event;
    public List<String> types = new ArrayList<>();

    public BEventItemVO(BListener event) {
        this.event = event;
    }

    public Handler getHandler() {
        if (_handler == null) {
            _handler = onHandler;
        }
        return _handler;
    }

    private Handler onHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map m = (Map) msg.obj;
            String eventType = (String) m.get("eventType");
            Object data = m.get("data");
            event.main(eventType, data);
        }
    };
}

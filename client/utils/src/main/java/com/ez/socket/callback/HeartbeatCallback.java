package com.ez.socket.callback;

import java.io.DataOutputStream;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-06-26 9:44
 * 功能: 心跳基础监听
 **/
public interface HeartbeatCallback {
    //心跳结束
    void onStopHeartbeat();

    //准备发送的心跳数据预处理格式定义
    void onPreSend(DataOutputStream out);
}

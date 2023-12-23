package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;

/**
 * @Author 李林麒
 * @Date 2023/12/22
 * @Description 心跳ping包
 */
public class PingMsg extends Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.PingMsg;
    }

    @Override
    public void executeIt() {

    }
}

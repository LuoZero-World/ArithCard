package com.llq.message.resp;

import com.llq.message.MessageType;

/**
 * @Author 李林麒
 * @Date 2023/12/22
 * @Description 心跳pong包
 */
public class PongMsg extends AbstructRespMessage{

    @Override
    public MessageType getMessageType() {
        return MessageType.PongMsg;
    }

    @Override
    public void executeIt() {

    }
}

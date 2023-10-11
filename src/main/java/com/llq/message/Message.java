package com.llq.message;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * @author 李林麒
 * @date 2023/1/14 20:19
 * @Description 消息抽象类
 */
public abstract class Message implements Serializable {

    abstract public MessageType getMessageType();

    public MessageType getReqMessageType(){
        return getMessageType();
    }
    /**
     * 业务处理
     */
    abstract public void executeIt();

    //预留接口
    public void writeBody(ByteBuf buf){}

    public void readBody(ByteBuf buf){}
}

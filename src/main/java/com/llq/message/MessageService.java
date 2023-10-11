package com.llq.message;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public enum MessageService {
    INSTANCE;

    public void execMessage(Message message){
        if(message == null) {
            log.debug("执行的消息体为空");
            return;
        }

        try {
            Method m = message.getClass().getMethod("executeIt");
            m.invoke(message, null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Message createNewMessage(int type){
        Class<? extends Message> clazz = MessageType.getMessageClassBy(type);
        if(clazz == null){
            throw new IllegalPacketException("类型为"+type+"的消息定义不存在");
        }

        Message msg = null;
        try {
            msg = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return msg;
    }

}

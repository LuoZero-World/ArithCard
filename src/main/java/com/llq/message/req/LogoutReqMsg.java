package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;

/**
 * @author 李林麒
 * @date 2023/1/20 19:39
 * @Description 登出消息
 */
public class LogoutReqMsg extends Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.logoutReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

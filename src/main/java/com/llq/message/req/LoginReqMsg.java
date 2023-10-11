package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/15 15:38
 * @Description 登录消息体
 */
@Data
@AllArgsConstructor
public class LoginReqMsg extends Message{

    String username;
    String password;

    @Override
    public MessageType getMessageType() {
        return MessageType.LoginReqMsg;
    }

    @Override
    public void executeIt() {

    }

}

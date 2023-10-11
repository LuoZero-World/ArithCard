package com.llq.message.resp;

import com.llq.message.MessageType;
import com.llq.message.manager.LoginManager;

/**
 * @author 李林麒
 * @date 2023/1/15 15:54
 * @Description 登录响应
 */
public class LoginRespMsg extends AbstructRespMessage{

    public LoginRespMsg(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LoginRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.LoginReqMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("执行登录操作");
        LoginManager.LOGIN_MANAGER.handlerLoginResp(this);
    }
}

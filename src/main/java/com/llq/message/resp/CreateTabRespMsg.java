package com.llq.message.resp;

import com.llq.message.MessageType;
import com.llq.message.manager.TableOpManager;

/**
 * @author 李林麒
 * @date 2023/1/25 11:43
 * @Description 对局创建响应
 */
public class CreateTabRespMsg extends AbstructRespMessage{

    public CreateTabRespMsg(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CreateTabRespMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("对局创建中");
        TableOpManager.TABLE_OP_MANAGER.createTable(this);
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.CreateTabReqMsg;
    }
}

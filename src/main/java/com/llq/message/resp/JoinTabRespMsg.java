package com.llq.message.resp;

import com.llq.entity.PlayerInfo;
import com.llq.message.MessageType;
import com.llq.message.manager.TableOpManager;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/27 11:41
 * @Description 加入对局响应
 */
@Data
public class JoinTabRespMsg extends AbstructRespMessage{

    private PlayerInfo masterInfo, challengeInfo;

    public JoinTabRespMsg(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.JoinTabRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return  MessageType.JoinTabReqMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("有人加入对局");
        TableOpManager.TABLE_OP_MANAGER.completeTable(this);
    }
}

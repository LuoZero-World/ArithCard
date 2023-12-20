package com.llq.message.resp;

import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;

/**
 * @author 李林麒
 * @date 2023/1/29 17:26
 * @Description 回合开始响应
 */
public class RoundStartRespMsg extends AbstructRespMessage{

    public RoundStartRespMsg(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.RoundStartRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.RoundStartReqMsg;
    }

    @Override
    public void executeIt() {
        GameManager.GAME_MANAGER.moveCard();
    }
}

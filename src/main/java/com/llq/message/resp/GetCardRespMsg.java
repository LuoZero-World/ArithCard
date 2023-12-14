package com.llq.message.resp;

import com.llq.entity.Card;
import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/27 22:54
 * @Description 获取卡牌响应
 */
@Data
public class GetCardRespMsg extends AbstructRespMessage{

    private Card card;

    public GetCardRespMsg(boolean success, String reason, Card card) {
        super(success, reason);
        this.card = card;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GetCardRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.GetCardReqMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("获取卡牌");
        GameManager.GAME_MANAGER.getCard(this);
    }
}

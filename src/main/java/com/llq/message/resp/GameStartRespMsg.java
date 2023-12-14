package com.llq.message.resp;

import com.llq.entity.PlayerInfo;
import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/29 19:53
 * @Description 游戏开始响应
 */
@Data
public class GameStartRespMsg extends AbstructRespMessage{

    private PlayerInfo masterInfo, challengeInfo;

    @Override
    public MessageType getMessageType() {
        return MessageType.GameStartRespMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("游戏开始了");
        GameManager.GAME_MANAGER.startGame(this);
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.GameStartReqMsg;
    }
}

package com.llq.message.resp;

import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/2/22 18:38
 * @Description 回合结束响应
 */
@Data
public class RoundEndRespMsg extends AbstructRespMessage{

    private boolean gameOver;

    public RoundEndRespMsg(boolean gameOver){
        super(true, "");
        this.gameOver = gameOver;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.RoundEndRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.RoundEndReqMsg;
    }

    @Override
    public void executeIt() {
        if(gameOver) {
            System.out.println("游戏结束");
            GameManager.GAME_MANAGER.endGame();
        }
        else {
            GameManager.GAME_MANAGER.endMyRound();
        }
    }
}

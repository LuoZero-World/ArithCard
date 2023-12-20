package com.llq.message.resp;

import com.llq.message.Message;
import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/12/20 19:00
 * @Description 用于同步游戏中玩家和对手的战斗信息，显示于界面上
 */
@Data
@AllArgsConstructor
public class StateChangeMsg extends Message {

    //代表自己和对手
    int blood1, blood2;
    boolean defend1, attack1;
    boolean defend2, attack2;

    @Override
    public MessageType getMessageType() {
        return MessageType.StateChangeMsg;
    }

    @Override
    public void executeIt() {
        GameManager.GAME_MANAGER.changeState(this);
    }
}

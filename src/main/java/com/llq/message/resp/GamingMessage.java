package com.llq.message.resp;

import com.llq.message.Message;
import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2022/11/26 20:50
 * @Description 游戏状态消息
 */
@Data
@AllArgsConstructor
public class GamingMessage extends Message {

    int idx;
    String content;

    @Override
    public MessageType getMessageType() {
        return MessageType.GamingMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("显示信息");
        GameManager.GAME_MANAGER.showMessage(content, idx);
    }
}

package com.llq.message.resp;

import com.llq.message.Message;
import com.llq.message.MessageType;
import com.llq.message.manager.GameManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author 李林麒
 * @date 2022/11/26 20:50
 * @Description 游戏状态消息
 */
@Data
@AllArgsConstructor
public class GamingMessage extends Message {

    List<String> content;

    @Override
    public MessageType getMessageType() {
        return MessageType.GamingMsg;
    }

    @Override
    public void executeIt() {
        GameManager.GAME_MANAGER.showMessage(content);
    }
}

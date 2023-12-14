package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/29 19:53
 * @Description 游戏开始请求
 */
@Data
@AllArgsConstructor
public class GameStartReqMsg extends Message {

    private String tableName;

    @Override
    public MessageType getMessageType() {
        return MessageType.GameStartReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

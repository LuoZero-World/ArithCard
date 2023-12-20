package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/29 17:25
 * @Description 回合开始请求
 */
@Data
@AllArgsConstructor
public class RoundStartReqMsg extends Message {

    private String tableName;

    @Override
    public MessageType getMessageType() {
        return MessageType.RoundStartReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/27 11:41
 * @Description 加入对局请求
 */
@Data
@AllArgsConstructor
public class JoinTabReqMsg extends Message {

    private String tableName;
    private String challengeName;

    @Override
    public MessageType getMessageType() {
        return MessageType.JoinTabReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

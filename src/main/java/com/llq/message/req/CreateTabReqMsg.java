package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/25 11:42
 * @Description 对局创建请求
 */
@Data
@AllArgsConstructor
public class CreateTabReqMsg extends Message {

    private String tableName;
    private String masterName;
    private int maxWatchers;

    @Override
    public MessageType getMessageType() {
        return MessageType.CreateTabReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

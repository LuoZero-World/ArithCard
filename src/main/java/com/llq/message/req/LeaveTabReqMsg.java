package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/26 21:57
 * @Description 对局离开请求(不需要响应)
 */
@Data
@AllArgsConstructor
public class LeaveTabReqMsg extends Message {

    String tableName;
    boolean master;

    @Override
    public MessageType getMessageType() {
        return MessageType.LeaveTabReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

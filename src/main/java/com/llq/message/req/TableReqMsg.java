package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/19 11:39
 * @Description 对局情况请求
 */
@Data
@AllArgsConstructor
public class TableReqMsg extends Message {

    boolean query;
    String tableName;

    @Override
    public MessageType getMessageType() {
        return MessageType.TableReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

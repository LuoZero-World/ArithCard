package com.llq.message.req;

import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/27 22:53
 * @Description 获取手牌请求
 */
@Data
@AllArgsConstructor
public class GetCardReqMsg extends Message {

    private int flag;           //1-数字牌 2-运算牌
    private boolean master;     //是主是客
    private String tableName;

    @Override
    public MessageType getMessageType() {
        return MessageType.GetCardReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

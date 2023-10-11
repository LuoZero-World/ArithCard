package com.llq.message.req;

import com.llq.entity.PlayerInfo;
import com.llq.message.Message;
import com.llq.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/2/22 18:37
 * @Description 回合结束请求
 */
@Data
@AllArgsConstructor
public class RoundEndReqMsg extends Message {

    private String tableName;
    private boolean master;
    private PlayerInfo playerInfo;

    @Override
    public MessageType getMessageType() {
        return MessageType.RoundEndReqMsg;
    }

    @Override
    public void executeIt() {

    }
}

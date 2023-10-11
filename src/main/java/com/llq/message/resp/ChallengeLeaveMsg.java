package com.llq.message.resp;

import com.llq.message.Message;
import com.llq.message.MessageType;
import com.llq.message.manager.TableOpManager;

/**
 * @author 李林麒
 * @date 2023/2/23 17:40
 * @Description 挑战者离开的响应
 */
public class ChallengeLeaveMsg extends Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.ChallengeLeaveMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("挑战者离开");
        TableOpManager.TABLE_OP_MANAGER.challengeLeave();
    }
}

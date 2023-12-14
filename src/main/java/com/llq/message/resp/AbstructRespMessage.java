package com.llq.message.resp;

import com.llq.message.Message;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/1/14 20:28
 * @Description 抽象响应类
 */
@Data
public abstract class AbstructRespMessage extends Message {

    protected boolean success;
    protected String reason;

    public AbstructRespMessage(){}

    public AbstructRespMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}

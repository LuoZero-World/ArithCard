package com.llq.message.resp;

import com.llq.entity.TabInfo;
import com.llq.message.MessageType;
import com.llq.message.manager.TableOpManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 李林麒
 * @date 2023/1/19 11:41
 * @Description 对局情况响应
 */
public class TableRespMsg extends AbstructRespMessage{

    @Getter @Setter
    List<TabInfo> tabInfos;
    int forWhich;       //1-第一个ListView     2-第二个ListView

    public TableRespMsg(boolean success, String reason, int which) {
        super(success, reason);
        forWhich = which;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TableRespMsg;
    }

    @Override
    public MessageType getReqMessageType() {
        return MessageType.TableReqMsg;
    }

    @Override
    public void executeIt() {
        System.out.println("对局刷新");
        if(forWhich == 1)
            TableOpManager.TABLE_OP_MANAGER.flushTable(this);
        else TableOpManager.TABLE_OP_MANAGER.flushTable2(this);
    }

}

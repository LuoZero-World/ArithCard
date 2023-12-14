package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.entity.TabInfo;
import com.llq.message.req.TableReqMsg;
import com.llq.message.resp.TableRespMsg;
import com.llq.server.service.CardTableService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 李林麒
 * @date 2023/1/19 14:51
 * @Description 对局情况响应处理
 */
@Slf4j
@ChannelHandler.Sharable
public class TableRespHandler extends SimpleChannelInboundHandler<TableReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TableReqMsg tableReqMsg) throws Exception {
        List<TabInfo> tabInfos = CardTableService.INSTANCE.getTableMap().values()
                .stream().map(CardTable::writeBody).collect(Collectors.toList());

        TableRespMsg msg = null;
        if(tableReqMsg.isQuery()){      //模糊查询
            String tabName = tableReqMsg.getTableName();
            List<TabInfo> tabInfos2 = tabInfos.stream().filter(tabInfo -> {
                if (tabInfo.getTableName().contains(tabName)) return true;
                return false;
            }).toList();

            if(tabInfos2.size() == 0)
                msg = new TableRespMsg(false, "当前暂无对局", 2);
            else msg = new TableRespMsg(true, "", 2);

            msg.setTabInfos(tabInfos2);
        } else{     //所有对战桌
            if(tabInfos.size() == 0)
                msg = new TableRespMsg(false, "当前暂无对局", 1);
            else msg = new TableRespMsg(true, "", 1);
            msg.setTabInfos(tabInfos);
        }
        ctx.writeAndFlush(msg);

        ReferenceCountUtil.release(tableReqMsg);
    }
}

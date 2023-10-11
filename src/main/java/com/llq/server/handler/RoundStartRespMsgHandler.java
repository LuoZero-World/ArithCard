package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.message.req.RoundStartReqMsg;
import com.llq.message.resp.RoundStartRespMsg;
import com.llq.server.service.CardTableService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 李林麒
 * @date 2023/1/29 18:13
 * @Description 回合开始响应处理
 */
@ChannelHandler.Sharable
public class RoundStartRespMsgHandler extends SimpleChannelInboundHandler<RoundStartReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoundStartReqMsg roundStartReqMsg) throws Exception {
        String tableName = roundStartReqMsg.getTableName();
        CardTable cardtable = CardTableService.INSTANCE.getTableBy(tableName);
        int idx = roundStartReqMsg.isMaster() ? 0 : 1;
        //给当前玩家返回回合开始响应
        if(CardTable.idxArr[cardtable.getIdx()] == idx){
            ctx.writeAndFlush(new RoundStartRespMsg(true, ""));
        }
        ReferenceCountUtil.release(roundStartReqMsg);
    }
}

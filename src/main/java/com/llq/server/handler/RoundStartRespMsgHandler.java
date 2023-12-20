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
 * @Description 回合开始响应处理 只由master发送回合开始请求
 */
@ChannelHandler.Sharable
public class RoundStartRespMsgHandler extends SimpleChannelInboundHandler<RoundStartReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoundStartReqMsg roundStartReqMsg) throws Exception {
        String tableName = roundStartReqMsg.getTableName();
        CardTable cardtable = CardTableService.INSTANCE.getTableBy(tableName);

        //给master玩家发送回合开始响应
        ctx.writeAndFlush(new RoundStartRespMsg(true, ""));

        ReferenceCountUtil.release(roundStartReqMsg);
    }
}

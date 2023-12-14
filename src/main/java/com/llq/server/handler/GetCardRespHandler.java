package com.llq.server.handler;

import com.llq.entity.Card;
import com.llq.entity.CardTable;
import com.llq.message.req.GetCardReqMsg;
import com.llq.message.resp.GetCardRespMsg;
import com.llq.server.service.CardTableService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 李林麒
 * @date 2023/1/30 18:30
 * @Description 获取卡牌处理
 */
@ChannelHandler.Sharable
public class GetCardRespHandler extends SimpleChannelInboundHandler<GetCardReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetCardReqMsg getCardReqMsg) throws Exception {
        int flag = getCardReqMsg.getFlag();
        boolean master = getCardReqMsg.isMaster();
        String tableName = getCardReqMsg.getTableName();

        CardTable cardtable = CardTableService.INSTANCE.getTableBy(tableName);
        Card card = cardtable.drawCard(master, flag);
        if(card != null){
            ctx.writeAndFlush(new GetCardRespMsg(true, "", card));
        }else{
            ctx.writeAndFlush(new GetCardRespMsg(false, "卡牌摸完了", null));
        }
    }
}

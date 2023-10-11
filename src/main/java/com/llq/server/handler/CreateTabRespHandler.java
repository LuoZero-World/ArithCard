package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.message.req.CreateTabReqMsg;
import com.llq.message.resp.CreateTabRespMsg;
import com.llq.server.service.CardTableService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 李林麒
 * @date 2023/1/25 11:53
 * @Description 创建对局处理
 */
@Slf4j
@ChannelHandler.Sharable
public class CreateTabRespHandler extends SimpleChannelInboundHandler<CreateTabReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CreateTabReqMsg msg) throws Exception {
        String tableName = msg.getTableName(), masName = msg.getMasterName();
        int maxWatchers = msg.getMaxWatchers();
        CardTable cardTable = CardTableService.INSTANCE.createCardTable(tableName, masName, maxWatchers);
        if(cardTable.isEmpty()){
            channelHandlerContext.writeAndFlush(new CreateTabRespMsg(false, "对局已存在"));
        } else{
            channelHandlerContext.writeAndFlush(new CreateTabRespMsg(true, ""));
        }
        ReferenceCountUtil.release(msg);
    }
}

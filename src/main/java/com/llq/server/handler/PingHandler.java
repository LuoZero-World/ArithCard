package com.llq.server.handler;

import com.llq.message.req.PingMsg;
import com.llq.server.service.ChannelBaseService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 李林麒
 * @Date 2023/12/22
 * @Description 处理ping包
 */
@Slf4j
@ChannelHandler.Sharable
public class PingHandler extends SimpleChannelInboundHandler<PingMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMsg pingMsg) throws Exception {

        if(ChannelBaseService.INSTANCE.getUser(ctx.channel()) == null) return;
        //ctx.writeAndFlush(new PongMsg());
       // ChannelBaseService.INSTANCE.putPingTime(ctx.channel(), DateUtil.date());
    }

}


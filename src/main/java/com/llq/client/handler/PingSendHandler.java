package com.llq.client.handler;

import com.llq.message.req.PingMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 李林麒
 * @Date 2023/12/22
 * @Description 每隔10s触发超时，发送ping包
 */
@Slf4j
@ChannelHandler.Sharable
public class PingSendHandler extends ChannelInboundHandlerAdapter {

    private int heartbeatCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE: handleReaderIdle(ctx); break;
                case WRITER_IDLE: handleWriterIdle(ctx); break;
                case ALL_IDLE: handleAllIdle(ctx); break;
                default: break;
            }
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
       log.debug("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        log.debug("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        log.debug("---ALL_IDLE---");
        sendPingMsg(ctx);
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        context.writeAndFlush(new PingMsg());
        heartbeatCount++;
        log.debug(" sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }
}

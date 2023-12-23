package com.llq.server.handler;

import com.llq.server.service.CardTableService;
import com.llq.server.service.ChannelBaseService;
import com.llq.server.service.UserService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 李林宸
 * @date 2022/11/25 8:42
 * @Description 客户端断开连接 处理
 */
@Slf4j
@ChannelHandler.Sharable
public class LogoutHandler extends ChannelInboundHandlerAdapter {

    //可以在这里处理Logout请求

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        UserService.INSTANCE.logout(ChannelBaseService.INSTANCE.getUser(ctx.channel()));
        log.info("{} 已断开", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String username = ChannelBaseService.INSTANCE.getUser(ctx.channel());
        CardTableService.INSTANCE.getCardTableBy(username).getRobot().setDisconnectedBy(username);
        UserService.INSTANCE.logout(username);
        log.error("{} 异常断开，异常原因 {}", ctx.channel(), cause.getMessage());
        ReferenceCountUtil.release(cause);
    }
}

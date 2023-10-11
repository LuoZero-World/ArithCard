package com.llq.server.handler;

import com.llq.message.req.LogoutReqMsg;
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof LogoutReqMsg){
            UserService.INSTANCE.logout(ChannelBaseService.INSTANCE.getUser(ctx.channel()));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //TODO 若创建对局、对局时出现异常 删除对局，告知对局另一方
        UserService.INSTANCE.logout(ChannelBaseService.INSTANCE.getUser(ctx.channel()));
        log.debug("{} 已断开", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.debug("{} 异常断开，异常原因 {}", ctx.channel(), cause.getMessage());
        cause.printStackTrace();
        ReferenceCountUtil.release(cause);
    }
}

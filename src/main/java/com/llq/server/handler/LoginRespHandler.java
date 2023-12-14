package com.llq.server.handler;

import com.llq.message.req.LoginReqMsg;
import com.llq.message.resp.LoginRespMsg;
import com.llq.server.service.UserService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 李林麒
 * @date 2023/1/15 15:37
 * @Description 登录响应处理
 */
@ChannelHandler.Sharable
public class LoginRespHandler extends SimpleChannelInboundHandler<LoginReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginReqMsg loginReqMsg) {
        String username = loginReqMsg.getUsername(), password = loginReqMsg.getPassword();
        System.out.println(username + password);
        if(UserService.INSTANCE.login(ctx.channel(), username, password)){
            LoginRespMsg loginRespMsg = new LoginRespMsg(true, "");
            ctx.writeAndFlush(loginRespMsg);
        } else{
            //TODO 完善reason
            LoginRespMsg loginRespMsg = new LoginRespMsg(false, "用户名或密码错误！");
            ctx.writeAndFlush(loginRespMsg);
        }
        ReferenceCountUtil.release(loginReqMsg);
    }
}

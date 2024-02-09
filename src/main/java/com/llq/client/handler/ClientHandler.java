package com.llq.client.handler;

import com.llq.message.Message;
import com.llq.message.MessageService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 李林麒
 * @date 2023/1/15 16:39
 * @Description 消息处理
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        //新起一个线程去执行操作
        new Thread(()->{
            MessageService.INSTANCE.execMessage(message);
            ReferenceCountUtil.release(msg);
        }).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
    }
}

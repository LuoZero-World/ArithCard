package com.llq.client;

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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CommunicationService.INSTANCE.registerSession(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;

        //int num = CommunicationService.PROMISES.getOrDefault(message.getReqMessageType(), 0);
        //if(message instanceof GamingMessage){
            //CommunicationService.PROMISES.put(message.getMessageType(), num-1);
//            if(message instanceof ChallengeLeaveMsg){
//                //挑战者离开，重置监听
//                CommunicationService.PROMISES.put(MessageType.JoinTabReqMsg, 1);
//            }
            //新起一个线程去执行操作
            new Thread(()->{
                MessageService.INSTANCE.execMessage(message);
                ReferenceCountUtil.release(msg);
            }).start();
        //}
    }
}

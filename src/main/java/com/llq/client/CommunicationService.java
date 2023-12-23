package com.llq.client;

import com.llq.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.Setter;

public enum CommunicationService {

    INSTANCE;
    //当前等待响应的消息集合
    //public static final Map<MessageType, Integer> PROMISES = new ConcurrentHashMap<>();
    @Setter
    private EventLoopGroup group;
    @Setter
    private Channel channel;

    public void sendMessage(Message request){
        channel.writeAndFlush(request);
    }
    public boolean isConnected() {
        return this.channel != null;
    }
    public void shutDown(){
        if(group != null) group.shutdownGracefully();
    }
}

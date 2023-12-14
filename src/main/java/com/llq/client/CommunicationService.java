package com.llq.client;

import com.llq.message.Message;
import io.netty.channel.Channel;

public enum CommunicationService {

    INSTANCE;
    //当前等待响应的消息集合
    //public static final Map<MessageType, Integer> PROMISES = new ConcurrentHashMap<>();

    private Channel channel;
    public void registerSession(Channel channel) {
        this.channel = channel;
    }

    public void sendMessage(Message request){

        channel.writeAndFlush(request);
    }

    public boolean isConnected() {
        return this.channel != null;
    }

    public void closeMyChannel(){
        if(channel != null) channel.close();
    }
}

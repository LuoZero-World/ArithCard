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
        //PROMISES.put(request.getMessageType(), PROMISES.getOrDefault(request.getMessageType(), 0)+1);

        //监听对手的事件
//        if(request instanceof CreateTabReqMsg){
//            PROMISES.put(MessageType.JoinTabReqMsg, 1);
//        }
//
//        if(request instanceof LeaveTabReqMsg){
//            PROMISES.put(MessageType.JoinTabReqMsg, 0);
//            PROMISES.put(MessageType.GameStartReqMsg, 1);
//        }
//
//        //挑战者加入，监听桌主事件
//        if(request instanceof JoinTabReqMsg){
//            PROMISES.put(MessageType.GameStartReqMsg, 1);
//        }

        channel.writeAndFlush(request);
    }

    public boolean isConnected() {
        return this.channel != null;
    }

    public void closeMyChannel(){
        if(channel != null) channel.close();
    }
}

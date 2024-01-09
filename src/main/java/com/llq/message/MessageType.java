package com.llq.message;

import com.llq.message.req.*;
import com.llq.message.resp.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum MessageType implements Serializable {

    LoginReqMsg(1_000, LoginReqMsg.class),
    LoginRespMsg(1_200,LoginRespMsg.class),

    logoutReqMsg(1_010, LogoutReqMsg.class),

    TableReqMsg(2_000, TableReqMsg.class),
    TableRespMsg(2_200, TableRespMsg.class),

    CreateTabReqMsg(2_010, CreateTabReqMsg.class),
    CreateTabRespMsg(2_210, CreateTabRespMsg.class),

    LeaveTabReqMsg(2_020, LeaveTabReqMsg.class),

    JoinTabReqMsg(3_000, JoinTabReqMsg.class),
    JoinTabRespMsg(3_200, JoinTabRespMsg.class),

    GetCardReqMsg(4_000, GetCardReqMsg.class),
    GetCardRespMsg(4_020, GetCardRespMsg.class),

    GameStartReqMsg(5_000, GameStartReqMsg.class),
    GameStartRespMsg(5_200, GameStartRespMsg.class),

    RoundStartReqMsg(5_010, RoundStartReqMsg.class),
    RoundStartRespMsg(5_210, RoundStartRespMsg.class),

    RoundEndReqMsg(6_000, RoundEndReqMsg.class),
    RoundEndRespMsg(6_010, RoundEndRespMsg.class),

    ChallengeLeaveMsg(7_000, ChallengeLeaveMsg.class),

    GamingMsg(20_000, GamingMessage.class),
    StateChangeMsg(20_100, StateChangeMsg.class),

    PingMsg(30_000, PingMsg.class),
    PongMsg(30_200, PongMsg.class),
    ;

    //req _000
    //resp _200
    @Getter
    @Setter
    private final int type;
    @Getter
    private final Class<? extends Message> messageClass;
    private static final Map<Integer, Class<? extends Message>> classMap = new ConcurrentHashMap<>();

    MessageType(int type, Class<? extends Message> messageClass) {
        this.type = type;
        this.messageClass = messageClass;
    }

    public static void initPackets() {
        Set<Integer> typeSet = new HashSet<>();
        Set<Class<?>> packets = new HashSet<>();
        for(MessageType m : MessageType.values()){
            if(classMap.containsKey(m.getType())) continue;
            classMap.put(m.getType(), m.getMessageClass());
        }
    }

    public static Class<? extends Message> getMessageClassBy(int type){
        return classMap.getOrDefault(type, null);
    }

}

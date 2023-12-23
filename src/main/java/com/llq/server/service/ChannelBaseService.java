package com.llq.server.service;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public enum ChannelBaseService {

    INSTANCE;

    private final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();


    public void bind(Channel channel, String username){
        userChannelMap.put(username, channel);
        channelUserMap.put(channel, username);
    }

    public void unbind(String username){
        if(username == null) return;
        Channel channel = userChannelMap.remove(username);
        channelUserMap.remove(channel);
    }

    public Channel getChannel(String username){
        return userChannelMap.getOrDefault(username, null);
    }

    public List<Channel> getChannels(Set<String> users){
        return userChannelMap.entrySet().stream()
                .filter(entry -> users.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public String getUser(Channel channel){
        return channelUserMap.getOrDefault(channel, null);
    }
}

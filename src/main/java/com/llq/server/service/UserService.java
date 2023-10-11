package com.llq.server.service;

import com.llq.entity.User;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum UserService {
    INSTANCE;

    private final Map<String, String> userPwdMap = new ConcurrentHashMap<>();
    private final Map<String, User> allUserMap = new ConcurrentHashMap<>();
    private final Set<String> loggedUser = new HashSet<>();
    final ChannelBaseService chService = ChannelBaseService.INSTANCE;
    {
        allUserMap.put("Jack", new User("Jack"));
        allUserMap.put("Tom", new User("Tom"));
        allUserMap.put("Lili", new User("Lili"));
        allUserMap.put("Pi", new User("Pi"));
        allUserMap.put("Admin", new User("Admin"));
    }
    {
        userPwdMap.put("Jack", "123");
        userPwdMap.put("Tom", "123");
        userPwdMap.put("Lili", "123");
        userPwdMap.put("Pi", "123");
        userPwdMap.put("Admin", "123456");
    }

    public boolean login(Channel channel, String username, String password) {
        String pass = userPwdMap.getOrDefault(username, null);
        if (pass == null) {
            log.debug(username+"用户名输入错误");
            return false;
        } else if(HasLogged(username)){
            log.debug(username+"重复登录");
            return false;
        } else if(pass.equals(password)){
            loggedUser.add(username);
            chService.bind(channel, username);
            return true;
        }
        return false;
    }

    public void logout(String username) {
        if(username == null) return;
        loggedUser.remove(username);
        chService.unbind(username);
    }

    public boolean HasLogged(String username){
        return loggedUser.contains(username);
    }

    public User getUserBy(String username){
        return allUserMap.getOrDefault(username, null);
    }
}

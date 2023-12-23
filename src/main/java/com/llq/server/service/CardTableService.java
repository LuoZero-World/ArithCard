package com.llq.server.service;

import com.llq.entity.CardTable;
import com.llq.entity.Player;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CardTableService {
    INSTANCE;

    @Getter
    private final Map<String, CardTable> tableMap = new ConcurrentHashMap<>();
    private final Map<String, CardTable> playerTableMap = new ConcurrentHashMap<>();

    public CardTable createCardTable(String tableName, String masterName, int maxWatchers){
        if(tableMap.containsKey(tableName)) return CardTable.EMPTY_TABLE;
        Player master = new Player(masterName, true);
        CardTable cardTable = new CardTable(tableName, master, maxWatchers);
        tableMap.put(tableName, cardTable);
        playerTableMap.put(masterName, cardTable);

        return cardTable;
    }

    public void deleteCardTable(String tableName){
        tableMap.remove(tableName);
    }

    public CardTable getTableBy(String tableName){
        return tableMap.getOrDefault(tableName, CardTable.EMPTY_TABLE);
    }

    public String getTableMasterName(String tableName){
        return getTableBy(tableName).getMaster().getPlayerInfo().getNickname();
    }

    public boolean joinTable(String tableName, String name){
        //对局名不存在
        if(!tableMap.containsKey(tableName)) return false;
        CardTable cardTable = tableMap.get(tableName);
        //对局正在进行，进入观战
        if(cardTable.isComplete()){
            //观战人数已满
            if(cardTable.isFullWatchers())
                return false;
            else
                cardTable.addWatcher(name);
        } else{     //加入对局对战
            playerTableMap.put(name, cardTable);
            Player challenge = new Player(name, false);
            cardTable.setChallenge(challenge);
        }
        return true;
    }

    public void leaveTable(String username){
        playerTableMap.remove(username);
    }

    public CardTable getCardTableBy(String username){
        return playerTableMap.getOrDefault(username, null);
    }
}

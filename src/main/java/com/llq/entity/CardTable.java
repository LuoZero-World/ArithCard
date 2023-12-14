package com.llq.entity;

import com.llq.message.resp.GamingMessage;
import com.llq.server.service.ChannelBaseService;
import com.llq.utility.PropertiesUtil;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.Getter;

import java.util.*;

/**
 * @author 李林麒
 * @date 2023/1/19 14:56
 * @Description 游戏对局
 */
@Data
public class CardTable {
    private String tableName;
    private Set<String> members;      //观战成员
    private int maxWatchers;
    private Player master, challenge;   //对战玩家
    public static final int[] idxArr = new int[]{0, 1, 1, 0};   //当前执行操作的玩家 0-master 1-challenge
    private int idx = 0;
    List<Card> DigitCard, OperCard;
    int Didx, Opidx;                //牌堆顶指针
    private boolean empty = false, complete = false, fullWatchers = false;

    DamageMaker dm;

    //空对局
    public static final CardTable EMPTY_TABLE = new CardTable(true, Collections.emptySet());

    public CardTable(String tableName, Player master, int maxWatchers) {
        this.tableName = tableName;
        this.master = master;
        this.maxWatchers = maxWatchers;
        this.members = new HashSet<>();
    }

    private CardTable(boolean b, Set<String> set){
        empty = b;
        members = set;
    }

    //转换当前正在执行操作的玩家
    public void turnPlayer(){
        idx = (idx+1) % 2;
    }

    public TabInfo writeBody(){
        TabInfo tabInfo = new TabInfo(tableName, master.getPlayerInfo().getNickname(), complete, maxWatchers);
        tabInfo.setWatchers(members.size());
        return tabInfo;
    }

    //当挑战者加入时进行加载
    public void load() {
        DigitCard = new ArrayList<>();
        OperCard = new ArrayList<>();
        dm = new DamageMaker(new int[]{1, 2, 4, 8});

        //加入1-13 数字牌*6
        for (int i = 0; i < 78; i++) {
            DigitCard.add(new Card(Card.CardType.DigitCard, String.valueOf(i % 13 + 1)));
        }
        //加入+ - 运算牌*6
        for (int i = 0; i < 6; i++) {
            OperCard.add(new Card(Card.CardType.OperationCard, "+"));
            OperCard.add(new Card(Card.CardType.OperationCard, "-"));
        }
        //加入* / % 运算牌*4
        for (int i = 0; i < 4; i++) {
            OperCard.add(new Card(Card.CardType.OperationCard, "*"));
            OperCard.add(new Card(Card.CardType.OperationCard, "/"));
            OperCard.add(new Card(Card.CardType.OperationCard, "%"));
        }
        //加入& | ^ 运算牌*3
        for (int i = 0; i < 3; i++) {
            OperCard.add(new Card(Card.CardType.OperationCard, "&"));
            OperCard.add(new Card(Card.CardType.OperationCard, "^"));
            OperCard.add(new Card(Card.CardType.OperationCard, "|"));
        }
        //加入 << >> 运算牌*2
        for (int i = 0; i < 2; i++) {
            OperCard.add(new Card(Card.CardType.OperationCard, ">>"));
            OperCard.add(new Card(Card.CardType.OperationCard, "<<"));
        }

        //玩家加载
        master.load();
        challenge.load();
        //打乱
        shuffle(1);
        shuffle(2);
        //先发牌，到时候询问起始牌时直接返回即可
        distribute();
        //设置牌指针
        Opidx = 8;
        Didx = 12;

        complete = true;
    }

    //牌堆重置
    void resetCard(int flag){
        if(flag == 1){
            Didx = 0;
            shuffle(1);
        } else if(flag == 2){
            Opidx = 0;
            shuffle(2);
        }
    }

    //flag-1 打乱数字牌      -2 打乱运算牌
    public void shuffle(int flag){
        Random rd = new Random(System.currentTimeMillis());
        if(flag == 1)
            Collections.shuffle(DigitCard, rd);
        else if(flag == 2)
            Collections.shuffle(OperCard, rd);
    }

    //分发起始手牌
    public void distribute(){
        List<Card> master_d = master.getPlayerInfo().getDigitList(), challenge_d = challenge.getPlayerInfo().getDigitList();
        List<Card> master_o = master.getPlayerInfo().getOperList(), challenge_o = challenge.getPlayerInfo().getOperList();
        //数字牌6张 运算牌4张
        for(int i = 0; i < 12; i++){
            if((i & 1) == 0)
                master_d.add(DigitCard.get(i));
            else challenge_d.add(DigitCard.get(i));
        }
        for(int i = 0; i < 8; i++){
            if((i & 1) == 0)
                master_o.add(OperCard.get(i));
            else challenge_o.add(OperCard.get(i));
        }
    }

    //摸牌
    public Card drawCard(boolean isMaster, int flag){
        Player player = isMaster ? master : challenge;
        Card card = null;

        //数字牌
        if(flag == 1){
            card = DigitCard.get(Didx++);
            player.getPlayerInfo().getDigitList().add(card);
            //数字牌已经摸完
            if(Didx >= 78) resetCard(1);
        } else if(flag == 2){
            card = OperCard.get(Opidx++);
            player.getPlayerInfo().getDigitList().add(card);
            //运算牌已经摸完
            if(Opidx >= 37) resetCard(2);
        }
        return card;
    }

    public void addWatcher(String name){
        members.add(name);
        if(members.size() == maxWatchers) fullWatchers =  true;
    }

    public boolean roundEndBattle(){
        int msgIdx = 1;
        sendGamingMessage("\n===该轮结束 系统执行判定===", msgIdx++);

        //固定伤害
        if (dm.round == 0) {
            dm.round = DamageMaker.Round;
            sendGamingMessage("对玩家进行固定伤害！", msgIdx++);
            doDamage(dm.fixedDamage[dm.idx], master, msgIdx);
            doDamage(dm.fixedDamage[dm.idx], challenge, msgIdx);
            dm.idx = (dm.idx+1) % dm.fixedDamage.length;
            if (isPlayerDie(msgIdx)) return true;
        }

        //玩家互殴
        if (master.attackIt()) {
            sendGamingMessage( master.getPlayerInfo().getNickname() + "发起攻击", msgIdx++);
            doDamage(1, challenge, msgIdx);
        }
        if (challenge.attackIt()) {
            sendGamingMessage(challenge.getPlayerInfo().getNickname() + "发起攻击", msgIdx++);
            doDamage(1, master, msgIdx++);
        }

        if (isPlayerDie(msgIdx)) return true;

        sendGamingMessage("无玩家阵亡", msgIdx++);
        sendGamingMessage("距离下一轮攻击还有" + dm.round + "回合", msgIdx++);
        dm.round--;
        return false;
    }
    //对玩家p 进行伤害为d的攻击
    void doDamage(int d, Player p, int msgIdx) {
        PlayerInfo playerInfo = p.getPlayerInfo();
        sendGamingMessage(playerInfo.getNickname() + ":", msgIdx++);
        if (!p.defendIt()) {
            sendGamingMessage(playerInfo.getNickname()+"未进行防御", msgIdx++);
            p.damage(d);
            sendGamingMessage(playerInfo.getNickname()+"受到" + d + "点伤害," + "当前血量剩余" + Math.max(0, playerInfo.getHP()), msgIdx++);
       } else sendGamingMessage("伤害抵挡成功", msgIdx++);
    }

    //检测是否有玩家死亡
    boolean isPlayerDie(int msgIdx) {
        PlayerInfo masterInfo = master.getPlayerInfo(), challengeInfo = challenge.getPlayerInfo();
        if (master.isDie() && challenge.isDie()) {
            sendGamingMessage("\n" + masterInfo.getNickname() + "和" + challengeInfo.getNickname() + "都已阵亡，平局！", msgIdx++);
            return true;
        } else if (challenge.isDie()) {
            sendGamingMessage("\n" + challengeInfo.getNickname() + "阵亡" + masterInfo.getNickname() + "取得胜利！", msgIdx++);
            return true;
        } else if (master.isDie()) {
            sendGamingMessage("\n" + masterInfo.getNickname() + "阵亡" + challengeInfo.getNickname() + "取得胜利！", msgIdx++);
            return true;
        }
        return false;
    }

    public void sendGamingMessage(String content, int msgIdx){
        Set<String> MContainsPlayer = new HashSet<>(members){{
            add(master.getPlayerInfo().getNickname());
            add(challenge.getPlayerInfo().getNickname());
        }};
        List<Channel> channelList = ChannelBaseService.INSTANCE.getChannels(MContainsPlayer);

        int msgIIdx = (dm.getRound()+1)*1000+msgIdx;
        channelList.forEach( c -> c.writeAndFlush(new GamingMessage(msgIIdx, content)));
    }

    class DamageMaker {

        int[] fixedDamage;  //固定伤害递增数组
        @Getter
        public int idx, round;     //下一次攻击剩余轮数

        static int Round = PropertiesUtil.getInt("damageRound");       //固定伤害间隔轮数

        public DamageMaker(int[] fixedDamage) {
            this.fixedDamage = fixedDamage;
            round = Round;
            idx = 0;
        }
    }
}

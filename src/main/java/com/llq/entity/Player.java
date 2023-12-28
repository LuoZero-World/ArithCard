package com.llq.entity;

import com.llq.utility.PropertiesUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 李林麒
 * @date 2022/11/1 20:32
 * @Description 玩家类
 */
@Data
@EqualsAndHashCode(of = "playerInfo")
public class Player implements Serializable {
    private PlayerInfo playerInfo;
    private boolean master;     //是否是桌主

    public Player(String name, boolean master) {
        this.master = master;
        playerInfo = new PlayerInfo();
        playerInfo.setNickname(name);
        //因为界面上需要HP的信息
        playerInfo.setHP(PropertiesUtil.getInt("HP"));
    }

    public void load() {
        playerInfo.setAttack(false);
        playerInfo.setDefend(false);
        playerInfo.setDigitList(new ArrayList<>());
        playerInfo.setOperList(new ArrayList<>());
    }

    public void damage(int d) {
        int tHP = playerInfo.getHP();
        playerInfo.setHP(Math.max(tHP-d, 0));
    }

    //返回值代表是否防御成功
    public boolean defendIt() {
        if (playerInfo.isDefend()) {
            playerInfo.setDefend(false);
            return true;
        } else return false;
    }

    //返回值代表发出攻击成功
    public boolean attackIt() {
        if (playerInfo.isAttack()) {
            playerInfo.setAttack(false);
            return true;
        } else return false;
    }

    public boolean isDie(){
        return playerInfo.getHP() <= 0;
    }

}


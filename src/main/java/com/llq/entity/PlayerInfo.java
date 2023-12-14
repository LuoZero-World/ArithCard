package com.llq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 李林麒
 * @date 2023/1/19 15:52
 * @Description 玩家信息类，用于传输，避免直接传输Player带来的大量开销
 */
@Data
@EqualsAndHashCode(of="nickname")
public class PlayerInfo implements Serializable {
    private String nickname;
    private int HP;
    private boolean attack, defend;     //是否可攻击、防御
    private List<Card> DigitList;       //数字牌
    private List<Card> OperList;        //运算牌

    public PlayerInfo(){}
}

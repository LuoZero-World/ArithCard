package com.llq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 李林麒
 * @date 2022/11/1 20:32
 * @Description 数字牌、运算牌
 */
@Data
@EqualsAndHashCode(of="content")
public class Card implements Serializable {

    public enum CardType {
        DigitCard, OperationCard
    }

    private CardType type;      //类型
    private String content;     //内容

    public Card(CardType type, String content) {
        this.type = type;
        this.content = content;
    }

    public Card(String content) {
        this.content = content;
        type = CardType.DigitCard;
    }
}

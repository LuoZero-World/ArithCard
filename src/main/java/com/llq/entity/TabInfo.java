package com.llq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 李林麒
 * @date 2023/1/19 15:56
 * @Description 对局消息类
 */
@Data
@EqualsAndHashCode(of = "tableName")
public class TabInfo implements Serializable {
    private String tableName;
    private String master, challenger;
    private boolean complete;
    private int watchers, max_watchers;

    public TabInfo(String tableName, String master, boolean complete, int max_watchers) {
        this.tableName = tableName;
        this.master = master;
        this.complete = complete;
        this.max_watchers = max_watchers;
    }
}

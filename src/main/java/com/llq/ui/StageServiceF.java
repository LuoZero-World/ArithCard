package com.llq.ui;

import com.llq.entity.PlayerInfo;
import com.llq.ui.controller.Controller;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public interface StageServiceF {

    Map<String, Stage> stages = new HashMap<>();
    Map<String, Controller> controllers = new HashMap<>();

    /**
     * 添加窗口
     *
     * @param name 窗口名称
     * @param stage 窗口
     */
    void addStage(String name, Stage stage);

    /**
     * 获取窗口
     *
     * @param name 窗口名称
     */
    default Stage getStageBy(String name){
        return stages.getOrDefault(name, null);
    }

    /**
     * 设置初始窗口
     */
    void setPrimaryStage(String name, Stage stage);

    /**
     * 加载窗口
     *
     * @param name      窗口名称
     * @param resource    url
     * @param styles    窗口样式
     */
    Stage loadStage(String name, String resource, StageStyle... styles);

    /**
     * 显示窗口,且窗口可拖动
     *
     * @param name 窗口名称
     */
    Stage setStage(String name);

    /**
     * 切换窗口
     *
     * toClose -> toShow
     */
    boolean switchStage(String toShow, String toClose);

    /**
     * 关闭窗口
     *
     * @param name 窗口名称
     */
    boolean closeStage(String name);

    //加载己方的对局界面
    void loadBattleStage();

    //加载敌手的对局界面
    void loadBattleStageAgain(PlayerInfo playerInfo);

    default public Controller getControllerBy(String name) {
        return this.controllers.getOrDefault(name, null);
    }

}

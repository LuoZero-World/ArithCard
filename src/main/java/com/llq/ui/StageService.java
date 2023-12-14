package com.llq.ui;

import com.llq.entity.PlayerInfo;
import com.llq.ui.controller.Controller;
import com.llq.ui.event.DragWindowHandler;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author 李林麒
 * @date 2023/1/16 16:41
 * @Description 提供与窗口操作相关的接口
 */
@Slf4j
public enum StageService implements StageServiceF{

    INSTANCE;

    @Override
    public void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }

    @Override
    public void setPrimaryStage(String name, Stage stage) {}

    @Override
    public Stage loadStage(String name, String resource, StageStyle... styles) {
        Stage res = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Scene scene = new Scene(loader.load());
            res = new Stage();
            res.setScene(scene);
            Controller controller = loader.getController();
            controllers.put(name, controller);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(StageStyle style : styles){
            res.initStyle(style);
        }
        addStage(name, res);
        return res;
    }

    @Override
    public Stage setStage(String name) {
        Stage stage = getStageBy(name);
        if (stage == null) {
            return null;
        }
        /**拖动*/
        Scene scene = stage.getScene();
        if(scene.getOnMouseDragged() == null){
            EventHandler<MouseEvent> handler = new DragWindowHandler(stage);;
            scene.setOnMousePressed(handler);   //如果去掉这一行代码将会使鼠标进入面板时面板左上角会定位到鼠标的位置
            scene.setOnMouseDragged(handler);
            TabPane tabPane = (TabPane) getNodeById("TabPane", stage);
            if(tabPane != null){
                tabPane.setOnMousePressed(handler);
                tabPane.setOnMouseDragged(handler);
            }
        }
        stage.show();

        return stage;
    }

    @Override
    public boolean switchStage(String toShow, String toClose) {
        if(!closeStage(toClose)) return false;
        setStage(toShow);
        return true;
    }

    @Override
    public boolean closeStage(String name) {
        Stage oldStage = getStageBy(name);
        if(oldStage == null){
            log.debug(name+"不存在");
            return false;
        }
        oldStage.close();
        return true;
    }

    @Override
    public void loadBattleStage() {
        Stage battle = getStageBy(UIUrl.Name.BattleView);
        Label nameLabel = (Label) getNodeById("#namelabel", battle);
        Label bloodLabel = (Label) getNodeById("#bloodlabel2", battle);
        PlayerInfo playerInfo = NameForAll.player.getPlayerInfo();
        nameLabel.setText(playerInfo.getNickname());
        bloodLabel.setText("HP："+playerInfo.getHP()+"/"+playerInfo.getHP());
    }

    @Override
    public void loadBattleStageAgain(PlayerInfo playerInfo) {
        Stage battle = getStageBy(UIUrl.Name.BattleView);
        Label nameLabel = (Label) getNodeById("#cnamelabel", battle);
        Label bloodLabel = (Label) getNodeById("#cbloodlabel2", battle);
        nameLabel.setText(playerInfo.getNickname());
        bloodLabel.setText("HP："+playerInfo.getHP()+"/"+playerInfo.getHP());

        getNodeById("#ancpane1", battle).setVisible(true);
    }

    Node getNodeById(String id, Stage stage){
        return stage.getScene().getRoot().lookup(id);
    }
}

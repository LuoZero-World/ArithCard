package com.llq.ui.controller;

import com.llq.client.CommunicationService;
import com.llq.entity.TabInfo;
import com.llq.message.req.CreateTabReqMsg;
import com.llq.message.req.LogoutReqMsg;
import com.llq.message.req.TableReqMsg;
import com.llq.ui.StageService;
import com.llq.ui.TabInfoCell;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * @author 李林麒
 * @date 2023/1/18 11:02
 * @Description 控制管理大厅界面
 */
public class HallController implements Controller{

    @FXML
    private ImageView flushView;

    @FXML
    private ListView<TabInfo> tableList, tableList2;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label tipLabel, errLabel1, errLabel2;

    @FXML
    private TextField tabNameTxt, tabNameTxt2, tabMaxTxt;

    RotateTransition rotateTransition;

    @FXML
    void initialize(){
        initAnimation();
        initListView();
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                tipLabel.setVisible(false);
                if(newTab.getText().equals("游戏大厅")){
                    OnFlush();
                }
            }
        });
    }

    private void initListView() {
        tableList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TabInfo> call(ListView<TabInfo> tabInfoListView) {
                return new TabInfoCell();
            }
        });
        tableList2.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TabInfo> call(ListView<TabInfo> tabInfoListView) {
                return new TabInfoCell();
            }
        });
    }

    @FXML
    void OnLogout() {
        CommunicationService.INSTANCE.sendMessage(new LogoutReqMsg());
        StageService.INSTANCE.switchStage(UIUrl.Name.LoginView, UIUrl.Name.HallView);
    }

    @FXML
    void OnClose(MouseEvent event) {
        CommunicationService.INSTANCE.shutDown();
        Platform.exit();
    }

    @FXML
    void OnCreate(MouseEvent event) {
        errLabel1.setVisible(false);
        errLabel2.setVisible(false);

        String tableName = tabNameTxt.getText();
        String content = tabMaxTxt.getText();
        //桌名输入不合规
        if(tableName.length() < 3 || tableName.length() > 16) {
            errLabel1.setText("输入长度应在3-16");
            errLabel1.setVisible(true);
            return;
        }
        //表示验证通过
        if(content.matches("\\d{1,2}")){
            int maxWatchers = Integer.parseInt(content);
            NameForAll.mytable_name = tableName;
            CommunicationService.INSTANCE.sendMessage(new CreateTabReqMsg(tableName, NameForAll.user_name, maxWatchers));
        } else{
            errLabel2.setVisible(true);
        }
    }

    @FXML
    void OnFlush() {
        tipLabel.setVisible(false);
        rotateTransition.play();
        CommunicationService.INSTANCE.sendMessage(new TableReqMsg(false, ""));
    }

    @FXML
    void OnQuery(){
        tipLabel.setVisible(false);
        String tabName = tabNameTxt2.getText();
        //不合规
        if(tabName == null || tabName.length() == 0) return;
        CommunicationService.INSTANCE.sendMessage(new TableReqMsg(true, tabName));
    }

    @Override
    public Stage getMyStage() {
        return StageService.INSTANCE.getStageBy(UIUrl.Name.HallView);
    }

    void initAnimation(){

        double play_time;
        double fromAngle = 0.0, toAngle=360.0;

        //根据旋转角度大小计算动画播放持续时间
        play_time =Math.abs(toAngle-fromAngle)*0.005;
        //Duration.seconds(3)设置动画持续时间
        rotateTransition = new  RotateTransition(Duration.seconds(play_time), flushView);
        //设置旋转角度
        rotateTransition.setFromAngle(fromAngle);
        rotateTransition.setToAngle(toAngle);
        // 只播放一次
        rotateTransition.setCycleCount(1);
    }
}

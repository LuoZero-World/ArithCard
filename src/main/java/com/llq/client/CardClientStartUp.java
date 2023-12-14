package com.llq.client;

import com.llq.message.MessageType;
import com.llq.ui.StageService;
import com.llq.utility.UIUrl;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * @author 李林麒
 * @date 2023/1/16 16:05
 * @Description 客户端启动类，启动可视化界面
 */
public class CardClientStartUp extends Application {

    private final StageService stService = StageService.INSTANCE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        MessageType.initPackets();
    }

    @Override
    public void start(Stage stage) {
        //netty客户端线程启动
        new Thread(() -> new CardClient().start()).start();
        OpenUi(stage);
    }

    private void OpenUi(Stage stage){
        stService.addStage("root", stage);

        //登录界面
        stService.loadStage(UIUrl.Name.LoginView, UIUrl.Layout.LoginView, StageStyle.UNDECORATED);

        //游戏大厅
        stService.loadStage(UIUrl.Name.HallView, UIUrl.Layout.HallView, StageStyle.UNDECORATED);

        //对战界面
        stService.loadStage(UIUrl.Name.BattleView, UIUrl.Layout.BattleView, StageStyle.UNDECORATED);

        //TODO map加载
//        NodeMap.loadMap();
        stService.setStage(UIUrl.Name.LoginView);
    }

}

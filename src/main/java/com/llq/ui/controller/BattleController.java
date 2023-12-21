package com.llq.ui.controller;

import com.llq.client.CommunicationService;
import com.llq.message.manager.GameManager;
import com.llq.message.req.GameStartReqMsg;
import com.llq.message.req.GetCardReqMsg;
import com.llq.message.req.LeaveTabReqMsg;
import com.llq.message.req.RoundEndReqMsg;
import com.llq.ui.StageService;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleController implements Controller {

    @FXML
    private AnchorPane ancPane1;

    @FXML
    private Label bloodLabel, bloodLabel2, nameLabel;

    @FXML
    private VBox infoBox;

    @FXML
    private HBox btnBox1, btnBox2, resBox;

    @FXML
    private Button btn1, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    @FXML
    void OnLeave(MouseEvent event) {
        if(NameForAll.player != null && NameForAll.mytable_name != null) {
            CommunicationService.INSTANCE.sendMessage(new LeaveTabReqMsg(NameForAll.mytable_name, NameForAll.player.isMaster()));
        }
        StageService.INSTANCE.switchStage(UIUrl.Name.HallView, UIUrl.Name.BattleView);
        NameForAll.clear();
    }

    @FXML
    void Onstart(MouseEvent event) {
        CommunicationService.INSTANCE.sendMessage(new GameStartReqMsg(NameForAll.mytable_name));
    }

    @FXML
    void MoveDigit(MouseEvent event) {
        btnBox1.setVisible(false);
        CommunicationService.INSTANCE.sendMessage(new GetCardReqMsg(1, NameForAll.player.isMaster(), NameForAll.mytable_name));
    }

    @FXML
    void MoveOper(MouseEvent event) {
        btnBox1.setVisible(false);
        CommunicationService.INSTANCE.sendMessage(new GetCardReqMsg(2, NameForAll.player.isMaster(), NameForAll.mytable_name));
    }

    @FXML
    void OnBack(MouseEvent event) {
        GameManager.GAME_MANAGER.goBackAndClear();
    }

    @FXML
    void OnChargeShield(MouseEvent event) {
        GameManager.GAME_MANAGER.charge(true);
    }

    @FXML
    void OnChargeSword(MouseEvent event) {
        GameManager.GAME_MANAGER.charge(false);
    }

    @FXML
    void OnOperation() {
        //5btnBox隐藏
        btnBox2.setVisible(false);
        //3btnBox显示
        btnBox1.setVisible(true);
        btn7.setVisible(false);
        btn9.setVisible(false);
        btn8.setVisible(true);
        btn8.setDisable(true);
        btn6.setVisible(true);

        resBox.setVisible(true);
    }

    @FXML
    void realOperation(){
        GameManager.GAME_MANAGER.operation();
    }

    @FXML
    void OnRoundEnd(MouseEvent event) {
        CommunicationService.INSTANCE.sendMessage(
                new RoundEndReqMsg(NameForAll.mytable_name, NameForAll.player.isMaster(), NameForAll.player.getPlayerInfo()));
        btnBox1.setVisible(false);
        btnBox2.setVisible(false);
    }

    @Override
    public Stage getMyStage() {
        return StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
    }
}

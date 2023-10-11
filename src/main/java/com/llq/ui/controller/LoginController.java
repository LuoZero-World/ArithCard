package com.llq.ui.controller;

import com.llq.client.CommunicationService;
import com.llq.message.manager.LoginManager;
import com.llq.ui.StageService;
import com.llq.utility.UIUrl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Controller{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField loginPwd;

    @FXML
    private TextField loginUnm;

    @FXML
    private VBox loginLeftV;

    @FXML
    private Pane loginCenPane, errPane;

    private List<Node> Ancnodes;
    private int preVisableIdx = 0;

    @FXML
    void OnLogin(MouseEvent event) {

        String name = loginUnm.getText();
        String password = loginPwd.getText();

        if(!CommunicationService.INSTANCE.isConnected()){
            Stage stage = StageService.INSTANCE.getStageBy(UIUrl.Name.LoginView);
            Parent root = stage.getScene().getRoot();
            Pane errPane = (Pane)root.lookup("#errpane");
            errPane.setVisible(true);
            ((Label)root.lookup("#errtype")).setText("还未连接到服务器！");
            root.lookup("#ancregister").setVisible(false);
        }
        else LoginManager.LOGIN_MANAGER.beginToLogin(name, password);
    }

    @FXML
    void initialize() {

        //收集Pane中的AnchorPane控件
        Ancnodes = new ArrayList<>();
        loginCenPane.getChildren().forEach(node -> {
            if(node.getTypeSelector().equals("AnchorPane")){
                Ancnodes.add(node);
            }
        });

        //给Vbox中每个Label添加事件
        loginLeftV.getChildren().forEach(node -> {
            //如果是Label控件
            if(node.getTypeSelector().equals("Label")){
                node.setOnMousePressed(mouseEvent -> {
                    int idx = node.getId().charAt(5)-'1';
                    Ancnodes.get(preVisableIdx).setVisible(false);
                    Ancnodes.get(idx).setVisible(true);
                    preVisableIdx = idx;
                });
            };
        });
    }

    @FXML
    void OnConfirm(MouseEvent event) {
        errPane.setVisible(false);
        loginCenPane.lookup("#anclogin").setVisible(true);
    }

    @FXML
    void OnClose(MouseEvent event) {
        CommunicationService.INSTANCE.closeMyChannel();
        Platform.exit();
    }

    @Override
    public Stage getMyStage() {
        return StageService.INSTANCE.getStageBy(UIUrl.Name.LoginView);
    }
}

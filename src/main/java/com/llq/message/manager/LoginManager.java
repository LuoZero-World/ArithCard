package com.llq.message.manager;

import com.llq.client.CommunicationService;
import com.llq.message.req.LoginReqMsg;
import com.llq.message.req.TableReqMsg;
import com.llq.message.resp.LoginRespMsg;
import com.llq.ui.StageService;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//登录控制器，真正执行登录成功/失败后的逻辑操作
public enum LoginManager {
    LOGIN_MANAGER;

    public void beginToLogin(String username, String password){

        System.out.println("======>>>>>发送登录请求");
        NameForAll.user_name = username;
        CommunicationService.INSTANCE.sendMessage(new LoginReqMsg(username, password));
    }

    public void handlerLoginResp(LoginRespMsg msg){
        if(msg.isSuccess()){
            Platform.runLater(()->{
                System.out.println("======>>>>>登录成功");
                StageService.INSTANCE.switchStage(UIUrl.Name.HallView, UIUrl.Name.LoginView);
                CommunicationService.INSTANCE.sendMessage(new TableReqMsg(false, ""));
            });
        } else{
            System.out.println(">>>>>登录失败");
            Platform.runLater(()->{
                Stage stage = StageService.INSTANCE.getStageBy(UIUrl.Name.LoginView);
                Parent root = stage.getScene().getRoot();
                Pane errPane = (Pane)root.lookup("#errpane");
                errPane.setVisible(true);
                ((Label)root.lookup("#errtype")).setText("登录失败:");
                ((Label)root.lookup("#reason")).setText(msg.getReason());
                root.lookup("#anclogin").setVisible(false);
            });
        }
    }
}

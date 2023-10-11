package com.llq.message.manager;

import com.llq.client.CommunicationService;
import com.llq.entity.Player;
import com.llq.entity.PlayerInfo;
import com.llq.entity.TabInfo;
import com.llq.message.req.TableReqMsg;
import com.llq.message.resp.CreateTabRespMsg;
import com.llq.message.resp.JoinTabRespMsg;
import com.llq.message.resp.TableRespMsg;
import com.llq.ui.StageService;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.NonNull;

public enum TableOpManager {
    TABLE_OP_MANAGER;

    public void flushTable(TableRespMsg msg){
        if(!msg.isSuccess()){
            System.out.println(msg.getReason());
            getNodeById("#tiplabel", StageService.INSTANCE.getStageBy(UIUrl.Name.HallView)).setVisible(true);
        }

        Platform.runLater(() -> {
            ListView<TabInfo> tableList = (ListView<TabInfo>) getNodeById("#tablelist", StageService.INSTANCE.getStageBy(UIUrl.Name.HallView));
            ObservableList<TabInfo> items = tableList.getItems();
            items.clear();
            items.addAll(msg.getTabInfos());
        });

    }

    public void flushTable2(TableRespMsg msg){
        if(!msg.isSuccess()){
            System.out.println(msg.getReason());
            getNodeById("#tiplabel", StageService.INSTANCE.getStageBy(UIUrl.Name.HallView)).setVisible(true);
        }

        Platform.runLater(() -> {
            ListView<TabInfo> tableList = (ListView<TabInfo>) getNodeById("#tablelist2", StageService.INSTANCE.getStageBy(UIUrl.Name.HallView));
            ObservableList<TabInfo> items = tableList.getItems();
            items.clear();
            items.addAll(msg.getTabInfos());
        });
    }

    public void createTable(CreateTabRespMsg msg){
        if(msg.isSuccess()){
            System.out.println(">>>>>对局创建成功");
            //创建你的对战角色
            NameForAll.player = new Player(NameForAll.user_name, true);
            Platform.runLater(()->{
                //隐藏另一半对局 显示离开按钮
                initStage();
                Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
                getNodeById("#leavebutton", battle).setVisible(true);

                //将自己(master)的信息加载到对局桌面上
                StageService.INSTANCE.loadBattleStage();
                StageService.INSTANCE.switchStage(UIUrl.Name.BattleView, UIUrl.Name.HallView);
            });
        } else{
            System.out.println(msg.getReason());
            Platform.runLater(()->{
                Label label = (Label) getNodeById("#errlabel1", StageService.INSTANCE.getStageBy(UIUrl.Name.HallView));
                label.setText("对局名已存在！");
                label.setVisible(true);
            });
        }
    }

    public void completeTable(JoinTabRespMsg msg){
        if (msg.isSuccess()){   //加入成功 分主客对界面进行渲染
            PlayerInfo masterInfo = msg.getMasterInfo();
            Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
            //桌主
            //这里不能用nameForAll中的player信息判断，因为挑战者的信息还未创建
            if(masterInfo.getNickname().equals(NameForAll.user_name)){
                PlayerInfo challengeInfo = msg.getChallengeInfo();
                Platform.runLater(()->{
                    getNodeById("#leavebutton", battle).setVisible(false);
                    getNodeById("#startbutton", battle).setVisible(true);
                    StageService.INSTANCE.loadBattleStageAgain(challengeInfo);
                });
            } else{
                //创建挑战者角色
                NameForAll.player = new Player(NameForAll.user_name, false);
                Platform.runLater(()->{
                    //将桌主信息和自己的信息加载到桌面上
                    initStage();
                    StageService.INSTANCE.loadBattleStage();
                    StageService.INSTANCE.loadBattleStageAgain(masterInfo);
                    StageService.INSTANCE.switchStage(UIUrl.Name.BattleView, UIUrl.Name.HallView);
                    getNodeById("#leavebutton", battle).setVisible(true);
                });
            }
        } else {        //加入失败 刷新大厅
            System.out.println(msg.getReason());
            CommunicationService.INSTANCE.sendMessage(new TableReqMsg(false, ""));
            NameForAll.mytable_name = null;
        }
    }

    public void challengeLeave(){
        Platform.runLater(()->{
            Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
            getNodeById("#startbutton", battle).setVisible(false);
            getNodeById("#leavebutton", battle).setVisible(true);
            //隐藏对手信息
            getNodeById("#ancpane1", battle).setVisible(false);

        });
    }

    //用于初始化(隐藏)battle界面的控件
    void initStage(){
        Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
        getNodeById("#ancpane1", battle).setVisible(false);
        getNodeById("#3btnbox", battle).setVisible(false);
        getNodeById("#6btnbox", battle).setVisible(false);
        getNodeById("#myattack", battle).setVisible(false);
        getNodeById("#mydefend", battle).setVisible(false);
        getNodeById("#challengeattack", battle).setVisible(false);
        getNodeById("#challengedefend", battle).setVisible(false);
        getNodeById("#leavebutton", battle).setVisible(false);
        getNodeById("#startbutton", battle).setVisible(false);
        ((VBox)(getNodeById("#infobox", battle))).getChildren().clear();
        HBox cardBox = (HBox) getNodeById("#resbox", battle);
        cardBox.setVisible(false);
        cardBox.getChildren().forEach(children->{
            if(children instanceof Label){
                ((Label)children).setText("");
            }
        });
    }

    Node getNodeById(String id, @NonNull Stage stage){
        return stage.getScene().getRoot().lookup(id);
    }

}

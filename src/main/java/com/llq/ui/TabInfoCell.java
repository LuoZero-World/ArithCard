package com.llq.ui;

import com.llq.client.CommunicationService;
import com.llq.entity.TabInfo;
import com.llq.message.req.JoinTabReqMsg;
import com.llq.utility.NameForAll;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

/**
 * @author 李林麒
 * @date 2023/1/19 13:54
 * @Description 规定ListView组件中每一个单元格的样式
 */
public class TabInfoCell extends ListCell<TabInfo> {

    private Label label;
    private BorderPane borderPane;
    private String tableName = "";

    public TabInfoCell(){
        label = new Label();
        borderPane = new BorderPane();
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        Button button = new Button();
        button.getStyleClass().add("enter-btn");
        button.setText("进入对局");
        button.setOnAction(e -> {
            CommunicationService.INSTANCE.sendMessage(new JoinTabReqMsg(tableName, NameForAll.user_name));
            int endIdx = label.getText().indexOf("房主");
            NameForAll.mytable_name = label.getText().substring(3, endIdx-4);
        });
        borderPane.setLeft(label);
        borderPane.setRight(button);
    }

    @Override
    protected void updateItem(TabInfo tabInfo, boolean empty) {
        super.updateItem(tabInfo, empty);
        if(tabInfo == null || empty){
            setGraphic(null);
        } else{
            tableName = tabInfo.getTableName();
            String str = "房名："+tableName+"    房主："+tabInfo.getMaster();
            if(tabInfo.isComplete()){
                String str2 = "    挑战者："+tabInfo.getChallenger()+"    观战人数："+tabInfo.getWatchers()+"/"+tabInfo.getMax_watchers();
                label.setText(str+str2);
            } else{
                label.setText(str+"    等待加入中...");
            }
            setGraphic(borderPane);
        }
    }
}

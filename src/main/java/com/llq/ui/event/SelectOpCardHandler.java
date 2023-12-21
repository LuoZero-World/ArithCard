package com.llq.ui.event;

import com.llq.utility.ArithmeticMachine;
import com.llq.utility.NameForAll;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author 李林麒
 * @date 2023/1/30 23:33
 * @Description 选择运算牌
 */
public class SelectOpCardHandler implements EventHandler<MouseEvent> {

    ImageView image;

    public SelectOpCardHandler(ImageView image) {
        this.image = image;
    }

    @Override
    public void handle(MouseEvent event) {
        Node root = image.getScene().getRoot();
        Label resLabel = (Label)(root.lookup("#reslabel"));
        //将确认按钮设置为不可用
        Button button = (Button) root.lookup("#btn8");
        button.setDisable(true);

        //回归原位置
        if(image.getTranslateY() < 0) {
            image.setTranslateY(0);
            NameForAll.operation = null;
            Label label = (Label) root.lookup("#oplabel");
            label.setText("");
            resLabel.setText("");
        } else{
            if(NameForAll.operation != null) return;
            //选择当前牌
            image.setTranslateY(-30);
            NameForAll.operation = image.getId();
            Label label = (Label) root.lookup("#oplabel");
            label.setText(NameForAll.operation);
            if(NameForAll.couldCompute())
                resLabel.setText(ArithmeticMachine.tryCompute(NameForAll.operation, NameForAll.first, NameForAll.second));

        }
    }
}

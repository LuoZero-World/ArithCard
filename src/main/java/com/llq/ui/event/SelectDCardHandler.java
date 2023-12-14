package com.llq.ui.event;

import com.llq.utility.ArithmeticMachine;
import com.llq.utility.NameForAll;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author 李林麒
 * @date 2023/1/30 23:29
 * @Description 选择数字牌
 */
public class SelectDCardHandler implements EventHandler<MouseEvent> {

    ImageView image;

    public SelectDCardHandler(ImageView image) {
        this.image = image;
    }

    @Override
    public void handle(MouseEvent event) {
        Node root = image.getScene().getRoot();
        Label resLabel = (Label)(root.lookup("#reslabel"));
        //回归原位置
        if(image.getTranslateY() < 0) {
            resLabel.setText("");
            image.setTranslateY(0);
            String content = image.getId();
            //还原
            if(content.equals(NameForAll.first)) {
                NameForAll.first = null;
                Label label = (Label) root.lookup("#firstlabel");
                label.setText("");
            }
            else{
                NameForAll.second = null;
                Label label = (Label) root.lookup("#secondlabel");
                label.setText("");
            }
        } else{
            //点了两张牌后又点其他牌
            if(NameForAll.first != null && NameForAll.second != null) return;
            //选择当前牌
            image.setTranslateY(-30);
            String content = image.getId();
            if(NameForAll.first == null) {
                NameForAll.first = content;
                Label label = (Label) root.lookup("#firstlabel");
                label.setText(content);
            }
            else{
                NameForAll.second = content;
                Label label = (Label) root.lookup("#secondlabel");
                label.setText(content);
            }

            //计算结果并显示
            if(NameForAll.couldCompute())
                resLabel.setText(ArithmeticMachine.tryCompute(NameForAll.operation, NameForAll.first, NameForAll.second));
        }
    }
}

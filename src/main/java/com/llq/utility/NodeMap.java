package com.llq.utility;

import com.llq.ui.StageService;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author 李林麒
 * @date 2023/1/30 22:21
 * @Description 在界面加载时，将FXML文件中所有节点加载入Map中
 */
public class NodeMap {

    public static HashMap<String, Node> idToNodeMap = new HashMap<>();

    private NodeMap() {}

    public static void loadMap(){
        Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
        battle.getScene().getRoot().getChildrenUnmodifiable().forEach(node -> {
            System.out.println(node.getId());
            idToNodeMap.put(node.getId(), node);
        });
    }
}

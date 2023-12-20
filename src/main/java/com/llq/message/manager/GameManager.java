package com.llq.message.manager;

import com.llq.client.CommunicationService;
import com.llq.entity.Card;
import com.llq.entity.PlayerInfo;
import com.llq.message.req.RoundStartReqMsg;
import com.llq.message.resp.GameStartRespMsg;
import com.llq.message.resp.GetCardRespMsg;
import com.llq.ui.StageService;
import com.llq.ui.event.SelectDCardHandler;
import com.llq.ui.event.SelectOpCardHandler;
import com.llq.utility.ImgUrl;
import com.llq.utility.NameForAll;
import com.llq.utility.UIUrl;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

@Slf4j
public enum GameManager {
    GAME_MANAGER;

    private Button[] buttons = null;
    private Label fLabel = null, sLabel = null, opLabel = null, resLabel = null;
    private HBox hbox3btn = null, hbox5btn = null;
    private VBox infoBox = null;

    //TODO 有序
    Deque<Pair<Integer, String>> msgQueue = new ArrayDeque<>();

    //游戏开始，显示手牌，发送回合开始请求
    public void startGame(GameStartRespMsg msg){
        //桌主
        if(NameForAll.player.isMaster()){
            NameForAll.player.setPlayerInfo(msg.getMasterInfo());
            Platform.runLater(()->{
                getNodeById("#startbutton").setVisible(false);
                loadNode();
                renderStage();
            });
            CommunicationService.INSTANCE.sendMessage(new RoundStartReqMsg(NameForAll.mytable_name));
        } else{
            NameForAll.player.setPlayerInfo(msg.getChallengeInfo());
            Platform.runLater(()->{
                getNodeById("#leavebutton").setVisible(false);
                loadNode();
                renderStage();
            });
        }
    }

    //将牌渲染上去
    void renderStage(){
        List<Card> dightList = NameForAll.player.getPlayerInfo().getDigitList();
        List<Card> operList = NameForAll.player.getPlayerInfo().getOperList();
        int num1 = dightList.size(), num2 = operList.size();
        int translate1 = 200/num1, translate2 = 200/num2;

        StackPane digit = (StackPane) getNodeById("#digitpane");
        StackPane operation = (StackPane) getNodeById("#operationpane");
        digit.getChildren().clear();
        operation.getChildren().clear();
        for(int i = 0; i < num1; i++){
            ImageView image = new ImageView(getUrlBy(dightList.get(i)));
            image.setFitHeight(130);
            image.setFitWidth(90);
            image.setId(dightList.get(i).getContent());         //id代表牌的内容
            image.setTranslateX(translate1*i);
            image.setOnMousePressed(new SelectDCardHandler(image));
            digit.getChildren().add(image);
        }
        for(int i = 0; i < num2; i++){
            ImageView image = new ImageView(getUrlBy(operList.get(i)));
            image.setFitHeight(130);
            image.setFitWidth(90);
            image.setId(operList.get(i).getContent());
            image.setTranslateX(translate2*i);
            image.setOnMousePressed(new SelectOpCardHandler(image));
            operation.getChildren().add(image);
        }
    }

    //左面栏目显示游戏信息
    public void showMessage(String content, int idx){
        Platform.runLater(()->{
            if(infoBox.getChildren().size() > 30) {
                infoBox.getChildren().clear();
            }
            Label infoLabel = new Label(content);
            infoLabel.setStyle("-fx-font-size: 12;");
            infoBox.getChildren().add(infoLabel);
        });
    }

    void loadNode(){
        buttons = new Button[10];
        for(int i = 1; i <= 9; i++){
            Button btn = (Button) getNodeById("#btn"+i);
            if(btn != null) buttons[i] = btn;
        }
        fLabel = (Label) getNodeById("#firstlabel");
        sLabel = (Label) getNodeById("#secondlabel");
        opLabel = (Label) getNodeById("#oplabel");
        resLabel = (Label) getNodeById("#reslabel");
        hbox3btn = (HBox) getNodeById("#3btnbox");
        hbox5btn = (HBox) getNodeById("#5btnbox");
        infoBox = (VBox) getNodeById("#infobox");
    }

    //摸牌
    public void moveCard(){
        Platform.runLater(()->{
            //摸牌询问
            hbox3btn.setVisible(true);
            buttons[7].setVisible(true);
            buttons[9].setVisible(true);
            buttons[8].setVisible(false);
        });
    }

   //回合正式结束
   public void endMyRound(){
       //由master发送回合开始请求
       if(NameForAll.player.isMaster())
           CommunicationService.INSTANCE.sendMessage(new RoundStartReqMsg(NameForAll.mytable_name));
   }

   //游戏结束
   public void endGame(){
        clear();
        Platform.runLater(()->{
            getNodeById("#leavebutton").setVisible(true);
        });
   }

    //获取卡牌刷新界面
    public void getCard(GetCardRespMsg msg){
        //获取卡牌失败
        if(!msg.isSuccess()){
            System.out.println(msg.getReason());
            return;
        }
        //更新内存中的玩家手牌
        Card card = msg.getCard();
        if(card.getType() == Card.CardType.DigitCard) NameForAll.player.getPlayerInfo().getDigitList().add(card);
        else NameForAll.player.getPlayerInfo().getOperList().add(card);

        Platform.runLater(()->{
            renderStage();
            //操作询问
            hbox5btn.setVisible(true);
            buttons[6].setVisible(false);
        });
    }

    //运算(天然提供预运算功能)
    public void operation() {
        //牌没有选完
        if(NameForAll.first == null || NameForAll.second == null || NameForAll.operation == null) {
            return;
        }

        System.out.println("============运算==============");
        //删牌补牌
        PlayerInfo playerInfo = NameForAll.player.getPlayerInfo();
        Iterator<Card> iterator1 = playerInfo.getDigitList().iterator();
        Iterator<Card> iterator2 = playerInfo.getOperList().iterator();
        while(iterator1.hasNext()){
            Card card = iterator1.next();
            if(card.getContent().equals(NameForAll.first)){
                NameForAll.first = null;
                iterator1.remove();
            } else if(card.getContent().equals(NameForAll.second)){
                NameForAll.second = null;
                iterator1.remove();
            }
            if(NameForAll.first == null && NameForAll.second == null) break;
        }
        while(iterator2.hasNext()){
            Card card = iterator2.next();
            if(card.getContent().equals(NameForAll.operation)){
                NameForAll.operation = null;
                iterator2.remove();
                break;
            }
        }

        playerInfo.getDigitList().add(new Card(resLabel.getText()));
        //渲染桌面
        renderStage();
        getNodeById("#resbox").setVisible(false);
        hbox3btn.setVisible(false);
        hbox5btn.setVisible(true);
        fLabel.setText("");
        sLabel.setText("");
        opLabel.setText("");
        resLabel.setText("");
    }

    //true代表给盾充能 false代表给剑充能
    public void charge(boolean flag){
        PlayerInfo playerInfo = NameForAll.player.getPlayerInfo();
        //已经充好能了
        if(flag && playerInfo.isDefend()){
            showMessage("盾牌已经充能完毕,无需重复", 10_001);
            return;
        }
        if(!flag && playerInfo.isAttack()){
            showMessage("剑已经充能完毕,无需重复", 10_002);
            return;
        }

        boolean f = false;

        Iterator<Card> iterator = playerInfo.getDigitList().iterator();
        while(iterator.hasNext()){
            Card card = iterator.next();
            if(card.getContent().equals("24")){
                f = true;
                iterator.remove();
                break;
            }
        }

        //没有找到 24 点
        if(!f){
            showMessage("没数字牌 24", 10_003);
            return;
        }

        if(flag) playerInfo.setDefend(true);
        else playerInfo.setAttack(true);

        Platform.runLater(()->{
            if(flag) getNodeById("#mydefend").setVisible(true);
            else getNodeById("#myattack").setVisible(true);

            renderStage();
        });
    }

    void clear(){
        //全局变量赋空
        NameForAll.clear();
        buttons = null;
        hbox5btn = null;
        hbox3btn = null;
        fLabel = null;
        sLabel = null;
        opLabel = null;
        resLabel = null;
        infoBox = null;
    }

    Node getNodeById(String id){
        Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
        return battle.getScene().getRoot().lookup(id);
    }

    String getUrlBy(Card card){
        String content = card.getContent();
        return switch (content) {
            case "1" -> ImgUrl.One;
            case "2" -> ImgUrl.Two;
            case "3" -> ImgUrl.Three;
            case "4" -> ImgUrl.Four;
            case "5" -> ImgUrl.Five;
            case "6" -> ImgUrl.Six;
            case "7" -> ImgUrl.Seven;
            case "8" -> ImgUrl.Eight;
            case "9" -> ImgUrl.Nine;
            case "10" -> ImgUrl.Ten;
            case "11" -> ImgUrl.Eleven;
            case "12" -> ImgUrl.Twelve;
            case "13" -> ImgUrl.Thirteen;
            case "14" -> ImgUrl.Fourteen;
            case "15" -> ImgUrl.Fifteen;
            case "16" -> ImgUrl.Sixteen;
            case "17" -> ImgUrl.Seventeen;
            case "18" -> ImgUrl.Eighteen;
            case "19" -> ImgUrl.Nineteen;
            case "20" -> ImgUrl.Twenty;
            case "21" -> ImgUrl.TwentyOne;
            case "22" -> ImgUrl.TwentyTwo;
            case "23" -> ImgUrl.TwentyThree;
            case "24" -> ImgUrl.TwentyFour;
            case "+" -> ImgUrl.Addition;
            case "-" -> ImgUrl.Subtraction;
            case "*" -> ImgUrl.Multiplication;
            case "/" -> ImgUrl.Division;
            case "%" -> ImgUrl.Redundant;
            case "&" -> ImgUrl.And;
            case "|" -> ImgUrl.Or;
            case "^" -> ImgUrl.Xor;
            case "<<" -> ImgUrl.Shift_left;
            case ">>" -> ImgUrl.Shift_right;
            default -> null;
        };
    }
}

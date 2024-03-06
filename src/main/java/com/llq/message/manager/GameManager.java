package com.llq.message.manager;

import com.llq.client.CommunicationService;
import com.llq.entity.Card;
import com.llq.entity.PlayerInfo;
import com.llq.message.req.RoundStartReqMsg;
import com.llq.message.resp.GameStartRespMsg;
import com.llq.message.resp.GetCardRespMsg;
import com.llq.message.resp.StateChangeMsg;
import com.llq.ui.StageService;
import com.llq.ui.event.SelectDCardHandler;
import com.llq.ui.event.SelectOpCardHandler;
import com.llq.utility.ImgUrl;
import com.llq.utility.NameForAll;
import com.llq.utility.PropertiesUtil;
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
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

/*有两种线程会用到GameManager
 * 1. 执行Msg中execute函数
 * 2. 执行Controller中事件触发函数
 */
@Slf4j
public enum GameManager {
    GAME_MANAGER;

    private final int blood = PropertiesUtil.getInt("HP");
    private final int bloodSegLength = 240/blood;
    private Button[] buttons = null;
    private Label fLabel = null, sLabel = null, opLabel = null, resLabel = null;
    private HBox hbox3btn = null, hbox5btn = null;
    private VBox infoBox = null;

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
    public void showMessage(List<String> content){
        Platform.runLater(()->{
            if(infoBox.getChildren().size() > 30) {
                infoBox.getChildren().clear();
            }
            content.forEach(msg ->{
                Label infoLabel = new Label(msg);
                infoLabel.setStyle("-fx-font-size: 12;");
                infoBox.getChildren().add(infoLabel);
            });
        });
    }

    public void showMessage(String content){
        Platform.runLater(()->{
            if(infoBox.getChildren().size() > 20) {
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
            buttons[6].setVisible(false);
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
        //TODO 要等待所有信息接受完毕 才能清除实例
       try {
           Thread.sleep(500);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }

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
        goBackAndClear();
    }

    public void goBackAndClear(){
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
            showMessage("盾牌已经充能完毕,无需重复");
            return;
        }
        if(!flag && playerInfo.isAttack()){
            showMessage("剑已经充能完毕,无需重复");
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
            showMessage("没数字牌 24");
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

    //改变游戏中角色对战状态
    public void changeState(StateChangeMsg msg){
        PlayerInfo playerInfo = NameForAll.player.getPlayerInfo();;
        playerInfo.setHP(msg.getBlood1());
        playerInfo.setAttack(msg.isAttack1());
        playerInfo.setDefend(msg.isDefend1());
        Platform.runLater(()->{
            ((Label) getNodeById("#bloodlabel1")).setPrefWidth(bloodSegLength*msg.getBlood1());
            ((Label) getNodeById("#cbloodlabel1")).setPrefWidth(bloodSegLength*msg.getBlood2());
            ((Label) getNodeById("#bloodlabel2")).setText("HP: "+msg.getBlood1()+"/"+blood);
            ((Label) getNodeById("#cbloodlabel2")).setText("HP: "+msg.getBlood2()+"/"+blood);
            getNodeById("#mydefend").setVisible(msg.isDefend1());
            getNodeById("#myattack").setVisible(msg.isAttack1());
            getNodeById("#challengedefend").setVisible(msg.isDefend2());
            getNodeById("#challengeattack").setVisible(msg.isAttack2());
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
        switch (content) {
            case "1": return ImgUrl.One;
            case "2": return ImgUrl.Two;
            case "3": return ImgUrl.Three;
            case "4": return ImgUrl.Four;
            case "5": return ImgUrl.Five;
            case "6": return ImgUrl.Six;
            case "7": return ImgUrl.Seven;
            case "8": return ImgUrl.Eight;
            case "9": return ImgUrl.Nine;
            case "10": return ImgUrl.Ten;
            case "11": return ImgUrl.Eleven;
            case "12": return ImgUrl.Twelve;
            case "13": return ImgUrl.Thirteen;
            case "14": return ImgUrl.Fourteen;
            case "15": return ImgUrl.Fifteen;
            case "16": return ImgUrl.Sixteen;
            case "17": return ImgUrl.Seventeen;
            case "18": return ImgUrl.Eighteen;
            case "19": return ImgUrl.Nineteen;
            case "20": return ImgUrl.Twenty;
            case "21": return ImgUrl.TwentyOne;
            case "22": return ImgUrl.TwentyTwo;
            case "23": return ImgUrl.TwentyThree;
            case "24": return ImgUrl.TwentyFour;
            case "+": return ImgUrl.Addition;
            case "-": return ImgUrl.Subtraction;
            case "*": return ImgUrl.Multiplication;
            case "/": return ImgUrl.Division;
            case "%": return ImgUrl.Redundant;
            case "&": return ImgUrl.And;
            case "|": return ImgUrl.Or;
            case "^": return ImgUrl.Xor;
            case "<<": return ImgUrl.Shift_left;
            case ">>": return ImgUrl.Shift_right;
            default: return null;
        }
    }
}

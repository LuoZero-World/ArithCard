package com.llq.utility;

import cn.hutool.core.util.NumberUtil;
import com.llq.ui.StageService;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author 李林麒
 * @date 2023/1/31 17:30
 * @Description 计算器
 */
public class ArithmeticMachine {
    static Map<String, BiFunction<Integer, Integer, String>> hmap = new HashMap<String, BiFunction<Integer, Integer, String>>(){{
        put("+", (a, b) -> String.valueOf(a + b));
        put("-", (a, b) -> String.valueOf(a - b));
        put("*", (a, b) -> String.valueOf(a * b));
        put("/", (a, b) -> String.valueOf(a / b));
        put("%", (a, b) -> String.valueOf(a % b));
        put("&", (a, b) -> String.valueOf(a & b));
        put("|", (a, b) -> String.valueOf(a | b));
        put("^", (a, b) -> String.valueOf(a ^ b));
        put(">>", (a, b) -> String.valueOf(a >> b));
        put("<<", (a, b) -> String.valueOf(a << b));
    }};

    public static String tryCompute(String operation, int firstNum, int secondNum){
        String res = "0";
        Stage battle = StageService.INSTANCE.getStageBy(UIUrl.Name.BattleView);
        try{
            res = hmap.get(operation).apply(firstNum, secondNum);
        } catch (ArithmeticException e){
            res = "运算异常！";
        }

        if (NumberUtil.isInteger(res)) {
            int int_res = Integer.parseInt(res);
            if(int_res > 24){
                res = String.valueOf(int_res % 25 + 1);
            } else if(int_res <= 0){
                res = "运算结果应该为正数！";
            }
        }
        //设置确认按钮 可用
        if(NumberUtil.isInteger(res)){
            Platform.runLater(()->{
                Button button = (Button) battle.getScene().getRoot().lookup("#btn8");
                button.setDisable(false);
            });
        }
        return res;
    }

    public static String tryCompute(@NonNull String operation, @NonNull String num1, @NonNull String num2){
        int firstNum = Integer.parseInt(num1), secondNum = Integer.parseInt(num2);
        return tryCompute(operation, firstNum, secondNum);
    }

}

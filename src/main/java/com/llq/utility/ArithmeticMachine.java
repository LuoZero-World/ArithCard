package com.llq.utility;

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
    static Map<String, BiFunction<Integer, Integer, String>> hmap = new HashMap<>(){{
        put("+", (a, b) -> String.valueOf(a + b));
        put("-", (a, b) -> {
            if (a <= b) return "运算结果应该为正数！";
            return String.valueOf(a - b);
        });
        put("*", (a, b) -> String.valueOf(a * b));
        put("/", (a, b) -> {
            if (b == 0) return "除零错误！";
            if(a < b) return "运算结果应该为正数！";
            return String.valueOf(a / b);
        });
        put("%", (a, b) -> {
            if (b == 0) return "余0错误！";
            return String.valueOf(a % b);
        });
        put("&", (a, b) -> String.valueOf(a & b));
        put("|", (a, b) -> String.valueOf(a | b));
        put("^", (a, b) -> String.valueOf(a ^ b));
        put(">>", (a, b) -> String.valueOf(a >> b));
        put("<<", (a, b) -> String.valueOf(a << b));
    }};

    public static String tryCompute(String operation, int firstNum, int secondNum){
        String res = hmap.get(operation).apply(firstNum, secondNum);
        int fnum = res.charAt(0) - '0';
        if (fnum > 0 && fnum <= 9) {  //取余
            int int_res = Integer.parseInt(res);
            if(int_res > 24)
                res = String.valueOf(int_res % 25 + 1);
        }
        return res;
    }

    public static String tryCompute(@NonNull String operation, @NonNull String num1, @NonNull String num2){
        int firstNum = Integer.parseInt(num1), secondNum = Integer.parseInt(num2);
        return tryCompute(operation, firstNum, secondNum);
    }
}

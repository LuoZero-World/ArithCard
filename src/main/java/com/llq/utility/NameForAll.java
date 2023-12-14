package com.llq.utility;

import com.llq.entity.Player;

/**
 * @author 李林麒
 * @date 2023/1/26 22:21
 * @Description 包括用户名，对局名等等
 */
public class NameForAll {
    public static String user_name = null;
    public static String mytable_name = null;
    public static Player player = null;

    public static String first = null;
    public static String second = null;
    public static String operation = null;

    public static void clear(){
        mytable_name = null;
        player = null;
        first = null;
        second = null;
        operation = null;
    }

    public static boolean couldCompute(){
        if(first != null && second != null && operation != null) return true;
        return false;
    }
}

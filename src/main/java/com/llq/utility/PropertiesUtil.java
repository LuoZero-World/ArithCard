package com.llq.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 李林麒
 * @date 2023/1/14 19:22
 * @Description: 读取properties工具类，不提供clear接口
 */
public class PropertiesUtil {

    static String folderRoot = "src/main/resources/config";

    public static Properties confProperties = new Properties();
    //将所有properties文件读入内存
    static {
        List<String> fileNames = getAllFile(folderRoot, true).stream()
                .map(str -> {
                    int idx = str.indexOf("\\config");
                    return str.substring(idx, str.length()).replaceAll("\\\\", "/");
                }).toList();

        for(String fileName : fileNames){

            try (InputStream in = PropertiesUtil.class.getResourceAsStream(fileName)) {
                confProperties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PropertiesUtil(){}

    public static String getStr(String key) {
        return confProperties.getProperty(key);
    }

    public static int getInt(String key){
        return Integer.parseInt(confProperties.getProperty(key));
    }

    //获取路径下所有文件名
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(isAddDirectory){
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }
}

package com.yinhai.tty.util;

import com.yinhai.tty.constant.PropertiesConst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    public static String getValue(String key,String file){
        Properties prop = new Properties();
        try {
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(file);
            prop.load(in);
            return prop.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return "没有参数："+key;
        }
    }

    public static void main(String[] args){
        String S = getValue("cloumn",PropertiesConst.DATABASE);
        System.out.println(S);
    }
}

package com.yinhai.tty.util;

import com.yinhai.tty.constant.DataBaseType;
import com.yinhai.tty.constant.PropertiesConst;
import com.yinhai.tty.entity.InfoBean;
import com.yinhai.tty.thread.ReadFileThread;
import com.yinhai.tty.thread.WriteDataBaseThread;

import java.io.*;
import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUpLoadUtil {

    /*public static void fileUpLoad(String filePath,String dataBaseType) throws Exception {
        InputStream is = null;
        BufferedReader reader = null;
        List<InfoBean> infoBeans = new ArrayList<InfoBean>();
        try {
            Instant start = Instant.now();
            File file = new File(filePath);
            if(file.isDirectory()){
                File[] filelist = file.listFiles();
                for(int i =0; i < filelist.length; i++){
                    infoBeans.addAll(readFile(filelist[i]));
                }
            }
            if(!file.isDirectory()){
                infoBeans = readFile(file);
            }
            Instant end = Instant.now();
            System.out.println("READ in milliseconds : " + Duration.between(start, end).toMillis());

            Connection conn = null;
            if(DataBaseType.Oracle.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("oraclejdbcurl"),
                        PropertiesUtil.getValue("oracleusername"),
                        PropertiesUtil.getValue("oracleuserpassword"),DataBaseType.Oracle.getDriverClassName());
            }
            if(DataBaseType.MySql.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("mysqljdbcurl"),
                        PropertiesUtil.getValue("mysqlusername"),
                        PropertiesUtil.getValue("mysqluserpassword"),DataBaseType.MySql.getDriverClassName());
            }
            if(DataBaseType.SQLServer.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("sqlserverjdbcurl"),
                        PropertiesUtil.getValue("sqlserverusername"),
                        PropertiesUtil.getValue("sqlserveruserpassword"),DataBaseType.SQLServer.getDriverClassName());
            }
            String result = DataBaseConnUtil.execute(conn,infoBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                reader.close();
            }
            if(reader!=null){
                reader.close();
            }
        }
    }

    public static List<InfoBean> readFile(File file) throws Exception {
        String encoding = "GBK";
        List<InfoBean> infoBeans = new ArrayList<InfoBean>();
        InfoBean infoBean=new InfoBean();
        if (file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            int size = 0;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] s=lineTxt.split(",");
                infoBean.setName(s[0]);
                infoBean.setSex(s[1]);
                infoBean.setIdcard(s[2]);
                infoBean.setBirthday(s[3]);
                infoBean.setType(s[4]);
                infoBean.setBalance(Double.parseDouble(s[5]));
                infoBean.setEmail(s[6]);
                infoBeans.add(infoBean);
            }
        }
        return infoBeans;
    }*/

    public static void fileUpLoad(String filePath,String dataBaseType){
        File file = new File(filePath);
        Connection conn = null;
        try {
            if(DataBaseType.Oracle.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("oraclejdbcurl",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("oracleusername",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("oracleuserpassword",PropertiesConst.APPLICATION),DataBaseType.Oracle.getDriverClassName());
            }
            if(DataBaseType.MySql.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("mysqljdbcurl",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("mysqlusername",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("mysqluserpassword",PropertiesConst.APPLICATION),DataBaseType.MySql.getDriverClassName());
            }
            if(DataBaseType.SQLServer.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection(PropertiesUtil.getValue("sqlserverjdbcurl",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("sqlserverusername",PropertiesConst.APPLICATION),
                        PropertiesUtil.getValue("sqlserveruserpassword",PropertiesConst.APPLICATION),DataBaseType.SQLServer.getDriverClassName());
            }
            ReadFileThread readFileThread = new ReadFileThread(file);
            List<Map<Integer,String>> infos = readFileThread.call();
            ReadFileThread readFileThread1 = new ReadFileThread(file);
            List<Map<Integer,String>> infos1 = readFileThread.call();

            WriteDataBaseThread writeDataBaseThread = new WriteDataBaseThread(conn,infos);
            String result = writeDataBaseThread.call();
            WriteDataBaseThread writeDataBaseThread1 = new WriteDataBaseThread(conn,infos1);
            String result1 = writeDataBaseThread.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Instant start = Instant.now();
            fileUpLoad("E:/JAVA/Workspaces/Idea/test","0");
            Instant end = Instant.now();
            System.out.println("milliseconds : " + Duration.between(start, end).toMillis());
        }catch (Exception e){
          e.printStackTrace();
        }

    }
}


package com.yinhai.tty.util;

import com.yinhai.tty.constant.DataBaseType;
import com.yinhai.tty.entity.InfoBean;

import java.io.*;
import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileUpLoadUtil {

    public static void fileUpLoad(String filePath,String dataBaseType) throws Exception {
        InputStream is = null;
        BufferedReader reader = null;
        List<InfoBean> infoBeans = new ArrayList<InfoBean>();
        try {
            Instant start = Instant.now();
            File file = new File(filePath);
            if(file.isDirectory()){
                File[] filelist = file.listFiles();
                for(int i =0; i < filelist.length; i++){
                    infoBeans = readFile(filelist[i]);
                }
            }
            if(!file.isDirectory()){
                infoBeans = readFile(file);
            }
            Instant end = Instant.now();
            System.out.println("Difference in milliseconds : " + Duration.between(start, end).toMillis());

            Connection conn = null;
            if(DataBaseType.Oracle.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","c##yjdexg","yjdexg",DataBaseType.Oracle.getDriverClassName());
            }
            if(DataBaseType.MySql.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("","","",DataBaseType.MySql.getDriverClassName());
            }
            if(DataBaseType.SQLServer.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("","","",DataBaseType.SQLServer.getDriverClassName());
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
    }
}


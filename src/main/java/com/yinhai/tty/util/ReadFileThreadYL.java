package com.yinhai.tty.util;

import com.yinhai.tty.constant.DataBaseType;
import com.yinhai.tty.entity.InfoBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 开启线程
 * Created by yanglei on 2018/12/20.
 */
public class ReadFileThreadYL extends Thread{
    File[] filelist;
    int start;
    int end;

    public ReadFileThreadYL(File[] filelist, int start, int end) {
        this.filelist = filelist;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        List<InfoBean> infoBeans = new ArrayList<InfoBean>();
        System.out.println("-----开始-----"+start+","+end);
        long time_s = System.currentTimeMillis();
        for(int i = start;i <= end; i++){
            try {
                infoBeans = readFile(filelist[i]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        insert("0",infoBeans);
        long time_e = System.currentTimeMillis();
        System.out.println("用时:"+(time_e-time_s)+"ms");
        System.out.println("------完成------");
    }


    public static void insert(String dataBaseType, List<InfoBean> infoBeans){
        Connection conn = null;
        try {
            if(DataBaseType.Oracle.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("jdbc:oracle:thin:@192.168.20.180:1521/orcl","ta3","ta3",DataBaseType.Oracle.getDriverClassName());
            }
            if(DataBaseType.MySql.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("","","",DataBaseType.MySql.getDriverClassName());
            }
            if(DataBaseType.SQLServer.getTypeId().equals(dataBaseType)){
                conn = DataBaseConnUtil.getConnection("","","",DataBaseType.SQLServer.getDriverClassName());
            }
           // String result = DataBaseConnUtil.execute(conn,infoBeans);
        }catch (Exception e){
          e.printStackTrace();
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

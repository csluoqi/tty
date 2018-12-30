package com.yinhai.tty;

import com.yinhai.tty.entity.InfoBean;
import com.yinhai.tty.util.ReadFileThreadYL;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanglei on 2018/12/20.
 */
public class test {
    public static void fileUpLoad(String filePath) throws Exception {
        System.out.println("-----------开始----------");
        long start = System.currentTimeMillis();
        InputStream is = null;
        BufferedReader reader = null;
        List<InfoBean> infoBeans = new ArrayList<InfoBean>();
        try {
            File file = new File(filePath);
            ReadFileThreadYL readFileThread= null;
            if(file.isDirectory()){
                File[] filelist = file.listFiles();
                int count = 1;
                BigDecimal bd1 = new BigDecimal(Double.toString(filelist.length));
                BigDecimal bd2 = new BigDecimal(Double.toString(count));
                BigDecimal big = bd1.divide(bd2,10,BigDecimal.ROUND_FLOOR);
                double j = big.doubleValue();
                for(int i = 0; i < count; i++) {
                   int s = (int)Math.ceil(i*j);
                   int e = (int)Math.ceil(j*(i+1))-1;
                    readFileThread = new ReadFileThreadYL(filelist,s,e);
                    readFileThread.start();
                }
            }
            if(!file.isDirectory()){
                infoBeans = readFile(file);
            }
        long end = System.currentTimeMillis();
        System.out.println("用时"+(end-start)/1000+"s");
        System.out.println("-----------结束----------");
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
        String encoding = "UTF-8";
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

    public static void main(String[] args) {
        try {
            fileUpLoad("E:/JAVA/Workspaces/Idea/test");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void xinacheng() throws InterruptedException {
        int count = 10;
        for(int i = 0; i < count; i++) {
            Thread worker = new Thread(new Runnable(){
                @Override
                public void run() {
                    System.out.println("执行子线程");
                }
            });
            worker.start();
        }
        System.out.println("执行主线程");

    }
}

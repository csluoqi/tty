package com.yinhai.tty.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinhai.tty.constant.PropertiesConst;
import com.yinhai.tty.util.PropertiesUtil;

import java.io.*;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * 开启读文件线程(带返回值)
 * Created by yuejun on 2018/12/21.
 */
public class ReadFileThread implements Callable {

    File file;
    private int start;//开始文件
    private int end;//结束文件
    private String threadname;
    Connection conn;
    BlockingQueue queue;

    public ReadFileThread(File file,int start,int end,String threadname,Connection conn,BlockingQueue queue) {
        this.file = file;
        this.start = start;
        this.end = end;
        this.threadname = threadname;
        this.conn = conn;
        this.queue = queue;
    }

    /**
     * 带返回值的线程
     * @return List<InfoBean> infoBeans
     * @throws Exception
     */
    @Override
    public BlockingQueue call(){
        List<Map<Integer,String>> infos = new ArrayList<>();
        try{
            Instant rstart = Instant.now();
            //读
            if(file.isDirectory()){
                File[] filelist = file.listFiles();
                for(int i =this.start; i <= this.end; i++){
                    infos.addAll(readFile(filelist[i-1],threadname));
                }
            }
            if(!file.isDirectory()){
                infos = readFile(file,threadname);
            }
            //queue.offer(infos);
            System.out.println("已读："+infos.size());
            Instant rend = Instant.now();
            System.out.println("READ "+this.threadname+" in milliseconds : " + Duration.between(rstart, rend).toMillis());
            queue.put(infos);
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queue;
    }

    /**
     * 读文件
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<Integer,String>> readFile(File file,String threadname){
        System.out.println(threadname+"----------"+Instant.now());
        String encoding = "GBK";
        List<Map<Integer,String>> infos = new ArrayList<>();
        InputStreamReader read = null;//考虑到编码格式
        BufferedReader bufferedReader = null;
        try {
            if (file.isFile() && file.exists()){ //判断文件是否存在
                read = new InputStreamReader(new FileInputStream(file), encoding);
                bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] s=lineTxt.split(",");
                    Map<Integer,String> info = new HashMap<>();
                    for(int i = 1; i <= s.length; i++){
                        info.put(i,s[i-1]);
                    }
                    infos.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(read!=null){
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return infos;
    }
}

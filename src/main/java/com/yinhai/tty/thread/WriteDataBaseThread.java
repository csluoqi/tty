package com.yinhai.tty.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinhai.tty.constant.PropertiesConst;
import com.yinhai.tty.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * 开启写入数据库线程(带返回值)
 * Created by yuejun on 2018/12/21.
 */
public class WriteDataBaseThread implements Runnable {

    Connection conn;
    private String threadname;
    BlockingQueue queue;
    int start;
    int end;

    public WriteDataBaseThread(Connection conn, String threadname,BlockingQueue queue,
                               int start,int end) {
        this.conn = conn;
        this.threadname = threadname;
        this.queue = queue;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        System.out.println(threadname+"----------"+Instant.now());
        try {
            String sql = PropertiesUtil.getValue("sql",PropertiesConst.DATABASE);
            PreparedStatement ps = conn.prepareStatement(sql);
            String jsonStr = PropertiesUtil.getValue("cloumn",PropertiesConst.DATABASE);
            JSONObject json = JSON.parseObject(jsonStr);
            int cloumn = 7;
            conn.setAutoCommit(false);
            for(int i = start; i <= end; i++){
                List<Map<Integer,String>> infs = (List<Map<Integer, String>>) queue.take();
                Instant wstart = Instant.now();
                for (Map<Integer,String> info : infs) {
                    for (int j = 1 ; j <= cloumn; j++){
                        if("String".equals(json.getString(String.valueOf(j)))){
                            ps.setString(j,info.get(j));
                        }
                        if("Double".equals(json.getString(String.valueOf(j)))){
                            ps.setDouble(j, Double.parseDouble(info.get(j)));
                        }
                        if("Date".equals(json.getString(String.valueOf(j)))){
                            ps.setString(j,info.get(j));
                        }
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
                System.out.println("已写："+infs.size());
                Instant wend = Instant.now();
                System.out.println("WRITE "+this.threadname+" in milliseconds : " + Duration.between(wstart, wend).toMillis());
            }
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

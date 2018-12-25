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
import java.util.concurrent.Callable;

/**
 * 开启写入数据库线程(带返回值)
 * Created by yuejun on 2018/12/21.
 */
public class WriteDataBaseThread implements Callable {

    Connection conn;
    List<Map<Integer,String>> infos;

    public WriteDataBaseThread(Connection conn, List<Map<Integer,String>> infos) {
        this.conn = conn;
        this.infos = infos;
    }

    @Override
    public String call(){
        String result = execute(conn,infos);
        return result;
    }

    /**
     * 执行sql
     * @param conn 数据库连接
     * @param infos
     * @return
     */
    public static String execute(Connection conn, List<Map<Integer,String>> infos){
        String result = "执行成功！";
        Instant start = Instant.now();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            String sql = PropertiesUtil.getValue("sql",PropertiesConst.DATABASE);
            ps = conn.prepareStatement(sql);
            String jsonStr = PropertiesUtil.getValue("cloumn",PropertiesConst.DATABASE);
            JSONObject json = JSON.parseObject(jsonStr);
            int cloumn = 7;
            for (Map<Integer,String> info : infos) {
                for (int i = 1 ; i <= cloumn; i++){
                    if("String".equals(json.getString(String.valueOf(i)))){
                        ps.setString(i,info.get(i));
                    }
                    if("Double".equals(json.getString(String.valueOf(i)))){
                        ps.setDouble(i, Double.parseDouble(info.get(i)));
                    }
                    if("Date".equals(json.getString(String.valueOf(i)))){
                        ps.setDate(i, Date.valueOf(info.get(i)));
                    }
                }
                ps.addBatch();
            }
            ps.setMaxFieldSize(10000);
            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
            Instant end = Instant.now();
            System.out.println("WRITE in milliseconds : " + Duration.between(start, end).toMillis());
        } catch (Exception e) {
            e.printStackTrace();
            result = "执行失败！";
        }finally {
            if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

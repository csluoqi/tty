package com.yinhai.tty.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yinhai.tty.constant.PropertiesConst;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;


/**
 * 数据库连接以及执行sql语句
 * @author yuejun
 */
public class DataBaseConnUtil {
    /**
     * 获取数据库连接
     * @param jdbcUrl
     * @param username
     * @param password
     * @return conn 数据库连接
     * @throws Exception
     */
    public static Connection getConnection(String jdbcUrl,String username,String password,String driver) throws Exception{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("启动ORACLE驱动失败！");
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("不能获取该数据库连接！");
        }
        return conn;
    }
    /**
     * 关闭连接 conn
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭连接 ps
     * @param ps
     */
    public static void closePS(PreparedStatement ps){
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接 rs
     * @param rs
     */
    public static void closeRS(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行sql
     * @param conn 数据库连接
     * @param infos
     * @return
     */
    public static String execute(Connection conn, List<Map<Integer,String>> infos) throws SQLException {
        String result = "执行成功！";
        Instant start = Instant.now();
        PreparedStatement ps = null;
        conn.setAutoCommit(false);
        try {
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
                ps.close();
            }
            if(conn!=null){
                conn.close();
            }
        }
        return result;
    }
}

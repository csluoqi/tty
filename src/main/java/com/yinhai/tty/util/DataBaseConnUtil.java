package com.yinhai.tty.util;

import com.yinhai.tty.entity.InfoBean;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


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
     * @param infoBeans
     * @return
     */
    public static String execute(Connection conn, List<InfoBean> infoBeans) throws SQLException {
        String result = "执行成功！";
        Instant start = Instant.now();
        Statement state= null;
        conn.setAutoCommit(false);
        try {
            state = conn.createStatement();
            for (InfoBean infoBean : infoBeans) {
                StringBuffer sql = new StringBuffer();
                sql.append("INSERT INTO USERINFO VALUES ('");
                sql.append(infoBean.getName()).append("','");
                sql.append(infoBean.getSex()).append("','");
                sql.append(infoBean.getIdcard()).append("',to_date('");
                sql.append(infoBean.getBirthday()).append("','yyyy/MM/dd'),'");
                sql.append(infoBean.getType()).append("',");
                sql.append(infoBean.getBalance()).append(",'");
                sql.append(infoBean.getEmail()).append("')");

                state.addBatch(sql.toString());
            }
            state.setMaxFieldSize(10000);
            state.executeBatch();
            state.clearBatch();
            conn.commit();
            Instant end = Instant.now();
            System.out.println("Difference in milliseconds : " + Duration.between(start, end).toMillis());
        } catch (Exception e) {
            e.printStackTrace();
            result = "执行失败！";
        }finally {
            if(state!=null){
                state.close();
            }
            if(conn!=null){
                conn.close();
            }
        }
        return result;
    }
}

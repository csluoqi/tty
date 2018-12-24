package com.yinhai.tty.util;

import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.*;

public class YJtest {

    public static void main(String[] args){
        DBPoolConnectionUtil dbp = DBPoolConnectionUtil.getInstance();    //获取数据连接池单例
        DruidPooledConnection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dbp.getConnection();    //从数据库连接池中获取数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ps){
                    ps.close();
                }
                if (null != conn){
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

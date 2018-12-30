package com.yinhai.tty.thread.job;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author lq
 * 创建时间 2018/12/26 14:14
 **/
public class Druidtest {
    public void selectColumn() throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("Root123@");
            dataSource.setUrl("jdbc:mysql://127.0.0.1/data_center?characterEncoding=UTF-8");
            Connection connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from data_control_price LIMIT 10 ");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("merchant_code"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

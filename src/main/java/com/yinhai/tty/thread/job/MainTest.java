package com.yinhai.tty.thread.job;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.sql.SQLUtils;
import com.yinhai.tty.util.DBPoolConnectionUtil;

import java.io.File;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lq
 * 创建时间 2018/12/25 17:28
 **/
public class MainTest {
    public static void main(String[] args) throws SQLException {
        DruidPooledConnection connection = DBPoolConnectionUtil.getInstance().getConnection();
        String insertSql = "insert INTO USERINFO (name, sex, idcard, birthday, type, balance, email) VALUES (?,?,?,?,?,?,?)";
        Long startTime = System.currentTimeMillis();
        PreparedStatement pst = null;
        try {
            connection.setAutoCommit(false);
            pst = connection.prepareStatement(insertSql);
            for (int i = 0; i < 50000; i++) {
                pst.setObject(1,"1");
                pst.setObject(2,"2");
                pst.setObject(3,"3");
                pst.setObject(4,new java.sql.Date(System.currentTimeMillis()));
                pst.setObject(5,"5");
                pst.setObject(6,5.6);
                pst.setObject(7,"7");
                pst.addBatch();
            }
            pst.executeLargeBatch();
            connection.commit();// 提交事务
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }

        } finally {
            if(pst!=null){
                pst.close();
            }
            connection.close();

        }

        Long endTime  = System.currentTimeMillis();
        System.out.println("use time "+(endTime-startTime));
    }

    public static void main2(String[] args) throws SQLException {
        DruidPooledConnection connection = DBPoolConnectionUtil.getInstance().getConnection();
        StringBuffer sql = new StringBuffer("INSERT ALL ");
        connection.setAutoCommit(false);
        Long startTime = System.currentTimeMillis();
        StringBuffer insert = new StringBuffer();
        //PreparedStatement pst = (PreparedStatement) connection.prepareStatement();
        for (int i = 0; i < 10000; i++) {
            //sql.append("INTO USERINFO(name, sex, idcard, birthday, type, balance, email) VALUES (1,2,3,sysdate,4,5,6) ");
            insert.append("insert INTO USERINFO (name, sex, idcard, birthday, type, balance, email) VALUES (1,2,3,sysdate,4,5,6);");

        }
        //sql.append(" SELECT 1 FROM DUAL");

        // 执行批量更新
        //pst.execute();
        // 语句执行完毕，提交本事务
        connection.commit();


        Long endTime = System.currentTimeMillis();
        System.out.println("用时：" + (endTime - startTime));
       // pst.close();
        connection.close();
    }

    public static void main1(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        FileReader fileReader;// = new FileReader();
        TtyJob fileReaderJob = new FileReaderJob(null,null);

        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();
        int splitNum = 4;
        SplitUtil splitUtil = new SplitUtil(fileReaderJob,files,splitNum) {
            @Override
            public void doExecute(Serializable[] array) {

            }

            @Override
            public void afterDo() {

            }
        };

        splitUtil.splitArray();

    }
}

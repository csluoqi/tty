package com.yinhai.tty.thread;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.yinhai.tty.thread.job.TtyUtils;
import com.yinhai.tty.util.DBPoolConnectionUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferQueueDemo {
    static LinkedTransferQueue<List<String>> lnkTransQueue = new LinkedTransferQueue<>();
    public static void main(String[] args) {
        ExecutorService producerThreadPool = Executors.newCachedThreadPool();
        ExecutorService consumerThreadPool = Executors.newCachedThreadPool();

        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();

        for (int i = 0;i<5;i++){

            producerThreadPool.execute(new ReaderJob(files));

            consumerThreadPool.execute(new WriterJob());


        }
        producerThreadPool.shutdown();
        consumerThreadPool.shutdown();
    }
    static class ReaderJob implements Runnable{
        private File[] files;

        public ReaderJob(File[] filearray) {
            files = filearray;
        }

        /**
         * 读取文件放入队列
         * @param file
         */
        public  void read(File file) {
            Path path = Paths.get(file.toURI());
            try {
                List<String> lines = Files.readAllLines(path,Charset.forName("UTF-8"));
                int batchSize = 4096;
                List<List<String>> batchList = TtyUtils.splitListByBatch(lines, batchSize);
                batchList.forEach(batch->{
                    lnkTransQueue.put(batch);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            for(int i = 0; i < files.length; i++){
                read(files[i]);
            }
        }

    }
    static class WriterJob implements Runnable{

/*        public WriterJob(List<List<String>> lineList) {
            this.lineList = lineList;
        }*/

        public  void write(List<String> lines) throws SQLException {
            DruidPooledConnection connection = DBPoolConnectionUtil.getInstance().getConnection();
            /*String insertSql = "insert INTO USERINFO (name, sex, idcard, birthday, type, balance, email) VALUES (?,?,?,?,?,?,?)";
            Long startTime = System.currentTimeMillis();
            PreparedStatement pst = null;
            try {
                connection.setAutoCommit(false);
                pst = connection.prepareStatement(insertSql);
                String[] lineArray;
                for (int i = 0; i < lines.size(); i++) {
                    lineArray = lines.get(i).split(",");
                    pst.setObject(1,lineArray[0]);
                    pst.setObject(2,lineArray[1]);
                    pst.setObject(3,lineArray[2]);
                    pst.setDate(4,new java.sql.Date(DateUtils.parseDate(lineArray[3],new String[]{"yyyy-mm-dd"}).getTime()));
                    pst.setObject(5,lineArray[4]);
                    pst.setObject(6,lineArray[5]);
                    pst.setObject(7,lineArray[6]);
                    pst.addBatch();
                }
                pst.executeLargeBatch();*/


            String insertSql = "insert INTO USERINFO (name, sex, idcard, birthday, type, balance, email) VALUES (?,?,?,?,?,?,?)";
            Long startTime = System.currentTimeMillis();
            PreparedStatement pst = null;
            try {
                connection.setAutoCommit(false);
                pst = connection.prepareStatement(insertSql);
                for (int i = 0; i < 100000; i++) {
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

/*            } catch (ParseException e) {
                e.printStackTrace();*/
            } finally {
                if(pst!=null){
                    pst.close();
                }
                connection.close();

            }

            Long endTime  = System.currentTimeMillis();
            System.out.println("use time "+(endTime-startTime));
        }
        @Override
        public void run() {

            while(true){
                List<String> lines = lnkTransQueue.poll();
                if (lines!=null){
                    System.out.println(Thread.currentThread().getName()+lines.size());
                    try {
                        write(lines);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }else{
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
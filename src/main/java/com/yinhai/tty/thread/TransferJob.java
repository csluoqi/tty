package com.yinhai.tty.thread;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.yinhai.tty.thread.job.TtyUtils;
import com.yinhai.tty.util.DBPoolConnectionUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class TransferJob {
   // public static ConcurrentLinkedQueue<List<String>> queue = new ConcurrentLinkedQueue<List<String>>();
    //public static  List<List<String>> queue = Collections.synchronizedList(new ArrayList<List<String>>());
    static LinkedTransferQueue<List<String>> queue = new LinkedTransferQueue<>();
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ExecutorService producerThreadPool = Executors.newCachedThreadPool();
        ExecutorService consumerThreadPool = Executors.newCachedThreadPool();

        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();

        List<Serializable[]> split = TtyUtils.splitArray(files, 2);
        split.forEach(fileSplit->{
            producerThreadPool.execute(new ReaderJob((File[]) fileSplit));
        });

        System.out.println("before reader");



        consumerThreadPool.execute(new WriterJob( producerThreadPool));
        consumerThreadPool.execute(new WriterJob( producerThreadPool));
        consumerThreadPool.execute(new WriterJob( producerThreadPool));
        System.out.println(" reade end start write");

        System.out.println("after writer");

     /*   for (int i = 0;i<2;i++){


        }*/
        producerThreadPool.shutdown();
        consumerThreadPool.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("use time in all "+(endTime-startTime)/6000);
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
            System.out.println("reader "+Thread.currentThread().getName());
            Path path = Paths.get(file.toURI());
            try {
                List<String> lines = Files.readAllLines(path,Charset.forName("UTF-8"));
                int batchSize = 30720;
                List<List<String>> batchList = TtyUtils.splitListByBatch(lines, batchSize);
                batchList.forEach(batch->{

                    //queue.put(batch);
                    try {
                        queue.transfer(batch);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            System.out.println("readjob run");
            for(int i = 0; i < files.length; i++){
                read(files[i]);
            }
           // Thread.currentThread().interrupt();
        }

    }
    static class WriterJob implements Runnable{
        ExecutorService producerThreadPool;
        public WriterJob(ExecutorService producerThreadPool){
            this.producerThreadPool = producerThreadPool;
        }
/*        public WriterJob(List<List<String>> lineList) {
            this.lineList = lineList;
        }*/

        public  void write(List<String> lines) throws SQLException {

            DruidPooledConnection connection = DBPoolConnectionUtil.getInstance().getConnection();
            String insertSql = "insert INTO USERINFO (name, sex, idcard, birthday, type, balance, email) VALUES (?,?,?,?,?,?,?)";
            Long startTime = System.currentTimeMillis();
            PreparedStatement pst = null;
            try {
                connection.setAutoCommit(false);
                pst = connection.prepareStatement(insertSql);
                    for (String line : lines) {
                        String[] lineArr = line.split(",");
                        pst.setObject(1, lineArr[0]);
                        pst.setObject(2, lineArr[1]);
                        pst.setObject(3, lineArr[2]);
                        pst.setObject(4, new java.sql.Date(System.currentTimeMillis()));
                        pst.setObject(5, lineArr[4]);
                        pst.setObject(6, lineArr[5]);
                        pst.setObject(7, lineArr[6]);
                        pst.addBatch();
                    }
                    System.out.println("writer "+Thread.currentThread().getName());
                    pst.executeBatch();
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
            System.out.println("writejob run");
            System.out.println(producerThreadPool.isTerminated());

            System.out.println("1111111111 "+(!producerThreadPool.isTerminated()&&queue.size()!=0));
            while(true){
                List<String> lines = queue.poll();
                if (lines!=null){
                    try {
                        write(lines);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(producerThreadPool.isTerminated()&&queue.isEmpty()){
                    break;
                }
            }
            Thread.currentThread().interrupt();
        }

    }
}
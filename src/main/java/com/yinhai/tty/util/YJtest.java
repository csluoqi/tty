package com.yinhai.tty.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.yinhai.tty.thread.ReadFileThread;
import com.yinhai.tty.thread.WriteDataBaseThread;

import java.io.File;
import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

public class YJtest {

    public static void main(String[] args){
        Instant start = Instant.now();
        BlockingQueue queue = new ArrayBlockingQueue(1024);
        File file = new File("E:/JAVA/Workspaces/Idea/test1");
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        Connection conn = null;
        try {
            conn = DBPoolConnectionUtil.getInstance().getConnection();
            ReadFileThread th1 = new ReadFileThread(file,1,2,"r1",conn,queue);
            ReadFileThread th2 = new ReadFileThread(file,3,4,"r2",conn,queue);
            ReadFileThread th3 = new ReadFileThread(file,5,6,"r3",conn,queue);
            ReadFileThread th4 = new ReadFileThread(file,7,8,"r4",conn,queue);
            ReadFileThread th5 = new ReadFileThread(file,9,10,"r5",conn,queue);
            ReadFileThread th6 = new ReadFileThread(file,11,12,"r6",conn,queue);
            ReadFileThread th7 = new ReadFileThread(file,13,14,"r7",conn,queue);
            ReadFileThread th8 = new ReadFileThread(file,15,16,"r8",conn,queue);

            WriteDataBaseThread w1 = new WriteDataBaseThread(conn,"w1",queue,0,1);
            WriteDataBaseThread w2 = new WriteDataBaseThread(conn,"w2",queue,2,3);

            threadPool.submit(th1);
            threadPool.submit(th2);
            threadPool.submit(th3);
            threadPool.submit(th4);
            threadPool.submit(th5);
            threadPool.submit(th6);
            threadPool.submit(th7);
            threadPool.submit(th8);

            while(true){
                while(queue.size()>0){
                    threadPool.submit(w1);
                    threadPool.submit(w2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(threadPool!=null){
                threadPool.shutdown();
            }
        }
        Instant end = Instant.now();
        System.out.println("milliseconds_ : " + Duration.between(start, end).toMillis());
    }
}

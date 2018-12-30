package com.yinhai.tty.thread;

import com.yinhai.tty.thread.job.FileReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lq
 * 创建时间 2018/12/25 11:13
 **/
public class TestCachedThreadPool {

    public static void main(String[] args) {
        //把数字分成n份，返回每份的起始和结束的下表
        int n = 4;//期望分为四份
        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();
        //求步长
        int step = files.length/n+1;
        if(files.length/n==0){
            step = files.length;
        }
        int index = 0;
        int end = 0;

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < n; i++) {
            end = index + step;
            if(end>files.length){
                end = files.length;
            }
            if(index==end){
                break;
            }
            File[] filesTmp = Arrays.copyOfRange(files, index, end);
            System.out.println(Arrays.toString(filesTmp));
            index = end;

            exec.execute(new FileReader(filesTmp));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();



    }

    public static void main1(String[] args) throws InterruptedException {
        //获取文件列表
        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();

        File[] tempFiles = null;

        int index=0;
        int step = files.length/5;
        //多线程读取
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            File[] filesTmp = Arrays.copyOfRange(files, index, index + step);

            exec.execute(new FileReader(filesTmp));
            Thread.sleep(10);
        }
        exec.shutdown();
    }

}

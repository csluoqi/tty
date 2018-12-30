package com.yinhai.tty.thread.job;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 切分工具
 * @author lq
 * 创建时间 2018/12/25 15:47
 **/
public abstract class SplitUtil {
    TtyJob ttyjob;
    Serializable[] array;
    int splitNum;

    public SplitUtil(TtyJob ttyjob,Serializable[] array,int splitNum) {
        this.ttyjob = ttyjob;
        this.array = array;
        this.splitNum = splitNum;
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        String path = "D:\\test\\test";
        File file = new File(path);
        File[] files = file.listFiles();
        TtyJob ttyjob = new TtyJob(){
            ExecutorService exec = Executors.newCachedThreadPool();
            @Override
            public void doExecute() {

            }
        };
        int splitNum = 4;
        SplitUtil splitUtil = new SplitUtil(ttyjob,files,splitNum) {
            @Override
            public void doExecute(Serializable[] array) {
                exec.execute(new FileReader(files));
            }
            @Override
            public void afterDo() {

            }
        };
        splitUtil.splitArray();

    }

    public void splitArray(){
        //把数字分成n份，返回每份的起始和结束的下表
        // ;//期望分为四份
        splitNum = splitNum<=0 ? 0 : splitNum;
        String path = "D:\\test\\test";
        File file = new File(path);
        //File[] files = file.listFiles();
        //求步长
        int step = array.length/splitNum+1;
        if(array.length/splitNum==0){
            step = array.length;
        }
        int index = 0;
        int end = 0;
        for (int i = 0; i < splitNum; i++) {
            end = index + step;
            if(end>array.length){
                end = array.length;
            }
            if(index==end){
                break;
            }
            Serializable[] filesTmp = Arrays.copyOfRange(array, index, end);
            System.out.println(Arrays.toString(filesTmp));
            index = end;
            //executor.execute(new FileReader());
            doExecute(array);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        afterDo();
        //executor.shutdown();
    }

    public abstract void  doExecute(Serializable[] array);
    public abstract void  afterDo();

}

package com.yinhai.tty.thread;

import com.yinhai.tty.entity.InfoBean;
import com.yinhai.tty.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 开启读文件线程(带返回值)
 * Created by yuejun on 2018/12/21.
 */
public class ReadFileThread implements Callable {

    File file;

    public ReadFileThread(File file) {
        this.file = file;
    }

    /**
     * 带返回值的线程
     * @return List<InfoBean> infoBeans
     * @throws Exception
     */
    @Override
    public List<Map<Integer,String>> call() throws Exception {
        List<Map<Integer,String>> infos = new ArrayList<>();
        try{
            Instant start = Instant.now();
            if(file.isDirectory()){
                File[] filelist = file.listFiles();
                for(int i =0; i < filelist.length; i++){
                    infos.addAll(readFile(filelist[i]));
                }
            }
            if(!file.isDirectory()){
                infos = readFile(file);
            }
            Instant end = Instant.now();
            System.out.println("READ in milliseconds : " + Duration.between(start, end).toMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    /**
     * 读文件
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<Integer,String>> readFile(File file) throws Exception {
        String encoding = "GBK";
        List<Map<Integer,String>> infos = new ArrayList<>();
        if (file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] s=lineTxt.split(",");
                Map<Integer,String> info = new HashMap<>();
                for(int i = 1; i <= s.length; i++){
                    info.put(i,s[i-1]);
                }
                infos.add(info);
            }
        }
        return infos;
    }
}

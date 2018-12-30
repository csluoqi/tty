package com.yinhai.tty.thread.job;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lq
 * 创建时间 2018/12/24 23:30
 **/
public class FileReader implements Runnable {
    private File[] files;

    public FileReader(File[] files) {
        this.files = files;
    }

    public static void main(String[] args) throws IOException {
        //read("D:\\test\\test\\91.txt");
    }
    public static void read(File file) {
            Path path = Paths.get(file.toURI());
            try {
                List<String> lines = Files.readAllLines(path,Charset.forName("UTF-8"));
                lines.forEach(str -> {
                    System.out.println(Thread.currentThread().getName()+" "+str);
                }  );
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

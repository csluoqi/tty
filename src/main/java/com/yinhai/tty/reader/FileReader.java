package com.yinhai.tty.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lq
 * 创建时间 2018/12/24 23:30
 **/
public class FileReader {
    public static void main(String[] args) throws IOException {
        read("D:\\test\\test\\91.txt");
    }
    public static void read(String filePath) throws IOException {
            StringBuffer xml = new StringBuffer();
            Path path = Paths.get(filePath);
            try {
                List<String> lines = Files.readAllLines(path,Charset.forName("UTF-8"));
                lines.forEach(str -> {
                    System.out.println(str);
                }  );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


}

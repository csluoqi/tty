package com.yinhai.tty.reader;

import com.sun.org.apache.xpath.internal.operations.String;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lq
 * 创建时间 2018/12/24 23:03
 **/
public class NioReader {

    public static void main(String[] args) throws IOException {
        readByChannel();
    }
    private static long readByChannel() throws FileNotFoundException, IOException {
        long counts = 0;
        File file = new File("D:\\test\\test\\1.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bbuf = ByteBuffer.allocate(2048);
        int offset = 0;
        while((offset = fc.read(bbuf)) != -1) {
            counts = counts + offset;
            bbuf.clear();
        }
        fc.close();
        fis.close();
        return counts;
    }

}

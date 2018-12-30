package com.yinhai.tty.thread.job;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author lq
 * 创建时间 2018/12/25 18:05
 **/
public class TtyUtils {
    public static List<Serializable[]> splitArray(Serializable[] array, int splitNum) {
        //参数验证
        List<Serializable[]> list = new ArrayList<>();
        splitNum = splitNum <= 1 ? 1 : splitNum;
        int step = array.length / splitNum;
        if(array.length % splitNum != 0){//数组长度和切分数一样
            step = step+1;
        }
        if(array.length < splitNum){
            splitNum = array.length;
        }
        int index;
        int end;
        Serializable[] tmpArray = null;
        int i = 0;
        for (; i < splitNum; i++) {
            index = step * i;
            end = step * (i + 1);
            //最后一个加载剩余所有的
            if(i==splitNum-1){
                tmpArray = Arrays.copyOfRange(array, index, array.length);
            }else{
                tmpArray = Arrays.copyOfRange(array, index, end);
            }
            list.add(tmpArray);
        }
        return list;
    }

    public static <T> List<List<T>> splitList(List<T> list, int splitNum) {
        List<List<T>> resultList = new ArrayList<>();
        int step = list.size() / splitNum;
        if(list.size() % splitNum != 0){//数组长度和切分数一样
            step = step+1;
        }
        if(list.size() < splitNum){
            splitNum = list.size();
        }
        int index;
        int end;
        List<T> tempList;
        for (int i = 0; i < splitNum; i++) {
            index = step * i;
            end = step * (i + 1);
            //最后一个加载剩余所有的
            if(i==splitNum-1){
                //tmpArray = Arrays.copyOfRange(array, index, array.length);
                tempList = list.subList(index, list.size());
            }else{
                //tmpArray = Arrays.copyOfRange(array, index, end);
                tempList = list.subList(index, end);
            }
            resultList.add(tempList);
        }
        return resultList;
    }

    public static <T> List<List<T>> splitListByBatch(List<T> list, int batchSize) {
        List<List<T>> resultList = new ArrayList<>();
        int splitNum = list.size() / batchSize;
        splitNum = splitNum < 1 ? 1 : (list.size() % batchSize == 0 ? splitNum : splitNum + 1);
        batchSize = batchSize < list.size() ? batchSize : list.size();
        int index;
        int end;
        List<T> tempList;
        for (int i = 0; i < splitNum; i++) {
            index = i * batchSize;
            end = batchSize * (i + 1);
            System.out.println(i);
            if (end > list.size()) {
                end = list.size();
            }
            tempList = list.subList(index, end);
            //tmpArray = Arrays.copyOfRange(array, index, end);
            resultList.add(tempList);
            index = end;
        }
        return resultList;
    }

    public static void main(String[] args) {

        String path = "D:\\test\\test";
        File file = new File(path);
        String[] files = new String[]{"1", "2", "3","4", "5","6","7"};
        //String[] fileArray = new String[]{"1", "2", "3","4", "5","6"};
       // List<String> files = Arrays.asList(fileArray);
       // System.out.println(files.size());
        List<String> fileList = Arrays.asList(files);

        List<List<String>> lists = splitListByBatch(fileList, 3);
        lists.forEach(list -> {
            System.out.println(list.toString());
        });

//        List<List<String>> serializables = splitList(files, 3);
//        serializables.forEach(s->{
//            System.out.println(s);
//        });

    }
}

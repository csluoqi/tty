package com.yinhai.tty.thread;
  
import org.junit.Test;  
  
import java.util.*;  
import java.util.concurrent.*;  
  
/** 
 * Created with IntelliJ IDEA. 
 * User: yangzl2008 
 * Date: 14-9-18 
 * Time: 下午8:36 
 * To change this template use File | Settings | File Templates. 
 */  
public class Test02 {  
  
    private int NUM = 10000;  
    private int THREAD_COUNT = 16;  
  
    @Test  
    public void testAdd() throws Exception {  
        List<Integer> list1 = new CopyOnWriteArrayList<Integer>();  
        List<Integer> list2 = Collections.synchronizedList(new ArrayList<Integer>());  
        Vector<Integer> v  = new Vector<Integer>();  
  
        CountDownLatch add_countDownLatch = new CountDownLatch(THREAD_COUNT);  
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);  
  
        int add_copyCostTime = 0;  
        int add_synchCostTime = 0;  
        for (int i = 0; i < THREAD_COUNT; i++) {  
            add_copyCostTime += executor.submit(new AddTestTask(list1, add_countDownLatch)).get();  
        }  
        System.out.println("CopyOnWriteArrayList add method cost time is " + add_copyCostTime);  
  
        for (int i = 0; i < THREAD_COUNT; i++) {  
            add_synchCostTime += executor.submit(new AddTestTask(list2, add_countDownLatch)).get();  
        }  
        System.out.println("Collections.synchronizedList add method cost time is " + add_synchCostTime);  
  
  
    }  
  
    @Test  
    public void testGet() throws Exception {  
        List<Integer> list = initList();  
  
        List<Integer> list1 = new CopyOnWriteArrayList<Integer>(list);  
        List<Integer> list2 = Collections.synchronizedList(list);  
  
        int get_copyCostTime = 0;  
        int get_synchCostTime = 0;  
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);  
        CountDownLatch get_countDownLatch = new CountDownLatch(THREAD_COUNT);  
        for (int i = 0; i < THREAD_COUNT; i++) {  
            get_copyCostTime += executor.submit(new GetTestTask(list1, get_countDownLatch)).get();  
        }  
        System.out.println("CopyOnWriteArrayList add method cost time is " + get_copyCostTime);  
  
        for (int i = 0; i < THREAD_COUNT; i++) {  
            get_synchCostTime += executor.submit(new GetTestTask(list2, get_countDownLatch)).get();  
        }  
        System.out.println("Collections.synchronizedList add method cost time is " + get_synchCostTime);  
  
    }  
  
  
    private List<Integer> initList() {  
        List<Integer> list = new ArrayList<Integer>();  
        int num = new Random().nextInt(1000);  
        for (int i = 0; i < NUM; i++) {  
            list.add(num);  
        }  
        return list;  
    }  
  
    class AddTestTask implements Callable<Integer> {  
        List<Integer> list;  
        CountDownLatch countDownLatch;  
  
        AddTestTask(List<Integer> list, CountDownLatch countDownLatch) {  
            this.list = list;  
            this.countDownLatch = countDownLatch;  
        }  
  
        @Override  
        public Integer call() throws Exception {
            System.out.println("AddTestTask "+Thread.currentThread().getName()+System.currentTimeMillis());
            int num = new Random().nextInt(1000);  
            long start = System.currentTimeMillis();  
            for (int i = 0; i < NUM; i++) {  
                list.add(num);  
            }  
            long end = System.currentTimeMillis();  
            countDownLatch.countDown();  
            return (int) (end - start);  
        }  
    }  
  
    class GetTestTask implements Callable<Integer> {  
        List<Integer> list;  
        CountDownLatch countDownLatch;  
  
        GetTestTask(List<Integer> list, CountDownLatch countDownLatch) {  
            this.list = list;  
            this.countDownLatch = countDownLatch;  
        }  
  
        @Override  
        public Integer call() throws Exception {
            System.out.println("GetTestTask "+Thread.currentThread().getName()+System.currentTimeMillis());
            int pos = new Random().nextInt(NUM);  
            long start = System.currentTimeMillis();  
            for (int i = 0; i < NUM; i++) {  
                list.get(pos);  
            }  
            long end = System.currentTimeMillis();  
            countDownLatch.countDown();  
            return (int) (end - start);  
        }  
    }  
}  
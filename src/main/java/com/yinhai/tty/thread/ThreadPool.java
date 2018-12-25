package com.yinhai.tty.thread;

import java.util.concurrent.*;

public class ThreadPool {

    private ThreadPoolExecutor threadPool;
    private String threadName = "new";

    private void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * 构造线程池（默认初始大小为1，默认为无容量阻塞队列，超出最大值后将阻塞提交）
     *
     * @param maxPoolSize
     *            线程池最大值。
     *
     */
    public ThreadPool(int maxPoolSize) {
        threadPool = new ThreadPoolExecutor(1, maxPoolSize, 0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                new CustomThreadFactory(), new CustomRejectedExecutionHandler());

    }

    /**
     * 提交任务
     *
     * @param callable
     *            节点执行类
     * @throws Exception
     */
    public synchronized void submitProc(String procThreadName,Callable callable ) throws Exception {
        if (threadPool == null) {
            throw new Exception("线程池未初始化！不能提交。");
        }
        this.setThreadName(procThreadName);
        threadPool.submit(callable);
    }
    public int getActiveThreadNum() {
        return this.threadPool.getActiveCount();
    }

    /**
     * 关闭线程池
     */
    public void close() {
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }

    // 线程创建工厂类
    private class CustomThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(threadName);
            return t;
        }
    }

    // 提交线程超出最大值处理类
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);// 超出最大线程数后再提交任务会被阻塞。
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

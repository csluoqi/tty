package com.yinhai.tty.thread.job;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lq
 * 创建时间 2018/12/25 17:24
 **/
public class FileReaderJob  implements  TtyJob{
    ExecutorService exec ;
    FileReader fileReader;

    public FileReaderJob(ExecutorService exec,FileReader fileReader) {
        this.exec = exec;
        this.fileReader = fileReader;
    }

    @Override
    public void doExecute() {
        exec.execute(fileReader);
    }
}

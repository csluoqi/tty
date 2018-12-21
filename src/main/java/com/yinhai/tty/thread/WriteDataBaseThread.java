package com.yinhai.tty.thread;

import com.yinhai.tty.entity.InfoBean;
import com.yinhai.tty.util.DataBaseConnUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 开启写入数据库线程(带返回值)
 * Created by yuejun on 2018/12/21.
 */
public class WriteDataBaseThread implements Callable {

    Connection conn;
    List<Map<Integer,String>> infos;

    public WriteDataBaseThread(Connection conn, List<Map<Integer,String>> infos) {
        this.conn = conn;
        this.infos = infos;
    }

    @Override
    public String call() throws Exception {
        String result = DataBaseConnUtil.execute(conn,infos);
        return result;
    }
}

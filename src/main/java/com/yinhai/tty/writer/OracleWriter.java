package com.yinhai.tty.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lq
 * 创建时间 2018/12/26 17:06
 **/
@Repository
public class OracleWriter implements Writer {

    @Autowired
    @Qualifier("oracleJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public void wirter(List<String> lines) {
        this.jdbcTemplate.queryForList("select * from student ");
    }





}

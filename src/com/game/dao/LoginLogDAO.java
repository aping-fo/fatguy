package com.game.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.Date;

@DAO
public interface LoginLogDAO {

    @SQL("insert into t_log_login(openid,loginTime) values(:openid,:loginTime)")
    public void insert(@SQLParam("openid") String openid,@SQLParam("loginTime")Date loginTime);

}

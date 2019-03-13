package com.game.dao;

import com.game.domain.consume.Consume;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

@DAO
public interface ConsumeDAO {

    @SQL("select * from t_u_consume where openId = :openId and itemId = :itemId")
    public Consume queryConsume(@SQLParam("openId") String openId,@SQLParam("itemId") int itemId);

    @SQL("insert into t_u_consume(openId,name,phone,address,itemId) values(:consume.openId,:consume.name,:consume.phone,:consume.address,:consume.itemId)")
    public void insert(@SQLParam("consume") Consume consume);
    @SQL("REPLACE INTO t_u_consume VALUES (:consume.openId,:consume.name,:consume.phone,:consume.address,:consume.itemId)")
    public void saveOrUpdate(@SQLParam("consume") Consume consume);

}

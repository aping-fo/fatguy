package com.game.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

@DAO
public interface ServerDataDAO {

    @SQL("select server_data from t_u_serverdata where id = 888")
    public byte[] queryServerData();

    @SQL("REPLACE INTO t_u_serverdata VALUES (:id,:data)")
    public void saveOrUpdate(@SQLParam("id") int id,@SQLParam("data") byte[] data);
}

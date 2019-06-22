package com.game.dao;

import com.game.domain.player.Player;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

@DAO
public interface PlayerDAO {

    @SQL("select * from t_u_player where openId = :openId")
    public Player queryPlayer(@SQLParam("openId") String openId);

    @SQL("insert into t_u_player(openId,nickName,avatarUrl,createTime,loginTime,stealTimes,playerData) values(:player.openId,:player.nickName,:player.avatarUrl,:player.createTime,:player.loginTime,:player.stealTimes,:player.playerData)")
    public void insert(@SQLParam("player") Player player);

    @SQL("REPLACE INTO t_u_player VALUES (:player.openId,:player.nickName,:player.avatarUrl,:player.createTime,:player.loginTime,:player.stealTimes,:player.playerData)")
    public void saveOrUpdate(@SQLParam("player") Player player);

    @SQL("select count(1) from t_u_player")
    public Integer selectPlayerCount();

    @SQL("SELECT openId,nickName,avatarUrl,stealTimes,playerData FROM t_u_player ORDER BY stealTimes DESC")
    public List<Player> queryAllPlayer();

    @SQL("SELECT openId FROM t_u_player")
    public List<String> queryAllPlayersOpenId();
}

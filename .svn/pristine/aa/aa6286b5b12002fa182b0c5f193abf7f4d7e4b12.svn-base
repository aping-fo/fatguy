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

    @SQL("insert into t_u_player(openId,nickName,level,step,iconUrl,createTime,loginTime,groupId,signTime,totalStep,todayTransStep) values(:player.openId,:player.nickName,:player.level,:player.step,:player.iconUrl,:player.createTime,:player.loginTime,:player.groupId,:player.signTime,:player.totalStep,:player.todayTransStep)")
    public void insert(@SQLParam("player") Player player);

    @SQL("REPLACE INTO t_u_player VALUES (:player.openId,:player.nickName,:player.level,:player.step,:player.iconUrl,:player.createTime,:player.loginTime,:player.groupId,:player.signTime,:player.totalStep,:player.todayTransStep)")
    public void saveOrUpdate(@SQLParam("player") Player player);

    @SQL("SELECT openId, nickName, level, totalStep FROM t_u_player ORDER BY totalStep DESC LIMIT 100")
    public List<Player> queryPlayerRank();

    @SQL("select count(1) from t_u_player")
    public Integer selectPlayerCount();

}

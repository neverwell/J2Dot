package com.nemo.game.data.mysql.mapper;

import com.nemo.common.jdbc.RowMapper;
import com.nemo.game.entity.RoleRank;
import com.nemo.game.system.rank.constant.RankField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RankMapper implements RowMapper<RoleRank>{
	@Override
    public RoleRank mapping(ResultSet rs) throws SQLException {
        RoleRank roleRank = new RoleRank();
        roleRank.setId(rs.getLong(RankField.UID));
        roleRank.setName(rs.getString(RankField.ROLE_NAME));
        roleRank.setRoleLevel(rs.getInt(RankField.ROLE_LEVEL));
        roleRank.setRoleRein(rs.getInt(RankField.ROLE_REIN));
        roleRank.setRoleExp(rs.getLong(RankField.ROLE_EXP));
        roleRank.setSex(rs.getInt(RankField.SEX));
        roleRank.setCareer(rs.getInt(RankField.CAREER));
        roleRank.setFightPower(rs.getLong(RankField.FIGHT_POWER));
        roleRank.setHeroFightPower(rs.getLong(RankField.HERO_FIGHT_POWER));
        roleRank.setJunxian(rs.getInt(RankField.JUN_XIAN));
        roleRank.setHonor(rs.getLong(RankField.HONOR));
        roleRank.setWingFightPower(rs.getInt(RankField.WING_FIGHT_POWER));
        roleRank.setWeiwang(rs.getInt(RankField.WEIWANG));
        roleRank.setBarrier(rs.getInt(RankField.BARRIER));
        roleRank.setSearchPk(rs.getInt(RankField.SEARCH_PK));
        roleRank.setVipLevel(rs.getInt(RankField.ROLE_VIP));
        roleRank.setLastLoginTime(rs.getInt(RankField.LAST_LOGIN_TIME));
        return roleRank;
    }

	@Override
	public void release() {
	}
}

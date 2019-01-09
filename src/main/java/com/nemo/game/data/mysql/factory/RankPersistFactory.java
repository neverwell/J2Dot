package com.nemo.game.data.mysql.factory;

import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.entity.RoleRank;

//排行榜数据持久化工厂
public class RankPersistFactory implements PersistFactory {

	private static final String INSERT = "INSERT INTO s_rank (uid, name, roleLevel, roleRein, roleExp, sex, career, " +
			"fightPower,heroFightPower,junxian,honor,wingFightPower,weiwang,barrier,searchPk,vipLevel,weiwangLevel,lastLoginTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE = "update s_rank set roleLevel = ?, roleRein = ?, roleExp = ?,fightPower = ?,heroFightPower = ?,junxian = ?,honor = ?,wingFightPower = ?,weiwang = ?, barrier = ?, searchPk = ?, vipLevel = ?, weiwangLevel = ?,lastLoginTime = ? where uid = ?";

	private static final String DELETE = "delete from s_rank where id = ?";

	@Override
	public String name() {
		return "排行榜";
	}

	@Override
	public int dataType() {
		return DataType.RANK;
	}

	@Override
	public String createInsertSql() {
		return INSERT;
	}

	@Override
	public String createUpdateSql() {
		return UPDATE;
	}

	@Override
	public String createDeleteSql() {
		return DELETE;
	}

	@Override
	public Object[] createInsertParameters(Persistable obj) {
		RoleRank rank = (RoleRank) obj;
		return new Object[]{
				rank.getId(),
				rank.getName(),
				rank.getRoleLevel(),
				rank.getRoleRein(),
				rank.getRoleExp(),
				rank.getSex(),
				rank.getCareer(),
				rank.getFightPower(),
				rank.getHeroFightPower(),
				rank.getJunxian(),
				rank.getHonor(),
				rank.getWingFightPower(),
				rank.getWeiwang(),
				rank.getBarrier(),
				rank.getSearchPk(),
				rank.getVipLevel(),
				rank.getWeiwangLevel(),
				rank.getLastLoginTime()
		};
	}

	@Override
	public Object[] createUpdateParameters(Persistable obj) {
		RoleRank rank = (RoleRank) obj;
		return new Object[]{
				rank.getRoleLevel(),
				rank.getRoleRein(),
				rank.getRoleExp(),
				rank.getFightPower(),
				rank.getHeroFightPower(),
				rank.getJunxian(),
				rank.getHonor(),
				rank.getWingFightPower(),
				rank.getWeiwang(),
				rank.getBarrier(),
				rank.getSearchPk(),
				rank.getVipLevel(),
				rank.getWeiwangLevel(),
				rank.getLastLoginTime(),
				rank.getId()};
	}

	@Override
	public Object[] createDeleteParameters(Persistable obj) {
		return new Object[]{obj.getId()};
	}

	@Override
	public long taskPeriod() {
		return 60 * 1000;
	}
}

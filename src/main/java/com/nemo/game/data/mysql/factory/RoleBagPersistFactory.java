package com.nemo.game.data.mysql.factory;

import com.nemo.common.jdbc.SerializerUtil;
import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.entity.RoleBag;
import lombok.extern.slf4j.Slf4j;

//背包 数据持久化工具
@Slf4j
public class RoleBagPersistFactory implements PersistFactory {
	private static final String INSERT = "insert into p_bag (id, data) values (?, ?)";

	private static final String UPDATE = "update p_bag set data = ? where id = ?";

	private static final String DELETE = "delete from p_bag where id = ?";

	@Override
	public String name() {
		return null;
	}

	@Override
	public int dataType() {
		return DataType.BAG;
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
		RoleBag roleBag = (RoleBag) obj;
		byte[] bytes = null;
		int retry = 0;
		while (retry < 10) {
			try {
				bytes = SerializerUtil.encode(roleBag, RoleBag.class);
				break;
			} catch (Throwable e) {
				retry++;
				log.error("数据" + roleBag.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
			}
		}
		if (retry > 1 && retry < 10) {
			log.error("数据{}入库序列化重试次数->{}", roleBag.getId(), retry);
		} else if (retry == 10) {
			log.error("数据{}入库失败", roleBag.getId());
		}
		if (bytes == null) {
			log.error("Protostuff编码错误,data:{}" + roleBag.getId());
			return null;
		}

		return new Object[]{obj.getId(), bytes};
	}

	@Override
	public Object[] createUpdateParameters(Persistable obj) {
		RoleBag roleBag = (RoleBag) obj;
		byte[] bytes = null;
		int retry = 0;
		while (retry < 10) {
			try {
				bytes = SerializerUtil.encode(roleBag, RoleBag.class);
				break;
			} catch (Throwable e) {
				retry++;
				log.error("数据" + roleBag.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
			}
		}
		if (retry > 1 && retry < 10) {
			log.error("数据{}入库序列化重试次数->{}", roleBag.getId(), retry);
		} else if (retry == 10) {
			log.error("数据{}入库失败", roleBag.getId());
		}
		if (bytes == null) {
			log.error("Protostuff编码错误,data:{}" + roleBag.getId());
			return null;
		}
		return new Object[]{bytes, obj.getId()};
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

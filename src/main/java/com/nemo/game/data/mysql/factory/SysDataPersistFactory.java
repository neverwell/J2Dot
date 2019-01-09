package com.nemo.game.data.mysql.factory;

import com.nemo.common.jdbc.SerializerUtil;
import com.nemo.common.persist.PersistFactory;
import com.nemo.common.persist.Persistable;
import com.nemo.game.data.DataType;
import com.nemo.game.entity.sys.AbstractSysData;
import lombok.extern.slf4j.Slf4j;

//系统数据持久化工厂
@Slf4j
public class SysDataPersistFactory implements PersistFactory {
	private static final String INSERT = "insert into s_data (id, data) values (?, ?)";

	private static final String UPDATE = "update s_data set data = ? where id = ?";

	private static final String DELETE = "delete from s_data where id = ?";

	@Override
	public String name() {
		return null;
	}

	@Override
	public int dataType() {
		return DataType.SYS;
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
	@SuppressWarnings("unchecked")
	public Object[] createInsertParameters(Persistable obj) {
		Class<AbstractSysData> clazz = (Class<AbstractSysData>) obj.getClass();
		AbstractSysData abstractSysData = (AbstractSysData) obj;
		byte[] bytes = null;
		int retry = 0;
		while (retry < 10) {
			try {
				bytes = SerializerUtil.encode(abstractSysData, clazz);
				break;
			} catch (Throwable e) {
				retry++;
				log.error("数据" + abstractSysData.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
			}
		}
		if (retry > 1 && retry < 10) {
			log.error("数据{}入库序列化重试次数->{}", abstractSysData.getId(), retry);
		} else if (retry == 10) {
			log.error("数据{}入库失败", abstractSysData.getId());
		}
		if (bytes == null) {
			log.error("Protostuff编码错误,data:{}" + abstractSysData.getId());
			return null;
		}
		return new Object[]{ obj.getId(), bytes };
	}

	@Override
	public Object[] createUpdateParameters(Persistable obj) {
		AbstractSysData abstractSysData = (AbstractSysData) obj;
		@SuppressWarnings("unchecked")
		Class<AbstractSysData> clazz = (Class<AbstractSysData>) obj.getClass();
		byte[] bytes = null;
		int retry = 0;
		while (retry < 10) {
			try {
				bytes = SerializerUtil.encode(abstractSysData, clazz);
				break;
			} catch (Throwable e) {
				retry++;
				log.error("数据" + abstractSysData.getId() + "入库序列化失败,进行重试，重试次数->" + retry, e);
			}
		}
		if (retry > 1 && retry < 10) {
			log.error("数据{}入库序列化重试次数->{}", abstractSysData.getId(), retry);
		} else if (retry == 10) {
			log.error("数据{}入库失败", abstractSysData.getId());
		}
		if (bytes == null) {
			log.error("Protostuff编码错误,data:{}" + abstractSysData.getId());
			return null;
		}
		return new Object[]{ bytes, obj.getId() };
	}

	@Override
	public Object[] createDeleteParameters(Persistable obj) {
		return new Object[]{ obj.getId() };
	}

	@Override
	public long taskPeriod() {
		return 60 * 1000;
	}
}

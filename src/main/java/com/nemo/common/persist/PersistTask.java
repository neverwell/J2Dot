package com.nemo.common.persist;

import com.nemo.common.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//定时执行增删改的持久化任务 如果不是立即执行的操作可以放到这里
public class PersistTask extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistTask.class);
    //批量操作限制数量 超过了就先执行一波
    private static final int BATCH_EXECUTE_MAX_SIZE = 300;
    //添加和执行时的锁
    private final Object LOCK = new Object();
    //所有需要执行的参数存放在这里，由PersistType获取对应语句来执行
    private Map<Long, PersistType> persistMap = new HashMap<>();
    //每一条执行语句里的参数集
    private LinkedList<Object[]> insertParams = new LinkedList();
    private LinkedList<Object[]> updateParams = new LinkedList();
    private LinkedList<Object[]> deleteParams = new LinkedList();
    //如果操作不成功 保存所有参数集 下一次再试图执行
    private List<Object[]> insertErrorParams = new LinkedList();
    private List<Object[]> updateErrorParams = new LinkedList();
    private List<Object[]> deleteErrorParams = new LinkedList();
    //从持久化工厂里获取sql语句
    private String insertSql;
    private String updateSql;
    private String deleteSql;
    //定义时传进来的JDBC模板 缓存 持久化工厂
    private JdbcTemplate template;
    private PersistableCache cache;
    private PersistFactory persistFactory;

    public PersistTask(JdbcTemplate template, PersistFactory persistFactory, PersistableCache cache) {
        this.template = template;
        this.cache = cache;
        this.persistFactory = persistFactory;
        this.insertSql = this.persistFactory.createInsertSql();
        this.updateSql = this.persistFactory.createUpdateSql();
        this.deleteSql = this.persistFactory.createDeleteSql();
    }

    public void add(Persistable obj, PersistType persistType) {
        if(obj != null) {
            synchronized (this.LOCK) {
                if(persistType == PersistType.INSERT) {
                    this.persistMap.put(obj.getId(), persistType);
                } else {
                    PersistType type;
                    if(persistType == PersistType.UPDATE) {
                        type = this.persistMap.get(obj.getId());
                        if(type == null) {
                            this.persistMap.put(obj.getId(), persistType);
                        }
                    } else {
                        if(persistType != PersistType.DELETE) {
                            throw new RuntimeException("不支持的数据持久化类型 dataid=" + obj.getId());
                        }

                        type = this.persistMap.get(obj.getId());
                        if(type != null && type == PersistType.INSERT) {
                            this.persistMap.remove(obj.getId());
                        } else {
                            this.persistMap.put(obj.getId(), PersistType.DELETE);
                        }
                    }
                }

                obj.setDirty(true);
            }
        }
    }

    @Override
    public void run() {
        try {
            //试图执行上次没成功的语句
            rescueErrorUpdate();

            Map<Long, PersistType> cloneMap = new HashMap<>();
            synchronized (this.LOCK) {
                cloneMap.putAll(this.persistMap);
                this.persistMap.clear();
            }

            Iterator<Map.Entry<Long, PersistType>> it = cloneMap.entrySet().iterator();
            while (it.hasNext()) {
                //要是超过了一定数量 先执行掉
                this.checkAndUpdate();
                Map.Entry<Long, PersistType> entry = it.next();
                if(entry != null) {
                    Long id = entry.getKey();
                    if(id != null) {
                        //通过主键从缓存中获取数据
                        Persistable data = this.cache.get(id.longValue());
                        if(data != null) {
                            PersistType type = entry.getValue();
                            if (type == PersistType.UPDATE) {
                                this.updateParams.add(this.persistFactory.createUpdateParameters(data));
                                data.setDirty(false);
                            } else if (type == PersistType.INSERT) {
                                this.insertParams.add(this.persistFactory.createInsertParameters(data));
                                data.setDirty(false);
                            } else if (type == PersistType.DELETE) {
                                this.cache.remove(id.longValue());
                                this.deleteParams.add(this.persistFactory.createDeleteParameters(data));
                            }
                        }
                    }
                }
            }
            //批量操作
            this.finallyUpdate();
        } catch (Throwable var8) {
            LOGGER.error("持久化任务执行失败", var8);
        }
    }

    private void rescueErrorUpdate() {
        if(!this.insertErrorParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.insertSql, this.insertErrorParams);
            } catch (Exception var4) {
                LOGGER.error(this.insertSql, var4);
            }
            this.insertErrorParams.clear();
        }

        if(!this.updateErrorParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.updateSql, this.updateErrorParams);
            } catch (Exception var3) {
                LOGGER.error(this.updateSql, var3);
            }
            this.updateErrorParams.clear();
        }

        if (!this.deleteErrorParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.deleteSql, this.deleteErrorParams);
            } catch (Exception var2) {
                LOGGER.error(this.deleteSql, var2);
            }

            this.deleteErrorParams.clear();
        }
    }

    private void checkAndUpdate() {
        if (this.insertParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            try {
                this.template.batchUpdate(this.insertSql, this.insertParams);
            } catch (Exception var4) {
                LOGGER.error("插入数据库失败，请立马重启");
                LOGGER.error(this.insertSql, var4);
                this.insertErrorParams.addAll(this.insertParams);
            }

            this.insertParams.clear();
        }

        if (this.updateParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            try {
                this.template.batchUpdate(this.updateSql, this.updateParams);
            } catch (Exception var3) {
                LOGGER.error("更新数据库失败，请立马重启");
                LOGGER.error(this.updateSql, var3);
                this.updateErrorParams.addAll(this.updateParams);
            }

            this.updateParams.clear();
        }

        if (this.deleteParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            try {
                this.template.batchUpdate(this.deleteSql, this.deleteParams);
            } catch (Exception var2) {
                LOGGER.error("删除数据库失败，请立马重启");
                LOGGER.error(this.deleteSql, var2);
                this.deleteErrorParams.addAll(this.deleteParams);
            }

            this.deleteParams.clear();
        }
    }

    private void finallyUpdate() {
        if (!this.insertParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.insertSql, this.insertParams);
            } catch (Exception var4) {
                LOGGER.error("插入数据库失败，请立马重启");
                LOGGER.error(insertSql, var4);
                this.insertErrorParams.addAll(this.insertParams);
            }

            this.insertParams.clear();
        }

        if (!this.updateParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.updateSql, this.updateParams);
            } catch (Exception var3) {
                LOGGER.error("更新数据库失败，请立马重启");
                LOGGER.error(updateSql, var3);
                this.updateErrorParams.addAll(this.updateParams);
            }

            this.updateParams.clear();
        }

        if (!this.deleteParams.isEmpty()) {
            try {
                this.template.batchUpdate(this.deleteSql, this.deleteParams);
            } catch (Exception var2) {
                LOGGER.error("删除数据库失败，请立马重启");
                LOGGER.error(deleteSql, var2);
                this.deleteErrorParams.addAll(this.deleteParams);
            }

            this.deleteParams.clear();
        }
    }

    public PersistFactory getPersistFactory() {
        return persistFactory;
    }
}

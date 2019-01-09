package com.nemo.game.data.mysql;

import com.nemo.common.jdbc.ConnectionPool;
import com.nemo.common.jdbc.DruidConnectionPool;
import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.common.jdbc.ProtostuffRowMapper;
import com.nemo.common.persist.Cacheable;
import com.nemo.common.persist.Persistable;
import com.nemo.common.persist.PersistableCache;
import com.nemo.game.back.entity.Announce;
import com.nemo.game.back.listener.BackServerHeartListener;
import com.nemo.game.data.DataType;
import com.nemo.game.data.IDataProvider;
import com.nemo.game.data.mysql.factory.*;
import com.nemo.game.data.mysql.mapper.AnnounceMapper;
import com.nemo.game.data.mysql.mapper.RankMapper;
import com.nemo.game.data.mysql.mapper.UserMapper;
import com.nemo.game.entity.*;
import com.nemo.game.entity.sys.AbstractSysData;
import com.nemo.game.server.ServerOption;
import com.nemo.game.system.user.field.UserField;
import com.nemo.game.util.JdbcUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//mysql数据库代理，所有数据库存储相关的请求，都代理到了CacheManager
public class MysqlDataProviderProxy implements IDataProvider{

    private static final Object NULL = new Object();
    private Map<String, Long> nameSidPid2Uid = new ConcurrentHashMap<>();
    private static Map<Long, Object> uidMap = new ConcurrentHashMap<>();
    public static Map<Long, Object> unionMaps = new ConcurrentHashMap<>();
    JdbcTemplate template;
    //管理各类数据缓存和定时任务执行
    private MysqlDataProvider provider;

    public MysqlDataProviderProxy(ServerOption option) throws Exception {
        String gameDbConfigPath = option.getGameDbConfigPath();
        //创建数据库连接池
        ConnectionPool connectionPool = new DruidConnectionPool(gameDbConfigPath);
        //创建JDBC模板
        JdbcTemplate template = new JdbcTemplate(connectionPool);
        this.template = template;
        provider = new MysqlDataProvider();
        provider.init(this.template);
        JdbcUtil.init(this.template);

        //注册factory
        provider.registerPersistTask(new UserPersistFactory());
        provider.registerPersistTask(new RolePersistFactory());
        provider.registerPersistTask(new SysDataPersistFactory());
        provider.registerPersistTask(new RankPersistFactory());
        provider.registerPersistTask(new SysUnionPersistFactory());
        provider.registerPersistTask(new OrderPersistFactory());
        provider.registerPersistTask(new RoleBagPersistFactory());
        provider.registerPersistTask(new RoleCountPersistFactory());
        provider.registerPersistTask(new AnnouncePersistFactory());

        //加载所有的uid
        List<Map<String, Object>> users = template.queryList("select id, loginName, sid, pid from p_user", JdbcTemplate.MAP_MAPPER);
        for(Map<String, Object> user : users) {
            long id = (long)user.get(UserField.ID);
            String loginName = (String) user.get(UserField.LOGIN_NAME);
            int sid = (int) user.get(UserField.SID);
            int pid = (int) user.get(UserField.PID);
            nameSidPid2Uid.put(loginName + "_" + sid + "_" + pid, id);
            uidMap.put(id, NULL);
        }

        //加载所有unionId
        List<Map<String, Object>> mapList = template.queryList("select id from s_union", JdbcTemplate.MAP_MAPPER);
        for(Map<String, Object> map : mapList) {
            long unionId = (long)map.get("id");
            unionMaps.put(unionId, NULL);
        }
    }

    //是否有这个公会
    private boolean hasUnion(long id) {
        return unionMaps.containsKey(id);
    }

    @Override
    public void updateData(Cacheable cache, boolean immediately) {
        provider.update(cache.getId(), cache.dataType(), immediately);
    }

    @Override
    public void updateData(long dataId, int dataType, boolean immediately) {
        provider.update(dataId, dataType, immediately);
    }

    //获取指定数据表的缓存
    @Override
    public PersistableCache getCache(int dataType) {
        return provider.getCache(dataType);
    }

    @Override
    public JdbcTemplate getTemplate() {
        return provider.template;
    }

    @Override
    public void deleteData(Cacheable cache, boolean immediately) {
        provider.removeFromDisk(cache.getId(), cache.dataType(), immediately);
    }

    @Override
    public void insertData(Cacheable cache, boolean immediately) {
        provider.insert((Persistable) cache, immediately);
    }

    @Override
    public void addData(Cacheable cache) {
        provider.put((Persistable) cache);
    }

    @Override
    public void removeData(Cacheable cache) {
        provider.removeFromCache(cache.getId(), cache.dataType());
    }

    @Override
    public void removeData(long id, int dateType) {
        provider.removeFromCache(id, dateType);
    }

    //是否有这个玩家
    @Override
    public boolean hasUser(long id) {
        return uidMap.containsKey(id);
    }

    @Override
    public void registerUser(User user) {
        nameSidPid2Uid.put(user.getLoginName() + "_" + user.getSid() + "_" + user.getPid(), user.getId());
        uidMap.put(user.getId(), NULL);
    }

    @Override
    public User getUser(String loginName, int sid, int pid) {
        String key = loginName + "_" + sid + "_" + pid;
        Long id = nameSidPid2Uid.get(key);
        if(id == null) {
            return null;
        }
        User user = provider.get(id, DataType.USER);
        if(user == null) {
            user = template.query("select id, loginName, roleName, sid, pid, ip, client, type, IDNumber, regTime, qudao, channel " +
                    "from p_user where id = ?", new UserMapper(), id);
            if(user != null) {
                provider.put(user);
            }
            return user;
        }
        return user;
    }

    @Override
    public User getUser(long id) {
        if (hasUser(id)) {
            User user = provider.get(id, DataType.USER);
            if (user == null) {
                user = template.query("select id, loginName, roleName, sid, pid, ip, client, type, IDNumber, regTime , qudao, channel from " +
                        "p_user where id = ?",
                        new UserMapper(), id);
                if (user != null) {
                    provider.put(user);
                }
            }
            return user;
        }
        return null;
    }

    @Override
    public User getUser(String suffixLoginName) {
        return this.template.query("select * from p_user where loginName like ?", new UserMapper(), "%" + suffixLoginName);
    }

    @Override
    public Role getRole(long id) {
        Role role = provider.get(id, DataType.ROLE);
        if(role == null) {
            if(hasUser(id)) {
                role = template.query("select data from p_role where id = ?", new ProtostuffRowMapper<>(Role.class), id);
            }
            if(role != null) {
                provider.put(role);
            }
        }
        return role;
    }

    @Override
    public <T extends AbstractSysData> T getSysData(long id, Class<T> clazz) {
        T data = provider.get(id, DataType.SYS);
        if (data == null) {
            data = this.template.query("select data from s_data where id = ?",
                    new ProtostuffRowMapper<>(clazz), id);
            if (data != null) {
                provider.put(data);
            }
            return data;
        }
        return data;
    }

    @Override
    public void store() {
        provider.store();
    }

    @Override
    public Union getUnion(long id) {
        Union union = provider.get(id, DataType.SYS_UNION);
        if (union == null) {
            if (hasUnion(id)) {
                union = template.query("select data from s_union where id = ?", new ProtostuffRowMapper<>(Union.class), id);
                if (union != null) {
                    provider.put(union);
                }
                return union;
            }
        }
        return union;
    }

    @Override
    public RoleRank getRank(long id) {
        RoleRank roleRank = provider.get(id, DataType.RANK);
        if (roleRank == null) {
            roleRank = template.query("select uid, name, roleLevel, roleRein, roleExp, sex, career, fightPower, heroFightPower, junxian, honor, wingFightPower, weiwang, barrier, searchPk, vipLevel, weiwangLevel,lastLoginTime from s_rank " +
                    "where uid = ?", new RankMapper(), id);
            if (roleRank != null) {
                provider.put(roleRank);
            }
        }
        return roleRank;
    }

    @Override
    public int size(int dataType) {
        return provider.size(dataType);
    }

    @Override
    public RoleBag getRoleBag(long id) {
        RoleBag roleBag = provider.get(id, DataType.BAG);
        if (roleBag == null) {
            if (hasUser(id)) {
                roleBag = template.query("select data from p_bag where id = ?",
                        new ProtostuffRowMapper<>(RoleBag.class), id);
            }
            if (roleBag != null) {
                provider.put(roleBag);
            }
        }
        return roleBag;
    }

    @Override
    public RoleCount getRoleCount(long id) {
        RoleCount roleCount = provider.get(id, DataType.COUNT);
        if (roleCount == null) {
            if (hasUser(id)) {
                roleCount = template.query("select data from p_count where id = ?",
                        new ProtostuffRowMapper<>(RoleCount.class), id);
            }
            if (roleCount != null) {
                provider.put(roleCount);
            }
        }
        return roleCount;
    }

    @Override
    public List<Announce> getAnnounceList() {
        return BackServerHeartListener.announces;
    }

    @Override
    public Announce getAnnounce(long id) {
        return template.query("select id, uniqueId, startTime, endTime, period, type, content from s_announce where id = ?",
                new AnnounceMapper(), id);
    }
}

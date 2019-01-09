package com.nemo.game.data;

import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.common.persist.Cacheable;
import com.nemo.common.persist.PersistableCache;
import com.nemo.game.back.entity.Announce;
import com.nemo.game.entity.*;
import com.nemo.game.entity.sys.AbstractSysData;

import java.util.List;

//数据提供者
public interface IDataProvider {
    //更新数据到磁盘 是否立即更新
    void updateData(Cacheable cache, boolean immediately);
    //更新数据
    void updateData(long dataId, int dataType, boolean immediately);

    JdbcTemplate getTemplate();
    //获取指定数据表的缓存
    PersistableCache getCache(int dataType);
    //从磁盘和缓存中删除一条数据 是否立即删除
    void deleteData(Cacheable cache, boolean immediately);
    //新增一条数据到磁盘和缓存 是否立即增加
    void insertData(Cacheable cache, boolean immediately);
    //增加一条数据到缓存
    void addData(Cacheable cache);
    //从缓存中移除一条数据
    void removeData(Cacheable cache);
    //从缓存中移除一条数据
    void removeData(long id, int dateType);
    //获取用户数据 用户登录名 服务器id 平台id
    User getUser(String loginName, int sid, int pid);
    //获取用户数据
    User getUser(long id);
    //获取用户数据（根据用户名->纯用户不含渠道前缀）
    User getUser(String suffixLoginName);
    //查询玩家数据
    Role getRole(long id);
    //查询系统数据
    <T extends AbstractSysData> T getSysData(long id, Class<T> clazz);
    //是否有这个id的user
    boolean hasUser(long id);
    //注册用户
    void registerUser(User user);
    //存储数据
    void store();
    //查找帮会
    Union getUnion(long id);
    //查找排行榜
    RoleRank getRank(long uid);
    //数据量大小
    int size(int dataType);
    //获取玩家背包
    RoleBag getRoleBag(long id);
    //获取玩家计数
    RoleCount getRoleCount(long id);
    //获取定时公告列表
    List<Announce> getAnnounceList();
    //查公告
    Announce getAnnounce(long id);
}

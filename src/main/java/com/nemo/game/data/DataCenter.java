package com.nemo.game.data;

import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.common.persist.Cacheable;
import com.nemo.common.persist.PersistableCache;
import com.nemo.game.GameContext;
import com.nemo.game.back.entity.Announce;
import com.nemo.game.data.mysql.MysqlDataProviderProxy;
import com.nemo.game.data.mysql.mapper.EmailMapper;
import com.nemo.game.entity.Role;
import com.nemo.game.entity.RoleBag;
import com.nemo.game.entity.RoleCount;
import com.nemo.game.entity.RoleRank;
import com.nemo.game.entity.Union;
import com.nemo.game.entity.User;
import com.nemo.game.entity.sys.AbstractSysData;
import com.nemo.game.entity.sys.DailyActivityData;
import com.nemo.game.entity.sys.GodDragonTowerData;
import com.nemo.game.entity.sys.OtherData;
import com.nemo.game.entity.sys.RobTreasureData;
import com.nemo.game.entity.sys.ShoBakData;
import com.nemo.game.entity.sys.SysActivityData;
import com.nemo.game.entity.sys.TianTiData;
import com.nemo.game.entity.sys.TowerData;
import com.nemo.game.server.ServerOption;
import com.nemo.game.system.chapter.entity.ChapterRankData;
import com.nemo.game.system.email.entity.Email;
import com.nemo.game.util.IDUtil;
import com.nemo.game.util.JdbcUtil;
import com.nemo.game.util.UpdateAction;

import java.util.ArrayList;
import java.util.List;

//数据中心，游戏中的缓存全由此类管理
public class DataCenter {
    private static IDataProvider provider;

    public static void init(ServerOption option) throws Exception {
        //数据库代理类
        provider = new MysqlDataProviderProxy(option);
    }

    //更新数据到磁盘
    public static void updateData(Cacheable cache, boolean immediately) {
        provider.updateData(cache, immediately);
    }

    public static void updateData(Cacheable cache, UpdateAction action) {
        provider.updateData(cache, false);
    }

    public static void updateData(Cacheable cache) {
        updateData(cache, null);
    }

    public static void updateData(long dataId, int dataType, boolean immediately) {
        provider.updateData(dataId, dataType, immediately);
    }

    public static PersistableCache getCache(int dataType) {
        return provider.getCache(dataType);
    }

    public static JdbcTemplate getTemplate() {
        return provider.getTemplate();
    }

    //从磁盘和缓存中删除一条数据 是否立即删除
    public static void deleteData(Cacheable cache, boolean immediately) {
        provider.deleteData(cache, immediately);
    }

    //新增一条数据到缓存和磁盘中
    public static void insertData(Cacheable cache, boolean immediately) {
        provider.insertData(cache, immediately);
    }

    //添加一条数据到缓存中
    public static void addData(Cacheable cache) {
        provider.addData(cache);
    }

    //从缓存中移除一条数据
    public static void removeData(Cacheable cache) {
        provider.removeData(cache);
    }

    //从缓存中移除一条数据
    public static void removeData(long id, int dataType) {
        provider.removeData(id, dataType);
    }

    public static User getUser(String loginName, int sid, int pid) {
        return provider.getUser(loginName, sid, pid);
    }

    //全部保存
    public static void store() {
        provider.store();
    }

    //获取用户数据
    public static User getUser(long id) {
        return provider.getUser(id);
    }

    //获取用户数据（根据登录名，不含渠道前缀）
    public static User getUser(String suffixLoginName) {
        return provider.getUser(suffixLoginName);
    }

    public static boolean hasUser(long id) {
        return provider.hasUser(id);
    }

    public static void registerUser(User user) {
        provider.registerUser(user);
    }

    public static Role getRole(long id) {
        Role role = provider.getRole(id);
        if (role != null) {
            role.setTouchTime(System.currentTimeMillis());

        }
        return role;
    }

    public static <T extends AbstractSysData> T getSysData(long id, Class<T> clazz) {
        return provider.getSysData(id, clazz);
    }

    public static Union getUnion(long uid) {
        return provider.getUnion(uid);
    }

    public static List<Union> getAllUnion() {
        List<Union> unions = new ArrayList<>();
        Long[] unionIds = MysqlDataProviderProxy.unionMaps.keySet().toArray(new Long[0]);
        for (Long unionId : unionIds) {
            Union union = DataCenter.getUnion(unionId);
            unions.add(union);
        }
        return unions;
    }

    public static OtherData getOtherData() {
        return SysDataProvider.get(OtherData.class);
    }

    public static RobTreasureData getRobTreasureData() {
        return SysDataProvider.get(RobTreasureData.class);
    }

    public static ShoBakData getShobakData() {
        return SysDataProvider.get(ShoBakData.class);
    }

    public static DailyActivityData getDailyActivityData() {
        return SysDataProvider.get(DailyActivityData.class);
    }

    public static SysActivityData getSysActivityData() {
        return SysDataProvider.get(SysActivityData.class);
    }

    public static ChapterRankData getChapterRank() {
        return SysDataProvider.get(ChapterRankData.class);
    }

    public static TowerData getTowerData() {
        return SysDataProvider.get(TowerData.class);
    }

    public static RoleRank getRoleRank(long uid) {
        return provider.getRank(uid);
    }

    public static GodDragonTowerData getGodDragonTowerRankData() {
        return SysDataProvider.get(GodDragonTowerData.class);
    }

    public static TianTiData getTianTiData() {
        return SysDataProvider.get(TianTiData.class);
    }

    //是否已经存在某个订单
    public static boolean hasOrder(String order_sn) {
        Integer count = JdbcUtil.query("select count(1) from s_order where orderId = ?", JdbcTemplate.INT_MAPPER, order_sn);
        //count == null代表查询报错了
        return count == null || count > 0;
    }

    public static int getGiftUsedCount(String card) {
        Integer ret = JdbcUtil.query("select count(1) from p_card where card = ?", JdbcTemplate.INT_MAPPER, card);
        return ret == null ? 0 : ret;
    }

    public static int getGiftUsedCountByUser(String card, long rid) {
        Integer ret = JdbcUtil.query("select count(1) from p_card where card = ? and rid = ?", JdbcTemplate.INT_MAPPER, card, rid);
        return ret == null ? 0 : ret;
    }

    public static void insertCard(long rid, String card) {
        JdbcUtil.update("insert into p_card (id, rid, card) values (?, ?, ?)", IDUtil.getId(), rid, card);
    }

    public static void insertEmail(Email email) {
        JdbcUtil.update("insert into p_mail (id, uid, sender, title, content, items, time, state) values (?,?,?,?,?,?,?,?)", email.getEmailId(), email.getOwnerId(), email.getSender(),
                email.getTitle(), email.getDesc(), email.getItems(), email.getTime(), email.getState());
    }

    public static void updateEmail(Email email) {
        JdbcUtil.update("update p_mail set items = ?, state = ? where id = ?", email.getItems(), email.getState(), email.getEmailId());
    }

    public static void deleteEmail(Email email) {
        JdbcUtil.update("delete from p_mail where id = ?", email.getEmailId());
    }

    public static List<Email> getRoleEmailList(long rid) {
        return JdbcUtil.queryList("select * from p_mail where uid = ?", new EmailMapper(), rid);
    }

    public static List<Announce> getAnnounceList() {
        return provider.getAnnounceList();
    }

    public static Announce getAnnounce(long id) {
        return provider.getAnnounce(id);
    }

    //获取数据大小
    public static int getSize(int dataType) {
        return provider.size(dataType);
    }

    public static RoleBag getRoleBag(long uid) {
        RoleBag ret = provider.getRoleBag(uid);
        if (ret != null) {
            ret.setTouchTime(System.currentTimeMillis());
        }
        return ret;

    }

    public static RoleCount getRoleCount(long id) {
        RoleCount ret = provider.getRoleCount(id);
        if (ret != null) {
            ret.setTouchTime(System.currentTimeMillis());
        }
        return ret;
    }

    public static List<String> getWhiteList() {
        return JdbcUtil.queryList("select ip from s_oper where type = 1 and pid = ? ", JdbcTemplate.STRING_MAPPER, GameContext.getOption().getPlatformId());
    }

    public static List<String> getRechargeWhiteList() {
        return JdbcUtil.queryList("select ip from s_oper where type  = 2 and pid = ?", JdbcTemplate.STRING_MAPPER, GameContext.getOption().getPlatformId());
    }

    //插入登录白名单地址
    public static void insertLoginWhiteIp(int type, String ip) {
        JdbcUtil.update("insert into s_oper (pid,type, ip) values (?, ?,?)", GameContext.getOption().getPlatformId(), type, ip);
    }

    //插入充值白名单地址
    public static void insertRechargeWhiteIp(int type, String ip) {
        JdbcUtil.update("insert into s_oper (pid,type, ip) values (? , ?, ?)", GameContext.getOption().getPlatformId(), type, ip);
    }

    //更新排行表中玩家名字
    public static void updateRoleRankName(Role role) {
        Integer ret = JdbcUtil.query("select count(1) from s_rank where uid = ?", JdbcTemplate.INT_MAPPER, role.getId());
        if (ret != null && ret > 0) {
            JdbcUtil.update("update s_rank set name = ? where uid = ?", role.getBasic().getName(), role.getId());
        }
    }

    //重置探索排行榜
    public static void initSearchRankData() {
        JdbcUtil.update("update s_rank set searchPk = 0");
    }
}

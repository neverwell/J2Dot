package com.nemo.game;

import com.nemo.common.config.ConfigDataManager;
import com.nemo.common.jdbc.ConnectionPool;
import com.nemo.common.jdbc.DruidConnectionPool;
import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.game.data.DataCenter;
import com.nemo.game.data.SysDataProvider;
import com.nemo.game.data.mysql.factory.SysDataPersistFactory;
import com.nemo.game.event.EventRegister;
import com.nemo.game.event.EventType;
import com.nemo.game.event.EventUtil;
import com.nemo.game.map.remote.FetchRemoteInfoCommand;
import com.nemo.game.map.remote.RemoteClientMessagePool;
import com.nemo.game.map.remote.TransformUtil;
import com.nemo.game.notice.LocalNoticeSender;
import com.nemo.game.notice.NoticeRegister;
import com.nemo.game.notice.NoticeUtil;
import com.nemo.game.server.EventListener;
import com.nemo.game.server.GameMessagePool;
import com.nemo.game.server.LocalMessageSender;
import com.nemo.game.server.MessageRouter;
import com.nemo.game.server.ServerOption;
import com.nemo.game.system.schedule.ScheduleManager;
import com.nemo.game.util.ExecutorUtil;
import com.nemo.game.util.MessageUtil;
import com.nemo.log.LogService;
import com.nemo.log.consumer.mysql.MysqlLogConsumer;
import com.nemo.net.NetworkService;
import com.nemo.net.NetworkServiceBuilder;
import com.nemo.script.ScriptEngine;

//游戏服
public class GameServer {
    public static String GAME_DB = "GAME_DB";
    //是否已经启动标志
    private boolean state = false;
    //消息分发器
    MessageRouter router;
    //Netty网络服务
    NetworkService netWork;

    public GameServer(ServerOption option) throws Exception{
        int bossLoopGroupCount = 4;
        int workerLoopGroupCount = Runtime.getRuntime().availableProcessors() < 8 ? 8
                : Runtime.getRuntime().availableProcessors();

        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setPort(option.getGameServerPort());

        builder.setMsgPool(new GameMessagePool());
        builder.setNetworkEventlistener(new EventListener());

        builder.setWebSocket(true);
        builder.setSsl(option.isSsl());
        builder.setSslKeyCertChainFile(option.getSslKeyCertChainFile());
        builder.setSslKeyFile(option.getSslKeyFile());
//        builder.setIdleMaxTime(option.getIdelTime() > 0 ? option.getIdelTime() : 10);

        //注册消息处理器
        router = new MessageRouter();
        builder.setConsumer(router);
        //创建网络服务
        netWork = builder.createService(); //搭建NetworkerService 最根本是搭建netty服务所需的ServerBootstrap
        //初始化数据中心
        DataCenter.init(option);
        //初始化配置表
        ConfigDataManager.getInstance().init(option.getConfigDataPath());
        //初始化日志服务
        String logDbPro = DruidConnectionPool.class.getClassLoader().getResource("conf").getPath() + "/logds.properties";
//        ConnectionPool connectionPool = new DruidConnectionPool(option.getLogDBConfigPath());
        ConnectionPool connectionPool = new DruidConnectionPool(logDbPro);
        JdbcTemplate template = new JdbcTemplate(connectionPool);
        LogService.addConsumer(new MysqlLogConsumer(template));
        //LogService.addConsumer(new TextLogConsumer());
        LogService.start("com.nemo.game.log", 4, 4);
        //系统数据
        SysDataProvider.init();
        //天梯数据初始化

        //合服初始化

        //加载名字

        //注册事件
        EventRegister.registerPreparedListeners();
        //初始化通知注册和通知发送类型
        NoticeUtil.init(new NoticeRegister(), new LocalNoticeSender());
        //注册消息发送类型 本地服用本地
        MessageUtil.init(new LocalMessageSender());
        //初始化消息转换池
        TransformUtil.init(new RemoteClientMessagePool());
        //初始化脚本系统
        ScriptEngine.load("com.nemo.game.script");
        //邮件数据

        //开启定时任务
        ScheduleManager.getInstance().start();
        //执行开服事件
        EventUtil.fireEvent(EventType.SERVER_START_UP);
        //获取远程服务器信息
        ExecutorUtil.submit(new FetchRemoteInfoCommand(option.getRemoteInfoAPI()));
    }

    public MessageRouter getRouter(){
        return router;
    }

    public void start() {
        netWork.start(); //启动netty服务
        if(netWork.isRunning()) {
            state = true;
        }
    }

    public void stop() {
        netWork.stop(); //优雅关闭netty服务
        state = false;
    }

    public boolean isOpen() {
        return state;
    }
}

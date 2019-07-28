package com.nemo.game.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by h on 2018/8/5.
 */
@Data
@Slf4j
public class ServerOption {
    public static final String SERVER_ID = "serverId";
    public static final String PLATFORM_ID = "platformId";
    public static final String SERVER_TYPE = "serverType";
    public static final String DES_KEY = "desKey";
    public static final String GAME_SERVER_PORT = "gameServerPort";
    public static final String BACK_SERVER_PORT = "backServerPort";
    public static final String HTTP_SERVER_PORT = "httpServerPort";
    public static final String CONFIG_DATA_PATH = "configDataPath";
    public static final String OPEN_TIME = "openTime";
    public static final String COMBINE_TIME = "combineTime";
    public static final String GAME_DB_CONFIG_PATH = "gameDbConfigPath";
    public static final String LOG_DB_CONFIG_PATH = "logDbConfigPath";
    public static final String HTTP_DB_CONFIG_PATH = "httpDbConfigPath";
    public static final String PLATFORM_API = "platformAPI";
    public static final String CENTER_API = "centerAPI";
    public static final String DEBUG = "debug";
    public static final String FCM_CHECK = "fcmCheck";
    public static final String WG_CHECK = "wgCheck";
    public static final String PUSH_CHAT = "pushChat";
    public static final String PUSH_ROLE = "pushRole";
    public static final String IDEL_TIME = "idelTime";
    public static final String WHITE_LIST = "whiteList";
    public static final String TANWAN_WHITE_LIST = "tanwanWhiteList";
    public static final String API_MD5 = "apiMd5";
    public static final String BACK_LOGIN_SIGN = "backLoginSign";

    public static final String ADD_LOGIN_WHITE_LIST = "addLoginWhiteIp";
    public static final String ADD_RECHARGE_WHITE_LIST = "addRechargeWhiteIp";
    public static final String REMOVE_WHITE_LIST = "removeWhiteIp";
    public static final String AUTO_BAN_CHAT = "autoBanChat";
    public static final String SSL_KEY_CERTCHAIN_FILE = "sslKeyCertChainFile";
    public static final String SSL_KEY_FILE = "sslKeyFile";
    public static final String SSL = "ssl";
    public static final String CENTER_KEY = "centerKey";
    public static final String HOST = "host";
    //跨服相关
    public static final String REMOTE_HOST = "remoteHost";
    public static final String CROSS_SERVER_GROUP = "crossServerGroup";
    public static final String REMOTE_INFO_TYPE = "remoteInfoType";
    public static final String REMOTE_INFO_API = "remoteInfoAPI";

    //服务器id
    protected int serverId;
    //平台id（自定）
    protected int platformId;
    //服务器类型1：游戏服 2：跨服
    protected int serverType = 1;
    //本机ip
    protected String host;
    //游戏服务器端口
    protected int gameServerPort;
    //后台服务器端口
    protected int backServerPort;
    //http服务器端口
    private int httpServerPort;
    //是des的加密code
    protected String desKey;
    //config.properties 配置地址
    protected String configPath;
    //data表文件所在路径
    private String configDataPath;

    private int idelTime;
    //开服日期
    private LocalDateTime openTime;
    //合服日期
    private LocalDateTime combineTime;
    //游戏服数据库连接池配置文件
    private String gameDbConfigPath;
    //礼包卡key
    private String centerKey;
    //日志数据库连接池配置文件
    private String logDBConfigPath;
    //http后台服务器数据库连接池配置文件
    private String httpDBConfigPath;
    //平台API，用于访问平台，比如说一些数据的推送
    private String platformAPI;
    //中心服API（用于礼包卡，全局排行榜等）
    private String centerAPI;
    //调试模式
    private boolean debug;
    //是否开启防沉迷
    private boolean fcmCheck;
    //是否开启外挂检测
    private boolean wgCheck;
    //是否开启聊天信息推送
    private boolean pushChat;
    //是否开启角色信息变更推送
    private boolean pushRole;

    private boolean autoBanChat;

    private int version;

    private String apiMd5;

    private String backLoginSign;
    //ssl crt文件
    private String sslKeyCertChainFile;
    //ssl key文件
    private String sslKeyFile;

    private boolean ssl;
    //==========================================================================
    //跨服相关配置
    //==========================================================================
    //跨服信息获取的方式 1本地配置 2api接口
    private int remoteInfoType;

    private String remoteHost;
    //一起跨服的区服列表
    private String crossServerGroup;
    //获取远程服务器信息的接口
    private String remoteInfoAPI;

    public void build(String configFile) {
        this.configPath = configFile;
        InputStream in = null;
        try {
            in = new FileInputStream(configFile);
            Properties pro = new Properties();
            pro.load(in);

            this.serverId = Integer.parseInt(pro.getProperty(SERVER_ID));
            this.platformId = Integer.parseInt(pro.getProperty(PLATFORM_ID));
            this.serverType = Integer.parseInt(pro.getProperty(SERVER_TYPE));
            this.desKey = pro.getProperty(DES_KEY);
            this.gameServerPort = Integer.parseInt(pro.getProperty(GAME_SERVER_PORT));
            this.backServerPort = Integer.parseInt(pro.getProperty(BACK_SERVER_PORT));
            this.httpServerPort = Integer.parseInt(pro.getProperty(HTTP_SERVER_PORT));
            this.configDataPath = pro.getProperty(CONFIG_DATA_PATH);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.openTime = LocalDateTime.parse(pro.getProperty(OPEN_TIME), formatter);
            String combineTime = COMBINE_TIME;
            if (pro.getProperty(combineTime) != null) {
                this.combineTime = LocalDateTime.parse(pro.getProperty(combineTime), formatter);
            }
            this.gameDbConfigPath = pro.getProperty(GAME_DB_CONFIG_PATH);
            this.centerKey = pro.getProperty(CENTER_KEY);
            this.logDBConfigPath = pro.getProperty(LOG_DB_CONFIG_PATH);
            this.httpDBConfigPath = pro.getProperty(HTTP_DB_CONFIG_PATH);
            this.apiMd5 = pro.getProperty(API_MD5);
            this.backLoginSign = pro.getProperty(BACK_LOGIN_SIGN);

            this.platformAPI = pro.getProperty(PLATFORM_API);
            this.centerAPI = pro.getProperty(CENTER_API);
            this.debug = Boolean.parseBoolean(pro.getProperty(DEBUG));
            this.fcmCheck = Boolean.parseBoolean(pro.getProperty(FCM_CHECK));
            this.wgCheck = Boolean.parseBoolean(pro.getProperty(WG_CHECK));
            this.pushChat = Boolean.parseBoolean(pro.getProperty(PUSH_CHAT));
            this.pushRole = Boolean.parseBoolean(pro.getProperty(PUSH_ROLE));
            this.autoBanChat = Boolean.parseBoolean(pro.getProperty(AUTO_BAN_CHAT));

            if (pro.getProperty(IDEL_TIME) != null) {
                this.idelTime = Integer.parseInt(pro.getProperty(IDEL_TIME));
            }

            this.sslKeyCertChainFile = pro.getProperty(SSL_KEY_CERTCHAIN_FILE);
            this.sslKeyFile = pro.getProperty(SSL_KEY_FILE);
            this.ssl = Boolean.parseBoolean(pro.getProperty(SSL, "false"));

            this.remoteHost = pro.getProperty(REMOTE_HOST);
            this.host = pro.getProperty(HOST);
            this.crossServerGroup = pro.getProperty(CROSS_SERVER_GROUP);
            this.remoteInfoType = Integer.parseInt(pro.getProperty(REMOTE_INFO_TYPE, "1"));
            this.remoteInfoAPI = pro.getProperty(REMOTE_INFO_API, "1");
        } catch (Exception e) {
            throw new RuntimeException("服务器初始配置文件读取错误，启动失败......", e);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

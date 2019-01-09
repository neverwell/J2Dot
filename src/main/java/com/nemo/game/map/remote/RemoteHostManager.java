package com.nemo.game.map.remote;

import com.nemo.game.constant.GameConst;
import com.nemo.game.map.MapManager;
import com.nemo.game.map.PlayerManager;
import com.nemo.game.map.duplicate.notice.HangUpNotice;
import com.nemo.game.map.remote.msg.bean.HostInfo;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.notice.NoticeUtil;
import com.nemo.game.server.GameMessagePool;
import com.nemo.game.server.MessageRouter;
import com.nemo.game.server.Session;
import com.nemo.game.server.SessionKey;
import com.nemo.game.system.attr.AttributeUtil;
import com.nemo.game.util.StringUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RemoteHostManager {

    private static RemoteHostManager INSTANCE = new RemoteHostManager();

    //远程服务器id -> 远程信息
    private Map<Integer, RemoteHost> hostMap = new ConcurrentHashMap<>();
    //ip和端口字符串 -> 远程信息
    private Map<String, RemoteHost> addressMap = new ConcurrentHashMap<>();

    private MessageRouter router = new RemoteHost.RemoteConsumer();

    private RemoteHostManager() {

    }

    public static RemoteHostManager getInstance() {
        return INSTANCE;
    }

    //"192.168.0.0:3300;192.168.0.1:3301"
    public void init(String remoteHost) {
        log.info("初始化远程服务器列表：{}", remoteHost);
        //初始化远程服务器
        if(StringUtil.isBlank(remoteHost)) {
            log.info("远程服务器列表为空");
            return;
        }

        String[] hosts = remoteHost.split(";");
        for(String ipAndPort : hosts) {
            String[] split = ipAndPort.split(":");
            RemoteHost host = new RemoteHost();
            host.setHost(split[0]);
            host.setPort(Integer.parseInt(split[1]));

            addressMap.put(ipAndPort, host);
            //起一个netty客户端去连接远程服务器
            host.connect();
        }
    }

    //"192.168.0.0:3300;192.168.0.1:3301" 本地重新设置远程连接信息
    public void reset(String remoteHost) {
        log.info("重置远程服务器：{}", remoteHost);
        Map<String, RemoteHost> addressMap = new ConcurrentHashMap<>();
        //初始化远程服务器
        if(!StringUtil.isBlank(remoteHost)) {
            String[] hosts = remoteHost.split(":");
            for (String ipAndPort : hosts) {
                String[] split = ipAndPort.split(":");
                RemoteHost host = new RemoteHost();
                host.setHost(split[0]);
                host.setPort(Integer.parseInt(split[1]));
                addressMap.put(ipAndPort, host);
            }
        } else {
            log.info("远程服务器列表为空");
            return;
        }

        //检测原来的远程
        for(String ipAndPort : this.addressMap.keySet().toArray(new String[0])) {
            if(!addressMap.containsKey(ipAndPort)) {
                //废弃了断开连接
                RemoteHost host = this.addressMap.get(ipAndPort);
                log.info("关闭废弃的远程服务器：{},{}:{}", host.getId(), host.getHost(), host.getPort());
                try {
                    host.getClient().stop(); //断开netty客户端
                } catch (IOException e) {
                    log.info("关闭远程host发生错误：" + ipAndPort, e);
                }
                removeHost(host);
            } else {
                //使用原有的host
                RemoteHost host = this.addressMap.get(ipAndPort);
                addressMap.put(ipAndPort, host);
            }
        }

        //增加新的连接
        List<RemoteHost> newHostList = new ArrayList<>();
        for(String ipAndPort : addressMap.keySet().toArray(new String[0])) {
            if(!this.addressMap.containsKey(ipAndPort)) {
                //新加的进行连接
                RemoteHost host = addressMap.get(ipAndPort);
                log.info("增加新的远程服务器：{},{}:{}", host.getId(), host.getHost(), host.getPort());
                newHostList.add(host);
            }
        }
        for (RemoteHost host : newHostList) {
            host.connect();
        }

        this.addressMap = addressMap;
    }

    //本地发送登录请求后远程返回的请求操作
    public void register(Session session, HostInfo hostInfo) {
        AttributeUtil.set(session.getChannel(), SessionKey.CLIENT_INDEX, hostInfo.getIndex());
        AttributeUtil.set(session.getChannel(), SessionKey.HOST_ID, hostInfo.getHostId());

        RemoteHost host = addressMap.get(hostInfo.getHostIp() + ":" + hostInfo.getHostPort());
        if(host == null) {
            log.info("一个不存在的远程服务器返回登录完成的信息：{}:{}", hostInfo.getHostIp(), hostInfo.getHostPort());
            return;
        }

        host.setId(hostInfo.getHostId());
        if(!host.isLogin()) {
            log.info("远程服务器返回登录完成：{}:{}", hostInfo.getHostIp(), hostInfo.getHostPort());
            hostMap.put(hostInfo.getHostId(), host);
        } else {
            log.info("远程服务器返回登录完成的时候已经有其他通道提前注册完毕：{}:{}", hostInfo.getHostIp(), hostInfo.getHostPort());
        }
    }

    //客户端断开连接回调逻辑
    public void unregister(Channel channel) {
        Integer hostId = AttributeUtil.get(channel, SessionKey.HOST_ID);
        if(hostId != null) {
            RemoteHost host = RemoteHostManager.getInstance().findHost(hostId);
            if(host != null) {
                Integer index = AttributeUtil.get(channel, SessionKey.CLIENT_INDEX);
                if(index == null) {
                    index = 0;
                }
                host.unregisterChannel(index);
                log.info("游戏服连接离线,id:{},host:{},port:{},index:{}",host.getId(), host.getHost(), host.getPort(), index);
                if(host.getClient().isEmpty()) {
                    //Player业务处理
                    PlayerManager.getInstance().getAllPlayer().forEach((k, v) -> {
                        if(v.isRemote() && v.getRemoteHostId() == hostId) {
                            log.info("玩家所在的host不可用，返回挂机,hostId:{},id:{},name:{}", v.getRemoteHostId(), v.getId(), v.getName());
                            PlayerManager.getInstance().removePlayer(v); //本地不可用，所以直接移除Player即可
                            HangUpNotice notice = new HangUpNotice(v.getId());
                            NoticeUtil.sendNotice(GameConst.QueueId.TWO_PLAYER, notice, v.getId());
                        }
                    });

                    RemoteHostManager.getInstance().removeHost(host);
                    log.info("游戏服连接全部断线，移除host,id:{},host:{},port:{},index:{}",host.getId(), host.getHost(), host.getPort(), index);
                }
            }
        }
    }

    public void register(RemoteHost host) {
        hostMap.put(host.getId(), host);
    }

    public RemoteHost findHost(int id) {
        return hostMap.get(id);
    }

    public RemoteHost findDefaultHost() {
        for (RemoteHost remoteHost : hostMap.values()) {
            return remoteHost;
        }
        return null;
    }

    public Collection<Integer> findAllHost() {
        return hostMap.keySet();
    }

    //获取远程连接的站点
    public Map<Integer, RemoteHost> getHostMap() {
        return hostMap;
    }

    //移除远程信息和相关的地图
    public void removeHost(RemoteHost host) {
        hostMap.remove(host.getId());

        List<GameMap> removeMap = new ArrayList<>();
        MapManager.getInstance().getMaps().forEach((k, v)->{
            if (v.isRemote()) {
                if (v.getRemoteHostId() == host.getId()) {
                    removeMap.add(v);
                }
            }
        });
        for(GameMap map : removeMap) {
            log.info("地图{}-{}的host不可用，移除地图", map.getId(), map.getLine());
            MapManager.getInstance().removeMap(map.getId(), map.getLine());
        }
    }

    //远程收到游戏服发来的ping消息 游戏服收到远程的pong消息
    public void updateHeart(Session session) {
        //如果长时间没有收到ping消息，需要处理游戏服掉线逻辑，清理该服务器的玩家
        session.sendMessage(RemoteHost.PONG); //这里游戏服虽然也发了 但是远程messagePool没注册pong消息 所以不会处理
        Channel channel = session.getChannel();
        Integer hostId = AttributeUtil.get(channel, SessionKey.HOST_ID);
        Integer channelIndex = AttributeUtil.get(channel, SessionKey.CLIENT_INDEX);
        log.debug("收到心跳：hostId:{}, index:{}", hostId, channelIndex);
        AliveCheckManager.getInsatnce().updateHeart(hostId, channelIndex);
    }

    public MessageRouter getRouter() {
        return router;
    }

    public void setRouter(MessageRouter router) {
        this.router = router;
    }
}

package com.nemo.game.map.remote;

import com.nemo.game.GameContext;
import com.nemo.game.constant.GameConst;
import com.nemo.game.map.Player;
import com.nemo.game.map.PlayerManager;
import com.nemo.game.map.duplicate.notice.HangUpNotice;
import com.nemo.game.map.remote.msg.ReqPlayerListMessage;
import com.nemo.game.notice.NoticeUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//服务器活性检测
@Slf4j
public class AliveCheckManager {

    private static final AliveCheckManager INSTANCE = new AliveCheckManager();

    private static final int MAX_HEART_DT = 10000;
    private Map<Integer, Long> heartTimeMap = new HashMap<>();

    private AliveCheckManager() {

    }

    public static final AliveCheckManager getInsatnce() {
        return INSTANCE;
    }

    //记录当前检测时间点
    public void updateHeart(int host, int index) {
        int key = host * 100 + index;
        heartTimeMap.put(key, System.currentTimeMillis());
    }





    //清理不可用的host的玩家
    public void clearUnavailableHostPlayer() {
        Map<Long, Player> allPlayer = PlayerManager.getInstance().getAllPlayer();
        List<Player> playerList = new ArrayList<>(); //删除的玩家
        for (Player player : allPlayer.values()) {
            if (player.isRemote()) {
                int hostId = player.getRemoteHostId();
                RemoteHost host = RemoteHostManager.getInstance().findHost(hostId);
                if (host == null) {
                    playerList.add(player);
                }
            }
        }
        for (Player player : playerList) {
            log.info("玩家所在的host不可用，返回挂机,hostId:{},id:{},name:{}", player.getRemoteHostId(), player.getId(), player.getName());
            PlayerManager.getInstance().removePlayer(player); //本地不可用，所以直接移除Player即可
            HangUpNotice notice = new HangUpNotice(player.getId());
            NoticeUtil.sendNotice(GameConst.QueueId.TWO_PLAYER, notice, player.getId());
        }
    }

    public void syncPlayerList() {
        Map<Long, Player> allPlayer = PlayerManager.getInstance().getAllPlayer();
        //key -> 服务器id
        Map<Integer, ReqPlayerListMessage> reqMap = new HashMap<>();
        for (Player player : allPlayer.values()) {
            if (player.isRemote()) {
                ReqPlayerListMessage req = reqMap.get(player.getRemoteHostId());
                if (req == null) {
                    req = new ReqPlayerListMessage();
                    req.setHostId(GameContext.getServerId());
                    reqMap.put(player.getRemoteHostId(), req);
                }
                req.getPlayerIdList().add(player.getId());
            }
        }

        reqMap.forEach((k, v) ->{
            RemoteHost host = RemoteHostManager.getInstance().findHost(k);
            if (host != null) {
                host.getClient().sendMsg(v);
            }
        });
    }

    //服务器定时检测
    public void doHeart() {
        Map<Integer, RemoteHost> hostMap = RemoteHostManager.getInstance().getHostMap();
        for(Integer hostId : hostMap.keySet()) {
            RemoteHost host = hostMap.get(hostId);
            if(host.getClient().isStopped()) {
                log.info("host已经关闭，不执行心跳：{}->{}:{}", host.getId(), host.getHost(),host.getPort());
                continue;
            }
            Channel[] channelArray = host.getClient().getChannelArray();
            for(Channel channel : channelArray) {
                if(channel != null && channel.isActive()) {
                    //向每一个有用的远程连接发送ping消息
                    channel.writeAndFlush(RemoteHost.PING);
                }
            }
        }
    }
}

package com.nemo.game.map.remote;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.game.entity.Role;
import com.nemo.game.map.MapManager;
import com.nemo.game.map.Player;
import com.nemo.game.map.PlayerManager;
import com.nemo.game.map.msg.ReqLoginMapMessage;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.map.remote.tranfrom.TransformToHostMessage;
import com.nemo.game.map.remote.tranfrom.TransformToMapMessage;
import com.nemo.game.map.remote.tranfrom.TransformToNoticeMessage;
import com.nemo.game.map.remote.tranfrom.TransformToPlayerMessage;
import com.nemo.game.map.remote.tranfrom.TransformToWorldMessage;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.notice.NoticeUtil;
import com.nemo.game.notice.ProcessNotice;
import com.nemo.game.server.Session;
import com.nemo.game.util.MessageUtil;
import com.nemo.net.Message;
import com.nemo.net.MessagePool;
import lombok.extern.slf4j.Slf4j;
import sun.misc.MessageUtils;

import java.nio.ByteBuffer;

@Slf4j
public class TransformUtil {

    //消息转换池 本地和跨服转发的消息需要注册在这里
    static MessagePool pool;

    public static void init(MessagePool pool) {
        TransformUtil.pool = pool;
    }

    //游戏服发给地图服 针对单个玩家
    public static void dispatch(TransformToMapMessage msg) {
        //将消息内容放到content中
        ByteBuffer buffer = ByteBuffer.wrap(msg.getMessages());
        byte[] content = new byte[buffer.remaining()];
        buffer.get(content);

        final int msgId = msg.getMessageId();
        Message targetMsg = pool.get(msgId);
        if (targetMsg == null) {
            log.error("远程消息[{}]未注册", msgId);
            return;
        }
        targetMsg.decode(content);
        //在跨服上处理 跟MapProcessor类似地交给地图的驱动去处理 本身是在RPCProcessor上处理的
        process(targetMsg, msg.getPid());
    }

    //地图服发给逻辑服的消息，仅仅做一次转发，类似于网关
    public static void dispatch(TransformToPlayerMessage msg) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getMessages());
        byte[] content = new byte[buffer.remaining()];
        buffer.get(content);

        final int msgId = msg.getMessageId();
        if(msgId == -1) {
            log.debug("收到远程消息组[{}], pid:{}", msgId, msg.getPidList());
            TransformMessageGroup group = new TransformMessageGroup(content);
            MessageUtil.sendMsgToRids(group, msg.getPidList());
        } else {
            Message targetMsg = pool.get(msgId);
            if(targetMsg == null) {
                log.error("远程消息[{}]未注册", msgId);
                return;
            }
            targetMsg.decode(content);
            log.debug("收到远程消息[{}]", msgId);

            MessageUtil.sendMsgToRids(targetMsg, msg.getPidList());
        }
    }

    //接收到进程间的通知
    public static void dispatch(TransformToNoticeMessage msg) {
        int noticeId = msg.getNoticeId();
        //派发处理
        ProcessNotice notice = NoticeUtil.getNotice(noticeId);
        if(notice == null) {
            log.info("找不到id为：{}的notice", noticeId);
            return;
        }
        notice.decode(msg.getNoticeBytes());
        byte processId = msg.getProcessId();
        long playerId = msg.getPlayerId();

        notice.send(processId, playerId);
    }

    //地图服收到的世界消息 广播 远程服务器里同一个hostId的玩家
    public static void dispatch(TransformToWorldMessage msg) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getMessages());
        byte[] content = new byte[buffer.remaining()];
        buffer.get(content);

        final int msgId = msg.getMessageId();
        if (msgId == -1) {
            log.error("收到远程消息组[{}], hostId:{}", msgId, msg.getLocalhostId());
            TransformMessageGroup group = new TransformMessageGroup(content);
            MessageUtil.sendToWorld(group, msg.getLocalhostId());
        } else {
            Message targetMsg = pool.get(msgId);
            if (targetMsg == null) {
                log.error("远程消息[{}]未注册", msgId);
                return;
            }
            targetMsg.decode(content);
            log.error("收到远程消息[{}]", msgId);

            MessageUtil.sendToWorld(targetMsg, msg.getLocalhostId());
        }
    }

    //游戏服发给地图服的广播
    public static void dispatch(TransformToHostMessage msg) {
        TransformToWorldMessage transform = new TransformToWorldMessage();
        transform.setMessages(msg.getMessages());
        transform.setLocalhostId(msg.getLocalhostId());
        transform.setMessageId(msg.getMessageId());

        for (RemoteHost remoteHost : RemoteHostManager.getInstance().getHostMap().values()) {
            if (remoteHost.getId() == msg.getLocalhostId()) {
                continue;
            }
            if (remoteHost.getOpenServerDay() < 30) {
                continue;
            }
            remoteHost.getClient().sendMsg(transform);
        }
    }

    //地图服处理消息
    private static void process(IQueueDriverCommand message, long pid) {
        Player player = PlayerManager.getInstance().getPlayer(pid);
        if(player == null) {
            log.error(pid + "-找不到玩家对象：" + message.getClass().getName());
            return;
        }
        PlayerActor actor = player.getCurrent();

        GameMap map = MapManager.getInstance().getMap(actor.getMapId(), actor.getLine());
        if(map == null) {
            return;
        }

        message.setQueueId(actor.getMapId());
        Session session = new Session();
        Role role = new Role();
        role.getBasic().setId(pid);
        session.registerRole(role);
        message.setParam(session);

        if(!player.isLoginMap()) {
            if(!(message instanceof ReqLoginMapMessage)) {
                return;
            }
        }

        map.getDriver().addCommand(message);
    }



}

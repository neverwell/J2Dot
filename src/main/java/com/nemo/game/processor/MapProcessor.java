package com.nemo.game.processor;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.game.constant.GameConst;
import com.nemo.game.map.MapManager;
import com.nemo.game.map.Player;
import com.nemo.game.map.PlayerManager;
import com.nemo.game.map.msg.ReqLoginMapMessage;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.map.remote.RemoteHost;
import com.nemo.game.map.remote.RemoteHostManager;
import com.nemo.game.map.remote.tranfrom.TransformToMapMessage;
import com.nemo.game.map.remote.tranfrom.TransformToNoticeMessage;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.notice.ProcessNotice;
import com.nemo.game.server.MessageProcessor;
import com.nemo.game.server.Session;
import com.nemo.net.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//即时战斗地图
public class MapProcessor implements MessageProcessor{
    private static final Logger LOGGER = LoggerFactory.getLogger(MapProcessor.class);

    @Override
    public void process(Message message) {
        Session session = (Session)message.getParam();
        Player player = PlayerManager.getInstance().getPlayer(session.getRole().getId());
        if(player == null) {
            LOGGER.error(session.getRole().getBasic().getName() + "-找不到玩家对象：" + message.getClass().getName());
            return;
        }

        if(player.isRemote()) {
            //发往远程
            TransformToMapMessage transform = new TransformToMapMessage();
            transform.setPid(player.getId());
            transform.setMessageId(message.getId());
            transform.setMessages(message.encode());
            RemoteHost host = RemoteHostManager.getInstance().findHost(player.getRemoteHostId());
            if(host == null) {
                return;
            }
            host.getClient().sendMsg(transform);
        } else {
            PlayerActor actor = player.getCurrent();

            GameMap map = MapManager.getInstance().getMap(actor.getMapId(), actor.getLine());
            if (map == null) { // 找不到地图
                return;
            }

            //设置新的队列id 否则添加到驱动的队列时不接受
            message.setQueueId(actor.getMapId());
            //如果没登录地图 除了登录消息 别的消息不处理
            if (!player.isLoginMap()) {
                if (!(message instanceof ReqLoginMapMessage)) {
                    return;
                }
            }
            //交给地图的驱动去处理
            map.getDriver().addCommand(message);
        }
    }

    @Override
    public void process(IQueueDriverCommand command, long id) {
        Player player = PlayerManager.getInstance().getPlayer(id);
        if(player == null) {
            LOGGER.error(id + "-找不到玩家对象：" + command.getClass().getName());
            return;
        }
        PlayerActor actor = player.getCurrent();

        GameMap map = MapManager.getInstance().getMap(actor.getMapId(), actor.getLine());
        if(map == null) {
            return;
        }

        //设置新的队列id 否则添加到驱动的队列时不接受
        command.setQueueId(actor.getMapId());
        map.getDriver().addCommand(command);
    }

    @Override
    public void process(ProcessNotice notice, long id) {
        Player player = PlayerManager.getInstance().getPlayer(id);
        if(player == null) {
            LOGGER.error(id + "-找不到玩家对象：" + notice.getClass().getName());
            return;
        }

        if(player.isRemote()) {
            //发往远程
            TransformToNoticeMessage transform = new TransformToNoticeMessage();
            transform.setProcessId(this.id());
            transform.setNoticeId(notice.id());
            transform.setPlayerId(player.getId());
            transform.setNoticeBytes(notice.encode());
            RemoteHost host = RemoteHostManager.getInstance().findHost(player.getRemoteHostId());
            if(host == null) {
                return;
            }
            host.getClient().sendMsg(transform);
        } else {
            PlayerActor actor = player.getCurrent();

            GameMap map = MapManager.getInstance().getMap(actor.getMapId(), actor.getLine());
            if (map == null) { // 找不到地图
                return;
            }

            //设置新的队列id 否则添加到驱动的队列时不接受
            notice.setQueueId(actor.getMapId());
            map.getDriver().addCommand(notice);
        }
    }

    @Override
    public byte id() {
        return GameConst.QueueId.FIVE_MAP;
    }
}

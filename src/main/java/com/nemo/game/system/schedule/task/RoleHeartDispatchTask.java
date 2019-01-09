package com.nemo.game.system.schedule.task;

import com.nemo.game.GameContext;
import com.nemo.game.constant.GameConst;
import com.nemo.game.entity.Role;
import com.nemo.game.event.EventType;
import com.nemo.game.event.IListener;
import com.nemo.game.server.MessageProcessor;
import com.nemo.game.server.Session;
import com.nemo.game.server.SessionManager;
import com.nemo.game.system.user.command.PlayerHeartCommand;

import javax.annotation.processing.Processor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoleHeartDispatchTask implements Runnable, IListener{

    private static Map<Long, PlayerHeartCommand> commandEvent = new ConcurrentHashMap<>();

    @Override
    public void run() {
        Session[] sessionArray = SessionManager.getInstance().sessionArray();

        for(Session session : sessionArray) {
            if(session.isRoleRegister()) {
                Role role = session.getRole();
                //本身在场景线程里执行 派发到玩家线程去执行
                MessageProcessor processor = GameContext.getGameServer().getRouter().getProcessor(GameConst.QueueId.TWO_PLAYER);

                PlayerHeartCommand playerHeartCommand = commandEvent.get(role.getId());
                if(playerHeartCommand == null) {
                    playerHeartCommand = new PlayerHeartCommand(this, role.getId());
                    commandEvent.put(role.getId(), playerHeartCommand);
                }

                processor.process(playerHeartCommand, role.getId());
            }
        }
    }

    //玩家退出游戏时执行
    @Override
    public void update(EventType type, Object param) {
        Role role = (Role)param;
        if(role == null) {
            return;
        }
        //移除心跳事件缓存
        commandEvent.remove(role.getId());
    }
}

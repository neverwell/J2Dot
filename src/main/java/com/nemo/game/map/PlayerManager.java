package com.nemo.game.map;

import com.nemo.game.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    public static final PlayerManager INSTANCE = new PlayerManager();

    public static final Map<Long, Player> PLAYER_MAP = new ConcurrentHashMap<>();

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public Player getPlayer(long id) {
        return PLAYER_MAP.get(id);
    }

    public Map<Long, Player> getAllPlayer() {
        return PLAYER_MAP;
    }

    public final void removePlayer(Player player) {
        LOGGER.info("移除player全局对象：{}", player.getName());
        PLAYER_MAP.remove(player.getId());
    }

    public final void removePlayer(long id) {
        LOGGER.info("移除player全局对象：{}", id);
        PLAYER_MAP.remove(id);
    }

    public Player createPlayer(Role role) {
        return createPlayer(role, 0);
    }

    public Player createPlayer(Role role, int aiType) {
        Player player = new Player();
        player.setId(role.getId());
        player.setName(role.getName());
//        for(Hero hero : )


        return player;
    }




}

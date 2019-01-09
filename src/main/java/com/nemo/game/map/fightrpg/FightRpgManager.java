package com.nemo.game.map.fightrpg;

import com.nemo.game.config.model.MapConfig;
import com.nemo.game.map.MapManager;
import com.nemo.game.map.Player;
import com.nemo.game.map.PlayerManager;
import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.map.scene.GameMap;
import com.nemo.game.system.miji.entity.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FightRpgManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(FightRpgManager.class);

    private static final FightRpgManager INSTANCE = new FightRpgManager();

    public static FightRpgManager getInstance(){
        return INSTANCE;
    }

    //玩家释放技能
    public void playerSkill(long rid, int skillId, int x, int y, long targetId) {
        Player player = PlayerManager.getInstance().getPlayer(rid);
        if(player == null) {
            return;
        }
        PlayerActor actor = player.getCurrent();
        if(actor == null) {
            return;
        }
        GameMap map = MapManager.getInstance().getMap(actor);
        if(map == null) {
            return;
        }
        MapConfig mapConfig = new MapConfig();
        if(mapConfig.getSafe() == 1) {
            return;
        }
        IMapObject obj = map.getObject(targetId);
        if(obj == null) {
            return;
        }

        Skill skill = actor.getSkillMap().get(skillId);
        if(skill == null || skill.getLevel() <= 0) {

        }





    }




}

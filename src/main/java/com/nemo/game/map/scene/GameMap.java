package com.nemo.game.map.scene;

import com.nemo.concurrent.QueueDriver;
import com.nemo.concurrent.ScheduledEventDispatcher;
import com.nemo.game.map.aoi.AOIEventListenerImpl;
import com.nemo.game.map.aoi.TowerAOI;
import com.nemo.game.map.obj.IMapObject;
import com.nemo.game.map.obj.MonsterActor;
import com.nemo.game.map.obj.PetActor;
import com.nemo.game.map.obj.PlayerActor;
import com.nemo.game.map.route.PathFinder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//场景顶层父类 所有地图类都继承
@Setter
@Getter
public class GameMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMap.class);

    //地形
    protected Topography topography;
    //灯塔视野管理
    protected TowerAOI aoi;
    //路径寻找器
    protected PathFinder pathFinder;

    //地图id
    protected int id;
    //地图分线
    protected int line;
    //地图名字
    protected String name;
    //分线id 配置表id
    protected int cfgId;

    protected boolean safe;
    //是否远程地图
    protected boolean remote;
    //远程主机id
    protected int remoteHostId;
    //玩家ai的配置id
    protected int[][] playerAISkill;
    //地图能否重叠站
    protected boolean canCross = false;

    //存储游戏对象的map
    protected Map<Long, IMapObject> objectMap = new HashMap<>();

//    protected Map<Long, GroundItem> itemMap = new HashMap<>();
//
    protected Map<Long, PlayerActor> playerMap = new HashMap<>();
//
//    protected Map<Long, MonsterActor> monsterMap = new HashMap<>();
//
//    protected Map<Long, GroundBuffer> bufferMap = new HashMap<>();
//
//    protected Map<Long, GroundEvent> eventMap = new HashMap<>();
//
//    protected Map<Long, NPCActor> npcMap = new HashMap<>();
//
//    protected Map<Long, PetActor> petMap = new HashMap<>();
//    protected Map<Long, HeroActor> heroMap = new HashMap<>();
//
//    protected Map<Integer, List<Event>> pointEventMap = new HashMap<>();

    //队列驱动
    protected QueueDriver driver;
    //定时任务
    protected ScheduledEventDispatcher eventDispatcher;

    //地图下玩家视野范围
    private int viewRange;

    public IMapObject getObject(long id) {
        return objectMap.get(id);
    }

    public GameMap(Topography topography) {
        this.topography = topography;
        this.aoi = new TowerAOI(topography.getWidth(), topography.getHeight());
        this.aoi.addListener(new AOIEventListenerImpl());
        this.pathFinder = new PathFinder(topography.getWidth(), topography.getHeight());
    }

    public void setTopography(Topography topography) {
        this.topography = topography;
    }

    public Topography getTopography() {
        return topography;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    public int getCfgId() {
        return cfgId;
    }

    public void setCanCross(boolean canCross) {
        this.canCross = canCross;
    }

    public boolean isCanCross() {
        return canCross;
    }

    public void setDriver(QueueDriver driver) {
        this.driver = driver;
    }

    public QueueDriver getDriver() {
        return driver;
    }

    public void setEventDispatcher(ScheduledEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public ScheduledEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    //用于做一些额外的初始化工作 在各种地图中
    public boolean init(){
        return true;
    }

}

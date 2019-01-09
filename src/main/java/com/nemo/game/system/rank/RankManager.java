package com.nemo.game.system.rank;

public class RankManager {
    private static RankManager ourInstance = new RankManager();

    private RankManager() {
    }

    public static RankManager getInstance() {
        return ourInstance;
    }

    //每半小时更新一次数据
    public void updateRankData() {

    }

}

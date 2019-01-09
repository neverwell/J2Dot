package com.nemo.game.system.activity;

public class DynamicActivityCfgReloadTask implements Runnable{

    public static final String QUERY_ACTIVITY_SQL = "select id, param from s_dync_activity;";
    public static final String QUERY_GOAL_SQL = "select id, param from s_dync_goal;";

    @Override
    public void run() {
        ActivityManager.getInstance().dynamicActivityCfgReloadTask();
    }
}

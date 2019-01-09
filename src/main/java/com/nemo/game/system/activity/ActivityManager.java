package com.nemo.game.system.activity;

public class ActivityManager {
    private static ActivityManager ourInstance = new ActivityManager();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        return ourInstance;
    }

    public void dynamicActivityCfgReloadTask() {
//        IActivityScript iScript = ScriptEngine.get1t1(IActivityScript.class);
//        if (iScript == null) {
//            return;
//        }
//        iScript.dynamicActivityCfgReloadTask();
    }
}

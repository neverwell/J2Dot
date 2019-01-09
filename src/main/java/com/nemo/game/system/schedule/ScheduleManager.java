package com.nemo.game.system.schedule;

import com.nemo.game.system.schedule.script.IScheduleScript;
import com.nemo.script.ScriptEngine;

public class ScheduleManager {

    private static final ScheduleManager INSTANCE = new ScheduleManager();

    private ScheduleManager(){
    }

    public static ScheduleManager getInstance() {
        return INSTANCE;
    }

    public void start() {
        IScheduleScript scheduleScript = ScriptEngine.get1t1(IScheduleScript.class);
        if(scheduleScript == null) {
            return;
        }
        scheduleScript.start();
    }



}

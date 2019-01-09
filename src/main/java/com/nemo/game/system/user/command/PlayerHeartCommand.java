package com.nemo.game.system.user.command;

import com.nemo.concurrent.AbstractCommand;
import com.nemo.game.data.DataCenter;
import com.nemo.game.entity.Role;
import com.nemo.game.event.EventType;
import com.nemo.game.event.EventUtil;
import com.nemo.game.system.schedule.task.RoleHeartDispatchTask;
import com.nemo.game.util.UpdateAction;

import java.util.Calendar;

public class PlayerHeartCommand extends AbstractCommand{

    private RoleHeartDispatchTask roleHeartDispatchTask;
    private long rid;
    private long lastMinute = 0;
    private long lastMills;
    private long lastDay;

    public PlayerHeartCommand(RoleHeartDispatchTask roleHeartDispatchTask, long rid) {
        this.roleHeartDispatchTask = roleHeartDispatchTask;
        this.rid = rid;
        this.lastMills = System.currentTimeMillis();
        this.lastMinute = lastMills / 1000; //这里不除60 第一次去执行纠正
        Calendar calendar = Calendar.getInstance();
        this.lastDay = calendar.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public void doAction() {
        long mills = System.currentTimeMillis();
        long minute = mills / 1000 / 60;
        int dt = (int) (mills - this.lastMills);

        Role role = DataCenter.getRole(rid);
        if(role == null) {
            return;
        }

        //每分 一次
        if(minute != lastMinute) {
            EventUtil.fireEvent(EventType.PLAYER_MINUTE_HEART);
            this.lastMinute = minute;
        }
        //每秒 一次
        PlayerHeartCommand.PlayerHeartEvent event = new PlayerHeartCommand.PlayerHeartEvent(dt, mills, role);
        EventUtil.fireEvent(EventType.PLAYER_SECOND_HEART, event);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int second = calendar.get(Calendar.SECOND);

        //每天一次（零点过后至少5秒才执行）
        if(day != this.lastDay && second > 5) {
            this.lastDay = day;


            EventUtil.fireEvent(EventType.PLAYER_MIDNIGHT, role);
            DataCenter.updateData(role, UpdateAction.PLAYER_MIDNIGHT);
        }


    }

    public class PlayerHeartEvent {
        private int dt;
        private long time;
        private Role role;

        public PlayerHeartEvent(int dt, long time, Role role) {
            this.dt = dt;
            this.time = time;
            this.role = role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public int getDt() {
            return dt;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public Role getRole() {
            return role;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}

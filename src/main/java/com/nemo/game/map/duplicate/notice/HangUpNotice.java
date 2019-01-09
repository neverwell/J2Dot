package com.nemo.game.map.duplicate.notice;

import com.nemo.common.jdbc.SerializerUtil;
import com.nemo.game.notice.ProcessNotice;

public class HangUpNotice extends ProcessNotice {

    private long rid;

    public int id() {
        return 14;
    }

    public HangUpNotice() {

    }

    @Override
    public byte[] encode() {
        return SerializerUtil.encode(this, HangUpNotice.class);
    }

    @Override
    public void decode(byte[] bytes) {
        SerializerUtil.decode(bytes, HangUpNotice.class, this);
    }

    public HangUpNotice(long rid) {
        this.rid = rid;
    }

    @Override
    public void doAction() {
//        Role role = DataCenter.getRole(rid);
//        if(role != null) {
//            LOGGER.info("收到进入挂机的通知：{}", role.getBasic().getName());
//            ChapterManager.getInstance().reqHangUp(role, true);
//        }
    }
}

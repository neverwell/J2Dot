package com.nemo.game.notice;

import com.nemo.game.map.duplicate.notice.HangUpNotice;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NoticeRegister {

    private Map<Integer, Class<? extends ProcessNotice>> noticeMap = new HashMap<>();

    public NoticeRegister() {
        //注册各种notice 主要跨服有用 跨服的notice数据打包传输到游戏服 再根据id解码成具体notice
        register(14, new HangUpNotice());


    }

    public void register(int id, ProcessNotice notice) {
        if(noticeMap.containsKey(id)) {
            throw new RuntimeException("notice注册重复：id->" + id);
        }
        if(notice.id() != id) {
            throw new RuntimeException("notice注册id和自身id不一致：" + notice.getClass().getName());
        }
        noticeMap.put(id, notice.getClass());
    }

    public ProcessNotice getNotice(int id) {
        Class<? extends ProcessNotice> clazz = noticeMap.get(id);
        if(clazz == null) {
            return null;
        }
        try {
            ProcessNotice notice = clazz.newInstance();
            return notice;
        } catch (Exception e) {
            log.error("创建notice失败：{}", clazz.getName());
        }
        return null;
    }
}

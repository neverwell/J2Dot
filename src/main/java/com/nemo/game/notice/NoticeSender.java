package com.nemo.game.notice;

public interface NoticeSender {

    void sendNotice(byte processId, ProcessNotice notice, long id);
}

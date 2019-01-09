package com.nemo.game.notice;

public class NoticeUtil {

    private static NoticeRegister register;

    private static NoticeSender sender;

    public static void init(NoticeRegister register, NoticeSender sender) {
        NoticeUtil.register = register;
        NoticeUtil.sender = sender;
    }

    public static ProcessNotice getNotice(int id) {
        return register.getNotice(id);
    }

    public static void sendNotice(byte processId, ProcessNotice notice, long id) {
        sender.sendNotice(processId, notice, id);
    }
}

package com.nemo.game.system.platform.constant;

public enum QuDao {
    ONE(1, ""),
    TWO(2, "")
    ;

    private int quDaoIndex;
    private String quDaoName;

    QuDao(int quDaoIndex, String quDaoName) {
        this.quDaoIndex = quDaoIndex;
        this.quDaoName = quDaoName;
    }

    public int getQuDaoIndex() {
        return quDaoIndex;
    }

    public String getQuDaoName() {
        return quDaoName;
    }

    public static QuDao valueOf(int quDaoIndex) {
        for (QuDao quDao : QuDao.values()) {
            if (quDao.getQuDaoIndex() == quDaoIndex) {
                return quDao;
            }
        }
        return null;
    }
}

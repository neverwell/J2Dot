package com.nemo.game.system.tip;

public class TipManager {

    private static final TipManager INSTANCE = new TipManager();

    public static TipManager getInstance() {
        return INSTANCE;
    }

    public void error(long uid, String error, Object... params) {



    }

}

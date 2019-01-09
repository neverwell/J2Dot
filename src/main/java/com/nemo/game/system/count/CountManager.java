package com.nemo.game.system.count;

import com.nemo.game.system.count.entity.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountManager.class);

    private static final CountManager INSTANCE = new CountManager();

    private CountManager() {
    }

    public static CountManager getInstance() {
        return INSTANCE;
    }

    public boolean checkReset(Count count) {
        return true;
    }
}

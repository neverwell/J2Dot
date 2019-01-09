package com.nemo.game.system.user;

import com.nemo.game.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

    private static final UserManager INSTANCE = new UserManager();

    public static UserManager getInstance() {
        return INSTANCE;
    }



    public void logout(Session session){

    }

}

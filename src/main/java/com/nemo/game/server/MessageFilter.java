package com.nemo.game.server;

//message执行过滤器， 可以过滤掉一些特殊条件
public interface MessageFilter {
    boolean before(AbstractMessage msg);

    boolean after(AbstractMessage msg);
}

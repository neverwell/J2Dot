package com.nemo.log.consumer;

import com.nemo.log.AbstractLog;

public interface LogConsumer {
    void consumer(AbstractLog log);

    int type();

    void parse(AbstractLog log) throws Exception;
}

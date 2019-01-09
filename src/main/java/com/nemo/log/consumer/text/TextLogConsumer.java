package com.nemo.log.consumer.text;

import com.nemo.log.AbstractLog;
import com.nemo.log.consumer.DefaultConsumerType;
import com.nemo.log.consumer.LogConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextLogConsumer implements LogConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger("TextLog");

    public TextLogConsumer() {
    }

    @Override
    public void consumer(AbstractLog log) {
        String text = log.toTextLog();
        if(text != null) {
            LOGGER.info(text);
        }
    }

    @Override
    public int type() {
        return DefaultConsumerType.TEXT;
    }

    @Override
    public void parse(AbstractLog log) throws Exception {
    }
}

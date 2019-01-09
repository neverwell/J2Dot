package com.nemo.log;

import com.nemo.log.consumer.LogConsumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LogDesc {
    private List<LogConsumer> consumers = new ArrayList();
}

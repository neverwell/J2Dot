package com.nemo.log;

import com.nemo.log.annotation.Consumer;
import com.nemo.log.consumer.DefaultConsumerType;
import com.nemo.log.consumer.LogConsumer;
import com.nemo.log.consumer.mysql.TableCycle;
import com.nemo.log.consumer.mysql.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Table(cycle = TableCycle.SINGLE)
@Consumer({DefaultConsumerType.MYSQL})
public abstract class AbstractLog implements Runnable{
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractLog.class);
    private static final Map<Class<?>, LogDesc> LOG_DESC_MAP = new HashMap<>();

    @Override
    public void run() {
        LogDesc desc = LOG_DESC_MAP.get(this.getClass());
        if(desc == null) {
            LOGGER.error("日志{}找不到描述信息", this.getClass());
        } else {
            Iterator<LogConsumer> iterator = desc.getConsumers().iterator();
            while (iterator.hasNext()) {
                LogConsumer consumer = iterator.next();
                consumer.consumer(this);
            }
        }
    }

    public void parse() throws Exception {
        Class<?> clazz = this.getClass();
        Consumer consumer = clazz.getAnnotation(Consumer.class);
        if(consumer != null && consumer.value().length != 0) {
            LogDesc desc = new LogDesc();

            //获取注解Consumer定义的各种LogConsumer
            int[] values = consumer.value();
            for(int i = 0; i < values.length; ++i) {
                int type = values[i];
                LogConsumer logConsumer = LogService.consumers.get(type);
                if(logConsumer == null) {
                    LOGGER.error("日志{}找不到消费者,type:{}", clazz.getName(), type);
                } else {
                    if(desc.getConsumers().contains(logConsumer)) {
                        LOGGER.error("日志{}消费者重复,type:{}", clazz.getName(), type);
                    }
                    desc.getConsumers().add(logConsumer);
                }
            }

            Iterator<LogConsumer> iterator = desc.getConsumers().iterator();
            while (iterator.hasNext()) {
                LogConsumer logConsumer = iterator.next();
                //交给LogConsumer的parse方法解析
                logConsumer.parse(this);
            }

            LOG_DESC_MAP.put(clazz, desc); //放入Map，执行提交任务时验证是否存在
        } else {
            LOGGER.error("日志{}消费者列表为空，请检查Consumer注解.", clazz.getName());
        }
    }

    public String toTextLog() {
        return null;
    }
}

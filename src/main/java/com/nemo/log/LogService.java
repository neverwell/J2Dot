package com.nemo.log;

import com.nemo.log.consumer.LogConsumer;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LogService {
    private static Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private static LogService INSTANCE = null;
    private static int coreThreadPoolSize;
    private static int maximumThreadPoolSize;
    static Map<Integer, LogConsumer> consumers = new HashMap<>();
    private final ThreadPoolExecutor executor;

    private LogService() {
        this.executor = new ThreadPoolExecutor(coreThreadPoolSize, maximumThreadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new LogService.LogThreadFactory(), new LogService.LogRejectedExecutionHandler());
    }

    public static void start(String logPath, int coreThreadPoolSize, int maximumThreadPoolSize) throws Exception {
        LogService.coreThreadPoolSize = coreThreadPoolSize;
        LogService.maximumThreadPoolSize = maximumThreadPoolSize;

        //获取logPath路径下的所有AbstractLog子类Class
        Set<Class<AbstractLog>> ret = LogBeanUtil.getSubClasses(logPath, AbstractLog.class);
        Iterator<Class<AbstractLog>> iterator = ret.iterator();
        while (iterator.hasNext()) {
            Class<AbstractLog> logClass = iterator.next();
            AbstractLog log = logClass.newInstance();
            //解析每个子类
            log.parse();
        }

        INSTANCE = new LogService();
    }

    //启动时添加各种consumer
    public static void addConsumer(LogConsumer consumer) {
        Iterator<LogConsumer> iter = consumers.values().iterator();

        LogConsumer con;
        do {
            if(!iter.hasNext()) {
                consumers.put(consumer.type(), consumer);
                return;
            }
            con = iter.next();
        } while (con.type() != consumer.type());

        throw new RuntimeException("消费者有重复的id，请仔细检查LogConsumer.type()方法");
    }

    //处理业务提交的日志任务
    public static void submit(AbstractLog log) {
        INSTANCE.execute(log);
    }

    private void execute(AbstractLog log) {
        this.executor.execute(log);
    }

    static class LogThreadFactory implements ThreadFactory {
        AtomicInteger count = new AtomicInteger(0);

        LogThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable r) {
            int curCount = this.count.incrementAndGet();
            return new Thread(r, "日志线程-" + curCount);
        }
    }

    static class LogRejectedExecutionHandler implements RejectedExecutionHandler {
        LogRejectedExecutionHandler() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            AbstractLog log = (AbstractLog)r;
            if(r != null) {
                LogService.LOGGER.error("丢弃日志提交请求, log:{}", log.getClass().getName());
            }
        }
    }
}

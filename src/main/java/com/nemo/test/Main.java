package com.nemo.test;


import com.nemo.commons.util.Cast;
import com.nemo.concurrent.QueueExecutor;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static final QueueExecutor EXECUTOR = new QueueExecutor("场景公共驱动线程",100,100);

    static Map<String, Integer> map = new ConcurrentHashMap<>();
    static Map<Integer, Rank> rankMap = new ConcurrentHashMap<>();

    static final Object LOCK = new Object();
    static final Object LOCK3 = new Object();
    static final Object LOCK4 = new Object();

    public static void update1(){
        Integer i = map.get("test1");
        if(i == null) {
            map.put("test1", 0);
        }
        map.put("test1", map.get("test1") + 1);
        System.out.println("test1: " + map.get("test1"));
    }

    public static void update2(){
        Integer i = map.get("test2");
        if(i == null) {
            map.put("test2", 0);
        }
        map.put("test2", map.get("test2") + 1);
        System.out.println("test2: " + map.get("test2"));
    }

    public static void update3(){
        Rank rank = rankMap.get(1);
        if(rank == null) {
            synchronized (LOCK3) {
                rank = rankMap.get(1);
                if(rank == null) {
                    rank = new Rank();
                    rankMap.put(1, rank);
                }
            }
        }
        int i;
        synchronized (LOCK3) {
            i = rank.getRank();
            rank.setRank(i+1);
        }

        System.out.println("test3: " + rankMap.get(1).getRank());
    }

    public static void update4(){
        Rank rank = rankMap.get(2);
        if(rank == null) {
            synchronized (LOCK4) {
                rank = rankMap.get(2);
                if(rank == null) {
                    rank = new Rank();
                    rankMap.put(2, rank);
                }
            }
        }
        int i;
        synchronized (LOCK4) {
            i = rank.getRank();
            rank.setRank(i+1);
        }

        System.out.println("test4: " + rankMap.get(2).getRank());
    }

    private static Mutex mutex = new Mutex();
    private volatile static int ret;

    public static void main(String[] args) throws Exception{
//        double a = 100;
//        int b = 12929;
//        int c = (int) (b * (a / 10000));
//        System.out.println(c);
//
//        String s = "Hello World";
//        String ss = StringUtils.uncapitalize(s);
//        System.out.println(ss);

//        map.put("test1", 1);
//        map.put("test2", 1);
//        for(int i = 0; i < 2000; i++) {
//            if(i % 2 == 0) {
//                new Thread(() -> update1()).start();
//            } else {
//                new Thread(() -> update2()).start();
//            }
//        }

//        for(int i = 0; i < 2000; i++) {
//            if(i % 2 == 0) {
//                new Thread(() -> update3()).start();
//            } else {
//                new Thread(() -> update4()).start();
//            }
//        }

//        Thread.sleep(5000);
//        System.out.println("test3: " + rankMap.get(1).getRank());
//        System.out.println("test4: " + rankMap.get(2).getRank());
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(100);
//        System.out.println(list);
//        list.add(list.size(), 99);
//        list.add(list.size(), 1);
//        System.out.println(list);
//        System.out.println(list.indexOf(99));
//
//        Map<Integer, RoleRank> rankMap = new HashMap<>();
//        System.out.println(rankMap.size() + " " +rankMap.get(111));

//        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                System.out.println("dd");
//                return "cc";
//            }
//        });
//
//
//
//        System.out.println("aa");
//        new Thread(futureTask).start();
//        System.out.println("bb");
//        while (!futureTask.isDone()) {
//            System.out.println("未完成");
//        }
//        System.out.println("已完成");
//        String s = futureTask.get();
//        System.out.println(s);

//        int i = 1998 * 25000 / 10000;
//        System.out.println(i);

//        char a = 10000;
//        System.out.println(Long.toBinaryString(a));
//        for(; a < 10000; a++) {
//            System.out.println(a);
//            System.out.println(Integer.toBinaryString(a));
//            System.out.println(a & 65280);
//        }
//        int mod = 4;
//        mod %= 3;
//        System.out.println(mod);

//        int i = -2 * -2;
//        System.out.println(new Random().nextInt(5));

//        int time = 3600;
//        time = (int)(time * ((double)(10000 - 5000) / 10000));
//        System.out.println(time);

//        Integer[] arr = new Integer[]{1, 2, 3, 4};
//        List<Integer> list = Arrays.asList(arr);
//        System.out.println(list);
//        System.out.println(arr);

//        long baseHp = 36000000000L;
//        long MIN_HP = 10000000000L;
//
//        int minPercent = (int) (MIN_HP * 10000 / baseHp) - 10000;
//        System.out.println(minPercent);
//
//        long maxHpNew = baseHp * (10000 + minPercent) / 10000;
//        System.out.println(maxHpNew);

//        int zeroTime = dayZeroSecondsFromNow();
//        System.out.println(zeroTime);
//
//        int now = (int)(System.currentTimeMillis() / 1000);
//        System.out.println(now);
//        System.out.println(now - zeroTime);

//        System.out.println(String.class.getClassLoader());
//        System.out.println(Main.class.getClassLoader());

//        Instant instant1 = Instant.ofEpochMilli(System.currentTimeMillis());
//        System.out.println(instant1);
//        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
//        System.out.println(localDateTime1);
//        long day1 = localDateTime1.getLong(ChronoField.EPOCH_DAY);
//        System.out.println(day1);
//        System.out.println(Instant.now());
//        System.out.println(LocalDate.now().atStartOfDay());

//        LocalDateTime localDataTime = LocalDate.now().atStartOfDay();
//        System.out.println(localDataTime.atZone(ZoneId.systemDefault()).toInstant());

//        System.out.println(0x7_FFFFL);

//        Father father = new Son();
//        System.out.println(father.age);
//        father.name();
//        father.age();


//        String a = "0.5";
//        double b = Cast.toDouble(a);
//        System.out.println(b);
//
//        int c = (int)(100 / b);
//        System.out.println(c);
//        System.out.println(12 / 4 * 6);

//        Set<String> set = new HashSet<>();
//        set.add("1#1");
//        set.add("1#2");
//        System.out.println(set.contains("1#1"));

//        for(int i = 0; i < 1000000; i++) {
//            new Thread(() -> addRet()).start();
//        }

//        Thread t = new Thread(() -> {
//            try {
//                while (true) {
//                    Thread.sleep(10000);
//                }
//            } catch (InterruptedException ie) {
//                System.out.println("由于产生InterruptedException异常，退出while(true)循环，线程终止！");
//            }
//        });
//        t.start();
//
//        Thread.sleep(5000);
//        t.interrupt();

//        int x, y;
//        x = 1;
//        try {
//            x = 2;
//            y = 0 / 0;
//        } catch (Exception e) {
//
//        } finally {
//            System.out.println("x = " + x);
//        }

//        WeakReference<String> wsr = new WeakReference<>(new String("wsr"));
//        String sr = "sr";
//
//        System.out.println(wsr.get());
//        System.out.println(sr);
//        System.gc();                //通知JVM的gc进行垃圾回收
//
////        Thread.sleep(10000);
//        System.out.println(wsr.get());
//        System.out.println(sr);

//        add.get();
//
//        for(int i = 0; i <= 100; i++) {
//            add.set(i);
//        }
//        System.out.println(add.get());
//
//        new Thread(() -> {
//            System.out.println(add.get());
//        }).start();

//        ReentrantLock lock = new ReentrantLock();
//
//        for(int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                lock.lock();
////                lock.lock();
//                for(int j = 0; j < 1000; j++) {
//                    ret ++;
//                }
//                lock.unlock();
//            }).start();
//        }
//        Thread.sleep(6000);
//        System.out.println(ret);

        int a = 4;
        Integer b = a;


        int[] as = new int[3];
        Integer[] bs = new Integer[3];
        Object[] os = new Object[2];
//        Object[] os = (Object[])as;
        System.out.println(as.getClass());
        System.out.println(bs.getClass());
        System.out.println(os.getClass());

        Object object = new int[5];
    }

    static ThreadLocal<Integer> add = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };



    private static void addRet() {
        if(mutex.tryLock()) {
            mutex.lock();
            ret++;
            mutex.unlock();
            System.out.println(ret);
        }
    }


    public static int dayZeroSecondsFromNow() {
        return (int) (dayZeroMillsFromNow() / 1000L);
    }

    public static long dayZeroMillsFromNow() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}

class Rank {
    private int rank;
    private int active;

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}

class Father{
    public int age = 80;
    public static void name(){
        System.out.println("father name");
    }
    public void age(){
        System.out.println("father age:"+age);
    }
}
class Son extends Father{
    public int age = 35;
    public static void name(){
        System.out.println("son name");
    }
    public void age(){
        System.out.println("Son age:"+age);
    }
}
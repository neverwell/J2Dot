package com.nemo.test;

import java.util.concurrent.locks.LockSupport;

public class ParkTest {

    public static void main(String[] args) {

        Thread main = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("5s后");
            LockSupport.unpark(main);
        }).start();

        LockSupport.park(main);
    }

}

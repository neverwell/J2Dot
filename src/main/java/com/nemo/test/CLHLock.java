package com.nemo.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CLHLock implements Lock {
    AtomicReference<QNode> tail ;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;

    private static class QNode {
        volatile boolean locked;
    }

    public CLHLock() {
        tail = new AtomicReference<>(new QNode());
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
        myPred = ThreadLocal.withInitial(() -> null);
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        qnode.locked = true;
        //返回之前的尾节点
        QNode pred = tail.getAndSet(qnode);
        myPred.set(pred);

        while (pred.locked) {
            //自旋
//            System.out.println(Thread.currentThread() + " 进入自旋状态");
        }
        System.out.println(Thread.currentThread() + " 当前线程获得锁");
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();
        qnode.locked = false;
        myNode.set(myPred.get());
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    static volatile int ret;

    public static void main(String[] args) {

        CLHLock clhLock = new CLHLock();

        for(int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                try {
                    clhLock.lock();
                    ret ++;
                    clhLock.unlock();
                    System.out.println(ret);
                } catch (Exception e) {
                    System.out.println("报错");
                }
            }, i+"");
            t.start();
        }

//        Thread t1 = new Thread(() -> {
//            try {
//                clhLock.lock();
//                Thread.sleep(5000);
//                clhLock.unlock();
//            } catch (Exception e) {
//
//            }
//        }, "t1");
//
//        Thread t2 = new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                clhLock.lock();
//            } catch (Exception e) {
//
//            }
//        }, "t2");
//
//        t1.start();
//        t2.start();

    }
}

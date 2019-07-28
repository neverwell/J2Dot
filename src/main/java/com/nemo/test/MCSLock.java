package com.nemo.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MCSLock implements Lock{
    private volatile AtomicReference<QNode> tail = new AtomicReference<>(null);
    private ThreadLocal<QNode> myNode = ThreadLocal.withInitial(QNode::new);

    @Override
    public void lock() {
        QNode qNode = myNode.get();
        QNode pred = tail.getAndSet(qNode);
        if(pred != null) {
            qNode.locked = true;
            pred.next = qNode;

            while (qNode.locked) {

            }
        }
    }

    @Override
    public void unlock() {
        QNode qNode = myNode.get();
        if(qNode.next == null) {
            if(tail.compareAndSet(qNode, null)) {
                return;
            }
            while (qNode.next == null) {

            }
        }

        //给下一个node点设置获取锁
        qNode.next.locked = false;
        qNode.next = null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    class QNode {
        volatile boolean locked = false;
        QNode next = null;
    }

    public static void main(String[] args) {
        MCSLock mcsLock = new MCSLock();
        mcsLock.lock();


    }
}

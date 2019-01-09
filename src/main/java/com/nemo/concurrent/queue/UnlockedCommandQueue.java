package com.nemo.concurrent.queue;

import java.util.ArrayDeque;
import java.util.Queue;

//包装了ArrayDeque的队列
public class UnlockedCommandQueue<V> implements ICommandQueue<V> {
    private final Queue<V> queue;
    private boolean running = false;
    private String name;

    public UnlockedCommandQueue() {
        this.queue = new ArrayDeque<>();
    }

    public UnlockedCommandQueue(int numElements) {
        this.queue = new ArrayDeque<>(numElements);
    }

    @Override
    public V poll() {
        return queue.poll();
    }

    @Override
    public boolean offer(V value) {
        return queue.offer(value);
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public void clear() {
        this.queue.clear();
    }
}

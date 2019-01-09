package com.nemo.client;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ClientFuture<R> implements Future<R>{
    private final ClientFuture<R>.Sync sync = new ClientFuture<R>.Sync();

    public static <R> ClientFuture<R> create() {
        return new ClientFuture<>();
    }

    public void result(R result) {
        this.sync.innerSet(result);
    }

    public void failure(Throwable failure) {
        this.sync.innerSetException(failure);
    }

    public R getResult() {
        if(this.isDone()) {
            try {
                return this.get();
            } catch (Throwable e) {

            }
        }
        return null;
    }

    protected void onComplete() {
    }

    public boolean isCancelled() {
        return this.sync.innerIsCancelled();
    }

    public boolean isDone() {
        return this.sync.ranOrCancelled();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.sync.innerCancel(mayInterruptIfRunning);
    }

    public R get() throws InterruptedException, ExecutionException {
        return this.sync.innerGet();
    }

    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.sync.innerGet(unit.toNanos(timeout));
    }

    protected void done() {
        this.onComplete();
    }

    //--------------------同步器---------------------
    private final class Sync extends AbstractQueuedSynchronizer {
        private static final int READY = 0;
        private static final int RESULT = 1;
        private static final int RAN = 2;
        private static final int CANCELLED = 3;
        private R result;
        private Throwable exception;

        private Sync() {
        }

        private boolean ranOrCancelled() {
            return (this.getState() & CANCELLED) != 0;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return this.ranOrCancelled() ? RESULT : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return true;
        }

        boolean innerIsCancelled() {
            return this.getState() == CANCELLED;
        }

        R innerGet() throws InterruptedException, ExecutionException {
            this.acquireSharedInterruptibly(READY);
            if(this.getState() == CANCELLED) {
                throw new CancellationException();
            } else if(this.exception != null) {
                throw new ExecutionException(this.exception);
            } else {
                return this.result;
            }
        }

        R innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException {
            if(!this.tryAcquireSharedNanos(0, nanosTimeout)) {
                throw new TimeoutException();
            } else if(this.getState() == CANCELLED) {
                throw new CancellationException();
            } else if(this.exception != null) {
                throw new ExecutionException(this.exception);
            } else {
                return this.result;
            }
        }

        void innerSet(R v) {
            if(this.compareAndSetState(READY, RESULT)) {
                this.result = v;
                this.setState(RAN);
                this.releaseShared(READY);
                ClientFuture.this.done();
            }
        }

        void innerSetException(Throwable t) {
            if(this.compareAndSetState(READY, RESULT)) {
                this.exception = t;
                this.setState(RAN);
                this.releaseShared(READY);
                ClientFuture.this.done();
            }
        }

        boolean innerCancel(boolean mayInterruptIfRunning) {
            if(this.compareAndSetState(READY, CANCELLED)) {
                this.releaseShared(READY);
                ClientFuture.this.done();
                return true;
            } else {
                return false;
            }
        }
    }
}

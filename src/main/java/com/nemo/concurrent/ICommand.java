package com.nemo.concurrent;

public interface ICommand extends Runnable{
    void doAction();
}

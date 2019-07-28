package com.nemo.test;

import java.util.ArrayList;
import java.util.List;

public class ArraySort implements Runnable{

    private int num;

    private static List<Integer> ret = new ArrayList<>();

    public ArraySort(int num) {
        this.num = num;
    }

    public static void main(String[] args) throws InterruptedException {
        int[] array = {11, 3, 20, 126, 1, 99, 546};
        for(int i : array) {
            new Thread(new ArraySort(i)).start();
        }
        Thread.sleep(1000);
        System.out.println(ret);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(num);
//            System.out.println(num);
            ret.add(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

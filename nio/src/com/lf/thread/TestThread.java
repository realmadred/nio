package com.lf.thread;

import java.util.concurrent.atomic.LongAdder;

/**
 * @auther Administrator
 * @date 2017/8/21
 * @description 描述
 */
public class TestThread {

    public static void main(String[] args) {

        ThreadJoin thread1 = new ThreadJoin(null,"thread1");
        ThreadJoin thread2 = new ThreadJoin(thread1,"thread2");
        ThreadJoin thread3 = new ThreadJoin(thread2,"thread3");

        thread2.start();
        thread3.start();
        thread1.start();

    }
}

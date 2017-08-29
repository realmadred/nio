package com.lf.thread;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

    private static Thread mainThread;

    public static void main(String[] args) throws InterruptedException {

        ThreadA ta = new ThreadA("ta");
        // 获取主线程
        mainThread = Thread.currentThread();

        System.out.println(Thread.currentThread().getName()+" start ta");
        ta.start();

//        LockSupport.unpark(ta);
        // LockSupport会响应中断
        ta.interrupt();
        System.out.println(Thread.currentThread().getName()+" continue");
    }

    static class ThreadA extends Thread{

        public ThreadA(String name) {
            super(name);
        }

        public void run() {
            System.out.println(Thread.currentThread().getName()+" wakup others");
            // 唤醒“主线程”
            LockSupport.park();
            System.out.println("ThreadA over!");
        }
    }
}
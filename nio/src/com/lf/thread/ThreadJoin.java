package com.lf.thread;

/**
 * @auther Administrator
 * @date 2017/8/21
 * @description 描述
 */
public class ThreadJoin extends Thread {

    private Thread prev;

    public ThreadJoin(final Thread prev, String name) {
        super(name);
        this.prev = prev;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+"执行开始！");
            if (prev != null) prev.join(100);
            Thread.sleep(0,100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"执行完成！");
    }
}

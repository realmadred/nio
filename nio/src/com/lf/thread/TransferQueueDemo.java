package com.lf.thread;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

public class TransferQueueDemo {

    private static TransferQueue<String> queue = new LinkedTransferQueue<>();

    public static void main(String[] args) throws Exception {

        new Product().start();
        new Consume().start();

        Thread.sleep(100);

        System.out.println("over.size=" + queue.size());
    }

    static class Product extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10000; i++) {
                    String result = "id=" + i;
                    System.out.println("begin to produce." + result);
                    queue.tryTransfer(result, 1, TimeUnit.SECONDS);
                    queue.transfer(result);
                    System.out.println("success to produce." + result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consume extends Thread {

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10000; i++) {
                    String result = " id=" + i;
                    System.out.println("begin to Consume." + result);
                    System.out.println(queue.take());
                    System.out.println(queue.poll(1,TimeUnit.SECONDS));
                    System.out.println("success to Consume." + result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.lf.thread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLH锁是一种高性能的自旋锁
 */
public class CLHLock {

    private static class Node {  
        private volatile boolean locked = false;//锁状态  
        private volatile Node next;  
    }  
  
    private AtomicReference<Node> tail = new AtomicReference<>();
    private AtomicReference<Node> head = new AtomicReference<>();  
  
    public CLHLock() {  
        Node node = new Node();  
        head.set(node);  
        tail.set(node);  
    }  
    //线程获取锁之后进入锁队列，locked设置为true，后面的线程将阻塞  
    public void lock() {
        Node newNode = new Node();  
        newNode.locked = true;  
        Node pred ;
        while (true) {
            // 加入到队列的尾部
            pred = tail.get();  
            if (tail.compareAndSet(pred, newNode)) {  
                pred.next = newNode;  
                break;  
            }  
        }  
        while (pred.locked) {
            // 后面的线程反复查看前面线程的状态 自旋
        }  
    }  
    //线程释放锁后将自己从锁队列中移除，并将locked修改为false  
    public void unlock() {
        Node h = head.get();  
        Node next = h.next;  
        while (next != null) {  
            if (head.compareAndSet(h, next)) {  
                next.locked = false;  
                break;  
            }  
            h = head.get();  
            next = h.next;  
        }  
    }  
}  
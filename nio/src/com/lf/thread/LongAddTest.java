package com.lf.thread;

import java.util.concurrent.atomic.LongAdder;

/**
 * @auther Administrator
 * @date 2017/8/25
 * @description 描述
 */
public class LongAddTest {

    public static void main(String[] args) {
        LongAdder adder = new LongAdder();
        adder.add(1);
        adder.add(3);
        adder.increment();
        adder.decrement();
        System.out.println(adder.sum());
        adder.add(5);
        System.out.println(adder.sumThenReset());
    }
}

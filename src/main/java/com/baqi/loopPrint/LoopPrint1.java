package com.baqi.loopPrint;

/**
 * 循环打印两个数，synchronized方式
 */
public class LoopPrint1 {
    public static void main(String[] args) {

        Object lock = new Object();

        Runnable r1 = () -> {
            synchronized (lock){
                while (true) {
                    System.out.println(1);
                    try {
                        Thread.sleep(100);
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable r2 = () -> {
            synchronized (lock){
                while (true) {
                    System.out.println(2);
                    try {
                        Thread.sleep(100);
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(r1).start();
        new Thread(r2).start();
    }
}

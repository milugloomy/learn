package com.baqi.loopPrint;

import java.util.concurrent.Semaphore;

/**
 * 循环打印三个数，信号量方式
 */
public class LoopPrint3 {

    public static void main(String[] args) throws InterruptedException {
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(1);
        Semaphore s3 = new Semaphore(1);

        Runnable r1 = () -> {
            while (true) {
                try {
                    s1.acquire();
                    System.out.println(1);
                    s2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                try {
                    s2.acquire();
                    System.out.println(2);
                    s3.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable r3 = () -> {
            while (true) {
                try {
                    s3.acquire();
                    System.out.println(3);
                    s1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        s2.acquire();
        s3.acquire();
        new Thread(r1).start();
        new Thread(r2).start();
        new Thread(r3).start();
    }
}

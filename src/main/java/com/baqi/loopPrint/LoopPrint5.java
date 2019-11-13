package com.baqi.loopPrint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 循环打印三个数，condition方式
 */
public class LoopPrint5 {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();
        Condition c3 = lock.newCondition();

        Runnable r1 = () -> {
            while (true) {
                try {
                    lock.lock();
                    System.out.println(1);
                    c2.signal();
                    c1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                try {
                    lock.lock();
                    System.out.println(2);
                    c3.signal();
                    c2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable r3 = () -> {
            while (true) {
                try {
                    lock.lock();
                    System.out.println(3);
                    c1.signal();
                    c3.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        new Thread(r1).start();
        new Thread(r2).start();
        new Thread(r3).start();
    }
}

package com.baqi.loopPrint;

/**
 * 循环打印三个数，volatile方式
 */
public class LoopPrint4 {

    static volatile Integer flag = 1;

    public static void main(String[] args) {
        Runnable r1 = () -> {
            while (true) {
                if(flag == 1) {
                    System.out.println(1);
                    flag = 2;
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                if(flag == 2) {
                    System.out.println(2);
                    flag = 3;
                }
            }
        };

        Runnable r3 = () -> {
            while (true) {
                if(flag == 3) {
                    System.out.println(3);
                    flag = 1;
                }
            }
        };

        new Thread(r1).start();
        new Thread(r2).start();
        new Thread(r3).start();
    }
}

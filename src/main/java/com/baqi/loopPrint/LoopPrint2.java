package com.baqi.loopPrint;

/**
 * 循环打印两个数，volatile方式
 */
public class LoopPrint2 {

    static volatile Boolean flag = false;

    public static void main(String[] args) {

        Runnable r1 = () -> {
            while (true) {
                if(flag) {
                    System.out.println(1);
                    flag = false;
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                if(!flag) {
                    System.out.println(2);
                    flag = true;
                }
            }
        };

        new Thread(r1).start();
        new Thread(r2).start();
    }
}

package com.iyo.juc;

import java.util.concurrent.TimeUnit;
public class DaemonTempl {
    /**
     * 用户线程和守护线程
     *    用户线程：由用户程序创建和管理的线程，它是进程内部的轻量级执行单元，每个线程都可以执行不同的任务或功能，但它们共享同一进程的内存空间和系统资源。
     *    守护线程：守护线程是运行在后台的线程，它通常用于执行一些后台任务，例如清理垃圾、监控内存等。当进程中的所有用户线程都结束时，守护线程也会自动结束。
     * 演示：
     *    若不对新建的线程做设置，默认为用户线程，执行main方法因为死循环不会停止，若设置为守护线程，则main方法结束。
     */
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 开始运行"+
                    (Thread.currentThread().isDaemon()?"守护线程":"用户线程"));
            while(true){

            }
        },"Thread-1");

        // t.setDaemon(true);
        t.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() +"\t 主线程");
    }
}

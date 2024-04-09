package com.iyo.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:iyo
 * @Date:2024/4/10 2:22
 * @Description: ReentrantLock
 *   1. 公平锁与非公平锁
 *     new ReentrantLock() 创建实例时给入参数 true 则表示是公平锁，默认为false。强行设置为公平锁可能会使线程的切换占用资源导致性能下降
 *   2. 可重入锁
 *     可重入锁是指在同步方法或代码块中可以进入持有相同锁的同步方法或代码块。比如加锁的一个递归方法需要反复调用自身。
 *     默认synchronized和ReentrantLock都是可重入锁
 */
public class ReentrantLockTempl {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread( ()-> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        },"thread-1").start();
        new Thread( ()-> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        },"thread-2").start();
        new Thread( ()-> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        },"thread-3").start();

    }
}
class Ticket{
    private int num = 50;
    // ReentrantLock lock = new ReentrantLock();
    ReentrantLock lock = new ReentrantLock(true);

    public void sale(){
        lock.lock();
        try {
            if(num >0){
                System.out.println(Thread.currentThread().getName() + " 卖出第" + (num--) + " 还剩下" + num + "张");
            }
        }finally {
            lock.unlock();
        }
    }
}

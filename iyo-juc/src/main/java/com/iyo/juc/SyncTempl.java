package com.iyo.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:iyo
 * @Date:2024/4/10 1:38
 * @Description: synchronized 的使用
 *   1. synchronized 修饰静态方法 -> 锁的是当前类的Class对象
 *   2. synchronized 修饰实例方法 -> 锁的是当前实例，如A实例执行该方法时，B实例发起执行是无需等待
 *   3. synchronized 修饰代码块 -> 锁的的synchronized(o) 括号中的对象
 */
public class SyncTempl {
    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone2 = new Phone();
        new Thread( ()-> {
            // System.out.println(Thread.currentThread().getName() + phone.sendMail());
            // System.out.println(Thread.currentThread().getName() + phone.sendWeChat());
            System.out.println(Thread.currentThread().getName() + phone.sendMsg());
        },"thread-1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread( ()-> {
            System.out.println(Thread.currentThread().getName() + phone2.sendMail());
            // System.out.println(Thread.currentThread().getName() + phone2.sendWeChat());
            // System.out.println(Thread.currentThread().getName() + phone.sendMsg());
        },"thread-2").start();

    }
}

class Phone{

    public /*static*/ synchronized String sendMsg(){
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return " sendMsg";
    }
    public /*static*/ synchronized String sendMail(){
        return " sendMail";
    }

    public String sendWeChat(){
        Object o = new Object();
        synchronized (o){
            return " sendWeChat";
        }
    }
}
package com.iyo.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:iyo
 * @Date:2024/4/14 5:45
 * @Description: 中断线程示例
 *   1.volatile关键字
 *   2.AtomicBoolean
 *   3.Thread类api
 *
 * notion:
 *   1.在使用 Lambda 表达式时，如果捕获了外部作用域中的变量，则这些变量必须是 最终的（final） 或 实际上的最终的（effectively final）.
 *   2.Lambda 表达式可以改变静态变量，这是由于静态变量自身的特性——类级别的共享状态和不受作用域限制。
 *      不过对静态变量的修改必须考虑到多线程环境下的线程安全性问题
 *   3.Thread的实例方法interrupt()，不会立即停止线程，只是修改了中断标识位，也就是说即使中断标识位为ture，线程仍可能在执行。
 */
public class InterruptTempl {

    static volatile boolean volatileStop = false;
    static AtomicBoolean autoStop = new AtomicBoolean(false);
    public static void main(String[] args) {
        // demoMethod1();
        // demoMethod2();
        demoMethod3();
    }

    /**
     * @Description: 使用volatile关键字
     */
    public static void demoMethod1(){
        // TODO: 这里如果将volatileStop前面的关键字去掉仍然可以终端线程，那么使用volatile修饰的意义是什么呢?
        Thread thread1 = new Thread(() -> {
            while (true) {
                if (volatileStop) {
                    System.out.println("thread-1线程被 volatile修饰的变量 中断");
                    break;
                }
                System.out.println("thread-1线程运行中");
            }
        }, "thread-1");
        thread1.start();
        try{
            TimeUnit.MILLISECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(()->{
            System.out.println("thread-2线程中断thread-1线程");
            volatileStop = true;
        },"thread-2").start();
    }

    public static void demoMethod2(){

        Thread thread1 = new Thread(() -> {
            while (true) {
                if (autoStop.get()) {
                    System.out.println("thread-1线程被 AtomicBoolean原子类 中断");
                    break;
                }
                System.out.println("thread-1线程运行中");
            }
        }, "thread-1");
        thread1.start();
        try{
            TimeUnit.MILLISECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(()->{
            System.out.println("thread-2线程中断thread-1线程");
            autoStop.set(true);
        },"thread-2").start();
    }

    /**
     * @Description:
     *   1.interrupt方法中断线程
     *   2.interrupt方法只是将线程的中断标志位设置为true，并不是真正的中断线程
     *   3.interrupt方法底层也是通过修改volatile修饰的布尔变量来控制线程的运行  {private volatile boolean interrupted;}
     */
    public static void demoMethod3(){

        Thread thread1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("thread-1线程被 interrupt方法 中断");
                    break;
                }
                System.out.println("thread-1线程运行中");
            }
        }, "thread-1");
        thread1.start();
        try{
            TimeUnit.MILLISECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(()->{
            System.out.println("thread-2线程中断thread-1线程");
            thread1.interrupt();
        },"thread-2").start();
    }

}

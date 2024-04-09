package com.iyo.juc;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Author:Flynn
 * @Date:2024/4/9 17:54
 * @Description: 引入CompletableFuture
 *   1. jdk8新增的CompletableFuture实现了Future和CompletionStage接口
 *   2. CompletableFuture可以通过空参构造获取，但并不推荐，推荐使用静态方法 runAsync()和supplyAsync()获取，在没有指定线程池的
 *     情况下使用默认的ForkJoinPool，
 *   3. CompletableFuture中的join()相比get()是都能获取返回值，但join()没有检查性异常
 *   4. CompletableFuture中的whenComplete()和exceptionally()的使用
 */
public class CompletableFutureTempl {
    public static void main(String[] args) {
        // 使用4种runAsync()和supplyAsync()的方法获取CompletableFuture
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t completableFuture1 implements runnable");
        });
        CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t completableFuture2 implements runnable -指定线程池");
        }, threadPool);

        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t completableFuture3 implements callable");
            return "callable result";
        });
        // whenComplete(BiConsumer<? super T, ? super Throwable> action) 类似回调方法，当子线程执行结束后自动返回结果，不用主动的使用get()或join()去获取
        // exceptionally(Function<Throwable, ? extends T> fn) 处理子线程有异常的情况
        CompletableFuture<String> completableFuture4 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t completableFuture4 implements callable -指定线程池");
            int ints = ThreadLocalRandom.current().nextInt(10);
            if(ints > 5){
                int i = 10/0;
            }
            return "线程执行随机数: " +ints;
        },threadPool).whenComplete((v,e)->{
            if(e==null){
                System.out.println("执行成功 "+ v);
            }
        }).exceptionally(e -> {
            System.out.println("执行失败");
            e.printStackTrace();
            return null;
        });
        // join()获取线程执行结果，没有检察性异常
        System.out.println(Thread.currentThread().getName()+"\t "+completableFuture3.join());
        // get()获取线程执行结果，需要处理检察性异常
        try {
            System.out.println(Thread.currentThread().getName()+"\t "+completableFuture3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
}

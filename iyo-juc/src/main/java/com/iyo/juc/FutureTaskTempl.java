package com.iyo.juc;

import java.util.concurrent.*;

/**
 * Future的优缺点：
 *   1.优-Future+线程池的组合执行异步多线程任务可以显著提升程序的执行效率。
 *   2.缺-一旦调用get()获取线程执行结果，就必须等待该线程执行完成，这容易造成线程阻塞。
 *   3.缺-调用isDone()轮询线程是否执行完毕，再获取线程执行结果，但这样可能会空耗cpu资源
 */
public class FutureTaskTempl {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        FutureTask futureTask1 = new FutureTask(() -> {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("FutureTask-1");
            return "FutureTask--1";
        });
        FutureTask futureTask2 = new FutureTask(() -> {
            TimeUnit.MILLISECONDS.sleep(400);
            System.out.println("FutureTask-2");
            return "FutureTask--2";
        });
        FutureTask futureTask3 = new FutureTask(() -> {
            TimeUnit.MILLISECONDS.sleep(6000);
            System.out.println("FutureTask-3");
            return "FutureTask--3";
        });
        threadPool.submit(futureTask1);
        threadPool.submit(futureTask2);
        threadPool.submit(futureTask3);
        // System.out.println(futureTask3.get());
        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());
        // 设置等待时间，超时仍未获取到返回值则抛出超时异常
        try {
            System.out.println(futureTask3.get(1000,TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            System.out.println("FutureTask-3 任务超时");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("任务执行耗时:  "+(endTime-startTime)+"毫秒");
        threadPool.shutdown();
    }
    
}

package com.iyo.juc;

import java.util.concurrent.*;

/**
 * 创建线程的四种方法
 * 1. 继承Thread类
 * 2. 实现Runnable接口
 * 3. 实现Callable接口
 * 4. 线程池
 */
public class ThreadTempl {

    public static void main(String[] args){
        // 1.继承Thread类
        new MyThread().start();
        // 2.实现Runnable接口
        new Thread(new MyRunnable()).start();
        // 3.实现Callable接口，实现Callable接口的类不能直接启动，需要包装成FutureTask。其中call()方法的返回值由FutureTask.get()获得
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask).start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // 4.线程池 创建固定数量线程
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        // execute() 参数为Runnable接口的实现类 返回类型为void
        threadPool.execute(()->{
            System.out.println("executors runnable");
        });
        // submit() 参数为Callable接口的实现类 返回类型为Future
        Future<String> future = threadPool.submit(() -> "executors callable");
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        threadPool.shutdown();
    }
}
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("extends thread");
    }
}
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("implements runnable");
    }
}
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "implements callable";
    }
}


package com.iyo.juc;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author:iyo
 * @Date:2024/4/9 20:49
 * @Description:模拟电商价格比较
 *   1.需求：a.同一商品在各个电商平台的价格
 *          b.商品在同一个平台各个商家的价格
 *   2.技术要点：a.函数式接口
 *                接口类型|参数|返回值
 *                Function(功能型)|有|有
 *                Consumer(消费型)|有|无
 *                Supplier(供给型)|无|有
 *             b.链式编程
 *             c.Stream流式计算
 */
public class PriceCompareDemo {

    static List<NetMall> mallList= Arrays.asList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("tmall"),
            new NetMall("taobao"),
            new NetMall("pdd")
    );

    // 每个电商网站挨个查询
    public static List<String> getPrice(List<NetMall> list,String productName){
        // String.format() % 为占位符, s 表示字符串,.2f 表示保留两位的浮点型数字
        return list.stream()
                .map(netMall ->
                        String.format(productName + " in %s price is %.2f", netMall.getMallName(),
                                netMall.queryPrices(productName)))
                .collect(Collectors.toList());
    }

    // 并发查询各电商网站的商品价格
    public static List<String> getPriceByCompletableFuture(List<NetMall> list,String productName){
        return list.stream().map(netMall ->
                        CompletableFuture.supplyAsync(() ->
                                String.format(productName + " in %s price is %.2f", netMall.getMallName(),
                                        netMall.queryPrices(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(completableFuture -> completableFuture.join())
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> list = getPrice(mallList, "mysql");
        for (String element:
             list) {
            System.out.println(element);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时: "+(endTime - startTime)+"ms");
        System.out.println("----------------------------");
        long startTime2 = System.currentTimeMillis();
        List<String> list2 = getPriceByCompletableFuture(mallList, "mysql");
        for (String element:
                list2) {
            System.out.println(element);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("耗时: "+(endTime2 - startTime2)+"ms");
    }
}

class NetMall{
    @Getter
    private String mallName;

    public NetMall (String mallName){
        this.mallName = mallName;
    }
    public Double queryPrices(String productName){
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // m 的ASCII码值
        // System.out.println("mysql".charAt(0)+1);
        return ThreadLocalRandom.current().nextDouble() * 2 + "mysql".charAt(0);

    }

}
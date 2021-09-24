package java8start.completablefuturetest;

import enums.Code;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * N
 *
 * @author wusd
 * @date : 2021/09/23 14:05
 */
@Data
public class Shop {
    private String name;

    private Random random = new Random();

    public Shop(String name) {
        this.name = name;
    }

    // 异步获取商品价格
    public Future<Double> getPriceAsyn(String product) {
        // 原始方式
        /*CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = getPrice(product);
                future.complete(price);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();
        return future;*/

        // lambda
        return CompletableFuture.supplyAsync(() -> getPrice(product));
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    // 返回shop-name:product:price:code格式订单信息
    public String getPriceString(String product) {
        double price = calculatePrice(product);
        // 模拟折扣
        Code code = Code.values()[random.nextInt(Code.values().length)];
        return String.format("%s:%s:%.2f:%s", this.getName(), product, price, code);
    }

    private double calculatePrice(String product) {
        // 模拟查询操作耗时
        delay();
        // 模拟商品价格
        return product.charAt(0) * this.name.charAt(0) * random.nextDouble();
    }

    private void delay() {
        // 模拟耗时1s
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

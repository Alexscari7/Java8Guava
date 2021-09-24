package java8start.completablefuturetest;

import enums.Code;

import java.util.Random;

/**
 * 模拟折扣(远程折扣服务，模拟耗时)
 *
 * @author wusd
 * @date : 2021/09/23 14:04
 */
public class Discount {

    public static String applyDiscount(Quote quote) {
        return String.format("%s cost %.2f in %s", quote.getProduct(), apply(quote.getPrice(),
                quote.getCode()), quote.getShopName());
    }

    private static Double apply(double price, Code code) {
        delay();
        return price * (100 - code.getPercentage()) / 100;
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 随机时间
    public static String applyDiscountRD(Quote quote) {
        return String.format("%s cost %.2f in %s", quote.getProduct(), applyRD(quote.getPrice(),
                quote.getCode()), quote.getShopName());
    }

    private static Double applyRD(double price, Code code) {
        delayRD();
        return price * (100 - code.getPercentage()) / 100;
    }

    private static void delayRD() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

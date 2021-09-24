package java8start.completablefuturetest;

import enums.Code;
import lombok.Data;

/**
 * 模拟订单
 *
 * @author wusd
 * @date : 2021/09/23 14:05
 */
@Data
public class Quote {
    private String shopName;
    private String product;
    private double price;
    private Code code;

    public Quote(String shopName, String product, double price, Code code) {
        this.shopName = shopName;
        this.product = product;
        this.price = price;
        this.code = code;
    }

    public static Quote parseToQuote(String order) {
        String[] values = order.split(":");
        return new Quote(values[0], values[1], Double.parseDouble(values[2]), Code.valueOf(values[3]));
    }
}

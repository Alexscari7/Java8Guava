package completablefuturetest;

import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Completion;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wusd
 * @description 异步获取最优价格
 * @createtime 2019/09/17 15:25
 */
public class BestPriceEp {
    @Test
    public void tfGetPrice(){
        getPrice("eggtart");
    }

    // 简单的异步获取
    private void getPrice(String product) {
        Shop family = new Shop("family");
        long startTime = System.nanoTime();
        Future<Double> future = family.getPriceAsyn(product);
        long invocationTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println(String.format("Get price takes %dms", invocationTime));
        try {
            Double price = future.get();
            System.out.println(String.format("The price is $%g", price));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long retrievalTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println(String.format("Price return after %dms", retrievalTime));
    }

    @Test
    public void tfGetAllPrice() {
        //getAllPriceUseStream("flower");
        getAllPriceUseCompletionFuture("cat");
    }

    // 使用并行流获取所有价格(并行流自动获取系统最大线程数，比如此机器最大12线程数，则查询12个商家耗时1ms，查询13个商家时耗时2ms)
    private void getAllPriceUseStream(String product) {
        List<Shop> shops = Arrays.asList(new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"));
        System.out.println(shops.size());
        long startTime = System.nanoTime();
        List<String> prices = shops.parallelStream()
                .map(shop -> String.format("%s costs %.2f in %s", product, shop.getPrice(product), shop.getName())).collect(Collectors.toList());
        prices.stream().forEach(System.out::println);
        System.out.println(String.format("Get all price takes %dms", (System.nanoTime() - startTime) / 1_000_000));
    }

    // 使用CompletionFuture获取所有价格(ForkJoinPool线程池数量为最大线程数12-1=11)，并且可以指定Executor
    // 为什么不能直接在同一个流上使用两次map():在某个线程上执行完CompletableFuture::join得到返回数据后才能再次创建CompletableFuture对象。
    // 如果在同一流水线上，执行CompletableFuture.supplyAsync()后直接进入下一次map，再执行CompletableFuture::join,
    // 得到返回数据后才能再次执行CompletableFuture.supplyAsync()创建线程。
    // 在串行流中，只有一条流水线，对于流中的元素都走一遍流水线（重点理解，不是执行完一整个操作后进入下一个操作）
    // 在并行流中，以最大线程数的流水线操作
    private void getAllPriceUseCompletionFuture(String product) {
        List<Shop> shops = Arrays.asList(new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"));
        long startTime = System.nanoTime();
        // 自定义线程池，使用守护线程的方式执行，当程序退出时，线程也会被回收。
        ExecutorService service = Executors.newFixedThreadPool(12, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        // 执行CompletableFuture.supplyAsync()相当于创建了线程并开始运行。
        List<CompletableFuture<String>> prices = shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s costs %.2f in %s", product,
                            shop.getPrice(product), shop.getName()), service)).collect(Collectors.toList());
        // 分开使用两个map后，相当于对CompletableFuture集合中的每个元素执行join,相当于get()方法，但是这里不会抛出任何异常，只会阻塞。每次最多有(线程池最大线程数量)消费
        prices.stream().map(CompletableFuture::join).forEach(System.out::println);
        System.out.println(String.format("Get all price takes %dms", (System.nanoTime() - startTime) / 1_000_000));
    }

    @Test
    public void tfFindPrices() {
        findPrices("eggtart");
    }

    // 使用CompletionFuture执行多个异步任务
    private void findPrices(String product) {
        List<Shop> shops = Arrays.asList(new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"), new Shop("walmart"), new Shop("jialefu"));
        long startTime = System.nanoTime();
        List<CompletableFuture<String>> futures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceString(product)))
                        .map(future -> future.thenApply(Quote::parseToQuote))
                        .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))))
                        .collect(Collectors.toList());
        futures.stream().map(CompletableFuture::join).forEach(System.out::println);
        System.out.println(String.format("Get all price takes %dms", (System.nanoTime() - startTime) / 1_000_000));
    }

    @Test
    public void tfFindBestPrice() {
        findBestPrice("eggtart");
    }

    private void findBestPrice(String product) {
        List<Shop> shops = Arrays.asList(new Shop("costco"), new Shop("walmart"), new Shop("jialefu"), new Shop("family"),
                new Shop("costco"));
        long startTime = System.nanoTime();
        CompletableFuture[] futures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceString(product)))
                .map(future -> future.thenApply(Quote::parseToQuote))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscountRD(quote))))
                .map(future -> future.thenAccept(s -> System.out.println(s + "====done in " + (System.nanoTime() - startTime) / 1_000_000))).toArray(size -> new CompletableFuture[size]);
        // CompletableFuture.allOf(futures).join(); // 阻塞至所有任务完成
        CompletableFuture.anyOf(futures).join();    // 阻塞至最快的任务完成
        System.out.println(String.format("Get all price takes %dms", (System.nanoTime() - startTime) / 1_000_000));
    }

}

@Data
class Shop {
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
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
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

// 模拟折扣(远程折扣服务，模拟耗时)
class Discount {
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code (int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return String.format("%s cost %.2f in %s", quote.getProduct(), apply(quote.getPrice(),
                quote.getCode()), quote.getShopName());
    }

    private static Double apply(double price, Code code) {
        delay();
        return price * (100 - code.percentage) / 100;
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
        return price * (100 - code.percentage) / 100;
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

// 模拟订单
@Data
class Quote {
    private String shopName;
    private String product;
    private double price;
    private Discount.Code code;

    public Quote (String shopName, String product, double price, Discount.Code code) {
        this.shopName = shopName;
        this.product = product;
        this.price = price;
        this.code = code;
    }

    public static Quote parseToQuote(String order) {
        String[] values = order.split(":");
        return new Quote(values[0], values[1], Double.parseDouble(values[2]), Discount.Code.valueOf(values[3]));
    }
}
package java8start;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/03/28 17:25
 */
public class AppleTest {
    private static void prettyApplePrinter(List<Apple> apples, AppleFormatter appleFormatter) {
        apples.forEach(t -> {
            System.out.println(appleFormatter.accept(t));
        });
        apples.forEach(System.out::println);
    }


    //行为抽象化 简洁度:lambda表达式>内部类>类
    @Test
    public void test() {
        ArrayList<Apple> apples = Lists.newArrayList(new Apple("red", 11d), new Apple("green", 4d));
        //类
        prettyApplePrinter(apples, new AppleHeavyPrinter());
        prettyApplePrinter(apples, new AppleSimplePrinter());
        //匿名内部类
        prettyApplePrinter(apples, new AppleFormatter() {
            @Override
            public String accept(Apple apple) {
                return null;
            }
        });
    }

    //方法引用
    @Test
    public void test2(){
        /*ArrayList<Apple> apples = Lists.newArrayList(new Apple("red", 11d), new Apple("green", 4d));
        apples.sort(Comparator.comparingDouble(Apple::getWeight));
        Supplier<Apple> supplier = Apple::new;
        Apple a1 = supplier.get();
        System.out.println(a1.toString());

        BiFunction<String, Double, Apple> function = Apple::new;
        Apple a2 = function.apply("cool", 20.0);
        System.out.println(a2.toString());

        List<Integer> ns = Arrays.asList(10, 29, 44);
        ns.forEach(t -> System.out.println(t));
        ns.forEach(System.out::println);*/

        /*List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> result = numbers1.stream()
                .flatMap(i -> numbers2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[]{i, j}))
                .collect(Collectors.toList());
        result.forEach(t -> System.out.println(Arrays.toString(t)));
        trans.stream().filter(Transactions t -> t.getYear() == 2011).sorted(Comparator<Transactions>.comparing(Transation::getValue)).collect(toList());
        trans.stream().map(transaction -> transaction.getTrader().getCity()).collect(toSet());
        trans.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity())).distinct()
                .sorted(Comparator.comparing(Trader::getName)).collect(toList());
        trans.stream().map(transaction -> transaction.getTrader().getName()).distinct().sorted().reduce(" ", (n1, n2) -> n1 + n2);
        trans.stream().anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));
        trans.stream().filter(transaction -> transaction.getTrader().getCity().equals("Cambridge")).map(Transaction::getValue)
                .forEach(System.out::println);
        trans.stream().map(Transaction::getValue).reduce(Integer::max);*/
        IntStream intStream = IntStream.range(1, 100).filter(t -> t % 2 == 0);
        System.out.println(intStream.count());
    }
}

@FunctionalInterface
interface AppleFormatter{
    String accept(Apple apple);
}

class AppleHeavyPrinter implements AppleFormatter {
    @Override
    public String accept(Apple apple) {
        return "A " + (apple.getWeight()>10 ? "heavy " : "light ") + apple.getColor() + " apple";
    }
}

class AppleSimplePrinter implements AppleFormatter {
    @Override
    public String accept(Apple apple) {
        return "An apple of " + apple.getWeight()+"g";
    }
}
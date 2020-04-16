import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.*;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/03/29 9:47
 */
public class LambdaForRunable {

    public static void processor(Runnable runnable){
        runnable.run();
    }

    @Test
    public void test(){
        Runnable r1 = ()->System.out.println("Hello World 1");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World 2");
            }
        };
        processor(r1);
        processor(r2);
        processor(()-> System.out.println("Hello  World 3"));
        BiFunction<String, Double, Apple> biFunction = Apple::new;
        Apple apple = biFunction.apply("green", 60d);
        Predicate<Apple> redApple = a -> "red".equals(a.getColor());
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 100);
        Predicate<Apple> morePredicateApple = redApple.and(a -> a.getWeight() > 100).or(a -> "green".equals(a.getColor()));
    }


}

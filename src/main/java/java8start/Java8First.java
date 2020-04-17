package java8start;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/03/28 15:12
 */
public class Java8First {
    private static List<Apple> ApplesFilter(List<Apple> apples, Predicate<Apple> applePredicate) {
        List<Apple> result = Lists.newArrayList();
        apples.forEach(t->{
            if (applePredicate.test(t)) {
                result.add(t);
            }
        });
        return result;
    }

    @Test
    public void test1(){
        ArrayList<Apple> apples = Lists.newArrayList(new Apple("red",11d), new Apple("green", 4d));
        apples.sort(Comparator.comparing(Apple::getWeight));
        //System.out.println(ApplesFilter(apples, Apple::isHeavyApple));
        //System.out.println(ApplesFilter(apples, (Apple a) -> a.getWeight() > 10 || "green".equals(a.getColor())));
        List<Apple> filtedApples = apples.stream().filter((Apple a) -> a.getWeight() > 10).collect(Collectors.toList());
        System.out.println(filtedApples);
    }

}

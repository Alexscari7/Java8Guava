package designdemo;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/07/29 13:56
 */
public class StrategyMode {
    private Predicate<String> predicate;
    public StrategyMode(Predicate<String> predicate){
        this.predicate = predicate;
    }
    public boolean validate(String s){
        return predicate.test(s);
    }

    @Test
    public void test(){
        StrategyMode strategyMode = new StrategyMode((String s) -> s.matches("\\d+"));
        boolean b = strategyMode.validate("wsd");
        System.out.println(b);
        System.out.println(111);
    }
}

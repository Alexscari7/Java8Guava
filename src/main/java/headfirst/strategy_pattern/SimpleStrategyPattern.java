package headfirst.strategy_pattern;

/**
 * N
 *
 * @author wusd
 * @date : 2021/07/15 14:25
 */
public class SimpleStrategyPattern {
    public static void main(String[] args) {
        Context context = new Context(new StrategyA());
        context.method();
    }
}

interface IStrategy {
    void strategyMethod();
}

class StrategyA implements IStrategy {
    @Override
    public void strategyMethod() {
        System.out.println("执行策略A");
    }
}

class Context {
    private IStrategy strategy;

    Context(IStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public IStrategy getStrategy() {
        return strategy;
    }

    public void method() {
        strategy.strategyMethod();
    }
}
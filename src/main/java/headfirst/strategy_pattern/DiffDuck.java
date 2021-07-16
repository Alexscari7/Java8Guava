package headfirst.strategy_pattern;

import org.junit.jupiter.api.Test;

/**
 * @author wusd
 * @description Strategy pattern 策略模式
 * @createtime 2019/08/08 16:26
 */
public class DiffDuck extends Duck {
    public DiffDuck(){
        this.flyBehavior = new FlyWithWings();
        this.quackBehavior = new QuackLikeGAGA();
    }

    public DiffDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior){
        this.flyBehavior = flyBehavior;
        this.quackBehavior = quackBehavior;
    }

    public void performFly(){
        flyBehavior.fly();
    }

    public void performQuack(){
        quackBehavior.quack();
    }
}

class Test23 {
    @Test
    public void testDuck(){
        DiffDuck duck = new DiffDuck(new FlyWithWings(), new QuackLikeGAGA());
        duck.performFly();
        duck.performQuack();
        duck.setQuackBehavior(new QuackLikeWAWA());
        duck.performQuack();
    }
}

class Duck {
    //会飞（有的会飞，有的不会)
    protected FlyBehavior flyBehavior;
    //会叫（嘎嘎，哇哇）
    protected QuackBehavior  quackBehavior;

    //会游泳
    public void swim(){
        System.out.println("I can swim");
    }
    //会show
    public void show(){
        System.out.println("Show time");
    }

    public void setFlyBehavior(FlyBehavior flyBehavior){
        this.flyBehavior = flyBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}

interface FlyBehavior {
    void fly();
}

interface QuackBehavior {
    void quack();
}

class FlyWithWings implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("I can fly");
    }
}

class FlyNoWay implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("I can't fly");
    }
}

class QuackLikeGAGA implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("GAGA");
    }
}

class QuackLikeWAWA implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("WAWA");
    }
}
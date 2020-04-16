package headfirst;

/**
 * @author wusd
 * @description Decorator Pattern 装饰者模式
 * @createtime 2019/08/28 15:54
 */
public class StarBucks {
    public static void main(String[] args) {
        Beverage myBeverage = new Lemon(new Mocha(new Espresso()));
        System.out.println(myBeverage.cost());
        System.out.println(myBeverage.getDescription());
    }
}

// 饮料基类
abstract class Beverage {
    String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    abstract double cost();
}
// 具体饮料类
class Espresso extends Beverage {

    Espresso(){
        description = "Espresso";
    }

    @Override
    double cost() {
        return 0.99;
    }
}
class Decaf extends Beverage {

    Decaf(){
        description = "Decaf";
    }

    @Override
    double cost() {
        return 1.29;
    }
}
// 配料基类
abstract class CondimentDecorator extends Beverage {
    protected  Beverage beverage;
}
// 具体调料类
class Lemon extends CondimentDecorator {
    Lemon(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return "Lemon, " + beverage.getDescription();
    }

    @Override
    double cost() {
        return 0.29 + beverage.cost();
    }
}
class Mocha extends CondimentDecorator {
    Mocha(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return "Mocha, " + beverage.getDescription();
    }

    @Override
    double cost() {
        return 0.39 + beverage.cost();
    }
}

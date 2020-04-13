package headfirst;

/**
 * @author wusd
 * @description Factory pattern 工厂模式
 * @createtime 2019/10/08 14:33
 */
public class PizzaFactory {
}

abstract class Pizza {
    abstract void bake();
}

class APizza extends Pizza {
    @Override
    void bake() {

    }
}
class BPizza extends Pizza {
    @Override
    void bake() {

    }
}
class CPizza extends Pizza {
    @Override
    void bake() {

    }
}
class DPizza extends Pizza {
    @Override
    void bake() {

    }
}
class DefaultPizza extends Pizza {
    @Override
    void bake() {

    }
}

// 抽象工厂
abstract class SimplePizzaStore {

    public Pizza orderPizza(String type){
        Pizza pizza = createPizza(type);
        pizza.bake();
        return pizza;
    }

    // 抽象方法创建对象
    abstract Pizza createPizza(String type);
}

class ChicagoPizzaStore extends SimplePizzaStore {
    @Override
    Pizza createPizza(String type) {
        Pizza pizza;
        switch (type) {
            case "A":
                pizza = new APizza();
                break;
            case "B":
                pizza = new BPizza();
                break;
            default:
                pizza = new DefaultPizza();
        }
        return pizza;
    }
}
class XianPizzaStore extends SimplePizzaStore {
    @Override
    Pizza createPizza(String type) {
        Pizza pizza;
        switch (type) {
            case "C":
                pizza = new CPizza();
                break;
            case "D":
                pizza = new DPizza();
                break;
            default:
                pizza = new DefaultPizza();
        }
        return pizza;
    }
}
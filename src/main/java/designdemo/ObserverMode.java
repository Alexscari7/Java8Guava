package designdemo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wusd
 * @description 空
 * @createtime 2019/07/29 14:43
 */
public class ObserverMode {
}

interface Observer {
    void notify(String s);
}

class MoneyObserver implements Observer{
    @Override
    public void notify(String s) {
        if (s != null && s.contains("money")){
            System.out.println("Money News!");
        }
    }
}
class ArmyObserver implements Observer{
    @Override
    public void notify(String s) {
        if (s != null && s.contains("army")){
            System.out.println("Army News!");
        }
    }
}
class FightObserver implements Observer{
    @Override
    public void notify(String s) {
        if (s != null && s.contains("fight")){
            System.out.println("Fight News!");
        }
    }
}

interface Listener{
    void registerObserver(Observer observer);

    void notifyObserver(String s);
}

class MyListener implements Listener{
    private final List<Observer> observerList = new ArrayList<>();
    @Override
    public void registerObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObserver(String s) {
        observerList.forEach(o -> o.notify(s));
    }

    @Test
    public void test(){
        Listener listener = new MyListener();
        listener.registerObserver(new MoneyObserver());
        listener.registerObserver(new ArmyObserver());
        listener.registerObserver(new FightObserver());
        listener.registerObserver((String s) -> {
            if (s.contains("123")){
                System.out.println("123 is here");
            }
        });
        listener.notifyObserver("ME This is my money,fight");
    }
}
package headfirst.observer_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * N
 *
 * @author wusd
 * @date : 2021/07/20 11:20
 */
public class SimpleObserverPattern {
    public static void main(String[] args) {
        ISubject subject = new SubjectA();
        subject.add(new ObserverA());
        subject.notifyThem();
        
    }
}

interface ISubject {
    void add(IObserver o);
    void remove(IObserver o);
    void notifyThem();
}

interface IObserver {
    void response();
}

class SubjectA implements ISubject {
    private List<IObserver> observerList;

    SubjectA() {
        observerList = new ArrayList<>();
    }

    @Override
    public void add(IObserver o) {
        observerList.add(o);
    }

    @Override
    public void remove(IObserver o) {
        observerList.remove(o);
    }

    @Override
    public void notifyThem() {
        for (IObserver observer : observerList) {
            observer.response();
        }
    }
}

class ObserverA implements IObserver {
    @Override
    public void response() {
        System.out.println("观察者A响应通知");
    }
}
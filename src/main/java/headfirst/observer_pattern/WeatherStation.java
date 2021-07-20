package headfirst.observer_pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author wusd
 * @date : 2019/08/23 15:20
 */
public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        Observer observer = new CurrentConditionService(weatherData);
        weatherData.setMeasurements(11F, 23F, 34F);
    }
}

// 主题必须实现这三个方法
interface Subject {
    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObserver();
}

// 观察者接口可以包括他们共有的方法
interface Observer {
    void update(float temperature, float humidity, float pressure);
}

// 主题实现类内部必须有一个容器来维护订阅了它的观察者们，并通过实现三个方法对观察者进行注册，删除和通知
class WeatherData implements Subject {

    private List<Observer> observers;

    private float temperature;

    private float humidity;

    private float pressure;

    WeatherData(){
        observers = new ArrayList();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            o.update(temperature, humidity, pressure);
        }
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    private void measurementsChanged(){
        notifyObserver();
    }
}

class CurrentConditionService implements Observer {
    private float temperature;

    private float humidity;

    CurrentConditionService(Subject weatherData) {
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    void display(){
        System.out.println("temperature: " + temperature + ", humidity: " + humidity);
    }
}
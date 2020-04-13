package headfirst;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author wusd
 * @description Observer pattern 观察者模式
 * @createtime 2019/08/23 15:20
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

    private ArrayList observers;

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
        if (observers.indexOf(o) >= 0) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObserver() {
        for (Object o : observers) {
            Observer os = (Observer)o;
            os.update(temperature, humidity, pressure);
        }
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    private void setRandomValue(){
        Random random = new Random();
        this.temperature = random.nextFloat();
        this.humidity = random.nextFloat();
        this.pressure = random.nextFloat();
    }

    private void measurementsChanged(){
        notifyObserver();
    }
}

// 观察者实现类，建立观察者与主题之间的依赖有两种方式：
// 1.在实例化观察者的时候注入主题，如下
// 2.在实例化主题的时候，直接通过registerObserver(Observer)方法添加观察者  （推荐）
class CurrentConditionService implements Observer {
    private float temperature;

    private float humidity;

    CurrentConditionService(Subject wertherData) {
        wertherData.registerObserver(this);
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
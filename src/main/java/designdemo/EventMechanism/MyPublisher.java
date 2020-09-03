package designdemo.EventMechanism;

import java.net.HttpURLConnection;

/**
 * @author wusd
 * @description 事件发布器
 * @create 2020/08/26 10:39
 */
public class MyPublisher implements Publisher{
    // 基本功能，当发布事件后，能通知匹配该事件类型的监听器，基本思想与观察者模式类似，
    // 维护一个容器持有事件监听器，并当事件发布后调用监听器执行相应动作，可将其功能抽象成一个MultiCaster类
    private MultiCaster multiCaster = new MultiCaster();

    // 给发布者添加监听者，这里本应该是监听事件，但监听器和事件没法创建联系
    // spring中实现为：在实例化bean后，检查bean类型是否为ApplicationListener类型，然后统一加入到发布者ApplicationContext中
    // 当ApplicationContext发布事件时，使用multicaster依次执行每个监听器的监听事件，具体由每个监听器决定是否支持事件类型
    // 所以ApplicationContext是个集合体，可以发布任何ApplicationEvent事件，通过bean加载时将监听器加载到它本身去
    // Spring中使用事件机制，只需要事件继承ApplicationEvent，监听器继承ApplicationListener，通过ApplicationContext发布事件即可
    public void addListener(Listener listener) {
        this.multiCaster.addListener(listener);
    }

    // 发布的事件可以是Event的所有子类型，在multicaster中统一执行监听器的监听方法
    public void publishEvent(Event event) {
        multiCaster.multiCast(event);
    }

    public void saySome(String s) {
        System.out.println(s);
        publishEvent(new Event(s));
    }
}

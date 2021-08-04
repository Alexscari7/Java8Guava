package headfirst.listener_pattern;

import java8start.Apple;
import javafx.application.Application;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Spring中使用事件机制，只需要事件继承ApplicationEvent，监听器继承ApplicationListener，通过ApplicationContext发布事件即可
 *
 * @author wusd
 * @date : 2021/08/04 15:46
 */
public class SpringEventMechanism {
    public static void main(String[] args) {
        ApplicationEvent eventa = new ApplicationEventA(new Apple("red", 1.0));
        ApplicationEventListener listenera= new ApplicationEventListenerA();
        ApplicationEvent eventb = new ApplicationEventB(new Apple("yellow", 2.2));
        ApplicationEventListener listenerb= new ApplicationEventListenerB();
        ApplicationContext context = new ApplicationContext();
        context.addListener(listenera);
        context.addListener(listenerb);
        context.publish(eventa);
        context.publish(eventb);
        context.close();
    }
}

abstract class ApplicationEvent extends EventObject {
    private final long timestamp;
    public ApplicationEvent(Object source) {
        super(source);
        timestamp = System.currentTimeMillis();
    }
    public final long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[source=" + source.toString() + ", ctime =" + getTimestamp() + "]";
    }
}
class ApplicationEventA extends ApplicationEvent {
    public ApplicationEventA(Object source) {
        super(source);
    }
}
class ApplicationEventB extends ApplicationEvent {
    public ApplicationEventB(Object source) {
        super(source);
    }
}

interface ApplicationEventPublisher {
    void publish(ApplicationEvent event);
}

interface ApplicationEventListener extends EventListener {
    boolean match(ApplicationEvent event);
    void onApplicationEvent(ApplicationEvent event);
}
class ApplicationEventListenerA implements ApplicationEventListener {
    @Override
    public boolean match(ApplicationEvent event) {
        return event instanceof ApplicationEventA;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (match(event)) {
            System.out.println("事件监听者A收到收到事件A：" + event.toString());
        }
    }
}
class ApplicationEventListenerB implements ApplicationEventListener {
    @Override
    public boolean match(ApplicationEvent event) {
        return event instanceof ApplicationEventB;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (match(event)) {
            System.out.println("事件监听者B收到收到事件B：" + event.toString());
        }
    }
}

interface ApplicationEventMulticaster {
    // spring中实现为：在实例化bean后，检查bean类型是否为ApplicationListener类型，然后统一加入到发布者ApplicationContext中
    void addListener(ApplicationEventListener listener);
    void removeListener(ApplicationEventListener listener);
    void multicastEvent(ApplicationEvent event);
}

class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {
    private Set<ApplicationEventListener> listeners = new LinkedHashSet<>();
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    @Override
    public void addListener(ApplicationEventListener listener) {
        listeners.add(listener);
    }
    @Override
    public void removeListener(ApplicationEventListener listener) {
        listeners.remove(listener);
    }
    @Override
    public void multicastEvent(ApplicationEvent event) {
        listeners.forEach(listener ->
            executor.execute(() -> listener.onApplicationEvent(event))
        );
    }

    public void close() {
        executor.shutdown();
    }
}

class ApplicationContext implements ApplicationEventPublisher {
    private SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();

    @Override
    public void publish(ApplicationEvent event) {
        multicaster.multicastEvent(event);
    }

    public void addListener(ApplicationEventListener listener) {
        multicaster.addListener(listener);
    }
    public void removeListener(ApplicationEventListener listener) {
        multicaster.removeListener(listener);
    }
    public void close() {
        multicaster.close();
    }
}

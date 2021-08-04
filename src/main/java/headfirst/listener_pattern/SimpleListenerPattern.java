package headfirst.listener_pattern;

import java8start.Apple;

/**
 * N
 *
 * @author wusd
 * @date : 2021/08/04 15:32
 */
public class SimpleListenerPattern {

    public static void main(String[] args) {
        IEventListener listener = new EventListenerA();
        EventSource eventSource = new EventSource(listener);
        eventSource.publishEvent(new EventA(new Apple("red", 1.4)));
    }
}

interface IEvent {
    Object getSource();
}

interface IEventListener {
    void response(IEvent event);
}

class EventA implements IEvent {
    private Object source;

    EventA(Object source) {
        this.source = source;
    }
    @Override
    public Object getSource() {
        return source;
    }
}

class EventListenerA implements IEventListener {
    @Override
    public void response(IEvent event) {
        System.out.println("监听者A收到事件通知：" + event.getSource().toString());
    }
}

class EventSource {
    private IEventListener listener;

    EventSource(IEventListener listener) {
        this.listener = listener;
    }

    public void publishEvent(IEvent event) {
        listener.response(event);
    }
}


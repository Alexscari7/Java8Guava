package designdemo.EventMechanism;

import java.util.EventListener;

/**
 * @author wusd
 * @description 事件监听器，这里可以实现为异步监听，实现类持有一个线程池就可以了，新建线程处理handle
 * @create 2020/08/26 10:40
 */
public interface Listener<E extends Event> extends EventListener {
    /**
     * 监听事件，事件被发布后做出处理
     * 可能需要监听多个事件，在handle中按事件类型匹配执行即可，不支持的事件不处理
     *
     * @param event
     * @return void
     **/
    void handle(E event);
}

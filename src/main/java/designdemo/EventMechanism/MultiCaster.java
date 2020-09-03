package designdemo.EventMechanism;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wusd
 * @description 为事件发布器工作，维护监听器并执行监听器相应方法
 * @create 2020/08/26 11:05
 */
public class MultiCaster {
    // 持有的事件监听器
    private List<Listener> listeners = Lists.newArrayList();

    // 基本功能
    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public List<Listener> getListeners() {
        return this.listeners;
    }

    // 通知监听器
    public void multiCast(Event event) {
        listeners.forEach(listener -> listener.handle(event));
    }

}

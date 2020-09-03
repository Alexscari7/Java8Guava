package designdemo.EventMechanism;

import java.util.EventObject;

/**
 * @author wusd
 * @description 事件源
 * @create 2020/08/26 10:36
 */
public class Event extends EventObject {
    public Event(Object source) {
        super(source);
    }
}

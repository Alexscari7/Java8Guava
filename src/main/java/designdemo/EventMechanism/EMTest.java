package designdemo.EventMechanism;

/**
 * @author wusd
 * @description 空
 * @create 2020/08/26 11:47
 */
public class EMTest {
    public static void main(String[] args) {
        MyPublisher myPublisher = new MyPublisher();
        myPublisher.addListener(event -> System.out.println("监听1监听到事件：" + event.getSource().toString()));
        myPublisher.addListener(event -> System.out.println("监听2监听到事件：" + event.getSource().toString()));
        myPublisher.saySome("I'm wsd");
    }
}

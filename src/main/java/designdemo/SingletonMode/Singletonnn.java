package designdemo.SingletonMode;

/**
 * @author wusd
 * @description 单例模式
 * 懒汉式-双检锁 线程安全，效率高。使用volatile关键字禁止重排序。
 * @createtime 2019/12/18 10:52
 */
public class Singletonnn {
    private Singletonnn(){}
    private volatile static Singletonnn instance;

    public static Singletonnn getInstance() {
        if (instance == null) {
            synchronized (Singletonnn.class) {
                if (instance == null) {
                    instance = new Singletonnn();
                }
            }
        }
        return instance;
    }
}

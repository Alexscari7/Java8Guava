package designdemo.SingletonMode;

/**
 * @author wusd
 * @description 单例模式
 * 要点1: 要保证只有一个实例，构造器必须要私有化
 * 要点2: 构造器私有化了，只能自己提供实例
 * 要点3: 需要一个公有的方法对外提供该实例
 * 饿汉式-静态常量方式或静态代码块方法(因为类加载时就初始化了实例，避免多线程问题，线程安全)
 * @createtime 2019/12/18 10:25
 */
public class Singleton {
    private Singleton(){}

    private static Singleton instance;

    /*static {
        instance = new Singleton();
    }*/
    public static Singleton getInstance() {
        return instance;
    }
}


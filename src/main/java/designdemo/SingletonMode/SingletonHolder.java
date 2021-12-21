package designdemo.SingletonMode;

/**
 * @author wusd
 * @description 单例模式
 * 懒汉式 与双检锁DCL方式相比，更加优雅，但是实例是由静态内部类创建的，无法传递参数。
 * @createtime 2019/12/18 15:06
 */
public class SingletonHolder {
    private SingletonHolder(){}
    private static class Holder{
        private static SingletonHolder instance = new SingletonHolder();
    }
    // jvm不会主动加载静态内部类，只有访问它时才去加载，在加载类的时候，所有类变量赋值和静态语句在<clinit>()方法中进行。
    // 虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确地加锁、同步，如果多个线程同时去初始化一个类，
    // 那么只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行<clinit>()方法完毕。
    // 如果在一个类的<clinit>()方法中有耗时很长的操作，就可能造成多个进程阻塞
    // 需要注意的是，其他线程虽然会被阻塞，但如果执行<clinit>()方法后，其他线程唤醒之后不会再次进入<clinit>()方法。
    // 同一个加载器下，一个类型只会初始化一次。在实际应用中，这种阻塞往往是很隐蔽的。
    public static SingletonHolder getInstance(){
        return Holder.instance;
    }
}

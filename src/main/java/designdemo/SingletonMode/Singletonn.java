package designdemo.SingletonMode;

/**
 * @author wusd
 * @description 单例模式
 * 懒汉式-只有在获取的时候才初始化实例
 * 普通上锁:多线程不安全，可通过同步锁保证安全，但性能开销大，每个线程进来需要先上锁，但实际上只有在第一次初始化时需要加锁
 * 错误双检锁:安全隐患。未初始化时，第一个线程进入synchronized初始化实例，
 * 但是由于可能存在指令重排序，实例指向了内存空间，但还未分配值。第二个线程获取实例不为空，但实际上还未初始化完成。
 * @createtime 2019/12/18 10:41
 */
public class Singletonn {
    private Singletonn(){}
    private static Singletonn instance;
    // 普通上锁
    public static synchronized Singletonn getInstance() {
        if (instance == null) {
            instance = new Singletonn();
        }
        return instance;
    }

    // 错误双检锁未使用volatile
    public static Singletonn getInstance1() {
        if (instance == null) {
            // 使用两次null判断，防止两个线程同时进入第一层for循环，线程1获取锁初始化实例成功后，线程2再进来就不应该再new了
            synchronized (Singletonn.class) {
                if (instance == null) {
                    instance = new Singletonn();
                }
            }
        }
        return instance;
    }

    public Singletonn getInstance2() {
        Singletonn localInstance = instance;
        if (localInstance == null) {
            synchronized (this) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Singletonn();
                }
            }
        }
        return localInstance;
    }

    public static void main(String[] args) {
        Singletonn s1 = new Singletonn();
        Singletonn instance1 = s1.getInstance2();
        Singletonn s2 = new Singletonn();
        Singletonn instance2 = s2.getInstance2();
    }
}

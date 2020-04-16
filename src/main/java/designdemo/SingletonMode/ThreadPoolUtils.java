package designdemo.SingletonMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wusd
 * @description 线程池工具类
 * @createtime 2019/12/18 16:01
 */
public class ThreadPoolUtils {
    private ExecutorService threadPool;

    private ThreadPoolUtils(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }
    private static class Holder {
        private static ThreadPoolUtils instance = new ThreadPoolUtils(new ThreadPoolExecutor(
                12, 12, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>())
        );
    }

    public static ThreadPoolUtils getInstance() {
        return Holder.instance;
    }

    public ExecutorService getThreadPool(){
        return threadPool;
    }

    public void doSomeThing(){
        threadPool.execute(() -> System.out.println(1));
    }
}

class Test{
    public static void main(String[] args) {
        ThreadPoolUtils instance = ThreadPoolUtils.getInstance();
        ExecutorService threadPool = instance.getThreadPool();
        ThreadPoolUtils instance1 = ThreadPoolUtils.getInstance();
        ExecutorService threadPool1 = instance.getThreadPool();
        ThreadPoolUtils.getInstance().doSomeThing();
    }
}

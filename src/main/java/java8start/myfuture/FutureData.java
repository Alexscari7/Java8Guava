package java8start.myfuture;

import java.util.concurrent.Callable;

/**
 * @author wusd
 * @description 空
 * @create 2020/07/17 15:57
 */
public class FutureData<T> implements Runnable {
    private T result = null;
    private volatile boolean isReady = false;
    private Callable<T> callable;

    FutureData(Callable<T> callable) {
        this.callable = callable;
    }

    public synchronized void setResult(T result){
        if (isReady) {
            return;
        }
        this.result = result;
        this.isReady = true;
        notifyAll();
    }

    public synchronized T getResult() throws InterruptedException {
        while (!isReady) {
            wait();
        }
        return result;
    }

    @Override
    public void run() {
        try {
            T call = callable.call();
            setResult(call);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

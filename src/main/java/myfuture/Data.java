package myfuture;

/**
 * @author wusd
 * @description 空
 * @create 2020/07/17 15:56
 */
public interface Data<T> extends Runnable{
    T getResult() throws InterruptedException;
}

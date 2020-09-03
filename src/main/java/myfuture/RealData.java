package myfuture;

/**
 * @author wusd
 * @description 空
 * @create 2020/07/17 15:58
 */
public class RealData<T> implements Data {
    @Override
    public void run() {

    }

    private T result;

    RealData(T t){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.result = t;
    }

    @Override
    public T getResult() {
        return result;
    }
}

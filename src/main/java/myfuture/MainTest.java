package myfuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author wusd
 * @description 空
 * @create 2020/07/17 16:12
 */
public class MainTest {
    private boolean is;
    public static void main(String[] args) throws InterruptedException {
        MainTest test = new MainTest();
        System.out.println(test.is);
    }
}

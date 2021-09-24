package java8start.completablefuturetest;


import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author wusd
 * @description 普通线程池——Future接口
 * @createtime 2019/09/16 14:42
 */
public class FutureTest {
    @Test
    public void test(){
        ExecutorService service = Executors.newCachedThreadPool();
        Future<String> future = service.submit(() -> doSomeThing());
        doOtherThings();
        try {
            // 使用get时，阻塞直到任务完成。推荐使用重载等待时间的方法
            //String result = future.get(1, TimeUnit.SECONDS);
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doOtherThings() {
        System.out.println("do other things on main thread");
    }

    private String doSomeThing() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("do something on secondary thread");
        return "done";
    }
}

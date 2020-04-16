package completablefuturetest;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author wusd
 * @description 包装线程池，内部维护一个先进先出的阻塞队列，使用take()方法时，移除已经完成了的任务，并阻塞，直到有任务完成。
 * @createtime 2019/09/16 15:26
 */
public class ExecutorCompletionServiceTest {
    @Test
    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        CompletionService<String> service = new ExecutorCompletionService<>(executorService);
        for (int i = 0; i < 20; i++) {
            int no = i;
            service.submit(() -> {
                // 第三个线程延迟0.2s
                if (no == 2) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "Running - " + Thread.currentThread().getName();
            });
        }
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(service.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

package redistest;

import jedistest.RedisUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

/**
 * @author wusd
 * @description 空
 * @create 2020/08/17 18:16
 */
class RedisTestOne {
    private static Jedis jedis;
    @BeforeAll
    static void getJedis() throws Exception {
        jedis = RedisUtils.getJedis();
    }

    @AfterAll
    static void releaseJedis(){
        RedisUtils.closeJedis(jedis);
    }

    @Test
    void setTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            String value = UUID.randomUUID().toString();
            String set = jedis.set("lock", value, new SetParams().ex(600).nx());
            System.out.println(set == null ? "null" : set);
        });
        thread.start();
        System.out.println(1);

    }

    public void lock(String key,int seconds){
        while (true) {
            String value = UUID.randomUUID().toString();
            String set = jedis.set(key, value, new SetParams().ex(seconds).nx());
            if (set != null) {
                return;
            }
            LockSupport.parkNanos(100 * 1000);
        }
    }

    public void unlock(String key){
        jedis.del(key);
    }


}

package redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.properties.PropertiesFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author wusd
 * @description redis测试
 * @create 2019/12/19 15:09
 */
@Slf4j
public class RedisUtils {
    private static JedisPool jedisPool;

    private RedisUtils() {

    }

    static {
        try {
            URL redisResource = RedisUtils.class.getClassLoader().getResource("redis.properties");
            Properties prop = PropertiesFactory.INSTANCE.load(redisResource);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(Integer.parseInt(prop.getProperty("redis.pool.maxTotal")));
            jedisPoolConfig.setMaxIdle(Integer.parseInt(prop.getProperty("redis.pool.maxIdel")));
            jedisPoolConfig.setMinIdle(Integer.parseInt(prop.getProperty("redis.pool.minIdel")));
            String host = prop.getProperty("redis.host");
            int port = Integer.parseInt(prop.getProperty("redis.port"));
            int timeout = Integer.parseInt(prop.getProperty("redis.timeout"));
            String password = prop.getProperty("redis.password");
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        } catch (IOException e) {
            log.error("读取文件失败");
            e.printStackTrace();
        }
    }

    public static Jedis getJedis() throws Exception {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            }
        } catch (Exception e) {
            log.error("获取Jedis失败");
            e.printStackTrace();
        }
        return null;
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        Jedis jedis1 = null;
        Jedis jedis2 = null;
        try {
            jedis1 = RedisUtils.getJedis();
            jedis2 = RedisUtils.getJedis();
            System.out.println(jedis1 + "," + jedis2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
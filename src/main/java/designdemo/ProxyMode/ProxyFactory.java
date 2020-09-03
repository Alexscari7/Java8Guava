package designdemo.ProxyMode;

import com.google.common.collect.Maps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author wusd
 * @description 代理工厂
 * @create 2020/09/02 19:30
 */
public class ProxyFactory {
    // 假设是Spring，就必须还持有一个代理目标单例缓存和代理对象的单例缓存
    private Map<String, Object> targetCacheFactory = Maps.newHashMap();
    private Map<String, Object> ProxyCacheFactory = Maps.newHashMap();

    public static <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 从单例缓存中取目标对象
                Object o = clazz.newInstance();
                Object invoke = method.invoke(o, args);
                return invoke;
            }
        });
    }
}

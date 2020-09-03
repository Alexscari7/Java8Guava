package designdemo.ProxyMode;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wusd
 * @description 空
 * @create 2020/09/02 18:37
 */
public class PMTest {
    // 传统代理,需要创建类
    @Test
    void test1() {
        UserService userService = new UserServiceImpl();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) myInvocationHandler.getProxy();
        proxy.save("wsd");
    }

    // 使用Lambda表达式，因为代理是一个函数式接口，只有一种行为
    @Test
    void test2() {
        UserService userService = new UserServiceImpl();
        UserService proxyObject = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
            userService.getClass().getInterfaces(),
            ((proxy, method, args) -> {
                System.out.println("开始");
                Object result = method.invoke(userService, args);
                System.out.println("结束");
                return result;
            }));
        proxyObject.save("wsd");
    }

    // 使用代理工厂
    @Test
    void test3() {
        UserService proxy = ProxyFactory.getProxy(UserServiceImpl.class);
        proxy.save("wsd");
    }
}

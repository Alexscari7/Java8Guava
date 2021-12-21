package designdemo.ProxyMode;

import java.lang.reflect.Proxy;

/**
 * @author wusd
 * @description 空
 * @create 2020/09/02 18:37
 */
public class ProxyClient {

    public static void main(String[] args) {
        // 静态代理
        UserService staticProxy = new StaticProxy(new UserServiceImpl());
        staticProxy.save("alex");

        // 动态代理
        UserService dynamicProxy = (UserService) Proxy.newProxyInstance(UserServiceImpl.class.getClassLoader(), new Class[]{UserService.class},
                new DynamicProxy(new UserServiceImpl()));
        dynamicProxy.save("wsd");
    }

}

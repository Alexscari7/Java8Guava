package designdemo.ProxyMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wusd
 */
public class DynamicProxy implements InvocationHandler {

    private Object target;

    DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("before method");
        Object result = method.invoke(target, args);
        System.out.println("after method");
        return result;
    }
}

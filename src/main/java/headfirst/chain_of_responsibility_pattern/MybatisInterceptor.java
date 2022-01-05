package headfirst.chain_of_responsibility_pattern;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wusd
 */
public class MybatisInterceptor {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        List<String> list = Lists.newArrayList("1");
        Interceptor mapInterceptor = new MapAlwaysNullInterceptor();
        Interceptor listInterceptor = new ListAlwaysEmptyInterceptor();
        InterceptorChain interceptorChain = new InterceptorChain().addInterceptor(mapInterceptor).addInterceptor(listInterceptor);
        List o = (List)interceptorChain.applyTo(list);
        System.out.println(o.get(1));
        System.out.println(Arrays.toString(o.toArray()));
    }


}

class Plugin implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    public Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    public static Object apply(Object target, Interceptor interceptor) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new Plugin(target, interceptor)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return interceptor.intercept(target, method, args);
    }

}

interface Interceptor {
    Object intercept(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}

@interface Signature{
    Class<?> type();
    String method();
    Class<?>[] args();
}

class InterceptorChain {
    private List<Interceptor> interceptors = new ArrayList<>();

    public Object applyTo(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = Plugin.apply(target, interceptor);
        }
        return target;
    }

    public InterceptorChain addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

}

class MapAlwaysNullInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (target instanceof Map) {
            return "null";
        }
        return method.invoke(target, args);
    }
}

class ListAlwaysEmptyInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (target instanceof List) {
            return "Empty";
        }
        return method.invoke(target, args);
    }
}




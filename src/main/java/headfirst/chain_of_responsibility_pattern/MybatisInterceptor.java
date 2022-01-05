package headfirst.chain_of_responsibility_pattern;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wusd
 */
public class MybatisInterceptor {

    public static void main(String[] args) throws NoSuchMethodException {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        List<String> list = Lists.newArrayList("1");
        Interceptor mapInterceptor = new MapAlwaysNullInterceptor();
        Interceptor listInterceptor = new ListAlwaysEmptyInterceptor();
        InterceptorChain interceptorChain = new InterceptorChain().addInterceptor(mapInterceptor).addInterceptor(listInterceptor);
        List o = (List)interceptorChain.applyTo(list);
        System.out.println(o.get(1));
    }


}

class Plugin implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    private Method method;

    public Plugin(Object target, Interceptor interceptor, Method method) {
        this.target = target;
        this.interceptor = interceptor;
        this.method = method;
    }

    public static Object apply(Object target, Interceptor interceptor) throws NoSuchMethodException {
        Signature sig = interceptor.getClass().getAnnotation(Signature.class);
        Class<?> type = sig.type();
        Method method = type.getMethod(sig.method(), sig.args());
        if (type.getInterfaces().length > 0) {
            return Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    new Plugin(target, interceptor, method)
            );
        }
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method == this.method) {
            return interceptor.intercept(target, method, args);
        }
        return method.invoke(target, args);
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

    public Object applyTo(Object target) throws NoSuchMethodException {
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

@Signature(type=Map.class, method="get", args={Object.class})
class MapAlwaysNullInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) {
        return "null";
    }
}

@Signature(type=List.class, method="get", args={Integer.class})
class ListAlwaysEmptyInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) {
        return "empty";
    }
}




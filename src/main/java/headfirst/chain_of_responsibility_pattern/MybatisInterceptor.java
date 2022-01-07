package headfirst.chain_of_responsibility_pattern;

import com.google.common.collect.Lists;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wusd
 */
public class MybatisInterceptor {

    public static void main(String[] args) throws NoSuchMethodException {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        List<String> list = Lists.newArrayList("1");

        Interceptor mapInterceptor = new AlwaysNullInterceptor();
        Interceptor listInterceptor = new AlwaysEmptyInterceptor();
        InterceptorChain interceptorChain = new InterceptorChain()
                .addInterceptor(mapInterceptor)
                .addInterceptor(listInterceptor);

        List o = (List)interceptorChain.applyTo(list);
        System.out.println(o.get(1));
        System.out.println(o.size());
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
        // 确认目标对象是否适配Interceptor中指定代理类。适配时代理，不适配时直接使用原对象
        Intercept sig = interceptor.getClass().getAnnotation(Intercept.class);
        Method proxyMethod = sig.type().getMethod(sig.method(), sig.args());
        List<Class<?>> interfaces = getAllInterfaces(target.getClass());
        if (interfaces.contains(sig.type())) {
            return Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    interfaces.toArray(new Class[0]),
                    new Plugin(target, interceptor, proxyMethod)
            );
        }
        return target;
    }

    private static List<Class<?>> getAllInterfaces(Class<?> type) {
        Set<Class<?>> interfaces = new HashSet<>();
        for (; type != null; type = type.getSuperclass()) {
            interfaces.addAll(Arrays.asList(type.getInterfaces()));
        }
        return new ArrayList<>(interfaces);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 确认调用方法是否为指定代理方法。匹配时代理方法，不匹配时执行原方法
        if (method.equals(this.method)) {
            return interceptor.intercept(target, method, args);
        }
        return method.invoke(target, args);
    }

}

interface Interceptor {
    Object intercept(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@interface Intercept {
    Class<?> type();
    String method();
    Class<?>[] args();
}

class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<>();

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

@Intercept(type=Map.class, method="get", args={Object.class})
class AlwaysNullInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) {
        return "null";
    }
}

@Intercept(type=List.class, method="get", args={int.class})
class AlwaysEmptyInterceptor implements Interceptor {
    @Override
    public Object intercept(Object target, Method method, Object[] args) {
        return "empty";
    }
}




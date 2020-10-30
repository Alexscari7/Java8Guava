package designdemo.SPIMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wusd
 * @description 服务管理者，中心化管理
 * @create 2020/09/11 20:42
 */
public class ServiceManager {
    private static Map<String, Provider> providers = new ConcurrentHashMap<>();

    private static final String DEFULT_PROVIDER_NAME = "default_provider";

    // 注册服务
    public static void registerProvider(String name, Provider provider) {
        providers.put(name, provider);
    }

    public static void registerProvider(Provider provider) {
        registerProvider(DEFULT_PROVIDER_NAME, provider);
    }

    // 提供服务
    public static Service getService(String name) {
        Provider provider = providers.get(name);
        return provider.getService();
    }

    public static Service getService() {
        return getService(DEFULT_PROVIDER_NAME);
    }
}

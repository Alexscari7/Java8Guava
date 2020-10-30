package designdemo.SPIMode;

/**
 * @author wusd
 * @description 提供者接口，由厂商实现提供服务
 * @create 2020/09/11 20:41
 */
public interface Provider {
    Service getService();
}

package designdemo.SPIMode;

/**
 * @author wusd
 * @description 服务接口
 * SPI是JDBC实现的基本框架，Connection是Java提供的服务者接口，DriverManager是服务管理者，用于服务注册和获取服务，Driver为提供者
 * 由各厂商实现，实际提供不同服务。JDBC要求在加载驱动时厂商自己注册服务，提供DriverManager.registerDriver()接口。
 * 所有厂商利用类加载机制，在类加载中的static初始化接口调用注册，将自己的服务注册到DriverManager
 * @create 2020/09/11 20:40
 */
public interface Service {

}

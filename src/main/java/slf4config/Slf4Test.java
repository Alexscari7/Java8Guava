package slf4config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wusd
 * @description 日志测试
 * @createtime 2019/08/14 15:36
 */
@Slf4j
public class Slf4Test {
    @Test
    public void test() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String url = String.format("http://%s:%s/admin", inetAddress.getHostAddress(), "8090");
        log.debug("debug");
        log.info("info");
        log.warn("{}",url);
        log.error("错误", new IllegalArgumentException("参数异常"));
    }
}

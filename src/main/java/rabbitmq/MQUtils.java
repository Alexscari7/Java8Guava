package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.properties.PropertiesFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author wusd
 * @description 空
 * @create 2020/08/22 11:13
 */
@Slf4j
public class MQUtils {
    private MQUtils(){}

    private static class Holder{
        private static ConnectionFactory connectionFactory;
        static{
            try {
                connectionFactory = new ConnectionFactory();
                // 通过类加载器得到配置文件，怎样都可以拿到
                URL url = MQUtils.class.getClassLoader().getResource("rabbitmq.properties");
                Properties prop = PropertiesFactory.INSTANCE.load(url);
                connectionFactory.load(prop);
            } catch (IOException e) {
                log.error("获取连接池失败");
                e.printStackTrace();
            }
        }
    }

    private static ConnectionFactory getFactory() {
        return Holder.connectionFactory;
    }

    public static Channel getChannel() {
        Channel channel = null;
        try {
            channel = getFactory().newConnection().createChannel();
        } catch (Exception e) {
            log.error("获取channel失败");
            e.printStackTrace();
        }
        return channel;
    }

    public static void releaseChannel(Channel channel) {
        try {
            if (channel != null){
                channel.close();
            }
        } catch (Exception e) {
            log.error("关闭channel失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Channel channel1 = MQUtils.getChannel();
        Connection connection1 = channel1.getConnection();
        Channel channel2 = MQUtils.getChannel();
        Connection connection2 = channel1.getConnection();
    }
}

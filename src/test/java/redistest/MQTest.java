package redistest;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rabbitmq.MQUtils;

import java.io.IOException;
import java.util.stream.IntStream;


/**
 * @author wusd
 * @description 空
 * @create 2020/08/22 12:00
 */
@Slf4j
class MQTest {
    private static Channel channel;

    @BeforeAll
    static void init() {
        channel = MQUtils.getChannel();
    }

    @AfterAll
    static void release() {
        MQUtils.releaseChannel(channel);
    }

    @Test
    void ctest() throws IOException {
        channel.basicQos(20, false);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                consume(consumerTag, new String(body));
            }

            @Override
            public void handleConsumeOk(String consumerTag) {
                System.out.println("consumeOK方法-消费者Tag："+ consumerTag);
            }

            private void consume(String consumerTag, String s) {
                System.out.println(s);
            }
        };
        String squeue = channel.basicConsume("squeue",consumer);
    }

    @Test
    void ptest() throws IOException, InterruptedException {
        channel.confirmSelect();
        channel.addReturnListener(((replyCode, replyText, exchange, routingKey, properties, body) -> {
            System.out.println(String.format("路由失败，找不到合适队列 replyCode:%d replyText:%s exchange:%s routingKey:%s " +
                    "props:%s body:%s", replyCode,replyText,exchange,routingKey,properties,new String(body)));
        }));
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("投递成功，deliveryTag:%s,multiple:%s", deliveryTag, multiple));
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("投递不成功，deliveryTag:%s,multiple:%s", deliveryTag, multiple));
            }
        });
        IntStream.rangeClosed(1, 100).forEach(t -> {
            try {
                channel.basicPublish("", "squeue",true, MessageProperties.BASIC, "厉害啊".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void ttest() throws IOException {
        channel.queueDeclare("squeuet", false, false, true, null);
        channel.basicPublish("", "squeuet", MessageProperties.BASIC, "lihia".getBytes());
    }
}

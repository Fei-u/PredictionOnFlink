package com_src_rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.BuiltinExchangeType;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    // private static final String QUEUE= "helloworld";
    public static void main(String[] args) throws IOException, TimeoutException{
        String queue_name1 = "queue1";
        String channel_name1 = "channel1";
        String key1 = "key1";
        String queue_name2 = "queue2";
        String channel_name2 = "channel2";
        String key2 = "key2";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("channel1", BuiltinExchangeType.DIRECT, true, false, null);
        channel.exchangeDeclare("channel2", BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("queue1", true, false, false, null);
        channel.queueDeclare("queue2", true, false, false, null);
        channel.queueBind(queue_name1, channel_name1, key1);
        channel.queueBind(queue_name2, channel_name2, key2);

        String message = "he";
        System.out.println("发送：" + message);

        channel.basicPublish(channel_name1, queue_name1, null, message.getBytes());
        channel.basicPublish(channel_name2, queue_name2, null, message.getBytes());

        channel.close();
        connection.close();
    }
}


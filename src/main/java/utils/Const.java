package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

public class Const {
    
    public static final Set<String> targetIds = new HashSet<>(Arrays.asList(
    "51_senior_1_1", "51_senior_2_1", "51_senior_3_1", "51_senior_4_1", "51_senior_5_1"
));

    public static final RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
                                                            .setHost("localhost")
                                                            .setPort(5672)
                                                            .setUserName("guest")
                                                            .setPassword("guest")
                                                            .setVirtualHost("/")
                                                            .build();

    public static final FlinkJedisPoolConfig InfluxDBconf = new FlinkJedisPoolConfig
                                                .Builder()
                                                .setHost("127.0.0.1")
                                                .build();

    public static final String modelPath = "/home/demo/src/main/java/xgboost/model.pmml";

}

package senior;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.redis.RedisSink;

import entity.JsonEntity;
import entity.TransEntity;
import entity.XgboostEntity;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

import utils.Const;
import flink_operator.Combine;
import flink_operator.KeySelectorForMerge;
import flink_operator.MeanWindowFunction;
import flink_operator.MyFilter;
import flink_operator.MyFlatMap;
import flink_operator.MyKeySelector;
import flink_operator.XgboostFunction;
import flink_operator.XgboostPredFunction;

import java.time.Duration;

import flink_sink.InfluxdbSink;
import flink_sink.MyRedisSink;

/**
 * Hello world!
 *
 */
public class App 
{    
        public static void main(String[] args) throws Exception {
                
                // 配置flink计算环境
                final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                env.enableCheckpointing(1000);
                
                // 连接rabbitmq，获取源数据                
                final DataStreamSource<String> stream_raw = env.addSource(new RMQSource<String>(
                        Const.connectionConfig,            
                        "queue1",                 
                        false,                        
                        new SimpleStringSchema()))
                        .setParallelism(1);    
                // stream_raw.print();

                // 源数据解析和映射
                // 设置watermark水印，考虑乱序
                SingleOutputStreamOperator <JsonEntity> stream = stream_raw
                        .flatMap(new MyFlatMap())
                        .setParallelism(10)
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<JsonEntity>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                                        .withTimestampAssigner(
                                new SerializableTimestampAssigner<JsonEntity>() {
                                   @Override
                                   public long extractTimestamp(JsonEntity event, long recordTimestamp) {
                                        return event.getPi_time();
                                   }}));
                // stream.print();

                //滑窗计算
                //插值
                //计算均值 方差 四分位
                DataStream<TransEntity> TransStream = stream
                        .filter(new MyFilter(Const.targetIds))
                        .keyBy(new MyKeySelector())
                        .window(SlidingEventTimeWindows.of(Time.seconds(12), Time.seconds(1)))
                        .process(new MeanWindowFunction())
                        .setParallelism(5);
                // TransStream.print();

                //将同一时刻的变量map到一个pojo中
                DataStream<XgboostEntity> XgboostStream = TransStream
                        .keyBy(TransEntity::getPi_time)
                        .process(new XgboostFunction());
                // XgboostStream.print();

                //调用xgboost_pmml, 写入预测值
                DataStream<JsonEntity> PredictStream = XgboostStream
                        .map(new XgboostPredFunction());
                // PredictStream.print();
                
                //合并
                //只有一个key需要填值，需要注意。。。
                    
                DataStream<JsonEntity> connectedStreams = stream
                        .coGroup(PredictStream)
                        .where(new KeySelectorForMerge())
                        .equalTo(new KeySelectorForMerge())
                        .window(TumblingEventTimeWindows.of(Time.seconds(1)))
                        .apply(new Combine());;
                // connectedStreams.print();
                                                                        
                // 写入redis缓存，设置数据保留5s
                // 写入redis中似乎存在一些问题
                // connectedStreams.addSink(new RedisSink<>(Const.InfluxDBconf, new MyRedisSink()));
                
                // 写入influxdb，方便后期查询
                connectedStreams.addSink(new InfluxdbSink());
                
                env.execute();
        }
}

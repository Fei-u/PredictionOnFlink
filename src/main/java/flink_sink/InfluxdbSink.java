package flink_sink;

import java.util.concurrent.TimeUnit;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import entity.JsonEntity;

public class InfluxdbSink extends RichSinkFunction<JsonEntity> {

    private transient InfluxDB influxDB;

    @Override
    public void open(Configuration parameters) throws Exception {
        // 在这里初始化 InfluxDB 连接
        influxDB = InfluxDBFactory.connect("http://localhost:8087", "username", "password");
        // 可选：设置 InfluxDB 数据库名称
        influxDB.setDatabase("mydb");
    }

    @Override
    public void invoke(JsonEntity value, Context context) {
        // 在这里将数据写入 InfluxDB
        Point point = Point.measurement("measurement_name")
                .tag("tag_name", value.getPi_tag())
                .time(value.getPi_time()/1000L, TimeUnit.SECONDS)
                .addField("value", value.getValue())
                .addField("state", value.getState())
                .addField("pred_val", value.getPre_val())
                .build();

        // influxDB.write(point);

        try {
            influxDB.write(point);
            // System.out.printf("write to influxdb sucessfully: "+value.getPi_time() + "\n");
        } catch (Exception e) {
            System.out.printf("Error writing data to InfluxDB"+ e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        influxDB.close();
    }
}

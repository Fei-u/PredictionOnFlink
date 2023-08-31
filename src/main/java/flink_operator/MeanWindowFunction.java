package flink_operator;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.util.Collector;

import entity.JsonEntity;
import entity.TransEntity;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Collections;

public class MeanWindowFunction extends ProcessWindowFunction<JsonEntity, TransEntity, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<JsonEntity> elements, Collector<TransEntity> out) throws Exception {
        List<Double> values = new ArrayList<>();
        long maxTime = Long.MIN_VALUE;
        Double latestValue = 0.0d;

        for (JsonEntity element : elements) {
            values.add(element.getValue());
            if (element.getPi_time() > maxTime) {
                maxTime = element.getPi_time();
                latestValue = element.getValue(); 
            }
        }

        double mean = values.stream().mapToDouble(value -> value.doubleValue()).average().orElse(0.0);
        double variance = values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum() / values.size();

        Collections.sort(values);

        int size = values.size();
        Double lowerQuartile = values.get(size / 4);
        Double upperQuartile = values.get(3 * size / 4);

        out.collect(new TransEntity(key, maxTime, latestValue, mean, variance, upperQuartile, lowerQuartile));
    }
}
    

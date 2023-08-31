package flink_operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import entity.JsonEntity;

public class MyFlatMap implements FlatMapFunction<String, JsonEntity>{

    @Override
    public void flatMap(String s, Collector<JsonEntity> collector) throws Exception {
            JSONObject object = JSONObject.parseObject(s, Feature.OrderedField);
            List<Map.Entry<String, Object>> entryList = new ArrayList<>(object.entrySet());
            
            String ML_ID = object.getString(entryList.get(0).getKey())+"_";
            Long pi_time = object.getLongValue(entryList.get(1).getKey())/1000000L;
            String state = object.getString(entryList.get(2).getKey());

            for (int i = 3; i < object.size(); i++) {
                    collector.collect(new JsonEntity(
                            ML_ID + entryList.get(i).getKey(),
                            pi_time,
                            object.getDouble(entryList.get(i).getKey()),
                            null,
                            state));
                    }
            }

}

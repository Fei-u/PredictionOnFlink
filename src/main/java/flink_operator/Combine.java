package flink_operator;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.util.Collector;

import entity.JsonEntity;

public class Combine implements CoGroupFunction<JsonEntity, JsonEntity, JsonEntity>{

    @Override
    public void coGroup(Iterable<JsonEntity> first, Iterable<JsonEntity> second, Collector<JsonEntity> collector) throws Exception {
        
        for (JsonEntity entity1 : first) {
            boolean noElements = true;
            for (JsonEntity entity2 : second) {
                noElements = false;
                JsonEntity mergedEntity = new JsonEntity(entity1.getPi_tag(), entity1.getPi_time(), entity1.getValue(), entity2.getPre_val(), entity1.getState());
                collector.collect(mergedEntity);
            }
            if(noElements){
                collector.collect(entity1);
            }
        }

    }
}
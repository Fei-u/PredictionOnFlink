package flink_operator;
import org.apache.flink.api.common.functions.FilterFunction;

import entity.JsonEntity;

import java.util.Set;


public class MyFilter implements FilterFunction<JsonEntity> {
    private Set<String> targetIds;

    public MyFilter(Set<String> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public boolean filter(JsonEntity myData) {
        return targetIds.contains(myData.getPi_tag());
    }
}




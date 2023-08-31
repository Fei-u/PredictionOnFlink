package flink_operator;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import entity.TransEntity;
import entity.XgboostEntity;

import java.util.Map;
import java.util.HashMap;

public class XgboostFunction extends KeyedProcessFunction<Long, TransEntity, XgboostEntity>{
    private transient Map<String, Integer> fieldMap;
    private transient ValueState<XgboostEntity> xgboostEntityState;

    private static final long CLEANUP_DELAY_MS = 500;


    @Override
    public void open(Configuration parameters) {
        fieldMap = new HashMap<>();

        fieldMap.put("51_senior_1_1", 0); 
        fieldMap.put("51_senior_2_1", 1);
        fieldMap.put("51_senior_3_1", 2);
        fieldMap.put("51_senior_4_1", 3);
        fieldMap.put("51_senior_5_1", 4); 

        ValueStateDescriptor<XgboostEntity> xgboostEntityDescriptor =
            new ValueStateDescriptor<>("xgboostEntityState", TypeInformation.of(XgboostEntity.class));
        xgboostEntityState = getRuntimeContext().getState(xgboostEntityDescriptor);
    }
    
    @Override
    public void processElement(TransEntity value, Context ctx, Collector<XgboostEntity> out) throws Exception {
        XgboostEntity currentXgboostEntity = xgboostEntityState.value();

        String key = value.getPi_tag();
        Integer fieldPosition = fieldMap.get(key);
        
        if (currentXgboostEntity == null) {
            currentXgboostEntity = new XgboostEntity("51_senior_1_1");
        }

        currentXgboostEntity.setPi_time(value.getPi_time());
        currentXgboostEntity.setArrayValues(fieldPosition, value.getValue());
        currentXgboostEntity.setArrayMeans(fieldPosition, value.getMean());
        currentXgboostEntity.setArrayVariances(fieldPosition, value.getVariance());
        currentXgboostEntity.setArrayUppers(fieldPosition, value.getUpper());
        currentXgboostEntity.setArrayLowers(fieldPosition, value.getLower());

        xgboostEntityState.update(currentXgboostEntity);

        ctx.timerService().registerEventTimeTimer(value.getPi_time() + CLEANUP_DELAY_MS);
    }
    
    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<XgboostEntity> out) throws Exception {

        XgboostEntity currentXgboostEntity = xgboostEntityState.value();
        out.collect(currentXgboostEntity);
        xgboostEntityState.clear();
    }
}



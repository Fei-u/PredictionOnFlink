package flink_operator;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;

import entity.JsonEntity;


public class KeySelectorForMerge implements KeySelector<JsonEntity, Tuple2<Long, String>> {
        @Override
        public Tuple2<Long, String> getKey(JsonEntity entity) throws Exception {
            return Tuple2.of(entity.getPi_time(), entity.getPi_tag());
            }
        }



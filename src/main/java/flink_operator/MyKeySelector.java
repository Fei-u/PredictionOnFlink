package flink_operator;

import org.apache.flink.api.java.functions.KeySelector;

import entity.JsonEntity;


public class MyKeySelector implements KeySelector<JsonEntity, String> {
                    @Override
                    public String getKey(JsonEntity myData) throws Exception {
                    return myData.getPi_tag();
                    }}
    


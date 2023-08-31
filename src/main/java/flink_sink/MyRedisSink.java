package flink_sink;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

import entity.JsonEntity;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;


public class MyRedisSink implements RedisMapper<JsonEntity>{ 
    /** 
     * 设置redis数据类型 
     */ 
    @Override 
    public RedisCommandDescription getCommandDescription() { 
        return new RedisCommandDescription(RedisCommand.HSET,"flink_pv_uv"); 
    } 
    //指定key 
    @Override 
    public String getKeyFromData(JsonEntity data) {
        String time = Long.toString(data.getPi_time()/1000L); 
        System.out.printf(time + "\n");
        return data.getPi_tag()+time; 
    } 
    //指定value 
    @Override 
    public String getValueFromData(JsonEntity data) { 
        return data.getValue().toString()+":"+data.getPre_val().toString(); 
    } 

    // @Override
    // public Optional<Integer> getAdditionalTTL(JsonEntity data) {
    //     Optional<Integer> TTL = Optional.of(5);
    //     return TTL;
    // } 
} 
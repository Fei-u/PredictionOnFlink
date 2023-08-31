package entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
public class JsonEntity implements Serializable{
    @Getter private String pi_tag;
    @Getter private Long pi_time;
    @Getter private Double value;
    @Getter private String state;
    @Getter private Double pre_val;

    public JsonEntity(String pi_tag, long pi_time, Double value, Double pre_val, String state){
       this.pi_tag=pi_tag;
       this.pi_time=pi_time;
       this.value = value;
       this.pre_val = pre_val;
       this.state = state;
    
    }
    
    @Override
    public String toString() {
        return "JsonEntity{" +
                "pi_tag='" + pi_tag + '\'' +
                ", pi_time=" + pi_time +
                ", value='" + value + '\'' +
                ", pre_value='" + pre_val + '\'' +
                ", state='" + state + '\'' +
                '}';
       }

}
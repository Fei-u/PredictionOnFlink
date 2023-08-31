package entity;

import java.io.Serializable;
import lombok.Getter;

public class TransEntity implements Serializable{
    @Getter private String pi_tag;
    @Getter private Long pi_time;
    @Getter private Double value;
    @Getter private Double mean;
    @Getter private Double variance;
    @Getter private Double upper;
    @Getter private Double lower;

    public TransEntity(String pi_tag, long pi_time, Double value, Double mean, Double variance, Double upper, Double lower){
       this.pi_tag=pi_tag;
       this.pi_time=pi_time;
       this.value = value;
       this.mean = mean;
       this.variance = variance;
       this.upper = upper;
       this.lower = lower;
    }
    
    @Override
    public String toString() {
        return "TransEntity{" +
                "pi_tag='" + pi_tag + '\'' +
                ", pi_time=" + pi_time +
                ", value='" + value + '\'' +
                ", mean='" + mean + '\'' +
                ", variance='" + mean + '\'' +
                ", upper='" + upper + '\'' +
                ", lower='" + lower + '\'' +
                '}';
       }

}
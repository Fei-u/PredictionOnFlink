package entity;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
public class XgboostEntity implements Serializable{

    private String pi_tag;
    @Setter private Long pi_time;
    private Double[] values = new Double[5];
    private Double[] means = new Double[5];
    private Double[] variances = new Double[5];
    private Double[] uppers = new Double[5];
    private Double[] lowers = new Double[5];

    public XgboostEntity(String pi_tag) {
       this.pi_tag = pi_tag;
       this.pi_time = null;
       for (int i = 0; i < 5; i++) {
           values[i] = null;
           means[i] = null;
           variances[i] = null;
           uppers[i] = null;
           lowers[i] = null;
       }
    }

    public void setArrayValues(int index, Double value) {
        if (index >= 0 && index < values.length) {
            values[index] = value;
        } else {
            throw new IndexOutOfBoundsException("Invalid array index: " + index);
        }
    }

    public void setArrayMeans(int index, Double mean) {
        if (index >= 0 && index < values.length) {
            means[index] = mean;
        } else {
            throw new IndexOutOfBoundsException("Invalid array index: " + index);
        }
    }

    public void setArrayVariances(int index, Double variance) {
        if (index >= 0 && index < values.length) {
            variances[index] = variance;
        } else {
            throw new IndexOutOfBoundsException("Invalid array index: " + index);
        }
    }

    public void setArrayUppers(int index, Double upper) {
        if (index >= 0 && index < values.length) {
            uppers[index] = upper;
        } else {
            throw new IndexOutOfBoundsException("Invalid array index: " + index);
        }
    }

    public void setArrayLowers(int index, Double lower) {
        if (index >= 0 && index < values.length) {
            lowers[index] = lower;
        } else {
            throw new IndexOutOfBoundsException("Invalid array index: " + index);
        }
    }

    @Override
    public String toString() {
        return "XgboostEntity{" +
                "pi_tag='" + pi_tag + '\'' +
                ", pi_time=" + pi_time +
                ", values=" + Arrays.toString(values) +
                ", means=" + Arrays.toString(means) +
                ", variances=" + Arrays.toString(variances) +
                ", uppers=" + Arrays.toString(uppers) +
                ", lowers=" + Arrays.toString(lowers) +
                '}';
    }
}

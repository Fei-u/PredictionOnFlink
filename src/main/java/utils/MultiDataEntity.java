package utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiDataEntity implements Serializable {

    private static final long serialVersionUID = -457566433778417297L;

    private String projectId;
    private String targetId;
    private String serialCode;
    private String targetType;
    private String typeTag;
    private String pickTime;
    private Double[] values;
    private Integer count;

}


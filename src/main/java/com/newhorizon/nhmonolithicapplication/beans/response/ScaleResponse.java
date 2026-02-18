package com.newhorizon.nhmonolithicapplication.beans.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleResponse {
    private String id;
    private Integer scaleCode;
    private String scaleName;
    private Integer scaleValue;
}

package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
public class CommonRq {
    @JsonProperty("signal_strength")
    private String signalStrength;
    private String mac;
    private String age;
}

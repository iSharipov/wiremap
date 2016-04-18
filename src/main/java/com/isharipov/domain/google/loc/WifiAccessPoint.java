package com.isharipov.domain.google.loc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isharipov.domain.common.CommonRq;
import lombok.Data;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
public class WifiAccessPoint{
    private String macAddress;
    private Integer signalStrength;
    private Integer age;
}

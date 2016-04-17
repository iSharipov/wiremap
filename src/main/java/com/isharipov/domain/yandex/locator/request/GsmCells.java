package com.isharipov.domain.yandex.locator.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
public class GsmCells {
    @JsonProperty("countrycode")
    private String countryCode;
    @JsonProperty("operatorid")
    private String operatorId;
    @JsonProperty("cellid")
    private String cellId;
    private String lac;
    @JsonProperty("signal_strength")
    private String signalStrength;
    private String age;
}

package com.isharipov.domain.yandex.locator.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
public class WifiNetworks {
    private String mac;
    @JsonProperty("signal_strength")
    private String signalStrength;
    private String age;
}

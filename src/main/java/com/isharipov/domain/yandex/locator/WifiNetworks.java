package com.isharipov.domain.yandex.locator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
public class WifiNetworks {
    @JsonProperty("signal_strength")
    private String signalStrength;
    private String mac;
    private String age;
}

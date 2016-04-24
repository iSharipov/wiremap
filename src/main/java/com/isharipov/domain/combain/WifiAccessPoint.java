package com.isharipov.domain.combain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WifiAccessPoint {
    private String macAddress;
    private String signalStrength;
}

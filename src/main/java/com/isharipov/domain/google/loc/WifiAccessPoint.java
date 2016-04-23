package com.isharipov.domain.google.loc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WifiAccessPoint {
    private String macAddress;
//    private String signalStrength;
//    private String age;
}

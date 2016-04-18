package com.isharipov.domain.google.loc;

import lombok.Data;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
public class WifiAccessPoint {
    private String macAddress;
    private Integer signalStrength;
    private Integer age;
}

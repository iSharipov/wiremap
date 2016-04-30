package com.isharipov.domain.google.loc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CellTower {
    private String cellId;
    private String locationAreaCode;
    private String mobileCountryCode;
    private String mobileNetworkCode;
}

package com.isharipov.domain.location.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationApiRq {
    private String token;
    private String radio;
    private String mcc;
    private String mnc;
    private List<Cell> cells;
    private List<WiFi> wifi;
}

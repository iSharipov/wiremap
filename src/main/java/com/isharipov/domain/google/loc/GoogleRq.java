package com.isharipov.domain.google.loc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
@Builder
public class GoogleRq {
    private List<WifiAccessPoint> wifiAccessPoints;
}

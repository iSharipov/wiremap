package com.isharipov.domain.combain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CombainRq {
    private String radioType;
    private List<WifiAccessPoint> wifiAccessPoints;
    private List<CellTower> cellTowers;
}

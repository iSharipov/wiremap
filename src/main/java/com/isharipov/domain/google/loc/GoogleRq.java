package com.isharipov.domain.google.loc;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Илья on 18.04.2016.
 */
@Data
@Builder
public class GoogleRq {
    private List<CellTower> cellTowers;
    private List<WifiAccessPoint> wifiAccessPoints;
    private Boolean considerIp;
}

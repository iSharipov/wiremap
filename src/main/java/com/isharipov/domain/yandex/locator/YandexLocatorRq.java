package com.isharipov.domain.yandex.locator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@Builder
public class YandexLocatorRq {

    private Common common;
    @JsonProperty("wifi_networks")
    private List<WifiNetworks> wifiNetworks;
    @JsonProperty("gsm_cells")
    private List<GsmCells> gsmCells;
    private Ip ip;
}

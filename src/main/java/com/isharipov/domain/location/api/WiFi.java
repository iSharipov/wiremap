package com.isharipov.domain.location.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WiFi {
    private String bssid;
    private String channel;
    private String frequency;
    private String signal;
}

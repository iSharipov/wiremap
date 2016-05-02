package com.isharipov.domain.yandex.locator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position {
    private String latitude;
    private String longitude;
    private String altitude;
    private Float precision;
    private String altitude_precision;
    private String type;
}

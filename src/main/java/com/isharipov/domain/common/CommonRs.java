package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isharipov.domain.common.error.ErrorResponse;
import com.isharipov.domain.google.loc.Location;
import com.isharipov.domain.yandex.locator.Position;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonRs {
    private Position position;
    private Object error;
    private Location location;
    private Float accuracy;
    @JsonProperty("LocationRS")
    private LocationRs locationRs;
    private ErrorResponse errorResponse;
}

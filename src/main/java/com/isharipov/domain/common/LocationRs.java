package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isharipov.domain.skyhook.Location;
import lombok.Data;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRs {
    private String error;
    private String version;
    private Location location;
}

package com.isharipov.domain.skyhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String nlac;
    private String latitude;
    private String lap;
    private String ncell;
    private String nap;
    private String hpe;
    private String age;
    private String longitude;
}

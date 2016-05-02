package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Илья on 22.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {
    private String lat;
    private String lon;
    private String provider;
    private String error;
    @JsonIgnore
    private Float accuracy;
}

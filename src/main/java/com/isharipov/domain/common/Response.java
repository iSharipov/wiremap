package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Илья on 22.04.2016.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {
    private String lat;
    private String lon;
    private String provider;
    private String error;
    @JsonIgnore
    private Float accuracy;
}

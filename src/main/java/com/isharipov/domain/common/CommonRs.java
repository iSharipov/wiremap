package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@JsonIgnoreProperties
public class CommonRs {
    private Map<String, String> position;
    private Map<String, String> error;
    @JsonProperty("LocationRS")
    private Map<String, String> locationRs;
    private Map<String, String> location;
    private Float accuracy;
}

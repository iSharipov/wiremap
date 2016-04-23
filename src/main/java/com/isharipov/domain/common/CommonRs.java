package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonRs {
    private Map<String, String> position;
    private Object error;
    private Map<String, Object> location;
    private Float accuracy;
}

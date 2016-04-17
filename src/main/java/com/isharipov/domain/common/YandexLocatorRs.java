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
public class YandexLocatorRs {
    private Map<String, String> position;
    private Map<String, String> error;
    @JsonProperty("LocationRS")
    private Map<String, String> locationRs;
}

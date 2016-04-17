package com.isharipov.processing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "position")
public class CommonRequest {
    private String error;
    private String latitude;
    private String longitude;
}

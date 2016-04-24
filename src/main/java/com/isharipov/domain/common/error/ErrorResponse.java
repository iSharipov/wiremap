package com.isharipov.domain.common.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 23.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private Error error;
}

package com.isharipov.domain.common.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 24.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private String message;
    private String code;
}

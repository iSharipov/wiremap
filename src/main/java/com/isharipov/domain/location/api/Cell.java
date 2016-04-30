package com.isharipov.domain.location.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cell {
    private String cid;
    private String lac;
}

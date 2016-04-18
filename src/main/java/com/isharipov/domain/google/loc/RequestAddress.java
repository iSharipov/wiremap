package com.isharipov.domain.google.loc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * Created by Илья on 18.04.2016.
 */
@Data
public class RequestAddress {
    @JsonProperty("request_address")
    private boolean requestAddress;
}

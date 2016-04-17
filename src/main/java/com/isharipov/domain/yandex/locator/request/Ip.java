package com.isharipov.domain.yandex.locator.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
public class Ip {
    @JsonProperty("address_v4")
    private String addressV4;
}

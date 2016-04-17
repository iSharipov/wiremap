package com.isharipov.domain.yandex.locator.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
public class Common {
    private String version;
    @JsonProperty("api_key")
    private String apiKey;
}

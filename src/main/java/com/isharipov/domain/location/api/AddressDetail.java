package com.isharipov.domain.location.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDetail {
    private String county;
    private String city;
    private String state;
    private String country;
    @JsonProperty(value = "country_code")
    private String countryCode;
    @JsonProperty(value = "postal_code")
    private String postalCode;
}

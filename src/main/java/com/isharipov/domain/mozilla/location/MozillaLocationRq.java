package com.isharipov.domain.mozilla.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Илья on 30.04.2016.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MozillaLocationRq {
    private static final Logger log = LoggerFactory.getLogger(MozillaLocationRq.class);

    @Value("${mozilla.location.site}")
    private String site;

    @Value("${mozilla.location.apikey}")
    private String apiKey;


}

package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.GoogleMozillaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 30.04.2016.
 */
@Slf4j
@Service("mozillaLocationHttpReuqest")
public class MozillaLocationHttpRequestServiceImpl implements HttpRequestService {

    @Value("${mozilla.location.site}")
    private String site;

    @Value("${mozilla.location.apikey}")
    private String apiKey;

    @Autowired
    private GoogleMozillaRequest googleMozillaRequest;

    @Override
    public Future<CommonRs> createHttpRequest(Map<String, List<String>> params) {
        return googleMozillaRequest.getRequest(params, site, apiKey, "gsm", log);
    }
}

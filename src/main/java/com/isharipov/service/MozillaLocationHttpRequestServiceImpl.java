package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.GoogleMozillaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 30.04.2016.
 */
@Service("mozillaLocationHttpReuqest")
public class MozillaLocationHttpRequestServiceImpl implements HttpRequestService {

    private static final Logger log = LoggerFactory.getLogger(MozillaLocationHttpRequestServiceImpl.class);

    @Value("${mozilla.location.site}")
    private String site;

    @Value("${mozilla.location.apikey}")
    private String apiKey;

    @Autowired
    private GoogleMozillaRequest googleMozillaRequest;

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, String> params) {
        return googleMozillaRequest.getRequest(params, site, apiKey, "gsm", log);
    }
}

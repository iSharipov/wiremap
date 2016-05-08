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
 * Created by Илья on 21.04.2016.
 */
@Slf4j
@Service("googleHttpRequestService")
public class GoogleHttpRequestServiceImpl implements HttpRequestService {

    @Value("${google.loc.site.address}")
    private String siteAddress;

    @Value("${google.loc.apikey}")
    private String apiKey;

    @Autowired
    private GoogleMozillaRequest googleMozillaRequest;

    @Override
    public Future<CommonRs> createHttpRequest(Map<String, List<String>> params) {
        return googleMozillaRequest.getRequest(params, siteAddress, apiKey, "gsm", log);
    }
}

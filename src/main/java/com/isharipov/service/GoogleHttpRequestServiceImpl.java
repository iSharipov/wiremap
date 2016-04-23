package com.isharipov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.google.loc.GoogleRq;
import com.isharipov.domain.google.loc.WifiAccessPoint;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 21.04.2016.
 */
@Service("googleHttpRequestService")
public class GoogleHttpRequestServiceImpl implements HttpRequestService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Value("${google.loc.site.address}")
    private String siteAddress;

    @Value("${google.loc.apikey}")
    private String apiKey;

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, String> params) {
        WifiAccessPoint wifiAccessPoint = new WifiAccessPoint();
        wifiAccessPoint.setMacAddress(StringUtils.getMac(params.get("bssid"), "-"));
//        wifiAccessPoint.setSignalStrength("");
//        wifiAccessPoint.setAge("");
        ObjectMapper objectMapper = new ObjectMapper();
        List<WifiAccessPoint> wifiAccessPointsList = new ArrayList<>();
        wifiAccessPointsList.add(wifiAccessPoint);
        GoogleRq googleRq = GoogleRq.builder().wifiAccessPoints(wifiAccessPointsList).build();
        String googleLocatorRqJsonString = null;
        try {
            googleLocatorRqJsonString = objectMapper.writeValueAsString(googleRq);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(googleLocatorRqJsonString, headers);
        CommonRs exchange = restTemplate.postForObject(siteAddress + apiKey, entity, CommonRs.class);
        log.info("google: {}", exchange);
        return null;
    }

}

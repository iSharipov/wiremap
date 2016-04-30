package com.isharipov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.google.loc.CellTower;
import com.isharipov.domain.google.loc.GoogleRq;
import com.isharipov.domain.google.loc.WifiAccessPoint;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger log = LoggerFactory.getLogger(getClass());
    @Value("${google.loc.site.address}")
    private String siteAddress;

    @Value("${google.loc.apikey}")
    private String apiKey;

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, String> params) {
        /*Wifi Networks*/
        String bssid = params.get("bssid");
        String age;
        String signalStrength;
        WifiAccessPoint wifiAccessPoint = null;
        List<WifiAccessPoint> wifiAccessPointsList = null;
        if (bssid != null) {
            bssid = StringUtils.getMac(StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid), "-");
            age = params.get("age");
            signalStrength = params.get("signal");
            wifiAccessPoint = new WifiAccessPoint();
            wifiAccessPoint.setMacAddress(bssid);
            wifiAccessPoint.setAge(age);
            wifiAccessPoint.setSignalStrength(signalStrength);
            wifiAccessPointsList = new ArrayList<>();
            wifiAccessPointsList.add(wifiAccessPoint);
        }
        /*GSM Cells*/
        String mobileCountryCode = params.get("mcc");
        String mobileNetworkCode;
        String locationAreaCode;
        String cellId;
        CellTower cellTower = null;
        List<CellTower> cellTowers = null;
        if (mobileCountryCode != null) {
            mobileNetworkCode = params.get("mnc");
            locationAreaCode = params.get("lac");
            cellId = params.get("cid");
            cellTower = new CellTower();
            cellTower.setMobileCountryCode(mobileCountryCode);
            cellTower.setMobileNetworkCode(mobileNetworkCode);
            cellTower.setLocationAreaCode(locationAreaCode);
            cellTower.setCellId(cellId);
            cellTowers = new ArrayList<>();
            cellTowers.add(cellTower);
        }

        GoogleRq googleRq = GoogleRq.builder().considerIp(false).cellTowers(cellTowers).wifiAccessPoints(wifiAccessPointsList).build();
        String googleLocatorRqJsonString = null;
        try {
            googleLocatorRqJsonString = objectMapper.writeValueAsString(googleRq);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(googleLocatorRqJsonString, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(siteAddress + apiKey, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }
}

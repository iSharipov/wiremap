package com.isharipov.domain.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.google.loc.CellTower;
import com.isharipov.domain.google.loc.GoogleRq;
import com.isharipov.domain.google.loc.WifiAccessPoint;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 30.04.2016.
 */
@Service
public class GoogleMozillaRequest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public Future<CommonRs> getRequest(Map<String, String> params, String site, String apiKey, String radioType, Logger log) {
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
            cellTower.setRadioType(radioType);
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

        ResponseEntity<String> responseEntity = restTemplate.exchange(site + apiKey, HttpMethod.POST, entity, String.class);
        String responseBody = responseEntity.getBody();
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }

}

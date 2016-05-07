package com.isharipov.domain.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.google.loc.CellTower;
import com.isharipov.domain.google.loc.GoogleRq;
import com.isharipov.domain.google.loc.WifiAccessPoint;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.Systems;
import org.slf4j.Logger;
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
 * Created by Илья on 30.04.2016.
 */
@Service
public class GoogleMozillaRequest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${systems}")
    private List<Systems> systems;

    @Async
    public Future<CommonRs> getRequest(Map<String, String[]> params, String site, String apiKey, String radioType, Logger log) {
        /*Wifi Networks*/
        String[] bssid = params.get("bssid");
        String[] age;
        String[] signalStrength;
        WifiAccessPoint wifiAccessPoint;
        List<WifiAccessPoint> wifiAccessPointsList = null;
        if (bssid != null) {
            wifiAccessPointsList = new ArrayList<>();
            for (String mac : bssid) {
                wifiAccessPoint = new WifiAccessPoint();
                wifiAccessPoint.setMacAddress(mac);
                wifiAccessPointsList.add(wifiAccessPoint);
            }

            signalStrength = params.get("signal");
            if (signalStrength != null) {
                for (int i = 0; i < signalStrength.length && i < bssid.length; i++) {
                    wifiAccessPointsList.get(i).setSignalStrength(signalStrength[i]);
                }
            }

            age = params.get("age");
            if (age != null) {
                for (int i = 0; i < age.length && i < age.length; i++) {
                    wifiAccessPointsList.get(i).setAge(age[i]);
                }
            }
        }
    /*GSM Cells*/
        String[] mobileCountryCode = params.get("mcc");
        String[] mobileNetworkCode;
        String[] locationAreaCode;
        String[] cellId;
        CellTower cellTower;
        List<CellTower> cellTowers = null;
        if (mobileCountryCode != null) {
            cellTowers = new ArrayList<>();
            for (String m : mobileCountryCode) {
                cellTower = new CellTower();
                cellTower.setMobileCountryCode(m);
                cellTower.setRadioType(radioType);
                cellTowers.add(cellTower);
            }
            mobileNetworkCode = params.get("mnc");
            for (int i = 0; i < mobileNetworkCode.length && i < mobileCountryCode.length; i++) {
                cellTowers.get(i).setMobileNetworkCode(mobileNetworkCode[i]);
            }
            locationAreaCode = params.get("lac");
            for (int i = 0; i < locationAreaCode.length && i < mobileCountryCode.length; i++) {
                cellTowers.get(i).setLocationAreaCode(locationAreaCode[i]);
            }
            cellId = params.get("cid");
            for (int i = 0; i < cellId.length && i < mobileCountryCode.length; i++) {
                cellTowers.get(i).setCellId(cellId[i]);
            }
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

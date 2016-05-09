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
    public Future<CommonRs> getRequest(Map<String, List<String>> params, String site, String apiKey, String radioType, Logger log) {
        /*Wifi Networks*/
        List<String> bssid = params.get("bssid");
        List<String> age;
        List<String> signalStrength;
        WifiAccessPoint wifiAccessPoint;
        List<WifiAccessPoint> wifiAccessPointsList = null;
        if (bssid != null) {
            wifiAccessPointsList = new ArrayList<>();
            for (String mac : bssid) {
                wifiAccessPoint = new WifiAccessPoint();
                wifiAccessPoint.setMacAddress(StringUtils.getMac(mac, ":"));
                wifiAccessPointsList.add(wifiAccessPoint);
            }

            signalStrength = params.get("ssw");
            if (signalStrength != null) {
                for (int i = 0; i < signalStrength.size() && i < bssid.size(); i++) {
                    wifiAccessPointsList.get(i).setSignalStrength(signalStrength.get(i));
                }
            }

            age = params.get("age");
            if (age != null) {
                for (int i = 0; i < age.size() && i < age.size(); i++) {
                    wifiAccessPointsList.get(i).setAge(age.get(i));
                }
            }
        }
    /*GSM Cells*/
        List<String> mobileCountryCode = params.get("mcc");
        List<String> mobileNetworkCode;
        List<String> locationAreaCode;
        List<String> cellId;
        List<String> ssc;
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
            for (int i = 0; i < mobileNetworkCode.size() && i < mobileCountryCode.size(); i++) {
                cellTowers.get(i).setMobileNetworkCode(mobileNetworkCode.get(i));
            }

            locationAreaCode = params.get("lac");
            for (int i = 0; i < locationAreaCode.size() && i < mobileCountryCode.size(); i++) {
                cellTowers.get(i).setLocationAreaCode(locationAreaCode.get(i));
            }

            cellId = params.get("cid");
            for (int i = 0; i < cellId.size() && i < mobileCountryCode.size(); i++) {
                cellTowers.get(i).setCellId(cellId.get(i));
            }

            ssc = params.get("ssc");
            if (ssc != null) {
                for (int i = 0; i < cellId.size() && i < mobileCountryCode.size(); i++) {
                    cellTowers.get(i).setSignalStrength(ssc.get(i));
                }
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
        ResponseEntity<String> responseEntity;
        String responseBody;
        try {
            responseEntity = restTemplate.exchange(site + apiKey, HttpMethod.POST, entity, String.class);
            responseBody = responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }

}

package com.isharipov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.combain.CellTower;
import com.isharipov.domain.combain.CombainRq;
import com.isharipov.domain.combain.WifiAccessPoint;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 24.04.2016.
 */
@Slf4j
@Service("combainHttpRequestService")
public class CombainHttpRequestServiceImpl implements HttpRequestService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${combain.site.address}")
    private String siteAddress;

    @Value("${combain.apikey}")
    private String apiKey;

    @Override
    public Future<CommonRs> createHttpRequest(Map<String, String[]> params) {
        /*Wifi Networks*/
        String[] bssid = params.get("bssid");
        String[] signalStrengthWifi;
        WifiAccessPoint wifiAccessPoint;
        List<WifiAccessPoint> wifiAccessPointList = null;
        if (bssid != null) {
            wifiAccessPointList = new ArrayList<>();
            for (String mac : bssid) {
                wifiAccessPoint = new WifiAccessPoint();
                wifiAccessPoint.setMacAddress(StringUtils.getMac(mac, ":"));
                wifiAccessPointList.add(wifiAccessPoint);
            }

            signalStrengthWifi = params.get("sstrw");
            if (signalStrengthWifi != null) {
                for (int i = 0; i < signalStrengthWifi.length && i < bssid.length; i++) {
                    wifiAccessPointList.get(i).setSignalStrength(signalStrengthWifi[i]);
                }
            }
        }

         /*GSM Cells*/
        String[] countryCode = params.get("mcc");
        String[] operatorId;
        String[] lac;
        String[] cellId;
        CellTower cellTower;
        List<CellTower> cellTowersList = null;
        if (countryCode != null) {
            cellTowersList = new ArrayList<>();
            for (String mcc : countryCode) {
                cellTower = new CellTower();
                cellTower.setMobileCountryCode(mcc);
                cellTowersList.add(cellTower);
            }

            operatorId = params.get("mnc");
            if (operatorId != null) {
                for (int i = 0; i < operatorId.length && i < countryCode.length; i++) {
                    cellTowersList.get(i).setMobileNetworkCode(operatorId[i]);
                }
            }

            lac = params.get("lac");
            if (lac != null) {
                for (int i = 0; i < lac.length && i < countryCode.length; i++) {
                    cellTowersList.get(i).setLocationAreaCode(lac[i]);
                }
            }

            cellId = params.get("cid");
            if (cellId != null) {
                for (int i = 0; i < cellId.length && i < countryCode.length; i++) {
                    cellTowersList.get(i).setLocationAreaCode(cellId[i]);
                }
            }
        }

        CombainRq combainRq = CombainRq.builder().cellTowers(cellTowersList)
                .wifiAccessPoints(wifiAccessPointList)
                .build();

        String combainRqJsonString = null;
        try {
            combainRqJsonString = objectMapper.writeValueAsString(combainRq);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(combainRqJsonString, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(siteAddress + apiKey, entity, String.class);
        String responseBody = responseEntity.getBody();
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }
}

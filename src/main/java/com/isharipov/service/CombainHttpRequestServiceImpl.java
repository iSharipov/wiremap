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
    public Future<CommonRs> createHttpRequest(Map<String, String> params) {
         /*GSM Cells*/
        String countryCode = params.get("ccode");
        String operatorId = params.get("opid");
        String cellId = params.get("cellid");
        String lac = params.get("lac");
        String signalStrengthCell = params.get("sstrc");
        String ageCell = params.get("agec");
        /*Wifi Networks*/
        String mac = StringUtils.getMac(params.get("bssid"), ":");
        String signalStrengthWifi = params.get("sstrw");

        WifiAccessPoint wifiAccessPoint = new WifiAccessPoint();
        wifiAccessPoint.setMacAddress(mac);
        wifiAccessPoint.setSignalStrength(signalStrengthWifi);
        List<WifiAccessPoint> wifiAccessPointList = new ArrayList<>();
        wifiAccessPointList.add(wifiAccessPoint);
        CellTower cellTower;
        List<CellTower> cellTowersList = null;
        if (cellId != null) {
            cellTower = new CellTower();
            cellTower.setMobileCountryCode(countryCode);
            cellTower.setMobileNetworkCode(operatorId);
            cellTower.setCellId(cellId);
            cellTower.setLocationAreaCode(lac);
            cellTower.setSignalStrength(signalStrengthCell);
            cellTowersList = new ArrayList<>();
            cellTowersList.add(cellTower);
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

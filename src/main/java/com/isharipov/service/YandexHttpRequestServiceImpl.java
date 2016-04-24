package com.isharipov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.yandex.locator.*;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 20.04.2016.
 */
@Service("yandexHttpRequestService")
public class YandexHttpRequestServiceImpl implements HttpRequestService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${yandexlocator.version}")
    private String version;

    @Value("${yandexlocator.apikey}")
    private String apiKey;

    @Value("${yandexlocator.site.address}")
    private String siteAddress;

    @Async
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
        String mac = StringUtils.getMac(params.get("bssid"), "-");
        String signalStrengthWifi = params.get("sstrw");
        String ageWifi = params.get("agew");

        Common common = new Common();
        common.setVersion(version);
        common.setApiKey(apiKey);

        WifiNetworks wifiNetworks = new WifiNetworks();
        wifiNetworks.setMac(mac);
        wifiNetworks.setSignalStrength(signalStrengthWifi);
        wifiNetworks.setAge(ageWifi);
        List<WifiNetworks> wifiNetworksList = new ArrayList<>();
        wifiNetworksList.add(wifiNetworks);
        GsmCells gsmCells;
        List<GsmCells> gsmCellsList = null;
        if (signalStrengthCell != null) {
            gsmCells = new GsmCells();
            gsmCells.setCountryCode(countryCode);
            gsmCells.setOperatorId(operatorId);
            gsmCells.setCellId(cellId);
            gsmCells.setLac(lac);
            gsmCells.setSignalStrength(signalStrengthCell);
            gsmCells.setAge(ageCell);
            gsmCellsList = new ArrayList<>();
            gsmCellsList.add(gsmCells);
        }

        YandexLocatorRq yandexLocatorRq = YandexLocatorRq.builder().common(common)
                .gsmCells(gsmCellsList)
                .wifiNetworks(wifiNetworksList)
                .build();

        String yandexLocatorRqJsonString = null;
        try {
            yandexLocatorRqJsonString = objectMapper.writeValueAsString(yandexLocatorRq);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("json", yandexLocatorRqJsonString);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(siteAddress, requestParams, String.class);
        String responseBody = responseEntity.getBody();
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }
}

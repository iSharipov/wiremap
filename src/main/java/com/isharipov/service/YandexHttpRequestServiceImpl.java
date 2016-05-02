package com.isharipov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.yandex.locator.Common;
import com.isharipov.domain.yandex.locator.GsmCells;
import com.isharipov.domain.yandex.locator.WifiNetworks;
import com.isharipov.domain.yandex.locator.YandexLocatorRq;
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
        /*Common*/
        Common common = new Common();
        common.setVersion(version);
        common.setApiKey(apiKey);

        /*Wifi Networks*/
        String bssid = params.get("bssid");
        String signal;
        String age;
        WifiNetworks wifiNetworks;
        List<WifiNetworks> wifiNetworksList = null;
        if (bssid != null) {
            bssid = StringUtils.getMac(StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid), "-");
            signal = params.get("signal");
            age = params.get("age");
            wifiNetworks = new WifiNetworks();
            wifiNetworks.setMac(bssid);
            wifiNetworks.setSignalStrength(signal);
            wifiNetworks.setAge(age);
            wifiNetworksList = new ArrayList<>();
            wifiNetworksList.add(wifiNetworks);
        }
        String mcc = params.get("mcc");
        String mnc;
        String lac;
        String cid;
        GsmCells gsmCells;
        List<GsmCells> gsmCellsList = null;
        if (mcc != null) {
            mnc = params.get("mnc");
            lac = params.get("lac");
            cid = params.get("cid");
            gsmCells = new GsmCells();
            gsmCells.setCountryCode(mcc);
            gsmCells.setOperatorId(mnc);
            gsmCells.setLac(lac);
            gsmCells.setCellId(cid);
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

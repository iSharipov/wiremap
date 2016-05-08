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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service("yandexHttpRequestService")
public class YandexHttpRequestServiceImpl implements HttpRequestService {

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
    public Future<CommonRs> createHttpRequest(Map<String, List<String>> params) {
        /*Common*/
        Common common = new Common();
        common.setVersion(version);
        common.setApiKey(apiKey);

        /*Wifi Networks*/
        List<String> bssid = params.get("bssid");
        List<String> signal;
        List<String> age;
        List<WifiNetworks> wifiNetworksList = null;
        WifiNetworks wifiNetworks;
        if (bssid != null) {
            wifiNetworksList = new ArrayList<>();
            for (String aBssid : bssid) {
                wifiNetworks = new WifiNetworks();
                wifiNetworks.setMac(StringUtils.getMac(aBssid, "-"));
                wifiNetworksList.add(wifiNetworks);
            }

            signal = params.get("signal");
            if (signal != null) {
                for (int i = 0; i < signal.size() && i < bssid.size(); i++) {
                    wifiNetworksList.get(i).setSignalStrength(signal.get(i));
                }
            } else {
                for (WifiNetworks w : wifiNetworksList) {
                    w.setSignalStrength(null);
                }
            }

            age = params.get("age");
            if (age != null) {
                for (int i = 0; i < age.size() && i < bssid.size(); i++) {
                    wifiNetworksList.get(i).setAge(age.get(i));
                }
            } else {
                for (WifiNetworks w : wifiNetworksList) {
                    w.setAge(null);
                }
            }
        }

        List<String> mcc = params.get("mcc");
        List<String> mnc;
        List<String> lac;
        List<String> cid;
        GsmCells gsmCells;
        List<GsmCells> gsmCellsList = null;
        if (mcc != null) {
            gsmCellsList = new ArrayList<>();
            for (String m : mcc) {
                gsmCells = new GsmCells();
                gsmCells.setCountryCode(m);
                gsmCellsList.add(gsmCells);
            }
            mnc = params.get("mnc");
            for (int i = 0; i < mnc.size() && i < mcc.size(); i++) {
                gsmCellsList.get(i).setOperatorId(mnc.get(i));
            }

            lac = params.get("lac");
            for (int i = 0; i < lac.size() && i < mcc.size(); i++) {
                gsmCellsList.get(i).setOperatorId(lac.get(i));
            }
            cid = params.get("cid");

            for (int i = 0; i < cid.size() && i < mcc.size(); i++) {
                gsmCellsList.get(i).setCellId(cid.get(i));
            }
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
        ResponseEntity<String> responseEntity;
        String responseBody;
        try {
            responseEntity = restTemplate.postForEntity(siteAddress, requestParams, String.class);
            responseBody = responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
    }
}

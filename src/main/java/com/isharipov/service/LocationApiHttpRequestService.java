package com.isharipov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 30.04.2016.
 */
@Slf4j
@Service("locationApiHttpRequest")
public class LocationApiHttpRequestService implements HttpRequestService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${location.api.token}")
    private String token;

    @Value("${location.api.site.asia}")
    private String site;

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, List<String>> params) {
//
//        /*WiFi*/
//        String bssid = params.get("bssid");
//        String signal;
//        WiFi wiFi1;
//        WiFi wiFi2;
//        List<WiFi> wiFiList = null;
//        if (bssid != null) {
////            bssid = StringUtils.getMac(StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid), ":");
//            signal = params.get("signal");
//            wiFi1 = new WiFi();
//            wiFi2 = new WiFi();
//            wiFi1.setBssid(bssid);
//            wiFi1.setSignal(signal);
//            wiFi2.setBssid("aa:aa:aa:aa:aa:aa");
//            wiFiList = new ArrayList<>();
//            wiFiList.add(wiFi1);
//            wiFiList.add(wiFi2);
//        }
//        /*Cell*/
//        String mcc = params.get("mcc");
//        String mnc = null;
//        String lac;
//        String cid;
//        String radio = null;
//        Cell cell;
//        List<Cell> cellList = null;
//        if (mcc != null) {
//            radio = "gsm";
//            mnc = params.get("mnc");
//            lac = params.get("lac");
//            cid = params.get("cid");
//            cell = new Cell();
//            cell.setLac(lac);
//            cell.setCid(cid);
//            cellList = new ArrayList<>();
//            cellList.add(cell);
//        }
//        LocationApiRq locationApiRq = LocationApiRq.builder()
//                .token(token).radio(radio).mcc(mcc).mnc(mnc)
//                .cells(cellList).wifi(wiFiList).build();
//
//        String locationApiRqJsonString = null;
//
//        try {
//            locationApiRqJsonString = objectMapper.writeValueAsString(locationApiRq);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        HttpEntity<String> httpEntity = new HttpEntity<>(locationApiRqJsonString);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(site, HttpMethod.POST, httpEntity, String.class);
//        String responseBody = responseEntity.getBody();
//        return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
//    }
        return null;
    }
}

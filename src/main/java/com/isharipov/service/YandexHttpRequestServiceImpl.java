package com.isharipov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.yandex.locator.*;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Илья on 20.04.2016.
 */
@Service
public class YandexHttpRequestServiceImpl implements HttpRequestService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${yandexlocator.version}")
    private String version;

    @Value("${yandexlocator.apikey}")
    private String apiKey;

    @Value("${yandexlocator.site.address}")
    private String siteAddress;

    @Override
    public HttpRequest createHttpRequest(Map<String, String> params) {
        /*GSM Cells*/
        String countryCode = params.get("ccode");
        String operatorId = params.get("opid");
        String cellId = params.get("cellid");
        String lac = params.get("lac");
        String signalStrengthCell = params.get("sstrc");
        String ageCell = params.get("agec");
        /*Wifi Networks*/
        String mac = params.get("mac");
        String signalStrengthWifi = params.get("sstrw");
        String ageWifi = params.get("agew");
        /*Ip*/
        String addressV4 = params.get("ip");

        Common common = new Common();
        common.setVersion(version);
        common.setApiKey(apiKey);

        WifiNetworks wifiNetworks = new WifiNetworks();
        wifiNetworks.setMac(mac);
        wifiNetworks.setSignalStrength(signalStrengthWifi);
        wifiNetworks.setAge(ageWifi);
        List<WifiNetworks> wifiNetworksList = new ArrayList<>();
        wifiNetworksList.add(wifiNetworks);

        GsmCells gsmCells = new GsmCells();
        gsmCells.setCountryCode(countryCode);
        gsmCells.setOperatorId(operatorId);
        gsmCells.setCellId(cellId);
        gsmCells.setLac(lac);
        gsmCells.setSignalStrength(signalStrengthCell);
        gsmCells.setAge(ageCell);
        List<GsmCells> gsmCellsList = new ArrayList<>();
        gsmCellsList.add(gsmCells);

        Ip ip = new Ip();
        ip.setAddressV4(addressV4);

        YandexLocatorRq yandexLocatorRq = YandexLocatorRq.builder().common(common)
                .gsmCells(gsmCellsList)
                .wifiNetworks(wifiNetworksList)
                .ip(ip)
                .build();

        try {
            String yandexLocatorRqJsonString = objectMapper.writeValueAsString(yandexLocatorRq);

            HttpPost request = new HttpPost(siteAddress);
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("json", yandexLocatorRqJsonString));

            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            return request;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

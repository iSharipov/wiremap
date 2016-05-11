package com.isharipov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.skyhook.AccessPoint;
import com.isharipov.domain.skyhook.AuthenticationParameters;
import com.isharipov.domain.skyhook.Key;
import com.isharipov.domain.skyhook.SkyhookLocationRq;
import com.isharipov.utils.ResponseUtil;
import com.isharipov.utils.Systems;
import lombok.extern.slf4j.Slf4j;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 20.04.2016.
 */
@Slf4j
@Service("skyHookHttpRequestService")
public class SkyHookHttpRequestServiceImpl implements HttpRequestService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${skyhook.email}")
    private String email;

    @Value("${skyhook.apikey}")
    private String apiKey;

    @Value("${skyhook.site.address}")
    private String siteAddress;

    @Value("${skyhook.authentication.version}")
    private String authenticationVersion;

    @Value("${skyhook.version}")
    private String version;

    @Value("${systems}")
    private List<Systems> systems;

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, List<String>> params) {
        List<String> bssid = params.get("bssid");
        if (bssid == null || !systems.contains(Systems.SKYHOOK)) {
            return new AsyncResult<>(null);
        }
        List<String> signal = params.get("ssw");

        SkyhookLocationRq skyhookLocationRq = new SkyhookLocationRq();
        Key key = new Key();
        key.setKey(apiKey);
        key.setUsername(email);
        AuthenticationParameters authenticationParameters = new AuthenticationParameters();
        authenticationParameters.setKey(key);
        authenticationParameters.setVersion(authenticationVersion);

        skyhookLocationRq.setAuthentication(authenticationParameters);
        skyhookLocationRq.setVersion(version);
        AccessPoint[] accessPoints = new AccessPoint[bssid.size()];

        for (int i = 0; i < accessPoints.length; i++) {
            accessPoints[i] = new AccessPoint();
            accessPoints[i].setMac(bssid.get(i));
            accessPoints[i].setSignalStrength("-60");
        }

        if (signal != null) {
            for (int i = 0; i < signal.size() && i < accessPoints.length; i++) {
                accessPoints[i].setSignalStrength(signal.get(i));
            }
        }

        skyhookLocationRq.setAccessPoint(accessPoints);

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(SkyhookLocationRq.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(skyhookLocationRq, sw);
            String xmlString = sw.toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(xmlString, headers);
            ResponseEntity<String> responseEntity;
            String responseBody;
            try {
                responseEntity = restTemplate.exchange(siteAddress, HttpMethod.POST, entity, String.class);
                responseBody = XML.toJSONObject(responseEntity.getBody()).toString();
            } catch (Exception e) {
                return null;
            }
            return ResponseUtil.handleError(responseEntity, responseBody, objectMapper, log);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
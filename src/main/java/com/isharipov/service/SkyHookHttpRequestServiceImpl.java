package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.skyhook.AccessPoint;
import com.isharipov.domain.skyhook.AuthenticationParameters;
import com.isharipov.domain.skyhook.Key;
import com.isharipov.domain.skyhook.SkyhookLocationRq;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 20.04.2016.
 */
@Service("skyHookHttpRequestService")
public class SkyHookHttpRequestServiceImpl implements HttpRequestService {

    private Logger log = LoggerFactory.getLogger(getClass());
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

    @Async
    @Override
    public Future<CommonRs> createHttpRequest(Map<String, String> params) {
        String mac = params.get("bssid");
        String signalStrengthWifi = params.get("sstrw");

        SkyhookLocationRq skyhookLocationRq = new SkyhookLocationRq();
        Key key = new Key();
        key.setKey(apiKey);
        key.setUsername(email);
        AuthenticationParameters authenticationParameters = new AuthenticationParameters();
        authenticationParameters.setKey(key);
        authenticationParameters.setVersion(authenticationVersion);

        skyhookLocationRq.setAuthentication(authenticationParameters);
        skyhookLocationRq.setVersion(version);
        skyhookLocationRq.setStreetAddressLookup("full");
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setMac(mac);
        accessPoint.setSignalStrength(signalStrengthWifi);
        skyhookLocationRq.setAccessPoint(accessPoint);
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(SkyhookLocationRq.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(skyhookLocationRq, sw);
            String xmlString = sw.toString();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            HttpEntity<String> entity = new HttpEntity<>(xmlString, headers);
            restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
            CommonRs exchange = restTemplate.postForObject(siteAddress, entity, CommonRs.class);
            log.info("skyhook: {}", exchange);
            return new AsyncResult<>(exchange);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
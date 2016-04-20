package com.isharipov.service;

import com.isharipov.domain.skyhook.AccessPoint;
import com.isharipov.domain.skyhook.AuthenticationParameters;
import com.isharipov.domain.skyhook.Key;
import com.isharipov.domain.skyhook.SkyhookLocationRq;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Илья on 20.04.2016.
 */
@Service
public class SkyHookHttpRequestServiceImpl implements HttpRequestService {

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

    @Override
    public HttpRequest createHttpRequest(Map<String, String> params) {
        String mac = params.get("mac");
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
        accessPoint.setSsid("");
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

            HttpPost request = new HttpPost(siteAddress);
            HttpEntity entity = null;
            try {
                entity = new ByteArrayEntity(xmlString.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            request.setEntity(entity);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
            return request;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}

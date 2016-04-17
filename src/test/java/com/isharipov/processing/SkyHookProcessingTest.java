package com.isharipov.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.config.spring.AppConfig;
import com.isharipov.config.spring.WebConfig;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.skyhook.AccessPoint;
import com.isharipov.domain.skyhook.AuthenticationParameters;
import com.isharipov.domain.skyhook.Simple;
import com.isharipov.domain.skyhook.SkyhookLocationRq;
import com.isharipov.domain.yandex.locator.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Илья on 11.04.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, WebConfig.class})
public class SkyHookProcessingTest {
    public static final String USER_NAME = "isharipov";
    public static final String REALM = "STOLOTO";
    private static final String SITE_ADDR =
            "https://api.skyhookwireless.com/wps2/location";
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getLocation() throws IOException, JAXBException {
        InetAddress ip;
        String macAddress = null;
        String signalStrength = "-70";
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);
            byte[] mac = networkInterface.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAddress = sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String xmlQuery = "<?xml version='1.0'?>\n" +
                "<LocationRQ xmlns='http://skyhookwireless.com/wps/2005'" +
                " version='2.10' street-address-lookup='full'>\n" +
                "<authentication version='2.0'>\n" +
                "<simple>\n" +
                "<username>" + "beta" + "</username>\n" +
                "<realm>" + "js.loki.com" + "</realm> " +
                "</simple>\n" +
                "</authentication> \n" +
                "<access-point> \n" +
                "<mac>" + macAddress + "</mac> \n" +
                "<signal-strength>" + "" +
                "</signal-strength>\n" +
                "<age>7984</age>\n" +
                "</access-point> \n" +
                "</LocationRQ>";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SITE_ADDR);
        String xml = xmlQuery;
        SkyhookLocationRq skyhookLocationRq = new SkyhookLocationRq();
        Simple simple = new Simple();
        simple.setUsername("beta");
        simple.setRealm("js.loki.com");
        AuthenticationParameters authenticationParameters = new AuthenticationParameters();
        authenticationParameters.setSimple(simple);
        authenticationParameters.setVersion("2.0");

        skyhookLocationRq.setAuthentication(authenticationParameters);
        skyhookLocationRq.setVersion("2.10");
        skyhookLocationRq.setStreetAddressLookup("full");
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setMac("EC-55-F9-17-53-28");
        accessPoint.setSignalStrength("-70");
        accessPoint.setAge("0");
        skyhookLocationRq.setAccessPoint(accessPoint);
        JAXBContext jaxbContext = JAXBContext.newInstance(SkyhookLocationRq.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(skyhookLocationRq, sw);
        String xmkString = sw.toString();
        log.info(xmkString);

        HttpEntity entity = new ByteArrayEntity(xmkString.getBytes("UTF-8"));
        post.setEntity(entity);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject xmlJsonObject = XML.toJSONObject(result);
        String prettyPrintJson = xmlJsonObject.toString(4);

        CommonRs commonRequest = objectMapper.readValue(prettyPrintJson, CommonRs.class);
    }

    @Test
    public void getYandexLocation() throws JsonProcessingException {
        Common common = new Common();
        common.setVersion("1.0");
        common.setApiKey("AHrj7lYBAAAAUTkMHwIA6MuvduXQTrnv_pLH4lRYhJ2RxDsAAAAAAAAAAACTHacV8CkBA_xGvIdLhRGynl8dGw==");

        WifiNetworks wifiNetworks = new WifiNetworks();
        wifiNetworks.setMac("00-1C-F0-E4-BB-F5");
        wifiNetworks.setSignalStrength("-88");
        wifiNetworks.setAge("0");
        List<WifiNetworks> wifiNetworksList = new ArrayList<>();
        wifiNetworksList.add(wifiNetworks);

        GsmCells gsmCells = new GsmCells();
        gsmCells.setCountryCode("250");
        gsmCells.setOperatorId("99");
        gsmCells.setCellId("42332");
        gsmCells.setLac("36003");
        gsmCells.setSignalStrength("-80");
        gsmCells.setAge("5555");
        List<GsmCells> gsmCellsList = new ArrayList<>();
        gsmCellsList.add(gsmCells);

        Ip ip = new Ip();
        ip.setAddressV4("178.247.233.32");

        YandexLocatorRq yandexLocatorRq = YandexLocatorRq.builder().common(common)
                .gsmCells(gsmCellsList)
                .wifiNetworks(wifiNetworksList)
                .ip(ip)
                .build();

        String yandexLocatorRqJsonString = objectMapper.writeValueAsString(yandexLocatorRq);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://api.lbs.yandex.net/geolocation");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("json", yandexLocatorRqJsonString));
        try {
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            CommonRs commonRequest = objectMapper.readValue(result, CommonRs.class);
            log.info(commonRequest.toString());
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


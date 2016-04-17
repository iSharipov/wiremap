package com.isharipov.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.config.spring.AppConfig;
import com.isharipov.config.spring.PersistenceConfig;
import com.isharipov.config.spring.WebConfig;
import com.isharipov.domain.YandexLocatorRq;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Илья on 11.04.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, PersistenceConfig.class, WebConfig.class})
public class SkyHookProcessingTest {
    public static final String USER_NAME = "isharipov";
    public static final String REALM = "STOLOTO";
    private static final String SITE_ADDR =
            "https://api.skyhookwireless.com/wps2/location";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getLocation() throws IOException {
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
            macAddress = "24:b6:57:b5:59:06";//sb.toString();
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
                "<username>" + "cyberzerocool" + "</username>\n" +
                "<realm>" + "cyberzerocool.com" + "</realm> " +
                "</simple>\n" +
                "</authentication> \n" +
                "<access-point> \n" +
                "<mac>" + "d4:d7:48:0c:0a:df" + "</mac> \n" +
                "<signal-strength>" + signalStrength +
                "</signal-strength>\n" +
                "<age>7984</age>\n" +
                "</access-point> \n" +
                "</LocationRQ>";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SITE_ADDR);
        String xml = xmlQuery;
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        post.setEntity(entity);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);

    }
    @Test
    public void getYandexLocation() {
        YandexLocatorRq yandexLocatorRq = new YandexLocatorRq();
        List<HashMap<String, String>> common= new ArrayList<>();
//        common.add(new HashMap<>().put("version", "1.0"));
        Map<String, String> commonMap = new HashMap<>();
        Map<String, Integer> gsmCells = new HashMap<>();
        Map<String, Integer> wifiNetworks = new HashMap<>();
        commonMap.put("version", "1.0");
        gsmCells.put("countrycode", 250);
        gsmCells.put("operatorid", 99);
        gsmCells.put("cellid", 42332);
        gsmCells.put("lac", 36002);
        gsmCells.put("signal_strength", -80);
        gsmCells.put("age", 5555);
       yandexLocatorRq.setCommon(commonMap);
        try {
            String s = objectMapper.writeValueAsString(yandexLocatorRq);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}


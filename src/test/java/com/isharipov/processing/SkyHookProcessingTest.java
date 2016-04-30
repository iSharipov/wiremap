package com.isharipov.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isharipov.config.spring.AppConfig;
import com.isharipov.config.spring.WebConfig;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.service.HttpRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 11.04.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, WebConfig.class})
public class SkyHookProcessingTest {

    private static final Logger log = LoggerFactory.getLogger(SkyHookProcessingTest.class);

    @Autowired
    @Qualifier("googleHttpRequestService")
    private HttpRequestService googleHttpRequestService;

    @Autowired
    @Qualifier("yandexHttpRequestService")
    private HttpRequestService yandexHttpRequestService;

    @Autowired
    @Qualifier("skyHookHttpRequestService")
    private HttpRequestService skyHookHttpRequestService;

    @Autowired
    @Qualifier("locationApiHttpRequest")
    private HttpRequestService locationApiHttpRequest;

    @Autowired
    @Qualifier("mozillaLocationHttpReuqest")
    private HttpRequestService mozillaLocationHttpRequest;

    @Test
    public void getSkyHookLocation() {
        getLocation(skyHookHttpRequestService);
    }

    @Test
    public void getYandexLocation() throws JsonProcessingException {
        getLocation(yandexHttpRequestService);
    }

    @Test
    public void getGoogleLocation() throws JsonProcessingException {
        getLocation(googleHttpRequestService);
    }

    @Test
    public void getLocationApiLocation() {
        getLocation(locationApiHttpRequest);
    }

    @Test
    public void getLocationMozillaLocation() {
        getLocation(mozillaLocationHttpRequest);
    }

    private void getLocation(HttpRequestService httpRequestService) {
        //SkyHook bssid E01C413BD514
        Map<String, String> wifiParams = new HashMap<>();
        wifiParams.put("bssid", "cc:5d:4e:50:8d:ac");
        Map<String, String> gsmParams = new HashMap<>();
        gsmParams.put("cid", "16655");
        gsmParams.put("lac", "27408");
        gsmParams.put("mnc", "99");
        gsmParams.put("mcc", "250");
        Map<String, String> commonParams = new HashMap<>();
        commonParams.put("bssid", "cc:5d:4e:50:8d:ac");
        commonParams.put("cid", "16655");
        commonParams.put("lac", "27408");
        commonParams.put("mnc", "99");
        commonParams.put("mcc", "250");
        long start = System.currentTimeMillis();
        Future<CommonRs> httpRequestWifi = httpRequestService.createHttpRequest(wifiParams);
        Future<CommonRs> httpRequestGsm = httpRequestService.createHttpRequest(gsmParams);
        Future<CommonRs> httpRequestCommon = httpRequestService.createHttpRequest(commonParams);
        try {
            while (!(httpRequestWifi.isDone() && httpRequestGsm.isDone() && httpRequestCommon.isDone())) {
                Thread.sleep(10); //10-millisecond pause between each check
            }
            CommonRs commonRsWifi = httpRequestWifi.get();
            CommonRs commonRsGsm = httpRequestGsm.get();
            CommonRs commonRsCommon = httpRequestCommon.get();
            log.info("{}", "Elapsed time: " + (System.currentTimeMillis() - start));
            log.info("{}", commonRsWifi);
            log.info("{}", commonRsGsm);
            log.info("{}", commonRsCommon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
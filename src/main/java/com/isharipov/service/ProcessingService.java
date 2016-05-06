package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import com.isharipov.utils.Systems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 22.04.2016.
 */
@Slf4j
@Service
public class ProcessingService {

    @Value("${systems}")
    private List<Systems> systems;

    @Autowired
    @Qualifier("yandexHttpRequestService")
    private HttpRequestService yandexHttpRequestService;

    @Autowired
    @Qualifier("googleHttpRequestService")
    private HttpRequestService googleHttpRequestService;

    @Autowired
    @Qualifier("skyHookHttpRequestService")
    private HttpRequestService skyHookHttpRequestService;

    @Autowired
    @Qualifier("combainHttpRequestService")
    private HttpRequestService combainHttpRequestService;

    @Autowired
    @Qualifier("mozillaLocationHttpReuqest")
    private HttpRequestService mozillaHttpRequestService;

    @Autowired
    private ResponseResolver responseResolver;

    @Cacheable(value = "isharipovCache", key = "#params")
    public Response process(Map<String, String> params) {

        long start = System.currentTimeMillis();
        Future<CommonRs> yandexCommonRs = null;
        if (systems.contains(Systems.YANDEX)) {
            yandexCommonRs = yandexHttpRequestService.createHttpRequest(params);
        }
        Future<CommonRs> googleCommonRs = null;
        if (systems.contains(Systems.GOOGLE)) {
            googleCommonRs = googleHttpRequestService.createHttpRequest(params);
        }

        Future<CommonRs> skyHookCommonRs = null;
        if (params.get("bssid") != null && systems.contains(Systems.SKYHOOK)) {
            skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(params);
        }
        Future<CommonRs> mozillaCommonRs = null;
        if (systems.contains(Systems.MOZILLA)) {
            mozillaCommonRs = mozillaHttpRequestService.createHttpRequest(params);
        }

        Map<String, CommonRs> commons = new HashMap<>();
        try {
            while (!((yandexCommonRs != null && yandexCommonRs.isDone()) && (googleCommonRs != null && googleCommonRs.isDone())
                    && (mozillaCommonRs != null && mozillaCommonRs.isDone())
            )) {
                Thread.sleep(5);
            }
            log.info("{}", "Elapsed time: " + (System.currentTimeMillis() - start));
            commons.put("yandexCommonRs", yandexCommonRs.get());
            commons.put("googleCommonRs", googleCommonRs.get());
            if (skyHookCommonRs != null) {
                commons.put("skyHookCommonRs", skyHookCommonRs.get());
            }
            commons.put("mozillaCommonRs", mozillaCommonRs.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return responseResolver.resolve(commons);
    }
}

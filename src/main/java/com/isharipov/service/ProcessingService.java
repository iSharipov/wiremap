package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 22.04.2016.
 */
@Slf4j
@Service
public class ProcessingService {

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
        Future<CommonRs> yandexCommonRs = yandexHttpRequestService.createHttpRequest(params);
        Future<CommonRs> googleCommonRs = googleHttpRequestService.createHttpRequest(params);
        Future<CommonRs> skyHookCommonRs = null;
        if (params.get("bssid") != null) {
            skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(params);
        }
        Future<CommonRs> mozillaCommonRs = mozillaHttpRequestService.createHttpRequest(params);

        Map<String, CommonRs> commons = new HashMap<>();
        try {
            while (!(yandexCommonRs.isDone() && googleCommonRs.isDone()
                    && mozillaCommonRs.isDone()
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return responseResolver.resolve(commons);
    }
}

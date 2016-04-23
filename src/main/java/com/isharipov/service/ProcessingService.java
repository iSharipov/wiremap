package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import com.isharipov.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 22.04.2016.
 */
@Service
public class ProcessingService {

    private final Logger log = LoggerFactory.getLogger(getClass());
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
    private ResponseResolver responseResolver;

    public Response process(Map<String, String> params) {
        for (String mac : StringUtils.macList()) {
            Map<String, String> maps = new HashMap<>();
            maps.put("bssid", StringUtils.replaceSpecialsSymbolsAndUpperCase(mac));
            maps.put("sstrw", "");
            maps.put("agew", "");
            Future<CommonRs> yandexCommonRs = yandexHttpRequestService.createHttpRequest(maps);
            Future<CommonRs> googleCommonRs = googleHttpRequestService.createHttpRequest(maps);
            Future<CommonRs> skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(maps);

            List<CommonRs> commons = new ArrayList<>();
            try {
                commons.add(yandexCommonRs.get());
                commons.add(googleCommonRs.get());
                commons.add(skyHookCommonRs.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Future<CommonRs> yandexCommonRs = yandexHttpRequestService.createHttpRequest(params);
        Future<CommonRs> googleCommonRs = googleHttpRequestService.createHttpRequest(params);
        Future<CommonRs> skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(params);

        List<CommonRs> commons = new ArrayList<>();
        try {
            commons.add(yandexCommonRs.get());
            commons.add(googleCommonRs.get());
            commons.add(skyHookCommonRs.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return responseResolver.resolve(commons);
    }
}

package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 22.04.2016.
 */
@Service
public class ProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ProcessingService.class);
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

    public Response process(Map<String, String> params) {
//        for (String mac : StringUtils.macList()) {
//            Map<String, String> maps = new HashMap<>();
//            maps.put("bssid", mac);
//            maps.put("signal", params.get("sstrw"));
//            maps.put("age", params.get("agew"));
//            Future<CommonRs> yandexCommonRs = yandexHttpRequestService.createHttpRequest(maps);
//            Future<CommonRs> googleCommonRs = googleHttpRequestService.createHttpRequest(maps);
//            Future<CommonRs> skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(maps);
//            Future<CommonRs> combainCommonRs = combainHttpRequestService.createHttpRequest(maps);
//            Future<CommonRs> mozillaCommonRs = mozillaHttpRequestService.createHttpRequest(maps);
//
//            Map<String, CommonRs> commons = new HashMap<>();
//            try {
//                while (!(yandexCommonRs.isDone() && googleCommonRs.isDone()
//                        && skyHookCommonRs.isDone() && combainCommonRs.isDone()
//                        && mozillaCommonRs.isDone()
//                )) {
//                    Thread.sleep(20);
//                }
//                commons.put("yandexCommonRs", yandexCommonRs.get());
//                commons.put("googleCommonRs", googleCommonRs.get());
//                commons.put("skyhookCommonRs", skyHookCommonRs.get());
//                commons.put("mozillaCommonRs", mozillaCommonRs.get());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
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

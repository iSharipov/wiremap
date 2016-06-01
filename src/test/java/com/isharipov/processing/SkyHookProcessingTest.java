package com.isharipov.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.config.spring.AppConfig;
import com.isharipov.config.spring.WebConfig;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import com.isharipov.repository.ResponseRepsitory;
import com.isharipov.service.HttpRequestService;
import com.isharipov.service.ResponseResolver;
import com.isharipov.utils.Pair;
import com.isharipov.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private ResponseResolver responseResolver;

    @Autowired
    private ResponseRepsitory responseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void coverageTest() throws ExecutionException, InterruptedException {

        Pair<List<String>, List<String>> pair = StringUtils.macList("586");
        List<String> macList = pair.getMacList();
        List<String> levelList = pair.getLevelListl();
        for (int i = 2; i < macList.size(); i += 3) {
            Map<String, List<String>> params = new HashMap<>();
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            list1.add(macList.get(i));
            list1.add(macList.get(i - 1));
            list1.add(macList.get(i - 2));
            list2.add(levelList.get(i));
            list2.add(levelList.get(i - 1));
            list2.add(levelList.get(i - 2));
            List<String> s = StringUtils.replaceSpecialsSymbolsAndUpperCase(list1);
            params.put("bssid", s);
            params.put("ssw", list2);
            long start = System.currentTimeMillis();
            Future<CommonRs> yandexCommonRs = yandexHttpRequestService.createHttpRequest(params);
            Future<CommonRs> googleCommonRs = googleHttpRequestService.createHttpRequest(params);
            Future<CommonRs> skyHookCommonRs = skyHookHttpRequestService.createHttpRequest(params);
            Future<CommonRs> mozillaCommonRs = mozillaLocationHttpRequest.createHttpRequest(params);

            Map<String, CommonRs> commons = new HashMap<>();

            while (!(yandexCommonRs.isDone() && googleCommonRs.isDone()
                    && skyHookCommonRs.isDone()
                    && mozillaCommonRs.isDone()
            )) {
                Thread.sleep(10);
            }
            long elapsedTime = System.currentTimeMillis() - start;
            commons.put("yandexCommonRs", yandexCommonRs.get());
            commons.put("googleCommonRs", googleCommonRs.get());
            commons.put("skyHookCommonRs", skyHookCommonRs.get());
            commons.put("mozillaCommonRs", mozillaCommonRs.get());
            String paramsString = null;
            try {
                paramsString = objectMapper.writeValueAsString(params);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            responseResolver.resolve(commons, paramsString, elapsedTime);
        }
    }

    @Test
    public void showResponsesByProviderName() {
        List<Response> responseList = responseRepository.findResponsesByProviderLike("skyhook");
        log.info("{}", responseList);
    }
}
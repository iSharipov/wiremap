package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import com.isharipov.repository.ResponseRepsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Илья on 22.04.2016.
 */
@Slf4j
@Service
public class ResponseResolver {

    private static final String ENABLE_TO_DETERMINE_LOCATION = "enable to determine location";

    @Autowired
    private ResponseRepsitory responseRepsitory;

    public Response resolve(Map<String, CommonRs> commons, String params) {
        Response response = new Response();
        response.setAccuracy(Float.MAX_VALUE);
        CommonRs googleCommonRs = commons.get("googleCommonRs");
        if (googleCommonRs != null) {
            if (googleCommonRs.getErrorResponse() != null || googleCommonRs.getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                response.setLat(googleCommonRs.getLocation().getLat());
                response.setLng(googleCommonRs.getLocation().getLng());
                response.setAccuracy(googleCommonRs.getAccuracy());
                response.setProvider("google");
                response.setParams(params);
                responseRepsitory.save(response);
            }
        }

        CommonRs mozillaCommonRs = commons.get("mozillaCommonRs");
        if (mozillaCommonRs != null) {
            if (mozillaCommonRs.getErrorResponse() != null || mozillaCommonRs.getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                Response statiscticResponse = new Response();
                statiscticResponse.setLat(mozillaCommonRs.getLocation().getLat());
                statiscticResponse.setLng(mozillaCommonRs.getLocation().getLng());
                statiscticResponse.setAccuracy(mozillaCommonRs.getAccuracy());
                statiscticResponse.setProvider("mozilla");
                statiscticResponse.setParams(params);
                responseRepsitory.save(statiscticResponse);
                if ((response.getLng() != null) && (response.getAccuracy() > mozillaCommonRs.getAccuracy())) {
                    response.setLat(mozillaCommonRs.getLocation().getLat());
                    response.setLng(mozillaCommonRs.getLocation().getLng());
                    response.setAccuracy(mozillaCommonRs.getAccuracy());
                    response.setProvider("mozilla");
                } else if (response.getLng() == null) {
                    response.setLat(mozillaCommonRs.getLocation().getLat());
                    response.setLng(mozillaCommonRs.getLocation().getLng());
                    response.setAccuracy(mozillaCommonRs.getAccuracy());
                    response.setProvider("mozilla");
                }
            }
        }

        CommonRs skyHookCommonRs = commons.get("skyHookCommonRs");
        if (skyHookCommonRs != null) {
            if (skyHookCommonRs.getErrorResponse() != null || skyHookCommonRs.getLocationRs().getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                Response statiscticResponse = new Response();
                statiscticResponse.setLat(skyHookCommonRs.getLocationRs().getLocation().getLatitude());
                statiscticResponse.setLng(skyHookCommonRs.getLocationRs().getLocation().getLongitude());
                statiscticResponse.setAccuracy(skyHookCommonRs.getLocationRs().getLocation().getHpe());
                statiscticResponse.setProvider("skyhook");
                statiscticResponse.setParams(params);
                responseRepsitory.save(statiscticResponse);
                if (response.getLng() == null) {
                    response.setLat(skyHookCommonRs.getLocationRs().getLocation().getLatitude());
                    response.setLng(skyHookCommonRs.getLocationRs().getLocation().getLongitude());
                    response.setAccuracy(skyHookCommonRs.getLocationRs().getLocation().getHpe());
                    response.setProvider("skyhook");
                } else if (response.getLng() != null && response.getAccuracy() > skyHookCommonRs.getLocationRs().getLocation().getHpe()) {
                    response.setLat(skyHookCommonRs.getLocationRs().getLocation().getLatitude());
                    response.setLng(skyHookCommonRs.getLocationRs().getLocation().getLongitude());
                    response.setAccuracy(skyHookCommonRs.getLocationRs().getLocation().getHpe());
                    response.setProvider("skyhook");
                } else {
                    log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
                }
            }
        }

        CommonRs yandexCommonRs = commons.get("yandexCommonRs");
        if (yandexCommonRs != null) {
            if (yandexCommonRs.getErrorResponse() != null || yandexCommonRs.getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                if (yandexCommonRs.getPosition().getType().equals("ip")) {
                    log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
                } else {
                    Response statisticResponse = new Response();
                    statisticResponse.setLat(yandexCommonRs.getPosition().getLatitude());
                    statisticResponse.setLng(yandexCommonRs.getPosition().getLongitude());
                    statisticResponse.setAccuracy(yandexCommonRs.getPosition().getPrecision());
                    statisticResponse.setProvider("yandex");
                    statisticResponse.setParams(params);
                    responseRepsitory.save(statisticResponse);
                    if (response.getLng() == null) {
                        response.setLng(yandexCommonRs.getPosition().getLongitude());
                        response.setLat(yandexCommonRs.getPosition().getLatitude());
                        response.setAccuracy(yandexCommonRs.getPosition().getPrecision());
                        response.setProvider("yandex");
                    } else if (response.getLng() != null && response.getAccuracy() > yandexCommonRs.getPosition().getPrecision()) {
                        response.setLng(yandexCommonRs.getPosition().getLongitude());
                        response.setLat(yandexCommonRs.getPosition().getLatitude());
                        response.setAccuracy(yandexCommonRs.getPosition().getPrecision());
                        response.setProvider("yandex");
                    } else {
                        log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
                    }
                }
            }
        }
        if (response.getLng() == null) {
            response.setAccuracy(null);
            response.setError(ENABLE_TO_DETERMINE_LOCATION);
        }
        return response;
    }
}
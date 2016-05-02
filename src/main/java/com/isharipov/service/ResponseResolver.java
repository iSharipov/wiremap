package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Илья on 22.04.2016.
 */
@Service
public class ResponseResolver {
    private static final Logger log = LoggerFactory.getLogger(ResponseResolver.class);

    private static final String ENABLE_TO_DETERMINE_LOCATION = "enable to determine location";

    public Response resolve(Map<String, CommonRs> commons) {
        Response response = new Response();
        CommonRs googleCommonRs = commons.get("googleCommonRs");
        if (googleCommonRs != null) {
            if (googleCommonRs.getErrorResponse() != null || googleCommonRs.getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                response.setLat(googleCommonRs.getLocation().getLat());
                response.setLon(googleCommonRs.getLocation().getLng());
                response.setAccuracy(googleCommonRs.getAccuracy());
                response.setProvider("google");
            }
        }

        CommonRs mozillaCommonRs = commons.get("mozillaCommonRs");
        if (mozillaCommonRs != null) {
            if (mozillaCommonRs.getErrorResponse() != null || mozillaCommonRs.getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                if ((response.getLon() != null) && (response.getAccuracy() > mozillaCommonRs.getAccuracy())) {
                    response.setLat(mozillaCommonRs.getLocation().getLat());
                    response.setLon(mozillaCommonRs.getLocation().getLng());
                    response.setProvider("mozilla");
                } else if (response.getLon() == null) {
                    response.setLat(mozillaCommonRs.getLocation().getLat());
                    response.setLon(mozillaCommonRs.getLocation().getLng());
                    response.setProvider("mozilla");
                }
            }
        }

        CommonRs skyHookCommonRs = commons.get("skyHookCommonRs");
        if (skyHookCommonRs != null) {
            if (skyHookCommonRs.getErrorResponse() != null || skyHookCommonRs.getLocationRs().getError() != null) {
                log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
            } else {
                response.setLat(skyHookCommonRs.getLocationRs().getLocation().getLatitude());
                response.setLon(skyHookCommonRs.getLocationRs().getLocation().getLongitude());
                response.setAccuracy(0f);
                response.setProvider("skyhook");
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
                    if (response.getLon() == null) {
                        response.setLon(yandexCommonRs.getPosition().getLongitude());
                        response.setLat(yandexCommonRs.getPosition().getLatitude());
                        response.setProvider("yandex");
                    } else if (response.getLon() != null && response.getAccuracy() > yandexCommonRs.getPosition().getPrecision()) {
                        response.setLon(yandexCommonRs.getPosition().getLongitude());
                        response.setLat(yandexCommonRs.getPosition().getLatitude());
                        response.setProvider("yandex");
                    } else {
                        log.info("{}", ENABLE_TO_DETERMINE_LOCATION);
                    }
                }
            }
        }
        if (response.getLon() == null) {
            response.setError(ENABLE_TO_DETERMINE_LOCATION);
        }
        return response;
    }
}

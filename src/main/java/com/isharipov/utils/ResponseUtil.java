package com.isharipov.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.error.ErrorResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by Илья on 24.04.2016.
 */
public class ResponseUtil {

    public static Future<CommonRs> handleError(ResponseEntity<String> responseEntity, String responseBody, ObjectMapper objectMapper, Logger log) {
        try {
            if (RestUtil.isError(responseEntity.getStatusCode())) {
                if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                    log.info("code: {}", HttpStatus.UNAUTHORIZED);
                } else {
                    ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                    log.info("code: {}, message: {}", errorResponse.getError().getCode(), errorResponse.getError().getMessage());
                    CommonRs commonRs = objectMapper.readValue(responseBody, CommonRs.class);
                    return new AsyncResult<>(commonRs);
                }
            } else {
                CommonRs commonRs = objectMapper.readValue(responseBody, CommonRs.class);
                log.info("response: {}", commonRs);
                return new AsyncResult<>(commonRs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

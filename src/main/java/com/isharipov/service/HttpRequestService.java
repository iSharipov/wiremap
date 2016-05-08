package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Илья on 20.04.2016.
 */
public interface HttpRequestService {
    Future<CommonRs> createHttpRequest(Map<String, List<String>> params);
}

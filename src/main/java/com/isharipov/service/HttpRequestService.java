package com.isharipov.service;

import org.apache.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Илья on 20.04.2016.
 */
public interface HttpRequestService {
    HttpRequest createHttpRequest(Map<String, String> params);
}

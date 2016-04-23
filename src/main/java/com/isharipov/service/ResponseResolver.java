package com.isharipov.service;

import com.isharipov.domain.common.CommonRs;
import com.isharipov.domain.common.Response;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Илья on 22.04.2016.
 */
@Service
public class ResponseResolver {
    public Response resolve(List<CommonRs> commons){
        Response response = new Response();
        response.setName("Hello");
        response.setSirname("From Controller");
        return response;
    }
}

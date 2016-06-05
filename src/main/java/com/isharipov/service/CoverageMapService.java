package com.isharipov.service;

import com.isharipov.domain.common.Response;
import com.isharipov.repository.ResponseRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Илья on 05.06.2016.
 */
@Service
public class CoverageMapService {
    @Autowired
    private ResponseRepsitory responseRepsitory;

    public List<Response> getAllResponses() {
        return responseRepsitory.findAll();
    }
}

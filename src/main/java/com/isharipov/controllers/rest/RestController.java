package com.isharipov.controllers.rest;

import com.isharipov.domain.common.Response;
import com.isharipov.service.ProcessingService;
import com.isharipov.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Илья on 22.04.2016.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private ProcessingService processingService;

    @RequestMapping(method = RequestMethod.GET, value = "/mac", produces = "application/json")
    public Response processRequestMac(@RequestParam(value = "bssid", required = true) List<String> bssid,
                                      @RequestParam(value = "signal", required = false) List<String> signal,
                                      @RequestParam(value = "age", required = false) List<String> age
    ) {
        bssid = StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid);
        for (String mac : bssid) {
            if (mac.length() < 12) {
                Response response = new Response();
                response.setError("bad query");
                return response;
            }
        }

        final Map<String, List<String>> map = new HashMap<>();
        map.put("bssid", bssid);
        map.put("signal", signal);
        map.put("age", age);

        return processingService.process(map);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cell", produces = "application/json")
    public Response processRequestSsid(@RequestParam(value = "mcc", required = true) List<String> mcc,
                                       @RequestParam(value = "mnc", required = true) List<String> mnc,
                                       @RequestParam(value = "lac", required = true) List<String> lac,
                                       @RequestParam(value = "cid", required = true) List<String> cid
    ) {
        final Map<String, List<String>> map = new HashMap<>();
        map.put("mcc", mcc);
        map.put("mnc", mnc);
        map.put("lac", lac);
        map.put("cid", cid);

        return processingService.process(map);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = "application/json")
    public Response processRequestCommon(
            @RequestParam(value = "mcc", required = true) List<String> mcc,
            @RequestParam(value = "mnc", required = true) List<String> mnc,
            @RequestParam(value = "lac", required = true) List<String> lac,
            @RequestParam(value = "lac", required = true) List<String> cid,
            @RequestParam(value = "bssid", required = true) List<String> bssid,
            @RequestParam(value = "signal", required = false) List<String> signal,
            @RequestParam(value = "age", required = false) List<String> age

    ) {
        final Map<String, List<String>> map = new HashMap<>();
        bssid = StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid);
        for (String mac : bssid) {
            if (mac.length() < 12) {
                Response response = new Response();
                response.setError("bad query");
                return response;
            }
        }
        map.put("mcc", mcc);
        map.put("mnc", mnc);
        map.put("lac", lac);
        map.put("cid", cid);
        map.put("bssid", bssid);
        map.put("signal", signal);
        map.put("age", age);

        return processingService.process(map);
    }
}

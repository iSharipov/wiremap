package com.isharipov.controllers.rest;

import com.isharipov.domain.common.Response;
import com.isharipov.service.ProcessingService;
import com.isharipov.service.ValidationService;
import com.isharipov.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@ManagedResource(objectName = "RestController:name=RestControllerBean")
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private ProcessingService processingService;

    @Autowired
    private ValidationService validationService;

    @ManagedOperation(description = "Returns location by mac")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "bssid", description = "BSSID"),
            @ManagedOperationParameter(name = "ssw", description = "Signal Strength"),
            @ManagedOperationParameter(name = "age", description = "Age")
    })

    @RequestMapping(method = RequestMethod.GET, value = "/mac", produces = "application/json")
    public Response processRequestMac(@RequestParam(value = "bssid", required = true) List<String> bssid,
                                      @RequestParam(value = "ssw", required = false) List<String> ssw,
                                      @RequestParam(value = "age", required = false) List<String> age
    ) {

        bssid = validationService.replaceSpecialSymbolsMacAndUpperCase(validationService.emptyList(bssid));
        ssw = validationService.emptyList(ssw);
        age = validationService.emptyList(age);
        if (bssid != null) {
            for (String mac : bssid) {
                if (mac.length() < 12) {
                    Response response = new Response();
                    response.setError("bad query");
                    return response;
                }
            }
        }


        final Map<String, List<String>> map = new HashMap<>();
        map.put("bssid", bssid);
        map.put("ssw", ssw);
        map.put("age", age);

        return processingService.process(map);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cell", produces = "application/json")
    public Response processRequestSsid(@RequestParam(value = "mcc", required = true) List<String> mcc,
                                       @RequestParam(value = "mnc", required = true) List<String> mnc,
                                       @RequestParam(value = "lac", required = true) List<String> lac,
                                       @RequestParam(value = "cid", required = true) List<String> cid,
                                       @RequestParam(value = "ssc", required = false) List<String> ssc
    ) {
        final Map<String, List<String>> map = new HashMap<>();
        map.put("mcc", mcc);
        map.put("mnc", mnc);
        map.put("lac", lac);
        map.put("cid", cid);
        map.put("ssc", ssc);

        return processingService.process(map);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = "application/json")
    public Response processRequestCommon(
            @RequestParam(value = "mcc", required = true) List<String> mcc,
            @RequestParam(value = "mnc", required = true) List<String> mnc,
            @RequestParam(value = "lac", required = true) List<String> lac,
            @RequestParam(value = "lac", required = true) List<String> cid,
            @RequestParam(value = "ssc", required = false) List<String> ssc,
            @RequestParam(value = "bssid", required = true) List<String> bssid,
            @RequestParam(value = "ssw", required = false) List<String> ssw,
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
        map.put("ssc", ssc);
        map.put("bssid", bssid);
        map.put("age", age);

        return processingService.process(map);
    }
}

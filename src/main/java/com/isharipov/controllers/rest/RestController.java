package com.isharipov.controllers.rest;

import com.isharipov.domain.common.Response;
import com.isharipov.service.ProcessingService;
import com.isharipov.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Илья on 22.04.2016.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private ProcessingService processingService;

    @RequestMapping(method = RequestMethod.GET, value = "/mac")
    public Response processRequestMac(@RequestParam(value = "bssid", required = true) String bssid,
                                      @RequestParam(value = "sstrw", required = false) String sstrw,
                                      @RequestParam(value = "agew", required = false) String age
    ) {
        Map<String, String> map = new HashMap<>();
        map.put("bssid", StringUtils.replaceSpecialsSymbolsAndUpperCase(bssid));
        map.put("sstrw", sstrw);
        map.put("agew", age);

        return processingService.process(map);

    }
}

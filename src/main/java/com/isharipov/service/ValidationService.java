package com.isharipov.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Илья on 12.05.2016.
 */
@Service
public class ValidationService {
    public List<String> emptyList(List<String> params) {
        if (params != null) {
            for (Iterator<String> inputParamsIter = params.iterator(); inputParamsIter.hasNext(); ) {
                String s = inputParamsIter.next();
                if (s.equals("")) {
                    inputParamsIter.remove();
                }
            }
            if (!params.isEmpty()) {
                return params;
            }
        }
        return null;
    }

    public List<String> replaceSpecialSymbolsMacAndUpperCase(List<String> params) {
        if (params != null) {
            List<String> temp = new ArrayList<>(params.size());
            for (String param : params) {
                temp.add(param.replaceAll("[^a-zA-Z0-9]+", "").toUpperCase());
            }
            return temp;
        }
        return null;
    }
}

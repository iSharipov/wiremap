package com.isharipov.utils;

import org.springframework.http.HttpStatus;

/**
 * Created by Илья on 23.04.2016.
 */
public class RestUtil {
    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }
}

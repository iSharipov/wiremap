package com.isharipov.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.RecoverableDataAccessException;

public class DataAccessExceptionAdapter {
    public DataAccessExceptionAdapter() {
    }

    public static boolean isRecoverable(DataAccessException dae) {
        return dae instanceof DataAccessResourceFailureException || dae instanceof RecoverableDataAccessException;
    }
}
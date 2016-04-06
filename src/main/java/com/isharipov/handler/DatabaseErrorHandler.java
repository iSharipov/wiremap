package com.isharipov.handler;

import com.isharipov.utils.DataAccessExceptionAdapter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Aspect
public class DatabaseErrorHandler implements Ordered {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String txManagerId;
    private long reconnectionTimeout;

    private int maxTriesCount;

    /**
     * Intercept all @Transactional methods
     *
     * @param joinPoint
     * @param transactional
     * @return
     * @throws Throwable
     */
    @Around("@annotation(transactional)")
    public Object transactionalmethod(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        return executeWithRetry(joinPoint, transactional);
    }

    /**
     * Intercept methods for classes which implement Spring Data JPA repositories
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(public * org.springframework.data.repository.Repository+.*(..))")
    public Object repositoryMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean hasClassAnnotation = false;
        Transactional transactional = null;

        for (Class<?> i : joinPoint.getTarget().getClass().getInterfaces()) {
            if ((transactional = i.getAnnotation(Transactional.class)) != null) {
                hasClassAnnotation = true;
                break;
            }
        }

        if (hasClassAnnotation) {
            return executeWithRetry(joinPoint, transactional);
        } else {
            return joinPoint.proceed();
        }
    }

    /**
     * Retries database operation with certain timeout if operation fails
     */
    public Object executeWithRetry(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        if (!txManagerId.equals(transactional.value())) {
// retry only specified transaction manager's methods invocations
            return joinPoint.proceed();
        }

        final boolean infiniteRetry = (maxTriesCount == 0);
        int numAttempts = 0;
        boolean stopRetry = false;
        Throwable databaseException;

        do {
            numAttempts++;

            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                Throwable t = (e instanceof TransactionSystemException) ?
                        ((TransactionSystemException) e).getOriginalException() : e;

                boolean isRecoverable;

                if (t instanceof DataAccessException) {
                    isRecoverable = DataAccessExceptionAdapter.isRecoverable((DataAccessException) t);
                } else {
                    isRecoverable = t instanceof TransactionException;
                }

                if (isRecoverable) {
                    String msg = "Recoverable error during database operation";

                    if (maxTriesCount != 0) {
                        msg = String.format("%s. Try #%d. Retrying in %d ms", msg, numAttempts, reconnectionTimeout);
                    }

                    log.warn(msg, e);

                    try {
                        TimeUnit.MILLISECONDS.sleep(reconnectionTimeout);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                        stopRetry = true;
                    }
                } else {
                    log.debug("Unrecoverable error during database operation", e);
                    stopRetry = true;
                }

                databaseException = e;
            }
        } while ((infiniteRetry || numAttempts < maxTriesCount) && !stopRetry);

        throw databaseException;
    }

    @Override
    public int getOrder() {
        return 1;
    }


    public void setReconnectionTimeout(long reconnectionTimeout) {
        this.reconnectionTimeout = reconnectionTimeout;
    }

    public void setMaxTriesCount(int maxTriesCount) {
        this.maxTriesCount = maxTriesCount;
    }

    public void setTxManagerId(String txManagerId) {
        this.txManagerId = txManagerId;
    }
}
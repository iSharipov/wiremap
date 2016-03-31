package com.isharipov.utils;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * Created by Илья on 31.03.2016.
 */
public class MySQL5BooleanDialect extends MySQL5Dialect {
    public MySQL5BooleanDialect() {
        super();
        registerColumnType(java.sql.Types.BOOLEAN, "bit");
    }
}

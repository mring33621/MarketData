
package com.mattring.marketdata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Matthew
 */
public class DbAware {

    protected final static JdbcTemplate JDBC_TEMPLATE;

    static {
        final int cacheSizeKb = Integer.getInteger("cacheSizeKb", 10000); // 10MB default
        final String dbUrl = String.format("jdbc:h2:file:C:/Data/marketdata/db;CACHE_SIZE=%d", cacheSizeKb);
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(1); // our H2 only allows 1 connection
        config.setAutoCommit(true); // I'm being lazy

        final HikariDataSource pooledDataSource = new HikariDataSource(config);

        JDBC_TEMPLATE = new JdbcTemplate(pooledDataSource);
    }

    protected final static SymToDBTableFn tblFn = new SymToDBTableFn();

}

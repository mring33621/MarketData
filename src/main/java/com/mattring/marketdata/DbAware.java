
package com.mattring.marketdata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

/**
 * @author Matthew
 */
public class DbAware {

    protected final static Sql2o db;

    static {
        final int cacheSizeKb = Integer.getInteger("cacheSizeKb", 10000); // 10MB default
        final String dbUrl = String.format("jdbc:h2:file:C:/Data/marketdata/db/db;CACHE_SIZE=%d", cacheSizeKb);
        final JdbcDataSource basicDataSource = new JdbcDataSource();
        basicDataSource.setURL(dbUrl);

        final HikariConfig config = new HikariConfig();
        config.setDataSource(basicDataSource);
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(1); // our H2 only allows 1 connection

        final HikariDataSource pooledDataSource = new HikariDataSource(config);

        db = new Sql2o(pooledDataSource); // supposedly thread safe
    }

    protected final static SymToDBTableFn tblFn = new SymToDBTableFn();

}

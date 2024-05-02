package jibs.middleware.db;

import jibs.middleware.MiddlewareException;
import jibs.middleware.config.MiddlewareDBConfig;
import jibs.utils.LazyInitializer;
import io.micronaut.context.annotation.Factory;
import java.sql.SQLException;
import java.util.Properties;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DataSource} factory
 *
 * @author gusto
 */
@Factory
class DBFactory {

    private static final Logger L = LoggerFactory.getLogger(DBFactory.class);
    private final MiddlewareDBConfig middlewareConfig;
    private final LazyInitializer<BasicDataSource, MiddlewareException> middlewareDatasource = new LazyInitializer<BasicDataSource, MiddlewareException>() {
        @Override
        @SuppressWarnings("java:S6212")
        protected BasicDataSource initialize() throws MiddlewareException {
            try {
                L.info("Init Middleware Database ...");
                MiddlewareDBConfig config = middlewareConfig;

                final String dbUrl = String.format("jdbc:postgresql://%s:%d/%s",
                        config.getHost(), config.getPort(), config.getDbName());

                final Properties dsProperties = new Properties();
                dsProperties.setProperty("driverClassName", "org.postgresql.Driver");
                dsProperties.setProperty("url", dbUrl);
                dsProperties.setProperty("username", config.getUser());
                dsProperties.setProperty("password", config.getPassword());

                dsProperties.setProperty("defaultAutoCommit", "true");
                dsProperties.setProperty("defaultTransactionIsolation", "READ_COMMITTED");
                dsProperties.setProperty("maxWaitMillis",String.valueOf(config.getMaxWaitMillis()));

                dsProperties.setProperty("initialSize", String.valueOf(config.getMinIdle()));
                dsProperties.setProperty("maxTotal", String.valueOf(config.getMaxTotal()));
                dsProperties.setProperty("maxIdle", String.valueOf(config.getMaxIdle()));
                dsProperties.setProperty("minIdle", String.valueOf(config.getMinIdle()));

                dsProperties.setProperty("validationQuery", "SELECT 1");
                dsProperties.setProperty("poolPreparedStatements", "true");

                return BasicDataSourceFactory.createDataSource(dsProperties);
            } catch (Exception ex) {
                throw new MiddlewareException(ex);
            }
        }

        @Override
        protected void destroy(BasicDataSource o) throws MiddlewareException {
            try {
                L.info("Closing Middleware datasource ...");
                o.close();
            } catch (SQLException ex) {
                throw new MiddlewareException(ex);
            }
        }
    };

    @Inject
    DBFactory(MiddlewareDBConfig middlewareConfig) {
        this.middlewareConfig = middlewareConfig;
    }


    @Singleton
    @MiddlewareDB
    DataSource provideMiddlewareDatasource() throws jibs.Exception {
        return middlewareDatasource.get();
    }

    @PreDestroy
    void destroy() throws jibs.Exception {
        L.info("Destroy DB factory");
        try {
            middlewareDatasource.close();
        } catch (jibs.Exception e) {
            L.warn("Unable to close FDW datasource", e);
        }

    }
}

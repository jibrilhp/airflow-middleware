package jibs.middleware.db;

import jibs.utils.JooqUtils;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jooq.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gusto
 */
@Factory
class JooqFactory {

    private static final Logger L = LoggerFactory.getLogger(JooqFactory.class);

    @Inject
    @MiddlewareDB
    private Provider<DataSource> dataSource;

    @Prototype
    Configuration provideJooqConfiguration() throws SQLException {
        L.debug("New JooQ configuration instance");
        return JooqUtils.configure(dataSource.get());
    }
}

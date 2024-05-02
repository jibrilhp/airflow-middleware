package jibs.middleware;


import jibs.middleware.db.MiddlewareDB;
import jibs.middleware.deployment.dao.MiddlewareDao;
import jibs.airflow.middlewaredb.tables.pojos.Middleware;
import jibs.utils.LazyInitializer;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gusto
 */
@Singleton
public class MiddlewareHelper {

    private static final Logger L = LoggerFactory.getLogger(MiddlewareHelper.class);

    @MiddlewareDB
    @Inject
    private Provider<DataSource> middlewareDatasourceProvider;


    private final LazyInitializer<MiddlewareDao, MiddlewareException> middlewareDaoInitializer = new LazyInitializer<MiddlewareDao, MiddlewareException>() {
        @Override
        protected MiddlewareDao initialize() throws MiddlewareException {
            try {
                return new MiddlewareDao(middlewareDatasourceProvider.get());
            } catch (SQLException ex) {
                throw new MiddlewareException("Unable to create Middleware dao instance", ex);
            }
        }

        @Override
        protected void destroy(MiddlewareDao o) throws MiddlewareException {
            // Do nothing, no need to worry about
        }
    };

    @SuppressWarnings({"java:S6212", "java:S6213"})
    private Middleware newMiddlewareRecord(OffsetDateTime nowTs) {
        Middleware record = new Middleware();

        record.setTriggerId(1);
        record.setTriggerName("Sips");
        record.setTriggerSourceDag("test1");
        record.setTriggerTargetDag("test2");
        record.setTriggerDescription("Hey");
        record.setDatahubUpdatedAt(nowTs);
        record.setCreatedAt(nowTs);
        record.setUpdatedAt(nowTs);

        return record;
    }

    @SuppressWarnings({"java:S6212", "java:S6213"})
    public void updateDAGInformation(String dagName) throws MiddlewareException {

        MiddlewareDao fdwDao = middlewareDaoInitializer.get();

        Middleware record = newMiddlewareRecord(OffsetDateTime.now());
        Arrays.asList(record);
        int sz = fdwDao.upsertMiddlewareInfo(Arrays.asList(record));
        L.info("{} OK", sz);


    }

}

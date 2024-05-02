package jibs.middleware.deployment.dao;

import static jibs.airflow.middlewaredb.Tables.MIDDLEWARE;
import jibs.airflow.middlewaredb.tables.pojos.Middleware;
import jibs.airflow.middlewaredb.tables.records.MiddlewareRecord;
import jibs.middleware.deployment.dao.Param.Mapper;
import jibs.utils.JooqUtils;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import javax.sql.DataSource;

import org.jooq.*;

import static org.jooq.impl.DSL.using;
import static jibs.middleware.deployment.dao.Param.newParam;


/**
 *
 * @author gusto
 */
@SuppressWarnings("java:S2176")
public class MiddlewareDao extends jibs.airflow.middlewaredb.tables.daos.MiddlewareDao {

    private static final Param<Integer> TRIGGER_ID = newParam(MIDDLEWARE.TRIGGER_ID);
    private static final Param<String> TRIGGER_NAME = newParam(MIDDLEWARE.TRIGGER_NAME);

    private static final Param<String> TRIGGER_SOURCE_DAG = newParam(MIDDLEWARE.TRIGGER_SOURCE_DAG);

    private static final Param<String> TRIGGER_TARGET_DAG = newParam(MIDDLEWARE.TRIGGER_TARGET_DAG);

    private static final Param<String> TRIGGER_DESCRIPTION = newParam(MIDDLEWARE.TRIGGER_DESCRIPTION);

    private static final Param<OffsetDateTime> DATAHUB_UPDATED_AT = newParam(MIDDLEWARE.DATAHUB_UPDATED_AT);

    private static final Param<OffsetDateTime> LAST_TRIGGER_AT = newParam(MIDDLEWARE.LAST_TRIGGER_AT);
    private static final Param<OffsetDateTime> CREATED_AT = newParam(MIDDLEWARE.CREATED_AT);

    private static final Param<OffsetDateTime> UPDATED_AT = newParam(MIDDLEWARE.UPDATED_AT);

    private static final Mapper<Middleware> MAPPER = new Mapper<Middleware>() {
        @Override
        protected void bind(Map<String, Object> m, Middleware o) {
            TRIGGER_ID.bind(m, o.getTriggerId());
            TRIGGER_NAME.bind(m, o.getTriggerName());
            TRIGGER_SOURCE_DAG.bind(m, o.getTriggerSourceDag());
            TRIGGER_TARGET_DAG.bind(m, o.getTriggerTargetDag());
            TRIGGER_DESCRIPTION.bind(m, o.getTriggerDescription());
            DATAHUB_UPDATED_AT.bind(m, o.getDatahubUpdatedAt());
            LAST_TRIGGER_AT.bind(m, o.getLastTriggerAt());
            CREATED_AT.bind(m, o.getCreatedAt());
            UPDATED_AT.bind(m, o.getUpdatedAt());
        }
    };

    public MiddlewareDao(DataSource dataSource) throws SQLException {
        this(JooqUtils.configure(dataSource));
    }

    public MiddlewareDao(Configuration configuration) {
        super(configuration);
    }

    @SuppressWarnings({"java:S6213", "java:S1602"})
    public int upsertMiddlewareInfo(final Collection<Middleware> records) {
        DSLContext ctx = using(configuration());
        InsertOnDuplicateSetMoreStep<MiddlewareRecord> query = ctx.insertInto(MIDDLEWARE)
                .set(MIDDLEWARE.TRIGGER_ID, TRIGGER_ID.get())
                .set(MIDDLEWARE.TRIGGER_NAME, TRIGGER_NAME.get())
                .set(MIDDLEWARE.TRIGGER_SOURCE_DAG, TRIGGER_SOURCE_DAG.get())
                .set(MIDDLEWARE.TRIGGER_TARGET_DAG, TRIGGER_TARGET_DAG.get())
                .set(MIDDLEWARE.TRIGGER_DESCRIPTION, TRIGGER_DESCRIPTION.get())
                .set(MIDDLEWARE.DATAHUB_UPDATED_AT, DATAHUB_UPDATED_AT.get())
                .set(MIDDLEWARE.LAST_TRIGGER_AT, LAST_TRIGGER_AT.get())
                .set(MIDDLEWARE.CREATED_AT, CREATED_AT.get())
                .set(MIDDLEWARE.UPDATED_AT, UPDATED_AT.get())
                .onDuplicateKeyUpdate()
                .set(MIDDLEWARE.TRIGGER_NAME, TRIGGER_NAME.get())
                .set(MIDDLEWARE.TRIGGER_SOURCE_DAG, TRIGGER_SOURCE_DAG.get())
                .set(MIDDLEWARE.TRIGGER_TARGET_DAG, TRIGGER_TARGET_DAG.get())
                .set(MIDDLEWARE.TRIGGER_DESCRIPTION, TRIGGER_DESCRIPTION.get())
                .set(MIDDLEWARE.DATAHUB_UPDATED_AT, DATAHUB_UPDATED_AT.get())
                .set(MIDDLEWARE.LAST_TRIGGER_AT, LAST_TRIGGER_AT.get())
                .set(MIDDLEWARE.CREATED_AT, CREATED_AT.get())
                .set(MIDDLEWARE.UPDATED_AT, UPDATED_AT.get());
        BatchBindStep batch = ctx.batch(query);
        records.forEach((Middleware record) -> {
            MAPPER.bind(batch, record);
        });
        int[] batchResult = batch.execute();
        return SqlBatchUtil.countSuccess(batchResult);
    }


}

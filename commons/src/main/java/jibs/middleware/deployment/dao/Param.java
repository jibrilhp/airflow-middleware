package jibs.middleware.deployment.dao;

import java.util.HashMap;
import java.util.Map;
import org.jooq.BatchBindStep;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 *
 * @author gusto
 */
final class Param<T> {

    private final org.jooq.Param<T> jooqParam;

    private Param(Field<T> field) {
        this.jooqParam = DSL.param(field.getName(), field);
    }

    static final <T> Param<T> newParam(Field<T> field) {
        return new Param<>(field);
    }

    protected final void bind(Map<String, Object> map, T value) {
        map.put(jooqParam.getParamName(), value);
    }

    org.jooq.Param<T> get() {
        return jooqParam;
    }

    static abstract class Mapper<O> {

        final Map<String, Object> bind(O o) {
            Map<String, Object> map = new HashMap<>();
            bind(map, o);
            return map;
        }

        final BatchBindStep bind(BatchBindStep batch, O o) {
            return batch.bind(bind(o));
        }

        protected abstract void bind(Map<String, Object> map, O o);
    }
}

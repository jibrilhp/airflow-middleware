package jibs.utils;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DefaultConfiguration;

/**
 *
 * @author Agus Purnomo (watonist@gmail.com)
 */
public class JooqUtils {

    private JooqUtils() {
    }

    public static Configuration configure(final DataSource ds) throws SQLException {
        final Settings settings = new Settings();
        settings.setStatementType(StatementType.PREPARED_STATEMENT);
        settings.setParamType(ParamType.INDEXED);

        return new DefaultConfiguration()
                .set(settings)
                .set(SQLDialect.POSTGRES)
                .set(ds);
    }
}

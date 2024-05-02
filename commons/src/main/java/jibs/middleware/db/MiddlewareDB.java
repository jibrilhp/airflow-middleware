package jibs.middleware.db;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.inject.Qualifier;

/**
 * FDW Database
 *
 * @author gusto
 */
@Qualifier
@Retention(RUNTIME)
public @interface MiddlewareDB {
}

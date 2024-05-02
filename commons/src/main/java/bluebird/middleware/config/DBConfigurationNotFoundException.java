package jibs.middleware.config;

import jibs.middleware.MiddlewareException;

/**
 *
 * @author gusto
 */
public class DBConfigurationNotFoundException extends MiddlewareException {

    public DBConfigurationNotFoundException() {
    }

    public DBConfigurationNotFoundException(String msg) {
        super(msg);
    }

    public DBConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }

    public DBConfigurationNotFoundException(String format, Object... args) {
        super(format, args);
    }
}

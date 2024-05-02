package jibs.middleware;

/**
 * MiddlewareException base Exception
 *
 * @author gusto
 */
public class MiddlewareException extends jibs.Exception {

    public MiddlewareException() {
        super();
    }

    public MiddlewareException(String msg) {
        super(msg);
    }

    public MiddlewareException(Throwable cause) {
        super(cause);
    }

    public MiddlewareException(String format, Object... args) {
        super(format, args);
    }
}

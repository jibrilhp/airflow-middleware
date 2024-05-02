package jibs.middleware;

/**
 * MiddlewareException base RuntimeException
 * 
 * @author gusto
 */
public class MiddlewareRuntimeException extends jibs.RuntimeException {

    public MiddlewareRuntimeException() {
        super();
    }

    public MiddlewareRuntimeException(String msg) {
        super(msg);
    }

    public MiddlewareRuntimeException(Throwable cause) {
        super(cause);
    }

    public MiddlewareRuntimeException(String format, Object... args) {
        super(format, args);
    }
}

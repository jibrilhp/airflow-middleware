package jibs;

/**
 * Customized base {@link java.lang.RuntimeException}
 *
 * @author gusto
 */
@SuppressWarnings("java:S2176")
public class RuntimeException extends java.lang.RuntimeException {

    public RuntimeException() {
        super();
    }

    public RuntimeException(String msg) {
        super(msg);
    }

    public RuntimeException(Throwable cause) {
        super(cause);
    }

    public RuntimeException(String format, Object... args) {
        super(Util.getMessage(format, args), Util.getCause(args));
    }
}

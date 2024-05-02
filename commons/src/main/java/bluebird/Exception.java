package jibs;

/**
 * Customized base {@link java.lang.Exception}
 *
 * @author gusto
 */
@SuppressWarnings("java:S2176")
public class Exception extends java.lang.Exception {

    public Exception() {
        super();
    }

    public Exception(String msg) {
        super(msg);
    }

    public Exception(Throwable cause) {
        super(cause);
    }

    public Exception(String format, Object... args) {
        super(Util.getMessage(format, args), Util.getCause(args));
    }
}

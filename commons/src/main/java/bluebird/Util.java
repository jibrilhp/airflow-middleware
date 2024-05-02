package jibs;

import java.util.Arrays;
import org.slf4j.helpers.MessageFormatter;

class Util {

    private Util() {
    }

    static final String getMessage(String format, Object... args) {
        int l = args.length;
        if (l <= 0) {
            return format;
        }
        Object o = args[l - 1];
        if (o instanceof Throwable) {
            if (l <= 1) {
                return format;
            }
            return MessageFormatter.arrayFormat(format, Arrays.copyOfRange(
                    args, 0, l - 1)).getMessage();
        }
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

    static final Throwable getCause(Object... args) {
        int l = args.length;
        if (l <= 0) {
            return null;
        }
        Object o = args[l - 1];
        if (o instanceof Throwable) {
            return (Throwable) o;
        }
        return null;
    }
}

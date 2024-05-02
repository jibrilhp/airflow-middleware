package jibs.utils;

/**
 * on demand initializer
 *
 * @author gusto
 * @param <T> object type
 * @param <E> raised Exception
 */
public abstract class LazyInitializer<T, E extends jibs.Exception> {

    private static final Object NO_INIT = new Object();

    @SuppressWarnings({"unchecked", "java:S3077"})
    private volatile T object = (T) NO_INIT;

    public final T get() throws E {
        T result = object;

        if (result == NO_INIT) {
            synchronized (this) {
                result = object;
                if (result == NO_INIT) {
                    object = result = initialize();
                }
            }
        }

        return result;
    }

    public final void close() throws E {
        T result = object;
        if (result == NO_INIT) {
            return;
        }
        synchronized (this) {
            result = object;
            if (result == NO_INIT) {
                return;
            }
            destroy(result);
            object = (T) NO_INIT;
        }
    }

    public final boolean isInitialized() {
        T result = object;
        return result != NO_INIT;
    }

    protected abstract T initialize() throws E;

    protected abstract void destroy(T o) throws E;
}

package jibs.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gusto
 */
public class StringSet {

    private final HashSet<String> base;

    public StringSet(Collection<String> base) {
        this.base = new HashSet<>(base);
    }

    /**
     * Returns new values at inputSet
     *
     * @param inputSet
     * @return
     */
    public Set<String> findNew(Collection<String> inputSet) {
        HashSet<String> result = new HashSet<>();
        result.addAll(inputSet);
        result.removeAll(base);
        return Collections.unmodifiableSet(result);
    }

    /**
     * Returns deleted values from inputSet
     *
     * @param inputSet
     * @return
     */
    public Set<String> findDeleted(Collection<String> inputSet) {
        HashSet<String> result = new HashSet<>();
        result.addAll(base);
        result.removeAll(inputSet);
        return Collections.unmodifiableSet(result);
    }
}

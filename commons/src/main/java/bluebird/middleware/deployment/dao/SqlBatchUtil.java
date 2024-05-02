package jibs.middleware.deployment.dao;

import java.sql.Statement;

/**
 *
 * @author gusto
 */
public class SqlBatchUtil {

    private SqlBatchUtil() {
    }

    public static int countSuccess(final int[] results) {
        int count = 0;
        for (final int result : results) {
            if (result >= 0) {
                count += result;
                continue;
            }
            if (result == Statement.SUCCESS_NO_INFO) {
                count++;
            }
        }
        return count;
    }
}

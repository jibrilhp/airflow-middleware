package jibs.utils;

/**
 *
 * @author gusto
 */
public class CaseUtils {

    private CaseUtils() {
    }

    public static String toSnakeCase(final String str) {
        String result;

        String regex = "([A-Z]+)([A-Z])([a-z])";
        String replacement = "$1_$2$3";
        result = str.replaceAll(regex, replacement);

        regex = "([a-z])([A-Z]+)";
        replacement = "$1_$2";
        result = result.replaceAll(regex, replacement).toLowerCase();

        return result;
    }
}

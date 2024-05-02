package jibs.middleware;

import picocli.CommandLine.IVersionProvider;

/**
 *
 * @author gusto
 */
public class VersionProvider implements IVersionProvider {

    @Override
    public String[] getVersion() throws Exception {
        final Package[] packages = Package.getPackages();
        for (final Package pkg : packages) {
            String pkgName = pkg.getName();
            if (pkgName.equalsIgnoreCase("jibs")) {
                return new String[]{pkg.getImplementationVersion()};
            }
        }
        return new String[]{"0"};
    }
}

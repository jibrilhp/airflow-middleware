package jibs.middleware;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

/**
 *
 * @author gusto
 */
@Command
public class HelperCommand implements Runnable {

    @Spec
    CommandSpec commandSpec;

    @SuppressWarnings("java:S106")
    @Override
    public void run() {
        commandSpec.commandLine().usage(System.err);
    }
}

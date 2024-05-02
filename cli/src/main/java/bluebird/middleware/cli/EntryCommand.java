package jibs.middleware.cli;

import jibs.middleware.VersionProvider;
import jibs.middleware.HelperCommand;
import io.micronaut.configuration.picocli.MicronautFactory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "airflow-middleware", description = "Middleware Generator", aliases = "aim",
        mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
        subcommands = {TestCommand.class, RunCommand.class}
)
public class EntryCommand extends HelperCommand {

    public static void main(String[] args) {
//        System.exit(PicocliRunner.execute(EntryCommand.class, args));
        try (ApplicationContext context = ApplicationContext.builder(EntryCommand.class, Environment.CLI).start()) {
            int exitCode = new CommandLine(EntryCommand.class, new MicronautFactory(context)).
                    setCaseInsensitiveEnumValuesAllowed(true).
                    setUsageHelpAutoWidth(true).
                    execute(args);
            System.exit(exitCode);
        }
    }
}

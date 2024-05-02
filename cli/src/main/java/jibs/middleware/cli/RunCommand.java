package jibs.middleware.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import jibs.middleware.VersionProvider;
import io.micronaut.runtime.server.EmbeddedServer;
import javax.inject.Inject;

/**
 *
 * @author jibs
 */
@CommandLine.Command(name = "run", description = "Run Middleware on Micronaut",
        mixinStandardHelpOptions = true, versionProvider = VersionProvider.class
)
public class RunCommand implements Runnable {

    private static final Logger L = LoggerFactory.getLogger(RunCommand.class);

    @Inject
    EmbeddedServer server;

    @SuppressWarnings("java:S6212")
    @Override
    public void run() {
        L.info("Running API ...");

        if (!server.isRunning()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                L.info("Shutting down HTTP server");
                server.stop();
            }));
            server.start();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
            }
        }

    }

}

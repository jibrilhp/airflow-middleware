package jibs.middleware.cli;

import jibs.middleware.db.MiddlewareDB;
import javax.sql.DataSource;
import javax.inject.Inject;
import javax.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jibs.middleware.HelperCommand;
import jibs.middleware.VersionProvider;


/**
 *
 * @author gusto
 */
@CommandLine.Command(name = "test", description = "Test sub-command", hidden = false,
        mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
        subcommands = {}
)
public class TestCommand extends HelperCommand {

    private static final Logger L = LoggerFactory.getLogger(TestCommand.class);

    @Inject
    @MiddlewareDB
    Provider<DataSource> middlewareDatasource;

    @SuppressWarnings("java:S6212")
    @Override
    public void run() {
        L.info("Testing registrar database ...");
        try ( Connection conn = middlewareDatasource.get().getConnection()) {
            try ( Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                while (rs.next()) {
                    L.info("Result: {}", rs.getInt(1));
                    System.out.println("OK");
                }
            }
        } catch (SQLException ex) {
            throw new jibs.RuntimeException("SQL test error", ex);
        }
    }
}

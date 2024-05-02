package jibs.middleware.db;

//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import javax.inject.Inject;
//import javax.inject.Provider;
//import javax.sql.DataSource;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//@MicronautTest
//public class DBFactoryTest {
//
//    private static final Logger L = LoggerFactory.getLogger(DBFactoryTest.class);
//
//    @Inject
//    @FdwDB
//    Provider<DataSource> fdwDataSource;
//
//    @Inject
//    @RegistrarDB
//    Provider<DataSource> registrarDatasource;
//
//    @Test
//    public void testProvideFdwDatasource1() throws SQLException {
//        L.info("Testing FDW database ...");
//        try ( Connection conn = fdwDataSource.get().getConnection()) {
//            try ( Statement stmt = conn.createStatement()) {
//                ResultSet rs = stmt.executeQuery("SELECT 1");
//                while (rs.next()) {
//                    L.info("Result: {}", rs.getInt(1));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void testProvideFdwDatasource2() throws SQLException {
//        L.info("Testing FDW database ...");
//        try ( Connection conn = fdwDataSource.get().getConnection()) {
//            try ( Statement stmt = conn.createStatement()) {
//                ResultSet rs = stmt.executeQuery("SELECT 2");
//                while (rs.next()) {
//                    L.info("Result: {}", rs.getInt(1));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void testProvideRegistrarDatasource() throws SQLException {
//        L.info("Testing registrar database ...");
//        try ( Connection conn = registrarDatasource.get().getConnection()) {
//            try ( Statement stmt = conn.createStatement()) {
//                ResultSet rs = stmt.executeQuery("SELECT 1");
//                while (rs.next()) {
//                    L.info("Result: {}", rs.getInt(1));
//                }
//            }
//        }
//    }
//}

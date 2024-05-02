package jibs.middleware.db;

//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
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
//public class PoolDBTest {
//
//    private static final Logger L = LoggerFactory.getLogger(PoolDBTest.class);
//
//    @Inject
//    Provider<PoolDB> datasourceProvider;
//
//    @Test
//    public void testQuery() throws Exception {
//        String pool = "bc";
//        DataSource ds = datasourceProvider.get().getDataSource(pool);
//        try ( Connection conn = ds.getConnection()) {
//            try ( Statement stmt = conn.createStatement()) {
//                ResultSet rs = stmt.executeQuery("SELECT 1");
//                while (rs.next()) {
//                    L.info("Pool: {}, Result: {}", pool, rs.getInt(1));
//                }
//            }
//        } catch (SQLException ex) {
//            throw new jibs.RuntimeException("SQL test error, pool: {}", pool, ex);
//        }
//    }
//
//    @Test
//    public void testDatabaseMeta() throws Exception {
//        String pool = "cf";
//        DataSource ds = datasourceProvider.get().getDataSource(pool);
//        try ( Connection conn = ds.getConnection()) {
//            DatabaseMetaData dbMeta = conn.getMetaData();
//            L.info("Product name: {}", dbMeta.getDatabaseProductName());
//            L.info("Product version: {}", dbMeta.getDatabaseProductVersion());
//
//            try ( ResultSet rs = dbMeta.getTables(null, null, null, new String[]{"TABLE"})) {
//                while (rs.next()) {
//                    L.info("  Table: {}", rs.getString("TABLE_NAME"));
//                }
//            }
//
//            try(ResultSet rs = dbMeta.getColumns(null, null, "TR_Hutang_PengemudiX", null)) {
//                int sz = 0;
//                while (rs.next()) {
//                    sz++;
//                    L.info("  Table: {} => {} {}",
//                            rs.getString("TABLE_NAME"),
//                            rs.getString("COLUMN_NAME"),
//                            rs.getString("TYPE_NAME"));
//                }
//                L.info("Field size: {}", sz);
//            }
//        } catch (SQLException ex) {
//            throw new jibs.RuntimeException("SQL test error, pool: {}", pool, ex);
//        }
//    }
//}

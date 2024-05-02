package jibs.middleware.config;

//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import javax.inject.Inject;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//@MicronautTest
//public class DeploymentConfigTest {
//    private static final Logger L = LoggerFactory.getLogger(DeploymentConfigTest.class);
//
//    @Inject
//    private DeploymentConfig config;
//
//    @Test
//    public void testGetTables() {
//        for (DeploymentConfig.Table table : config.getTables()) {
//            L.info("Table: {}({}) => {}", table.getOriginTablename(),
//                    table.getTablename("CJ"), table.getExportAs());
//        }
//
//        DeploymentConfig.Table table = config.getTable("ADM_Tabungan_Pengemudi_2");
//        L.info("Table: '{}' Field: KodePool => {}", table.getOriginTablename(),
//                table.getFieldExportAs("KodePool"));
//    }
//
//}

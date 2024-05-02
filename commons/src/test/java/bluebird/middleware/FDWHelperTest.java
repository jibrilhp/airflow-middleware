package jibs.middleware;

//import jibs.fdw.fdwmeta.FdwMeta;
//import jibs.fdw.fdwmeta.FieldMeta;
//import jibs.fdw.fdwmeta.TableMeta;
//import jibs.utils.SQLType;
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import javax.inject.Inject;
//import org.apache.commons.codec.digest.DigestUtils;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//@MicronautTest
//public class FDWHelperTest {
//
//    private static final Logger L = LoggerFactory.getLogger(FDWHelperTest.class);
//
//    @Inject
//    FDWHelper fdwHelper;
//
//    @Test
//    @SuppressWarnings("java:S2699")
//    public void testSyncPoolInformation() throws Exception {
//        fdwHelper.syncPoolInformation();
//    }
//
//    @Test
//    public void testSynchronizeDBVersion() throws Exception {
//        fdwHelper.synchronizeDBVersion("PM");
//    }
//
//    @Test
//    public void testMD5() throws IOException {
//        String hash = "35cdc8e8def550d85a333fbc8aa19f51".toUpperCase();
//        String password = "hello java";
//
//        try ( ByteArrayInputStream in = new ByteArrayInputStream(password.getBytes())) {
//            String md5Hex = DigestUtils.md5Hex(in).toUpperCase();
//            assertEquals(md5Hex, hash);
//        }
//    }
//
//    @Test
//    public void testGetTables() throws Exception {
//        FdwMeta fdw = fdwHelper.getTables("BC");
//        for (TableMeta table : fdw.getTables()) {
//            L.info("Table: {} => {}", table.getName(), table.getExportName());
//            for (FieldMeta field : table.getFields()) {
//                L.info("  {}/{}  {}:({},{}) => {}", field.getName(), field.getExportName(),
//                        field.getDatatype(), field.getLength(), field.getScale(),
//                        SQLType.valueOf(field.getSqltype()));
//            }
//        }
//    }
//
//    @Test
//    public void testGenMD5() throws Exception {
//        FdwMeta fdwMeta = fdwHelper.getTables("BC");
//        String md5 = fdwHelper.genMD5(fdwMeta);
//        L.info("MD5 Hash: {}", md5);
//    }
//
//    @Test
//    public void testDeployFDW() throws Exception {
//        fdwHelper.deployFDW();
//    }
//
//    @Test
//    public void testDeploy() throws FdwException {
//        fdwHelper.deploy("CJ");
////        fdwHelper.deploy("DM"); // 10.50.6560
////        fdwHelper.deploy("HL"); // 10.50.6000
////        fdwHelper.deploy("KG"); // 10.50.4000
////        fdwHelper.deploy("KJ"); // 10.50.1600
////        fdwHelper.deploy("PA"); // 09.00.4035
////        fdwHelper.deploy("TB"); // 09.00.3042
////        fdwHelper.deploy("MN"); // 08.00.2039
//    }
//}

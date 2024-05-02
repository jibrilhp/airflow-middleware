package jibs.middleware.config;

//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import java.util.Collection;
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
//public class PoolDBConfigMapTest {
//
//    private static final Logger L = LoggerFactory.getLogger(PoolDBConfigMapTest.class);
//
//    @Inject
//    PoolDBConfigMap configMap;
//
//    @Test
//    public void testGetConfigNames() {
//        Collection<String> configNames = configMap.getConfigNames();
//        for (String configName : configNames) {
//            L.info("Config name: {}", configName);
//        }
//    }
//
//    @Test
//    public void testGetPoolDBConfig() {
//        PoolDBConfig config = configMap.getPoolDBConfig("BC");
//        L.info("Name: {}", config.getName());
//        L.info("  DB: {}", config.getDbName());
//        L.info("  User: {}", config.getUser());
//        L.info("  Password: {}", config.getPassword());
//        L.info("  Port: {}", config.getPort());
//    }
//}

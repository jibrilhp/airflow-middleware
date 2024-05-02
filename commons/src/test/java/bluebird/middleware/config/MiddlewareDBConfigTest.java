package jibs.middleware.config;

import jibs.middleware.MiddlewareHelper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gusto
 */
@MicronautTest
public class MiddlewareDBConfigTest {

    private static final Logger L = LoggerFactory.getLogger(MiddlewareDBConfigTest.class);

    @Inject
    MiddlewareDBConfig config;

    @Inject
    MiddlewareHelper middlewareHelper;


    @Test
    public void testPrintConfigs() {
        L.info("Host: {}", config.getHost());
        L.info("Port: {}", config.getPort());
        L.info("Username: {}", config.getUser());
        L.info("Password: {}", config.getPassword());
        L.info("Min.Idle: {}", config.getMinIdle());
        L.info("Max.Idle: {}", config.getMaxIdle());
        L.info("Max.Connection: {}", config.getMaxTotal());
    }

    @Test
    @SuppressWarnings("java:S2699")
    public void testSyncPoolInformation() throws Exception {
        middlewareHelper.updateDAGInformation("TEST");
    }

}

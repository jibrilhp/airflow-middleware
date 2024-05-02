package jibs.middleware.config;

import jibs.middleware.db.MiddlewareDB;
import io.micronaut.context.annotation.ConfigurationProperties;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * {@link MiddlewareDB } datasource configuration
 *
 * @author gusto
 */
@ConfigurationProperties("middleware-db")
public class MiddlewareDBConfig {

    @NotEmpty
    private String host;

    @Positive
    private int port = 5432;

    @NotEmpty
    private String user;

    @NotEmpty
    private String password;

    @NotEmpty
    private String dbName;

    @PositiveOrZero
    private int minIdle = 0;

    @PositiveOrZero
    private int maxIdle = 2;

    @PositiveOrZero
    private int maxTotal = 2;

    @PositiveOrZero
    private int maxWaitMillis = 60000;

    public String getHost() {
        return host;
    }

    void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getMinIdle() {
        return minIdle;
    }

    void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
}

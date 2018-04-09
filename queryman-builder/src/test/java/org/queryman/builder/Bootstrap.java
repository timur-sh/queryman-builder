/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class Bootstrap {
    public static final Bootstrap  BOOT       = new Bootstrap();
    private final static HikariDataSource dataSource;
    private static      Properties properties = new Properties();

    static {
        try {
            load();
        } catch (IOException e) {
            throw new BootstrapException(e.getMessage());
        }

        HikariConfig config = new HikariConfig(Bootstrap.BOOT.getProperties());
        config.setIdleTimeout(1000);
        config.setMaximumPoolSize(50);

        dataSource = new HikariDataSource(config);
    }

    private static void load() throws IOException {
        URL resources = Bootstrap.class.getClassLoader().getResource("database.properties");

        if (resources != null) {
            properties.load(resources.openStream());
            return;
        }

        throw new FileNotFoundException("File database.properties not found");
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public Properties getProperties() {
        return properties;
    }
}

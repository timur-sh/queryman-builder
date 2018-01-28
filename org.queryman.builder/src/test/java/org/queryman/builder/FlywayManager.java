/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.flywaydb.core.Flyway;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public final class FlywayManager {
    public  Flyway     flyway     = new Flyway();
    private Properties properties = new Properties();

    public FlywayManager() {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        flyway.setDataSource(
           properties.getProperty("url"),
           properties.getProperty("username"),
           properties.getProperty("password")
        );
    }

    private void load() throws IOException {
        URL resources = this.getClass().getClassLoader().getResource("database.properties");

        if (resources != null) {
            properties.load(resources.openStream());
            return;
        }

        throw new FileNotFoundException("File database.properties not found");
    }

    public void migrate() {
        System.err.close();
        flyway.migrate();
        System.setErr(System.out);
    }

    public void clean() {
        flyway.clean();
    }
}

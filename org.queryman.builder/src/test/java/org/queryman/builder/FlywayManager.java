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
    public Flyway flyway = new Flyway();

    public void init() {
        PropertiesLoader loader = new PropertiesLoader();
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceLoadException(e.getMessage());
        }

        Properties properties = loader.getProperties();

        flyway.setDataSource(
           properties.getProperty("url"),
           properties.getProperty("user"),
           properties.getProperty("password")
        );
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

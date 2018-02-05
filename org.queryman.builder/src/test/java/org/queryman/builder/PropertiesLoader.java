/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class PropertiesLoader {
    private static Properties properties = new Properties();

    public void load() throws IOException {
        URL resources = this.getClass().getClassLoader().getResource("database.properties");

        if (resources != null) {
            properties.load(resources.openStream());
            return;
        }

        throw new FileNotFoundException("File database.properties not found");
    }

    public Properties getProperties() {
        return properties;
    }
}

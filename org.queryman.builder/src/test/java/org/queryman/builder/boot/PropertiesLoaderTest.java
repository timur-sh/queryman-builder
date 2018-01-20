/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.loader.PropertiesLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Timur Shaidullin
 */
public class PropertiesLoaderTest {
    private final String QUERYMAN_CONNECTION_URL      = "queryman.connection.url";
    private final String QUERYMAN_CONNECTION_USER     = "queryman.connection.user";
    private final String QUERYMAN_CONNECTION_PASSWORD = "queryman.connection.password";
    private final String QUERYMAN_TRANSACTION         = "queryman.transaction";

    @Test
    public void loadQuerymanProperties() throws IOException, ClassNotFoundException {
        PropertiesLoader loader = new PropertiesLoader("queryman-builder.properties");

        loader.load();

        assertEquals(loader.getConfiguration().size(), 4);
        Properties  properties = loader.getConfiguration();
        Set<String> names      = properties.stringPropertyNames();

        assertTrue(names.contains(QUERYMAN_CONNECTION_PASSWORD));
        assertTrue(names.contains(QUERYMAN_CONNECTION_URL));
        assertTrue(names.contains(QUERYMAN_CONNECTION_USER));
        assertTrue(names.contains(QUERYMAN_TRANSACTION));
    }

    @Test
    public void fileNotPassed() {
        PropertiesLoader loader = new PropertiesLoader();
        assertThrows(FileNotFoundException.class, loader::load);
    }


    @Test
    public void loaderException() {
        PropertiesLoader loader = new PropertiesLoader("not.exists.properties");
        assertThrows(FileNotFoundException.class, loader::load);
    }

    @Test
    public void filePassedToMethod() throws IOException, ClassNotFoundException {
        PropertiesLoader loader = new PropertiesLoader();
        assertThrows(FileNotFoundException.class, loader::load);
        assertEquals(loader.getConfiguration().size(), 0);

        loader.setFile("queryman.properties");
        loader.load();
        assertEquals(loader.getConfiguration().size(), 4);
    }
}
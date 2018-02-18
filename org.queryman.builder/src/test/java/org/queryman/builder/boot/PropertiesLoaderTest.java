/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Metadata;
import org.queryman.builder.cfg.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Timur Shaidullin
 */
public class PropertiesLoaderTest {
    @Test
    public void loadOk() throws IOException, ClassNotFoundException {
        PropertiesLoader loader = new PropertiesLoader("queryman-builder.properties");
        loader.load();
        Properties metadata = loader.getConfiguration();

        assertEquals(metadata.size(), 1);
        assertEquals(Boolean.valueOf(metadata.getProperty(Settings.USE_UPPERCASE)), true);
    }

    @Test
    public void loadFileNotFound() {
        PropertiesLoader loader = new PropertiesLoader("deleted-properties.properties");
        Throwable throwable = assertThrows(FileNotFoundException.class, loader::load);
        assertEquals(throwable.getMessage(), "Configuration file is  not found");
    }

    @Test
    public void loadFileNotSpecified() {
        PropertiesLoader loader = new PropertiesLoader("");
        Throwable throwable = assertThrows(IllegalStateException.class, loader::load);
        assertEquals(throwable.getMessage(), "Properties file is not specified");
    }
}
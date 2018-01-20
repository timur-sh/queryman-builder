package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.loader.JaxbLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JaxbLoaderTest {
    private final String QUERYMAN_CONNECTION_URL      = "queryman.connection.url";
    private final String QUERYMAN_CONNECTION_USER     = "queryman.connection.user";
    private final String QUERYMAN_CONNECTION_PASSWORD = "queryman.connection.password";

    @Test
    public void loadQuerymanProperties() throws IOException, ClassNotFoundException {
        JaxbLoader loader = new JaxbLoader("queryman-configuration.xml");

        loader.load();

        assertEquals(loader.getConfiguration().size(), 3);
        Properties  properties = loader.getConfiguration();
        Set<String> names      = properties.stringPropertyNames();

        assertTrue(names.contains(QUERYMAN_CONNECTION_PASSWORD));
        assertTrue(names.contains(QUERYMAN_CONNECTION_URL));
        assertTrue(names.contains(QUERYMAN_CONNECTION_USER));
    }

    @Test
    public void fileNotPassed() {
        JaxbLoader loader = new JaxbLoader();
        assertThrows(FileNotFoundException.class, loader::load);
    }


    @Test
    public void loaderException() {
        JaxbLoader loader = new JaxbLoader("not.exists.properties");
        assertThrows(FileNotFoundException.class, loader::load);
    }

    @Test
    public void filePassedToMethod() throws IOException, ClassNotFoundException {
        JaxbLoader loader = new JaxbLoader();
        assertThrows(FileNotFoundException.class, loader::load);
        assertEquals(loader.getConfiguration(), null);

        loader.setFile("queryman-configuration.xml");
        loader.load();
        assertEquals(loader.getConfiguration().size(), 3);
    }
}
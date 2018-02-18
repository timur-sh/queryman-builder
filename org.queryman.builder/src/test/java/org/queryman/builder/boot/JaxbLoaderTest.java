package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Metadata;
import org.queryman.builder.cfg.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JaxbLoaderTest {
    @Test
    public void loadOk() throws IOException, ClassNotFoundException {
        JaxbLoader loader = new JaxbLoader("queryman-builder.xml");
        loader.load();
        Properties metadata = loader.getConfiguration();

        assertEquals(metadata.size(), 1);
        assertTrue(metadata.contains(Settings.USE_UPPERCASE));
        assertEquals(Boolean.valueOf(metadata.getProperty(Settings.USE_UPPERCASE)), false);
    }

    @Test
    public void loadFileNotFound() {
        JaxbLoader loader = new JaxbLoader("deleted-file.xml");
        Throwable throwable = assertThrows(FileNotFoundException.class, loader::load);
        assertEquals(throwable.getMessage(), "Configuration file is  not found");
    }

    @Test
    public void loadFileNotSpecified() {
        JaxbLoader loader = new JaxbLoader("");
        Throwable throwable = assertThrows(IllegalStateException.class, loader::load);
        assertEquals(throwable.getMessage(), "Xml file is not specified");
    }

    @Test
    public void loadEmpty() throws IOException, ClassNotFoundException {
        JaxbLoader loader = new JaxbLoader("queryman-builder-broke.xml");
        Properties metadata = loader.getConfiguration();

        assertEquals(Boolean.valueOf(metadata.getProperty(Settings.USE_UPPERCASE)), false);
    }
}
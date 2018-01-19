package org.queryman.loader.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.queryman.loader.Loader;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoaderImplTest {
    @Test
    void buildXmlConfig() throws IOException, ClassNotFoundException {
        Loader builder = new LoaderImpl();
        builder.load();

        assertEquals(builder.getConfiguration().size(), 3);
    }

    @Test
    void buildPropertiesConfig() throws IOException, ClassNotFoundException {
        Loader builder = new LoaderImpl();
        builder
            .setXmlConfigurationName("")
            .load();

        assertEquals(builder.getConfiguration().size(), 4);
    }

    @Test
    void buildConfigurationNotFound() throws IOException, ClassNotFoundException {
        Loader builder = new LoaderImpl();
        Executable executable = builder
            .setXmlConfigurationName("")
            .setPropertiesConfigurationName("")
            ::load;

        assertThrows(FileNotFoundException.class, executable);
    }
}
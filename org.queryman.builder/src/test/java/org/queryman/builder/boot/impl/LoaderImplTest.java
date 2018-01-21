package org.queryman.builder.boot.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.queryman.builder.boot.ServiceLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoaderImplTest {
    @Test
    void buildXmlConfig() throws IOException, ClassNotFoundException {
        ServiceLoader builder = new ServiceLoaderImpl();
        builder.load();

        assertEquals(builder.getConfiguration().getProperties().size(), 1);
    }

//    @Test
//    void buildPropertiesConfig() throws IOException, ClassNotFoundException {
//        ServiceLoader builder = new ServiceLoaderImpl();
//        builder
//            .load();
//
//        assertEquals(builder.getConfiguration().size(), 4);
//    }
//
//    @Test
//    void buildConfigurationNotFound() throws IOException, ClassNotFoundException {
//        ServiceLoader builder = new ServiceLoaderImpl();
//        Executable executable = builder
//            .setXmlConfigurationName("")
//            .setPropertiesConfigurationName("")
//            ::load;
//
//        assertThrows(FileNotFoundException.class, executable);
//    }
}
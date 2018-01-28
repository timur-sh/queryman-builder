package org.queryman.builder.boot.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Metadata;
import org.queryman.builder.boot.JaxbLoader;
import org.queryman.builder.boot.ServiceLoader;
import org.queryman.builder.boot.ServiceLoaderImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceLoaderImplTest {
    @Test
    void loadOk() throws IOException, ClassNotFoundException {
        ServiceLoader loader = new ServiceLoaderImpl(
            new JaxbLoader("queryman-builder.xml")
        );
        assertTrue(loader.load());

       assertTrue(loader.getConfiguration() instanceof Metadata);
    }

    @Test
    void loadFail() throws IOException, ClassNotFoundException {
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> {
            ServiceLoader loader = new ServiceLoaderImpl();
        });

        assertEquals(throwable.getMessage(), "ConfigLoader is not passed");
    }
}
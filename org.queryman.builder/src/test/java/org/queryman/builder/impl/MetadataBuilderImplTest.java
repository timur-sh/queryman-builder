package org.queryman.builder.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.cfg.Settings;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MetadataBuilderImplTest {
    @Test
    void buildSuccess() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder.build();

        Metadata metadata = builder.getMetadata();
        assertNotNull(metadata);

        assertEquals(metadata.getProperties().size(), 1);
    }

    @Test
    void fileNotFound() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder
           .setXmlCfg("")
           .setPropertiesCfg("");

        assertThrows(IOException.class, builder::build);
    }

    @Test
    void loadFromProperties() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder
           .setXmlCfg("")
           .build();

        Metadata metadata = builder.getMetadata();
        assertEquals(metadata.getProperties().size(), 1);
    }

    @Test
    void emptyValues() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder
           .setXmlCfg("queryman-builder-empty.xml")
           .build();

        Metadata metadata = builder.getMetadata();
        assertEquals(metadata.getProperty(Settings.USE_UPPERCASE), "true");
        assertEquals(metadata.getProperties().size(), 1);
    }

    @Test
    void merge() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();

        Metadata metadata = new MetadataImpl()
           .addProperty("queryman.builder.quotes", "none");

        builder.build(metadata);

        Metadata newMetadata = builder.getMetadata();
        assertEquals(newMetadata.getProperties().size(), 2);
    }
}
package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.builder.cfg.Settings;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MetadataBuilderImplTest {
    @Test
    void buildOk() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder.build();

        Metadata metadata = builder.getMetadata();
        assertNotNull(metadata);

        assertEquals(metadata.getProperties().size(), 1);
    }

    @Test
    void applyDefaultSettings() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();builder
            .setXmlCfg("")
            .setPropertiesCfg("")
            .build();

        Metadata metadata = builder.getMetadata();
        assertEquals(metadata.getProperty(Settings.USE_UPPERCASE), "false");
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
    void brokeProperties() throws IOException, ClassNotFoundException {
        MetadataBuilder builder = new MetadataBuilderImpl();
        builder
            .setXmlCfg("")
            .setPropertiesCfg("queryman-builder-broken.properties")
            .build();

        Metadata metadata = builder.getMetadata();
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
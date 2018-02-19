package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.builder.cfg.Settings;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRegisterTest {
    @Test
    void makeOk() {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.make();
    }

    @Test
    void makeDefaultMetadata() {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.getMetadataBuilder().setPropertiesCfg("").setXmlCfg("");

        serviceRegister.make();
        Metadata metadata = serviceRegister.getMetadataBuilder().getMetadata();
        assertEquals(metadata.getProperties().size(), 1);
        assertEquals(metadata.getProperty(Settings.USE_UPPERCASE), Settings.DEFAULTS.get(Settings.USE_UPPERCASE));
    }

    @Test
    void customMetadataBuilder() throws IOException, ClassNotFoundException {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.changeBuilder(new MetadataBuilderImpl());

        serviceRegister.make();
    }
}
package org.queryman.builder.boot;

import org.junit.jupiter.api.Test;
import org.queryman.builder.CMD;
import org.queryman.builder.impl.MetadataBuilderImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRegisterTest {
    @Test
    void makeSuccess() throws IOException, ClassNotFoundException {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.make();
    }

    @Test
    void initSQLManager() throws IOException, ClassNotFoundException {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.make();

        CMD cmd = serviceRegister.getCmd();
        assertNotNull(cmd);
    }

    @Test
    void makeFail() {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.getMetadataBuilder().setPropertiesCfg("").setXmlCfg("");

        assertThrows(FileNotFoundException.class, serviceRegister::make);
    }

    @Test
    void customMetadataBuilder() throws IOException, ClassNotFoundException {
        ServiceRegister serviceRegister = new ServiceRegister();
        serviceRegister.changeBuilder(new MetadataBuilderImpl());

        serviceRegister.make();
    }
}
/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.impl.MetadataBuilderImpl;
import org.queryman.builder.impl.MetadataImpl;

import java.io.IOException;

/**
 * @author Timur Shaidullin
 */
public class ServiceRegisterExample {
    public void register() throws IOException, ClassNotFoundException {
        //tag::simple-register[]
        ServiceRegister serviceRegister = new ServiceRegister().make();
        //end::simple-register[]

        //tag::change-xml-file[]
        serviceRegister.getMetadataBuilder()
           .setXmlCfg("/etc/queryman/builder.xml");
        //end::change-xml-file[]

        //tag::change-properties-file[]
        serviceRegister.getMetadataBuilder()
           .setXmlCfg("/etc/queryman/builder.properties");
        //end::change-properties-file[]


        //tag::custom-metadata[]
        Metadata metadata = new MetadataImpl();

        serviceRegister.make(metadata);
        //end::custom-metadata[]
    }

    public void provideMetadataBuidler() throws IOException, ClassNotFoundException {
        MetadataBuilder metadataBuilder = new MetadataBuilderImpl();

        //tag::provide-builder[]
        ServiceRegister serviceRegister = new ServiceRegister()
           .changeBuilder(metadataBuilder)
           .make();
        //end::provide-builder[]
    }
}

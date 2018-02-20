/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.boot.Metadata;
import org.queryman.builder.boot.MetadataBuilder;
import org.queryman.builder.boot.MetadataBuilderImpl;
import org.queryman.builder.boot.MetadataImpl;

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
        // Change the name of xml configuration file.
        serviceRegister.getMetadataBuilder()
           .setXmlCfg("another-settings.xml");

        // Change the name of properties configuration file.
        serviceRegister.getMetadataBuilder()
           .setXmlCfg("another-settings.properties");
        //end::change-xml-file[]

        //tag::get-tree-factory[]
        PostgreSQL.setTreeFactory(serviceRegister.treeFactory());
        //end::get-tree-factory[]


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

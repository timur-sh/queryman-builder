/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.boot.impl;

import org.queryman.core.boot.Metadata;
import org.queryman.core.boot.MetadataBuilder;

import java.io.Serializable;

/**
 * @author Timur Shaidullin
 */
public class MetadataBuilderImpl implements MetadataBuilder, Serializable {
    private String xmlFile = "queryman-configuration.xml";
    private String propertiesFile = "queryman.properties";

    private Metadata metadata;

    @Override
    public MetadataBuilder build() {
        return this;
    }

    @Override
    public MetadataBuilder build(Metadata metadata) {
        build();

        return this;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public MetadataBuilder setXmlConfigurationName(String name) {
        xmlFile = name;
        return this;
    }

    @Override
    public MetadataBuilder setPropertiesConfigurationName(String name) {
        propertiesFile = name;
        return this;
    }
//
//    private InputStream getResource(String resourceName) throws IOException {
//        ClassLoader classLoader = this.getClass().getClassLoader();
//
//        if (classLoader == null) {
//            return null;
//        }
//
//        URL resource = classLoader.getResource(resourceName);
//        if (resource == null) {
//            return null;
//        }
//
//        return resource.openStream();
//    }
}

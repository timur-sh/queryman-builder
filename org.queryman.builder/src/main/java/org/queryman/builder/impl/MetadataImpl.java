/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.impl;

import org.queryman.builder.Metadata;

import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class MetadataImpl implements Metadata {
    private Properties properties = new Properties();

    @Override
    public Metadata addProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key, null);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public Metadata addProperties(Properties properties) {
        this.properties = properties;
        return this;
    }
}

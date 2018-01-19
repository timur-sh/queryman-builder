/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.boot.impl;

import org.queryman.core.boot.Metadata;
import org.queryman.core.boot.cfg.Database;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public final class MetadataImpl implements Metadata, Serializable {
    private Properties properties = new Properties();

    public MetadataImpl(String url, String user, String password) {
        properties.put("url", url);
        properties.put("user", user);
        properties.put("password", password);
    }

    @Override
    public Metadata setUrl(String url) {
        properties.put("url", url);
        return this;
    }

    @Override
    public Metadata setUser(String user) {
        properties.put("user", user);
        return this;
    }

    @Override
    public Metadata setPassword(String password) {
        properties.put("password", password);
        return this;
    }

    @Override
    public Database getDatabase() {
        return new Database(
           properties.get("url").toString(),
           properties.get("user").toString(),
           properties.get("password").toString()
        );
    }

    @Override
    public Metadata addProperty(String name, String value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public Metadata addProperties(Properties properties) {
        this.properties = properties;
        return this;
    }


}

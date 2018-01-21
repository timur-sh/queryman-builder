/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot.impl;

import org.queryman.builder.Metadata;
import org.queryman.builder.boot.ConfigLoader;
import org.queryman.builder.boot.ServiceLoader;

import java.io.IOException;

/**
 * @author Timur Shaidullin
 */
public class ServiceLoaderImpl implements ServiceLoader {
    private final ConfigLoader[] loaders;
    private Metadata metadata;

    public ServiceLoaderImpl(ConfigLoader... loaders) {
        this.loaders = loaders;
    }

    @Override
    public boolean load() {
        for (ConfigLoader loader : loaders) {
            try {
                loader.load();
                metadata = loader.getConfiguration();
                return true;
            } catch (ClassNotFoundException | IOException e) {
                //todo log an error
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public Metadata getConfiguration() {
        return metadata;
    }
}

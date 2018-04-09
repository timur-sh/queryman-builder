/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class ServiceLoaderImpl implements ServiceLoader {
    private final static Logger LOG = LogManager.getLogger("org.queryman.builder.boot");

    private final ConfigLoader[] loaders;
    private Properties properties;

    public ServiceLoaderImpl(ConfigLoader... loaders) {
        if (loaders.length == 0) {
            throw new IllegalArgumentException("ConfigLoader is not passed");
        }
        this.loaders = loaders;
    }

    @Override
    public boolean load() {
        for (ConfigLoader loader : loaders) {
            try {
                loader.load();
                properties = loader.getConfiguration();
                return true;
            } catch (IllegalStateException | ClassNotFoundException | IOException e) {
                LOG.error(e.getMessage());
            }
        }

        return false;
    }

    @Override
    public Properties getConfiguration() {
        return properties;
    }
}

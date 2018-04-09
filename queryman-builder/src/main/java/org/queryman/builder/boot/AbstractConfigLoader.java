/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractConfigLoader implements ConfigLoader {

    protected String cfgFile;

    public AbstractConfigLoader(String cfgFile) {
        this.cfgFile = cfgFile;
    }

    protected InputStream getResource(String resourceName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (classLoader == null) {
            throw new ClassNotFoundException();
        }

        URL resource = classLoader.getResource(resourceName);
        if (resource == null) {
            throw new FileNotFoundException("Configuration file is  not found");
        }

        return resource.openStream();
    }
}

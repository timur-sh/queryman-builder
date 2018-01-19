/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load a persistence set from a property file
 *
 * @author Timur Shaidullin
 */
public final class PropertiesLoader extends AbstractConfigLoader {

    private final Properties properties = new Properties();

    public PropertiesLoader() {
        super();
    }

    public PropertiesLoader(String cfgFile) {
        super(cfgFile);
    }

    @Override
    public boolean load() throws IOException, ClassNotFoundException {
        if (StringUtils.isEmpty(cfgFile)) {
            throw new FileNotFoundException("Configuration file not specified");
        }
        InputStream stream = getResource(cfgFile);

        try {
            properties.load(stream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();  //todo log stacktrace
            return false;
        }
    }

    @Override
    public Properties getConfiguration() {
        return properties;
    }
}

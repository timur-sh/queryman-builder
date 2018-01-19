/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader.impl;

import org.queryman.loader.Loader;
import org.queryman.loader.JaxbLoader;
import org.queryman.loader.PropertiesLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class LoaderImpl implements Loader {

    private final PropertiesLoader propertiesLoader = new PropertiesLoader("queryman.properties");
    private final JaxbLoader       jaxbLoader       = new JaxbLoader("queryman-configuration.xml");

    private Properties configuration;

    @Override
    public Loader load() throws ClassNotFoundException, IOException {
        if (loadingHandler(jaxbLoader::load)) {
            configuration = jaxbLoader.getConfiguration();
            return this;
        } else if (loadingHandler(propertiesLoader::load)) {
            configuration = propertiesLoader.getConfiguration();
            return this;
        }

        throw new FileNotFoundException("Configuration file not found");
    }

    private boolean loadingHandler(LoadingHandler handler) throws ClassNotFoundException, IOException {
        try {
            return handler.load();
        } catch (FileNotFoundException e) {
            return false;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Loader setXmlConfigurationName(String name) {
        jaxbLoader.setFile(name);
        return this;
    }

    @Override
    public Loader setPropertiesConfigurationName(String name) {
        propertiesLoader.setFile(name);
        return this;
    }

    @Override
    public Properties getConfiguration() {
        return configuration;
    }

    private interface LoadingHandler {
        boolean load() throws IOException, ClassNotFoundException;
    }
}

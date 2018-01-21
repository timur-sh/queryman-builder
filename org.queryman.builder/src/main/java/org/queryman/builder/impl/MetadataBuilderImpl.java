/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.impl;

import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.boot.JaxbLoader;
import org.queryman.builder.boot.PropertiesLoader;
import org.queryman.builder.boot.ServiceLoader;
import org.queryman.builder.boot.impl.ServiceLoaderImpl;
import org.queryman.builder.cfg.Settings;

/**
 * @author Timur Shaidullin
 */
public class MetadataBuilderImpl implements MetadataBuilder {
    private Metadata metadata;

    private String xmlCfgFile        = "queryman-builder.xml";
    private String propertiesCfgFile = "queryman-builder.properties";

    public MetadataBuilderImpl() {
    }

    @Override
    public MetadataBuilder setXmlCfg(String cfgFile) {
        xmlCfgFile = cfgFile;
        return this;
    }

    @Override
    public MetadataBuilder setPropertiesCfg(String cfgFile) {
        propertiesCfgFile = cfgFile;
        return this;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void build() {
        load();
        checker();
        applyDefaults();
    }

    /**
     * Load configuration.
     */
    private boolean load() {
        ServiceLoader loader = new ServiceLoaderImpl(
            new JaxbLoader(xmlCfgFile),
            new PropertiesLoader(propertiesCfgFile)
        );

        try {
            if (loader.load()) {
                metadata = loader.getConfiguration();
                return true;
            }
        } catch (IllegalStateException e) {
            //todo log
        }

        metadata = new MetadataImpl();
        return false;
    }

    /**
     * The {@code metadata} is validated.
     */
    private void checker() {
       //todo Establish integrity each value of metadata by particular key.
    }

    /**
     * Default values of settings is assigned to {@code metadata} if any of them
     * are not in it.
     */
    private void applyDefaults() {
        for (String setting : Settings.settings) {
            if (metadata.isEmpty(setting)) {
                metadata.addProperty(setting, Settings.DEFAULTS.get(setting));
            }
        }
    }

    @Override
    public void build(Metadata metadata) {
        load();
        checker();
        merge(this.metadata, metadata);

        applyDefaults();
    }

    /**
     * The both metadata objects are merged. The metadata provides by
     * {@link ServiceLoader} takes precedence. If any key/value pair of {@code fromUser}
     * is missed by the {@code fromCfg}, it is copied.
     *
     * @param fromCfg  {@link Metadata} object represents configuration loaded
     *                 by {@link ServiceLoader}
     * @param fromUser {@link Metadata} objects represents metadata provides by user
     */
    private void merge(Metadata fromCfg, Metadata fromUser) {

        for (String key : fromUser.getProperties().stringPropertyNames()) {
            //todo check the key is a part of Settings
            if (!fromCfg.contains(key)) {
                fromCfg.addProperty(key, fromUser.getProperty(key));
            }
        }
    }
}

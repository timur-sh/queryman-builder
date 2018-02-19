/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.cfg.Settings;

/**
 * Standard implementation of {@link MetadataBuilder}.
 *
 * @author Timur Shaidullin
 */
class MetadataBuilderImpl implements MetadataBuilder {
    private final Metadata metadata = new MetadataImpl();

    private String xmlCfgFile        = "queryman-builder.xml";
    private String propertiesCfgFile = "queryman-builder.properties";

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
    public void buildFromDefault() {
        applyDefaults();
    }

    @Override
    public void build() {
        load();
        checker();
        applyDefaults();
    }

    @Override
    public void build(Metadata metadata) {
        load();
        checker();
        merge(this.metadata, metadata);

        applyDefaults();
    }

    /**
     * Load configuration.
     *
     * @return {@code true} if configuration loaded successfully.
     */
    private boolean load() {
        ServiceLoader loader = new ServiceLoaderImpl(
            new JaxbLoader(xmlCfgFile),
            new PropertiesLoader(propertiesCfgFile)
        );

        try {
            if (loader.load()) {
                metadata.addProperties(loader.getConfiguration());
                return true;
            }
        } catch (IllegalStateException e) {
            //todo log
        }

        return false;
    }

    /**
     * The {@code metadata} is validated.
     */
    private void checker() {
       //todo Establish integrity each value of metadata for particular key.
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

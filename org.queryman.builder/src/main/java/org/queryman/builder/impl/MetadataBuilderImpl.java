/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.impl;

import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.cfg.Settings;
import org.queryman.loader.Loader;
import org.queryman.loader.impl.LoaderImpl;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class MetadataBuilderImpl implements MetadataBuilder {
    private final Loader loader = new LoaderImpl();
    private Metadata metadata;

    public MetadataBuilderImpl() {
        loader.setPropertiesConfigurationName("queryman-builder.properties")
           .setXmlConfigurationName("queryman-builder.xml");
    }

    @Override
    public MetadataBuilder setXmlCfg(String cfgFile) {
        loader.setXmlConfigurationName(cfgFile);

        return this;
    }

    @Override
    public MetadataBuilder setPropertiesCfg(String cfgFile) {
        loader.setPropertiesConfigurationName(cfgFile);
        return this;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void build() throws IOException, ClassNotFoundException {
        loader.load();
        Properties properties = loader.getConfiguration();
        metadata = new MetadataImpl();

        for (String setting : Settings.settings) {
            if (properties.containsKey(setting)) {
                metadata.addProperty(setting, properties.getProperty(setting));
            }
        }
    }

    @Override
    public void build(Metadata metadata) throws IOException, ClassNotFoundException {
        build();
        merge(this.metadata, metadata);
    }

    /**
     * The both metadata objects are merged. The metadata provides by
     * {@link Loader} takes precedence. If any key/value pair of {@code fromUser}
     * is missed by the {@code fromCfg}, it is copied.
     *
     * @param fromCfg {@link Metadata} object represents configuration loaded
     *                                by {@link Loader}
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

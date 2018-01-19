/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.io.IOException;

/**
 * MetadataBuilder populate {@link Metadata} from either xml or <b>properties</b> file.
 * Actually the xml config is loaded first, if it does, the {@link Metadata}
 * is populated by it. Otherwise it tries to populate {@link Metadata} from
 * <b>properties</b>.
 *
 * @author Timur Shaidullin
 */
public interface MetadataBuilder {
    /**
     * Set xml configuration file. The previous one is replaced.
     */
    MetadataBuilder setXmlCfg(String cfgFile);

    /**
     * Set xml properties file. The previous one is replaced.
     */
    MetadataBuilder setPropertiesCfg(String cfgFile);

    /**
     * Return populated {@link Metadata}.
     */
    Metadata getMetadata();

    /**
     * The configuration loads, then the {@link Metadata} is populated by it.
     */
    void build() throws IOException, ClassNotFoundException;

    /**
     * The configuration loads, then the {@link Metadata} is populated by it.
     * After that the loaded metadata and {@code metadata} are merged.
     * The loaded metadata take precedence.
     */
    void build(Metadata metadata) throws IOException, ClassNotFoundException;
}

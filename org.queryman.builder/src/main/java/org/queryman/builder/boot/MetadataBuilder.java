/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

/**
 * MetadataBuilder populate {@link Metadata} from either <b>xml</b> or <b>properties</b> file.
 *
 * Actually metadata builder tries to populate a {@link Metadata} from <b>xml</b>.
 * If the attempt is failed, {@code builder} will try to populate it from
 * <b>properties</b>. If the second attempt is failed too, the default metadata
 * will be used.
 *
 * @author Timur Shaidullin
 */
public interface MetadataBuilder {
    /**
     * Set xml configuration file. The previous one is replaced.
     *
     * @param cfgFile xml file name
     * @return itself
     */
    MetadataBuilder setXmlCfg(String cfgFile);

    /**
     * Set properties file. The previous one is replaced.
     *
     * @param cfgFile properties file name
     * @return itself
     */
    MetadataBuilder setPropertiesCfg(String cfgFile);

    /**
     * Return populated metadata instance.
     *
     * @return populated {@link Metadata}.
     */
    Metadata getMetadata();

    /**
     * Assembles a default {@link Metadata}.
     */
    void buildFromDefault();

    /**
     * The configuration loads, then builder populates the {@link Metadata}.
     */
    void build();

    /**
     * The configuration loads, then the {@link Metadata} is populated by it.
     * After that the loaded metadata and {@code metadata} are merged.
     * The loaded metadata take precedence.
     *
     * @param metadata user provided metadata
     */
    void build(Metadata metadata);
}

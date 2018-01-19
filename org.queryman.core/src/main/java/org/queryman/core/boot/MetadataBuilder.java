/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.boot;

/**
 * The responsibility of this class is populate metadata from configuration
 * file. Actually there are two choices to provide configuration file:
 * <ol>
 *     <li>queryman-configuration.xml</li>
 *     <li>queryman.properties</li>
 * </ol>
 *
 * <p>One of them must locate at the <b>resource</b> path.
 * The {@link org.queryman.core.exception.ConfigurationFileNotFound} will raise if
 * the file not found.</p>
 *
 * <p>It's possible to add properties that are missed in the configuration file</p>
 *
 * @author Timur Shaidullin
 */
public interface MetadataBuilder {
    /**
     * Populate {@link Metadata} from configuration file.
     */
    MetadataBuilder build();

    /**
     * Populate {@link Metadata} from configuration file. Properties lacked
     * in the configuration file may be populated from {@code metadata}.
     *
     * The properties from configuration file take precedence over properties
     * provide by {@code metadata}
     */
    MetadataBuilder build(Metadata metadata);

    /**
     *  Return the built {@link Metadata}.
     */
    Metadata getMetadata();

    /**
     * It allows to change xml configuration file name either on a full qualified
     * name such or a path to it within <code>resource</code>
     */
    MetadataBuilder setXmlConfigurationName(String name);

    /**
     * It allows to change properties configuration file name either on a full qualified
     * name such or a path to it within <code>resource</code>
     */
    MetadataBuilder setPropertiesConfigurationName(String name);
}

/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import java.util.Properties;

/**
 * This {@code class} manages a configuration loaders. When the loaded configurations
 * populate the {@link Metadata}. The location of configuration
 * file is <b>resource</b> path. Actually there are two choices to provide
 * configuration file:
 * <ol>
 *     <li>via xml file</li>
 *     <li>via properties file</li>
 * </ol>
 *
 * If no configuration file found, the default settings will apply.
 *
 * @author Timur Shaidullin
 */
public interface ServiceLoader {
    /**
     * The attempt loading a configuration xml file. If it fails, next attempt
     * loading a configuration properties file occurs.
     */
    boolean load();

    /**
     *  @return the built {@link Properties}.
     */
    Properties getConfiguration();
}

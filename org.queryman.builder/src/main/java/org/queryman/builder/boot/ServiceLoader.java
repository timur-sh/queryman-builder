/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.Metadata;

import java.util.Properties;

/**
 * This {@code class} loads configuration from source. Then the loaded configurations
 * populate the {@link Metadata}. The location of configuration
 * file is <b>resource</b> path. Actually there are two choices to provide
 * configuration file:
 * <ol>
 *     <li>via xml file</li>
 *     <li>via properties file</li>
 * </ol>
 *
 * By default {@code ConfigLoader} search for xml file. If file is not found,
 * the {@code ConfigLoader} search for properties file. If it is not found also,
 * the {@link java.io.FileNotFoundException} exception is raised.
 *
 * @author Timur Shaidullin
 */
public interface ServiceLoader {
    /**
     * The attempt loading a configuration xml file. It if fell, next attempt
     * loading a configuration properties file occurs.
     */
    boolean load();

    /**
     *  Return the built {@link Properties}.
     */
    Metadata getConfiguration();
}

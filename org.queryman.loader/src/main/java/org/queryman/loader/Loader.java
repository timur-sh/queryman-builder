/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.loader;

import java.io.IOException;
import java.util.Properties;

/**
 * The responsibility of this class is populate {@code Properties} from configuration
 * file in the <b>resource</b> path. Actually there are two choices to provide
 * configuration file:
 * <ol>
 *     <li>via properties file</li>
 *     <li>via xml file</li>
 * </ol>
 *
 * By default {@code ConfigLoader} search for xml file. If file is not find,
 * the {@code ConfigLoader} search for properties file. If it is not find too,
 * the {@link java.io.FileNotFoundException} exception is raised.
 *
 * It is possible to set location of configuration file other than
 * <b>resource</b> path.
 *
 * @author Timur Shaidullin
 */
public interface Loader {
    /**
     * Populate {@link Properties} from configuration file.
     */
    Loader load() throws ClassNotFoundException, IOException;

    /**
     *  Return the built {@link Properties}.
     */
    Properties getConfiguration();

    /**
     * Set either a full qualified xml configuration file name
     * or a relative path to it within <code>resource</code>
     */
    Loader setXmlConfigurationName(String name);

    /**
     * Set either a full qualified properties configuration file name
     * or a relative path to it within <code>resource</code>
     */
    Loader setPropertiesConfigurationName(String name);
}

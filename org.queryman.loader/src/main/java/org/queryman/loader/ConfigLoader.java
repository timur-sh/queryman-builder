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
 * This {@code ConfigLoader} class load persistence from any source.
 * By default it implies that the configuration file locate in
 * <b>resource</b> path
 *
 * @author Timur Shaidullin
 */
public interface ConfigLoader {
    boolean load() throws IOException, ClassNotFoundException;

    Properties getConfiguration();
}

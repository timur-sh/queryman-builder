/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.Metadata;

import java.io.IOException;

/**
 * This {@code class} loads source.
 * By default it implies that the configuration file locations is
 * <b>resource</b> path
 *
 * @author Timur Shaidullin
 */
public interface ConfigLoader {
    boolean load() throws IOException, ClassNotFoundException;

    Metadata getConfiguration();
}

/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import java.util.Properties;

/**
 * This {@code class} represents all of settings for current module.
 *
 * @author Timur Shaidullin
 */
public interface Metadata {
    /**
     * Add the property value by key. The previous value stored is replaced.
     *
     * Note. The allowable keys stores in {@link org.queryman.builder.cfg.Settings}
     */
    Metadata addProperty(String key, String value);

    /**
     * This checks a property existing
     */
    boolean contains(String key);

    /**
     * Return true if value of key is null or empty string.
     */
    boolean isEmpty(String key);

    /**
     * Get property by key. If key does not exist, {@code null} is returned.
     *
     * Note. The allowable keys stores in {@link org.queryman.builder.cfg.Settings}
     */
    String getProperty(String key);

    /**
     * Get all of properties.
     */
    Properties getProperties();

    /**
     * Add {@code properties} object.
     */
    Metadata addProperties(Properties properties);
}

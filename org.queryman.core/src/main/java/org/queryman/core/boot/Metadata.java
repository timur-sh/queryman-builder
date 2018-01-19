/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.boot;

import org.queryman.core.boot.cfg.Database;

import java.util.Properties;

/**
 * Any Queryman metadata is contained by this class.
 *
 * @author Timur Shaidullin
 */
public interface Metadata {
    /**
     *  Add JDBC url. Previous one is replaced by new {@code url}.
     */
    Metadata setUrl(String url);

    /**
     * Add Database user. Previous one is replaced by new {@code user}.
     */
    Metadata setUser(String user);

    /**
     * Add password of user of database. Previous one is replaced by new {@code password}.
     */
    Metadata setPassword(String password);

    /**
     * Return database configurations type.
     */
    Database getDatabase();

    /**
     * Add the property by name with the value. If the previous property's
     * value is specified, it is replaced by new {@code value}.
     */
    Metadata addProperty(String name, String value);

    /**
     * Add the {@code properties}. The previous properties is
     * replaced by new {@code properties}.
     */
    Metadata addProperties(Properties properties);
}

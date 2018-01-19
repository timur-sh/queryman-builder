/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.exception;

import java.io.FileNotFoundException;

/**
 * This exception must be thrown if configuration file not found
 *
 * @author Timur Shaidullin
 */
public class ConfigurationFileNotFound extends FileNotFoundException {
    public ConfigurationFileNotFound(String message) {
        super(message);
    }
}

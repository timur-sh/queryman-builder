/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

/**
 * @author Timur Shaidullin
 */
public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }
}

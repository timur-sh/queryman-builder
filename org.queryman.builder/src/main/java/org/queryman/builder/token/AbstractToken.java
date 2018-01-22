/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * @author Timur Shaidullin
 */
public abstract class AbstractToken implements Token {
    protected final String name;

    public AbstractToken(String name) {
        this.name = name;
    }
}

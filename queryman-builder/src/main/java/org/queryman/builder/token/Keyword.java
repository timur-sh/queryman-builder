/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * PostgreSQL keyword. Examples: SELECT, UPDATE, DELETE,
 * INSERT, WITH, FROM, DISTINCT etc.
 *
 * @author Timur Shaidullin
 */
public class Keyword extends AbstractToken {
    public Keyword(String name) {
        super(name);
    }
}

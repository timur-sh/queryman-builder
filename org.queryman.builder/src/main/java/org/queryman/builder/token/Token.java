/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * PostgreSQL token is sequence of key words, identifiers, constants.
 * Any of above tokens must be based on this interface.
 *
 * @author Timur Shaidullin
 */
public interface Token {
    String getName();
}

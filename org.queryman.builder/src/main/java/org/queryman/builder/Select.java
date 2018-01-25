/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

/**
 *
 * This is marker interface represents {@code SELECT} clause of SQL. All of
 * other interfaces of select must be based on this interface.
 *
 * @author Timur Shaidullin
 */
public interface Select extends Query {
}

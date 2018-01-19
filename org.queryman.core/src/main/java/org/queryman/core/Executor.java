/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core;

/**
 * It sends a request to fetch data or manipulate it to database.
 *
 * @author Timur Shaidullin
 */
public interface Executor {
    <T> T fetch(T t);
}

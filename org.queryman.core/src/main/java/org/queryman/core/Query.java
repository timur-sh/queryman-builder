/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core;

import org.queryman.core.Executor;

/**
 * The main interface aim of it is interaction with {@link Executor}.
 *
 * @author Timur Shaidullin
 */
public interface Query {
    String getSql();

    String toString();
}

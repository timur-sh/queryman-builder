/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.from;

import org.queryman.builder.token.Expression;

/**
 * TABLESAMPLE indicates a method and optional length of argument is used
 * to retrieve subset rows of table.
 *
 * The sample precedes the any filter of query such is WHERE.
 *
 * PostgreSQL provides two sampling methods:
 * <ul>
 *     <li>BERNOULLI</li>
 *     <li>SYSTEM</li>
 * </ul>
 *
 * @author Timur Shaidullin
 */
public interface FromTablesampleStep extends FromFinalStep {
    /**
     * Set sampling method.
     *
     * @see #tablesample(String, Expression...)
     *
     * @param method is a method name.
     * @param arguments are arguments.
     */
    FromRepeatableStep tablesample(String method, String... arguments);

    /**
     * Set sampling method.
     *
     * @param method is a method name.
     * @param arguments are arguments.
     */
    FromRepeatableStep tablesample(String method, Expression... arguments);
}

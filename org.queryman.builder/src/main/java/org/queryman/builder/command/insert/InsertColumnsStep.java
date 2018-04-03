/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.token.Expression;

/**
 * INSERT INTO .. (column_name [, ...]) step.
 *
 * @author Timur Shaidullin
 */
public interface InsertColumnsStep extends InsertDefaultValuesStep {
    /**
     * @param columns set of columns
     * @return insert overriding value step
     *
     * @see #columns(Expression...)
     */
    InsertColumnsManyStep columns(String... columns);

    /**
     * @param columns set of columns
     * @return insert overriding value step
     */
    InsertColumnsManyStep columns(Expression... columns);
}

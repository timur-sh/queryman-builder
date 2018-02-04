/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.token.Expression;

/**
 * {@code FROM} clause is a part of {@code SELECT} statement.
 *
 * Usual rows are derived from table, but it is possible to retrieve
 * rows from subselect or other virtual table, created {@code VIEW}, etc..
 *
 * @author Timur Shaidullin
 */
public interface SelectFromStep extends Query {
    /**
     * Specify table name that be used to retrieve rows. If several names are
     * provided, they will be cross-joined together.
     */
    SelectFromManySteps from(String... tables);

    /**
     * Specify table name that be used to retrieve rows. If several names are
     * provided, they will be cross-joined together.
     */
    SelectFromManySteps from(Expression... tables);
}

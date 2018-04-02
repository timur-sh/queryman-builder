/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.Query;
import org.queryman.builder.token.Expression;

/**
 * WITH .. (columns, column) AS .. step.
 *
 * @author Timur Shaidullin
 */
public interface WithAsStep {
    /**
     * Specifies sub-queries
     *
     * @param query CTE's query
     * @return select first step
     */
    WithAsManySteps as(Query query);

    /**
     * Specifies sub-queries
     *
     * @param query CTE's query
     * @return select first step
     */
    WithAsManySteps as(Expression query);
}

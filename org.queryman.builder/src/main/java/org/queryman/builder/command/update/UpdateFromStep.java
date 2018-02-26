/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

import org.queryman.builder.token.Expression;

/**
 * UPDATE .. FROM .. step.
 *
 * @author Timur Shaidullin
 */
public interface UpdateFromStep extends UpdateWhereFirstStep {
    /**
     * Set list of tables that are used in WHERE or SET clauses.
     *
     * @param tables list of tables
     * @return update where step
     *
     * @see #from(Expression...)
     */
    UpdateWhereFirstStep from(String... tables);

    /**
     * Set list of tables that are used in WHERE or SET clauses.
     *
     * @param tables list of tables
     * @return update where step
     */
    UpdateWhereFirstStep from(Expression... tables);
}

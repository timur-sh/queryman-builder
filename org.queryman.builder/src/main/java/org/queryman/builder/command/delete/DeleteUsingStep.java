/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.delete;

import org.queryman.builder.token.Expression;

/**
 * DELETE .. USING .. step.
 *
 * @author Timur Shaidullin
 */
public interface DeleteUsingStep extends DeleteWhereFirstStep {
    /**
     * Set the list of table that will be appeared in WHERE clause.
     *
     * @param tables list of tables or table expressions
     * @return deleteFrom where step
     */
    DeleteWhereFirstStep using(Expression... tables);

    /**
     * Set the list of table that will be appeared in WHERE clause.
     *
     * @param tables list of tables or table expressions
     * @return deleteFrom where step
     *
     * @see #using(Expression...)
     */
    DeleteWhereFirstStep using(String... tables);
}

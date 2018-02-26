/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

import org.queryman.builder.Query;
import org.queryman.builder.token.Expression;

/**
 * UPDATE .. SET .. step.
 *
 * @author Timur Shaidullin
 */
public interface UpdateSetStep extends UpdateFromStep {
    /**
     * Set a column name and an expression that is be assigned to the column.
     *
     * @param column column name
     * @param value expression value
     * @return itself
     */
    UpdateSetStep set(String column, String value);

    /**
     * Set a columns names and an expression list that are be assigned to the columns.
     *
     * @param listColumns columns names
     * @param listValues expressions list
     * @return itself
     */
    UpdateSetStep set(Expression listColumns, Expression listValues);

    /**
     * Set a columns names and a sub-select that is be assigned to the columns.
     *
     * @param listColumns columns names
     * @param subSelect sub-select
     * @return itself
     */
    UpdateSetStep set(Expression listColumns, Query subSelect);
}
